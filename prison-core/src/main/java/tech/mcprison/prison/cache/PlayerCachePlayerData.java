package tech.mcprison.prison.cache;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.autofeatures.PlayerMessaging;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;

/**
 * <p>This class represents the temporal state of a player's balance
 * represented by both the internal (within the database) all the way 
 * through to the uncommitted value.  There are three primary values
 * and they server an important part to maintaining a fast and 
 * reliable cache.
 * </p>
 * 
 * 
 * @author RoyalBlueRanger
 *
 */
public class PlayerCachePlayerData {
	
	public static final long SESSION_TIMEOUT_MINING_MS = 1000 * 15; // 15 seconds

	private transient Player player;
	
	
	private String playerUuid;
	private String playerName;
	
	private transient File playerFile = null;
	
	// NOTE: Since we are logging time online all players that have been online are
	//       always considered dirty since their online time would need to be updated.
//	private transient boolean dirty = false;
	
	/**
	 *  This Object lock is used to synchronized the public side of this class
	 *  and the protected side of this class which is the database transaction
	 *  side of things.
	 */
	@SuppressWarnings( "unused" )
	private transient final Object lock = new Object();
	
	private transient PlayerCacheRunnable task = null;
	
	
	// lastSeenDate tries to track when the player was last on the server.
	// This is important to know for refreshing player stats.
	private long lastSeenDate;
	private double lastSeenBalance;
	
	private long onlineTimeTotal = 0L;

	// "active" time is not needed. It's the total onlineTimeTotal minus
	// the other types. Tracking active could result in discrepancies and 
	// would be redundant.
//	private long onlineActiveTimeTotal = 0L;
	private long onlineAFKTimeTotal = 0L;
	private long onlineMiningTimeTotal = 0L;
	
	
	
	private long blocksTotal = 0L;
	
	private TreeMap<String, Integer> blocksByMine;
	private TreeMap<String, Integer> blocksByType;
	
	private TreeMap<String, Long> timeByMine;
	private String lastMine = null;


	private TreeMap<String, Double> earningsByMine;
	
	private transient TreeMap<String, Double> earningsPerMinute;
	
	
	private long tokens;
	private long tokensTotal;
	private long tokensTotalRemoved;
	private long tokensLastBlocksTotals;
	
	private TreeMap<String, Long> tokensByMine;
	private transient TreeMap<String, Long> tokensPerMinute;
	
	
	 
	
	
	// This is the time when the "session" was started:
	private transient SessionType sessionType;
	
	
//	private transient long sessionTimingStart = 0;
	private transient long sessionTimingLastCheck = 0;
	
	// sessionLastLocation is used for afk calculations:
//	private transient Location sessionLastLocation = null;
	
	
	private transient PlayerMessaging playerMessaging;
	
	
	private List<Inventory> backpacks;
	
	private transient boolean dirty = false;
	
	
	public enum SessionType {
		active,
		mining,
		afk;
	}
	
	public PlayerCachePlayerData() {
		super();
		
		this.blocksByMine = new TreeMap<>();
		this.blocksByType = new TreeMap<>();

		this.timeByMine = new TreeMap<>();
		this.lastMine = null;
		
		this.earningsByMine = new TreeMap<>();

		this.earningsPerMinute = new TreeMap<>();
		
		
		this.tokensByMine = new TreeMap<>();
		this.tokensPerMinute = new TreeMap<>();

		
		this.sessionType = SessionType.active;
		
//		this.sessionTimingStart = System.currentTimeMillis();
		this.sessionTimingLastCheck = System.currentTimeMillis();
		
//		this.sessionLastLocation = null;
		
		this.playerMessaging = new PlayerMessaging();
		
		this.backpacks = new ArrayList<>();
		
	}
	
	public PlayerCachePlayerData( Player player, File playerFile ) {
		this();
		
		this.player = player;
		
		if ( isOnline() ) {
			
			this.lastSeenDate = System.currentTimeMillis();
//			this.lastSeenBalance = player.get
			
			this.dirty = true;
		}
		
		this.playerUuid = player.getUUID().toString();
		this.playerName = player.getName();
		
		this.playerFile = playerFile;
		
//		this.sessionLastLocation = player.getLocation();
		
	}

	public boolean isOnline() {
		return getPlayer() != null && getPlayer().isOnline();
	}
	
	public void checkTimers() {

		if ( isOnline() ) {
			
			// Do not change the session type, but pass null for the mine to indicate 
			// that this is a checkTimers...
			checkTimersMining( sessionType, null );
			
		}
	}
	
	/**
	 * <p>If the SessionType is mining, then that means that the last time 
	 * a checkTimer was called, it was for mining.  Mining can only be active for
	 * only a short duration after breaking a block.  So once a block is broken, 
	 * another block must be broken in a specified amount of time to be considered
	 * actively mining.
	 * </p>
	 * 
	 * <p>This function can only be called from a block break event.
	 * </p>
	 * 
	 * <p>If the prior SessionType was not mining, then that session segment may be
	 * either afk or active.  Pass to afk check.  Set current session type to mining.
	 * </p>
	 * 
	 * <p>If prior session type was mining, then do nothing if the last 
	 * @param mine 
	 */
	private void checkTimersMining( SessionType currentSessionType, String mine ) {

		if ( !isOnline() ) {
			return;
		}

		final long currentTime = System.currentTimeMillis();
		final long duration = currentTime - sessionTimingLastCheck;

		
		
		// temp fix:
		if ( onlineTimeTotal < 0 ) {
			onlineTimeTotal = 0;
		}
		if ( onlineMiningTimeTotal < 0 ) {
			onlineMiningTimeTotal = 0;
		}
		

		if ( currentSessionType != SessionType.mining ) {
			
			//checkTimersAfk();
			
			setLastMine( null );
			sessionType = currentSessionType;
			
			sessionTimingLastCheck = currentTime;
			
			dirty = true;
		}
		
		else if ( currentSessionType == SessionType.mining ) {
			// The current session type is still mining... and was before. 
			// Must check sessionTimingLastCheck to see if we went over the 
			// max mining idle time:
			
			
			if ( sessionType != SessionType.mining || 
					getLastMine() == null ) {
				
				dirty = true;
				
			}
			
			else if ( mine == null && getLastMine() != null && 
					duration > SESSION_TIMEOUT_MINING_MS ) {
				
				addTimeToMine( getLastMine(), SESSION_TIMEOUT_MINING_MS );
				
				onlineMiningTimeTotal += SESSION_TIMEOUT_MINING_MS;
				
				// Since this is being called from checkTimer(), need to 
				// set lastMine to null and sessionType to active:
				
				setLastMine( null );
				sessionType = SessionType.active;
				
				// Save as total online time.
				onlineTimeTotal += duration;
				
				dirty = true;
				return;
			}
			
			else if ( mine == null ) {
				
				// This is running in checkTimers() and not enough time has passed to
				// exceed the SESSION_TIMEOUT_MINING_MS.  So need to wait longer.
				// return without setting anything.
				
				return;
			}
			
			// Mine has changed since last check, so apply duration to last mine:
			else if ( getLastMine() != null && 
					(mine == null ||
					!mine.equalsIgnoreCase( getLastMine() ) )) {
				
				
				if ( duration > SESSION_TIMEOUT_MINING_MS ) {
					addTimeToMine( getLastMine(), SESSION_TIMEOUT_MINING_MS );
					
					onlineMiningTimeTotal += SESSION_TIMEOUT_MINING_MS;
					
				}
				else {
					
					addTimeToMine( getLastMine(), duration );
					
					onlineMiningTimeTotal += duration;
				}

			}
			
			
			else if ( duration > SESSION_TIMEOUT_MINING_MS ) {
				
				// Same mine, but exceeded the duration:
				
				addTimeToMine( mine, SESSION_TIMEOUT_MINING_MS );
				
				onlineMiningTimeTotal += SESSION_TIMEOUT_MINING_MS;

			}
			else {
				// same mine and less than session length, so add duration:

				addTimeToMine( mine, duration );
				
				onlineMiningTimeTotal += duration;

			}
			
			
			// Now change the active mine
			setLastMine( mine );

			// Should already be mining:
//			sessionType = currentSessionType;
			
			// Set new session to this boundary and discard the extra time:
			sessionTimingLastCheck = currentTime;
			
			dirty = true;
		}
		
		// Always Save as total online time.
		onlineTimeTotal += duration;

	}



	/**
	 * <p>Also generates tokens based upon blocks mined.
	 * </p>
	 * 
	 * @param mine
	 * @param blockName
	 * @param quantity
	 */
	public void addBlock( String mine, String blockName, int quantity )
	{
		if ( quantity > 0 && blockName != null && 
				!PrisonBlock.AIR.getBlockName().equalsIgnoreCase( blockName )
				) {
			this.blocksTotal += quantity;
			
			if ( blockName != null ) {
				addBlockByType( blockName, quantity );
			}
			
			if ( mine != null ) {
				addBlockByMine( mine, quantity );
			}
			
			checkTimersMining( SessionType.mining, mine );
			dirty = true;
		}
			
		addTokensByBlocks( mine, quantity );
	}
	
	private void addBlockByType( String blockName, int quantity ) {
		int qty = quantity;
		
		if ( getBlocksByType().containsKey( blockName ) ) {
			qty += getBlocksByType().get( blockName );
		}
		
		getBlocksByType().put( blockName, qty );
	}
	private void addBlockByMine( String mine, int quantity ) {
		int qty = quantity;
		
		if ( getBlocksByMine().containsKey( mine ) ) {
			qty += getBlocksByMine().get( mine );
			
		}
		
		getBlocksByMine().put( mine, qty );
	}
	
	private void addEarningsByMine( String mine, double amount ) {
		double amt = amount;
		
		if ( getEarningsByMine().containsKey( mine ) ) {
			amt += getEarningsByMine().get( mine );
			
		}
		
		getEarningsByMine().put( mine, amt );
	}
	
	private void addTokensByMine( String mine, long tokens ) {
		long toks = 0;
		
		if ( getTokensByMine().containsKey( mine ) ) {
			toks = getTokensByMine().get( mine );
		}
		
		getTokensByMine().put( mine, (toks + tokens) );
	}
	
	private void addTimeToMine( String mine, long miningDuration )
	{
		if ( mine != null && !mine.trim().isEmpty() ) {
			
			long duration = miningDuration;
			
			if ( getTimeByMine().containsKey( mine ) ) {
				duration += getTimeByMine().get( mine );
			}
			
			setLastMine( mine );
			getTimeByMine().put( mine, duration );
		}
	}
	
	/**
	 * This stores the earnings from the player so they can
	 * be averaged to find their earnings per minute.
	 * 
	 * @param earnings
	 */
	public void addEarnings( double earnings, String mineName ) {
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd_hh:mm");
		String key = dateFmt.format( new Date() );
		
		
		double earningsPM = earnings;
		if ( getEarningsPerMinute().containsKey( key )  ) {
			earningsPM += getEarningsPerMinute().get( key );
		}
		getEarningsPerMinute().put( key, earningsPM );
		
		
		if ( getEarningsPerMinute().size() > 5 ) {
			getEarningsPerMinute().remove( 
					getEarningsPerMinute().firstEntry().getKey() );
		}
		
		if ( mineName != null && sessionType != SessionType.mining ) {
			sessionType = SessionType.mining;
		}
		if ( mineName == null && getLastMine() != null ) {
			mineName = getLastMine();
		}
		
		// If earnings are within the session timeout for mining, then add the 
		// earnings to the moneyByMine:
		if ( sessionType == SessionType.mining && mineName != null ) {
			long duration = System.currentTimeMillis() - sessionTimingLastCheck;
			if ( duration < SESSION_TIMEOUT_MINING_MS ) {
				
				addEarningsByMine( mineName, earnings );
			}
		}
		
	}
	
	/**
	 * This returns the average amount earned per minute for the
	 * last 5 minutes.
	 * 
	 * @return
	 */
	public double getAverageEarningsPerMinute() {
		double results = 0;
		
		int size = 0;
		for ( double value : earningsPerMinute.values() ) {
			results += value;
			size++;
		}
		
		return ( size == 0 ? 0 : ( results / size ));
	}
	
	private void addTokensByBlocks( String mineName, int blocks ) {
		
		if ( AutoFeaturesWrapper.getInstance().isBoolean( AutoFeatures.tokensEnabled ) ) {
			int blocksPerToken = AutoFeaturesWrapper.getInstance().getInteger( AutoFeatures.tokensBlocksPerToken );
		
			if ( blocksPerToken > 0 ) {
				
				// If blocksTotal > 10k and tokensLastBlocksTotals == 0, then this means
				// the player was mining before tokens was enabled, so set the 
				// tokensLastBlocksTotals to the blocks total, minus current block count.
				if ( tokensLastBlocksTotals == 0 && blocksTotal > 10000 ) {
					tokensLastBlocksTotals = blocksTotal - blocks;
				}
				
				// tokensLastBlocksTotals should never be greater than blocksTotal:
				if ( tokensLastBlocksTotals > blocksTotal ) {
					tokensLastBlocksTotals = blocksTotal;
				}
				
				double delta = blocksTotal - tokensLastBlocksTotals;
				
				double tokens = delta / (double) blocksPerToken;
				
				if ( tokens >= 1.0 ) {
					long tokensLong = (long) Math.floor( tokens );
					
					long blocksForTokens = tokensLong * blocksPerToken;
					tokensLastBlocksTotals += blocksForTokens;
					
					addTokens( tokensLong, mineName );
					
				}
			}
		}
	}
	
	/**
	 * <p>This stores the tokens from the player so they can
	 * be averaged to find their tokens per minute. Tokens are 
	 * automatically generated based upon blocks mined so normally 
	 * you wouldn't have to call this function.
	 * </p>
	 * 
	 * <p>If you just want to add tokens, you can pass a null for the 
	 * mine name.
	 * </p>
	 * 
	 * <p>Note: Unlike adding blocks from mines, adding tokens with a mine name
	 * does not need to be concerned about session types.  If a mine name is
	 * provided, then that's the mine it should be attributed to.
	 * </p>
	 * 
	 * @param newTokens
	 */
	public void addTokens( long newTokens, String mineName ) {
		
		addTokensAdmin( newTokens );
		
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd_hh:mm");
		String key = dateFmt.format( new Date() );

		
		long tokensPM = newTokens;
		if ( getTokensPerMinute().containsKey( key )  ) {
			tokensPM += getTokensPerMinute().get( key );
		}
		getTokensPerMinute().put( key, tokensPM );
		
		
		if ( getTokensPerMinute().size() > 5 ) {
			getTokensPerMinute().remove( 
					getTokensPerMinute().firstEntry().getKey() );
		}
		
		// If we are getting tokens from mining, then we have the mine name. 
		// No need to mess with sessions.
//		if ( mineName != null && sessionType != SessionType.mining ) {
//			sessionType = SessionType.mining;
//		}
//		if ( mineName == null && getLastMine() != null ) {
//			mineName = getLastMine();
//		}
		
		// If earnings are within the session timeout for mining, then add the 
		// earnings to the tokensByMine:
		if ( mineName != null ) {

			addTokensByMine( mineName, newTokens );
		}
		
		dirty = true;
	}
	
	
	/**
	 * <p>This adds tokens to the player, but it is from an admin-related purpose, or task,
	 * so it has no effects on the player's Tokens-Per-Minute calculations, or 
	 * per mine stats.
	 * </p>
	 * 
	 * @param newTokens
	 */
	public void addTokensAdmin( long newTokens ) {
		
		this.tokens += newTokens;
		this.tokensTotal += newTokens;
		
		dirty = true;
	}
	
	/**
	 * <p>This removes tokens from the player, but it is from an admin-related purpose, or task, 
	 * so it has no effects on the player's Tokens-Per-Minute calculations, or 
	 * per mine stats.
	 * <p>
	 * 
	 * <p>Note: The player can have a negative balance with this function! This may be useful for
	 * small "token loans" when purchasing something.  Such allowances would have to be 
	 * handled in the task that calls this function.
	 * </p>
	 * 
	 * @param newTokens
	 */
	public void removeTokensAdmin( long removeTokens ) {
		
		this.tokens -= removeTokens;
		this.tokensTotalRemoved += removeTokens;
		
		dirty = true;
	}
	
	public void setTokensAdmin( long newBalance ) {
		
		long delta = newBalance - this.tokens;
		
		if ( delta > 0 ) {
			
			this.tokens += delta;
			this.tokensTotal += delta;
		}
		else {
			
			this.tokens -= delta;
			this.tokensTotalRemoved -= delta;
		}
		
		dirty = true;
	}
	
	/**
	 * This returns the average tokens earned per minute for the
	 * last 5 minutes.
	 * 
	 * @return
	 */
	public double getAverageTokensPerMinute() {
		double results = 0;
		
		int size = 0;
		for ( double value : tokensPerMinute.values() ) {
			results += value;
			size++;
		}
		
		return ( size == 0 ? 0 : ( results / size ));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		String totalTime = PlaceholdersUtil.formattedTime( onlineTimeTotal / 1000d );
		String miningTime = PlaceholdersUtil.formattedTime( onlineMiningTimeTotal / 1000d );
		
		sb.append( getPlayerName() )
			.append( " " )
			.append( isOnline() ? "online" : "OFFLINE" )
			.append( "  avg earnings/min: " )
			.append( getAverageEarningsPerMinute() )
		
			.append( "  TotalOnlineTime: " )
			.append( totalTime )
			.append( "  MiningTime: " )
			.append( miningTime )
			
			.append( "  totalBlocks: " )
			.append( blocksTotal )
			.append( "  mines: " )
			.append( blocksByMine )
			.append( "  blocks: " )
			.append( blocksByType )

			.append( "  avg tokens/min: " )
			.append( getAverageTokensPerMinute() )
			.append( "  totalTokens: " )
			.append( getTokensTotal() )
			.append( "  totalTokensRemoved: " )
			.append( getTokensTotalRemoved() )
			.append( "  tokens: " )
			.append( getTokens() )
			;
		
		return sb.toString();
	}

	public void updateLastSeen() {
			
		if ( getPlayer() != null && getPlayer().isOnline() ) {
			lastSeenDate = System.currentTimeMillis();
		}
	}

	protected Player getPlayer() {
		return player;
	}
	protected void setPlayer( Player player ) {
		this.player = player;
	}

	public long getLastSeenDate() {
		return lastSeenDate;
	}
	public void setLastSeenDate( long lastSeenDate ) {
		this.lastSeenDate = lastSeenDate;
	}

	public File getPlayerFile() {
		return playerFile;
	}
	public void setPlayerFile( File playerFile ) {
		this.playerFile = playerFile;
	}
	
	public String getPlayerUuid() {
		return playerUuid;
	}
	public void setPlayerUuid( String playerUuid ) {
		this.playerUuid = playerUuid;
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName( String playerName ) {
		this.playerName = playerName;
	}

	public PlayerCacheRunnable getTask() {
		return task;
	}
	public void setTask( PlayerCacheRunnable task ) {
		this.task = task;
	}

	public TreeMap<String, Double> getEarningsPerMinute() {
		if ( earningsPerMinute == null ) {
			earningsPerMinute = new TreeMap<>();
		}
		return earningsPerMinute;
	}
	public void setEarningsPerMinute( TreeMap<String, Double> earningsPerMinute ) {
		this.earningsPerMinute = earningsPerMinute;
	}

	public long getOnlineTimeTotal() {
		return onlineTimeTotal;
	}
	public void setOnlineTimeTotal( long onlineTimeTotal ) {
		this.onlineTimeTotal = onlineTimeTotal;
	}

	public long getOnlineAFKTimeTotal() {
		return onlineAFKTimeTotal;
	}
	public void setOnlineAFKTimeTotal( long onlineAFKTimeTotal ) {
		this.onlineAFKTimeTotal = onlineAFKTimeTotal;
	}

	public long getOnlineMiningTimeTotal() {
		return onlineMiningTimeTotal;
	}
	public void setOnlineMiningTimeTotal( long onlineMiningTimeTotal ) {
		this.onlineMiningTimeTotal = onlineMiningTimeTotal;
	}

	public long getBlocksTotal() {
		return blocksTotal;
	}
	public void setBlocksTotal( long blocksTotal ) {
		this.blocksTotal = blocksTotal;
	}

	public TreeMap<String, Integer> getBlocksByMine() {
		if ( blocksByMine == null ) {
			blocksByMine = new TreeMap<>();
		}
		return blocksByMine;
	}
	public void setBlocksByMine( TreeMap<String, Integer> blocksByMine ) {
		this.blocksByMine = blocksByMine;
	}

	public TreeMap<String, Integer> getBlocksByType() {
		if ( blocksByType == null ) {
			blocksByType = new TreeMap<>();
		}
		return blocksByType;
	}
	public void setBlocksByType( TreeMap<String, Integer> blocksByType ) {
		this.blocksByType = blocksByType;
	}

	public TreeMap<String, Long> getTimeByMine() {
		return timeByMine;
	}
	public void setTimeByMine( TreeMap<String, Long> timeByMine ) {
		this.timeByMine = timeByMine;
	}

	public long getTokens() {
		return tokens;
	}
	public void setTokens( long tokens ) {
		this.tokens = tokens;
	}

	public long getTokensTotal() {
		return tokensTotal;
	}
	public void setTokensTotal( long tokensTotal ) {
		this.tokensTotal = tokensTotal;
	}

	public long getTokensTotalRemoved() {
		return tokensTotalRemoved;
	}
	public void setTokensTotalRemoved( long tokensTotalRemoved ) {
		this.tokensTotalRemoved = tokensTotalRemoved;
	}

	public long getTokensLastBlocksTotals() {
		return tokensLastBlocksTotals;
	}
	public void setTokensLastBlocksTotals( long tokensLastBlocksTotals ) {
		this.tokensLastBlocksTotals = tokensLastBlocksTotals;
	}

	public TreeMap<String, Long> getTokensByMine() {
		return tokensByMine;
	}
	public void setTokensByMine( TreeMap<String, Long> tokensByMine ) {
		this.tokensByMine = tokensByMine;
	}

	public TreeMap<String, Long> getTokensPerMinute() {
		return tokensPerMinute;
	}
	public void setTokensPerMinute( TreeMap<String, Long> tokensPerMinute ) {
		this.tokensPerMinute = tokensPerMinute;
	}

	public String getLastMine() {
		return lastMine;
	}
	public void setLastMine( String lastMine ) {
		this.lastMine = lastMine;
	}

	public TreeMap<String, Double> getEarningsByMine() {
		return earningsByMine;
	}
	public void setEarningsByMine( TreeMap<String, Double> earningsByMine ) {
		this.earningsByMine = earningsByMine;
	}

	public PlayerMessaging getPlayerMessaging() {
		return playerMessaging;
	}
	public void setPlayerMessaging( PlayerMessaging playerMessaging ) {
		this.playerMessaging = playerMessaging;
	}

	public List<Inventory> getBackpacks() {
		return backpacks;
	}
	public void setBackpacks( List<Inventory> backpacks ) {
		this.backpacks = backpacks;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty( boolean dirty ) {
		this.dirty = dirty;
	}

}

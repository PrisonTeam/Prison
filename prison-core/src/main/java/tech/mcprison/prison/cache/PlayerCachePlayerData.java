package tech.mcprison.prison.cache;

import java.io.File;
import java.util.TreeMap;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Location;

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
	
	
	private long onlineTimeTotal = 0L;
	
	
	private long onlineActiveTimeTotal = 0L;
	private long onlineAFKTimeTotal = 0L;
	private long onlineMiningTimeTotal = 0L;
	
	
	
	private long blocksTotal = 0L;
	
	private TreeMap<String, Integer> blocksByMine;
	private TreeMap<String, Integer> blocksByType;
	
	
	// This is the time when the "session" was started:
	private transient SessionType sessionType;
	
	
	private transient long sessionTimingStart = 0;
	private transient long sessionTimingLastCheck = 0;
	
	private transient Location sessionLastLocation = null;
	
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
		
		this.sessionType = SessionType.active;
		
		this.sessionTimingStart = System.currentTimeMillis();
		this.sessionTimingLastCheck = sessionTimingStart;
		
		this.sessionLastLocation = null;
		
	}
	
	public PlayerCachePlayerData( Player player, File playerFile ) {
		this();
		
		this.player = player;
		
		this.playerUuid = player.getUUID().toString();
		this.playerName = player.getName();
		
		this.playerFile = playerFile;
		
		this.sessionLastLocation = player.getLocation();
		
	}

	public void checkTimers() {

		// Do not change the session type, so pass it the current:
		checkTimersMining( sessionType );
		
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
	 */
	private void checkTimersMining( SessionType targetType ) {
		final long currentTime = System.currentTimeMillis();

		if ( sessionType == targetType && sessionType != SessionType.mining) {
			// No change in status
			
			sessionTimingLastCheck = currentTime;

			final long duration = currentTime - sessionTimingLastCheck;
			// if duration is greater than 15 minutes, then move the start point:
			if ( duration > 900000 ) {
				
				sessionTimingStart = currentTime;
				dirty = true;
			}
			
		}
		else if ( sessionType != SessionType.mining ) {
			
			// Always Save as total time. Use sessionTimingStart. Ignore sessionTimingLastCheck.
			final long duration = currentTime - sessionTimingStart;
			onlineActiveTimeTotal += duration;
			
			//checkTimersAfk();
			
			sessionType = targetType;
			sessionTimingStart = currentTime;
			sessionTimingLastCheck = currentTime;
			dirty = true;
		}
		else {
			// Its still mining... 
			// Must check sessionTimingLastCheck to see if we went over the 
			// max mining idle time:
			
			final long duration = currentTime - sessionTimingLastCheck;
			
			
			// If the duration is less than the session mining timeout, then player
			// is still mining.  Just set the sessionTimingLastCheck.
			if ( duration < SESSION_TIMEOUT_MINING_MS ) {
				 
				sessionTimingLastCheck = currentTime;
			}
			
			// Mining can only be active for no more than SESSION_TIMEOUT_MINING_MS 
			// after the last  block was broken.  So check duration between now and 
			// sessionOnlineTimeLastCheck and if more than permitted, then shutdown 
			// mining session and log it for a duration since session start to 
			// last check plus the mining timeout value.  Then set session start to 
			// that position.
			
			else if ( duration > SESSION_TIMEOUT_MINING_MS ) {
				
				// Calculate the end point of the mining session, which will be 30 seconds after 
				// the last time check:
				final long tempTime = sessionTimingLastCheck + SESSION_TIMEOUT_MINING_MS;
				final long miningDuration = sessionTimingStart - tempTime;
				onlineActiveTimeTotal += miningDuration;
				onlineMiningTimeTotal += miningDuration;
				
				// Set new session to this boundary:
				sessionTimingStart = tempTime;
				sessionTimingLastCheck = tempTime;
				
				// Since the last SessionType and current are mining, then the duration from
				// the new sessionTimingStart to currentTime needs to go to active:
				final long durationActive = currentTime - sessionTimingStart;
				onlineActiveTimeTotal += durationActive;
				
				//checkTimersAfk();
				
				
				// Now reset the current session:
				sessionType = targetType;
				sessionTimingStart = currentTime;
				sessionTimingLastCheck = currentTime;
				dirty = true;
				
			}
		}
		

	}

	public void addBlock( String mine, String blockName, int quantity )
	{
		if ( quantity > 0 && blockName != null && 
				!PrisonBlock.AIR.getBlockName().equalsIgnoreCase( blockName )
				) {
			this.blocksTotal++;
			
			if ( blockName != null ) {
				addBlockByType( blockName, quantity );
			}
			
			if ( mine != null ) {
				addBlockByMine( mine, quantity );
			}
			
			checkTimersMining( SessionType.mining );
			
		}
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
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getPlayerName() )
			.append( "  total: " )
			.append( blocksTotal )
			.append( "  mines: " )
			.append( blocksByMine )
			.append( "  blocks: " )
			.append( blocksByType );
		
		return sb.toString();
	}
	

	protected Player getPlayer() {
		return player;
	}
	protected void setPlayer( Player player ) {
		this.player = player;
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
		return blocksByMine;
	}
	public void setBlocksByMine( TreeMap<String, Integer> blocksByMine ) {
		this.blocksByMine = blocksByMine;
	}

	public TreeMap<String, Integer> getBlocksByType() {
		return blocksByType;
	}
	public void setBlocksByType( TreeMap<String, Integer> blocksByType ) {
		this.blocksByType = blocksByType;
	}

}

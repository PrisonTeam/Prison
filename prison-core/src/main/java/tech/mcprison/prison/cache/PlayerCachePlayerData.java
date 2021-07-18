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
	
	public static final long SESSION_TIMEOUT_MINING_MS = 1000 * 30; // 30 seconds

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
	
	private transient long sessionOnlineTimeStart = 0;
	private transient long sessionOnlineTimeLastCheck = 0;
	
	private transient Location sessionLastLocation = null;
	
	
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
		
		this.sessionOnlineTimeStart = System.currentTimeMillis();
		
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
		
		if ( sessionType == SessionType.mining ) {
			long currentTime = System.currentTimeMillis();
			
			// Mining can only be active for no more than 30 seconds after the last
			// block was broken.  So check duration between now and sessionOnlineTimeLastCheck
			// and if more than 30 seconds, then shutdown mining session and log it, then
			// reset sessionOnlineTimeLastCheck to the cutover time, 30 seconds later.
			
			long duration = currentTime - sessionOnlineTimeLastCheck;
			
			if ( duration > SESSION_TIMEOUT_MINING_MS ) {
				
				// Calculate the end point of the mining session, which will be 30 seconds after 
				// the last time check:
				long tempTime = sessionOnlineTimeLastCheck + SESSION_TIMEOUT_MINING_MS;
				long miningDuration = sessionOnlineTimeStart - tempTime;
				onlineMiningTimeTotal += miningDuration;
				
				sessionOnlineTimeStart = tempTime;
				
				// This may not be active, but could be afk...  but set it to active for now, 
				// and then the next check will evaluate to see if it should be afk.
				sessionType = SessionType.active;
			}
		
			if ( sessionType == SessionType.afk ) {
				// check to see if the player has moved... 
				
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
			
			// Record the SessionType which is mining:
			if ( sessionType != SessionType.mining ) {
				
				// The last session type was not mining so calculate duration and add to 
				// correct buckit:
				sessionOnlineTimeLastCheck = System.currentTimeMillis();
				long duration = sessionOnlineTimeLastCheck - sessionOnlineTimeStart;
				
				if ( sessionType == SessionType.afk ) {
					onlineAFKTimeTotal += duration;
				}
				else if ( sessionType == SessionType.active ) {
					onlineActiveTimeTotal += duration;
				}
				
				sessionType = SessionType.mining;
				sessionOnlineTimeStart = sessionOnlineTimeLastCheck;
			}
			else {
				// Already in mining mode so do nothing:
				
			}
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

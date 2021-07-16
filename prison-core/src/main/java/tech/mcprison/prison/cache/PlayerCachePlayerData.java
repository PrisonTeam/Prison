package tech.mcprison.prison.cache;

import java.io.File;

import tech.mcprison.prison.internal.Player;

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

	private transient Player player;
	
	private String playerUuid;
	private String playerName;
	
	private transient File playerFile = null;
	
	/**
	 *  This Object lock is used to synchronized the public side of this class
	 *  and the protected side of this class which is the database transaction
	 *  side of things.
	 */
	@SuppressWarnings( "unused" )
	private transient final Object lock = new Object();
	
	private transient boolean updateTaskSubmitted = false;
	
	
	
	/**
	 * This is just to hold on to the BukkitTask when the database update is submitted.
	 */
//	private PlayerCacheRunnable playerTask;
	
	
	private long onlineTimeTotal = 0L;
	private long onlineAFKTimeTotal = 0L;
	private long onlineMiningTimeTotal = 0L;
	
	
	public PlayerCachePlayerData( Player player, File playerFile ) {
		super();
		
		this.player = player;
		
		this.playerUuid = player.getUUID().toString();
		this.playerName = player.getName();
		
		this.playerFile = playerFile;
		
//		this.playerTask = null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getPlayer().getName() ).append( " [value= " ).append( 0 )
				.append( isUpdateTaskSubmitted() ? " *UpdateSubmitted*" : "" )
				.append( " ]" );
		
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

	public boolean isUpdateTaskSubmitted() {
		return updateTaskSubmitted;
	}
	public void setUpdateTaskSubmitted( boolean updateTaskSubmitted ) {
		this.updateTaskSubmitted = updateTaskSubmitted;
	}

//	public PlayerCacheRunnable getPlayerTask() {
//		return playerTask;
//	}
//	public void setPlayerTask( PlayerCacheRunnable playerTask ) {
//		this.playerTask = playerTask;
//	}

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

}

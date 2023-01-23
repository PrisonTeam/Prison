package tech.mcprison.prison.backpacks;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;

import tech.mcprison.prison.cache.CoreCacheData;
import tech.mcprison.prison.internal.Player;

public class BackpackCachePlayerData
	implements CoreCacheData {
	
	private transient Player player;
	
	private String playerUuid;
	private String playerName;
	
	private transient File playerFile = null;
	
//	/**
//	 *  This Object lock is used to synchronized the public side of this class
//	 *  and the protected side of this class which is the database transaction
//	 *  side of things.
//	 */
//	@SuppressWarnings( "unused" )
//	private transient final Object lock = new Object();
	
	private transient BackpackCacheRunnable task = null;
	
	
	private TreeMap<String, PlayerBackpack> backpacks;

	
	private transient boolean dirty = false;
	
	
	public BackpackCachePlayerData() {
		super();
		
		this.backpacks = new TreeMap<>();
	}

	public BackpackCachePlayerData( Player player, File playerFile ) {
		this();
		
		this.player = player;
		
		
		this.playerUuid = player.getUUID().toString();
		this.playerName = player.getName();
		
		this.playerFile = playerFile;
		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getPlayerName() ).append( ": " );
		
		Set<String> keys = getBackpacks().keySet();
		for (String key : keys) {
			PlayerBackpack bp = getBackpacks().get(key);

			sb.append( "[" ).append( bp.getBackpackType() )
				.append( " " ).append( bp.getName() )
				.append( " size: ").append( bp.getInventorySize() )
				.append( "] " );
		}
		
		return sb.toString();
	}
	
	protected Player getPlayer() {
		return player;
	}
	protected void setPlayer( Player player ) {
		this.player = player;
	}

	public TreeMap<String, PlayerBackpack> getBackpacks() {
		return backpacks;
	}
	public void setBackpacks(TreeMap<String, PlayerBackpack> backpacks) {
		this.backpacks = backpacks;
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

	public BackpackCacheRunnable getTask() {
		return task;
	}
	public void setTask(BackpackCacheRunnable task) {
		this.task = task;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty( boolean dirty ) {
		this.dirty = dirty;
	}
}

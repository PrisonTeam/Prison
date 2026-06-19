package tech.mcprison.prison.spigot.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.OfflineMcPlayer;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

public class SpigotOfflinePlayer
	implements OfflineMcPlayer {
	
	private RankPlayer rankPlayer;
	
	private OfflinePlayer offlinePlayer;
	
	
    private transient File filePlayer;
    private transient File fileCache;
    
    private transient String miscText;
	
	public SpigotOfflinePlayer(OfflinePlayer offlinePlayer) {
		this.offlinePlayer = offlinePlayer;
	}

	public static SpigotOfflinePlayer getOfflinePlayer( RankPlayer rankPlayer ) {
		SpigotOfflinePlayer result = null;
		
		// Get an offline player:
		OfflinePlayer olPlayer = Bukkit.getOfflinePlayer( rankPlayer.getUUID() );

		if ( olPlayer != null ) {
			result = new SpigotOfflinePlayer( olPlayer );
			
			result.setRankPlayer( rankPlayer );
		}
		
		return result;
	}

	@Override
	public String getName() {
		return offlinePlayer.getName();
	}
	
    /**
     * <p>This constructs a player file named based upon the UUID followed 
     * by the player's name.  This format is used so it's easier to identify
     * the correct player.
     * </p>
     * 
     * <p>The format should be UUID-PlayerName.json.  The UUID is a shortened 
     * format, which should still produce a unique id.  The name, when read, 
     * is based upon the UUID and not the player's name, which may change.
     * This format includes the player's name to make it easier to identify
     * who's record is whom's.
     * </p>
     * 
     * @return
     */
    public String getPlayerFileName() {
    	
    	return filenamePlayer();
    }
    
    
	public File getFilePlayer() {
		if ( filePlayer == null ) {
			filePlayer = JsonFileIO.filePlayer( this );;
		}
		return filePlayer;
	}
	public void setFilePlayer(File filePlayer) {
		this.filePlayer = filePlayer;
	}

	public File getFileCache() {
		if ( fileCache == null ) {
			fileCache = JsonFileIO.fileCache( this );
		}
		return fileCache;
	}
	public void setFileCache(File fileCache) {
		this.fileCache = fileCache;
	}

	/**
     * <p>This is a helper function to ensure that the given file name is 
     * always generated correctly and consistently.
     * </p>
     * 
     * @return "player_" plus the least significant bits of the UID
     */
    public String filenamePlayer()
    {
    	return getFilePlayer().getName();
    }
    
    public String filenameCache()
    {
    	return getFileCache().getName();
    }
    

    @Override
    public String toString() {
    		return getName();
    }
    
    
	@Override
	public void dispatchCommand( String command ) {
		
	}

	@Override
	public UUID getUUID() {
		return offlinePlayer.getUniqueId();
	}

	@Override
	public String getDisplayName() {
		return offlinePlayer.getName();
	}

	@Override
	public boolean isOnline() {
		return offlinePlayer.isOnline();
	}
	
	
	@Override
	public long getLastSeenDate() {
		return offlinePlayer.getLastPlayed();
	}
	
	/**
	 * NOTE: A SpigotOfflinePlayer does not represent an online player with inventory.  
	 *       This class is not "connected" to the underlying bukkit player
	 *       so technically this is not a player object, especially since it
	 *       always represents offline players too.
	 */
    @Override 
    public boolean isPlayer() {
	    	return ( offlinePlayer != null && offlinePlayer.getPlayer() != null &&
	    					offlinePlayer.getPlayer() instanceof Player );
    }
    
	@Override
	public void setDisplayName( String newDisplayName ) {
		Output.get().logError( "SpigotOfflinePlayer.setDisplayName: Cannot set display names." );
	}
	
	@Override
	public void sendMessage( String message ) {
		Output.get().logError( "SpigotOfflinePlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendMessage( String[] messages ) {
		Output.get().logError( "SpigotOfflinePlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendRaw( String json ) {
		Output.get().logError( "SpigotOfflinePlayer.sendRaw: Cannot send messages to offline players." );
	}

	@Override
	public boolean doesSupportColors() {
		return false;
	}

	@Override
	public void give( ItemStack itemStack ) {
		Output.get().logError( "SpigotOfflinePlayer.give: Cannot give to offline players." );
	}

	@Override
	public Location getLocation() {
//		Output.get().logError( "SpigotOfflinePlayer.getLocation: Offline players have no location." );
		return null;
	}

	@Override
	public boolean teleport( Location location ) {
		Output.get().logError( "SpigotOfflinePlayer.teleport: Offline players cannot be teleported." );
		return false;
	}

	@Override
	public void setScoreboard( Scoreboard scoreboard ) {
		Output.get().logError( "SpigotOfflinePlayer.setScoreboard: Offline players cannot use scoreboards." );
	}

	@Override
	public Gamemode getGamemode() {
		Output.get().logError( "SpigotOfflinePlayer.getGamemode: Offline is not a valid gamemode." );
		return null;
	}

	@Override
	public void setGamemode( Gamemode gamemode ) {
	}

	@Override
	public Optional<String> getLocale() {
		Output.get().logError( "SpigotOfflinePlayer.getLocale: Offline is not a valid gamemode." );
		return null;
	}

	@Override
    public tech.mcprison.prison.internal.block.Block getLineOfSightBlock() {
		return null;
	}
	
	@Override
    public List<tech.mcprison.prison.internal.block.Block> getLineOfSightBlocks() {
    	
    	List<tech.mcprison.prison.internal.block.Block> results = new ArrayList<>();
    		return results;
	}
	
	
	@Override
	public boolean isOp() {
		return offlinePlayer.isOp();
	}

	@Override
	public void updateInventory() {
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	
	public OfflinePlayer getWrapper() {
		return offlinePlayer;
	}

	/**
	 * <p>Technically, offline players do not have any perms through bukkkit so
	 * bukkit cannot recalculate any permissions.
	 * </p>
	 */
	@Override
	public void recalculatePermissions() {
		
//		offlinePlayer.getPlayer().recalculatePermissions();
	}
	
    @Override
    public List<String> getPermissions() {
	    	List<String> results = new ArrayList<>();
	    	
	    	if ( offlinePlayer.getPlayer() != null ) {
	    		
	    		Set<PermissionAttachmentInfo> perms = offlinePlayer.getPlayer().getEffectivePermissions();
	    		for ( PermissionAttachmentInfo perm : perms )
	    		{
	    			results.add( perm.getPermission() );
	    		}
	    	}
	    	else {
	    		// try to use vault:
	    		
	    		// TODO add permission integrations here!!
	    	}
	    	
	    	
	    	return results;
    }
    
    @Override
    public List<String> getPermissions( String prefix ) {
    	
    		return getPermissions( prefix, getPermissions() );
    }
    
    @Override
    public List<String> getPermissions( String prefix, List<String> perms ) {
	    	List<String> results = new ArrayList<>();
	    	
	    	for ( String perm : perms ) {
	    		if ( perm.startsWith( prefix ) ) {
	    			results.add( perm );
	    		}
	    	}
	    	
	    	return results;
    }
    
	@Override
	public boolean hasPermission( String perm ) {
		boolean hasPerm = false;
		
		if ( offlinePlayer.getPlayer() != null ) {
			
			hasPerm = offlinePlayer.getPlayer().hasPermission( perm );
		}
		else {
			List<String> perms = getPermissions( perm );
			hasPerm = perms.contains( perm );
		}
		
		return hasPerm;
	}
    
    
	@Override
	public List<String> getPermissionsIntegrations( boolean detailed ) {
		List<String> results = new ArrayList<>();
		
		return results;
	}

    
    /**
     * <p>SpigotOfflinePlayer represents a player that is offline, and the only time 
     * when bukkit has the player's perms loaded is when they are online.  If the 
     * player is online, when you have an instance of this object, then they will
     * be available through the bukkit's OfflinePlayer.getPlayer() function, otherwise
     * it will be null. 
     * </p>
     * 
     */
    @Override
    public double getSellAllMultiplier() {
    		double results = 1.0;
    	
		SellAllUtil sellall = SpigotPrison.getInstance().getSellAllUtil();
		
		if ( sellall != null && getWrapper() != null ) {
			
			results = sellall.getPlayerMultiplier( this );
		}
		
		return results;
    }
    
    @Override
    public double getSellAllMultiplierDebug() {
    		double results = 1.0;
    	
    	// NOTE: isPlayer() is a check to see if it's tied to the bukkit Player object, of 
    	//       which offline player is not.  But the sellall multiplier can still be called.
    		
    		SellAllUtil sellall = SpigotPrison.getInstance().getSellAllUtil();
    		
    		if ( sellall != null && getWrapper() != null ) {
    			
    			results = sellall.getPlayerMultiplierDebug( this );
    		}
    	
    	return results;
    }
    
    public List<String> getSellAllMultiplierListings() {
	    	List<String> results = new ArrayList<>();
	    	
	    	if ( isPlayer() ) {
	    		
	    		SellAllUtil sellall = SpigotPrison.getInstance().getSellAllUtil();
	    		
	    		if ( sellall != null && getWrapper() != null ) {
	    			results.addAll( sellall.getPlayerMultiplierList((org.bukkit.entity.Player) getWrapper()) );
	    		}
	    	}
	    	
	    	return results;
    }
    
	@Override
	public void setTitle( String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
	}
	
	@Override
	public void setActionBar( String actionBar ) {
	}

	public RankPlayer getRankPlayer() {
		
		if ( rankPlayer == null && PrisonRanks.getInstance() != null &&
				PrisonRanks.getInstance().isEnabled() ) {
			
			rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer( this );
		}
		return rankPlayer;
	}
	private void setRankPlayer( RankPlayer rankPlayer ) {
		this.rankPlayer = rankPlayer;
	}
	
	
	@Override
	public PlayerCache getPlayerCache() {
		return PlayerCache.getInstance();
	}
	
	@Override
	public PlayerCachePlayerData getPlayerCachePlayerData() {
		return PlayerCache.getInstance().getOnlinePlayer( this );
	}
	
	@Override
	public boolean isSneaking() {
		return false;
	}
	
	
	@Override
	public boolean isMinecraftStatisticsEnabled() {
		return false;
	}
	

	@Override
	public void incrementMinecraftStatsMineBlock( tech.mcprison.prison.internal.Player player,
			String blockName, int quantity) {
		
	}
    
	@Override
	public void incrementMinecraftStatsDropCount( tech.mcprison.prison.internal.Player player, 
			String blockName, int quantity) {
		
	}

	@Override
	public void sendMessage(List<String> messages) {
		
	}

	@Override
	public tech.mcprison.prison.internal.Player getPlatformPlayer() {
		
		SpigotPlayer sPlayer = SpigotPlayer.getSpigotPlayer( getRankPlayer() );
		
		return sPlayer;
	}

	
	/**
	 * This miscText is not used for any specific purpose other than to hold a String 
	 * value.  It can be used to return a message from a function, but it should always
	 * be cleared when done using it.
	 * 
	 * @return
	 */
	@Override
	public String getMiscText() {
		return miscText;
	}
	@Override
	public void setMiscText( String text )  {
		miscText = text;
	}
}

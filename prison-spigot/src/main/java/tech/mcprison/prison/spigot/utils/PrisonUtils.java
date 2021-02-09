package tech.mcprison.prison.spigot.utils;

import java.util.Optional;

import org.bukkit.Bukkit;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.spigot.game.SpigotOfflinePlayer;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public abstract class PrisonUtils
		implements PrisonUtilsInterface {
	
	private String pluginName;
	private boolean pluginRegistered = false;
	private String pluginVersion;
	
	
	private Boolean enabled = null;
	
	
	/**
	 * <p>This constructor is for classes that do not need to access other plugins.
	 * </p>
	 * 
	 */
	public PrisonUtils() {
		super();
	}
	
	/**
	 * <p>This constructor is for setting up and initializing access to other plugins.
	 * </p>
	 * 
	 * @param pluginName
	 */
	public PrisonUtils( String pluginName ) {
		super();
		
		this.pluginName = pluginName;
		
		pluginInitialize();
	}
	
	
	private void pluginInitialize() {
		
		setPluginRegistered( Bukkit.getPluginManager().isPluginEnabled( getPluginName() ) );
		
    	setPluginVersion( isPluginRegistered() ? 
    			Bukkit.getPluginManager().getPlugin( getPluginName() )
    											.getDescription().getVersion() : null );
		
    	this.enabled = initialize();
	}
	
	@Override
	public boolean isEnabled() {

		
		return enabled.booleanValue();
	}
	
	/**
	 * <p>This function performs all of the initialization and to determine if the 
	 * module should be, or could be, enabled.
	 * </p>
	 * 
	 * @return
	 */
	abstract protected Boolean initialize();

	
	/**
	 * <p>This function checks the perms of the player.  If the CommandSender has no 
	 * permission to run these commands, then this function will return a null which is
	 * indicates the command cannot be ran.
	 * </p>
	 * 
	 * <p>Since playerName is optional, it may contain values from options so it is
	 * perfectly fine if playerName does not map to an actual player.  If playerName
	 * contains options, this function does not need to deal with that situation since
	 * it's handled within the options processing.
	 * </p>
	 * 
	 * @param sender
	 * @param playerName
	 * @param permsSelf
	 * @param permsOthers
	 * @return
	 */
	protected SpigotPlayer checkPlayerPerms( CommandSender sender, String playerName, 
					String permsSelf, String permsOthers ) {
		
		boolean isConsole = !(sender instanceof org.bukkit.entity.Player);
		
    	SpigotPlayer player = getSpigotPlayer( playerName, false );
    	
    	// Player's name was not found then it's either being ran from console, or on self.
    	if ( player == null ) {
    		
    		// Ran from console, so if player is null, then playerName was either not
    		// specified or was invalid:
    		if ( isConsole ) {
    			sender.sendMessage( String.format(
    					"&3PlayerName is incorrect or they are not online. [&7%s&3]", 
    					(playerName == null ? "null" : playerName) ));
    		}
    		else if ( !sender.isOp() && !sender.hasPermission( permsSelf ) ) {
    			sender.sendMessage( String.format(
    					"&3You do not have the permission to use on another player. [&7%s&3]", 
    					(playerName == null ? "null" : playerName) ));
    				
    		}
    		else {
    			
    			// sender is able to use the command on their self:
    			player = new SpigotPlayer( (org.bukkit.entity.Player) sender);
    		}

    	}
    	else {
    		
    		// The provided playerName was valid.
    		
    		// Need to confirm the sender is either console, OP, or has perms to 
    		// use command on someone else, if none of these apply, then reject
    		// the request to run the command.
    		if ( !isConsole && !sender.isOp() && !sender.hasPermission( permsOthers ) ) {
    			sender.sendMessage( String.format(
    					"&3You do not have the permission to use on another player. [%s]", 
    					(playerName == null ? "null" : playerName) ));

    			// Set player to null to indicate authentication failed:
    			player = null;
    		}
    	}
    	
    	return player;
	}
	
	
    /**
     * <p>Gets a player by name.  If the player is not online, then try to get them from 
     * the offline player list. If not one is found, then return a null.
     * </p>
     * 
     * <p>The getOfflinePlayer() will now include RankPlayer as a fall back to help
     * ensure a player is always returned, if its a valid player.
     * </p>
     * 
     * @param sender
     * @param playerName is optional, if not supplied, then sender will be used
     * @return Player if found, or null.
     */
	protected SpigotPlayer getSpigotPlayer( String playerName, boolean useOfflinePlayer ) {
		SpigotPlayer result = null;
		
		if ( playerName != null ) {
			Optional<Player> opt = Prison.get().getPlatform().getPlayer( playerName );
			
			if ( opt.isPresent() ) {
				result = (SpigotPlayer) opt.get();
			}
			else if ( useOfflinePlayer ) {
				Optional<Player> optOLP = Prison.get().getPlatform().getOfflinePlayer( playerName );
				
				if ( optOLP.isPresent() ) {
					
					SpigotOfflinePlayer offlinePlayer = (SpigotOfflinePlayer) optOLP.get();
					
					result = new SpigotPlayer( offlinePlayer.getWrapper().getPlayer() );
				}
			}
			
		}
		return result;
	}
	
	/**
	 * <p>This parses a String value to an int.  It uses a default value and
	 * also constrains the results to be between within a given range.
	 * </p>
	 * 
	 * @param value
	 * @param defaultValue
	 * @param rangeLow
	 * @param rangeHigh
	 * @return
	 */
	protected int intValue( String value, int defaultValue, int rangeLow, int rangeHigh ) {
		int results = defaultValue;
		
		if ( value != null && !value.trim().isEmpty() ) {
			
		}
		
		try {
			results = Integer.parseInt( value );
		}
		catch ( NumberFormatException e ) {
			// Not a valid number so ignore
		}
		
		if ( results < rangeLow ) {
			results = rangeLow;
		}
		else if ( results > rangeHigh ) {
			results = rangeHigh;
		}
		
		return results;
	}

	
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName( String pluginName ) {
		this.pluginName = pluginName;
	}

	public boolean isPluginRegistered() {
		return pluginRegistered;
	}
	public void setPluginRegistered( boolean pluginRegistered ) {
		this.pluginRegistered = pluginRegistered;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}
	public void setPluginVersion( String pluginVersion ) {
		this.pluginVersion = pluginVersion;
	}

	
}

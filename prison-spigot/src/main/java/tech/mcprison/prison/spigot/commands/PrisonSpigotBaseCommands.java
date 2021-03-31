package tech.mcprison.prison.spigot.commands;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotCommandSender;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotBaseCommands {

	private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

    public Configuration getMessages() {
		return messages;
	}

	protected boolean isConfig( String configId ) {
    	
    	String config = getConfig().getString( configId );

		return config != null && config.equalsIgnoreCase( "true" );
    }
    
    protected boolean isPrisonConfig( String configId ) {

    	return SpigotPrison.getInstance().isPrisonConfig( configId );
    }
    
    protected String getConfig( String configId ) {
    	
    	String config = getConfig().getString( configId );

    	return config == null ? "" : config;
    }
    
    
    protected String getPrisonConfig( String configId ) {
		return SpigotPrison.getInstance().getConfig().getString( configId );
    }
    
    protected Configuration getConfig() {
		return SpigotPrison.getInstance().getGuiConfig();
    }

    protected Player getSpigotPlayer( CommandSender sender ) {
        Player player = null;
        
        if ( sender instanceof SpigotCommandSender ) {
        	SpigotCommandSender cmdSender = (SpigotCommandSender) sender;
        	
        	if (cmdSender.getWrapper() instanceof Player) {
        		player = (Player) cmdSender.getWrapper();
        	}
        }
        return player;
	}

	/**
	 * Java getBoolean's broken so I made my own.
	 * */
	public boolean getBoolean(String string){
		return string != null && string.equalsIgnoreCase("true");
	}

}

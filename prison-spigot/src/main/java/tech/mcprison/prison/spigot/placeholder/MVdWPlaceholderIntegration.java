package tech.mcprison.prison.spigot.placeholder;

import java.util.function.Function;

import org.bukkit.Bukkit;

import tech.mcprison.prison.integration.PlaceholderIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * LuckPerms v5 documentation notes on MVdWPlaceholderAPI:
 * 
 * To use the LuckPerms placeholders in plugins which support Maximvdw's MVdWPlaceholderAPI, 
 * you need to install the LuckPerms placeholder hook plugin.
 * https://github.com/lucko/LuckPerms/wiki/Placeholders
 * 
 * So this means that if this plugin is "loaded" and so is LuckPerms, then
 * we need to confirm that user also installed the required jar file on their 
 * server.  We can do that by checking to see if the class files exist in the 
 * classpath?  if they don't, then we should provide a warning message
 * so the the admin will better understand why its failing.
 *
 */
public class MVdWPlaceholderIntegration 
	implements PlaceholderIntegration {

    public static final String PROVIDER_NAME = "MVdWPlaceholderAPI";
    private MVdWPlaceholderIntegrationWrapper placeholderWrapper;
	
    public MVdWPlaceholderIntegration() {
    	super();
    }
	
	@Override
	public void integrate() {
		if ( Bukkit.getPluginManager().isPluginEnabled(PROVIDER_NAME))
		placeholderWrapper = new MVdWPlaceholderIntegrationWrapper(PROVIDER_NAME);
	}
	
    @Override
    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
        if (placeholderWrapper != null) {
        	placeholderWrapper.registerPlaceholder( placeholder, action );
        }
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    @Override
    public String getKeyName() {
    	return PROVIDER_NAME;
    }
    
    @Override
    public boolean hasIntegrated() {
        return (placeholderWrapper != null);
    }

}

package tech.mcprison.prison.spigot.placeholder;

import java.util.function.Function;

import org.bukkit.Bukkit;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;
import tech.mcprison.prison.integration.PlaceholderIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.util.Text;

/**
 * <p>This hooks up the registration when the Prison plugin starts to run.
 * The MVdWPlaceholderIntegrationWrapper sets up the registrations. It should
 * be noted that the registrations used to occur within the ChatHandler, but
 * that did not make sense since these should be self-contained with the 
 * registration process if these plugins are active.
 * </p>
 * 
 * <p>LuckPerms v5 documentation notes on MVdWPlaceholderAPI:
 * 
 * To use the LuckPerms placeholders in plugins which support Maximvdw's MVdWPlaceholderAPI, 
 * you need to install the LuckPerms placeholder hook plugin.
 * https://github.com/lucko/LuckPerms/wiki/Placeholders
 * </p>
 *
 */
public class MVdWPlaceholderIntegration 
	extends PlaceholderIntegration {

    private MVdWPlaceholderIntegrationWrapper placeholderWrapper;
	
    public MVdWPlaceholderIntegration() {
    	super( "MVdWPlaceholderAPI", "MVdWPlaceholderAPI" );
    }
	
	@Override
	public void integrate() {
		if ( isRegistered()) {
			try {
				if ( Bukkit.getPluginManager().isPluginEnabled(getProviderName())) {
					placeholderWrapper = new MVdWPlaceholderIntegrationWrapper(getProviderName());
					
					PrisonAPI.getIntegrationManager().addDeferredInitialization( this );
				}
			}
			catch ( NoClassDefFoundError | IllegalStateException e ) {
				// ignore this exception since it means the plugin was not loaded
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	
	
    @Override
	public void deferredInitialization()
	{
    	PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    	for ( PrisonPlaceHolders placeHolder : PrisonPlaceHolders.values() ) {
    		if ( !placeHolder.isSuppressed() ) {
    			registerPlaceholder(placeHolder.name(),
    					player -> Text.translateAmpColorCodes(
    							pm.getTranslatePlayerPlaceHolder( player.getUUID(), placeHolder.name() )
    							));
    		}
    	}
	}

	@Override
    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
        if (placeholderWrapper != null) {
        	placeholderWrapper.registerPlaceholder( placeholder, action );
        }
    }
	
    @Override
    public boolean hasIntegrated() {
        return (placeholderWrapper != null);
    }
    
    @Override
    public String getAlternativeInformation() {
    	return "&7Available PlaceHolders: " + PrisonPlaceHolders.getAllChatTextsOmitSuppressable();
    }

	@Override
	public String getPluginSourceURL() {
		return "https://www.spigotmc.org/resources/mvdwplaceholderapi.11182/";
	}
    
}

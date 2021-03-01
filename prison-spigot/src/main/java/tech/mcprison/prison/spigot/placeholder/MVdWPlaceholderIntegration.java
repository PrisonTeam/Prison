package tech.mcprison.prison.spigot.placeholder;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Bukkit;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.placeholders.PlaceHolderKey;
import tech.mcprison.prison.placeholders.PlaceholderAttribute;
import tech.mcprison.prison.placeholders.PlaceholderIntegration;
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
	
	
	/**
	 * <p>Register both the player and mines placeholders with the MVdW plugin.
	 * </p>
	 */
    @Override
	public void deferredInitialization()
	{
    	boolean registered = false;
    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		if ( pm != null ) {
    			List<PlaceHolderKey> placeholderPlayerKeys = pm.getTranslatedPlaceHolderKeys();
    			
    			for ( PlaceHolderKey placeHolderKey : placeholderPlayerKeys ) {
    				if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
    					registerPlaceholder(placeHolderKey.getKey(),
    							player -> Text.translateAmpColorCodes(
    									pm.getTranslatePlayerPlaceHolder( 
    											player.getUUID(), player.getName(),
    											placeHolderKey, null )
    									));
    					if ( !registered ) {
    						registered = true;
    					}
    				}
    			}
    		}
    	}
    	

    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();
    		if ( mm != null ) {
    			List<PlaceHolderKey> placeholderMinesKeys = mm.getTranslatedPlaceHolderKeys();
    			
    			for ( PlaceHolderKey placeHolderKey : placeholderMinesKeys ) {
    				if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
    					registerPlaceholder(placeHolderKey.getKey(),
    							player -> Text.translateAmpColorCodes(
    									mm.getTranslateMinesPlaceHolder( placeHolderKey, (PlaceholderAttribute) null  )
    									));
    					if ( !registered ) {
    						registered = true;
    					}
    				}
    			}
    		}
    	}
    	
//    	if ( registered ) {
//    		Output.get().logWarn( "Prison registered all placeholders with MVdWPlaceholderAPI, " +
//    						"but unfortunately MVdWPlaceholderAPI does not support dynamic placeholders " +
//    						"that are available for customizations within prison.  Please try adding " +
//    						"Vault and PlaceholderAPI (papi) to your setup, if they do not already exist, " +
//    						"to enable these features.");
//    	}
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
    	return null;
    }
    

	@Override
	public String getPluginSourceURL() {
		return "https://www.spigotmc.org/resources/mvdwplaceholderapi.11182/";
	}
    
}

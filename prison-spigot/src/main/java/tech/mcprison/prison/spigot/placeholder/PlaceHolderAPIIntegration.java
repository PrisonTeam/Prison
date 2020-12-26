package tech.mcprison.prison.spigot.placeholder;

import java.util.function.Function;

import org.bukkit.Bukkit;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.placeholders.PlaceholderIntegration;

public class PlaceHolderAPIIntegration 
	extends PlaceholderIntegration {
	
	private PlaceHolderAPIIntegrationWrapper placeHolderWrapper;

	public PlaceHolderAPIIntegration() {
		super( "PlaceholderAPI", "PlaceholderAPI" );
	}
	
	@Override
	public void integrate() {
		if ( isRegistered()) {
			try {
				if ( Bukkit.getPluginManager().isPluginEnabled(getProviderName()) ) {
					this.placeHolderWrapper = new PlaceHolderAPIIntegrationWrapper();
					this.placeHolderWrapper.register();
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
	 * <p>For PlaceHolderAPI this function is not needed since it is registered when
	 * integrate() is called since it is a simple integration.
	 * </p>
	 */
	@Override
	public void registerPlaceholder(String placeholder, Function<Player, String> action) {
//		if ( placeHolderWrapper != null ) {
//			placeHolderWrapper.registerPlaceholder( placeholder, action );
//		}
	}
    
	@Override
	public void deferredInitialization() {
	}
	
	@Override
	public boolean hasIntegrated() {
		return (placeHolderWrapper != null);
	}
	
	@Override
    public String getAlternativeInformation() {
    	return null;
    }

	@Override
	public String getPluginSourceURL() {
		return "https://www.spigotmc.org/resources/placeholderapi.6245/";
	}
}

package tech.mcprison.prison.spigot.placeholder;

import java.util.function.Function;

import org.bukkit.Bukkit;

import tech.mcprison.prison.integration.PlaceholderIntegration;
import tech.mcprison.prison.internal.Player;

public class PlaceHolderAPIIntegration 
	
	implements PlaceholderIntegration {
	
	public static final String PROVIDER_NAME = "PlaceHolderAPI";
	private PlaceHolderAPIIntegrationWrapper placeHolderWrapper;
//	private boolean pluginInstalled;

	public PlaceHolderAPIIntegration() {
		super();
	}
	
	@Override
	public void integrate() {
		if ( Bukkit.getPluginManager().isPluginEnabled(PROVIDER_NAME) ) {
			this.placeHolderWrapper = new PlaceHolderAPIIntegrationWrapper();
		}
	}
	
	@Override
	public void registerPlaceholder(String placeholder, Function<Player, String> action) {
		if ( placeHolderWrapper != null ) {
			placeHolderWrapper.registerPlaceholder( placeholder, action );
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
		return (placeHolderWrapper != null);
	}

}

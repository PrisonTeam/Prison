package tech.mcprison.prison.spigot.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import tech.mcprison.prison.placeholders.ManagerPlaceholders;
import tech.mcprison.prison.placeholders.PlaceHolderKey;
import tech.mcprison.prison.placeholders.PlaceholderIdentifier;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrisonCustomPlaceholders
	implements ManagerPlaceholders {
	
	public static final String CUSTOM_PLACEHOLDER_CONFIG_PATH = "placeholder.custom-placeholders";

	private List<PlaceHolderKey> translatedPlaceHolderKeys;
	
	public PrisonCustomPlaceholders() {
		
	}
	
	
	public String getTranslateCustomPlaceHolder( PlaceholderIdentifier identifier ) {
		String results = null;
		
		List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
		
		for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
			
			if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
				
				results = placeHolderKey.getData();
				identifier.setText(results);
				
				break;
			}
			
		}
		
		return results;
	}
	
	
    
    public String getTranslateCustomPlaceHolder( UUID playerUuid, String playerName, String identifier ) {
    	String results = null;

    	if ( playerUuid != null ) {
    		
    		List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    		
    		
    		PlaceholderIdentifier phIdentifier = new PlaceholderIdentifier( identifier );
    		phIdentifier.setPlayer(playerUuid, playerName);
    		
    		
    		for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
    			
    			if ( phIdentifier.checkPlaceholderKey(placeHolderKey) ) {
    				
    				results = placeHolderKey.getData();
    				
    				break;
    			}
    			
    		}
    	}
    	
    	return results;
    }
    
	
	@Override
	public List<PlaceHolderKey> getTranslatedPlaceHolderKeys() {
		
		if ( translatedPlaceHolderKeys == null ) {
    		translatedPlaceHolderKeys = new ArrayList<>();
    		
    		FileConfiguration config = SpigotPrison.getInstance().getConfig();

    		if ( config.isConfigurationSection(CUSTOM_PLACEHOLDER_CONFIG_PATH) ) {
    			
    			PrisonPlaceHolders customPlaceholder = PrisonPlaceHolders.custom_placeholder__;
    			
    			ConfigurationSection cpConfigs = config.getConfigurationSection(CUSTOM_PLACEHOLDER_CONFIG_PATH);
    			
    			Set<String> keys = cpConfigs.getKeys( false );
    			
    			for (String key : keys) {
    				
    				String customPlaceholderStr = cpConfigs.getString( key );
    				
    				PlaceHolderKey placeholder = new PlaceHolderKey(key, customPlaceholder, customPlaceholderStr );
    				translatedPlaceHolderKeys.add(placeholder);
    			}
    		}
		}
		return translatedPlaceHolderKeys;
	}

	@Override
	public void reloadPlaceholders() {
    	
    	// clear the class variable so they will regenerate:
    	translatedPlaceHolderKeys = null;
    	
    	// Regenerate the translated placeholders:
    	getTranslatedPlaceHolderKeys();
		
	}

}

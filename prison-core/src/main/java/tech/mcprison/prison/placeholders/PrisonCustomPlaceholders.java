package tech.mcprison.prison.placeholders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;

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
    
    /**
     * <p>This checks for custom placeholders and gets their values.
     * A custom placeholder can be abbreviated, or expanded.
     * <p>
     * 
     * <p>An abbreviated placeholder only has the text with no other settings:
     * </p>
     * <pre>
     *   
  custom-placeholders:
    prison__chat_prefix: "{prison_rank_tag_default}{prison_rank_tag_prestiges}"
     * </pre>
     * 
     * <p>An extended placeholder has more details and settings:
     * </p>
     * <pre>
  custom-placeholders:
    prison__chat_prefix: 
      placeholder: "{prison_rank_tag_default}{prison_rank_tag_prestiges}"
      papi_expansion: false
      </pre>
     */
	@Override
	public List<PlaceHolderKey> getTranslatedPlaceHolderKeys() {
		
		if ( translatedPlaceHolderKeys == null ) {
    		translatedPlaceHolderKeys = new ArrayList<>();
    		
//    		FileConfiguration config = SpigotPrison.getInstance().getConfig();
    		
    		Platform pf = Prison.get().getPlatform();
    		
    		if ( pf.isConfigSection(CUSTOM_PLACEHOLDER_CONFIG_PATH) ) {
    			
    			
    			
    			PrisonPlaceHolders customPlaceholder = PrisonPlaceHolders.custom_placeholder__;
    			
    			
    			List<String> keys = pf.getConfigHashKeys(CUSTOM_PLACEHOLDER_CONFIG_PATH);
    			
//    			ConfigurationSection cpConfigs = config.getConfigurationSection(CUSTOM_PLACEHOLDER_CONFIG_PATH);
//    			
//    			Set<String> keys = cpConfigs.getKeys( false );
    			
    			for (String key : keys) {
    				
    				String keyPath = CUSTOM_PLACEHOLDER_CONFIG_PATH + "." + key;
    				
    				String custExpandedPlaceholderKey = keyPath + ".placeholder";
    				String custExpandedPapiExpansionKey = keyPath + ".papi_expansion";
    				String custExpandedDescriptionKey = keyPath + ".description";
    				
    				String cePlaceholder = pf.getConfigString( custExpandedPlaceholderKey );
    				boolean cePapiExpansion = pf.getConfigBooleanFalse( custExpandedPapiExpansionKey );
    				String ceDescription = pf.getConfigString( custExpandedDescriptionKey );
    				
    				String customPlaceholderStr = 
    							cePlaceholder != null ? 
    									cePlaceholder : pf.getConfigString( keyPath );
    				
    				PlaceHolderKey placeholder = new PlaceHolderKey(key, customPlaceholder, customPlaceholderStr );
    				placeholder.setPapiExpansion( cePapiExpansion );
    				placeholder.setDescription( ceDescription );
    				
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

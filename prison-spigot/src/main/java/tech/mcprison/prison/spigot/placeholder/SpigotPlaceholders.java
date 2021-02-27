package tech.mcprison.prison.spigot.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceHolderKey;
import tech.mcprison.prison.placeholders.PlaceholderAttribute;
import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceHolderFlags;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;
import tech.mcprison.prison.placeholders.PlaceholderResults;
import tech.mcprison.prison.placeholders.Placeholders;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.SpigotPrison;

public class SpigotPlaceholders
	implements Placeholders {

    
	@Override
    public Map<PlaceHolderFlags, Integer> getPlaceholderDetailCounts() {
    	Map<PlaceHolderFlags, Integer> placeholderDetails = new TreeMap<>();
    	
    	List<PlaceHolderKey> placeholders = new ArrayList<>();
    	
    	
    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		if ( pm != null ) {
    			placeholders.addAll( pm.getTranslatedPlaceHolderKeys() );
    		}
    	}

    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();
    		if ( mm != null ) {
    			placeholders.addAll( mm.getTranslatedPlaceHolderKeys() );
    		}
    	}
    	
    	for ( PlaceHolderKey phKey : placeholders ) {
			for ( PlaceHolderFlags flag : phKey.getPlaceholder().getFlags() ) {

				int count = 0;
				
				if ( placeholderDetails.containsKey( flag ) ) {
					count = placeholderDetails.get( flag );
				}
				
				placeholderDetails.put( flag, new Integer(count + 1) );
			}
		}

    	return placeholderDetails;
    }
    
    @Override
    public int getPlaceholderCount() {
    	int placeholdersRawCount = 0;
    	
    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		if ( pm != null ) {
    			List<PlaceHolderKey> placeholderPlayerKeys = pm.getTranslatedPlaceHolderKeys();
    			placeholdersRawCount += placeholderPlayerKeys.size();
    			
    		}
    	}
    	

    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();
    		if ( mm != null ) {
    			List<PlaceHolderKey> placeholderMinesKeys = mm.getTranslatedPlaceHolderKeys();
    			placeholdersRawCount += placeholderMinesKeys.size();
    			
    		}
    		
    	}

    	return placeholdersRawCount;
    }
    
    @Override
    public int getPlaceholderRegistrationCount() {
    	int placeholdersRegistered = 0;
    	
    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		if ( pm != null ) {
    			List<PlaceHolderKey> placeholderPlayerKeys = pm.getTranslatedPlaceHolderKeys();
    			
    			for ( PlaceHolderKey placeHolderKey : placeholderPlayerKeys ) {
    				if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
    					placeholdersRegistered++;
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
    					placeholdersRegistered++;
    				}
    			}
    		}
    		
    	}

    	return placeholdersRegistered;
    }

    
    /**
     * <p>Provides placeholder translation for any placeholder identifier
     * that is provided.  This not the full text with one or more placeholders,
     * but it is strictly just the placeholder.
     * </p>
     * 
     * <p>This is a centrally located placeholder translator that is able
     * to access both the PlayerManager (ranks) and the MineManager (mines).
     * </p>
     *  
     * <p>This function is used with: 
     * tech.mcprison.prison.spigot.placeholder.PlaceHolderAPIIntegrationWrapper.onRequest()
     * </p>
     * 
     * <p>Note: MVdWPlaceholder integration is using something else.  Probably should be
     * converted over to use this to standardize and simplify the code.
     * </p>
     * 
     */
    @Override
    public String placeholderTranslate(UUID playerUuid, String playerName, String identifier) {
		String results = null;

		if (  PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() && 
					playerUuid != null ) {
			PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
			if ( pm != null ) {
				results = pm.getTranslatePlayerPlaceHolder( playerUuid, playerName, identifier );
			}
		}
		
		// If it did not match on a player placeholder, then try mines:
		if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() && 
					results == null ) {
			MineManager mm = PrisonMines.getInstance().getMineManager();
			if ( mm != null ) {
				results = mm.getTranslatePlayerMinesPlaceHolder( playerUuid, playerName, identifier );

				if ( results == null ) {
					results = mm.getTranslateMinesPlaceHolder( identifier );
				}
			}
			
		}
		
//		if ( results == null ) {
//			Output.get().logInfo( "### ### SpigotPlaceholders.placeholderTranslate: " +
//					"could not translate identifyer: %s  playerUuid: %s  playerName: %s", 
//					(identifier == null ? "(null)" : identifier), playerUuid, playerName );
//		}
		
		return results;
	}
    
    /**
     * <p>This function is used in this class's placeholderTranslateText() and
     * also in tech.mcprison.prison.mines.MinesChatHandler.onPlayerChat().
     * </p>
     * 
     */
    @Override
    public String placeholderTranslateText( String text) {
		String results = text;

		
		if ( results != null && PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
			MineManager mm = PrisonMines.getInstance().getMineManager();
			
			if ( mm != null ) {
				
				List<PlaceHolderKey> placeholderKeys = mm.getTranslatedPlaceHolderKeys();
				
				for ( PlaceHolderKey placeHolderKey : placeholderKeys ) {
					
					String key1 = "{" + placeHolderKey.getKey();
					String key2 = "}";
					
					int idx = results.indexOf( key1 );
					int idx2 = idx == -1 ? -1 : results.indexOf( key2, idx );
					if ( idx > -1 && idx2 > -1 ) {
						
						String identifier = results.substring( idx + 1, idx2 );
						
//						String msg = "&a##^^##  &7key1=" + key1 + " key2=" + key2 + "  idx=" + idx + 
//								"idx2= " + idx2 + " identifier=[" + identifier + "] ";
//						Output.get().logInfo( msg );
						
						String replacement = mm.getTranslateMinesPlaceHolder( placeHolderKey, identifier );
						if ( replacement == null ) {
							replacement = "";
						}
						
						results = results.replace("{" + identifier + "}", replacement );

					}
					
					//String key = "{" + placeHolderKey.getKey() + "}";
//					if ( results.contains( key )) {
//						results = results.replace(key, 
//								mm.getTranslateMinesPlaceHolder( placeHolderKey ) );
//					}
				}
			}
		}
		
		


		return results;
	}
    
    /**
     * <p>Since a player UUID is provided, first translate for any possible 
     * player specific placeholder, then try to translate for any mine
     * related placeholder.
     * </p>
     * 
     * <p>This function is used with the command: /prison placeholders test
     * </p>
     * 
     * @param playerUuid
     * @param text
     * @return
     */
    @Override
    public String placeholderTranslateText( UUID playerUuid, String playerName, String text) {
    	String results = text;
    	
    	// First the player specific placeholder, which must have a UUID:
    	if ( text != null && PrisonRanks.getInstance() != null && 
    			PrisonRanks.getInstance().isEnabled() && playerUuid != null ) {
    		
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		
    		List<PlaceHolderKey> placeholderKeys = pm.getTranslatedPlaceHolderKeys();
    		
    		for ( PlaceHolderKey placeHolderKey : placeholderKeys ) {
    			
    			PlaceholderResults identifier = placeHolderKey.getIdentifier( results );
    			
    			if ( results != null && identifier != null && identifier.hasResults() ) {
    				
    				
    				results = results.replace( identifier.getEscapedIdentifier(), 
							pm.getTranslatePlayerPlaceHolder( playerUuid, playerName, identifier.getIdentifier() ) );
    			}
    			
//    			// Rank in to an issue with placeholders: prison_mbm_minename and prison_mbm_pm, 
//    			// because the mine P has a placeholder prison_mbm_p which gets hit for the
//    			// prison_mbm_pm.  So to zero in on the correct placeholder, but bracket the end
//    			// of the placeholder with either } or :: to ensure the correct association.
//    			String test1 = "{" + placeHolderKey.getKey().toLowerCase() + "}";
//    			String test2 = "{" + placeHolderKey.getKey().toLowerCase() + 
//    								PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR;
//    			
//    			String resultsLowercase = results.toLowerCase();
//    			if ( resultsLowercase.contains( test1 ) || resultsLowercase.contains( test2 ) ) {
//    				
//    				// The key1 and key2 helps ensure that the full placeholder, 
//    				// including the attribute, is replaced:
//    				String key1 = "{" + placeHolderKey.getKey().toLowerCase();
//    				String key2 = "}";
//    				
//    				int idx = resultsLowercase.indexOf( key1 );
//    				int idx2 = ( idx == -1 ? -1 : resultsLowercase.indexOf( key2, idx + key1.length() - 1 ) );
//    				if ( idx > -1 && idx2 > -1 ) {
//    					
//    					String identifier = results.substring( idx + 1, idx2 );
//    					
//    					results = results.replace("{" + identifier + "}", 
//    							pm.getTranslatePlayerPlaceHolder( playerUuid, playerName, identifier ) );
//    				}
//    			}

    		}
    	}
    	
    	
    	
		// Check the mine's playerMines placeholders:
		if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
			MineManager mm = PrisonMines.getInstance().getMineManager();
			if ( mm != null ) {
				
	    		List<PlaceHolderKey> placeholderKeys = mm.getTranslatedPlaceHolderKeys();
	    		
	    		for ( PlaceHolderKey placeHolderKey : placeholderKeys ) {
	    			
	    			PlaceholderResults identifier = placeHolderKey.getIdentifier( results );
	    			
	    			if ( identifier.hasResults() ) {
	    				results = results.replace( identifier.getEscapedIdentifier(), 
								mm.getTranslatePlayerMinesPlaceHolder( playerUuid, playerName, 
													placeHolderKey, identifier.getIdentifier() ) );
	    			}
	   
	    			
//	    			// Rank in to an issue with placeholders: prison_mbm_minename and prison_mbm_pm, 
//	    			// because the mine P has a placeholder prison_mbm_p which gets hit for the
//	    			// prison_mbm_pm.  So to zero in on the correct placeholder, but bracket the end
//	    			// of the placeholder with either } or :: to ensure the correct association.
//	    			String test1 = "{" + placeHolderKey.getKey() + "}";
//	    			String test2 = "{" + placeHolderKey.getKey() + 
//	    								PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR;
//	    			if ( results.contains( test1 ) || results.contains( test2 ) ) {
//	    				
//	    				// The key1 and key2 helps ensure that the full placeholder, 
//	    				// including the attribute, is replaced:
//	    				String key1 = "{" + placeHolderKey.getKey();
//	    				String key2 = "}";
//	    				
//	    				int idx = results.indexOf( key1 );
//	    				if ( idx > -1 && results.indexOf( key2, idx ) > -1 ) {
//	    					
//	    					String identifier = results.substring( idx + 1, results.indexOf( key2, idx ) );
//	    					String target = "{" + identifier + "}";
//	    					
//	    					String replacement = mm.getTranslatePlayerMinesPlaceHolder( 
//	    										playerUuid, playerName, placeHolderKey, identifier );
//	    					
//	    					results = results.replace( target, 
//		    									(replacement == null ? "" : replacement) );
//	    				}
//	    			}
	    			
	    		}
			}
			
		}

    	// Then translate any remaining non-player (mine) related placeholders:
    	results = placeholderTranslateText( results);
    	
    	return results;
    }

	@Override
	public List<String> placeholderSearch( UUID playerUuid, String playerName, String[] patterns )
	{
		List<String> results = new ArrayList<>();
		
		TreeMap<String, PlaceHolderKey> placeholderKeys = new TreeMap<>();
		
		MineManager mm = null;
		PlayerManager pm = null;
		
		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			pm = PrisonRanks.getInstance().getPlayerManager();
			addAllPlaceHolderKeys( placeholderKeys, pm.getTranslatedPlaceHolderKeys() );
		}
		
		if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
			mm = PrisonMines.getInstance().getMineManager();
			addAllPlaceHolderKeys( placeholderKeys, mm.getTranslatedPlaceHolderKeys() );
		}
    	
		Set<String> keys = placeholderKeys.keySet();
		for ( String key : keys) {
			PlaceHolderKey placeHolderKey = placeholderKeys.get( key );
			if ( placeHolderKey.isPrimary() && 
											placeholderKeyContains(placeHolderKey, patterns) ) {
				String placeholder = "{" + placeHolderKey.getKey() + "}";
				
//	    		String key1 = "{" + placeHolderKey.getKey();
//	    		String key2 = "}";
//	    		
//	    		int idx = newFormat.indexOf( key1 );
//	    		if ( idx > -1 && newFormat.indexOf( key2, idx ) > -1 ) {
//	    			
//	    			String identifier = newFormat.substring( idx + 1, newFormat.indexOf( key2, idx ) );
//	    			
//	    		}
				
				String value = null;
				
				if ( mm != null && (placeHolderKey.getPlaceholder().hasFlag( PlaceHolderFlags.MINES ) ||
							placeHolderKey.getPlaceholder().hasFlag( PlaceHolderFlags.PLAYERMINES ))) {
					PlaceholderAttribute attribute = null;
					value = mm.getTranslateMinesPlaceHolder( placeHolderKey, attribute );
				}
				else if ( pm != null && (placeHolderKey.getPlaceholder().hasFlag( PlaceHolderFlags.PLAYER ) || 
							placeHolderKey.getPlaceholder().hasFlag( PlaceHolderFlags.LADDERS ))) {
					value = pm.getTranslatePlayerPlaceHolder( playerUuid, playerName, placeHolderKey, null );
				}
				
				String placeholderAlias = ( placeHolderKey.getAliasName() == null ? null : 
					"{" + placeHolderKey.getAliasName() + "}");
				
				String placeholderResults = String.format( "  &7%s: &3%s", 
									placeholder, 
									(value == null ? "" : value) );
				results.add( placeholderResults );
				
				if ( placeholderAlias != null ) {
					String placeholderResultsAlias = String.format( "    &7Alias:  &7(&b%s&7)", placeholderAlias );
					results.add( placeholderResultsAlias );
				}
			}
			
		}
		
		return results;
	}
	
	private void addAllPlaceHolderKeys( TreeMap<String, PlaceHolderKey> placeholderKeys,
							List<PlaceHolderKey> translatedPlaceHolderKeys ) {
		for ( PlaceHolderKey placeHolderKey : translatedPlaceHolderKeys ) {
			placeholderKeys.put( placeHolderKey.getKey(), placeHolderKey );
		}
	}

	
	private boolean placeholderKeyContains( PlaceHolderKey placeHolderKey, String... patterns ) {
		PrisonPlaceHolders ph = placeHolderKey.getPlaceholder();
		PrisonPlaceHolders phAlias = placeHolderKey.getPlaceholder().getAlias();
		
		return !ph.isAlias() && !ph.isSuppressed() && 
						placeholderKeyContains( ph, placeHolderKey.getKey(), patterns) || 
				  phAlias != null && !phAlias.isSuppressed() && 
				  		placeholderKeyContains( phAlias, placeHolderKey.getAliasName(), patterns)
				  ;

	}

	private boolean placeholderKeyContains( PrisonPlaceHolders placeholder, String key, String... patterns ) {
		boolean results = key != null && patterns != null && patterns.length > 0;
		
		if ( results ) {
			for ( String pattern : patterns ) {
				if ( pattern == null || !key.contains( pattern ) ) {
					results = false;
					break;
				}
			}
		}
		return results;
	}
	
	/**
	 * This forces the PlayerManager to reload the internal mapped placeholder listing. 
	 * This forces the MineManager to reload the internal mapped placeholder listing. 
	 * 
	 */
	public void reloadPlaceholders() {
		
		// Must force Prison to reload the config.yml file:
		Prison.get().getPlatform().reloadConfig();
		
		
		Prison.get().getPlaceholderManager().reloadPlaceholderBarConfig();
    	
    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		if ( pm != null ) {
    			 pm.reloadPlaceholders();
    		}
    	}

    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();
    		if ( mm != null ) {
    			mm.reloadPlaceholders();
    		}
    	}

    	// Force the re-registration of the placeholder integrations:
    	SpigotPrison.getInstance().reloadIntegrationsPlaceholders();
    	
        
    	// Finally, print the placeholder stats:
    	printPlaceholderStats();

	}
	
	
	@Override
	public void printPlaceholderStats() {
        
		Output.get().logInfo( "Total placeholders generated: %d", 
				getPlaceholderCount() );
		
		Map<PlaceHolderFlags, Integer> phDetails = getPlaceholderDetailCounts();
		for ( PlaceHolderFlags key : phDetails.keySet() ) {
			Output.get().logInfo( "  %s: %d", 
					key.name(), phDetails.get( key ) );
			
		}
		
		Output.get().logInfo( "Total placeholders available to be Registered: %d",
				getPlaceholderRegistrationCount() );
        

	}
}

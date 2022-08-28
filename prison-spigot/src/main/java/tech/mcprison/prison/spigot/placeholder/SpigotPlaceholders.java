package tech.mcprison.prison.spigot.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceHolderKey;
import tech.mcprison.prison.placeholders.PlaceholderIdentifier;
import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceholderFlags;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;
import tech.mcprison.prison.placeholders.PlaceholderManagerUtils;
import tech.mcprison.prison.placeholders.PlaceholderStatsData;
import tech.mcprison.prison.placeholders.Placeholders;
import tech.mcprison.prison.placeholders.PlaceholdersStats;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.spigot.SpigotPrison;

public class SpigotPlaceholders
	implements Placeholders {

	private MineManager mm = null;
	private PlayerManager pm = null;
	private RankManager rm = null;
	

	// NOTE: These patterns are from: 
	// https://github.com/PlaceholderAPI/PlaceholderAPI/blob/master/src/main/java/me/clip/placeholderapi/PlaceholderAPI.java
	
	// We want to include the placeholder escape characters in group 1:
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([%]([^%]+)[%])");
	private static final Pattern BRACKET_PLACEHOLDER_PATTERN = Pattern.compile("([{]([^{}]+)[}])");

//	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");
//	private static final Pattern BRACKET_PLACEHOLDER_PATTERN = Pattern.compile("[{]([^{}]+)[}]");
	  
	
	public SpigotPlaceholders() {
		super();
		
	}
	
	
	/**
	 * <p>After the modules have been loaded, then need to initialize the references to the managers that
	 * handles the placeholders.
	 * </p>
	 */
	private void initializePlaceholderManagers() {
		
		if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
			this.mm = PrisonMines.getInstance().getMineManager();
		}
		
		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			
			this.pm = PrisonRanks.getInstance().getPlayerManager();
			
			this.rm = PrisonRanks.getInstance().getRankManager();
		}
	}
	
    
	@Override
    public Map<PlaceholderFlags, Integer> getPlaceholderDetailCounts() {
    	Map<PlaceholderFlags, Integer> placeholderDetails = new TreeMap<>();
    	
    	List<PlaceHolderKey> placeholders = new ArrayList<>();
    	
    	if ( pm != null ) {
    		placeholders.addAll( pm.getTranslatedPlaceHolderKeys() );
    	}
    	
    	if ( rm != null ) {
    		placeholders.addAll( rm.getTranslatedPlaceHolderKeys() );
    	}
    	
    	if ( mm != null ) {
    		placeholders.addAll( mm.getTranslatedPlaceHolderKeys() );
    	}
    	    	
    	for ( PlaceHolderKey phKey : placeholders ) {
			for ( PlaceholderFlags flag : phKey.getPlaceholder().getFlags() ) {

				int count = 0;
				
				if ( placeholderDetails.containsKey( flag ) ) {
					count = placeholderDetails.get( flag );
				}
				
				placeholderDetails.put( flag, Integer.valueOf(count + 1) );
			}
		}

    	return placeholderDetails;
    }
    
    @Override
    public int getPlaceholderCount() {
    	int placeholdersRawCount = 0;
    	
    	if ( pm != null ) {
    		List<PlaceHolderKey> placeholderPlayerKeys = pm.getTranslatedPlaceHolderKeys();
    		placeholdersRawCount += placeholderPlayerKeys.size();
    		
    	}

    	if ( rm != null ) {
    		List<PlaceHolderKey> placeholderPlayerKeys = rm.getTranslatedPlaceHolderKeys();
    		placeholdersRawCount += placeholderPlayerKeys.size();
    		
    	}

    	if ( mm != null ) {
    		List<PlaceHolderKey> placeholderMinesKeys = mm.getTranslatedPlaceHolderKeys();
    		placeholdersRawCount += placeholderMinesKeys.size();
    		
    	}

    	return placeholdersRawCount;
    }
    
    @Override
    public int getPlaceholderRegistrationCount() {
    	int placeholdersRegistered = 0;
    	
    	if ( pm != null ) {
    		List<PlaceHolderKey> placeholderPlayerKeys = pm.getTranslatedPlaceHolderKeys();
    		
    		for ( PlaceHolderKey placeHolderKey : placeholderPlayerKeys ) {
    			if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
    				placeholdersRegistered++;
    			}
    		}
    	}

    	if ( rm != null ) {
    		List<PlaceHolderKey> placeholderPlayerKeys = rm.getTranslatedPlaceHolderKeys();
    		
    		for ( PlaceHolderKey placeHolderKey : placeholderPlayerKeys ) {
    			if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
    				placeholdersRegistered++;
    			}
    		}
    	}

    	if ( mm != null ) {
    		List<PlaceHolderKey> placeholderMinesKeys = mm.getTranslatedPlaceHolderKeys();
    		
    		for ( PlaceHolderKey placeHolderKey : placeholderMinesKeys ) {
    			if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
    				placeholdersRegistered++;
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
     * <p>NOTE: Not sure if this can include a placeholder attribute???
     * </p>
     * 
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
    public String placeholderTranslate( UUID playerUuid, String playerName, String placeholderText ) {
    	PlaceholderIdentifier identifier = new PlaceholderIdentifier( placeholderText );
    	identifier.setPlayer(playerUuid, playerName);
    	
    	return processPlaceholderIdentifier( identifier );
	}

    // NOTE: This is obsolete since the player should always be included:
//    /**
//     * <p>This function is used in this class's placeholderTranslateText() and
//     * also in tech.mcprison.prison.mines.MinesChatHandler.onPlayerChat().
//     * </p>
//     * 
//     */
//    @Override
//    public String placeholderTranslateText( String text) {
//    	PlaceholderIdentifier identifier = new PlaceholderIdentifier( text );
//    	
//    	
//    	return placeholderTranslateText( identifier );
//    }
    	
    private String processPlaceholderIdentifier( PlaceholderIdentifier identifier ) {
    	
    	long nanoStart = System.nanoTime();
    	
    	PlaceholderStatsData stats = PlaceholdersStats.getInstance().getStats( identifier );
    	
    	String results = null;
    	
    	if ( !stats.isFailedMatch() ) {
    		
    		if ( identifier.getPlaceholderKey() == null ) {
    			
    			results = processPlaceholderSearchForPlaceholderKey( identifier );
    		}
    		else {
    			
    			results = processPlaceholderHavePlaceholderKey( identifier );
    		}
    		
    	}

    	long nanoEnd = System.nanoTime();
    	
    	PlaceholdersStats.getInstance().setStats( identifier, stats, nanoStart, nanoEnd );
    	
    	
    	return results == null ? "" : results;
    }
    
    private String processPlaceholderSearchForPlaceholderKey( PlaceholderIdentifier identifier ) {
//		String results = text;

		
    	if ( mm != null ) {
    		
    		List<PlaceHolderKey> placeholderKeys = mm.getTranslatedPlaceHolderKeys();
    		
    		for ( PlaceHolderKey placeHolderKey : placeholderKeys ) {
    			
    			if ( identifier.checkPlaceholderKey( placeHolderKey ) ) {
    				mm.getTranslateMinesPlaceholder( identifier );
    				break;
    			}
    			
    		}
    	}
    	
		
		if ( !identifier.isFoundAMatch() ) {
			
			if ( pm != null ) {
				
				List<PlaceHolderKey> placeholderKeys = pm.getTranslatedPlaceHolderKeys();
				
				for ( PlaceHolderKey placeHolderKey : placeholderKeys ) {
	    			
	    			if ( identifier.checkPlaceholderKey( placeHolderKey ) ) {
	    				pm.getTranslatePlayerPlaceHolder( identifier );
						break;
					}
	    				    			
	    		}
			}
			
			
			if ( !identifier.isFoundAMatch() && rm != null ) {

				List<PlaceHolderKey> placeholderKeys = rm.getTranslatedPlaceHolderKeys();
	    		
	    		for ( PlaceHolderKey placeHolderKey : placeholderKeys ) {
	    			
	    			if ( identifier.checkPlaceholderKey( placeHolderKey ) ) {
	    				rm.getTranslateRanksPlaceHolder( identifier );
						break;
					}
	    			
	    		}
			}
		}
		

		return identifier.getText();
	}
    
    private String processPlaceholderHavePlaceholderKey( PlaceholderIdentifier identifier ) {
    	
    	PlaceHolderKey placeHolderKey = identifier.getPlaceholderKey(); 
    	
    	if ( placeHolderKey != null && placeHolderKey.getPlaceholder() != null ) {
    		
    		if ( mm != null && 
    				(placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.MINES ) ||
    						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.STATSMINES ) ||
    						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.MINEPLAYERS ) ||
    						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.PLAYERBLOCKS ) 
    						)) {
    			
    			if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
    				
    				mm.getTranslateMinesPlaceholder(identifier);
    			}
    			
    		}
    		else if ( pm != null && 
    				(placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.PLAYER ) || 
    						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.LADDERS )
    						)) {
    			
    			if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
    				
    				pm.getTranslatePlayerPlaceHolder( identifier );
    			}
    			
    		}
    		else if ( rm != null && 
    				(placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.RANKS ) || 
    						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.RANKPLAYERS ) ||
    						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.STATSRANKS ) ||
    						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.STATSPLAYERS ) 
    						)) {
    			
    			if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
    				
    				rm.getTranslateRanksPlaceHolder( identifier );
    			}
    			
    		}
    	}
	
		
    	return identifier.getText();
    }
    
//    /**
//     * This provides for a case insensitive replacement of placeholders.
//     * 
//     * String target = "FOOBar";
//	 * target = target.replaceAll("(?i)foo", "");
//     * 
//     * @param text The full text that contains one or more placeholders.  This is also the value that
//     * 				will be returned, with the replacement of the contained placeholder.
//     * @param placeholder The individual placeholder that should be replaced.  This should be the 
//     * 				raw value of the identifier, including escape chaacters and placeholder attributes.
//     * @param target The text that should replace the whole placeholder.
//     * @return
//     */
//    private String placeholderReplace( String text, String placeholder, String target ) {
//    	
//    	return text == null || placeholder == null || target == null ?
//    			text : text.replaceAll( "(?i)" + Pattern.quote(placeholder) , target );
//    }
    
    
    /**
     * <p>The rawText passed to this function may have zero, one, or more placeholders included
     * in the String.  The rawText will mostly contain more than just placeholders, and must be
     * returned.
     * </p>
     * 
     * <p>This function is used in the following locations: </p>
     * <ul>
     * 		<li>
     * 			With the command: /prison placeholders test
     * 		</li>
     * 		<li>
     * 			tech.mcprison.prison.mines.MinesChatHandler.onPlayerChat();
     * 		</li>
     * 		<li>
     * 			The Rank chat handler
     * 		</li>
     * </ul>
     * 
     * @param playerUuid
     * @param text
     * @return
     */
    @Override
	public String placeholderTranslateText(UUID playerUuid, String playerName, String rawText) {
    	String results = rawText;

		// First check for % % placeholders:

		final Matcher matcher = PLACEHOLDER_PATTERN.matcher(results);

		results = replaceAllPlaceholders( playerUuid, playerName, results, matcher );

		final Matcher matcher2 = BRACKET_PLACEHOLDER_PATTERN.matcher(results);
		
		results = replaceAllPlaceholders( playerUuid, playerName, results, matcher2 );
		
		return results;
	}


	private String replaceAllPlaceholders(UUID playerUuid, String playerName, String rawText, final Matcher matcher) {
		String results = rawText;
		
		while (matcher.find()) {
			final String placeholderText = matcher.group(1);

			PlaceholderIdentifier identifier = new PlaceholderIdentifier( placeholderText );
			identifier.setPlayer(playerUuid, playerName);
			
			String replacementText = processPlaceholderIdentifier(identifier);
			if ( identifier.isFoundAMatch() ) {
				results = results.replace( placeholderText, replacementText );
			}
			
		}
		return results;
	}

    
    /**
     * <p>This is used with the command `/prison placeholders search` and the patterns may be any
     * fragment of a placeholder, and any number of them.\
     * </p>
     * 
     */
	@Override
	public List<String> placeholderSearch( UUID playerUuid, String playerName, String[] patterns )
	{
		List<String> results = new ArrayList<>();


		
		TreeMap<String, PlaceHolderKey> placeholderKeys = new TreeMap<>();
		
		PlayerManager pm = null;
		RankManager rm = null;
		MineManager mm = null;
		
		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			pm = PrisonRanks.getInstance().getPlayerManager();
			addAllPlaceHolderKeys( placeholderKeys, pm.getTranslatedPlaceHolderKeys() );
		}
		
		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			rm = PrisonRanks.getInstance().getRankManager();
			addAllPlaceHolderKeys( placeholderKeys, rm.getTranslatedPlaceHolderKeys() );
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
				
				PlaceholderIdentifier identifier = new PlaceholderIdentifier( placeholder );
				identifier.setPlayer(playerUuid, playerName);
				identifier.setPlaceholderKey(placeHolderKey);
				
				
				String value = processPlaceholderHavePlaceholderKey( identifier );
				
				// Note: STATSMINES will not work here since the sequence is not being addressed.
				
//				if ( mm != null && (placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.MINES ) ||
//							placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.STATSMINES ) ||
//							placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.MINEPLAYERS ) ||
//							placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.PLAYERBLOCKS ) 
//						)) {
//					if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
//						
//						value = mm.getTranslateMinesPlaceholder(identifier);
//					}
//
//				}
//				else if ( pm != null && (placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.PLAYER ) || 
//							placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.LADDERS ))) {
//					
//					if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
//						
//						value = pm.getTranslatePlayerPlaceHolder( identifier );
//					}
//					
//				}
//				else if ( rm != null && 
//						(placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.RANKS ) || 
//						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.RANKPLAYERS ) ||
//						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.STATSRANKS ) ||
//						placeHolderKey.getPlaceholder().hasFlag( PlaceholderFlags.STATSPLAYERS ) ) ) {
//					
//					if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
//						
//						value = rm.getTranslateRanksPlaceHolder( identifier );
//					}
//					
//				}
				
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
		
		
		PlaceholderManagerUtils.getInstance().reloadPlaceholderBarConfig();
		
		initializePlaceholderManagers();
    	
    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		if ( pm != null ) {
    			 pm.reloadPlaceholders();
    		}
    		
    		if ( rm != null ) {
    			rm.reloadPlaceholders();
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
		
		Map<PlaceholderFlags, Integer> phDetails = getPlaceholderDetailCounts();
		for ( PlaceholderFlags key : phDetails.keySet() ) {
			Output.get().logInfo( "  %s: %d", 
					key.name(), phDetails.get( key ) );
			
		}
		
		Output.get().logInfo( "Total placeholders available to be Registered: %d",
				getPlaceholderRegistrationCount() );
        

	}
}

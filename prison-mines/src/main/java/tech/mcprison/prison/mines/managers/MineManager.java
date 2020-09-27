/*
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.mines.managers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.integration.IntegrationManager.PlaceHolderFlags;
import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;
import tech.mcprison.prison.integration.ManagerPlaceholders;
import tech.mcprison.prison.integration.PlaceHolderKey;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

/**
 * Manages the creation, removal, and management of mines.
 *
 * @author Dylan M. Perks
 */
public class MineManager 
	implements ManagerPlaceholders {

	public static final double TIME_SECOND = 1.0;
	public static final double TIME_MINUTE = TIME_SECOND * 60.0;
	public static final double TIME_HOUR = TIME_MINUTE * 60.0;
	public static final double TIME_DAY = TIME_HOUR * 24.0;
	
	
    // Base list
    private List<Mine> mines;
    private TreeMap<String, Mine> minesByName;
    
    private TreeMap<String, List<Mine>> unavailableWorlds;

    private Collection coll;

    private List<PlaceHolderKey> translatedPlaceHolderKeys;
    
    private boolean mineStats = false;

    /**
     * <p>MineManager must be fully instantiated prior to trying to load the mines,
     * otherwise if the mines cannot find the world they should be, they will be
     * unable to register that the world is unavailable.
     * </p>
     * 
     */
    public MineManager() {
    	this.mines = new ArrayList<>();
    	this.minesByName = new TreeMap<>();
    	
    	this.unavailableWorlds = new TreeMap<>();
    	
    	this.coll = null;
    	
    }
    

    public void loadFromDbCollection( PrisonMines pMines ) {
        
        Optional<Collection> collOptional = pMines.getDb().getCollection("mines");

        if (!collOptional.isPresent()) {
        	Output.get().logError("Could not create 'mines' collection.");
        	pMines.getStatus().toFailed("Could not create mines collection in storage.");
        	return;
        }

        this.coll = collOptional.get();

        int offsetTiming = 5;
        loadMines(offsetTiming);
        

        Output.get().logInfo( String.format("Loaded %d mines and submitted with a %d " +
        		"second offset timing for auto resets.", 
        			getMines().size(), offsetTiming));
        
        
//        // When finished loading the mines, then if there are any worlds that
//        // could not be loaded, dump the details:
//        List<String> unavailableWorlds = getUnavailableWorldsListings();
//        for ( String uWorld : unavailableWorlds ) {
//			Output.get().logInfo( uWorld );
//		}
        
        
//        // Submit all the loaded mines to run:
//        int offset = 0;
//        for ( Mine mine : mines )
//		{
//			mine.submit(offset);
//			offset += 5;
//		}
//        Output.get().logInfo("Mines are all queued to run auto resets.");
    }

//    public void loadMine(String mineFile) throws IOException, MineException {
//        Document document = coll.get(mineFile).orElseThrow(IOException::new);
//        Mine m = new Mine(document);
//        add(m, false, 0);
//    }

    /**
     * Adds a {@link Mine} to this {@link MineManager} instance.
     * 
     * Also saves the mine to the file system.
     *
     * @param mine the mine instance
     * @return if the add was successful
     */
    public boolean add(Mine mine) {
    	return add(mine, true, 0);
    }
    
    /**
     * Adds a {@link Mine} to this {@link MineManager} instance.
     * 
     * Also saves the mine to the file system.
     *
     * @param mine the mine instance
     * @param save - bypass the option to save. Useful for when initially loading the mines since
     *               no data has changed.
     * @return if the add was successful
     */
    private boolean add(Mine mine, boolean save, int offsetTiming ) {
    	boolean results = false;
        if (!getMines().contains(mine)){
        	if ( save ) {
        		saveMine( mine );
        	}
        	
            results = getMines().add(mine);
            getMinesByName().put( mine.getName().toLowerCase(), mine );
            
            // Start its scheduling:
            mine.submit(offsetTiming);
        }
        return results;
    }


    public boolean removeMine(String mineName){
    	boolean results = false;
    	if ( mineName != null ) {
    		Mine mine = getMinesByName().get( mineName.toLowerCase() );
    		if ( mine != null ) {
    			results = removeMine(mine);
    		}
    	}
    	
    	return results;
    }

    public boolean removeMine(Mine mine) {
    	boolean success = false;
    	if ( mine != null ) {
    		coll.delete( mine.getName() );
    		getMinesByName().remove(mine.getName());
    		success = getMines().remove(mine);
    	}
	    return success;
    }



    private void loadMines( int offsetTiming ) {
        List<Document> mineDocuments = coll.getAll();

        int offset = 0;
        for (Document document : mineDocuments) {
            try {
                Mine m = new Mine(document);
                add(m, false, offset);
                offset += offsetTiming;
                
            } catch (Exception e) {
                Output.get()
                    .logError("&cFailed to load mine " + document.getOrDefault("name", "null"), e);
            }
        }
        
    }

    /**
     * Saves the specified mine. This should only be used for the instance created by {@link
     * PrisonMines}
     */
    public void saveMine(Mine mine) {
        coll.save(mine.toDocument());
    }

    public void saveMines(){
        for (Mine m : getMines()){
            saveMine(m);
        }
    }


    /**
     * Returns the mine with the specified name.
     *
     * @param name The mine's name, case-sensitive.
     * @return An optional containing either the {@link Mine} if it could be found, or empty if it
     * does not exist by the specified name.
     */
    public Mine getMine(String mineName) {
    	return (mineName == null ? null : getMinesByName().get( mineName.toLowerCase() ));
    	
        //return mines.stream().filter(mine -> mine.getName().equals(name)).findFirst();
    }

    public List<Mine> getMines() {
        return mines;
    }

	public TreeMap<String, Mine> getMinesByName() {
		return minesByName;
	}

	public boolean isMineStats()
	{
		return mineStats;
	}
	public void setMineStats( boolean mineStats )
	{
		this.mineStats = mineStats;
	}
	
	

	/**
	 * <p>Add the missing world and the associated mine to the collection. Create the
	 * base entries if needed.
	 * </p>
	 * 
	 * <p>Upon the first entry in to this collection of worlds and mines, an error
	 * message will be generated indicating the world does not exist.
	 * </p>
	 * 
	 * @param worldName
	 * @param mine
	 */
	public void addUnavailableWorld( String worldName, Mine mine ) {
		if ( worldName != null && worldName.trim().length() > 0 && mine != null ) {
			if ( !getUnavailableWorlds().containsKey( worldName )) {
				getUnavailableWorlds().put( worldName, new ArrayList<>() );
				
				Output.get().logWarn( "&7Mine Loader: &aWorld does not exist! " +
						"&7This maybe a temporary " +
	            		"condition until the world can be loaded. " +
	            		" &3worldName= " + worldName );
			}

			if ( !getUnavailableWorlds().get( worldName ).contains( mine )) {
				getUnavailableWorlds().get( worldName ).add( mine );
			}
		}
	}
    public TreeMap<String, List<Mine>> getUnavailableWorlds() {
		return unavailableWorlds;
	}
	public void setUnavailableWorlds( TreeMap<String, List<Mine>> unavailableWorlds ) {
		this.unavailableWorlds = unavailableWorlds;
	}

	public void assignAvailableWorld( String worldName ) {
    	if ( worldName != null && worldName.trim().length() > 0 ) {
    		
    		Optional<World> worldOptional = Prison.get().getPlatform().getWorld(worldName);
    		
    		if ( worldOptional.isPresent() && getUnavailableWorlds().containsKey( worldName )) {
    			World world = worldOptional.get();
    			
    			// Store this mine and the world in MineManager's unavailableWorld for later
    			// processing and hooking up to the world object.
    			List<Mine> unenabledMines = getUnavailableWorlds().get( worldName );

//    			List<Mine> remove = new ArrayList<>();
    			
    			for ( Mine mine : unenabledMines ) {
    				if ( !mine.isEnabled() ) {
    					mine.setWorld( world );
    				}
//    				remove.add( mine );
    			}
    			
//    			// Purge all removed mines from the unenabledMines list:
//    			if ( remove.size() > 0 ) {
//    				unenabledMines.removeAll( remove );
//    			}
    			
//    			// If no mines remain, then remove this world from unavailableWorlds:
//    			if ( unenabledMines.size() == 0 ) {
//    				getUnavailableWorlds().remove( worldName );
//    			}
    			
    			// Since the world is loaded and all available mines have been hooked up
    			// with the world, so remove these entries.
    			unenabledMines.clear();
    			getUnavailableWorlds().remove( worldName );
    		}
    	}
	}
	
    public List<String> getUnavailableWorldsListings() {
    	List<String> results = new ArrayList<>();
    	
    	if ( getUnavailableWorlds().size() > 0 ) {
    		results.add( "&cUnavailable Worlds: &3Deferred loading of mines." );

    		Set<String> worlds = getUnavailableWorlds().keySet();
    		
    		for ( String worldName : worlds ) {
    			int enabledCount = 0;
    			
				List<Mine> mines = getUnavailableWorlds().get( worldName );
				for ( Mine mine : mines ) {
					if ( mine.isEnabled() ) {
						enabledCount++;
					}
				}
				results.add( 
						String.format( "&7    world: &3%s &7(&c%s &7of &c%s &7mines enabled) ", 
								worldName, Integer.toString( enabledCount ),
								Integer.toString( mines.size() )));
			}
    	}
    	
    	return results;
    }
	
	public String getTranslateMinesPlaceHolder( String identifier ) {
    	String results = null;
    	List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    	
		if ( !identifier.startsWith( IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED )) {
			identifier = IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
		}
    	
    	for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
			if ( placeHolderKey.getKey().equalsIgnoreCase( identifier )) {
				
				Mine mine = getMine( placeHolderKey.getData() );
				
				results = getTranslateMinesPlaceHolder( placeHolderKey, mine );
				break;
			}
		}
    	
    	return results;
    }
    
	public String getTranslateMinesPlaceHolder( PlaceHolderKey placeHolderKey ) {
		Mine mine = getMine( placeHolderKey.getData() );
		return getTranslateMinesPlaceHolder( placeHolderKey, mine );
	}
	
    private String getTranslateMinesPlaceHolder( PlaceHolderKey placeHolderKey, Mine mine ) {
		String results = null;

		if ( placeHolderKey != null && placeHolderKey.getData() != null ) {

			// If the mine is not provided, try to get it from the placeholder data: 
			if ( mine == null ) {
				mine = getMine( placeHolderKey.getData() );
			}

			if ( mine != null ) {
				DecimalFormat dFmt = new DecimalFormat("#,##0.00");
				DecimalFormat iFmt = new DecimalFormat("#,##0");
				
				switch ( placeHolderKey.getPlaceholder() ) {
					case prison_mi_minename:
					case prison_mines_interval_minename:
					case prison_mi_pm:
					case prison_mines_interval_playermines:
						results = iFmt.format( mine.getResetTime() );
						break;
						
					case prison_mif_minename:
					case prison_mines_interval_formatted_minename:
					case prison_mif_pm:
					case prison_mines_interval_formatted_playermines:
						double timeMif = mine.getResetTime();
						results = formattedTime( timeMif );
						break;
						
					case prison_mtl_minename:
					case prison_mines_timeleft_minename:
					case prison_mtl_pm:
					case prison_mines_timeleft_playermines:
						// NOTE: timeleft can vary based upon server loads:
						results = dFmt.format( mine.getRemainingTimeSec() );
						break;
						
					case prison_mtlb_minename:
					case prison_mines_timeleft_bar_minename:
					case prison_mtlb_pm:
					case prison_mines_timeleft_bar_playermines:
						// NOTE: timeleft can vary based upon server loads:
						
						results = getRemainingTimeBar( mine );
						break;
						
					case prison_mtlf_minename:
					case prison_mines_timeleft_formatted_minename:
					case prison_mtlf_pm:
					case prison_mines_timeleft_formatted_playermines:
						// NOTE: timeleft can vary based upon server loads:
						double timeMtlf = mine.getRemainingTimeSec();
						results = formattedTime( timeMtlf );
						break;
						
					case prison_ms_minename:
					case prison_mines_size_minename:
					case prison_ms_pm:
					case prison_mines_size_playermines:
						results = iFmt.format( mine.getBounds().getTotalBlockCount() );
						break;
						
					case prison_mr_minename:
					case prison_mines_remaining_minename:
					case prison_mr_pm:
					case prison_mines_remaining_playermines:
						int remainingBlocks = mine.getRemainingBlockCount();
						results = iFmt.format( remainingBlocks );
						break;
						
					case prison_mrb_minename:
					case prison_mines_remaining_bar_minename:
					case prison_mrb_pm:
					case prison_mines_remaining_bar_playermines:
						int totalBlocks = mine.getBounds().getTotalBlockCount();
						int blocksRemaining = mine.getRemainingBlockCount();
						
						results = Prison.get().getIntegrationManager().
									getProgressBar( ((double) blocksRemaining), ((double) totalBlocks), false );
						break;
						
					case prison_mp_minename:
					case prison_mines_percent_minename:
					case prison_mp_pm:
					case prison_mines_percent_playermines:
						// mine.refreshAirCount(); // async & delayed : Very high cost
						double percentRemaining = mine.getPercentRemainingBlockCount();
						results = dFmt.format( percentRemaining );
						break;
						
					case prison_mpc_minename:
					case prison_mines_player_count_minename:
					case prison_mpc_pm:
					case prison_mines_player_count_playermines:
						results = iFmt.format( mine.getPlayerCount() );
						break;
						
					case prison_mbm_minename:
					case prison_mines_blocks_mined_minename:
					case prison_mbm_pm:
					case prison_mines_blocks_mined_playermines:
						results = iFmt.format( mine.getTotalBlocksMined() );
						break;
						
					case prison_mrc_minename:
					case prison_mines_reset_count_minename:
					case prison_mrc_pm:
					case prison_mines_reset_count_playermines:
						results = iFmt.format( mine.getResetCount() );
						break;
						
					default:
						break;
				}
			}
			
		}
		
		return results;
    }
    
    public String getTranslatePlayerMinesPlaceHolder( UUID playerUuid, String playerName, String identifier ) {
    	String results = null;

    	if ( playerUuid != null ) {
    		
    		List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    		
    		if ( !identifier.startsWith( IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED )) {
    			identifier = IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
    		}
    		
    		for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
    			if ( placeHolderKey.getKey().equalsIgnoreCase( identifier )) {
    				results = getTranslatePlayerMinesPlaceHolder( playerUuid, playerName, placeHolderKey );
    				break;
    			}
    		}
    	}
    	
    	return results;
    }
    
    /**
     * We must have a player to process these placeholders.
     * 
     * @param playerUuid
     * @param playerName
     * @param placeHolderKey
     * @return
     */
    public String getTranslatePlayerMinesPlaceHolder( UUID playerUuid, String playerName, PlaceHolderKey placeHolderKey ) {
		String results = null;

		if ( playerUuid != null ) {
			
			// there is no data stored for PLAYERMINES:
//			String data = placeHolderKey.getData();
			
			Player player = getPlayer(playerUuid, playerName);
			
			if ( player != null ) {
				Mine mine = PrisonMines.getInstance().findMineLocation( player );
				
				if ( mine != null ) {
					results = getTranslateMinesPlaceHolder( placeHolderKey, mine );
				}
			}
		}
		
		return results;
    }
    

    /**
     * Get the Player based first on UUID, then on name. 
     * 
     * @param playerUuid
     * @param playerName
     * @return
     */
    private Player getPlayer( UUID playerUuid, String playerName ) {
    	Player player = null;
    	Player playerAlt = null;
    	
    	// First try to match on UUID
		for ( Player p : Prison.get().getPlatform().getOnlinePlayers() ) {
			if ( p.getUUID().compareTo( playerUuid ) == 0 ) {
				player = p;
				break;
			} 
			else if ( p.getName().equalsIgnoreCase( playerName ) ) {
				// If we get a hit on the name, save it as an alt...
				playerAlt = p;
			}
		}
		
		// if player is null, and we have a playerAlt, then use it:
		if ( player == null && playerAlt != null ) {
			player = playerAlt;
		}
		return player;
	}


	private String getRemainingTimeBar( Mine mine ) {

    	double timeRemaining = mine.getRemainingTimeSec();
    	int time = mine.getResetTime();
    	
    	return Prison.get().getIntegrationManager().
    					getProgressBar( timeRemaining, ((double) time), true );
	}


	private String formattedTime( double time ) {
    	StringBuilder sb = new StringBuilder();
    	
    	long days = (long)(time / TIME_DAY);
    	time -= (days * TIME_DAY);
    	if ( days > 0 ) {
    		sb.append( days );
    		sb.append( "d " );
    	}
    	
    	long hours = (long)(time / TIME_HOUR);
    	time -= (hours * TIME_HOUR);
    	if ( sb.length() > 0 || hours > 0 ) {
    		sb.append( hours );
    		sb.append( "h " );
    	}
    	
    	long mins = (long)(time / TIME_MINUTE);
    	time -= (mins * TIME_MINUTE);
    	if ( sb.length() > 0 || mins > 0 ) {
    		sb.append( mins );
    		sb.append( "m " );
    	}
    	
    	double secs = (double)(time / TIME_SECOND);
    	time -= (secs * TIME_SECOND);
    	DecimalFormat dFmt = new DecimalFormat("#0");
    	sb.append( dFmt.format( secs ));
    	sb.append( "s " );
    	
		return sb.toString();
	}


	/**
     * <p>Generates a list of all of the placeholder keys, which includes the
     * mine name, the placeholder enumeration, and then the actual translated
     * placeholder with the mine's name. This is generated only once, but if new
     * mines are added or mines removed, then just set the class variable to
     * null to auto regenerate with the new values. 
     * </p>
     */
    @Override
    public List<PlaceHolderKey> getTranslatedPlaceHolderKeys() {
    	if ( translatedPlaceHolderKeys == null ) {
    		translatedPlaceHolderKeys = new ArrayList<>();
    		
    		List<PrisonPlaceHolders> placeHolders = 
    				PrisonPlaceHolders.getTypes( PlaceHolderFlags.MINES );
//    				PrisonPlaceHolders.excludeTypes( 
//    					PrisonPlaceHolders.getTypes( PlaceHolderFlags.MINES ),
//    						PlaceHolderFlags.PLAYERMINES);
    		
    		for ( Mine mine : getMines() ) {
    			for ( PrisonPlaceHolders ph : placeHolders ) {
    				String key = ph.name().replace( 
    						IntegrationManager.PRISON_PLACEHOLDER_MINENAME_SUFFIX, "_" + mine.getName() ).
    						toLowerCase();
    				
    				PlaceHolderKey placeholder = new PlaceHolderKey(key, ph, mine.getName() );
    				if ( ph.getAlias() != null ) {
    					String aliasName = ph.getAlias().name().replace( 
    							IntegrationManager.PRISON_PLACEHOLDER_MINENAME_SUFFIX, "_" + mine.getName() ).
    							toLowerCase();
    					placeholder.setAliasName( aliasName );
    				}
    				translatedPlaceHolderKeys.add( placeholder );
    				
    				// Getting too many placeholders... add back the extended prefix when looking up:

//    				// Now generate a new key based upon the first key, but without the prison_ prefix:
//    				String key2 = key.replace( 
//    						IntegrationManager.PRISON_PLACEHOLDER_PREFIX + "_", "" );
//    				PlaceHolderKey placeholder2 = new PlaceHolderKey(key2, ph, mine.getName(), false );
//    				translatedPlaceHolderKeys.add( placeholder2 );
    				
    			}
    		}
    		
    		
    		// Next we need to register all the PLAYERMINES.  The mines are dynamic, based upon which one
    		// the player is in.  So this is just a simple registration.
    		placeHolders = PrisonPlaceHolders.getTypes( PlaceHolderFlags.PLAYERMINES );
    		
			for ( PrisonPlaceHolders ph : placeHolders ) {
				String key = ph.name();
				
				PlaceHolderKey placeholder = new PlaceHolderKey(key, ph );
				if ( ph.getAlias() != null ) {
					String aliasName = ph.getAlias().name();
					placeholder.setAliasName( aliasName );
				}
				translatedPlaceHolderKeys.add( placeholder );
			}

    	}
    	return translatedPlaceHolderKeys;
    }

    /**
     * <p>If new
     * mines are added or mines removed, then just set the class variable to
     * null to auto regenerate with the new values. 
     * </p>
     */
    public void resetTranslatedPlaceHolderKeys() {
    	translatedPlaceHolderKeys = null;
    }
    
    
    @Override
    public void reloadPlaceholders() {
    	
    	// clear the class variable so they will regenerate:
    	translatedPlaceHolderKeys = null;
    	
    	// Regenerate the translated placeholders:
    	getTranslatedPlaceHolderKeys();
    }

}

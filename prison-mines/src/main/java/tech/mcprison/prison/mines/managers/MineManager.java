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
import java.util.TreeMap;

import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.integration.IntegrationManager.PlaceHolderFlags;
import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;
import tech.mcprison.prison.integration.ManagerPlaceholders;
import tech.mcprison.prison.integration.PlaceHolderKey;
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

    // Base list
    private List<Mine> mines;
    private TreeMap<String, Mine> minesByName;

    private Collection coll;

    private List<PlaceHolderKey> translatedPlaceHolderKeys;
    
    private boolean mineStats = false;

    /**
     * Initializes a new instance of {@link MineManager}
     */
    public MineManager(tech.mcprison.prison.store.Collection collection) {
        this.mines = new ArrayList<>();
        this.minesByName = new TreeMap<>();
        
        this.coll = collection;

        int offsetTiming = 5;
        loadMines(offsetTiming);

        Output.get().logInfo( String.format("Loaded %d mines and submitted with a %d " +
        		"second offset timing for auto resets.", 
        			getMines().size(), offsetTiming));
        
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

    public static MineManager fromDb() {
    	PrisonMines pMines = PrisonMines.getInstance();
    	
        Optional<Collection> collOptional = pMines.getDb().getCollection("mines");

        if (!collOptional.isPresent()) {
        	Output.get().logError("Could not create 'mines' collection.");
        	pMines.getStatus().toFailed("Could not create mines collection in storage.");
        	return null;
        }

        return new MineManager(collOptional.get());
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

	
    public String getTranslateMinesPlaceHolder( String identifier ) {
    	String results = null;
    	List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    	
    	for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
			if ( placeHolderKey.getKey().equalsIgnoreCase( identifier )) {
				results = getTranslateMinesPlaceHolder( placeHolderKey );
				break;
			}
		}
    	
    	return results;
    }
    
    public String getTranslateMinesPlaceHolder( PlaceHolderKey placeHolderKey ) {
		String results = null;

		if ( placeHolderKey != null && placeHolderKey.getData() != null ) {
			Mine mine = getMine( placeHolderKey.getData() );

			if ( mine != null ) {
				DecimalFormat dFmt = new DecimalFormat("#,##0.00");
				DecimalFormat iFmt = new DecimalFormat("#,##0");
				
				switch ( placeHolderKey.getPlaceholder() ) {
					case prison_mi_minename:
					case prison_mines_interval_minename:
						results = iFmt.format( mine.getResetTime() );
						break;
						
					case prison_mtl_minename:
					case prison_mines_timeleft_minename:
						// NOTE: timeleft can vary based upon server loads:
						long targetResetTime = mine.getTargetRestTime();
						double remaining = ( targetResetTime <= 0 ? 0d : 
							(targetResetTime - System.currentTimeMillis()) / 1000d);
						results = dFmt.format( remaining );
						break;
						
					case prison_ms_minename:
					case prison_mines_size_minename:
						results = iFmt.format( mine.getBounds().getTotalBlockCount() );
						break;
						
					case prison_mr_minename:
					case prison_mines_remaining_minename:
						mine.refreshAirCount(); // async & delayed : Very high cost
						int remainingBlocks = mine.getRemainingBlockCount();
						results = iFmt.format( remainingBlocks );
						break;
						
					case prison_mp_minename:
					case prison_mines_percent_minename:
						mine.refreshAirCount(); // async & delayed : Very high cost
						double percentRemaining = mine.getPercentRemainingBlockCount();
						results = dFmt.format( percentRemaining );
						break;
						
					case prison_mpc_minename:
					case prison_mines_player_count_minename:
						results = iFmt.format( mine.getPlayerCount() );
						break;
						
					// Temp to test the onBlockBreak event handling:
					case prison_mrt_minename:
					case prison_mines_remaining_temp_minename:
						mine.refreshAirCount(); // async & delayed : Very high cost
						int remainingBlocksTemp = mine.getBounds().getTotalBlockCount() - mine.getBlockBreakCount();
						results = iFmt.format( remainingBlocksTemp );
						break;
						
						
					default:
						break;
				}
			}
			
		}
		
		return results;
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
    		
    		List<PrisonPlaceHolders> placeHolders = PrisonPlaceHolders.getTypes( PlaceHolderFlags.MINES );
    		
    		for ( Mine mine : getMines() ) {
    			for ( PrisonPlaceHolders ph : placeHolders ) {
    				String key = ph.name().replace( 
    						IntegrationManager.PRISON_PLACEHOLDER_MINENAME_SUFFIX, "_" + mine.getName() );
    				PlaceHolderKey placeholder = new PlaceHolderKey(key, ph, mine.getName() );
    				translatedPlaceHolderKeys.add( placeholder );
    				
    				// Now generate a new key based upon the first key, but without the prison_ prefix:
    				String key2 = key.replace( 
    						IntegrationManager.PRISON_PLACEHOLDER_PREFIX + "_", "" );
    				PlaceHolderKey placeholder2 = new PlaceHolderKey(key2, ph, mine.getName() );
    				translatedPlaceHolderKeys.add( placeholder2 );
    				
    			}
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

}

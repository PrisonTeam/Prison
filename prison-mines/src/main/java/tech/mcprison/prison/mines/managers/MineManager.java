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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.integration.IntegrationManager.PlaceHolderFlags;
import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;
import tech.mcprison.prison.integration.ManagerPlaceholders;
import tech.mcprison.prison.integration.PlaceHolderKey;
import tech.mcprison.prison.mines.MineException;
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

    private Collection coll;

    private List<PlaceHolderKey> translatedPlaceHolderKeys;
    
    private boolean mineStats = false;

    /**
     * Initializes a new instance of {@link MineManager}
     */
    public MineManager(tech.mcprison.prison.store.Collection collection) {
        mines = new ArrayList<>();
        coll = collection;

        loadMines();

        Output.get().logInfo("Loaded " + mines.size() + " mines and starting to submit to run: ");
        
        // Submit all the loaded mines to run:
        int offset = 0;
        for ( Mine mine : mines )
		{
			mine.submit(offset);
			offset += 5;
		}
        Output.get().logInfo("Mines are all queued to run auto resets.");
    }

    public void loadMine(String mineFile) throws IOException, MineException {
        Document document = coll.get(mineFile).orElseThrow(IOException::new);
        mines.add(new Mine(document));
    }

    /**
     * Adds a {@link Mine} to this {@link MineManager} instance.
     * 
     * Also saves the mine to the file system.
     *
     * @param mine the mine instance
     * @return if the add was successful
     */
    public boolean add(Mine mine) {
    	return add(mine, true);
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
    public boolean add(Mine mine, boolean save) {
    	boolean results = false;
        if (!mines.contains(mine)){
        	if ( save ) {
        		saveMine( mine );
        	}
        	
            results = mines.add(mine);
            
            // Start its scheduling:
            mine.submit(0);
        }
        return results;
    }


    public boolean removeMine(String id){
        if (getMine(id).isPresent()) {
            return removeMine(getMine(id).get());
        }
        else{
            return false;
        }
    }

    public boolean removeMine(Mine mine) {
    	boolean success = false;
    	if ( mine != null ) {
    		mines.remove(mine);
    		success = coll.delete( mine.getName() );
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

    private void loadMines() {
        List<Document> mineDocuments = coll.getAll();

        for (Document document : mineDocuments) {
            try {
                Mine m = new Mine(document);
                add(m, false);
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
        for (Mine m : mines){
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
    public Optional<Mine> getMine(String name) {
        return mines.stream().filter(mine -> mine.getName().equals(name)).findFirst();
    }

    public List<Mine> getMines() {
        return mines;
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
			Optional<Mine> mineOptional = getMine( placeHolderKey.getData() );
			Mine mine = mineOptional.isPresent() ? mineOptional.get() : null;

			if ( mine != null ) {
				DecimalFormat dFmt = new DecimalFormat("#,##0.00");
				DecimalFormat iFmt = new DecimalFormat("#,##0");
				
				switch ( placeHolderKey.getPlaceholder() ) {
					case prison_mines_interval_minename:
					case mines_interval_minename:
						results = iFmt.format( mine.getResetTime() );
						break;
						
					case prison_mines_timeleft_minename:
					case mines_timeleft_minename:
						// NOTE: timeleft can vary based upon server loads:
						long targetResetTime = mine.getTargetRestTime();
						double remaining = ( targetResetTime <= 0 ? 0d : 
							(targetResetTime - System.currentTimeMillis()) / 1000d);
						results = dFmt.format( remaining );
						break;
						
					case prison_mines_size_minename:
					case mines_size_minename:
						results = iFmt.format( mine.getBounds().getTotalBlockCount() );
						break;
						
					case prison_mines_remaining_minename:
					case mines_remaining_minename:
						mine.refreshAirCount(); // async & delayed : Very high cost
						int remainingBlocks = mine.getBounds().getTotalBlockCount() - mine.getAirCount();
						results = iFmt.format( remainingBlocks );
						break;
						
					case prison_mines_percent_minename:
					case mines_percent_minename:
						mine.refreshAirCount(); // async & delayed : Very high cost
						int totalCount = mine.getBounds().getTotalBlockCount();
						double remainingBlocksP = (totalCount - mine.getAirCount()) * 100d;
						double originalCount = totalCount - mine.getAirCountOriginal();
						double percentRemaining = (originalCount == 0d ? 0d : remainingBlocksP / originalCount);
						results = dFmt.format( percentRemaining );
						break;
						
					case prison_mines_player_count_minename:
					case mines_player_count_minename:
						results = iFmt.format( mine.getPlayerCount() );
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

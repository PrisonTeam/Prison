/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2017 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.mines;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.convert.ConversionManager;
import tech.mcprison.prison.error.ErrorManager;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.mines.commands.MinesCommands;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.MinesConfig;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.mines.managers.PlayerManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Database;
import tech.mcprison.prison.util.Location;

/**
 * The Prison 3 Mines Module
 *
 * @author The MC-Prison Team
 */
public class PrisonMines extends Module {
	public static final String MODULE_NAME = "Mines";

    private static PrisonMines i = null;
    private MinesConfig config;
//    private List<String> worlds;
    private LocaleManager localeManager;
    private Database db;
    private ErrorManager errorManager;
    
    private JsonFileIO jsonFileIO;

    private MineManager mineManager;
    private PlayerManager player;


    /**
     * <p>playerCache tries to provide a faster way to identify which mine a player is
     * in. The theory is that there is a very high chance it will be the last mine 
     * they were in.  So this records the last mine they were in, and if that is not
     * where they are, then, and only then, do we check all mines to see if it may
     * be something else.
     * </p>
     * 
     */
	private final TreeMap<Long, Mine> playerCache;

	
    
    public PrisonMines(String version) {
        super(MODULE_NAME, version, 3);

    	this.playerCache = new TreeMap<>();
    }

    public static PrisonMines getInstance() {
        return i;
    }

    @Override
    public String getBaseCommands() {
    	return "&7/&2mines";
    }
    
    public void enable() {
        i = this;

        this.errorManager = new ErrorManager(this);
        this.jsonFileIO = new JsonFileIO( errorManager, getStatus() );
        
        initDb();
        initConfig();
        this.localeManager = new LocaleManager(this, "lang/mines");

//        initWorlds();
        
        this.mineManager = new MineManager();
        getMineManager().loadFromDbCollection(this);
        
        player = new PlayerManager();
        
//        initMines();
        PrisonAPI.getEventBus().register(new MinesListener());

        Prison.get().getCommandHandler().registerCommands(new MinesCommands());
        //Prison.get().getCommandHandler().registerCommands(new PowertoolCommands());

        ConversionManager.getInstance().registerConversionAgent(new MinesConversionAgent());
    }


    private void initDb() {
        Optional<Database> dbOptional =
            Prison.get().getPlatform().getStorage().getDatabase("mines");

        if (!dbOptional.isPresent()) {

            Prison.get().getPlatform().getStorage().createDatabase("mines");
            dbOptional = Prison.get().getPlatform().getStorage().getDatabase("mines");

            if (!dbOptional.isPresent()) {
                Output.get().logError("Could not load the mines database.");
                getStatus().toFailed("Could not load storage database.");
                return;
            }
        }

        this.db = dbOptional.get();
    }

    private void initConfig() {
        config = new MinesConfig();

        File configFile = new File(getDataFolder(), "config.json");
        
        if (!configFile.exists()) {
        	getJsonFileIO().saveJsonFile( configFile, config );
        } else {
        	config = (MinesConfig) getJsonFileIO().readJsonFile( configFile, config );
        }
        
    }

    
    
    
    /**
     * <p>Search all mines to find if the given block is located within any
     * of the mines. If not, then return a null.
     * </p>
     * 
     * @param block
     * @return
     */
	public Mine findMineLocation( Location locationToCheck ) {
		Mine mine = null;
		for ( Mine m : getMines() ) {
			if ( m.isInMine( locationToCheck ) ) {
				mine = m;
				break;
			}
		}
		return mine;
	}

	public TreeMap<Long, Mine> getPlayerCache() {
		return playerCache;
	}
	
	public Mine findMineLocation( Player player ) {
		Mine result = null;
		
		Long playerUUIDLSB = Long.valueOf( player.getUUID().getLeastSignificantBits() );
		
		// Get the cached mine, if it exists:
		Mine mine = getPlayerCache().get( playerUUIDLSB );
		
		if ( mine == null || !mine.isInMine( player.getLocation() ) ) {
			// Look for the correct mine to use. 
			// Set mine to null so if cannot find the right one it will return a null:
			mine = findMineLocation( player.getLocation() );
			
			// Store the mine in the player cache if not null:
			if ( mine != null ) {
				getPlayerCache().put( playerUUIDLSB, mine );
			}
		}

		return result;
	}

//    private void initMines() {
////        mines = MineManager.fromDb();
////        player = new PlayerManager();
////        Prison.get().getPlatform().getScheduler().runTaskTimer(mines.getTimerTask(), 20, 20);
//    }

    public JsonFileIO getJsonFileIO()
	{
		return jsonFileIO;
	}

    /**
     * <p>Mines are now saved whenever changes are made.  Do not save the Mines on server
     * shutdown since they will never be in a dirty state; they will always be saved.
     * </p>
     * 
     */
	public void disable() {
		// Nothing to do...
    }

    public MinesConfig getConfig() {
        return config;
    }

    public Database getDb() {
        return db;
    }

    public MineManager getMineManager() {
        return mineManager;
    }

    public List<Mine> getMines() {
        return getMineManager().getMines();
    }
    
    public List<Mine> getMines( MineSortOrder sortOrder ) {
    	return getMineManager().getMines( sortOrder );
    }

    public Mine getMine(String mineName) {
    	return getMineManager().getMine(mineName);
    }
    
    public LocaleManager getMinesMessages() {
        return localeManager;
    }

//    public List<String> getWorlds() {
//        return worlds;
//    }

    public PlayerManager getPlayerManager() {
        return player;
    }

}

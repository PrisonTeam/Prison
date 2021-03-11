/*
 * Copyright (C) 2017 The MC-Prison Team
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

package tech.mcprison.prison.ranks;

import java.io.IOException;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.convert.ConversionManager;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleStatus;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.commands.CommandCommands;
import tech.mcprison.prison.ranks.commands.LadderCommands;
import tech.mcprison.prison.ranks.commands.RankUpCommand;
import tech.mcprison.prison.ranks.commands.RanksCommands;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Database;

/**
 * @author Faizaan A. Datoo
 */
public class PrisonRanks 
	extends Module {
	
	public static final String MODULE_NAME = "Ranks";
    /*
     * Fields & Constants
     */

    private static PrisonRanks instance;
    private RankManager rankManager;
    private LadderManager ladderManager;
    private PlayerManager playerManager;

    private Database database;

    /*
     * Constructor
     */

    public PrisonRanks(String version) {
        super(MODULE_NAME, version, 3);
    }


    @Override
    public String getBaseCommands() {
    	return "/ranks /rankup /rankupMax /prestige /prestiges";
    }
    
    /*
     * Methods
     */

    public static PrisonRanks getInstance() {
        return instance;
    }

    @Override 
    public void enable() {
        instance = this;

        if (!PrisonAPI.getIntegrationManager().hasForType(IntegrationType.ECONOMY)) {
            getStatus().setStatus(ModuleStatus.Status.FAILED);
            getStatus().setMessage("&cNo economy plugin");
            
            String integrationDebug = PrisonAPI.getIntegrationManager()
            			.getIntegrationDetails(IntegrationType.ECONOMY);
            Output.get().logError( "PrisonRanks.enable() - Failed - No Economy Plugin Active - " + 
            			integrationDebug );
            return;
        }

        Optional<Database> databaseOptional = PrisonAPI.getStorage().getDatabase("ranksDb");
        if (!databaseOptional.isPresent()) {
            PrisonAPI.getStorage().createDatabase("ranks");
            databaseOptional = PrisonAPI.getStorage().getDatabase("ranks");
        }
        this.database = databaseOptional.get();

        // Load up the ranks

        rankManager = new RankManager(initCollection("ranks"));
        try {
            rankManager.loadRanks();
        } catch (IOException e) {
        	getStatus().setStatus(ModuleStatus.Status.FAILED);
            getStatus().addMessage("&cFailed Loading Ranks: " + e.getMessage());
            Output.get().logError("A rank file failed to load.", e);
        }

        // Load up the ladders


        ladderManager = new LadderManager(initCollection("ladders"), this);
        try {
            ladderManager.loadLadders();
        } catch (IOException e) {
        	getStatus().setStatus(ModuleStatus.Status.FAILED);
        	getStatus().addMessage("&cFailed Loading Loadders: " + e.getMessage());
            Output.get().logError("A ladder file failed to load.", e);
        }
        createDefaultLadder();

        
//        // Set the rank relationships:
//        rankManager.connectRanks();

        
        // Verify that all ranks that use currencies have valid currencies:
        rankManager.identifyAllRankCurrencies();
        
        
        // Load up the players


        playerManager = new PlayerManager(initCollection("players"));
        try {
            playerManager.loadPlayers();
        } catch (IOException e) {
        	getStatus().setStatus(ModuleStatus.Status.FAILED);
        	getStatus().addMessage("&cFailed Loading Players: " + e.getMessage());
            Output.get().logError("A player file failed to load.", e);
        }

        // Load up the commands

        CommandCommands rankCommandCommands = new CommandCommands();
        RanksCommands ranksCommands = new RanksCommands(rankCommandCommands );
        RankUpCommand rankupCommands = new RankUpCommand();
        LadderCommands ladderCommands = new LadderCommands();
        
        rankManager.setRankCommandCommands( rankCommandCommands );
        rankManager.setRanksCommands( ranksCommands );
        rankManager.setRankupCommands( rankupCommands );
        rankManager.setLadderCommands( ladderCommands );

        Prison.get().getCommandHandler().registerCommands( rankCommandCommands );
        Prison.get().getCommandHandler().registerCommands( ranksCommands );
        Prison.get().getCommandHandler().registerCommands( rankupCommands );
        Prison.get().getCommandHandler().registerCommands( ladderCommands );

        // Load up all else

        new FirstJoinHandler();
        new ChatHandler();
        ConversionManager.getInstance().registerConversionAgent(new RankConversionAgent());


        Output.get().logInfo("Loaded " + getRankCount() + " ranks.");
        Output.get().logInfo("Loaded " + getladderCount() + " ladders.");
        Output.get().logInfo("Loaded " + getPlayersCount() + " players.");
        
        

        // Display all Ranks in each ladder:
    	boolean includeAll = true;
    	PrisonRanks.getInstance().getRankManager().ranksByLadders( includeAll );
        
    }


    /**
     * This function deferredStartup() will be called after the integrations have been
     * loaded.  
     * 
     */
    @Override
	public void deferredStartup() {
	}

    /**
     * <p>Do not save ranks upon server shutdown or plugin disable events.  The 
     * ranks should be saved every time there is a modification to them.
     * </p>
     */
    @Override 
    public void disable() {
//        try {
//            rankManager.saveRanks();
//        } catch (IOException e) {
//            Output.get().logError("A ranks file failed to save.", e);
//        }
    }
    

	private Collection initCollection(String collName) {
        Optional<Collection> collectionOptional = database.getCollection(collName);
        if (!collectionOptional.isPresent()) {
            database.createCollection(collName);
            collectionOptional = database.getCollection(collName);
        }

        return collectionOptional.orElseThrow(RuntimeException::new);
    }

    /**
     * A default ladder is absolutely necessary on the server, so let's create it if it doesn't exist, this also create the prestiges ladder.
     */
    private void createDefaultLadder() {
        if ( ladderManager.getLadder("default") == null ) {
            RankLadder rankLadder = ladderManager.createLadder("default");

            if ( rankLadder == null ) {
            	String message = "Failed to create a new default ladder, preexisting one not be found.";
            	Output.get().logError(message);
                super.getStatus().toFailed("&c" + message);
                return;
            }

            try {
                ladderManager.saveLadder( rankLadder );
            } catch (IOException e) {
            	String message = "Failed to save a new default ladder, preexisting one not be found.";
            	Output.get().logError(message, e);
                super.getStatus().toFailed("&c" + message);
            }
        }

        if ( ladderManager.getLadder("prestiges") == null ) {
            RankLadder rankLadder = ladderManager.createLadder("prestiges");

            if ( rankLadder == null ) {
                String message = "Failed to create a new prestiges ladder, preexisting one not be found.";
                Output.get().logError(message);
                super.getStatus().toFailed("&c" + message);
                return;
            }

            try {
                ladderManager.saveLadder( rankLadder );
            } catch (IOException e) {
                String message = "Failed to save a new prestiges ladder, preexisting one not be found.";
                Output.get().logError(message, e);
                super.getStatus().toFailed("&c" + message);
            }
        }

    }

    /*
     * Getters & Setters
     */



    public RankManager getRankManager() {
        return rankManager;
    }

    public LadderManager getLadderManager() {
        return ladderManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public RankLadder getDefaultLadder() {
        return getLadderManager().getLadder("default");
    }

    public Database getDatabase() {
        return database;
    }

    public int getRankCount() {
    	int rankCount = getRankManager() == null ||getRankManager().getRanks() == null ? 0 : 
    			getRankManager().getRanks().size();
    	return rankCount;
    }
    
    public int getladderCount() {
    	int ladderCount = getLadderManager() == null || getLadderManager().getLadders() == null ? 0 : 
    		getLadderManager().getLadders().size();
    	return ladderCount;
    }
    
    public int getPlayersCount() {
    	int playersCount = getPlayerManager() == null || getPlayerManager().getPlayers() == null ? 0 : 
    		getPlayerManager().getPlayers().size();
    	return playersCount;
    }
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.convert.ConversionManager;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.modules.ModuleStatus;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.commands.CommandCommands;
import tech.mcprison.prison.ranks.commands.FailedRankCommands;
import tech.mcprison.prison.ranks.commands.LadderCommands;
import tech.mcprison.prison.ranks.commands.RankUpCommand;
import tech.mcprison.prison.ranks.commands.RanksCommands;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.ranks.tasks.RanksStartupPlayerValidationsAsyncTask;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Database;

/**
 * @author Faizaan A. Datoo
 */
public class PrisonRanks 
	extends PrisonRanksMessages {
	
	public static final String MODULE_NAME = "Ranks";
    /*
     * Fields & Constants
     */

    private static PrisonRanks instance;
    private RankManager rankManager;
    private LadderManager ladderManager;
    private PlayerManager playerManager;

    private Database database;
    
	private List<String> prisonStartupDetails;
	
    private LocaleManager localeManager;
    

    /*
     * Constructor
     */

    public PrisonRanks(String version) {
        super(MODULE_NAME, version, 3);
        
        this.prisonStartupDetails = new ArrayList<>();
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

        this.localeManager = new LocaleManager(this, "lang/ranks");
        
        if (!PrisonAPI.getIntegrationManager().hasForType(IntegrationType.ECONOMY)) {
            getStatus().setStatus(ModuleStatus.Status.FAILED);
            
            getStatus().setMessage( prisonRanksFailureNoEconomyStatusMsg() );
            
            String integrationDebug = PrisonAPI.getIntegrationManager()
            			.getIntegrationDetails(IntegrationType.ECONOMY);
            
            logStartupMessageError( prisonRanksFailureNoEconomyMsg( integrationDebug ) );
            
            
            // Register the failure /ranks command handler:
            
            FailedRankCommands failedRanksCommands = new FailedRankCommands();
            Prison.get().getCommandHandler().registerCommands( failedRanksCommands );
            
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
        } 
        catch (IOException e) {
        	getStatus().setStatus(ModuleStatus.Status.FAILED);
        	
            getStatus().setMessage( prisonRanksFailureLoadingRankStatusMsg( e.getMessage() ) );

            logStartupMessageError( prisonRanksFailureLoadingRanksMsg( e.getMessage() ) );
        }

        // Load up the ladders


        ladderManager = new LadderManager(initCollection("ladders"), this);
        try {
            ladderManager.loadLadders();
        } 
        catch (IOException e) {
        	getStatus().setStatus(ModuleStatus.Status.FAILED);
        	
            getStatus().setMessage( prisonRanksFailureLoadingLadderStatusMsg( e.getMessage() ) );

            logStartupMessageError( prisonRanksFailureLoadingLadderMsg( e.getMessage() ) );
        }
        createDefaultLadder();

        
//        // Set the rank relationships:
//        rankManager.connectRanks();

        
        
        // NOTE: The following is not needed since the ladders are already hooked up to the ranks.
//        for ( Rank rank : rankManager.getRanks() ) {
//        	
//        	if ( rank.getLadder() == null ) {
//    			// Hook up the ladder if it has not been setup yet:
//    			RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( rank );
//    			
//    			rank.setLadder( ladder );
//        	}
//        }
        
        
        // Verify that all ranks that use currencies have valid currencies:
        rankManager.identifyAllRankCurrencies( getPrisonStartupDetails() );
        
        
        // Load up the players


        playerManager = new PlayerManager(initCollection("players"));
        
        try {
            playerManager.loadPlayers();
        } 
        catch (IOException e) {
        	getStatus().setStatus(ModuleStatus.Status.FAILED);
        	
            getStatus().setMessage( prisonRanksFailureLoadingPlayersStatusMsg( e.getMessage() ) );

            logStartupMessageError( prisonRanksFailureLoadingPlayersMsg( e.getMessage() ) );

        	getStatus().addMessage( prisonRanksFailedLoadingPlayersMsg( e.getMessage() ));
        	logStartupMessageError( prisonRanksFailedToLoadPlayFileMsg( e.getMessage() ));
        }


        // Hook up all players to the ranks:
        playerManager.connectPlayersToRanks( false );
        
        Output.get().logInfo( "Ranks: Finished Connecting Players to Ranks." );
        
  
        
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

        Output.get().logInfo( "Ranks: Finished registering Rank Commands." );
        
        
        
        
        // Load up all else

        new FirstJoinHandler();
        new ChatHandler();
        ConversionManager.getInstance().registerConversionAgent(new RankConversionAgent());


        logStartupMessage( prisonRanksStatusLoadedLaddersMsg( getladderCount() ) );
        
		int totalRanks = getRankCount();
		int defaultRanks = getDefaultLadderRankCount();
		int prestigesRanks = getPrestigesLadderRankCount();
		int otherRanks = totalRanks - defaultRanks - prestigesRanks;

        logStartupMessage( prisonRanksStatusLoadedRanksMsg( 
        		totalRanks, defaultRanks, prestigesRanks, otherRanks ) );
        
        logStartupMessage( prisonRanksStatusLoadedPlayersMsg( getPlayersCount() ) );
        

        // Display all Ranks in each ladder:
        List<String> rankDetails = PrisonRanks.getInstance().getRankManager().ranksByLadders();
        for (String msg : rankDetails) {
			Output.get().logInfo(msg);
		}
//    	boolean includeAll = true;
//    	PrisonRanks.getInstance().getRankManager().ranksByLadders( includeAll );
        
        
        
        // Start up the TopNPlayer's collections after all players have been loaded:
        // NOTE: getting the instance of TopNPlayers must be done "after" player validation.
        //       So that thread needs to initiate it after done validating and fixing all players.
//        TopNPlayers.getInstance();
      
        
        // Check all players to see if any need to join:
        RanksStartupPlayerValidationsAsyncTask.submitTaskSync( this );
//        checkAllPlayersForJoin();
    
        
    }


	public void checkAllPlayersForJoin()
	{
		
		boolean addNewPlayers = Prison.get().getPlatform().getConfigBooleanTrue( 
				"prison-ranks.startup.add-new-players-on-startup" );
		
		if ( addNewPlayers ) {
			
			RankUpCommand rankupCommands = rankManager.getRankupCommands();
			
			// If there is a default rank on the default ladder, then
			// check to see if there are any players not in prison: add them:
			RankLadder defaultLadder = getLadderManager().getLadderDefault();
//        RankLadder defaultLadder = getLadderManager().getLadder( LadderManager.LADDER_DEFAULT );
			
			if ( defaultLadder != null && defaultLadder.getRanks().size() > 0 ) {
				int addedPlayers = 0;
				int fixedPlayers = 0;
				
				for ( Player player : Prison.get().getPlatform().getOfflinePlayers() ) {
					
					// getPlayer() will add a player who does not exist:
					RankPlayer rPlayer = playerManager.getPlayer( player );
					if ( rPlayer != null ) {
						if ( rPlayer.checkName( player.getName() ) ) {
							playerManager.savePlayer( rPlayer );
							addedPlayers++;
						}
					}
				}
				
				
				RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
				
				// If any player does not have a rank on the default ladder, then add the default 
				// ladder and rank:
				Rank defaultRank = defaultLadder.getLowestRank().get();
				
				if ( defaultRank == null ) {
					Output.get().logInfo( 
							"PrisonRanks.checkAllPlayersForJoin: Warning: No default rank exists, so bypassing " +
									"the player checks.  There may be players online without a rank which could " + 
									"cause problems.  Create a default rank and then restart the server to validate and " +
							"repair all players.");
					return;
				}
				
				
				for ( RankPlayer rPlayer : playerManager.getPlayers() ) {
					
//        		@SuppressWarnings( "unused" )
//				String rp = rPlayer.toString();
					
					Rank rankOnDefault = null;
					
					PlayerRank pRank = rankPlayerFactory.getRank( rPlayer, defaultLadder );
					
					if ( pRank != null ) {
						
						rankOnDefault = pRank.getRank();
						
//        			Output.get().logInfo( "#### %s  ladder = %s  isRankNull= %s  rank= %s %s [%s]" ,
//        					rPlayer.getName(),
//        					defaultLadder.getName(), 
//        					(rankOnDefault == null ? "true" : "false"), (rankOnDefault == null ? "null" : rankOnDefault.getName()),
//        					(rankOnDefaultStr == null ? "true" : "false"), (rankOnDefaultStr == null ? "null" : rankOnDefaultStr.getName()),
//        					rp );
						
					}
					if ( rankOnDefault == null ) {
						
						rankupCommands.setPlayerRank( rPlayer, defaultRank );
						
						if ( rankPlayerFactory.getRank( rPlayer, defaultLadder ) != null ) {
							
							String message = prisonRankAddedNewPlayer( rPlayer.getName() );
							
							Output.get().logInfo( message );
						}
						
						fixedPlayers++;
					}
					
				}
				if ( addedPlayers > 0 || fixedPlayers > 0 ) {
					
					Output.get().logInfo( prisonRankAddedAndFixedPlayers( addedPlayers, fixedPlayers ) );
				}
			}
			
			Output.get().logInfo( "Ranks: Finished First Join Checks." );
		}
		else {
			
			Output.get().logInfo( "Ranks: First Join Checks disabled. Enable in 'config.yml' by adding " +
							"'prison-ranks.startup.add-new-players-on-startup: true'.");
		}
		
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
        if ( ladderManager.getLadder(LadderManager.LADDER_DEFAULT) == null ) {
            RankLadder rankLadder = ladderManager.createLadder(LadderManager.LADDER_DEFAULT);

            if ( rankLadder == null ) {
            	
            	String failureMsg = prisonRanksFailureCreateDefaultLadderMsg();
            	
            	Output.get().logError( failureMsg );
                super.getStatus().toFailed( failureMsg );
                return;
            }

            if ( !ladderManager.save( rankLadder ) ) {
            	
            	String failureMsg = prisonRanksFailureSavingDefaultLadderMsg();
            	
            	Output.get().logError( failureMsg );
                super.getStatus().toFailed( failureMsg );
            }
        }

        if ( ladderManager.getLadder(LadderManager.LADDER_PRESTIGES) == null ) {
            RankLadder rankLadder = ladderManager.createLadder(LadderManager.LADDER_PRESTIGES);

            if ( rankLadder == null ) {

            	String failureMsg = prisonRanksFailureCreatePrestigeLadderMsg();
            	
            	Output.get().logError( failureMsg );
                super.getStatus().toFailed( failureMsg );
                return;
            }

            if ( !ladderManager.save( rankLadder ) ) {

            	String failureMsg = prisonRanksFailureSavingPrestigeLadderMsg();
            	
            	Output.get().logError( failureMsg );
                super.getStatus().toFailed( failureMsg );
            }
        }

    }



    private void logStartupMessageError( String message ) {
    	logStartupMessage( LogLevel.ERROR, message );
    
    }

    private void logStartupMessage( String message ) {
    	logStartupMessage( LogLevel.INFO, message );
    	
    }
    private void logStartupMessage( LogLevel logLevel, String message ) {
    	
    	Output.get().log( message, logLevel );
    	
    	getPrisonStartupDetails().add( message );
    }
    
    public List<String> getPrisonStartupDetails() {
    	return prisonStartupDetails;
    }
    public void setPrisonStartupDetails( List<String> prisonStartupDetails ){
    	this.prisonStartupDetails = prisonStartupDetails;
    }

    

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
        return getLadderManager().getLadder(LadderManager.LADDER_DEFAULT);
    }
    
    public RankLadder getPrestigesLadder() {
    	return getLadderManager().getLadder(LadderManager.LADDER_PRESTIGES);
    }

    public Database getDatabase() {
        return database;
    }

    public int getRankCount() {
    	int rankCount = getRankManager() == null ||getRankManager().getRanks() == null ? 0 : 
    		getRankManager().getRanks().size();
    	return rankCount;
    }
    
    private int getLadderRankCount( String ladderName ) {
    	int rankCount = 0;
    	
    	if ( getLadderManager() != null && getLadderManager().getLadders() != null ) {
    		RankLadder ladder = getLadderManager().getLadder( ladderName );
    		if ( ladder != null ) {
    			rankCount = ladder.getRanks().size();
    		}
    	}
    	return rankCount;
    }
    
    public int getDefaultLadderRankCount() {
    	return getLadderRankCount( LadderManager.LADDER_DEFAULT );
    }
    
    public int getPrestigesLadderRankCount() {
    	return getLadderRankCount( LadderManager.LADDER_PRESTIGES );
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
    
    public LocaleManager getRanksMessages() {
        return localeManager;
    }
}

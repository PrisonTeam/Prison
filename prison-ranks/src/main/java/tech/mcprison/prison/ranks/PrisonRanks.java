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
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.modules.ModuleManager;
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
import tech.mcprison.prison.ranks.data.TopNPlayers;
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
	
	public static final String MODULE_NAME = ModuleManager.MODULE_NAME_RANKS;

    private static PrisonRanks instance;
    private RankManager rankManager;
    private LadderManager ladderManager;
    private PlayerManager playerManager;

    private Database database;
    
	private List<String> prisonStartupDetails;
	
    private LocaleManager localeManager;
    

    public PrisonRanks(String version) {
        super(MODULE_NAME, version, 3);
        
        this.prisonStartupDetails = new ArrayList<>();
    }


    @Override
    public String getBaseCommands() {
    		return "/ranks /rankup /rankupMax /prestige /prestiges";
    }
    

    public static PrisonRanks getInstance() {
	    	if ( instance == null ) {
	    		synchronized (PrisonRanks.class) {
	    			if ( instance == null ) {
	    				instance = new PrisonRanks( "disabled" );
	    				instance.setEnabled( false );
	    			}
			}
	    	}
        return instance;
    }

    @Override 
    public void enable() {
        instance = this;
        
        setEnabled( true );

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
            ladderManager.loadLadders( getRankManager() );
        } 
        catch (IOException e) {
        		getStatus().setStatus(ModuleStatus.Status.FAILED);
        	
            getStatus().setMessage( prisonRanksFailureLoadingLadderStatusMsg( e.getMessage() ) );

            logStartupMessageError( prisonRanksFailureLoadingLadderMsg( e.getMessage() ) );
        }
        ladderManager.createDefaultLadder();

        
        
        // Verify that all ranks that use currencies have valid currencies:
        rankManager.identifyAllRankCurrencies( getPrisonStartupDetails() );
        
        
        // Load up the players
        playerManager = new PlayerManager(initCollection("players"));
        
        
        try {
        		playerManager.loadAllPlayers();
        } 
        catch (IOException e) {
        		getStatus().setStatus(ModuleStatus.Status.FAILED);
        	
            getStatus().setMessage( prisonRanksFailureLoadingPlayersStatusMsg( e.getMessage() ) );

            logStartupMessageError( prisonRanksFailureLoadingPlayersMsg( e.getMessage() ) );

	        	getStatus().addMessage( prisonRanksFailedLoadingPlayersMsg( e.getMessage() ));
	        	logStartupMessageError( prisonRanksFailedToLoadPlayFileMsg( e.getMessage() ));
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

        Output.get().logInfo( "Ranks: Finished registering Rank Commands." );
        
        
        
        
        // Load up everything else

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
        
        
        
        // Start up the TopNPlayer's collections after all players have been loaded:
        // NOTE: getting the instance of TopNPlayers must be done "after" player validation.
        //       So that thread needs to initiate it after done validating and fixing all players.
        // NOTE: Issue: player validation may take a long time, or could be disabled. So 
        //       load topNPlayers now, and then refresh after validation.
        TopNPlayers.getInstance();
      
        
        // Check all players to see if any need to join:
        RanksStartupPlayerValidationsAsyncTask.submitTaskSync( this );
    
        
    }

    
    public boolean reloadRanksAndLadders() {
	    	boolean success = false;
	    	
	    	try {
	    		
	    		// New temp RankManager to reload ranks:
	    		RankManager rManager = new RankManager(initCollection("ranks"));
	    		
	    		rManager.reloadAllRanks();
	
	            // Load up the ladders
	
	    		// New temp LadderManager to reload ladders:
	            LadderManager lManager = new LadderManager(initCollection("ladders"), this);
	            
	            // NOTE: This instance of the LadderManager requires the new temp instance of the rank manager:
	            lManager.reloadAllLadders( rManager );
	           
	            
	            // Now replace all rank data and ladder data with the newly loaded data:
	            getRankManager().setLoadedRanks( rManager.getRanks() );
	            getRankManager().setRanksByName( rManager.getRanksByName() );
	            getRankManager().setRanksById( rManager.getRanksById() );
	            
	            getLadderManager().setLoadedLadders( lManager.getLoadedLadders() );
				
	            
	            // Must reload all players now, so they are properly aligned with the ranks and ladders:
	            getPlayerManager().reloadAllPlayers();
	            
	            
			} catch (IOException e) {
				String msg = String.format(
						"PrisonRanks: reloadRanksAndLadders: Failed. [%s]",
						e.getMessage());
				
				Output.get().logInfo( msg );
			}
	    	
	    	return success;
    }

    /**
     * <p>This is actually a very bad idea to add any players that have not joined.
     * This should be disabled.  When a player joins the server for the first time,
     * then it will add them successfully.
     * </p>
     * 
     */
	public void checkAllPlayersForJoin()
	{
		
		boolean addNewPlayers = 
				Prison.get().getPlatform().getConfigBooleanTrue( 
								"ranks.startup.add-new-players-on-startup" ) ||
				Prison.get().getPlatform().getConfigBooleanFalse( 
								"prison-ranks.startup.add-new-players-on-startup" );
		
		if ( addNewPlayers ) {
			
			long startMs = System.currentTimeMillis();
			
			RankUpCommand rankupCommands = rankManager.getRankupCommands();
			
			// If there is a default rank on the default ladder, then
			// check to see if there are any players not in prison: add them:
			RankLadder defaultLadder = getLadderManager().getLadderDefault();
			
			if ( defaultLadder != null && defaultLadder.getRanks().size() > 0 ) {
				int addedPlayers = 0;
				int fixedPlayers = 0;
				
				List<RankPlayer> rPlayers = playerManager.getPlayers();
				
				// NOTE: The Platform.getOfflinePlayers() only returns the playerManager.getPlayers() because
				//       bukkit can seriously lag the server and kill it.
//				List<Player> players = Prison.get().getPlatform().getOfflinePlayers();

				
				// Prison will no longer add offline players.  They must join to be added. Causes massive lag on huge servers!
				
				
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
				
				
				for ( RankPlayer rPlayer : rPlayers ) {
					
					
					PlayerRank pRank = rankPlayerFactory.getRank( rPlayer, defaultLadder );
					
					if ( pRank == null || pRank.getRank() == null ) {
						
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
			
			long endMs =  System.currentTimeMillis();
			
			long duration = endMs - startMs;
			
			Output.get().logInfo( "Ranks: Finished First Join Checks: " + duration + " ms" );
		}
		else {
			
			Output.get().logInfo( "Ranks: First Join Checks disabled. Enable in 'config.yml' by adding " +
							"'ranks.startup.add-new-players-on-startup: true'.");
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
    		setEnabled( false );
    }
    

	private Collection initCollection(String collName) {
        Optional<Collection> collectionOptional = database.getCollection(collName);
        if (!collectionOptional.isPresent()) {
            database.createCollection(collName);
            collectionOptional = database.getCollection(collName);
        }

        return collectionOptional.orElseThrow(RuntimeException::new);
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
	    	if ( rankManager == null && !isEnabled() ) {
	    		rankManager = new RankManager();
	    	}
        return rankManager;
    }

	public LadderManager getLadderManager() {
        return ladderManager;
    }

    public PlayerManager getPlayerManager() {
    	
	    	if ( playerManager == null && !isEnabled() ) {
	    		playerManager = new PlayerManager( null );
	    	}
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
    
    
    /**
     * For modules that have elements, this will return the count.  If a module has no
     * elements, then it will return a -1.  Otherwise a zero would indicate that a module
     * should have elements, but it currently has none.
     * 
     * Example would be ranks and mines.  For these, if it returns a zero, then they have 
     * no ranks or mines defined.  If it return a -1 then the module is not active.
     * 
     * @return
     */
    public int getElementCount() {
	    	int results = isEnabled() ? 0 : -1;
	    	
	    	if ( isEnabled() ) {
	    		results = getRankCount();
	    	}
	    	
	    	return results;
    }
}

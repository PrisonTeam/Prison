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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.ranks.events.RankUpEvent;
import tech.mcprison.prison.tasks.PrisonCommandTask;
import tech.mcprison.prison.tasks.PrisonCommandTask.CustomPlaceholders;

/**
 * Utilities for changing the ranks of players.
 *
 * @author Faizaan A. Datoo
 */
public class RankUtil
		extends RankUtilMessages {

	
	public enum RankupCommands {
		rankup,
		promote,
		demote,
		setrank,
		firstJoin;
	}
	
	public enum RankupModes {
		ONE_RANK,
		MAX_RANKS
	}
	
	
	public enum RankupStatus {
		RANKUP_SUCCESS,
		DEMOTE_SUCCESS,
		
		RANKUP_FAILURE,
		RANKUP_FAILURE_COULD_NOT_LOAD_PLAYER,
		RANKUP_FAILURE_COULD_NOT_LOAD_LADDER,
		RANKUP_FAILURE_UNABLE_TO_ASSIGN_RANK,
		RANKUP_FAILURE_COULD_NOT_SAVE_PLAYER_FILE,
		
		RANKUP_FAILURE_RANK_DOES_NOT_EXIST,
		RANKUP_FAILURE_RANK_IS_NOT_IN_LADDER,
		RANKUP_FAILURE_CURRENCY_IS_NOT_SUPPORTED,
		RANKUP_FAILURE_NO_PLAYERRANK,
		
		RANKUP_EVENT_CANCELED,
		
		RANKUP_LOWEST,
		RANKUP_HIGHEST,
		RANKUP_CANT_AFFORD,
		RANKUP_NO_RANKS,
		
		RANKUP_LADDER_REMOVED,
		RANKUP_FAILURE_REMOVING_LADDER,
		
		
		IN_PROGRESS
		;
	}
	

	public enum RankupTransactions {
		
		tring_to_rankup,
		tring_to_promote,
		trying_to_demote,
		trying_to_setrank,
		trying_to_firstJoin,
		
		bypassing_cost_for_player,
		costs_paid_by_player,
		costs_refunded_to_player,
		
		failed_player,
		failed_ladder,

		player_has_no_rank_on_ladder,
		orginal_rank,
		
		failed_rank_not_found,
		failed_setrank, 
		
		failed_rank_not_in_ladder,
		
		assigned_default_ladder, 
		assigned_default_rank,

		next_rank_set, 
		set_to_default_rank,
		original_rank_is_null, 
		set_to_next_higher_rank,
		set_to_prior_lower_rank,
		
		failed_unable_to_assign_rank, 
		
		no_ranks_found_on_ladder, 
		no_higher_rank_found,
		no_lower_rank_found, 
		
		custom_currency,
		specified_currency_not_found, 
		player_cannot_afford,
		player_balance_initial,
		player_balance_decreased,
		player_balance_increased,
		player_balance_final,
		zero_cost_to_player,
		
		attempting_to_delete_ladder_from_player,
		cannot_delete_default_ladder,
		ladder_was_removed_from_player,
		could_not_delete_ladder,
		
		failed_rankup_event_canceled_outside_of_prison,

		
		failure_cannot_save_player_file,
		
		rankupCommandsStart,
		rankupCommandsCompleted,
		
		fireRankupEvent,
		
		rankup_successful, 
		demote_successful, 

		failure_exception_caught_check_server_logs, 
		successfully_saved_player_rank_data,
		
		failure_orginal_playerRank_does_not_exist
		
		;
	}

	public enum PromoteForceCharge {
		no_charge,
		charge_player,
		refund_player
		;
		
		public static PromoteForceCharge fromString( String forceCharge ) {
			PromoteForceCharge results = null;
			
			for ( PromoteForceCharge pfc : values() ) {
				if ( pfc.name().equalsIgnoreCase( forceCharge )) {
					results = pfc;
					break;
				}
			}
			
			return results;
		}
	}
	
    public RankUtil() {
    	super();
    }

    
    
    public RankupResults rankupPlayer(Player player, RankPlayer rankPlayer, String ladderName, String playerName, 
    		List<PrisonCommandTask> cmdTasks ) {
    	
    	return rankupPlayer(RankupCommands.rankup, player, rankPlayer, ladderName, null, 
    					playerName, null, PromoteForceCharge.charge_player, cmdTasks );
    }
    
    public RankupResults promotePlayer(Player player, RankPlayer rankPlayer, String ladderName, 
    										String playerName, String executorName, PromoteForceCharge pForceCharge, 
    										List<PrisonCommandTask> cmdTasks ) {
    	
    	return rankupPlayer(RankupCommands.promote, player, rankPlayer, ladderName, null, 
    					playerName, executorName, pForceCharge, cmdTasks );
    }
    
    public RankupResults demotePlayer(Player player, RankPlayer rankPlayer, String ladderName, 
    										String playerName, String executorName, PromoteForceCharge pForceCharge, 
    										List<PrisonCommandTask> cmdTasks ) {
    	
    	return rankupPlayer(RankupCommands.demote, player, rankPlayer, ladderName, null, 
    					playerName, executorName, pForceCharge, cmdTasks );
    }
    
    public RankupResults setRank(Player player, RankPlayer rankPlayer, String ladderName, String rankName, 
    										String playerName, String executorName, 
    										List<PrisonCommandTask> cmdTasks ) {
    	
    	RankupCommands rankupCmd = "FirstJoinEvent".equalsIgnoreCase( executorName ) ?
    						RankupCommands.firstJoin : RankupCommands.setrank;
    	
    	return rankupPlayer( rankupCmd, player, rankPlayer, ladderName, rankName, 
    					playerName, executorName, PromoteForceCharge.no_charge, cmdTasks );
    }
    
    /**
     * <p>This intermediate function ensures the results are logged before allowing the
     * results be return to the calling functions.
     * </p>
     * 
     * @param command
     * @param player
     * @param ladderName The ladder to use. If null then uses the "default" ladder.
     * @param bypassCost
     * @param promote
     * @param rank The rank to use. If null with the default ladder, it will use the default rank (the first one).
     * @param playerName
     * @param executorName
     * @return
     */
    private RankupResults rankupPlayer(RankupCommands command, Player player, RankPlayer rankPlayer, String ladderName, 
    				String rankName, String playerName, String executorName, 
    				PromoteForceCharge pForceCharge, List<PrisonCommandTask> cmdTasks ) {
    	
    	RankupResults results = new RankupResults(command, rankPlayer, executorName, ladderName, rankName);
    	
    	switch ( command ) {
			case rankup:
				results.addTransaction(RankupTransactions.tring_to_rankup);
				break;
				
			case promote:
				results.addTransaction(RankupTransactions.tring_to_promote);
				break;
				
			case demote:
				results.addTransaction(RankupTransactions.trying_to_demote);
				break;
				
			case setrank:
				results.addTransaction(RankupTransactions.trying_to_setrank);
				break;
				
			case firstJoin:
				results.addTransaction(RankupTransactions.trying_to_firstJoin);
				break;
				
			default:
				break;
		}
    	
    	switch ( pForceCharge ) {
			case no_charge:
				results.addTransaction( RankupTransactions.bypassing_cost_for_player );
				break;

			case charge_player:
				results.addTransaction( RankupTransactions.costs_paid_by_player );
				break;
				
			case refund_player:
				results.addTransaction( RankupTransactions.costs_refunded_to_player );
				
				break;
				
			default:
				break;
		}
    	
    	
    	
//      Player prisonPlayer = rankPlayer;
//      Player prisonPlayer = PrisonAPI.getPlayer(player.uid).orElse(null);
    	if( player == null ) {
    		results.addTransaction( RankupStatus.RANKUP_FAILURE_COULD_NOT_LOAD_PLAYER, RankupTransactions.failed_player );
    		return results;
    	}
    	
    	
        // If ladderName is null, then assign it the default ladder:
        if ( ladderName == null ) {
        	ladderName = "default";
        	results.addTransaction(RankupTransactions.assigned_default_ladder);
        } 
    	

    	try {
    		rankupPlayerInternal(results, command, player, rankPlayer, ladderName, 
    				rankName, pForceCharge, cmdTasks );
    	} catch (Exception e ) {
    		results.addTransaction( RankupTransactions.failure_exception_caught_check_server_logs );
    		
    		Output.get().logError( rankUtilFailureInternalMsg( e.getMessage() ), e );
    	}
    	
    	// Log the results:
    	logTransactionResults(results);
    	
    	return results;
    }



	/**
     * Sends the player to the next rank.
     *
     * @param player     The {@link RankPlayer} to rank up.
     * @param ladderName The name of the ladder to rank up this player on.
     */
    private void rankupPlayerInternal(RankupResults results, 
    		RankupCommands command, Player prisonPlayer, RankPlayer rankPlayer, String ladderName, 
    		String rankName, 
    		PromoteForceCharge pForceCharge, List<PrisonCommandTask> cmdTasks ) {

    	Output.get().logDebug( DebugTarget.rankup, "Rankup: rankupPlayerInternal: ");
    	
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if( ladder == null ) {
        	results.addTransaction( RankupStatus.RANKUP_FAILURE_COULD_NOT_LOAD_LADDER, RankupTransactions.failed_ladder );
        	return;
        }
        
        results.setLadder( ladder );

        // This should never be null, since if a player is not on this ladder, then they 
        // should never make it this far in to this code:
        // Um... not true when performing a prestige for the first time.... lol
        // Also not true when being added to a ladder, since they will not have an existing rank.
        
        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        
        
        
        // originalRank can be null...
        PlayerRank originalRank = rankPlayerFactory.getRank( rankPlayer, ladder );
        
        if ( originalRank == null ) {
        	
        	results.addTransaction( RankupTransactions.player_has_no_rank_on_ladder );
        }
        
//        if ( originalRank == null && ladder.getName().equals( "default" ) ) {
//        	
//        	// Only default ladder should be logged as an error if there is no rank:
//        	results.addTransaction( RankupStatus.RANKUP_FAILURE_NO_PLAYERRANK, 
//        					RankupTransactions.failure_orginal_playerRank_does_not_exist );
//        	return;
//        }
        
//        Optional<Rank> currentRankOptional = player.getRank(ladder);
//        Rank originalRank = currentRankOptional.orElse( null );
        
        results.addTransaction( RankupTransactions.orginal_rank );
        results.setPlayerRankOriginal( originalRank );
        results.setOriginalRank( originalRank == null ? null : originalRank.getRank() );
        

        
        /**
         * calculate the target rank.  In this function the original rank is updated within the
         * results object, so from here on out, use the results object for original rank.
         */
        Rank targetRank = calculateTargetRank( command, results, rankName );
        
       
        if ( results.getStatus() != RankupStatus.IN_PROGRESS ) {
        	// Failed while calculatingTargetRank so return now:
        	return;
        }
        
        
        // Process the remove rank request
        if ( command == RankupCommands.setrank && "-remove-".equalsIgnoreCase( rankName ) ) {
        	results.addTransaction(RankupTransactions.attempting_to_delete_ladder_from_player);
        	
        	if ("default".equalsIgnoreCase( ladderName ) ) {
        		results.addTransaction(RankupTransactions.cannot_delete_default_ladder);
        	}
        	else {
        		boolean success = rankPlayerFactory.removeLadder( rankPlayer, ladder.getName() );
        		
        		if ( success ) {
        	        if ( savePlayerRank( results, rankPlayer ) ) {

        	        	results.addTransaction( RankupStatus.RANKUP_LADDER_REMOVED, 
        	        			RankupTransactions.ladder_was_removed_from_player );
        	        	
        	        	return;
        	        }
        		}
        	}
        	
        	results.addTransaction( RankupStatus.RANKUP_FAILURE_REMOVING_LADDER, 
        			RankupTransactions.could_not_delete_ladder );
        	
        	return;
        }
        
        

        // Target rank is still null, so something failed so terminate:
        if ( targetRank == null ) {
        	results.addTransaction( RankupStatus.RANKUP_FAILURE_UNABLE_TO_ASSIGN_RANK, 
        												RankupTransactions.failed_unable_to_assign_rank );
        	return;
        }
        

        // This calculates the target rank, and takes in to consideration the player's existing rank:
        PlayerRank pRankNext =
        			originalRank == null ? null :
        				originalRank.getTargetPlayerRankForPlayer( rankPlayer, targetRank );
//        		new PlayerRank( targetRank, originalRank.getRankMultiplier() );
		
        // If player does not have a rank on this ladder, then grab the first rank on the ladder since they need
        // to be added to the ladder.
        if ( pRankNext == null ) {
        	
        	pRankNext = rankPlayerFactory.createPlayerRank( targetRank );
        	
//        	pRankNext = originalRank.getTargetPlayerRankForPlayer( rankPlayer, ladder.getLowestRank().get() );
        }
        
        	
		results.setPlayerRankTarget( pRankNext );
        results.setTargetRank( targetRank );
        
        
//        String currency = "";
        double nextRankCost = pRankNext.getRankCost();
        double currentRankCost = ( results.getPlayerRankOriginal() == null ? 0 : 
        				results.getPlayerRankOriginal().getRankCost() );
        
        
        results.addTransaction( RankupTransactions.fireRankupEvent );
        
        // Fire the rankup event to see if it should be canceled.
        RankUpEvent rankupEvent = new RankUpEvent(rankPlayer, results.getOriginalRank(), targetRank, nextRankCost, 
        								command, pForceCharge );
        Prison.get().getEventBus().post(rankupEvent);

        if ( rankupEvent.isCanceled() ) {
        	
        	
        	results.addTransaction( RankupStatus.RANKUP_EVENT_CANCELED, 
        						RankupTransactions.failed_rankup_event_canceled_outside_of_prison );
        	return;
        	
        }
        

        // We're going to be making a transaction here
        // We'll check if the player can afford it first, and if so, we'll make the transaction and proceed.
        
        if (pForceCharge != PromoteForceCharge.no_charge ) {
        	
        	if ( targetRank.getCurrency() != null ) {
        		results.addTransaction( RankupTransactions.custom_currency );
        		results.setCurrency( targetRank.getCurrency() );
        		
        		EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
        											.getEconomyForCurrency( targetRank.getCurrency() );
				if ( currencyEcon == null ) {
					results.addTransaction( RankupStatus.RANKUP_FAILURE_CURRENCY_IS_NOT_SUPPORTED, 
							RankupTransactions.specified_currency_not_found );
					return;
				}
        	}
        	
        	results.addTransaction( RankupTransactions.player_balance_initial );
        	results.setBalanceInitial( rankPlayer.getBalance( targetRank.getCurrency() ) );
        	results.setCurrency( targetRank.getCurrency() );
        	
        	if ( pForceCharge == PromoteForceCharge.charge_player) {
        		if ( rankPlayer.getBalance(targetRank.getCurrency()) < nextRankCost ) {
        			results.addTransaction( RankupStatus.RANKUP_CANT_AFFORD, 
        					RankupTransactions.player_cannot_afford );
        			return;
        		}
        		
        		results.addTransaction( RankupTransactions.player_balance_decreased );
        		rankPlayer.removeBalance( targetRank.getCurrency(), nextRankCost );
        	} else 
        		if ( pForceCharge == PromoteForceCharge.refund_player) {
        			
    			results.addTransaction( RankupTransactions.player_balance_increased);
    			if ( results.getOriginalRank() != null ) {
    				rankPlayer.addBalance( results.getOriginalRank().getCurrency(), currentRankCost );
    			}
    		} else {
    			// Should never hit this code!!
    		}
        	
        	results.addTransaction( RankupTransactions.player_balance_final );
        	results.setBalanceFinal( rankPlayer.getBalance( targetRank.getCurrency() ) );
        	
        	
        } else {
        	results.addTransaction( RankupTransactions.zero_cost_to_player );
        }

        // Actually apply the new rank here:
        rankPlayer.addRank(targetRank);
        


        if ( !savePlayerRank( results, rankPlayer ) ) {
        	return;
        }

        // Now, we'll run the rank up commands.

        results.addTransaction( RankupTransactions.rankupCommandsStart );
        results.setRankupCommandsAvailable( targetRank.getRankUpCommands().size() );
        
        int count = 0;
        
        List<String> rankupCommands = new ArrayList<>();
        
        rankupCommands.addAll( ladder.getRankUpCommands() );
        rankupCommands.addAll( targetRank.getRankUpCommands() );
        
        for ( int row = 0; row < rankupCommands.size(); row++ ) {
        	
        	String cmd = rankupCommands.get( row );
        	if ( cmd != null && 
        			( !cmd.contains( "{firstJoin}" ) || 
        			   cmd.contains( "{firstJoin}" ) && command == RankupCommands.firstJoin )  ) {
        		
        		PlayerRank opRank = results.getPlayerRankOriginal();
        		PlayerRank tpRank = results.getPlayerRankTarget();
        		
        		Rank oRank = results.getOriginalRank();
        		Rank tRank = results.getTargetRank();
        		
        		if ( command == RankupCommands.firstJoin && cmd.contains( "{firstJoin}" ) ) {
        			cmd = cmd.replace( "{firstJoin}", "" );
        		}
        		
				PrisonCommandTask cmdTask = new PrisonCommandTask( command.name(), cmd, row );
				
				cmdTask.setLadder( ladder );
				cmdTask.setRankTarget( tpRank );
				cmdTask.setRankOriginal( opRank );
				
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.balanceInitial, Double.toString( results.getBalanceInitial()) );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.balanceFinal, Double.toString( results.getBalanceFinal()) );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.currency, results.getCurrency() );
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.originalRankCost, 
								opRank == null ? "" : Double.toString( opRank.getRankCost() ) );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.rankupCost, 
								tpRank == null ? "" : Double.toString( tpRank.getRankCost() ) );
				
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.ladder, results.getLadderName() );
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.rank,
									(oRank == null ? "none" : oRank.getName()) );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.rankTag, 
									(oRank == null ? "none" : oRank.getTag()) );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.targetRank, 
									(tRank == null ? "none" : tRank.getName()) );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.targetRankTag, 
									(tRank == null ? "none" : tRank.getTag()) );
				
				
				cmdTasks.add( cmdTask );
				
				// Comment this out to stack the rank commands:
				cmdTask.submitCommandTask( prisonPlayer );

        		
//        		String formatted = cmd.replace("{player}", prisonPlayer.getName())
//        				.replace("{player_uid}", rankPlayer.getUUID().toString());

//            Prison.get().getPlatform().logPlain(
//            		String.format( "RankUtil.rankupPlayerInternal:  Rank Command: [%s]", 
//            					formatted ));
        		
//        		PrisonAPI.dispatchCommand(formatted);
        		count++;
        	}
        }
        results.setRankupCommandsExecuted( count );
        results.addTransaction( RankupTransactions.rankupCommandsCompleted );

        
        
        // Recalculate the rankup cost multipliers to apply to the next rankup.
        // This must be done AFTER the ranks commands sets up the placeholder 
        // values so they will reflect the correct amounts.
        rankPlayer.recalculateRankMultipliers();
        
        
//        results.addTransaction( RankupTransactions.fireRankupEvent );
//        
//        // Nothing can cancel a RankUpEvent:
//        RankUpEvent rankupEvent = new RankUpEvent(rankPlayer, originalRank, targetRank, nextRankCost);
//        Prison.get().getEventBus().post(rankupEvent);
        
        
        if ( RankupCommands.demote == command ) {
        	
        	results.addTransaction( RankupStatus.DEMOTE_SUCCESS, RankupTransactions.demote_successful );
        }
        else {
        	
        	results.addTransaction( RankupStatus.RANKUP_SUCCESS, RankupTransactions.rankup_successful );
        }
        
    }



	private boolean savePlayerRank( RankupResults results, RankPlayer rankPlayer ) {
		boolean success = false;
//		try {
            PrisonRanks.getInstance().getPlayerManager().savePlayer(rankPlayer);
            
            results.addTransaction( 
            		RankupTransactions.successfully_saved_player_rank_data );
            
            success = true;
//        } 
//		catch (IOException e) {
//        	
//    		Output.get().logError( rankUtilFailureSavingPlayerMsg( e.getMessage() ), e );
//    		
//            results.addTransaction( RankupStatus.RANKUP_FAILURE_COULD_NOT_SAVE_PLAYER_FILE, 
//            			RankupTransactions.failure_cannot_save_player_file );
//        }
		
		return success;
	}

    
    private Rank calculateTargetRank(RankupCommands command, RankupResults results, 
    		// Rank originalRank, // RankLadder ladder, String ladderName, 
    		String rankName ) {
    	Rank targetRank = null;
    	
    	
        // For all commands except for setrank, if the player does not have a current rank, then
        // set it to the default and skip all other rank processing:
    	
    	// NOTE: With new processing using PlayerRank, not sure if the default rank should be set to anything... 
    	//       I'm thinking no...
        
        if ( results.getOriginalRank() == null && 
        		( command == RankupCommands.rankup || 
        		  command == RankupCommands.promote ||
        		  command == RankupCommands.demote )) {
        	// Set the default rank:
            Optional<Rank> lowestRank = results.getLadder().getLowestRank();
//            Optional<Rank> lowestRank = ladder.getByPosition(0);
            if (!lowestRank.isPresent()) {
            	results.addTransaction( RankupStatus.RANKUP_NO_RANKS, 
            					RankupTransactions.no_ranks_found_on_ladder );
            	return targetRank;
            }
            results.addTransaction( RankupTransactions.set_to_default_rank );
            targetRank = lowestRank.get();
            
            // need to set this to a valid value:
            results.setOriginalRank( lowestRank.get() );
        }
        
        if ( results.getOriginalRank() == null ) {
        	results.addTransaction( RankupTransactions.original_rank_is_null );
        	
        }

        
        
        // If default ladder and rank is null at this point, that means use the "default" rank:
        if ( command == RankupCommands.setrank || command == RankupCommands.firstJoin ) {
        	
        	if ( "-remove-".equalsIgnoreCase( rankName ) ) {
        		
        		// process the -remove- rank after this function returns:
        		return targetRank;
        	}
        	 
        	else if ("default".equalsIgnoreCase( results.getLadder().getName() ) && rankName == null ) {
	        	Optional<Rank> lowestRank = results.getLadder().getLowestRank();
	        	if ( lowestRank.isPresent() ) {
	        		targetRank = lowestRank.get();
	        		rankName = targetRank.getName();
	
	        		results.addTransaction(RankupTransactions.assigned_default_rank);
	        	} 
        	
        	} 
        	
        	if ( targetRank == null && rankName != null ) {
        		
        		targetRank = PrisonRanks.getInstance().getRankManager().getRank( rankName );
        		
        		if ( targetRank != null ) {
        			
        			if ( !results.getLadder().containsRank( targetRank )) {
        				results.addTransaction( RankupStatus.RANKUP_FAILURE_RANK_IS_NOT_IN_LADDER, 
        						RankupTransactions.failed_rank_not_in_ladder );
        				return targetRank;
        			}
        		} else {
        			results.addTransaction( RankupStatus.RANKUP_FAILURE_RANK_DOES_NOT_EXIST, 
        					RankupTransactions.failed_rank_not_found );
        			return targetRank;
        		}
        	} else {
        		results.addTransaction( RankupTransactions.failed_setrank );
        		
        		// Got a problem... if using setrank and no rankName is provided, this is a problem
        		// But it should never get this far if that is the situation
        	}
        }
    	

        
        if ( targetRank == null ) {

        	if ( command ==  RankupCommands.rankup || command == RankupCommands.promote ) {
        		// Trying to promote: 
//        		nextRankOptional = ladder.getNext(ladder.getPositionOfRank(currentRankOptional.get()));
        		
        		if ( results.getOriginalRank().getRankNext() == null ) {
        			// We're already at the highest rank.
        			results.addTransaction( RankupStatus.RANKUP_HIGHEST, 
        								RankupTransactions.no_higher_rank_found );
        			return targetRank;
        		}
        		targetRank = results.getOriginalRank().getRankNext();
        		results.addTransaction( RankupTransactions.set_to_next_higher_rank );

        	} else if ( command == RankupCommands.demote ) {
        		// Trying to demote:
//        		nextRankOptional = ladder.getPrevious(ladder.getPositionOfRank(currentRankOptional.get()));
        		
        		if ( results.getOriginalRank().getRankPrior() == null ) {
        			// We're already at the lowest rank.
        			results.addTransaction( RankupStatus.RANKUP_LOWEST, 
        								RankupTransactions.no_lower_rank_found );
        			return targetRank;
        		}
        		targetRank = results.getOriginalRank().getRankPrior();
        		results.addTransaction( RankupTransactions.set_to_prior_lower_rank );
        	}
        }
        
    	return targetRank;
    }
    

//    public static int doubleToInt(Object d) {
//        return Math.toIntExact(Math.round((double) d));
//    }
//    
//    public static long doubleToLong(Object d) {
//    	return Math.round((double) d);
//    }
    
    
    
    private void logTransactionResults( RankupResults results )
	{
    	StringBuilder sb = new StringBuilder();
    	DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    	DecimalFormat iFmt = new DecimalFormat("#,##0");

    	Rank oRank = results.getOriginalRank();
    	PlayerRank opRank = results.getPlayerRankOriginal();
    	
    	Rank tRank = results.getTargetRank();
    	PlayerRank tpRank = results.getPlayerRankTarget();
    	
    	for ( RankupTransactions rt : RankupTransactions.values() ) {
    		
    		// Log the entry if it exists in the results:
    		if ( results.getTransactions().contains( rt ) ) {
    			if ( sb.length() > 0 ) {
    				sb.append( " " );
    			}
    			
    			// Log the transaction:
    			sb.append( rt.name() );
    			
    			// If the transaction has supporting data, log it too:
    			switch ( rt ) {
    				case orginal_rank:
    					sb.append( "=" );
    					sb.append( oRank == null ? "" : oRank.getName() );
    					
    					break;
    					
    				case custom_currency:
    					sb.append( "=" );
    					sb.append( tRank == null || tRank.getCurrency() == null ? "" : tRank.getCurrency() );
    					
    					break;
    					
    				case specified_currency_not_found:
    					sb.append( "=" );
    					sb.append( tRank == null || tRank.getCurrency() == null ? "" : tRank.getCurrency() );
    					
    					break;
    					
    				case player_balance_initial:
    					sb.append( "=" );
    					sb.append( dFmt.format( results.getBalanceInitial() ) );
    					
    					break;
    					
    				case player_balance_decreased:
    					sb.append( "=" );
    					sb.append( tpRank == null ? "" : dFmt.format( tpRank.getRankCost() ) );
    					
    					break;
    					
    				case player_balance_increased:
    					sb.append( "=" );
    					sb.append( opRank == null ? "" : dFmt.format( opRank.getRankCost() ) );
    					
    					break;
    					
    				case player_balance_final:
    					sb.append( "=" );
    					sb.append( dFmt.format( results.getBalanceFinal() ) );
    					
    					break;
    					
    				case rankupCommandsStart:
    					sb.append( "=" );
    					sb.append( iFmt.format( results.getRankupCommandsAvailable() ) );
    					
    					break;
    					
    				case rankupCommandsCompleted:
    					sb.append( "=" );
    					sb.append( iFmt.format( results.getRankupCommandsExecuted() ) );
    					
    					break;
    					
    				default:
    					break;
    			}
    		}
    	}
    	
    	
    	// Add in the prefix for the log entry:
    	String prefix = String.format( 
    			"Rankup: command=%s player=%s executor=%s status=%s " +
    			"ladderName=%s rankName=%s " +
    			"originalRank=(%s%s%s) targetRank=(%s%s%s) " +
    			"runtime=%s ms message=[%s] ", 
    			
    			results.getCommand().name(), 
    			results.getRankPlayer().getName(), 
    			(results.getExecutor() == null ? "(see player)" : results.getExecutor()), 
    			(results.getStatus() == null ? "" : results.getStatus().name()),
    			
    			(results.getLadderName() == null ? "" : results.getLadderName() ),
    			(results.getRankName() == null ? "" : results.getRankName() ),
    			
    			
    			(oRank == null ? "none" : oRank.getName()), 
    			(opRank == null ? "" : " " + dFmt.format( opRank.getRankCost() )), 
    			(oRank == null || oRank.getCurrency() == null ? "" : " " + oRank.getCurrency()),
    			
    			(tRank == null ? "none" : tRank.getName()), 
    			(tpRank == null ? "" : " " + dFmt.format( tpRank.getRankCost())), 
    			(tRank == null || tRank.getCurrency() == null ? "" : " " + tRank.getCurrency()),
				
				iFmt.format( results.getElapsedTime() ),
    			(results.getMessage() == null ? "" : results.getMessage()) 
    			);
    	
    	sb.insert( 0, prefix );
    	
		
    	Output.get().logInfo( sb.toString() );
	}


//    @Deprecated
//    public static class RankUpResult {
//
//        private RankupStatus status;
//        private Rank rank;
//        private String message;
//
//        public RankUpResult(RankupStatus status, Rank rank, String message) {
//            this.status = status;
//            this.rank = rank;
//            this.message = message;
//        }
//        
//        public RankUpResult(RankupStatus status, Rank rank) {
//        	this(status, rank, null);
//        }
//        
//        public RankUpResult(RankupStatus status) {
//        	this(status, null, null);
//        }
//        
//
//		public RankupStatus getStatus() {
//			return status;
//		}
//		public void setStatus( RankupStatus status ) {
//			this.status = status;
//		}
//
//		public Rank getRank() {
//			return rank;
//		}
//		public void setRank( Rank rank ) {
//			this.rank = rank;
//		}
//
//		public String getMessage() {
//			return message;
//		}
//		public void setMessage( String message ) {
//			this.message = message;
//		}
//    }


}

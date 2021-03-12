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
import java.text.DecimalFormat;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.events.RankUpEvent;

/**
 * Utilities for changing the ranks of players.
 *
 * @author Faizaan A. Datoo
 */
public class RankUtil {

	
	public enum RankupCommands {
		rankup,
		promote,
		demote,
		setrank;
	}
	
	public enum RankupModes {
		ONE_RANK,
		MAX_RANKS
	}
	
	
	public enum RankupStatus {
		RANKUP_SUCCESS,
		RANKUP_FAILURE,
		RANKUP_FAILURE_COULD_NOT_LOAD_PLAYER,
		RANKUP_FAILURE_COULD_NOT_LOAD_LADDER,
		RANKUP_FAILURE_UNABLE_TO_ASSIGN_RANK,
		RANKUP_FAILURE_COULD_NOT_SAVE_PLAYER_FILE,
		
		RANKUP_FAILURE_RANK_DOES_NOT_EXIST,
		RANKUP_FAILURE_RANK_IS_NOT_IN_LADDER,
		RANKUP_FAILURE_CURRENCY_IS_NOT_SUPPORTED,
		
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
		
		bypassing_cost_for_player,
		costs_paid_by_player,
		costs_refunded_to_player,
		
		failed_player,
		failed_ladder,

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
		failure_exception_caught_check_server_logs, 
		successfully_saved_player_rank_data
		
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

    
    
    public RankupResults rankupPlayer(Player player, RankPlayer rankPlayer, String ladderName, String playerName) {
    	return rankupPlayer(RankupCommands.rankup, player, rankPlayer, ladderName, null, 
    					playerName, null, PromoteForceCharge.charge_player );
    }
    
    public RankupResults promotePlayer(Player player, RankPlayer rankPlayer, String ladderName, 
    										String playerName, String executorName, PromoteForceCharge pForceCharge) {
    	return rankupPlayer(RankupCommands.promote, player, rankPlayer, ladderName, null, 
    					playerName, executorName, pForceCharge);
    }
    
    public RankupResults demotePlayer(Player player, RankPlayer rankPlayer, String ladderName, 
    										String playerName, String executorName, PromoteForceCharge pForceCharge) {
    	return rankupPlayer(RankupCommands.demote, player, rankPlayer, ladderName, null, 
    					playerName, executorName, pForceCharge);
    }
    
    public RankupResults setRank(Player player, RankPlayer rankPlayer, String ladderName, String rankName, 
    										String playerName, String executorName) {
    	return rankupPlayer(RankupCommands.setrank, player, rankPlayer, ladderName, rankName, 
    					playerName, executorName, PromoteForceCharge.no_charge );
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
    				PromoteForceCharge pForceCharge ) {
    	
    	RankupResults results = new RankupResults(command, playerName, executorName, ladderName, rankName);
    	
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
    				rankName, playerName, executorName, pForceCharge );
    	} catch (Exception e ) {
    		results.addTransaction( RankupTransactions.failure_exception_caught_check_server_logs );
    		String message = String.format( 
    				"Failure to perform rankupPlayerInternal check server logs for stack trace: %s", e.getMessage() );
    		Output.get().logError( message, e );
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
    		String rankName, String playerName, String executorName, 
    		PromoteForceCharge pForceCharge) {

    	

        
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if( ladder == null ) {
        	results.addTransaction( RankupStatus.RANKUP_FAILURE_COULD_NOT_LOAD_LADDER, RankupTransactions.failed_ladder );
        	return;
        }

        

        Rank originalRank = rankPlayer.getRank(ladder.getName());
//        Optional<Rank> currentRankOptional = player.getRank(ladder);
//        Rank originalRank = currentRankOptional.orElse( null );
        
        results.addTransaction( RankupTransactions.orginal_rank );
        results.setOriginalRank( originalRank );
       
        
        /**
         * calculate the target rank:
         */
        Rank targetRank = calculateTargetRank( command, results, originalRank, ladder, 
        				ladderName, rankName );
        
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
        		boolean success = rankPlayer.removeLadder( ladder.getName() );
        		
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
        	results.addTransaction( RankupStatus.RANKUP_FAILURE_UNABLE_TO_ASSIGN_RANK, RankupTransactions.failed_unable_to_assign_rank );
        	return;
        }
        
        results.setTargetRank( targetRank );

        
        
        double nextRankCost = targetRank.getCost();
        double currentRankCost = (originalRank == null ? 0 : originalRank.getCost());
        
        
        results.addTransaction( RankupTransactions.fireRankupEvent );
        
        // Fire the rankup event to see if it should be canceled.
        RankUpEvent rankupEvent = new RankUpEvent(rankPlayer, originalRank, targetRank, nextRankCost, 
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
    			if ( originalRank != null ) {
    				rankPlayer.addBalance( originalRank.getCurrency(), currentRankCost );
    			}
    		} else {
    			// Should never hit this code!!
    		}
        	
        	results.addTransaction( RankupTransactions.player_balance_final );
        	results.setBalanceFinal( rankPlayer.getBalance( targetRank.getCurrency() ) );
        	
        	
        } else {
        	results.addTransaction( RankupTransactions.zero_cost_to_player );
        }

        rankPlayer.addRank(ladder, targetRank);

        if ( !savePlayerRank( results, rankPlayer ) ) {
        	return;
        }

        // Now, we'll run the rank up commands.

        results.addTransaction( RankupTransactions.rankupCommandsStart );
        results.setRankupCommandsAvailable( targetRank.getRankUpCommands().size() );
        
        int count = 0;
        for (String cmd : targetRank.getRankUpCommands()) {
        	if ( cmd != null ) {
        		
        		String formatted = cmd.replace("{player}", prisonPlayer.getName())
        				.replace("{player_uid}", rankPlayer.getUUID().toString());
        		
//            Prison.get().getPlatform().logPlain(
//            		String.format( "RankUtil.rankupPlayerInternal:  Rank Command: [%s]", 
//            					formatted ));
        		
        		PrisonAPI.dispatchCommand(formatted);
        		count++;
        	}
        }
        results.setRankupCommandsExecuted( count );
        results.addTransaction( RankupTransactions.rankupCommandsCompleted );

        
//        results.addTransaction( RankupTransactions.fireRankupEvent );
//        
//        // Nothing can cancel a RankUpEvent:
//        RankUpEvent rankupEvent = new RankUpEvent(rankPlayer, originalRank, targetRank, nextRankCost);
//        Prison.get().getEventBus().post(rankupEvent);
        
        
        results.addTransaction( RankupStatus.RANKUP_SUCCESS, RankupTransactions.rankup_successful );
        
    }



	private boolean savePlayerRank( RankupResults results, RankPlayer rankPlayer ) {
		boolean success = false;
		try {
            PrisonRanks.getInstance().getPlayerManager().savePlayer(rankPlayer);
            
            results.addTransaction( 
            		RankupTransactions.successfully_saved_player_rank_data );
            
            success = true;
        } catch (IOException e) {
            Output.get().logError("An error occurred while saving player files.", e);
            
            results.addTransaction( RankupStatus.RANKUP_FAILURE_COULD_NOT_SAVE_PLAYER_FILE, 
            			RankupTransactions.failure_cannot_save_player_file );
        }
		
		return success;
	}

    
    private Rank calculateTargetRank(RankupCommands command, RankupResults results, 
    		Rank originalRank, RankLadder ladder, String ladderName, String rankName ) {
    	Rank targetRank = null;
    	
    	
        // For all commands except for setrank, if the player does not have a current rank, then
        // set it to the default and skip all other rank processing:
        
        if ( originalRank == null && 
        		( command == RankupCommands.rankup || 
        		  command == RankupCommands.promote ||
        		  command == RankupCommands.demote )) {
        	// Set the default rank:
            Optional<Rank> lowestRank = ladder.getByPosition(0);
            if (!lowestRank.isPresent()) {
            	results.addTransaction( RankupStatus.RANKUP_NO_RANKS, 
            					RankupTransactions.no_ranks_found_on_ladder );
            	return targetRank;
            }
            results.addTransaction( RankupTransactions.set_to_default_rank );
            targetRank = lowestRank.get();
            
            // need to set this to a valid value:
            originalRank = lowestRank.get();
        }
        
        if ( originalRank == null ) {
        	results.addTransaction( RankupTransactions.original_rank_is_null );
        	
        }

        
        
        // If default ladder and rank is null at this point, that means use the "default" rank:
        if ( command == RankupCommands.setrank ) {
        	
        	if ( "-remove-".equalsIgnoreCase( rankName ) ) {
        		
        		// process the -remove- rank after this function returns:
        		return targetRank;
        	}
        	 
        	else if ("default".equalsIgnoreCase( ladderName ) && rankName == null ) {
	        	Optional<Rank> lowestRank = ladder.getLowestRank();
	        	if ( lowestRank.isPresent() ) {
	        		targetRank = lowestRank.get();
	        		rankName = targetRank.getName();
	
	        		results.addTransaction(RankupTransactions.assigned_default_rank);
	        	} 
        	
        	} 
        	
        	if ( targetRank == null && rankName != null ) {
        		
        		targetRank = PrisonRanks.getInstance().getRankManager().getRank( rankName );
        		
        		if ( targetRank != null ) {
        			
        			if ( !ladder.containsRank( targetRank.getId() )) {
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
        		
        		if ( originalRank.getRankNext() == null ) {
        			// We're already at the highest rank.
        			results.addTransaction( RankupStatus.RANKUP_HIGHEST, 
        								RankupTransactions.no_higher_rank_found );
        			return targetRank;
        		}
        		targetRank = originalRank.getRankNext();
        		results.addTransaction( RankupTransactions.set_to_next_higher_rank );

        	} else if ( command == RankupCommands.demote ) {
        		// Trying to demote:
//        		nextRankOptional = ladder.getPrevious(ladder.getPositionOfRank(currentRankOptional.get()));
        		
        		if ( originalRank.getRankPrior() == null ) {
        			// We're already at the lowest rank.
        			results.addTransaction( RankupStatus.RANKUP_LOWEST, 
        								RankupTransactions.no_lower_rank_found );
        			return targetRank;
        		}
        		targetRank = originalRank.getRankPrior();
        		results.addTransaction( RankupTransactions.set_to_prior_lower_rank );
        	}
        }
        
    	return targetRank;
    }
    

    public static int doubleToInt(Object d) {
        return Math.toIntExact(Math.round((double) d));
    }
    
    public static long doubleToLong(Object d) {
    	return Math.round((double) d);
    }
    
    
    
    private void logTransactionResults( RankupResults results )
	{
    	StringBuilder sb = new StringBuilder();
    	DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    	DecimalFormat iFmt = new DecimalFormat("#,##0");

    	Rank oRank = results.getOriginalRank();
    	Rank tRank = results.getTargetRank();
    	
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
    					sb.append( tRank == null ? "" : dFmt.format( tRank.getCost() ) );
    					
    					break;
    					
    				case player_balance_increased:
    					sb.append( "=" );
    					sb.append( tRank == null ? "" : dFmt.format( oRank.getCost() ) );
    					
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
    			results.getPlayer(), 
    			(results.getExecutor() == null ? "(see player)" : results.getExecutor()), 
    			(results.getStatus() == null ? "" : results.getStatus().name()),
    			
    			(results.getLadderName() == null ? "" : results.getLadderName() ),
    			(results.getRankName() == null ? "" : results.getRankName() ),
    			
    			
    			(oRank == null ? "none" : oRank.getName()), 
    			(oRank == null ? "" : " " + dFmt.format( oRank.getCost())), 
    			(oRank == null || oRank.getCurrency() == null ? "" : " " + oRank.getCurrency()),
    			
    			(tRank == null ? "none" : tRank.getName()), 
    			(tRank == null ? "" : " " + dFmt.format( tRank.getCost())), 
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

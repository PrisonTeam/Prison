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
import java.text.NumberFormat;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.integration.IntegrationType;
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
	
	
	public enum RankupStatus {
		RANKUP_SUCCESS,
		RANKUP_FAILURE,
		RANKUP_FAILURE_RANK_DOES_NOT_EXIST,
		RANKUP_FAILURE_RANK_IS_NOT_IN_LADDER,
		RANKUP_FAILURE_CURRENCY_IS_NOT_SUPPORTED,
		
		RANKUP_LOWEST,
		RANKUP_HIGHEST,
		RANKUP_CANT_AFFORD,
		RANKUP_NO_RANKS,
		
		IN_PROGRESS
		;
	}
	

	public enum RankupTransactions {
		
		bypassing_cost_for_player,
		
		tring_to_promote,
		trying_to_demote,
		
		failed_player,
		failed_ladder,
		failed_rank_not_found,
		failed_rank_not_in_ladder,
		
		orginal_rank,
		next_rank_set, 
		set_to_default_rank,
		set_to_next_higher_rank,
		set_to_prior_lower_rank,
		
		no_ranks_found_on_ladder, 
		no_higher_rank_found,
		no_lower_rank_found, 
		
		custom_currency,
		specified_currency_not_found, 
		player_cannot_afford,
		player_balance_initial,
		player_balance_decreased,
		player_balance_final,
		zero_cost_to_player,
		
		failure_cannot_save_player_file,
		
		rankupCommandsStart,
		rankupCommandsCompleted,
		
		fireRankupEvent,
		
		rankup_successful
		
	}


    public RankUtil() {
    	super();
    }

    
    
    public RankupResults rankupPlayer(RankPlayer player, String ladderName, String playerName) {
    	return rankupPlayer(RankupCommands.rankup, player, ladderName, false, true, null, playerName, null);
    }
    
    public RankupResults promotePlayer(RankPlayer player, String ladderName, 
    										String playerName, String executorName) {
    	return rankupPlayer(RankupCommands.promote, player, ladderName, true, true, null, playerName, executorName);
    }
    
    public RankupResults demotePlayer(RankPlayer player, String ladderName, 
    										String playerName, String executorName) {
    	return rankupPlayer(RankupCommands.demote, player, ladderName, true, false, null, playerName, executorName);
    }
    
    public RankupResults setRank(RankPlayer player, String ladderName, String rank, 
    										String playerName, String executorName) {
    	return rankupPlayer(RankupCommands.setrank, player, ladderName, true, true, rank, playerName, executorName);
    }
    
    private RankupResults rankupPlayer(RankupCommands command, RankPlayer player, String ladderName, 
    		boolean bypassCost, boolean promote, String rank, 
    		String playerName, String executorName) {
    	
    	RankupResults results = rankupPlayerInternal(command, player, ladderName, 
        		bypassCost, promote, rank, playerName, executorName);
    	
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
    private RankupResults rankupPlayerInternal(RankupCommands command, RankPlayer player, String ladderName, 
    		boolean bypassCost, boolean promote, String rank, 
    		String playerName, String executorName) {
    	
    	RankupResults results = new RankupResults(command, playerName, executorName);

    	// Log when cost for player is being bypassed:
    	if ( bypassCost ) {
    		results.addTransaction( RankupTransactions.bypassing_cost_for_player );
    	}
    	
    	// Log either promotion or demotion:
    	results.addTransaction( 
    			promote ? 
    					RankupTransactions.tring_to_promote : 
    					RankupTransactions.trying_to_demote );

        Player prisonPlayer = PrisonAPI.getPlayer(player.uid).orElse(null);
        if( prisonPlayer == null ) {
        	return results.addTransaction( RankupStatus.RANKUP_FAILURE, RankupTransactions.failed_player );
        }
        
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName).orElse(null);
        if( ladder == null ) {
        	return results.addTransaction( RankupStatus.RANKUP_FAILURE, RankupTransactions.failed_ladder );
        }
        
        
        
        Rank targetRank = null;
        
        if ( rank != null ) {
        	Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank( rank );
        	
        	if ( rankOptional.isPresent() ) {
        		targetRank = rankOptional.get();
        		
        		if ( !ladder.containsRank( targetRank.id )) {
        			return results.addTransaction( RankupStatus.RANKUP_FAILURE_RANK_IS_NOT_IN_LADDER, 
        					RankupTransactions.failed_rank_not_in_ladder );
        		}
        	} else {
        		return results.addTransaction( RankupStatus.RANKUP_FAILURE_RANK_DOES_NOT_EXIST, 
        				RankupTransactions.failed_rank_not_found );
        	}
        }
        

        Optional<Rank> currentRankOptional = player.getRank(ladder);
        results.addTransaction( RankupTransactions.orginal_rank );
        results.setOriginalRank( currentRankOptional.orElse( null ) );
        
        
        if ( targetRank != null ) {
        	results.addTransaction( RankupTransactions.next_rank_set );
        }
        else if (!currentRankOptional.isPresent()) {
            Optional<Rank> lowestRank = ladder.getByPosition(0);
            if (!lowestRank.isPresent()) {
            	return results.addTransaction( RankupStatus.RANKUP_NO_RANKS, 
            					RankupTransactions.no_ranks_found_on_ladder );
            }
            results.addTransaction( RankupTransactions.set_to_default_rank );
            targetRank = lowestRank.get();
        } else {
        	Optional<Rank> nextRankOptional = null;
        	if ( promote ) {
        		// Trying to promote: 
        		nextRankOptional = ladder.getNext(ladder.getPositionOfRank(currentRankOptional.get()));
        		
        		if (!nextRankOptional.isPresent()) {
        			// We're already at the highest rank.
        			return results.addTransaction( RankupStatus.RANKUP_HIGHEST, 
        								RankupTransactions.no_higher_rank_found );
        		}
        		targetRank = nextRankOptional.get();
        		results.addTransaction( RankupTransactions.set_to_next_higher_rank );

        	} else {
        		// Trying to demote:
        		nextRankOptional = ladder.getPrevious(ladder.getPositionOfRank(currentRankOptional.get()));
        		
        		if (!nextRankOptional.isPresent()) {
        			// We're already at the lowest rank.
        			return results.addTransaction( RankupStatus.RANKUP_LOWEST, 
        								RankupTransactions.no_lower_rank_found );
        		}
        		targetRank = nextRankOptional.get();
        		results.addTransaction( RankupTransactions.set_to_prior_lower_rank );
        	}
        }
        results.setTargetRank( targetRank );
        

        // We're going to be making a transaction here
        // We'll check if the player can afford it first, and if so, we'll make the transaction and proceed.

        double nextRankCost = targetRank.cost;
        if (!bypassCost) {
        	
        	
        	if ( targetRank.currency != null ) {
        		results.addTransaction( RankupTransactions.custom_currency );
        		
        		EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
        						.getEconomyForCurrency( targetRank.currency );
        		if ( currencyEcon == null ) {
        			return results.addTransaction( RankupStatus.RANKUP_FAILURE_CURRENCY_IS_NOT_SUPPORTED, 
        					RankupTransactions.specified_currency_not_found );
        		} else {
            		if (!currencyEcon.canAfford(prisonPlayer, nextRankCost, targetRank.currency)) {
            			//results.setTargetRank( targetRank );
            			return results.addTransaction( RankupStatus.RANKUP_CANT_AFFORD, 
            					RankupTransactions.player_cannot_afford );
            		}
            		
            		results.addTransaction( RankupTransactions.player_balance_initial );
            		results.setBalanceInitial( currencyEcon.getBalance( prisonPlayer, targetRank.currency ) );
            		results.addTransaction( RankupTransactions.player_balance_decreased );
            		currencyEcon.removeBalance(prisonPlayer, nextRankCost, targetRank.currency );
            		results.addTransaction( RankupTransactions.player_balance_final );
            		results.setBalanceFinal( currencyEcon.getBalance( prisonPlayer, targetRank.currency ) );
        		}
        		
        	} else {
        		
        		EconomyIntegration economy = (EconomyIntegration) PrisonAPI.getIntegrationManager()
        				.getForType(IntegrationType.ECONOMY).orElseThrow(IllegalStateException::new);
        		if (!economy.canAfford(prisonPlayer, nextRankCost)) {
        			//results.setTargetRank( targetRank );
        			return results.addTransaction( RankupStatus.RANKUP_CANT_AFFORD, 
        					RankupTransactions.player_cannot_afford );
        		}
        		
        		results.addTransaction( RankupTransactions.player_balance_initial );
        		results.setBalanceInitial( economy.getBalance( prisonPlayer ) );
        		results.addTransaction( RankupTransactions.player_balance_decreased );
        		economy.removeBalance(prisonPlayer, nextRankCost);
        		results.addTransaction( RankupTransactions.player_balance_final );
        		results.setBalanceFinal( economy.getBalance( prisonPlayer ) );
        	}
        	
        } else {
        	results.addTransaction( RankupTransactions.zero_cost_to_player );
        }

        player.addRank(ladder, targetRank);

        try {
            PrisonRanks.getInstance().getPlayerManager().savePlayer(player);
        } catch (IOException e) {
            Output.get().logError("An error occurred while saving player files.", e);
            
            return results.addTransaction( RankupStatus.RANKUP_FAILURE, 
            			RankupTransactions.failure_cannot_save_player_file );
        }

        // Now, we'll run the rank up commands.

        results.addTransaction( RankupTransactions.rankupCommandsStart );
        results.setRankupCommandsAvailable( targetRank.rankUpCommands.size() );
        
        int count = 0;
        for (String cmd : targetRank.rankUpCommands) {
            String formatted = cmd.replace("{player}", prisonPlayer.getName())
                .replace("{player_uid}", player.uid.toString());
            PrisonAPI.dispatchCommand(formatted);
            count++;
        }
        results.setRankupCommandsExecuted( count );
        results.addTransaction( RankupTransactions.rankupCommandsCompleted );

        
        results.addTransaction( RankupTransactions.fireRankupEvent );
        Prison.get().getEventBus().post(
            new RankUpEvent(player, currentRankOptional.orElse(null), targetRank, nextRankCost));
        
        
        results.addTransaction( RankupStatus.RANKUP_SUCCESS, RankupTransactions.rankup_successful );
        
        return results;
    }

    public static String doubleToDollarString(double val) {
        return NumberFormat.getCurrencyInstance().format(val);
    }

    public static int doubleToInt(Object d) {
        return Math.toIntExact(Math.round((double) d));
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
    					sb.append( oRank == null ? "" : oRank.name );
    					
    					break;
    					
    				case custom_currency:
    					sb.append( "=" );
    					sb.append( tRank == null || tRank.currency == null ? "" : tRank.currency );
    					
    					break;
    					
    				case specified_currency_not_found:
    					sb.append( "=" );
    					sb.append( tRank == null || tRank.currency == null ? "" : tRank.currency );
    					
    					break;
    					
    				case player_balance_initial:
    					sb.append( "=" );
    					sb.append( dFmt.format( results.getBalanceInitial() ) );
    					
    					break;
    					
    				case player_balance_decreased:
    					sb.append( "=" );
    					sb.append( tRank == null ? "" : dFmt.format( tRank.cost ) );
    					
    					break;
    					
    				case player_balance_final:
    					sb.append( "=" );
    					sb.append( dFmt.format( results.getBalanceInitial() ) );
    					
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
    			"originalRank=(%s%s%s) targetRank=(%s%s%s) " +
    			"runtime=%s ms message=[%s] ", 
    			
    			results.getCommand().name(), results.getPlayer(), 
    			results.getExecutor() == null ? "" : results.getExecutor(), 
    			results.getStatus().name(),
    			
    			(oRank == null ? "none" : oRank.name), 
    			(oRank == null ? "" : " " + dFmt.format( oRank.cost)), 
    			(oRank == null || oRank.currency == null ? "" : " " + oRank.currency),
    			
    			(tRank == null ? "none" : tRank.name), 
    			(tRank == null ? "" : " " + dFmt.format( tRank.cost)), 
    			(tRank == null || tRank.currency == null ? "" : " " + tRank.currency),
				
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

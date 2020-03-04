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

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.events.RankUpEvent;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

/**
 * Utilities for changing the ranks of players.
 *
 * @author Faizaan A. Datoo
 */
public class RankUtil {

    /*
     * Fields & Constants
     */

	public enum RankupStatus {
		RANKUP_SUCCESS,
		RANKUP_FAILURE,
		RANKUP_LOWEST,
		RANKUP_HIGHEST,
		RANKUP_CANT_AFFORD,
		RANKUP_NO_RANKS
		;
	}
//    public static final int RANKUP_SUCCESS = 0, RANKUP_FAILURE = 1, RANKUP_HIGHEST = 2,
//        RANKUP_CANT_AFFORD = 3, RANKUP_NO_RANKS = 4;

    /*
     * Constructor
     */

    private RankUtil() {
    }

    /*
     * Method
     */
    
    
    public static RankUpResult rankUpPlayer(RankPlayer player, String ladderName) {
    	return rankUpPlayer(player, ladderName, false, true);
    }
    
    public static RankUpResult promotePlayer(RankPlayer player, String ladderName) {
    	return rankUpPlayer(player, ladderName, true, true);
    }
    
    public static RankUpResult demotePlayer(RankPlayer player, String ladderName) {
    	return rankUpPlayer(player, ladderName, true, false);
    }
    
    
    /**
     * Sends the player to the next rank.
     *
     * @param player     The {@link RankPlayer} to rank up.
     * @param ladderName The name of the ladder to rank up this player on.
     */
    private static RankUpResult rankUpPlayer(RankPlayer player, String ladderName, 
    		boolean bypassCost, boolean promote) {

        Player prisonPlayer = PrisonAPI.getPlayer(player.uid).orElse(null);
        RankLadder ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName).orElse(null);

        if(prisonPlayer == null || ladder == null) {
            return new RankUpResult(RankupStatus.RANKUP_FAILURE);
        }

        Optional<Rank> currentRankOptional = player.getRank(ladder);
        Rank nextRank;

        if (!currentRankOptional.isPresent()) {
            Optional<Rank> lowestRank = ladder.getByPosition(0);
            if (!lowestRank.isPresent()) {
                return new RankUpResult(RankupStatus.RANKUP_NO_RANKS);
            }
            nextRank = lowestRank.get();
        } else {
        	Optional<Rank> nextRankOptional = null;
        	if ( promote ) {
        		nextRankOptional = ladder.getNext(ladder.getPositionOfRank(currentRankOptional.get()));
        		
        		if (!nextRankOptional.isPresent()) {
        			return new RankUpResult(RankupStatus.RANKUP_HIGHEST,
        					currentRankOptional.get()); // We're already at the highest rank.
        		}
        	} else {
        		nextRankOptional = ladder.getPrevious(ladder.getPositionOfRank(currentRankOptional.get()));
        		
        		if (!nextRankOptional.isPresent()) {
        			return new RankUpResult(RankupStatus.RANKUP_LOWEST,
        					currentRankOptional.get()); // We're already at the lowest rank.
        		}
        	}

            nextRank = nextRankOptional.get();
        }

        // We're going to be making a transaction here
        // We'll check if the player can afford it first, and if so, we'll make the transaction and proceed.

        double nextRankCost = nextRank.cost;
        if (!bypassCost) {
        	EconomyIntegration economy = (EconomyIntegration) PrisonAPI.getIntegrationManager()
        			.getForType(IntegrationType.ECONOMY).orElseThrow(IllegalStateException::new);
        	if (!economy.canAfford(prisonPlayer, nextRankCost)) {
        		return new RankUpResult(RankupStatus.RANKUP_CANT_AFFORD, nextRank);
        	}
        	
        	economy.removeBalance(prisonPlayer, nextRankCost);
        }

        player.addRank(ladder, nextRank);

        try {
            PrisonRanks.getInstance().getPlayerManager().savePlayer(player);
        } catch (IOException e) {
            Output.get().logError("An error occurred while saving player files.", e);
            return new RankUpResult(RankupStatus.RANKUP_FAILURE);
        }

        // Now, we'll run the rank up commands.

        for (String cmd : nextRank.rankUpCommands) {
            String formatted = cmd.replace("{player}", prisonPlayer.getName())
                .replace("{player_uid}", player.uid.toString());
            PrisonAPI.dispatchCommand(formatted);
        }

        Prison.get().getEventBus().post(
            new RankUpEvent(player, currentRankOptional.orElse(null), nextRank, nextRankCost));
        return new RankUpResult(RankupStatus.RANKUP_SUCCESS, nextRank, 
        		(bypassCost ? "Bypass cost: " + nextRankCost : null));
    }

    public static String doubleToDollarString(double val) {
        return NumberFormat.getCurrencyInstance().format(val);
    }

    public static int doubleToInt(Object d) {
        return Math.toIntExact(Math.round((double) d));
    }

    /*
     * Member Classes
     */


    public static class RankUpResult {

        private RankupStatus status;
        private Rank rank;
        private String message;

        public RankUpResult(RankupStatus status, Rank rank, String message) {
            this.status = status;
            this.rank = rank;
            this.message = message;
        }
        
        public RankUpResult(RankupStatus status, Rank rank) {
        	this(status, rank, null);
        }
        
        public RankUpResult(RankupStatus status) {
        	this(status, null, null);
        }
        

		public RankupStatus getStatus() {
			return status;
		}
		public void setStatus( RankupStatus status ) {
			this.status = status;
		}

		public Rank getRank() {
			return rank;
		}
		public void setRank( Rank rank ) {
			this.rank = rank;
		}

		public String getMessage() {
			return message;
		}
		public void setMessage( String message ) {
			this.message = message;
		}
    }


}

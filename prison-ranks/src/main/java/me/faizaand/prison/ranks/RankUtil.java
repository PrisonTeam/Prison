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

package me.faizaand.prison.ranks;

import me.faizaand.prison.Prison;
import me.faizaand.prison.integration.EconomyIntegration;
import me.faizaand.prison.integration.IntegrationType;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.ranks.data.Rank;
import me.faizaand.prison.ranks.data.RankLadder;
import me.faizaand.prison.ranks.data.RankPlayer;
import me.faizaand.prison.ranks.events.RankUpEvent;

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

    public static final int RANKUP_SUCCESS = 0, RANKUP_FAILURE = 1, RANKUP_HIGHEST = 2,
        RANKUP_CANT_AFFORD = 3, RANKUP_NO_RANKS = 4;

    /*
     * Constructor
     */

    private RankUtil() {
    }

    /*
     * Method
     */

    /**
     * Sends the player to the next rank.
     *
     * @param player     The {@link RankPlayer} to rank up.
     * @param ladderName The name of the ladder to rank up this player on.
     */
    public static RankUpResult rankUpPlayer(RankPlayer player, String ladderName) {

        GamePlayer prisonPlayer = Prison.get().getPlatform().getPlayerManager().getPlayer(player.uid).orElse(null);
        RankLadder ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName).orElse(null);

        if(prisonPlayer == null || ladder == null) {
            return new RankUpResult(RANKUP_FAILURE, null);
        }

        Optional<Rank> currentRankOptional = player.getRank(ladder);
        Rank nextRank;

        if (!currentRankOptional.isPresent()) {
            Optional<Rank> lowestRank = ladder.getByPosition(0);
            if (!lowestRank.isPresent()) {
                return new RankUpResult(RANKUP_NO_RANKS, null);
            }
            nextRank = lowestRank.get();
        } else {
            Optional<Rank> nextRankOptional =
                ladder.getNext(ladder.getPositionOfRank(currentRankOptional.get()));

            if (!nextRankOptional.isPresent()) {
                return new RankUpResult(RANKUP_HIGHEST,
                    currentRankOptional.get()); // We're already at the highest rank.
            }

            nextRank = nextRankOptional.get();
        }

        // We're going to be making a transaction here
        // We'll check if the player can afford it first, and if so, we'll make the transaction and proceed.

        EconomyIntegration economy = (EconomyIntegration) Prison.get().getIntegrationManager()
            .getForType(IntegrationType.ECONOMY).orElseThrow(IllegalStateException::new);
        if (!economy.canAfford(prisonPlayer, nextRank.cost)) {
            return new RankUpResult(RANKUP_CANT_AFFORD, nextRank);
        }

        economy.removeBalance(prisonPlayer, nextRank.cost);

        player.addRank(ladder, nextRank);

        try {
            PrisonRanks.getInstance().getPlayerManager().savePlayer(player);
        } catch (IOException e) {
            Output.get().logError("An error occurred while saving player files.", e);
            return new RankUpResult(RANKUP_FAILURE, null);
        }

        // Now, we'll run the rank up commands.

        for (String cmd : nextRank.rankUpCommands) {
            String formatted = cmd.replace("{player}", prisonPlayer.getName())
                .replace("{player_uid}", player.uid.toString());
            Prison.get().getPlatform().dispatchCommand(formatted);
        }

        return new RankUpResult(RANKUP_SUCCESS, nextRank);
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

        public int status;
        public Rank rank;

        public RankUpResult(int status, Rank rank) {
            this.status = status;
            this.rank = rank;
        }
    }


}

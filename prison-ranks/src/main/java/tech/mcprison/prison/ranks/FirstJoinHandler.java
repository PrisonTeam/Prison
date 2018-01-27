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

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.events.FirstJoinEvent;

import java.io.IOException;
import java.util.Optional;

/**
 * Handles the players upon their first join.
 *
 * @author Faizaan A. Datoo
 */
public class FirstJoinHandler {

    /*
     * Constructor
     */

    public FirstJoinHandler() {
        Prison.get().getEventBus().register(this);
    }

    /*
     * Listeners
     */

    @Subscribe public void onFirstJoin(FirstJoinEvent event) {
        RankPlayer player = event.getPlayer();

        Optional<Rank> firstRank = PrisonRanks.getInstance().getDefaultLadder().getLowestRank();

        if (firstRank.isPresent()) {
            player.addRank(PrisonRanks.getInstance().getDefaultLadder(), firstRank.get());
        } else {
            Output.get().logWarn("There are no ranks on the server! New player has no rank.");
        }

        try {
            PrisonRanks.getInstance().getPlayerManager().savePlayer(player);
        } catch (IOException e) {
            Output.get().logError("Could not save player files.", e);
        }
    }

}

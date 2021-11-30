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
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.ranks.events.FirstJoinEvent;

/**
 * Handles the players upon their first join.
 *
 * @author Faizaan A. Datoo
 */
public class FirstJoinHandler
		extends FirstJoinHandlerMessages {

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

        // Try to perform the first join processing to give them the default rank:
        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        rankPlayerFactory.firstJoin( player );

        PrisonRanks.getInstance().getPlayerManager().savePlayer(player);
//        try {
//        } catch (IOException e) {
//        	
//        	Output.get().logError( firstJoinErrorCouldNotSavePlayer(), e );
//        }
    }

}

/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.ranks.events;

import tech.mcprison.prison.platform.Player;
import tech.mcprison.prison.platform.events.Cancelable;
import tech.mcprison.prison.ranks.Rank;

/**
 * @author Camouflage100
 */
public class RankPromoteEvent implements Cancelable {

    private Player player;
    private Rank oldRank;
    private Rank newRank;

    public RankPromoteEvent(Player player, Rank oldRank, Rank newRank) {
        this.player = player;
        this.oldRank = oldRank;
        this.newRank = newRank;
    }

    @Override public boolean isCanceled() {
        return false;
    }

    @Override public void setCanceled(boolean canceled) {

    }

    public Player getPlayer() {
        return player;
    }

    public Rank getOldRank() {
        return oldRank;
    }

    public Rank getNewRank() {
        return newRank;
    }

    public void setNewRank(Rank newRank) {
        this.newRank = newRank;
    }
}

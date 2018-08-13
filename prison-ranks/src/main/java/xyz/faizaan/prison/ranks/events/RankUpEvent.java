/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
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

package xyz.faizaan.prison.ranks.events;

import xyz.faizaan.prison.ranks.data.Rank;
import xyz.faizaan.prison.ranks.data.RankPlayer;

/**
 * An event that fires when a player ranks up.
 *
 * @author Faizaan A. Datoo
 */
public class RankUpEvent {

    /*
     * Fields & Constants
     */

    private RankPlayer player;
    private Rank oldRank;
    private Rank newRank;
    private double cost;

    /*
     * Constructor
     */

    public RankUpEvent(RankPlayer player, Rank oldRank, Rank newRank, double cost) {
        this.player = player;
        this.oldRank = oldRank;
        this.newRank = newRank;
        this.cost = cost;
    }

    /*
     * Getters & Setters
     */

    public RankPlayer getPlayer() {
        return player;
    }

    public void setPlayer(RankPlayer player) {
        this.player = player;
    }

    public Rank getOldRank() {
        return oldRank;
    }

    public void setOldRank(Rank oldRank) {
        this.oldRank = oldRank;
    }

    public Rank getNewRank() {
        return newRank;
    }

    public void setNewRank(Rank newRank) {
        this.newRank = newRank;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}

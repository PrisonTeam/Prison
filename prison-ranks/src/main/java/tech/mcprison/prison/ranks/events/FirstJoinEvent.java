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

package tech.mcprison.prison.ranks.events;

import tech.mcprison.prison.ranks.data.RankPlayer;

/**
 * An event that fires when a player joins the server for the first time.
 * In reality, this is posted when a player's files are created in the {@link tech.mcprison.prison.ranks.managers.PlayerManager}.
 *
 * @author Faizaan A. Datoo
 */
public class FirstJoinEvent {

    /*
     * Fields & Constants
     */

    private RankPlayer player;

    /*
     * Constructors
     */

    public FirstJoinEvent(RankPlayer player) {
        this.player = player;
    }

    /*
     * Getters & Setters
     */

    public RankPlayer getPlayer() {
        return player;
    }

}

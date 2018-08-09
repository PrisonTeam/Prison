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

package xyz.faizaan.prison.selection;

import xyz.faizaan.prison.internal.Player;

/**
 * Fired when both selections in a {@link Selection} are made.
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class SelectionCompletedEvent {

    private Player player;
    private Selection selection;

    public SelectionCompletedEvent(Player player, Selection selection) {
        this.player = player;
        this.selection = selection;
    }

    public Player getPlayer() {
        return player;
    }

    public Selection getSelection() {
        return selection;
    }

}

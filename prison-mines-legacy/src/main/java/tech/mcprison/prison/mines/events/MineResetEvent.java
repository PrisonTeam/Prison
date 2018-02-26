/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2017 The Prison Team
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

package tech.mcprison.prison.mines.events;

import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.mines.Mine;

/**
 * Represents an event called when a mine is being reset
 */
public class MineResetEvent implements Cancelable {

    private Mine mine;
    private boolean canceled = false; // false by default

    public MineResetEvent(Mine mine) {
        this.mine = mine;
    }

    /**
     * Gets the mine associated with this event
     *
     * @return the mine associated with this event
     */
    public Mine getMine() {
        return mine;
    }

    /**
     * Checks to see if this event has been canceled
     *
     * @return true if this event has been canceled, false otherwise
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Sets the canceled status of this event
     *
     * @param canceled the new canceled status of this event
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}

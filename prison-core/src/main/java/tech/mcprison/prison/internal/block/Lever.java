/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.internal.block;

/**
 * Represents the state of a lever.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Lever extends BlockState {

    /**
     * Returns whether the lever is on or off.
     * On is defined as emitting a redstone signal.
     *
     * @return true if the lever is on, false if it's off.
     */
    boolean isOn();

    /**
     * Set a lever as on (i.e. emitting redstone current) or off.
     *
     * @param on true to power it, false otherwise.
     */
    void setOn(boolean on);

}

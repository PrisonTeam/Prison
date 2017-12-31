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

package tech.mcprison.prison.mines;

/**
 * A filter usable with certain methods in {@link MineManager}
 */
public interface MinesFilter {

    /**
     * Used in {@link MineManager#select(MinesFilter)}
     * <p>
     * Check if this {@link MinesFilter} accepts the {@link Mine}. If it does, continue operating.
     *
     * @param c the mine in question
     * @return true if accepted, false otherwise
     */
    boolean accept(Mine c);

    /**
     * Used in {@link MineManager#forEach(MinesFilter)}
     * <p>
     * Execute methods related to the specified {@link Mine}.
     *
     * @param c the mine to execute on
     */
    void action(Mine c);

}

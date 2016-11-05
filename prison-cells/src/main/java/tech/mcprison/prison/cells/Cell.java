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

package tech.mcprison.prison.cells;

import tech.mcprison.prison.util.Bounds;

/**
 * @author SirFaizdat
 */
public class Cell {

    private long id;
    private Bounds bounds;

    /**
     * For serialization purposes only.
     */
    public Cell() {
    }

    /**
     * Create a new cell.
     *
     * @param id     The ID to use for the new cell.
     * @param bounds The boundaries that this cell will occupy.
     */
    public Cell(long id, Bounds bounds) {
        this.id = id;
        this.bounds = bounds;
    }

    public long getId() {
        return id;
    }

    public Bounds getBounds() {
        return bounds;
    }

}

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
import tech.mcprison.prison.util.Location;

/**
 * @author SirFaizdat
 */
public class Cell {

    private int id;
    private Bounds bounds;
    private Location doorLocation;

    /**
     * For serialization purposes.
     */
    public Cell() {
    }

    public Cell(int id, Bounds bounds) {
        this.id = id;
        this.bounds = bounds;
    }

    public int getId() {
        return id;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public Location getDoorLocation() {
        return doorLocation;
    }

    public void setDoorLocation(Location doorLocation) {
        this.doorLocation = doorLocation;
    }

}

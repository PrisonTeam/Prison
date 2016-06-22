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

package io.github.prison.cells;

import io.github.prison.util.Bounds;
import io.github.prison.util.Location;

/**
 * Represents a single cell.
 * All values are serializable.
 *
 * @author SirFaizdat
 */
public class Cell {

    private int cellId;
    private Bounds bounds;
    private Location doorLocation;
    private CellUser owner;

    public Cell() {
    }

    public Cell(int cellId, Bounds bounds, CellUser owner) {
        this.cellId = cellId;
        this.bounds = bounds;
        this.owner = owner;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public Location getDoorLocation() {
        return doorLocation;
    }

    public void setDoorLocation(Location doorLocation) {
        this.doorLocation = doorLocation;
    }

    public CellUser getOwner() {
        return owner;
    }

    public void setOwner(CellUser owner) {
        this.owner = owner;
    }
}

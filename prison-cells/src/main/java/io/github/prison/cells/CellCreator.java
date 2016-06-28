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

import io.github.prison.internal.Player;
import io.github.prison.util.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stores the instances of the in-progress cells before they are complete.
 *
 * @author SirFaizdat
 */
public class CellCreator {

    private CellsModule cellsModule;
    private Map<UUID, Cell> cellMap;

    public CellCreator(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
        this.cellMap = new HashMap<>();
    }

    /**
     * Commence a cell's setup. The cell's bounderies should already have been set.
     *
     * @param creator The {@link Player} that is creating the cell.
     * @param cell    The {@link Cell} being created.
     */
    public void commence(Player creator, Cell cell) {
        cellMap.put(creator.getUUID(), cell);
    }

    /**
     * Gives the player's cell its door location.
     *
     * @param player       The {@link Player} that is creating the cell.
     * @param doorLocation The {@link Location} where the door is.
     */
    public void addDoorLocation(Player player, Location doorLocation) {
        Cell cell = getCell(player);
        if(cell == null) return;
        cell.setDoorLocation(doorLocation);
        cellMap.put(player.getUUID(), cell);
    }

    /**
     * Complete the creation of the cell. This will register the cell and save it to a file,
     * and then remove it from this cell creator.
     *
     * @param player The {@link Player} that created the cell.
     * @return the ID of the new cell.
     * @see CellsModule#saveCell(Cell)
     */
    public int complete(Player player) {
        Cell cell = getCell(player);
        cellsModule.saveCell(cell);
        cellMap.remove(player.getUUID());

        return cell.getId();
    }

    public Cell getCell(Player player) {
        return cellMap.get(player.getUUID());
    }

}

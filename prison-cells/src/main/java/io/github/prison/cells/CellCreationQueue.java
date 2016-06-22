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

import java.util.HashMap;
import java.util.Map;

/**
 * This queue stores incomplete cells correlated with the players who are setting them up.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class CellCreationQueue {

    private CellsModule cellsModule;
    private Map<String, Cell> queue = new HashMap<>();

    public CellCreationQueue(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }

    /**
     * Returns true if the player is present in the queue.
     */
    public boolean isQueued(Player player) {
        return queue.containsKey(player.getName());
    }

    /**
     * Returns the in-progress cell of a player present in the queue.
     */
    public Cell getQueuedCell(Player player) {
        return queue.get(player.getName());
    }

    /**
     * Set the in-progress cell of a player in the queue.
     */
    public void setQueuedCell(Player player, Cell cell) {
        queue.put(player.getName(), cell);
    }

    /**
     * Saves the cell and removes it from the queue.
     */
    public void complete(Player player) {
        if (!isQueued(player)) return;
        Cell cell = getQueuedCell(player);
        cellsModule.saveCell(cell);
        queue.remove(player.getName());
        player.sendMessage(String.format(cellsModule.getMessages().cellCreationSuccess, cell.getCellId()));
    }

    /**
     * Cancel a cell's creation. The cell will not be saved.
     */
    public void cancel(Player player) {
        queue.remove(player.getName());
    }

}

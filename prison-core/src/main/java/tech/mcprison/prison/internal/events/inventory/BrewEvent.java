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

package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.internal.inventory.BrewerInventory;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class BrewEvent implements Cancelable {

    private boolean canceled = false;
    private BrewerInventory contents = null;
    private int fuelLevel = 0;
    private Block block = null;

    public BrewEvent(Block block, BrewerInventory contents, int fuelLevel) {
        this.contents = contents;
        this.fuelLevel = fuelLevel;
        this.block = block;
    }

    public BrewerInventory getContents() {
        return contents;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public Block getBlock() {
        return block;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

}

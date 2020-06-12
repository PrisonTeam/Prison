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

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.events.Cancelable;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class FurnaceBurnEvent implements Cancelable {

    private boolean canceled = false;
    private int burnTime = -1;
    private ItemStack fuel = null;
    private boolean burning = false;
    private Block block;

    public FurnaceBurnEvent(Block furnace, ItemStack fuel, int burnTime, boolean burning) {
        block = furnace;
        this.fuel = fuel;
        this.burnTime = burnTime;
        this.burning = burning;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public boolean isBurning() {
        return burning;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public Block getBlock() {
        return block;
    }

}

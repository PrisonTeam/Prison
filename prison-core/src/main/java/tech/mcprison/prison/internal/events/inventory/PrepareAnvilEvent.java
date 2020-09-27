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
import tech.mcprison.prison.internal.inventory.AnvilInventory;
import tech.mcprison.prison.internal.inventory.Viewable;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class PrepareAnvilEvent extends InventoryEvent {

    private ItemStack result;

    public PrepareAnvilEvent(Viewable transaction, ItemStack result) {
        super(transaction);
        this.result = result;
    }

    public AnvilInventory getInventory() {
        return (AnvilInventory) getView().getTopInventory();
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

}

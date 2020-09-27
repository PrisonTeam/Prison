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
import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.internal.inventory.Inventory;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class InventoryMoveItemEvent implements Cancelable {

    private boolean sourceInitiated;
    private boolean canceled = false;
    private Inventory source;
    private Inventory destination;
    private ItemStack item;

    public InventoryMoveItemEvent(Inventory sourceInventory, ItemStack itemStack,
        Inventory destinationInventory, boolean didSourceInitiate) {
        this.source = sourceInventory;
        this.destination = destinationInventory;
        this.item = itemStack;
        this.sourceInitiated = didSourceInitiate;
    }

    public Inventory getDestination() {
        return destination;
    }

    public Inventory getInitiator() {
        return sourceInitiated ? source : destination;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack itemStack) {
        item = itemStack;
    }

    public Inventory getSource() {
        return source;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

}

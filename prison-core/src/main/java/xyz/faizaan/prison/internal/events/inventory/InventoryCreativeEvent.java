/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

package xyz.faizaan.prison.internal.events.inventory;

import xyz.faizaan.prison.internal.ItemStack;
import xyz.faizaan.prison.internal.inventory.InventoryType;
import xyz.faizaan.prison.internal.inventory.Viewable;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class InventoryCreativeEvent extends InventoryClickEvent {

    private ItemStack item;

    public InventoryCreativeEvent(Viewable transaction, InventoryType.SlotType type, int slot,
                                  ItemStack newItem) {
        super(transaction, type, slot, Click.CREATIVE, Action.PLACE_ALL);
        item = newItem;
    }

    public ItemStack getCursor() {
        return item;
    }

    public void setCursor(ItemStack item) {
        this.item = item;
    }

}

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

package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents an enchanting inventory (menu), typically created by an Enchanting Table
 *
 * @author Dylan M. Perks
 * @since API 1.0
 */
public interface EnchantingInventory extends Inventory {

    /**
     * Gets the {@link ItemStack} that is being enchanted
     *
     * @return the item being enchanted
     */
    ItemStack getItem();

    /**
     * Sets the {@link ItemStack} being enchanted
     *
     * @param item the new item to be enchanted
     */
    void setItem(ItemStack item);

}

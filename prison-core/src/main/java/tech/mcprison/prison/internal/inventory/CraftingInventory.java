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
 * Represents a crafting inventory, typically created by a Crafting Table
 *
 * @author Dylan M. Perks
 * @since API 1.0
 */
public interface CraftingInventory extends Inventory {

    /**
     * Gets the items in the 9 slots
     *
     * @return an array of the items in the 9 crafting slots
     */
    ItemStack[] getMatrix();

    /**
     * Sets the items in the 9 slots
     *
     * @param contents the new contents of this crafting inventory
     */
    void setMatrix(ItemStack[] contents);

    /**
     * Gets the {@link Recipe}, if any, created in this crafting inventory
     *
     * @return the crafting recipe contained within this inventory
     */
    Recipe getRecipe();

    /**
     * Gets the resulting {@link} of the recipe created in this crafting inventory
     *
     * @return the result of the crafting recipe
     * @see Recipe
     */
    ItemStack getResult();

    /**
     * Sets the result of the recipe created in this crafting inventory, if any.
     * <p>
     * Most platforms allow this to be set even if there is no recipe registered with the server.
     *
     * @param newResult the result of a created recipe
     */
    void setResult(ItemStack newResult);

}

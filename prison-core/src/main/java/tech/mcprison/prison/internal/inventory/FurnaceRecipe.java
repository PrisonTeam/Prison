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
import tech.mcprison.prison.util.BlockType;

/**
 * Represents a recipe used in a furnace, consisting of an input and a result.
 *
 * @author Dylan M. Perks
 * @since API 1.0
 */
public interface FurnaceRecipe extends Recipe {

    /**
     * Gets the input (item to be smelted) of this recipe
     *
     * @return the {@link ItemStack} to be smelted
     */
    ItemStack getInput();

    /**
     * Sets the input (item to be smelted) in this recipe
     *
     * @param input the input of this recipe ()
     */
    FurnaceRecipe setInput(BlockType input);

    /**
     * Gets the {@link ItemStack} created when the input is smelted.
     */
    ItemStack getResult();

}

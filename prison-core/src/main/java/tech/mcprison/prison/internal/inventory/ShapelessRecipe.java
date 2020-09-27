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

import java.util.List;

/**
 * Represents a shapeless recipe (a crafting recipe that has no requirement on a specific shape)
 *
 * @author Dylan M. Perks
 * @since API 1.0
 */
public interface ShapelessRecipe extends Recipe {

    /**
     * Adds an ingredient to this recipe
     *
     * @param count      the amount of the ingredient required to complete the recipe
     * @param ingredient the ingredient to add to the recipe
     * @return this instance to allow chain calls
     */
    ShapelessRecipe addIngredient(int count, BlockType ingredient);

    /**
     * Adds an ingredient to this recipe
     *
     * @param ingredient the ingredient to add to the recipe
     * @return this instance to allow chain calls
     */
    ShapelessRecipe addIngredient(BlockType ingredient);

    /**
     * Gets all the ingredients in this recipe
     *
     * @return a list containing all the ingredients
     */
    List<ItemStack> getIngredientList();

    /**
     * Removes an ingredient from this recipe
     *
     * @param count      the amount of the ingredient to remove
     * @param ingredient the ingredient to remove from this recipe
     * @return this instance to allow chain calls
     */
    ShapelessRecipe removeIngredient(int count, BlockType ingredient);

    /**
     * Removes an ingredient from this recipe
     *
     * @param ingredient the ingredient to remove from this recipe
     * @return this instance to allow chain calls
     */
    ShapelessRecipe removeIngredient(BlockType ingredient);

}

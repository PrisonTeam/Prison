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

import java.util.Map;

/**
 * Represents a shaped crafting recipe (a recipe that must be of a specific shape to be completed).
 * An example of this is the crafting recipe for a Bow.
 */
public interface ShapedRecipe extends Recipe {

    /**
     * Gets the ingredient map, where the value is the item represented by the key in the shape
     *
     * @return the ingredient map
     */
    Map<Character, ItemStack> getIngredientMap();

    /**
     * Gets the shape of this recipe. In a crafting table, this is the shape that must be preserved
     * for the recipe to be complete
     *
     * @return the shape of the recipe
     */
    String[] getShape();

    /**
     * Maps an ingredient to a character.
     * <p>
     * In the shape() method, you set the shape using different characters for different items.
     * This method assigns a character to an ingredient (item). See the documentation for shape()
     * for more information.
     * </p>
     *
     * @param key        the character to assign the ingredient to
     * @param ingredient the ingredient
     * @return this instance to allow chain calls
     */
    ShapedRecipe setIngredient(char key, BlockType ingredient);

    /**
     * Sets the shape of this recipe <p><b>Example:</b> If you assign letter D to Diamond and S to
     * Stick, the following recipe would give you a Diamond Pickaxe: <b>"DDD", "-S-", "-S-"</b>
     *
     * @param shape the new shape of this recipe
     * @return this instance to allow chain calls
     */
    ShapedRecipe shape(String... shape);

}

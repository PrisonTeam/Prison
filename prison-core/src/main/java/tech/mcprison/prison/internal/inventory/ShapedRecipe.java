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
     * Sets the shape of this recipe
     * <p><b>Example:</b>
     * If you assign letter D to Diamond and S to Stick, the following recipe would give you a Diamond Pickaxe:
     * <b>"DDD", "-S-", "-S-"</b>
     *
     * @param shape the new shape of this recipe
     * @return this instance to allow chain calls
     */
    ShapedRecipe shape(String... shape);
}

package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.util.BlockType;

import java.util.List;

/**
 * Represents a shapeless recipe (a crafting recipe that has no requirement on a specific shape)
 *
 * @author Dylan M. Perks
 * @since API 0.1
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

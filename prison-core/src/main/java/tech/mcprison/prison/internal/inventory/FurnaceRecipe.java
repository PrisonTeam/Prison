package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.util.BlockType;

/**
 * Represents a recipe used in a furnace, consisting of an input and a result.
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public interface FurnaceRecipe extends Recipe {
    /**
     * Gets the input (item to be smelted) of this recipe
     * @return the {@link ItemStack} to be smelted
     */
    ItemStack getInput();

    /**
     * Gets the {@link ItemStack} created when the input is smelted.
     * @return
     */
    ItemStack getResult();

    /**
     * Sets the input (item to be smelted) in this recipe
     * @param input the input of this recipe ()
     * @return
     */
    FurnaceRecipe setInput(BlockType input);
}

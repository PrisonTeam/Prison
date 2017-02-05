package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents a crafting inventory, typically created by a Crafting Table
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public interface CraftingInventory extends Inventory {
    /**
     * Gets the items in the 9 slots
     * @return an array of the items in the 9 crafting slots
     */
    ItemStack[] getMatrix();

    /**
     * Gets the {@link Recipe}, if any, created in this crafting inventory
     * @return the crafting recipe contained within this inventory
     */
    Recipe getRecipe();

    /**
     * Gets the resulting {@link} of the recipe created in this crafting inventory
     * @return the result of the crafting recipe
     * @see Recipe
     */
    ItemStack getResult();

    /**
     * Sets the items in the 9 slots
     * @param contents the new contents of this crafting inventory
     */
    void setMatrix(ItemStack[] contents);

    /**
     * Sets the result of the recipe created in this crafting inventory, if any.
     * <p>
     * Most platforms allow this to be set even if there is no recipe registered with the server.
     * @param newResult the result of a created recipe
     */
    void setResult(ItemStack newResult);
}

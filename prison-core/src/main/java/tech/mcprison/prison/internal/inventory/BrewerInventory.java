package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents a brewing inventory, typically created by a {@link tech.mcprison.prison.internal.block.BrewingStand}
 *
 * @since API 0.1
 */
public interface BrewerInventory extends Inventory {

    /**
     * Gets the ingredient (item at the top of the inventory) of this inventory
     *
     * @return the ingredient {@link ItemStack}
     */
    ItemStack getIngredient();

    /**
     * Sets the ingredient (item at the top of the inventory) of this inventory
     *
     * @param ingredient the ingredient {@link ItemStack}
     */
    void setIngredient(ItemStack ingredient);
}

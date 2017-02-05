package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents the barebones for crafting recipe
 *
 * @since API 1.1
 * @author Dylan M. Perks
 */
public interface Recipe {
    /**
     * Gets the result of this recipe
     * @return the resulting item
     */
    ItemStack getResult();
}

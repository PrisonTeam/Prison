package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents the barebones for crafting recipe
 *
 * @author Dylan M. Perks
 * @since API 0.1
 */
public interface Recipe {
    /**
     * Gets the result of this recipe
     *
     * @return the resulting item
     */
    ItemStack getResult();
}

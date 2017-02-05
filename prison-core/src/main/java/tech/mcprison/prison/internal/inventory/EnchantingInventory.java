package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents an enchanting inventory (menu), typically created by an Enchanting Table
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public interface EnchantingInventory extends Inventory {
    /**
     * Gets the {@link ItemStack} that is being enchanted
     *
     * @return the item being enchanted
     */
    ItemStack getItem();

    /**
     * Sets the {@link ItemStack} being enchanted
     *
     * @param item the new item to be enchanted
     */
    void setItem(ItemStack item);
}

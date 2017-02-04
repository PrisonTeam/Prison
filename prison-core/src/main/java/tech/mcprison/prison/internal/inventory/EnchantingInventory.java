package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface EnchantingInventory extends Inventory {
    ItemStack getItem();

    void setItem(ItemStack item);
}

package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Created by DMP9 on 03/02/2017.
 */
public interface BeaconInventory {
    ItemStack getItem();

    void setItem(ItemStack item);
}

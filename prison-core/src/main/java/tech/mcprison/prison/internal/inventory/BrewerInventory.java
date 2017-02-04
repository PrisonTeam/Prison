package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface BrewerInventory extends Inventory {

    ItemStack getIngredient();

    void setIngredient(ItemStack ingredient);
}

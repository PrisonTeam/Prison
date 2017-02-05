package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface CraftingInventory extends Inventory {
    ItemStack[] getMatrix();

    void setMatrix(ItemStack[] contents);

    Recipe getRecipe();

    ItemStack getResult();

    void setResult(ItemStack newResult);
}

package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.util.BlockType;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface FurnaceRecipe extends Recipe {
    ItemStack getInput();

    ItemStack	getResult();

    FurnaceRecipe	setInput(BlockType input);
}

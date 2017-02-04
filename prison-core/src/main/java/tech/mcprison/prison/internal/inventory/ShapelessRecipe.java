package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.util.BlockType;

import java.util.List;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface ShapelessRecipe extends Recipe {
    ShapelessRecipe	addIngredient(int count, BlockType ingredient);

    ShapelessRecipe	addIngredient(BlockType ingredient);

    List<ItemStack> getIngredientList();

    ItemStack	getResult();

    ShapelessRecipe	removeIngredient(int count, BlockType ingredient);

    ShapelessRecipe	removeIngredient(BlockType ingredient);
}

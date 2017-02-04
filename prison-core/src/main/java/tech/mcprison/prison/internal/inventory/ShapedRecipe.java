package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.util.BlockType;

import java.util.Map;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface ShapedRecipe extends Recipe {
    Map<Character, ItemStack> getIngredientMap();

    ItemStack	getResult();

    String[] getShape();

    ShapedRecipe setIngredient(char key, BlockType ingredient);

    ShapedRecipe shape(String... shape);
}

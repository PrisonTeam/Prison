package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.ShapedRecipe;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.BlockType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotShapedRecipe extends SpigotRecipe implements ShapedRecipe {
    public SpigotShapedRecipe(org.bukkit.inventory.ShapedRecipe wrapper) {
        super(wrapper);
    }

    @Override public Map<Character, ItemStack> getIngredientMap() {
        Map<Character, org.bukkit.inventory.ItemStack> stackMap =
            ((org.bukkit.inventory.ShapedRecipe) getWrapper()).getIngredientMap();
        Map<Character, ItemStack> result = new HashMap<>();
        stackMap.forEach((x, y) -> result.put(x, SpigotUtil.bukkitItemStackToPrison(y)));
        return result;
    }

    @Override public String[] getShape() {
        return ((org.bukkit.inventory.ShapedRecipe) getWrapper()).getShape();
    }

    @Override public ShapedRecipe setIngredient(char key, BlockType ingredient) {
        ((org.bukkit.inventory.ShapedRecipe) getWrapper())
            .setIngredient(key, SpigotUtil.blockTypeToMaterial(ingredient));
        return this;
    }

    @Override public ShapedRecipe shape(String... shape) {
        ((org.bukkit.inventory.ShapedRecipe) getWrapper()).shape(shape);
        return this;
    }
}

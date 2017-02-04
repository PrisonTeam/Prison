package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.ShapelessRecipe;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.BlockType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotShapelessRecipe extends SpigotRecipe implements ShapelessRecipe {
    public SpigotShapelessRecipe(org.bukkit.inventory.ShapelessRecipe wrapper) {
        super(wrapper);
    }

    @Override public ShapelessRecipe addIngredient(int count, BlockType ingredient) {
        ((org.bukkit.inventory.ShapelessRecipe)getWrapper()).addIngredient(count, SpigotUtil.blockTypeToMaterial(ingredient));
        return this;
    }

    @Override public ShapelessRecipe addIngredient(BlockType ingredient) {
        ((org.bukkit.inventory.ShapelessRecipe)getWrapper()).addIngredient(SpigotUtil.blockTypeToMaterial(ingredient));
        return this;
    }

    @Override public List<ItemStack> getIngredientList() {
        List<org.bukkit.inventory.ItemStack> bukkit = ((org.bukkit.inventory.ShapelessRecipe)getWrapper()).getIngredientList();
        List<ItemStack> result = new ArrayList<>();
        bukkit.forEach(x -> result.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return result;
    }

    @Override public ShapelessRecipe removeIngredient(int count, BlockType ingredient) {
        ((org.bukkit.inventory.ShapelessRecipe)getWrapper()).removeIngredient(count, SpigotUtil.blockTypeToMaterial(ingredient));
        return this;
    }

    @Override public ShapelessRecipe removeIngredient(BlockType ingredient) {
        ((org.bukkit.inventory.ShapelessRecipe)getWrapper()).removeIngredient(SpigotUtil.blockTypeToMaterial(ingredient));
        return this;
    }
}

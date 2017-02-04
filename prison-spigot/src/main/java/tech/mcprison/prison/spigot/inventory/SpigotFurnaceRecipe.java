package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.FurnaceRecipe;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.BlockType;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotFurnaceRecipe extends SpigotRecipe implements FurnaceRecipe {
    public SpigotFurnaceRecipe(org.bukkit.inventory.FurnaceRecipe wrapper) {
        super(wrapper);
    }

    @Override public ItemStack getInput() {
        return SpigotUtil.bukkitItemStackToPrison(((org.bukkit.inventory.FurnaceRecipe)getWrapper()).getInput());
    }

    @Override public FurnaceRecipe setInput(BlockType input) {
        ((org.bukkit.inventory.FurnaceRecipe)getWrapper()).setInput(SpigotUtil.blockTypeToMaterial(input));
        return this;
    }
}

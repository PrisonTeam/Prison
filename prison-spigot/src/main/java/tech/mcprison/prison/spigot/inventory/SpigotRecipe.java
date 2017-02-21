package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.Recipe;
import tech.mcprison.prison.spigot.SpigotUtil;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotRecipe implements Recipe {
    org.bukkit.inventory.Recipe wrapper;

    public SpigotRecipe(org.bukkit.inventory.Recipe wrapper) {
        this.wrapper = wrapper;
    }

    public org.bukkit.inventory.Recipe getWrapper() {
        return wrapper;
    }

    @Override public ItemStack getResult() {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getResult());
    }
}

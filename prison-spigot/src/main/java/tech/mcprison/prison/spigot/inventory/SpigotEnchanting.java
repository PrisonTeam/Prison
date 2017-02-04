package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.EnchantingInventory;
import tech.mcprison.prison.spigot.SpigotUtil;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotEnchanting extends SpigotInventory implements EnchantingInventory {
    public SpigotEnchanting(org.bukkit.inventory.EnchantingInventory wrapper) {
        super(wrapper);
    }

    @Override
    public ItemStack getItem() {
        return SpigotUtil.bukkitItemStackToPrison(((org.bukkit.inventory.EnchantingInventory)getWrapper()).getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        ((org.bukkit.inventory.EnchantingInventory)getWrapper()).setItem(SpigotUtil.prisonItemStackToBukkit(item));
    }
}

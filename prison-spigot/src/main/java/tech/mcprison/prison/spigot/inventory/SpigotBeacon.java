package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.BeaconInventory;
import tech.mcprison.prison.spigot.SpigotUtil;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotBeacon extends SpigotInventory implements BeaconInventory {
    public SpigotBeacon(org.bukkit.inventory.BeaconInventory wrapper) {
        super(wrapper);
    }

    @Override public ItemStack getItem() {
        return SpigotUtil.bukkitItemStackToPrison(((org.bukkit.inventory.BeaconInventory)getWrapper()).getItem());
    }

    @Override public void setItem(ItemStack item) {
        ((org.bukkit.inventory.BeaconInventory)getWrapper()).setItem(SpigotUtil.prisonItemStackToBukkit(item));
    }
}

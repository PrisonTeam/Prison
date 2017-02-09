package tech.mcprison.prison.spigot.inventory;

import org.bukkit.block.BrewingStand;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.BrewerInventory;
import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotBrewingStand;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotBrewer extends SpigotInventory implements BrewerInventory {
    public SpigotBrewer(org.bukkit.inventory.BrewerInventory wrapper) {
        super(wrapper);
    }

    @Override public ItemStack getIngredient() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.BrewerInventory) getWrapper()).getIngredient());
    }

    @Override public void setIngredient(ItemStack ingredient) {
        ((org.bukkit.inventory.BrewerInventory) getWrapper())
            .setIngredient(SpigotUtil.prisonItemStackToBukkit(ingredient));
    }

    @Override public InventoryHolder getHolder() {
        return new SpigotBrewingStand((BrewingStand) getWrapper().getHolder());
    }
}

package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.inventory.DoubleChestInventory;
import tech.mcprison.prison.internal.inventory.Inventory;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotDoubleChest extends SpigotInventory implements DoubleChestInventory {
    public SpigotDoubleChest(org.bukkit.inventory.DoubleChestInventory wrapper) {
        super(wrapper);
    }

    @Override
    public Inventory getLeftSide() {
        return new SpigotInventory(((org.bukkit.inventory.DoubleChestInventory)getWrapper()).getLeftSide());
    }

    @Override
    public Inventory getRightSide() {
        return new SpigotInventory(((org.bukkit.inventory.DoubleChestInventory)getWrapper()).getLeftSide());
    }
}

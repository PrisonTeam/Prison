package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.inventory.AnvilInventory;

/**
 * Created by DMP9 on 03/02/2017.
 */
public class SpigotAnvil extends SpigotInventory implements AnvilInventory {
    public SpigotAnvil(org.bukkit.inventory.AnvilInventory wrapper) {
        super(wrapper);
    }
}

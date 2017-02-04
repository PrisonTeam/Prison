package tech.mcprison.prison.spigot.inventory;

import org.bukkit.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.AnvilInventory;

/**
 * Created by DMP9 on 03/02/2017.
 */
public class SpigotAnvil extends SpigotInventory implements AnvilInventory {
    public SpigotAnvil(Inventory wrapper) {
        super(wrapper);
    }

    public int getSize(){
        return 3;
    }
    public void setSize(int size){

    }
}

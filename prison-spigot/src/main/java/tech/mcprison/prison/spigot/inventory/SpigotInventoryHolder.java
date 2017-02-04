package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.spigot.SpigotUtil;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotInventoryHolder implements InventoryHolder {
    org.bukkit.inventory.InventoryHolder wrapper;
    public SpigotInventoryHolder(org.bukkit.inventory.InventoryHolder wrapper){
        this.wrapper = wrapper;
    }
    public Inventory getInventory(){
        switch (SpigotUtil.bukkitInventoryTypeToPrison(wrapper.getInventory().getType())){
            case ANVIL:
                return new SpigotAnvil(wrapper.getInventory());
        }
        return null;
    }
}

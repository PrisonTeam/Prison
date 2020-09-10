package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class Spigot113GUI 
		extends Spigot113Blocks 
		implements CompatibilityGUI {

    @Override
    public String getTitle(InventoryClickEvent e){
        return e.getView().getTitle();
    }

}

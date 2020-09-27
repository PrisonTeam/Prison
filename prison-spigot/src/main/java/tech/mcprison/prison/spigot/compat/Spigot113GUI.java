package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryEvent;

public abstract class Spigot113GUI 
		extends Spigot113Blocks 
		implements CompatibilityGUI {

    @Override
    public String getGUITitle(InventoryEvent e){
        return e.getView().getTitle();
    }

}

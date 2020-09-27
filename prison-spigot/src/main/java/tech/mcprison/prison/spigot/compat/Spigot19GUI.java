package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryEvent;

public class Spigot19GUI 
		extends Spigot18Blocks 
		implements CompatibilityGUI {
	
    @Override
    public String getGUITitle(InventoryEvent e){
        return e.getView().getTitle();
    }
}

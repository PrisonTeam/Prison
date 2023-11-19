package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryEvent;

public class Spigot_1_9_GUI 
		extends Spigot_1_9_Player 
		implements CompatibilityGUI {
	
    @Override
    public String getGUITitle(InventoryEvent e){
        return e.getView().getTitle();
    }
}

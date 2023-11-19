package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryEvent;

public abstract class Spigot_1_13_GUI 
		extends Spigot_1_13_Blocks 
		implements CompatibilityGUI {

    @Override
    public String getGUITitle(InventoryEvent e){
        return e.getView().getTitle();
    }

}

package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryEvent;

public abstract class Spigot_1_8_GUI 
		extends Spigot_1_8_Blocks 
		implements CompatibilityGUI {

    @SuppressWarnings( "deprecation" )
    @Override
    public String getGUITitle(InventoryEvent e){
        return e.getInventory().getTitle();
    }

}

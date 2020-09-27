package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryEvent;

public abstract class Spigot18GUI 
		extends Spigot18Blocks 
		implements CompatibilityGUI {

    @SuppressWarnings( "deprecation" )
    @Override
    public String getGUITitle(InventoryEvent e){
        return e.getInventory().getTitle();
    }

}

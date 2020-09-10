package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class Spigot18GUI 
		extends Spigot18Blocks 
		implements CompatibilityGUI {

    @SuppressWarnings( "deprecation" )
    @Override
    public String getTitle(InventoryClickEvent e){
        return e.getInventory().getTitle();
    }

}

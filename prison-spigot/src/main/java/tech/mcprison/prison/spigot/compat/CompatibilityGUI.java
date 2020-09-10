package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryEvent;

public interface CompatibilityGUI extends CompatibilityBlocks {

    public String getGUITitle(InventoryEvent e);
}

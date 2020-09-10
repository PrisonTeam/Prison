package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface CompatibilityGUI extends CompatibilityBlocks {

    public String getTitle(InventoryClickEvent e);
}

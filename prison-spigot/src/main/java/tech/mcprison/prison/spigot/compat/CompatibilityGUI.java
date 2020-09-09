package tech.mcprison.prison.spigot.compat;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface CompatibilityGUI extends CompatibilityBlocks {

    String getTitle(InventoryClickEvent e);
}

package tech.mcprison.prison.spigot.gui.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackPacksListeners;
import tech.mcprison.prison.spigot.backpacks.BackPacksUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

public class SpigotPlayerBackPacksGUI extends SpigotGUIComponents {

    private final Player p;
    private final BackPacksUtil backPacksUtil = BackPacksUtil.get();
    int dimension = 54;

    public SpigotPlayerBackPacksGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Create the inventory
        dimension = backPacksUtil.getBackpackSize(p);
        Inventory inv = Bukkit.createInventory(p, dimension, SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        inv = backPacksUtil.getInventory(p, inv);
        // Open the inventory
        if (inv != null) {
            //p.sendMessage(""); // Space
            //p.sendMessage("I'm opening a Backpack (Debug message from SpigotPlayerBackPacksGUI class)!");
            p.openInventory(inv);
            BackPacksListeners.get().addToBackpackActive(p);
            BackPacksListeners.get().removeFromHasClosedBackpack(p);
        }
    }
}

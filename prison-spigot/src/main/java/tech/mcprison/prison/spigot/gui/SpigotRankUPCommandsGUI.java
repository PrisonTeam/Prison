package tech.mcprison.prison.spigot.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.data.Rank;

public class SpigotRankUPCommandsGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;
    private Rank rank;

    public SpigotRankUPCommandsGUI(Player p, Rank rank) {
        this.p = p;
        this.rank = rank;
    }

    public void open() {

        // Init the ItemStack
        ItemStack itemcommand;

        // Init the lore array with default values for ladders
        List<String> commandslore = createLore(
        "§cPress Shift + Right click to delete");

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil(rank.rankUpCommands.size() / 9D)*9;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3Ranks -> RankUPCommands");

        // For every command make a button
        for (String command : rank.rankUpCommands) {
            // Make the button with materials, amount, lore and name
            itemcommand = createButton(Material.TRIPWIRE_HOOK, 1, commandslore, "§6" + rank.name + " " + command);

            // Add the button to the inventory
            inv.addItem(itemcommand);
        }

        // Open the inventory
        this.p.openInventory(inv);

    }
}

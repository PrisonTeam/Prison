package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
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

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil(rank.rankUpCommands.size() / 9D)*9;

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format("&cSorry, but there're too many RankupCommands and the max's 54 for the GUI"));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Ranks -> RankUPCommands"));

        // For every command make a button
        for (String command : rank.rankUpCommands) {

            // Init the lore array with default values for ladders
            List<String> commandslore = createLore(
                    "&cPress Shift + Right click to delete.",
                    "",
                    "&8&l|&3Info&8|");

            // Adding a lore
            commandslore.add(SpigotPrison.format("&3Command: &7" + command));

            // Make the button with materials, amount, lore and name
            itemcommand = createButton(Material.TRIPWIRE_HOOK, 1, commandslore, SpigotPrison.format("&3" + rank.name + " " + command));

            // Add the button to the inventory
            inv.addItem(itemcommand);
        }

        // Open the inventory
        this.p.openInventory(inv);

    }
}

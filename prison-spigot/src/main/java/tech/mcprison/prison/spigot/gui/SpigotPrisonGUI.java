package tech.mcprison.prison.spigot.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class SpigotPrisonGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;

    public SpigotPrisonGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3PrisonManager"));

        // Lore of the button
        List<String> rankslore = createLore(
        		"&8Ranks GUI manager.",
        		"&8Click to open.");

        // Lore of the button
        List<String> prisontaskslore = createLore(
                "&8Prison Tasks GUI manager.",
                "&8Click to open.");

        // Lore of the button
        List<String> mineslore = createLore(
                "&8Mines GUI manager.",
                "&8Click to open.");

        // Create the button, set up the material, amount, lore and name
        ItemStack ranks = createButton(Material.TRIPWIRE_HOOK, 1, rankslore, SpigotPrison.format("&3" + "Ranks"));

        // Create the button, set up the material, amount, lore and name
        ItemStack prisontasks = createButton(Material.IRON_PICKAXE, 1, prisontaskslore, SpigotPrison.format("&3" + "AutoManager"));

        // Create the button, set up the material, amount, lore and name
        ItemStack mines = createButton(Material.DIAMOND_ORE, 1, mineslore, SpigotPrison.format("&3" + "Mines"));

        //Position of the button
        inv.setItem(10, ranks);

        //Position of the button
        inv.setItem(13, prisontasks);

        //Position of the button
        inv.setItem(16, mines);

        // Open the inventory
        this.p.openInventory(inv);
    }

}

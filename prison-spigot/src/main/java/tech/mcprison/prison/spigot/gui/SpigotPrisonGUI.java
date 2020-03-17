package tech.mcprison.prison.spigot.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpigotPrisonGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;

    public SpigotPrisonGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3PrisonManager");



        // The Ranks button
        // Lore of the button
        List<String> rankslore = createLore(
        		"§8Ranks GUI manager.",
        		"§8Click to open.");

        // Create the button, set up the material, amount, lore and name
        ItemStack ranks = createButton(Material.TRIPWIRE_HOOK, 1, rankslore, "§3" + "Ranks");

        //Position of the button
        inv.setItem(dimension - 17, ranks);



        // The Prison Tasks button
        // Lore of the button
        List<String> prisontaskslore = createLore(
        		"§8Prison Tasks GUI manager.",
        		"§8Click to open.",
                "§d§lComing Soon.");

        // Create the button, set up the material, amount, lore and name
        ItemStack prisontasks = createButton(Material.IRON_PICKAXE, 1, prisontaskslore, "§3" + "Prison Tasks");

        //Position of the button
        inv.setItem(dimension - 14, prisontasks);



        // The mines button
        // Lore of the button
        List<String> mineslore = createLore(
        		"§8Mines GUI manager.",
        		"§8Click to open.");

        // Create the button, set up the material, amount, lore and name
        ItemStack mines = createButton(Material.DIAMOND_ORE, 1, mineslore, "§3" + "Mines");

        //Position of the button
        inv.setItem(dimension - 11, mines);

        // Open the inventory
        this.p.openInventory(inv);
    }

}

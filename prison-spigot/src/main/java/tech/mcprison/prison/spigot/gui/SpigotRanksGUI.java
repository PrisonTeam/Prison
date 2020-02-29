package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpigotRanksGUI {

    private int dimension = 27;
    private Player p;

    public SpigotRanksGUI(Player p){
        this.p = p;
    }

    private ItemStack createButton(Material id, int amount, List<String> lore, String display) {

        ItemStack item = new ItemStack(id, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3PrisonManager");



        // The Ranks button
        // Lore of the button
        List<String> rankslore = new ArrayList<>();
        rankslore.add("§3Ranks GUI manager");
        rankslore.add("§8Click to open");

        // Create the button, set up the material, amount, lore and name
        ItemStack ranks = createButton(Material.TRIPWIRE_HOOK, 1, rankslore, "§7" + "Ranks");

        //Position of the button
        inv.setItem(dimension - 17, ranks);



        // The Prison Tasks button
        // Lore of the button
        List<String> prisontaskslore = new ArrayList<>();
        prisontaskslore.add("§3Prison Tasks GUI manager");
        prisontaskslore.add("§8Click to open");

        // Create the button, set up the material, amount, lore and name
        ItemStack prisontasks = createButton(Material.IRON_PICKAXE, 1, prisontaskslore, "§b" + "Prison Tasks");

        //Position of the button
        inv.setItem(dimension - 14, prisontasks);



        // The mines button
        // Lore of the button
        List<String> mineslore = new ArrayList<>();
        mineslore.add("§3Mines GUI manager");
        mineslore.add("§8Click to open");

        // Create the button, set up the material, amount, lore and name
        ItemStack mines = createButton(Material.DIAMOND_ORE, 1, mineslore, "§1" + "Mines");

        //Position of the button
        inv.setItem(dimension - 11, mines);



        // Open the inventory
        this.p.openInventory(inv);
    }

}

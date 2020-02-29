package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.util.ArrayList;
import java.util.List;

public class SpigotLaddersGUI {

    private int dimension = 27;
    private Player p;

    public SpigotLaddersGUI(Player p){
        this.p = p;
    }

    private ItemStack createButton(Material id, int amount, List<String> lore, String display) {

        org.bukkit.inventory.ItemStack item = new ItemStack(id, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public void open(){

        // Init the ItemStack
        ItemStack itemladder;

        // Init the lore array with default values for ladders
        List<String> ladderslore = new ArrayList<>();
        ladderslore.add("§8Click to open");

        // Get the dimensions and if needed increases them
        while (dimension <= PrisonRanks.getInstance().getLadderManager().getLadders().toArray().length + 8){
            dimension = dimension + 9;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3RanksManager -> Ladders");

        // Make for every ladder a button
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()){
            itemladder = createButton(Material.LADDER, 1, ladderslore, "§6" + ladder.name);
            inv.addItem(itemladder);
        }

        // Open the inventory
        this.p.openInventory(inv);
    }

}

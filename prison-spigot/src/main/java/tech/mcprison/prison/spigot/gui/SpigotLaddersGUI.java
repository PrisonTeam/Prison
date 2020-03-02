package tech.mcprison.prison.spigot.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;

public class SpigotLaddersGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;

    public SpigotLaddersGUI(Player p){
        this.p = p;
    }

    public void open(){

        // Init the ItemStack
        ItemStack itemladder;
        
        LadderManager lm = PrisonRanks.getInstance().getLadderManager();

        // Init the lore array with default values for ladders
        List<String> ladderslore = createLore(
        "§8Click to open",
        "§cPress Shift + Right click to delete");

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil( lm.getLadders().size() / 9D)*9;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3RanksManager -> Ladders");

        // Make for every ladder a button
        for (RankLadder ladder : lm.getLadders()){

            // Create the button
            itemladder = createButton(Material.LADDER, 1, ladderslore, "§6" + ladder.name);

            // Add the button to the inventory
            inv.addItem(itemladder);
        }

        // Open the inventory
        this.p.openInventory(inv);
    }

}

package tech.mcprison.prison.spigot.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class SpigotLaddersGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;

    public SpigotLaddersGUI(Player p){
        this.p = p;
    }

    public void open(){

        // Init the ItemStack
        ItemStack itemladder;

        // Init variable
        LadderManager lm = PrisonRanks.getInstance().getLadderManager();

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil( lm.getLadders().size() / 9D)*9;

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format("&cSorry, but there're too many ladders and the max's 54 for the GUI"));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3RanksManager -> Ladders"));

        // Make for every ladder a button
        for (RankLadder ladder : lm.getLadders()){

            // Init the lore array with default values for ladders
            List<String> ladderslore = createLore(
                    "&8Click to open.",
                    "&cPress Shift + Right click to delete.");

            // Create the button
            itemladder = createButton(Material.LADDER, 1, ladderslore, SpigotPrison.format("&3" + ladder.name));

            // Add the button to the inventory
            inv.addItem(itemladder);
        }

        // Open the inventory
        this.p.openInventory(inv);
    }

}

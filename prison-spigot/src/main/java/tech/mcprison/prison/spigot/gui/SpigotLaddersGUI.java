package tech.mcprison.prison.spigot.gui;

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

public class SpigotLaddersGUI
	extends SpigotGUIComponents {

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
        		"§cPress Shift + Right click to delete", 
        		"", 
        		"§8§l|§3Info§8|");

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil( lm.getLadders().size() / 9D)*9;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3RanksManager -> Ladders");

        // Make for every ladder a button
        for (RankLadder ladder : lm.getLadders()){

            // Get the number of ranks in this ladder and...
            ladderslore.add("§8There're §3" + ladder.ranks.size() + " §3Ranks §8in this ladder:" );

            // Make a list of them
            for(RankLadder.PositionRank pos : ladder.ranks){
                Optional<Rank> rankOptional = ladder.getByPosition(pos.getPosition());

                // Well... check if the rank is null probably
                if (!rankOptional.isPresent()) {
                    continue; // Skip it
                }

                // Get the specific rank and add it
                ladderslore.add("§8§l|§3Rank§8| §8§l- " + rankOptional.get().name);
            }

            // Create the button
            itemladder = createButton(Material.LADDER, 1, ladderslore, "§6" + ladder.name);

            // Add the button to the inventory
            inv.addItem(itemladder);
        }

        // Open the inventory
        this.p.openInventory(inv);
    }

}

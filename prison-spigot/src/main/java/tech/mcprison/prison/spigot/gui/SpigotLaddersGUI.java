package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        ladderslore.add("§cPress Shift + Right click to delete");
        ladderslore.add("");
        ladderslore.add("§8§l|§3Info§8|");

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil( PrisonRanks.getInstance().getLadderManager().getLadders().size() / 9D)*9;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3RanksManager -> Ladders");

        // Make for every ladder a button
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()){

            // Get the number of ranks in this ladder and...
            ladderslore.add("§8There're §3" + PrisonRanks.getInstance().getLadderManager().getLadder(ladder.name).get().ranks.size() + " §3Ranks §8in this ladder:" );

            // Make a list of them
            for(RankLadder.PositionRank pos : PrisonRanks.getInstance().getLadderManager().getLadder(ladder.name).get().ranks){
                Optional<Rank> rankOptional = PrisonRanks.getInstance().getLadderManager().getLadder(ladder.name).get().getByPosition(pos.getPosition());

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

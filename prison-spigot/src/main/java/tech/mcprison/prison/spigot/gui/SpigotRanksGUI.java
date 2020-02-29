package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpigotRanksGUI {

    private int dimension = 27;
    private Player p;
    private Optional<RankLadder> ladder;

    public SpigotRanksGUI(Player p, Optional<RankLadder> ladder) {
        this.p = p;
        this.ladder = ladder;
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

    public void open() {

        // Init the ItemStack
        ItemStack itemrank;

        // Init the lore array with default values for ladders
        List<String> rankslore = new ArrayList<>();

        // Get the ranks
        List<RankLadder.PositionRank> ranks = ladder.get().ranks;

        // Get the dimensions and if needed increases them
        while (dimension <= ranks.toArray().length + 8){
            dimension = dimension + 9;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3Ladders -> Ranks");

        // For every rank make a button
        for (RankLadder.PositionRank pos : ranks) {
            Optional<Rank> rankOptional = ladder.get().getByPosition(pos.getPosition());

            // Well... check if the rank is null probably
            if (!rankOptional.isPresent()) {
                continue; // Skip it
            }

            // Get the specific rank
            Rank rank = rankOptional.get();

            // Make the button with materials, amount, lore and name
            itemrank = createButton(Material.TRIPWIRE_HOOK, 1, rankslore, "§6" + rank.name);

            // Add the button to the inventory
            inv.addItem(itemrank);
        }

        // Open the inventory
        this.p.openInventory(inv);
    }
}

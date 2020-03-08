package tech.mcprison.prison.spigot.gui;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;

public class SpigotRanksGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;
    private Optional<RankLadder> ladder;

    public SpigotRanksGUI(Player p, Optional<RankLadder> ladder) {
        this.p = p;
        this.ladder = ladder;
    }

    public void open() {

        // Init the ItemStack
        ItemStack itemrank;

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil(ladder.get().ranks.size() / 9D)*9;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3" + "Ladders -> Ranks");

        // For every rank make a button
        for (RankLadder.PositionRank pos : ladder.get().ranks) {
            Optional<Rank> rankOptional = ladder.get().getByPosition(pos.getPosition());

            // Well... check if the rank is null probably
            if (!rankOptional.isPresent()) {
                continue; // Skip it
            }

            // Init the lore array with default values for ladders
            List<String> rankslore = createLore(
                    "§cPress Shift + Right click to delete",
                    "",
                    "§8§l|§3Info§8|");

            // Get the specific rank
            Rank rank = rankOptional.get();

            // Add the RankID Lore
            rankslore.add("§3Rank id: §7" + rank.id);

            // Add the RankName lore
            rankslore.add("§3Rank Name: §7" + rank.name);

            // Add the Rank Tag lore
            rankslore.add("§3Rank Tag: §7" + ChatColor.translateAlternateColorCodes('&', rank.tag));

            // Add the Price lore
            rankslore.add("§3Rank Price: §a" + rank.cost);

            List<RankPlayer> players =
                    PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
                            .filter(rankPlayer -> rankPlayer.getRanks().values().contains(rankOptional.get()))
                            .collect(Collectors.toList());

            // Add the number of players with this rank
            rankslore.add("§3Players with this rank: §7" + players.size());

            // RankUpCommands info lore
            rankslore.add("");
            if (rank.rankUpCommands == null || rank.rankUpCommands.size() == 0){
                rankslore.add("§3The Rank " + rank.name + " contains no commands.");
            } else {
                rankslore.add("§8There're §3" + rank.rankUpCommands.size() + " §3Commands §8in this ladder:");
                for (String command : rank.rankUpCommands) {
                    rankslore.add("§8§l|§3RankUPCommands§8| §8§l- §3" + command);
                }
                rankslore.add("§8Click to manage RankUPCommands");
            }

            // Make the button with materials, amount, lore and name
            itemrank = createButton(Material.TRIPWIRE_HOOK, 1, rankslore, "§3" + rank.name);

            // Add the button to the inventory
            inv.addItem(itemrank);
        }

        // Open the inventory
        this.p.openInventory(inv);
    }
}

package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author GABRYCA
 */
public class SpigotRanksGUI extends SpigotGUIComponents {

    private final Player p;
    private final Optional<RankLadder> ladder;
    private int counter;

    public SpigotRanksGUI(Player p, Optional<RankLadder> ladder, int counter) {
        this.p = p;
        this.ladder = ladder;
        this.counter = counter;
    }

    public void open(){

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Get the dimensions and if needed increases them
        if (!ladder.isPresent() || ladder.get().getRanks().size() == 0) {
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.NoRanksFoundAdmin")));
            return;
        }

        // Get the dimensions and if needed increases them
        int dimension = 54;
        int pageSize = 45;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ladders -> Ranks"));


        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < ladder.get().getRanks().size() && i < counter + pageSize; i++ ) {

            // Init the lore array with default values for ladders
            List<String> ranksLore = createLore(
                    messages.getString("Lore.ShiftAndRightClickToDelete"),
                    messages.getString("Lore.ClickToManageRank"),
                    "",
                    messages.getString("Lore.Info"));

            Rank rank = ladder.get().getRanks().get(i);

            // Add the RankID Lore
            ranksLore.add(SpigotPrison.format(messages.getString("Lore.Id") + rank.getId()));
            ranksLore.add(SpigotPrison.format(messages.getString("Lore.Name") + rank.getName()));
            ranksLore.add(SpigotPrison.format(messages.getString("Lore.Tag2") + ChatColor.translateAlternateColorCodes('&', rank.getTag())));
            ranksLore.add(SpigotPrison.format(messages.getString("Lore.Price3") + rank.getCost()));

            // Init a variable
            List<RankPlayer> players =
                    PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
                            .filter(rankPlayer -> rankPlayer.getLadderRanks().containsValue(rank))
                            .collect(Collectors.toList());

            // Add the number of players with this rank
            ranksLore.add(SpigotPrison.format(messages.getString("Lore.PlayersWithTheRank") + players.size()));
            ranksLore.add("");
            //getCommands(ranksLore, rank);

            // Make the button with materials, amount, lore and name
            ItemStack itemRank = createButton(XMaterial.TRIPWIRE_HOOK.parseItem(), ranksLore, SpigotPrison.format("&3" + rank.getName()));

            // Add the button to the inventory
            inv.addItem(itemRank);
        }

        if (i < ladder.get().getRanks().size()) {
            List<String> nextPageLore = createLore(messages.getString("Lore.ClickToNextPage"));

            ItemStack nextPageButton = createButton(Material.BOOK, 1, nextPageLore, "&7Next " + (i + 1));
            inv.setItem(53, nextPageButton);
        }
        if (i >= (pageSize * 2)) {
            List<String> priorPageLore = createLore(messages.getString("Lore.ClickToPriorPage"));

            ItemStack priorPageButton = createButton(Material.BOOK, 1, priorPageLore,
                    "&7Prior " + (i - (pageSize * 2) - 1));
            inv.setItem(51, priorPageButton);
        }

        // Open the inventory
        openGUI(p, inv);
    }
}

package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotRanksGUI extends SpigotGUIComponents {

    private final Player p;
    private final Optional<RankLadder> ladder;

    public SpigotRanksGUI(Player p, Optional<RankLadder> ladder) {
        this.p = p;
        this.ladder = ladder;
    }

    public void open() {

        // Init the ItemStack
        ItemStack itemrank;

        int dimension = 27;

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Get the dimensions and if needed increases them
        if (ladder.isPresent()) {
            dimension = (int) Math.ceil(ladder.get().ranks.size() / 9D) * 9;
        }

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // If the inventory is empty
        if (dimension == 0){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.EmptyGui")));
            if (p.getOpenInventory() != null){
                p.closeInventory();
                return;
            }
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format("&cSorry, but there're too many ranks and the max's 54 for the GUI"));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ladders -> Ranks"));

        // For every rank make a button
        if (ladder.isPresent()) {
            for (RankLadder.PositionRank pos : ladder.get().ranks) {
                Optional<Rank> rankOptional = ladder.get().getByPosition(pos.getPosition());

                // Well... check if the rank is null probably
                if (!rankOptional.isPresent()) {
                    continue; // Skip it
                }

                // Init the lore array with default values for ladders
                List<String> rankslore = createLore(
                        GuiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable"),
                        GuiConfig.getString("Gui.Lore.ClickToManageRank"),
                        "",
                        GuiConfig.getString("Gui.Lore.Info"));

                // Get the specific rank
                Rank rank = rankOptional.get();

                // Add the RankID Lore
                rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Id") + rank.id));

                // Add the RankName lore
                rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Name") + rank.name));

                // Add the Rank Tag lore
                rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Tag2") + ChatColor.translateAlternateColorCodes('&', rank.tag)));

                // Add the Price lore
                rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Price3") + rank.cost));

                // Init a variable
                List<RankPlayer> players =
                        PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
                                .filter(rankPlayer -> rankPlayer.getRanks().containsValue(rankOptional.get()))
                                .collect(Collectors.toList());

                // Add the number of players with this rank
                rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.PlayersWithTheRank") + players.size()));

                // RankUpCommands info lore
                rankslore.add("");
                getCommands(rankslore, rank);

                // Make the button with materials, amount, lore and name
                itemrank = createButton(Material.TRIPWIRE_HOOK, 1, rankslore, SpigotPrison.format("&3" + rank.name));

                // Add the button to the inventory
                inv.addItem(itemrank);
            }
        }

        // Open the inventory
        this.p.openInventory(inv);
    }

    static void getCommands(List<String> rankslore, Rank rank) {

        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (rank.rankUpCommands == null || rank.rankUpCommands.size() == 0) {
            rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.ContainsTheRank") + rank.name + GuiConfig.getString("Gui.Lore.ContainsNoCommands")));
        } else {
            rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.LadderThereAre") + rank.rankUpCommands.size() + GuiConfig.getString("Gui.Lore.LadderCommands")));
            for (String command : rank.rankUpCommands) {
                rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.RankupCommands") + command));
            }
            rankslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.ClickToManageCommands")));
        }
    }
}

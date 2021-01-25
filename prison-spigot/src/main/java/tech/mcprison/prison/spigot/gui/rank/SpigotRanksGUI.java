package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.PrisonSetupGUI;
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

        int dimension;

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Get the dimensions and if needed increases them
        if (ladder.isPresent() && !(ladder.get().getRanks().size() == 0)) {
            dimension = (int) Math.ceil(ladder.get().getRanks().size() / 9D) * 9;
        } else {
            p.sendMessage(SpigotPrison.format(messages.getString("Message.NoRanksFoundAdmin")));
            return;
        }

        // If the inventory is empty
        if (dimension == 0){
            p.closeInventory();
            PrisonSetupGUI gui = new PrisonSetupGUI(p);
            gui.open();
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format(messages.getString("Message.TooManyRanks")));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ladders -> Ranks"));

        // For every rank make a button
        for (Rank rank : ladder.get().getRanks()) {


            if (guiBuilder(inv, Optional.of( rank ))) return;

        }

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv, Optional<Rank> rankOptional) {
        try {
            buttonsSetup(inv, rankOptional);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, Optional<Rank> rankOptional) {

        ItemStack itemRank;
        // Init the lore array with default values for ladders
        List<String> ranksLore = createLore(
                messages.getString("Lore.ShiftAndRightClickToDelete"),
                messages.getString("Lore.ClickToManageRank"),
                "",
                messages.getString("Lore.Info"));

        if (!rankOptional.isPresent()){
            p.sendMessage(SpigotPrison.format(messages.getString("Message.CantGetRanksAdmin")));
            return;
        }

        // Get the specific rank
        Rank rank = rankOptional.get();

        // Add the RankID Lore
        ranksLore.add(SpigotPrison.format(messages.getString("Lore.Id") + rank.getId()));
        ranksLore.add(SpigotPrison.format(messages.getString("Lore.Name") + rank.getName()));
        ranksLore.add(SpigotPrison.format(messages.getString("Lore.Tag2") + ChatColor.translateAlternateColorCodes('&', rank.getTag())));
        ranksLore.add(SpigotPrison.format(messages.getString("Lore.Price3") + rank.getCost()));

        // Init a variable
        List<RankPlayer> players =
                PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
                        .filter(rankPlayer -> rankPlayer.getLadderRanks().containsValue(rankOptional.get()))
                        .collect(Collectors.toList());

        // Add the number of players with this rank
        ranksLore.add(SpigotPrison.format(messages.getString("Lore.PlayersWithTheRank") + players.size()));
        ranksLore.add("");
        //getCommands(ranksLore, rank);

        // Make the button with materials, amount, lore and name
        itemRank = createButton(XMaterial.TRIPWIRE_HOOK.parseItem(), ranksLore, SpigotPrison.format("&3" + rank.getName()));

        // Add the button to the inventory
        inv.addItem(itemRank);
    }

    //static void getCommands(List<String> ranksLore, Rank rank) {

    //    if (rank.rankUpCommands == null || rank.rankUpCommands.size() == 0) {
    //        ranksLore.add(SpigotPrison.format(messages.getString("Lore.ContainsTheRank") + rank.name + messages.getString("Lore.ContainsNoCommands")));
    //    } else {
    //        ranksLore.add(SpigotPrison.format(messages.getString("Lore.LadderThereAre") + rank.rankUpCommands.size() + messages.getString("Lore.LadderCommands")));
    //        for (String command : rank.rankUpCommands) {
    //            ranksLore.add(SpigotPrison.format(messages.getString("Lore.RankupCommands") + command));
    //        }
    //        ranksLore.add(SpigotPrison.format(messages.getString("Lore.ClickToManageCommands")));
    //    }
    //}
}

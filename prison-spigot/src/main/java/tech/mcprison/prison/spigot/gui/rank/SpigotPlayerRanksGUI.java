package tech.mcprison.prison.spigot.gui.rank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;
import java.util.Optional;

/**
 * @author GABRYCA
 */
public class SpigotPlayerRanksGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotPlayerRanksGUI(Player p) {
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ranks -> PlayerRanks"));

        LadderManager lm = PrisonRanks.getInstance().getLadderManager();

        Optional<RankLadder> ladder = lm.getLadder("default");

        if (!(ladder.isPresent())){
            p.closeInventory();
            return;
        } else {
            dimension = (int) Math.ceil(ladder.get().ranks.size() / 9D) * 9;
        }

        RankLadder ladderData = ladder.get();

        if (!(ladderData.getLowestRank().isPresent())){
            p.closeInventory();
            return;
        }

        Configuration GuiConfig = SpigotPrison.getGuiConfig();
        Rank rank = ladderData.getLowestRank().get();
        // Init the ItemStack
        ItemStack itemrank;
        int amount = 1;

        List<String> ranksloreFirst = createLore(
                GuiConfig.getString("Gui.Lore.Info"),
                GuiConfig.getString("Gui.Lore.Price3") + rank.cost
        );

        itemrank = createButton(Material.TRIPWIRE_HOOK, amount, ranksloreFirst, SpigotPrison.format(rank.tag));
        inv.addItem(itemrank);

        while ( rank.rankNext != null ) {
            rank = rank.rankNext;
            amount++;
            List<String> rankslore = createLore(
                    GuiConfig.getString("Gui.Lore.Info"),
                    GuiConfig.getString("Gui.Lore.Price3") + rank.cost
            );
            itemrank = createButton(Material.TRIPWIRE_HOOK, amount, rankslore, SpigotPrison.format(rank.tag));
            inv.addItem(itemrank);
        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

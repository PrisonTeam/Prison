package tech.mcprison.prison.spigot.gui.rank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
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
    // private final RankPlayer rankPlayer;

    public SpigotPlayerRanksGUI(Player p /*RankPlayer rankPlayer*/) {
        this.p = p;
        // this.rankPlayer = rankPlayer;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension;

        LadderManager lm = PrisonRanks.getInstance().getLadderManager();

        Optional<RankLadder> ladder = lm.getLadder("default");

        if (!(ladder.isPresent())){
            p.closeInventory();
            return;
        } else {
            dimension = (int) Math.ceil(ladder.get().ranks.size() / 9D) * 9;
        }

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ranks -> PlayerRanks"));

        RankLadder ladderData = ladder.get();

        if (!(ladderData.getLowestRank().isPresent())){
            p.closeInventory();
            return;
        }

        // Get the rank
        // Rank playerRank = rankPlayer.getRank("default");

        Configuration GuiConfig = SpigotPrison.getGuiConfig();
        Rank rank = ladderData.getLowestRank().get();
        // Init the ItemStack
        ItemStack itemrank;
        int amount = 1;

        List<String> ranksloreFirst = createLore(
                GuiConfig.getString("Gui.Lore.Info"),
                GuiConfig.getString("Gui.Lore.Price3") + rank.cost
        );

        Material material = Material.TRIPWIRE_HOOK;

        itemrank = createButton(material, amount, ranksloreFirst, SpigotPrison.format(rank.tag));
        inv.addItem(itemrank);

        // boolean haveTheRank = true;

        while ( rank.rankNext != null ) {
            /*if (!(haveTheRank)){
                material = Material.REDSTONE_BLOCK;
                itemrank.addEnchantment(Enchantment.LUCK, 1);
            }*/
            rank = rank.rankNext;
            amount++;
            /*if (rank == playerRank){
                haveTheRank = false;
            }*/
            List<String> rankslore = createLore(
                    GuiConfig.getString("Gui.Lore.Info"),
                    GuiConfig.getString("Gui.Lore.Price3") + rank.cost
            );
            itemrank = createButton(material, amount, rankslore, SpigotPrison.format(rank.tag));
            inv.addItem(itemrank);
        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

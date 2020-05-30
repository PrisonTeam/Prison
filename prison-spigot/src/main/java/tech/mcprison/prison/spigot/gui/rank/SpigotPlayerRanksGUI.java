package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotPlayerRanksGUI extends SpigotGUIComponents {

    private final Player player;

    private final PrisonRanks rankPlugin;
    private final RankPlayer rankPlayer;

    public SpigotPlayerRanksGUI(Player player) {
        this.player = player;

        // If you need to get a SpigotPlayer:
//        SpigotPlayer sPlayer = new SpigotPlayer(p);

        Server server = SpigotPrison.getInstance().getServer();

        PrisonRanks rankPlugin = null;
        RankPlayer rPlayer = null;
        Plugin plugin = server.getPluginManager().getPlugin( PrisonRanks.MODULE_NAME );
        if ( plugin != null && plugin instanceof PrisonRanks ) {
            rankPlugin = (PrisonRanks) plugin;
            Optional<RankPlayer> oPlayer = rankPlugin.getPlayerManager().getPlayer( getPlayer().getUniqueId() );
            if ( oPlayer.isPresent() ) {
                rPlayer = oPlayer.get();
            }
        }
        this.rankPlugin = rankPlugin;
        this.rankPlayer = rPlayer;

    }

    public Player getPlayer() {
        return player;
    }

    public PrisonRanks getRankPlugin() {
        return rankPlugin;
    }

    public RankPlayer getRankPlayer() {
        return rankPlayer;
    }

    public void open() {

        // First ensure the ranks module is enabled:
        if ( getRankPlugin() == null ) {
            // Error? Cannot open if Rank module is not loaded.
            getPlayer().closeInventory();
            return;
        }

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        LadderManager lm = getRankPlugin().getLadderManager();
        Optional<RankLadder> ladder = lm.getLadder(GuiConfig.getString("Options.Ranks.Ladder"));

        // Ensure ladder is present and that it has a rank:
        if ( !ladder.isPresent() || !ladder.get().getLowestRank().isPresent() ){
            getPlayer().closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = (int) (Math.ceil(ladder.get().ranks.size() / 9D) * 9) + 9;

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ranks -> PlayerRanks"));

        RankLadder ladderData = ladder.get();

        Configuration guiConfig = SpigotPrison.getGuiConfig();
        Rank rank = ladderData.getLowestRank().get();

        Rank playerRank = getRankPlayer().getRank( ladderData ).orElse( null );

        // Not sure how you want to represent this:
        Material materialHas = Material.TRIPWIRE_HOOK;
        Material materialHasNot = Material.REDSTONE_BLOCK;

        // boolean haveTheRank = true;

        int amount = 1;
        while ( rank != null ) {
            /*if (!(haveTheRank)){
                material = Material.REDSTONE_BLOCK;
                itemrank.addEnchantment(Enchantment.LUCK, 1);
            }*/
//            amount++;
            /*if (rank == playerRank){
                haveTheRank = false;
            }*/

            boolean playerHasThisRank = playerRank != null && playerRank.equals( rank );

            List<String> rankslore = createLore(
                    guiConfig.getString("Gui.Lore.Info"),
                    guiConfig.getString("Gui.Lore.Price3") + rank.cost
            );
            ItemStack itemrank = createButton(
                    (playerHasThisRank ? materialHas : materialHasNot),
                    amount++, rankslore, SpigotPrison.format(rank.tag));
            inv.addItem(itemrank);

            rank = rank.rankNext;
        }

        List<String> rankupLore = createLore(
                GuiConfig.getString("Gui.Lore.IfYouHaveEnoughMoney"),
                GuiConfig.getString("Gui.Lore.ClickToRankup")
        );

        ItemStack rankupButton = createButton(Material.EMERALD_BLOCK, 1, rankupLore, SpigotPrison.format(GuiConfig.getString("Gui.Lore.Rankup")));
        inv.setItem(dimension - 5, rankupButton);

        // Open the inventory
        getPlayer().openInventory(inv);

    }

}
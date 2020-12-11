package tech.mcprison.prison.spigot.gui.rank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;
import java.util.Optional;

/**
 * @author GABRYCA
 */
public class SpigotPlayerPrestigesGUI extends SpigotGUIComponents {

    private final Player player;
    private PrisonRanks rankPlugin;
    private RankPlayer rankPlayer;
    private final Configuration guiConfig = guiConfig();
    private final Configuration messages = messages();

    public SpigotPlayerPrestigesGUI(Player player) {
        this.player = player;

        // If you need to get a SpigotPlayer:
        // SpigotPlayer sPlayer = new SpigotPlayer(p);

        Server server = SpigotPrison.getInstance().getServer();

        PrisonRanks rankPlugin;
        RankPlayer rPlayer;

        ModuleManager modMan = Prison.get().getModuleManager();
        Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );

        rankPlugin = (PrisonRanks) module;

        if (rankPlugin == null){
            player.sendMessage(SpigotPrison.format("&3[PRISON WARN] &cLooks like the Ranks module's disabled"));
            return;
        }

        if (rankPlugin.getPlayerManager() == null) {
            return;
        }

        PlayerManager playerManager = rankPlugin.getPlayerManager();

        rPlayer = playerManager.getPlayer( player.getUniqueId(), player.getName() ).orElse( null );
        LadderManager lm = rankPlugin.getLadderManager();

        for ( RankLadder ladderData : lm.getLadders() ) {
//            Rank playerRank = rPlayer == null ? null : rPlayer.getRank( ladderData ).orElse( null );
            Rank rank = ladderData.getLowestRank().orElse( null );

            while ( rank != null ) {
//                boolean playerHasThisRank = playerRank != null && playerRank.equals( rank );

                rank = rank.rankNext;
            }
        }

        Plugin plugin = server.getPluginManager().getPlugin( PrisonRanks.MODULE_NAME );
        if (plugin instanceof PrisonRanks) {
            rankPlugin = (PrisonRanks) plugin;
            Optional<RankPlayer> oPlayer = rankPlugin.getPlayerManager().
            									getPlayer( getPlayer().getUniqueId(), getPlayer().getName() );
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

        LadderManager lm = getRankPlugin().getLadderManager();
        Optional<RankLadder> ladder = lm.getLadder("prestiges");

        // Ensure ladder is present and that it has a rank:
        if ( !ladder.isPresent() || !ladder.get().getLowestRank().isPresent() ){
            getPlayer().closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = (int) (Math.ceil(ladder.get().ranks.size() / 9D) * 9) + 9;

        // Create an inventory
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Prestiges -> PlayerPrestiges"));

        // guiBuilder and validation
        if (guiBuilder(ladder, dimension, inv)) return;

        // Open the inventory
        openGUI(getPlayer(), inv);
    }

    private boolean guiBuilder(Optional<RankLadder> ladder, int dimension, Inventory inv) {
        try {
            buttonsSetup(ladder, dimension, inv);
        } catch (NullPointerException ex){
            getPlayer().sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Optional<RankLadder> ladder, int dimension, Inventory inv) {


        if (!ladder.isPresent()){
            player.sendMessage(SpigotPrison.format(messages.getString("Message.LadderPrestigesNotFound")));
            return;
        }

        RankLadder ladderData = ladder.get();

        if (!ladderData.getLowestRank().isPresent()){
            player.sendMessage(SpigotPrison.format(messages.getString("Message.NoRanksPrestigesLadder")));
            return;
        }

        Rank rank = ladderData.getLowestRank().get();

        Rank playerRank = getRankPlayer().getRank("prestiges");

        // Not sure how you want to represent this:
        Material materialHas = Material.getMaterial(guiConfig.getString("Options.Ranks.Item_gotten_rank"));
        Material materialHasNot = Material.getMaterial(guiConfig.getString("Options.Ranks.Item_not_gotten_rank"));

        boolean playerHasThisRank = true;
        int hackyCounterEnchant = 0;

        int amount = 1;
        while ( rank != null ) {

            List<String> ranksLore = createLore(
                    messages.getString("Lore.Info"),
                    messages.getString("Lore.Price3") + rank.cost
            );
            ItemStack itemrank = createButton(
                    (playerHasThisRank ? materialHas : materialHasNot),
                    amount++, ranksLore, SpigotPrison.format(rank.tag));
            if (playerRank != null && playerRank.equals( rank )){
                playerHasThisRank = false;
            }
            if (!(playerHasThisRank)){
                if (hackyCounterEnchant <= 0) {
                    hackyCounterEnchant++;
                    if (guiConfig.getString("Options.Ranks.Enchantment_effect_current_rank").equalsIgnoreCase("true")) {
                        itemrank.addUnsafeEnchantment(Enchantment.LUCK, 1);
                    }
                }
            }
            inv.addItem(itemrank);

            rank = rank.rankNext;
        }

        List<String> rankupLore = createLore(
                messages.getString("Lore.IfYouHaveEnoughMoney"),
                messages.getString("Lore.ClickToRankup")
        );

        ItemStack rankupButton = createButton(Material.EMERALD_BLOCK, 1, rankupLore, SpigotPrison.format("&aPrestige"));
        inv.setItem(dimension - 5, rankupButton);
    }
}

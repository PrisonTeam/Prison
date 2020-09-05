package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

/**
 * @author GABRYCA
 */
public class SpigotPlayerRanksGUI extends SpigotGUIComponents {

    private final Player player;

    private PrisonRanks rankPlugin;
    private RankPlayer rankPlayer;

    public SpigotPlayerRanksGUI(Player player) {
        this.player = player;

        // If you need to get a SpigotPlayer:
        //        SpigotPlayer sPlayer = new SpigotPlayer(p);

        Server server = SpigotPrison.getInstance().getServer();

        PrisonRanks rankPlugin;
        RankPlayer rPlayer;

        ModuleManager modMan = Prison.get().getModuleManager();
 	    Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );

 	    // Check
        if (!(checkRanks(player))){
            return;
        }

 	    rankPlugin = (PrisonRanks) module;

        if (rankPlugin == null){
            player.sendMessage(SpigotPrison.format("&cError: rankPlugin == null"));
            return;
        }

 	    if (rankPlugin.getPlayerManager() == null) {
 	        player.sendMessage(SpigotPrison.format("&cError: rankPlugin.getPlayerManager() == null"));
 	    	return;
 	    }

 	    PlayerManager playerManager = rankPlugin.getPlayerManager();

    	rPlayer = playerManager.getPlayer( player.getUniqueId() ).orElse( null );

        Plugin plugin = server.getPluginManager().getPlugin( PrisonRanks.MODULE_NAME );
        if (plugin instanceof PrisonRanks) {
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
            getPlayer().sendMessage(SpigotPrison.format("&cSorry, but there aren't Ranks in the default or selected ladder or the ladder &3[" + GuiConfig.getString("Options.Ranks.Ladder") + "]&c isn't found!"));
            getPlayer().closeInventory();
            return;
        }

        // Get the dimensions and if needed increases them
        if (ladder.get().ranks.size() == 0) {
            getPlayer().sendMessage(SpigotPrison.format("&cSorry, but there aren't Ranks in the default or selected ladder!"));
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = (int) (Math.ceil(ladder.get().ranks.size() / 9D) * 9) + 9;

        Configuration guiConfig = SpigotPrison.getGuiConfig();

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ranks -> PlayerRanks"));

        RankLadder ladderData = ladder.get();

        Rank rank = ladderData.getLowestRank().get();

        Rank playerRank = getRankPlayer().getRank( ladderData ).orElse( null );

        if (guiBuilder(GuiConfig, dimension, guiConfig, inv, rank, playerRank)) return;

        // Open the inventory
        getPlayer().openInventory(inv);

    }

    private boolean guiBuilder(Configuration guiConfig, int dimension, Configuration guiConfig2, Inventory inv, Rank rank, Rank playerRank) {
        try {
            buttonsSetup(guiConfig, dimension, guiConfig2, inv, rank, playerRank);
        } catch (NullPointerException ex){
            getPlayer().sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Configuration guiConfig, int dimension, Configuration guiConfig2, Inventory inv, Rank rank, Rank playerRank) {
        // Not sure how you want to represent this:
        Material materialHas = Material.getMaterial(Objects.requireNonNull(guiConfig.getString("Options.Ranks.Item_gotten_rank")));
        Material materialHasNot = Material.getMaterial(Objects.requireNonNull(guiConfig.getString("Options.Ranks.Item_not_gotten_rank")));

        boolean playerHasThisRank = true;
        int hackyCounterEnchant = 0;

        int amount = 1;
        while ( rank != null ) {

            List<String> rankslore = createLore(
                    guiConfig2.getString("Gui.Lore.Info"),
                    guiConfig2.getString("Gui.Lore.Price3") + rank.cost
            );
            ItemStack itemrank = createButton(
                    (playerHasThisRank ? materialHas : materialHasNot),
                    amount++, rankslore, SpigotPrison.format(rank.tag));
            if (playerRank != null && playerRank.equals(rank)){
                playerHasThisRank = false;
            }
            if (!(playerHasThisRank)){
                if (hackyCounterEnchant <= 0) {
                    hackyCounterEnchant++;
                    if (Objects.requireNonNull(guiConfig2.getString("Options.Ranks.Enchantment_effect_current_rank")).equalsIgnoreCase("true")) {
                        itemrank.addUnsafeEnchantment(Enchantment.LUCK, 1);
                    }
                }
            }
            inv.addItem(itemrank);

            rank = rank.rankNext;
        }

        List<String> rankupLore = createLore(
                guiConfig.getString("Gui.Lore.IfYouHaveEnoughMoney"),
                guiConfig.getString("Gui.Lore.ClickToRankup")
        );

        ItemStack rankupButton = createButton(Material.EMERALD_BLOCK, 1, rankupLore, SpigotPrison.format(guiConfig.getString("Gui.Lore.Rankup")));
        inv.setItem(dimension - 5, rankupButton);
    }

}
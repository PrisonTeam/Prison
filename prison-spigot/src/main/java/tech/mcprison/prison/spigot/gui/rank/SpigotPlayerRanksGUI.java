package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.cryptomorin.xseries.XMaterial;

import me.clip.placeholderapi.PlaceholderAPI;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotPlayerRanksGUI extends SpigotGUIComponents {

    private final Player player;

    private PrisonRanks rankPlugin;
    private RankPlayer rankPlayer;
    private final boolean placeholderAPINotNull = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null || Bukkit.getPluginManager().getPlugin("PlaceholdersAPI") != null;

    public SpigotPlayerRanksGUI(Player player) {
        this.player = player;

        Server server = SpigotPrison.getInstance().getServer();
        PrisonRanks rankPlugin;
        RankPlayer rPlayer;
        ModuleManager modMan = Prison.get().getModuleManager();
 	    Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );
        rankPlugin = (PrisonRanks) module;

 	    // Check
        if (!(checkRanks(player))){
            return;
        }

        if (rankPlugin == null){
            Output.get().sendWarn(new SpigotPlayer(player), SpigotPrison.format("&c: rankPlugin == null."));
            return;
        }

 	    if (rankPlugin.getPlayerManager() == null) {
 	        Output.get().sendError(new SpigotPlayer(player), SpigotPrison.format("&c: rankPlugin.getPlayerManager() == null."));
 	    	return;
 	    }

 	    PlayerManager playerManager = rankPlugin.getPlayerManager();
    	rPlayer = playerManager.getPlayer( player.getUniqueId(), player.getName() );
        Plugin plugin = server.getPluginManager().getPlugin( PrisonRanks.MODULE_NAME );

        if (plugin instanceof PrisonRanks) {
            rankPlugin = (PrisonRanks) plugin;
            rPlayer = rankPlugin.getPlayerManager().
            								getPlayer( getPlayer().getUniqueId(), getPlayer().getName() );
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

        LadderManager lm = getRankPlugin().getLadderManager();
        RankLadder ladder = lm.getLadder(guiConfig.getString("Options.Ranks.Ladder"));

        // Ensure ladder is present and that it has a rank:
        if ( ladder == null || !ladder.getLowestRank().isPresent()){
            Output.get().sendWarn(new SpigotPlayer(getPlayer()), SpigotPrison.format(messages.getString("Message.NoRanksFoundHelp1") + guiConfig.getString("Options.Ranks.Ladder") + messages.getString("Message.NoRanksFoundHelp2")));
            getPlayer().closeInventory();
            return;
        }

        // Get the dimensions and if needed increases them
        if (ladder.getRanks().size() == 0) {
            Output.get().sendWarn(new SpigotPlayer(getPlayer()), SpigotPrison.format(messages.getString("Message.NoRanksFound")));
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = (int) (Math.ceil(ladder.getRanks().size() / 9D) * 9) + 9;

        // Create the inventory
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format(guiConfig.getString("Options.Titles.PlayerRanksGUI")));

        // Get many parameters
        RankLadder ladderData = ladder;
        Rank rank = ladderData.getLowestRank().get();
        Rank playerRank = getRankPlayer().getRank(guiConfig.getString("Options.Ranks.Ladder"));

        // Call the whole GUI and build it
        if (guiBuilder(dimension, inv, rank, playerRank)) return;

        // Open the inventory
        openGUI(getPlayer(), inv);
    }

    private boolean guiBuilder( int dimension, Inventory inv, Rank rank, Rank playerRank) {
        try {
            buttonsSetup(dimension, inv, rank, playerRank);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(getPlayer()), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(int dimension, Inventory inv, Rank rank, Rank playerRank) {

        // Not sure how you want to represent this:
        Material materialHas = Material.getMaterial(guiConfig.getString("Options.Ranks.Item_gotten_rank"));
        Material materialHasNot = Material.getMaterial(guiConfig.getString("Options.Ranks.Item_not_gotten_rank"));

        // Variables
        boolean playerHasThisRank = true;
        int hackyCounterEnchant = 0;
        int amount = 1;

        while ( rank != null ) {

            List<String> ranksLore = createLore(
                    " ",
                    "&8-----------------------",
                    " ",
                    messages.getString("Lore.Info"),
                    messages.getString("Lore.Price3") + rank.getCost(),
                    " ",
                    "&8-----------------------"
                    );

            if (placeholderAPINotNull) {
                if (hackyCounterEnchant == 1) {
                    hackyCounterEnchant++;
                    ranksLore.add(SpigotPrison.format(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUniqueId()), "%prison_rcb_default%")));
                }
            }

            ItemStack itemRank = createButton(
                    (playerHasThisRank ? materialHas : materialHasNot),
                    amount++, ranksLore, SpigotPrison.format(rank.getTag()));

            if (playerRank != null && playerRank.equals(rank)){
                playerHasThisRank = false;
            }

            if (!(playerHasThisRank)){
                if (hackyCounterEnchant <= 0) {
                    hackyCounterEnchant++;
                    if (guiConfig.getString("Options.Ranks.Enchantment_effect_current_rank").equalsIgnoreCase("true")) {
                        itemRank.addUnsafeEnchantment(Enchantment.LUCK, 1);
                    }
                }
            }

            inv.addItem(itemRank);
            rank = rank.getRankNext();
        }

        List<String> rankupLore = createLore(
                messages.getString("Lore.IfYouHaveEnoughMoney"),
                messages.getString("Lore.ClickToRankup")
        );

        ItemStack rankupButton = createButton(XMaterial.EMERALD_BLOCK.parseItem(), rankupLore, SpigotPrison.format(messages.getString("Lore.Rankup")));
        inv.setItem(dimension - 5, rankupButton);
    }
}
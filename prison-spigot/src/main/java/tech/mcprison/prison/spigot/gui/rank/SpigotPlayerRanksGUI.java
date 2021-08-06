package tech.mcprison.prison.spigot.gui.rank;

import java.text.DecimalFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.cryptomorin.xseries.XMaterial;

import me.clip.placeholderapi.PlaceholderAPI;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotPlayerRanksGUI extends SpigotGUIComponents {

    private final Player player;

    private PrisonRanks rankPlugin;
    private RankPlayer rankPlayer;
    private final boolean placeholderAPINotNull = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null || Bukkit.getPluginManager().getPlugin("PlaceholdersAPI") != null;
    private final List<String> configCustomLore = guiConfig.getStringList("EditableLore.Ranks");

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
            Output.get().sendWarn(new SpigotPlayer(player), "&c: rankPlugin == null.");
            return;
        }

 	    if (rankPlugin.getPlayerManager() == null) {
 	        Output.get().sendWarn(new SpigotPlayer(player), "&c: rankPlugin.getPlayerManager() == null.");
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
            Output.get().sendWarn(new SpigotPlayer(getPlayer()), messages.getString("Message.NoRanksFoundHelp1") + guiConfig.getString("Options.Ranks.Ladder") + messages.getString("Message.NoRanksFoundHelp2"));
            getPlayer().closeInventory();
            return;
        }

        // Get the dimensions and if needed increases them
        if (ladder.getRanks().size() == 0) {
            Output.get().sendWarn(new SpigotPlayer(getPlayer()), messages.getString("Message.NoRanksFound"));
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = (int) (Math.ceil(ladder.getRanks().size() / 9D) * 9) + 9;

        // Get many parameters
        Rank rank = ladder.getLowestRank().get();
        Rank playerRank = getRankPlayer().getRank(guiConfig.getString("Options.Ranks.Ladder"));

        PrisonGUI gui = new PrisonGUI(getPlayer(), dimension, guiConfig.getString("Options.Titles.PlayerRanksGUI"));

        // Not sure how you want to represent this:
        XMaterial materialHas = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_gotten_rank"));
        XMaterial materialHasNot = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_not_gotten_rank"));

        // Variables
        boolean playerHasThisRank = true;
        int hackyCounterEnchant = 0;
        int amount = 1;

        // Global booleans.
        boolean enchantmentEffectEnabled = getBoolean(guiConfig.getString("Options.Ranks.Enchantment_effect_current_rank"));

        // Decimal Rank cost format.
        DecimalFormat formatDecimal = new DecimalFormat("###,##0.00");

        while (rank != null) {

            ButtonLore ranksLore = new ButtonLore();

            for (String stringValue : configCustomLore) {
                stringValue = stringValue.replace("{rankPrice}", PlaceholdersUtil.formattedKmbtSISize(rank.getCost(), formatDecimal, ""));
                stringValue = stringValue.replace("{rankName}", rank.getName());
                stringValue = stringValue.replace("{rankTag}", SpigotPrison.format(rank.getTag()));
                ranksLore.addLineLoreAction(stringValue);
            }
            if (placeholderAPINotNull){
                ranksLore.setLoreAction(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUniqueId()), ranksLore.getLoreAction()));
            }

            boolean showNumber = getBoolean(guiConfig.getString("Options.Ranks.Number_of_Rank_Player_GUI"));
            Button itemRank = new Button(null, playerHasThisRank ? materialHas : materialHasNot, showNumber ? amount : 1, ranksLore, SpigotPrison.format(rank.getTag()));

            amount++;

            if (playerRank != null && playerRank.equals(rank)){
                playerHasThisRank = false;
            }

            if (!(playerHasThisRank)){
                if (hackyCounterEnchant <= 0) {
                    hackyCounterEnchant++;
                    if (enchantmentEffectEnabled) {
                        itemRank.addUnsafeEnchantment(Enchantment.LUCK, 1);
                    }
                }
            }

            gui.addButton(itemRank);
            rank = rank.getRankNext();
        }

        ButtonLore rankupLore = new ButtonLore(messages.getString("Lore.ClickToRankup"), messages.getString("Lore.IfYouHaveEnoughMoney"));

        // Add button.
        gui.addButton(new Button(dimension - 5, XMaterial.EMERALD_BLOCK, rankupLore, SpigotPrison.format(messages.getString("Lore.Rankup"))));

        // Open GUI.
        gui.open();
    }
}
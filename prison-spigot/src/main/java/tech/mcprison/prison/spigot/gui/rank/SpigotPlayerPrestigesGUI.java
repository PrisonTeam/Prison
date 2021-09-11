package tech.mcprison.prison.spigot.gui.rank;

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
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotPlayerPrestigesGUI extends SpigotGUIComponents {

    private final Player player;
    private PrisonRanks rankPlugin;
    private RankPlayer rankPlayer;
    private final boolean placeholderAPINotNull = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null || Bukkit.getPluginManager().getPlugin("PlaceholdersAPI") != null;

    public SpigotPlayerPrestigesGUI(Player player) {
        this.player = player;

        Server server = SpigotPrison.getInstance().getServer();

        PrisonRanks rankPlugin;
        RankPlayer rPlayer;
        ModuleManager modMan = Prison.get().getModuleManager();
        Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );
        rankPlugin = (PrisonRanks) module;

        if (rankPlugin == null){
            Output.get().sendWarn(new SpigotPlayer(player), "&3Looks like the Ranks module's disabled");
            return;
        }

        if (rankPlugin.getPlayerManager() == null) {
            return;
        }

        PlayerManager playerManager = rankPlugin.getPlayerManager();

        rPlayer = playerManager.getPlayer( player.getUniqueId(), player.getName() );
        LadderManager lm = rankPlugin.getLadderManager();

        for ( RankLadder ladderData : lm.getLadders() ) {
            Rank rank = ladderData.getLowestRank().orElse( null );

            while ( rank != null ) {

                rank = rank.getRankNext();
            }
        }

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

        // Load config

        LadderManager lm = getRankPlugin().getLadderManager();
        RankLadder ladder = lm.getLadder("prestiges");

        // Ensure ladder is present and that it has a rank:
        if ( ladder == null || !ladder.getLowestRank().isPresent() ){
            getPlayer().closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = (int) (Math.ceil(ladder.getRanks().size() / 9D) * 9) + 9;

        PrisonGUI gui = new PrisonGUI(getPlayer(), dimension, guiConfig.getString("Options.Titles.PlayerPrestigesGUI"));

        // dead code:
//        if ( ladder == null ){
//            Output.get().sendWarn(new SpigotPlayer(player), messages.getString("Message.LadderPrestigesNotFound"));
//            return;
//        }

        if (!ladder.getLowestRank().isPresent()){
            Output.get().sendWarn(new SpigotPlayer(player), messages.getString(MessagesConfig.StringID.spigot_message_prestiges_empty));
            return;
        }

        Rank rank = ladder.getLowestRank().get();
        
        PlayerRank prestigePlayerRank = getRankPlayer().getRank("prestiges");

        Rank playerRank = prestigePlayerRank == null ? null : prestigePlayerRank.getRank();

        // Not sure how you want to represent this:
        XMaterial materialHas = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_gotten_rank"));
        XMaterial materialHasNot = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_not_gotten_rank"));

        // Variables.
        boolean playerHasThisRank = true;
        int hackyCounterEnchant = 0;

        // Global strings.
        String loreInfo = messages.getString(MessagesConfig.StringID.spigot_gui_lore_info);
        String lorePrice3 = messages.getString(MessagesConfig.StringID.spigot_gui_lore_price);

        // Global boolean.
        boolean enchantmentEffectEnabled = getBoolean(guiConfig.getString("Options.Ranks.Enchantment_effect_current_rank"));

        int amount = 1;
        while ( rank != null ) {

        	// Need to create a new PlayerRank specifically for the player which takes in to consideration 
        	// all of their multipliers.
        	PlayerRank targetPlayerRank = PlayerRank.getTargetPlayerRankForPlayer( rankPlayer, rank );
        	double cost = targetPlayerRank == null ? -1 : targetPlayerRank.getRankCost(); 
        	
            ButtonLore ranksLore = new ButtonLore(loreInfo, lorePrice3 + cost );

            if (placeholderAPINotNull) {
                if (hackyCounterEnchant == 1) {
                    hackyCounterEnchant++;
                    ranksLore.addLineLoreDescription(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUniqueId()), "%prison_rcb_prestiges%"));
                }
            }

            Button itemrank = new Button(null, playerHasThisRank ? materialHas : materialHasNot, amount++, ranksLore, rank.getTag());
            if (playerRank != null && playerRank.equals( rank )){
                playerHasThisRank = false;
            }
            if (!(playerHasThisRank)){
                if (hackyCounterEnchant <= 0) {
                    hackyCounterEnchant++;
                    if (enchantmentEffectEnabled) {
                        itemrank.addUnsafeEnchantment(Enchantment.LUCK, 1);
                    }
                }
            }
            gui.addButton(itemrank);

            rank = rank.getRankNext();
        }

        ButtonLore rankupLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_rankup), messages.getString(MessagesConfig.StringID.spigot_gui_lore_rankup_if_enough_money));

        // Add button to GUI.
        gui.addButton(new Button(dimension - 5, XMaterial.EMERALD_BLOCK, rankupLore, SpigotPrison.format("&aPrestige")));

        // Open GUI.
        gui.open();
    }
}

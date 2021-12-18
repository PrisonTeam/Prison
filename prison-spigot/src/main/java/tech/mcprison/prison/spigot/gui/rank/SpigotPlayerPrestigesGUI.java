package tech.mcprison.prison.spigot.gui.rank;

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
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;
import tech.mcprison.prison.spigot.gui.rank.SpigotGUIMenuTools.GUIMenuPageData;

/**
 * @author GABRYCA
 */
public class SpigotPlayerPrestigesGUI extends SpigotGUIComponents {

    private final Player player;
    private PrisonRanks rankPlugin;
    private RankPlayer rankPlayer;
    private final boolean placeholderAPINotNull = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null || Bukkit.getPluginManager().getPlugin("PlaceholdersAPI") != null;

    private int page;
    
    public SpigotPlayerPrestigesGUI(Player player, int page ) {
        this.player = player;

        this.page = page;
        
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

        
        
        int totalArraySize = ladder.getRanks().size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, "gui prestiges", "gui" );


        List<Rank> ranksDisplay = ladder.getRanks().subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        
        

        
        
//         Create the inventory and set up the owner, dimensions or number of slots, and title
//        int dimension = (int) (Math.ceil(ladder.getRanks().size() / 9D) * 9) + 9;

        PrisonGUI gui = new PrisonGUI(getPlayer(), guiPageData.getDimension(), guiConfig.getString("Options.Titles.PlayerPrestigesGUI"));

        // dead code:
//        if ( ladder == null ){
//            Output.get().sendWarn(new SpigotPlayer(player), messages.getString("Message.LadderPrestigesNotFound"));
//            return;
//        }

        if (!ladder.getLowestRank().isPresent()){
            Output.get().sendWarn(new SpigotPlayer(player), messages.getString(MessagesConfig.StringID.spigot_message_prestiges_empty));
            return;
        }

//        Rank rank = ladder.getLowestRank().get();
        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
//        PlayerRank prestigePlayerRank = rankPlayerFactory.getRank( getRankPlayer(), "prestiges");
//        Rank playerRank = prestigePlayerRank == null ? null : prestigePlayerRank.getRank();
        XMaterial materialHas;
        XMaterial materialHasNot;

        // Not sure how you want to represent this:
        try {
            materialHas = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_gotten_rank"));
        } catch (IllegalArgumentException ignored){
            materialHas = XMaterial.TRIPWIRE_HOOK;
        }
        try {
            materialHasNot = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_not_gotten_rank"));
        } catch (IllegalArgumentException ignored){
            materialHasNot = XMaterial.REDSTONE_BLOCK;
        }

        // Variables.
//        boolean playerHasThisRank = true;
        int hackyCounterEnchant = 0;

        // Global strings.
        String loreInfo = messages.getString(MessagesConfig.StringID.spigot_gui_lore_info);
        String lorePrice3 = messages.getString(MessagesConfig.StringID.spigot_gui_lore_price);

        // Global boolean.
        boolean enchantmentEffectEnabled = getBoolean(guiConfig.getString("Options.Ranks.Enchantment_effect_current_rank"));

        for ( Rank rank : ranksDisplay )
		{ 
        	// hasAccess uses access by rank, and access by perm:
        	boolean playerHasThisRank = getRankPlayer() != null && getRankPlayer().hasAccessToRank( rank );

//        int amount = 1;
//        while ( rank != null ) {

        	// Need to create a new PlayerRank specifically for the player which takes in to consideration 
        	// all of their multipliers.
//        	PlayerRank pRank = rankPlayerFactory.createPlayerRank( rank );
//        	PlayerRank targetPlayerRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, rank );
        	
        	PlayerRank pRank = rankPlayerFactory.getRank( getRankPlayer(), rank.getLadder() );
        	
        	double cost = pRank == null ? -1 : pRank.getRankCost(); 
        	
            ButtonLore ranksLore = new ButtonLore(loreInfo, lorePrice3 + cost );

            if (placeholderAPINotNull) {
                if (hackyCounterEnchant == 1) {
                    hackyCounterEnchant++;
                    ranksLore.addLineLoreDescription(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUniqueId()), "%prison_rcb_prestiges%"));
                }
            }

//            if (playerRank != null && playerRank.equals( rank )){
//                playerHasThisRank = false;
//            }

            Button itemrank = new Button(null, playerHasThisRank ? materialHas : materialHasNot, 1, ranksLore, rank.getTag());
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
//            if (amount > 45){
//                break;
//            }
        }
        

        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );

        

        ButtonLore rankupLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_rankup), messages.getString(MessagesConfig.StringID.spigot_gui_lore_rankup_if_enough_money));
        Button rankupButton = new Button(0, XMaterial.EMERALD_BLOCK, rankupLore, SpigotPrison.format("&aPrestige"));
        // NOTE: Button position will be properly assigned in the setButtonNextAvilable:
        gui.addButton( guiPageData.setButtonNextAvailable( rankupButton ) );
        
        
        
        // Open GUI.
        gui.open();
    }
}

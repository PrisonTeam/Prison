package tech.mcprison.prison.spigot.gui.rank;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.nbt.PrisonNBTUtil;
import tech.mcprison.prison.util.Text;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotPlayerRanksGUI 
	extends SpigotGUIMessages {

    private final Player player;

    private PrisonRanks rankPlugin;
    private RankPlayer rankPlayer;
    private final boolean placeholderAPINotNull = 
    		Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null || 
    		Bukkit.getPluginManager().getPlugin("PlaceholdersAPI") != null;
//    private final List<String> configCustomLore = guiConfig.getStringList("EditableLore.Ranks");
    
    private String ladderName;
    private int page = 0;
    private String cmdPage;
    private String cmdReturn;

    public SpigotPlayerRanksGUI(Player player, String ladderName, int page, String cmdPage, String cmdReturn ) {
        this.player = player;
        
        this.ladderName = ladderName;
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;

        Server server = SpigotPrison.getInstance().getServer();
        PrisonRanks rankPlugin;
        RankPlayer rPlayer;
        ModuleManager modMan = Prison.get().getModuleManager();
 	    Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME );
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
        RankLadder ladder = lm.getLadder( ladderName );
//        RankLadder ladder = lm.getLadder(guiConfig.getString("Options.Ranks.Ladder"));

        // Ensure ladder is present and that it has a rank:
        if ( ladder == null || !ladder.getLowestRank().isPresent()){
        	guiRanksLadderIsEmptyMsg( new SpigotPlayer(getPlayer()), guiConfig.getString("Options.Ranks.Ladder") );
            getPlayer().closeInventory();
            return;
        }

        // Get the dimensions and if needed increases them
        if (ladder.getRanks().size() == 0) {
        	guiRanksNoRanksMsg( new SpigotPlayer(getPlayer()) );
            return;
        }
        
        int totalArraySize = ladder.getRanks().size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


        List<Rank> ranksDisplay = ladder.getRanks().subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        
        
        // Get many parameters
//        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
//        Rank rank = ladder.getLowestRank().get();
//        PlayerRank playerRankRank = rankPlayerFactory.getRank( getRankPlayer(), guiConfig.getString("Options.Ranks.Ladder"));
        
        
//        Rank playerRank = playerRankRank == null ? null : playerRankRank.getRank();

        PrisonGUI gui = new PrisonGUI(getPlayer(), guiPageData.getDimension(), guiConfig.getString("Options.Titles.PlayerRanksGUI"));

        
        
        String guiItemNameDefaultSetting = guiConfig.getString( "Options.Ranks.GuiItemNameDefault" );

        
        // Not sure how you want to represent this:
        XMaterial materialHas = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_gotten_rank"));
        XMaterial materialHasNot = XMaterial.valueOf(guiConfig.getString("Options.Ranks.Item_not_gotten_rank"));

        
        List<String> configCustomLore = guiConfig.getStringList("EditableLore.Ranks");

        
        // Variables
        int hackyCounterEnchant = 0;
        
        
        int amount = guiPageData.getPosStart();
//        int amount = 1;

        // Global booleans.
        boolean enchantmentEffectEnabled = getBoolean(guiConfig.getString("Options.Ranks.Enchantment_effect_current_rank"));

        // Decimal Rank cost format.
        DecimalFormat formatDecimal = Prison.get().getDecimalFormat("###,##0.00");
        DecimalFormat mFmt = Prison.get().getDecimalFormat("###,##0.0000");
        boolean showNumber = getBoolean(guiConfig.getString("Options.Ranks.Number_of_Rank_Player_GUI"));

//        PlayerRank pRank = rankPlayerFactory.getRank( getRankPlayer(), ladder, true );
        
        for ( Rank rank : ranksDisplay )
		{
        	
            
            String guiItemNameDefault = 
            		(guiItemNameDefaultSetting == null || guiItemNameDefaultSetting.trim().length() == 0) ?
            				rank.getName() :
            				guiItemNameDefaultSetting
            						.replace( "{rankName}", rank.getName() )
            						.replace( "{rankTag}", rank.getTag() );
            
            String guiItemName = guiConfig.getString( "Options.Ranks.GuiItemNames." + rank.getName() );

            // Get Rank Name. First use 'guiItemName' if not null, then try to use 'guiItemNameDefault'
            // if not null, and then use the rank's tag, or if that's null, then use the name:
            String rankName =
            		guiItemName != null ? guiItemName :
            			guiItemNameDefault != null ? guiItemNameDefault :
            				rank.getTag() != null ? rank.getTag() :
            					rank.getName();
    
            
            // hasAccess uses access by rank, and access by perm:
            boolean playerHasThisRank = getRankPlayer() != null && getRankPlayer().hasAccessToRank( rank );
            
           	
        	// The valid names to use for Options.Ranks.MaterialType.<MaterialName> must be
        	// based upon the XMaterial enumeration name, or supported past names.
        	String materialTypeStr = guiConfig.getString("Options.Ranks.MaterialType." + rank.getName());
        	XMaterial materialType =
        			materialTypeStr == null ? null :
        				XMaterial.valueOf( materialTypeStr.toUpperCase() );
        	
        	materialType = 
        			!playerHasThisRank ? materialHasNot : 
        				materialType != null ? materialType :
        					materialHas;
        	
        	String rankLoreKey = "EditableLore.Rank." + ladderName + "." + rank.getName();
        	List<String> rankLore = new ArrayList<>( configCustomLore );
        	List<String> rankLore2 = guiConfig.getStringList( rankLoreKey );
        	rankLore.addAll( rankLore2 );
        	
            ButtonLore ranksLore = new ButtonLore();

            PlayerRank calPRank = rankPlayer.calculateTargetPlayerRank( rank );
//            PlayerRank calPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, rank );
            
            double rankPrice = calPRank.getRankCost();
            double rankMultiplier = calPRank.getRankMultiplier();

            StringBuilder sbMines = new StringBuilder();
            if ( rank.getMines() != null && rank.getMines().size() > 0 ) {
            	
            	for (ModuleElement mine : rank.getMines() ) {
            		if ( sbMines.length() > 0 ) {
            			sbMines.append( " " );
            		}
            		sbMines.append( mine.getTag() );
            	}
            }
            else {
            	sbMines.append( "&3None" );
            }

            for ( String stringValue : rankLore ) {
                
            	String currency = (rank.getCurrency() == null || 
        				"default".equalsIgnoreCase( rank.getCurrency()) ||
							rank.getCurrency().trim().length() == 0  ?
									"" : " " + rank.getCurrency() );
            	
                stringValue = stringValue.replace("{rankPrice}", 
                		PlaceholdersUtil.formattedKmbtSISize(
                				rankPrice, formatDecimal, "") + currency
                		);
            	
                stringValue = stringValue.replace("{rankName}", rank.getName());
                stringValue = stringValue.replace("{rankTag}", Text.translateAmpColorCodes(rank.getTag()));
                stringValue = stringValue.replace("{rankMultiplier}", mFmt.format( rankMultiplier ));
                stringValue = stringValue.replace("{ladderName}", rank.getLadder().getName());
                
                stringValue = stringValue.replace("{linkedMines}", sbMines.toString() );
                
                ranksLore.addLineLoreAction(stringValue);
            }
            
            if ( placeholderAPINotNull ) {
            	
            	List<String> lores = PlaceholderAPI.setPlaceholders(
            			Bukkit.getOfflinePlayer(player.getUniqueId()), 
        				ranksLore.getLoreAction());
            	
                ranksLore.setLoreAction( lores );
            }

            Button itemRank = new Button(null, 
            		materialType,
//            		playerHasThisRank ? materialHas : materialHasNot, 
            				showNumber ? amount : 1, ranksLore, 
            						rankName );

            amount++;

//            if (playerRank != null && playerRank.equals(rank)){
//                playerHasThisRank = false;
//            }

            if (!(playerHasThisRank)){
                if (hackyCounterEnchant <= 0) {
                    hackyCounterEnchant++;
                    if (enchantmentEffectEnabled) {
                        itemRank.addUnsafeEnchantment(Enchantment.LUCK, 1);
                    }
                }
            }


            // Before adding the button, add an NBT tag for the command and rank name:
//			PrisonNBTUtil nbtUtil = new PrisonNBTUtil();
//			NBTItem nbtItem = nbtUtil == null ? null : nbtUtil.getNBT( itemRank.getButtonItem());
			PrisonNBTUtil.setNBTBoolean( itemRank.getButtonItem(), SpigotGUIMenuTools.GUI_MENU_TOOLS_NBT_ENABLED, true);
//			nbtUtil.setNBTString(nbtItem, SpigotGUIMenuTools.GUI_MENU_TOOLS_NBT_COMMAND, noCommmand );
			PrisonNBTUtil.setNBTString( itemRank.getButtonItem(), SpigotGUIMenuTools.GUI_MENU_TOOLS_NBT_RANK_NAME, rank.getName() );
			
            
            gui.addButton(itemRank);
            
//            rank = rank.getRankNext();
        }

        
        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );

        
		if ( LadderManager.LADDER_DEFAULT.equalsIgnoreCase( ladderName ) &&
        		Prison.get().getPlatform().getConfigBooleanTrue( "ranks.gui-default-include-rankup-button") ||
        	 LadderManager.LADDER_PRESTIGES.equalsIgnoreCase( ladderName ) &&
        		Prison.get().getPlatform().getConfigBooleanTrue( "ranks.gui-prestiges-include-rankup-button") ||
        	 !LadderManager.LADDER_DEFAULT.equalsIgnoreCase( ladderName ) &&
        	 	!LadderManager.LADDER_PRESTIGES.equalsIgnoreCase( ladderName ) &&
        	 	Prison.get().getPlatform().getConfigBooleanTrue( "ranks.gui-others-include-rankup-button")
        		) {
        	
			// Add Rankup button: Using NBTs:
			String rankupTitle = ladderName.equalsIgnoreCase("prestiges") ? "Prestige" : "Rankup";
			
			ButtonLore rankupLore = new ButtonLore( guiRanksLoreClickToRankupMsg(), 
					guiRanksLoreRankupIfEnoughMoneyMsg());
			
			Button rankupButton = new Button( 0, XMaterial.EMERALD_BLOCK, rankupLore, rankupTitle );
			String rankupCommand = "rankup " + (ladderName.equalsIgnoreCase("default") ? "" : ladderName);
			
//			PrisonNBTUtil nbtUtil = new PrisonNBTUtil();
//			NBTItem nbtItem = nbtUtil == null ? null : nbtUtil.getNBT(rankupButton.getButtonItem());
			PrisonNBTUtil.setNBTBoolean( rankupButton.getButtonItem(), SpigotGUIMenuTools.GUI_MENU_TOOLS_NBT_ENABLED, true);
			PrisonNBTUtil.setNBTString( rankupButton.getButtonItem(), SpigotGUIMenuTools.GUI_MENU_TOOLS_NBT_COMMAND, rankupCommand);
			
//        		SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_gui_lore_rankup)));
			// NOTE: Button position will be properly assigned in the setButtonNextAvilable:
			gui.addButton( guiPageData.setButtonNextAvailable( rankupButton ) );
			
        }
        	
        
        // Open GUI.
        gui.open();
    }
}
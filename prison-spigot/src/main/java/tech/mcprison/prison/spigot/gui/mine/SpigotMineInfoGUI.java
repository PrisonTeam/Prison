package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotGUIMessages;

/**
 * @author GABRYCA
 */
public class SpigotMineInfoGUI 
	extends SpigotGUIMessages {

    private final Player p;
	private final Mine mine;
    private final String mineName;
//    int dimension = 45;

    public SpigotMineInfoGUI(Player p, Mine mine, String mineName){
        this.p = p;
        this.mine = mine;
        this.mineName = mineName;
    }

    public void open(){

        int totalArraySize = 45;
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, 1, "gui admin mines", "gui admin mines" );

        
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3Mines -> MineInfo" );

        ButtonLore resetMineLore = new ButtonLore(createLore(
        		guiLeftClickToResetMsg(),
        		guiRightClickToToggleMsg(),
        		guiRightClickShiftToToggleMsg() ),
                createLore(
                		guiRanksLoreSkipResetInstruction1Msg(),
                		guiRanksLoreSkipResetInstruction2Msg(),
                		guiRanksLoreSkipResetInstruction3Msg(),
                        "",
                        guiRanksLoreSetMineDelayInstruction1Msg(),
                        guiRanksLoreSetMineDelayInstruction2Msg(),
                        guiRanksLoreSetMineDelayInstruction3Msg() ));

        ButtonLore mineSpawnLore = new ButtonLore(
        			guiRanksLoreClickToUseMsg(), 
        			guiRanksLoreSpawnPointMsg());

        ButtonLore minesNotificationsLore = new ButtonLore( guiClickToOpenMsg(), guiClickToEditMsg() );
        
        ButtonLore minesTpLore = new ButtonLore(
        			guiRanksLoreClickToTeleportMsg(), 
        			guiRanksLoreTpToMineMsg() );
        
        ButtonLore blocksOfTheMineLore = new ButtonLore(
        		guiClickToOpenMsg(), 
        		guiRanksLoreBlocksMsg());
        
        ButtonLore mineResetTimeLore = new ButtonLore(createLore( guiClickToOpenMsg() ), 
        		createLore(
        				guiRanksLoreResetTimeMsg() + " &7" + mine.getResetTime()));
        
        ButtonLore mineRenameLore = new ButtonLore(
        		guiRanksLoreClickToRenameMsg(), 
        		guiRanksLoreMineNameMsg() + " " + mineName);
//        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);

        // Create the button, set the material, amount, lore and name
//        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, "&cClose")));
        gui.addButton(new Button(10, XMaterial.EMERALD_BLOCK, resetMineLore, "&3Reset_Mine: " + mineName ));
        gui.addButton(new Button(12, XMaterial.COMPASS, mineSpawnLore, "&3Mine_Spawn: " + mineName ));
        gui.addButton(new Button(14, XMaterial.OAK_SIGN, minesNotificationsLore, "&3Mine_notifications: " + mineName ));
        gui.addButton(new Button(16, XMaterial.ARROW, minesTpLore, "&3TP_to_the_Mine: " + mineName ));
        gui.addButton(new Button(28, XMaterial.COAL_ORE, blocksOfTheMineLore, "&3Blocks_of_the_Mine: " + mineName ));
        gui.addButton(new Button(30, XMaterial.CLOCK, 1, mineResetTimeLore, "&3Reset_Time: " + mineName ));
        gui.addButton(new Button(32 ,XMaterial.FEATHER, mineRenameLore, "&3Mine_Name: " + mineName ));

        // Mine show Item of Player's GUI aka /gui mines.
        XMaterial xMaterial = XMaterial.COAL_ORE;
        String customItem = guiConfig.getString("Options.Mines.MaterialType." + mineName);
        if (customItem != null){
            XMaterial mineXMaterial = SpigotUtil.getXMaterial(customItem);
            if (mineXMaterial != null){
                xMaterial = mineXMaterial;
            }
        }

        // Lore
        ButtonLore mineShowItemLore = new ButtonLore(createLore( guiClickToEditMsg() ), 
        		createLore(
        				guiRanksLoreShowItemMsg() + " &7" + xMaterial.name(),
        				guiRanksLoreShowItemDescription1Msg(),
        				guiRanksLoreShowItemDescription2Msg(),
        				guiRanksLoreShowItemDescription3Msg()
        ));

        // ItemStack
        gui.addButton(new Button(34, xMaterial, mineShowItemLore, "&3Mine_Show_Item: " + mineName));

        
        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsNoPaging( gui, guiPageData );

        
        // Opens the inventory
        gui.open();
    }
}

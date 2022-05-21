package tech.mcprison.prison.spigot.gui.sellall;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SellAllDelayGUI extends SpigotGUIComponents {

    private final Player p;
    private final int val;

    private int page = 0;
    private String cmdPage;
    private String cmdReturn;

    public SellAllDelayGUI( Player p, int val, int page, String cmdPage, String cmdReturn ){
        this.p = p;
        this.val = val;
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;
    }

    public void open() {

        updateSellAllConfig();

        
        
        
        int totalArraySize = 45;
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


//        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3SellAll -> Delay");

        ButtonLore changeDecreaseValueLore = new ButtonLore( guiClickToDecreaseMsg(), null);
//        ButtonLore changeDecreaseValueLore = new ButtonLore(
//        		messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_decrease), null);
        ButtonLore confirmButtonLore = new ButtonLore(createLore(
        		guiLeftClickToConfirmMsg(), 
        		guiRightClickToCancelMsg() ), 
        		createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_delay) + " " + val + "s"));
        
        ButtonLore changeIncreaseValueLore = new ButtonLore( guiClickToIncreaseMsg(), null);
//        ButtonLore changeIncreaseValueLore = new ButtonLore(
//        		messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_increase), null);

        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;
        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, "&3Delay " + + val + " - 1" ));
        gui.addButton(new Button(10, decreaseMat, 10, changeDecreaseValueLore, "&3Delay " + val + " - 10" ));
        gui.addButton(new Button(19, decreaseMat, changeDecreaseValueLore, "&3Delay " + val + " - 100"));
        gui.addButton(new Button(28, decreaseMat, changeDecreaseValueLore, "&3Delay " + val + " - 1000"));
        gui.addButton(new Button(37, decreaseMat, changeDecreaseValueLore, "&3Delay " + val + " - 10000"));

        // Create a button and set the position
        gui.addButton(new Button(22, XMaterial.CLOCK, confirmButtonLore, "&3Confirm: Delay " + val));

        // Increase button
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, "&3Delay " + val + " + 1"));
        gui.addButton(new Button(16, increaseMat, 10, changeIncreaseValueLore, "&3Delay " + val + " + 10"));
        gui.addButton(new Button(25, increaseMat, changeIncreaseValueLore,"&3Delay " + val + " + 100"));
        gui.addButton(new Button(34, increaseMat, changeIncreaseValueLore, "&3Delay " + val + " + 1000"));
        gui.addButton(new Button(43, increaseMat, changeIncreaseValueLore, "&3Delay " + val + " + 10000"));

        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsNoPaging( gui, guiPageData );

        
        
        gui.open();
    }
}

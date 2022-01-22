package tech.mcprison.prison.spigot.gui;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotPrisonGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotPrisonGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create PrisonGUI, it requires Player (who will open the GUI), size and title.
    	
        int totalArraySize = 36;
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, 1, "gui", "close" );


    	
//        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3PrisonManager");

        // Create and add buttons.
        gui.addButton(new Button(10, XMaterial.TRIPWIRE_HOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_button_description)), "&3Ranks - Ladders"));
        gui.addButton(new Button(13, XMaterial.IRON_PICKAXE, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), messages.getString(MessagesConfig.StringID.spigot_gui_lore_autofeatures_button_description)), SpigotPrison.format("&3AutoManager")));
        gui.addButton(new Button(16, XMaterial.DIAMOND_ORE, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), messages.getString(MessagesConfig.StringID.spigot_gui_lore_mines_button_description)), SpigotPrison.format("&3Mines")));
        gui.addButton(new Button(29, XMaterial.CHEST, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_button_description)), SpigotPrison.format("&3SellAll")));

        //        gui.addButton(new Button(33, XMaterial.CHEST_MINECART, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), messages.getString(MessagesConfig.StringID.spigot_gui_lore_backpacks_button_description)), SpigotPrison.format("&3Backpacks")));
//        gui.addButton(new Button(44, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null), SpigotPrison.format("&cClose")));

        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsNoPaging( gui, guiPageData );

        
        gui.open();
    }
}

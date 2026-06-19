package tech.mcprison.prison.spigot.gui;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

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


    	
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3PrisonManager");

        // Create and add buttons.
        gui.addButton(new Button(10, XMaterial.TRIPWIRE_HOOK, new ButtonLore( guiLeftClickToOpenMsg(), messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_button_description)), "&3Ranks - Ladders" ));
        gui.addButton(new Button(16, XMaterial.DIAMOND_ORE, new ButtonLore( guiLeftClickToOpenMsg(), messages.getString(MessagesConfig.StringID.spigot_gui_lore_mines_button_description)), "&3Mines" ));
        gui.addButton(new Button(29, XMaterial.CHEST, new ButtonLore( guiLeftClickToOpenMsg(), messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_button_description)), "&3SellAll" ));


        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsNoPaging( gui, guiPageData );

        
        gui.open();
    }
}

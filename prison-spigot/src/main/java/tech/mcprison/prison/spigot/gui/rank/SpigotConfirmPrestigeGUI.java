package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotConfirmPrestigeGUI extends SpigotGUIComponents {

    private final Player player;
    private final List<String> lore;

    public SpigotConfirmPrestigeGUI(Player player, List<String> lore ) {
        this.player = player;
        this.lore = lore;
    }
    
    public void open(){
    	
    	// Create the inventory
    	int dimension = 9;
    	PrisonGUI gui = new PrisonGUI( player, dimension, "&3Prestige -> Confirmation");
    	
    	ButtonLore confirmLore = new ButtonLore(createLore(
    			guiClickToConfirmMsg()), lore );
    	
    	ButtonLore cancelLore = new ButtonLore( guiClickToCancelMsg(), null);
    	
    	
    	// Create the button, set up the material, amount, lore and name
    	gui.addButton(new Button(2, XMaterial.EMERALD_BLOCK, confirmLore, "&3Confirm: Prestige" ));
    	gui.addButton(new Button(6, XMaterial.REDSTONE_BLOCK, cancelLore, "&3Cancel: Don't Prestige" ));
    	
    	gui.open();
    }

//    public void open(){
//
//        // Create the inventory
//        int dimension = 9;
//        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Prestige -> Confirmation");
//
//        ButtonLore confirmLore = new ButtonLore(createLore(
//        		guiClickToConfirmMsg()), createLore(
//                messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_warning_1),
//                messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_warning_2),
//                messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_warning_3)));
//
//        ButtonLore cancelLore = new ButtonLore( guiClickToCancelMsg(), null);
//
//
//        // Create the button, set up the material, amount, lore and name
//        gui.addButton(new Button(2, XMaterial.EMERALD_BLOCK, confirmLore, "&3Confirm: Prestige" ));
//        gui.addButton(new Button(6, XMaterial.REDSTONE_BLOCK, cancelLore, "&3Cancel: Don't Prestige" ));
//
//        gui.open();
//    }
}

package tech.mcprison.prison.spigot.gui.rank;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotRankPriceGUI extends SpigotGUIComponents {

    private final Player p;
    private final String rankName;
    private final Integer val;

    public SpigotRankPriceGUI(Player p, Integer val, String rankName){
        this.p = p;
        this.val = val;
        this.rankName = rankName;
    }

    public void open() {

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3RankManager -> RankPrice");

        ButtonLore changeDecreaseValueLore = new ButtonLore( guiClickToDecreaseMsg(), null);
//        ButtonLore changeDecreaseValueLore = new ButtonLore(
//        		messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_decrease), null);
        
        ButtonLore confirmButtonLore = new ButtonLore(createLore(
        		guiLeftClickToConfirmMsg(), 
        		guiRightClickToCancelMsg() ), 
        		createLore( guiPriceMsg(val) ));
        
        ButtonLore changeIncreaseValueLore = new ButtonLore( guiClickToIncreaseMsg(), null);
//        ButtonLore changeIncreaseValueLore = new ButtonLore(
//        		messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_increase), null);

        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, "&3" + rankName + " " + val + " - 1" ));
        gui.addButton(new Button(10, decreaseMat, 10, changeDecreaseValueLore, "&3" + rankName + " " + val + " - 10" ));
        gui.addButton(new Button(19, decreaseMat, changeDecreaseValueLore, "&3" + rankName + " " + val + " - 100" ));
        gui.addButton(new Button(28, decreaseMat, changeDecreaseValueLore, "&3" + rankName + " " + val + " - 1000" ));
        gui.addButton(new Button(37, decreaseMat, changeDecreaseValueLore, "&3" + rankName + " " + val + " - 10000" ));

        // Create a button and set the position
        gui.addButton(new Button(22, XMaterial.TRIPWIRE_HOOK, confirmButtonLore, "&3Confirm: " + rankName + " " + val ));

        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;

        // Increase button
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, "&3" + rankName + " " + val + " + 1" ));
        gui.addButton(new Button(16, increaseMat, 10, changeIncreaseValueLore, "&3" + rankName + " " + val + " + 10" ));
        gui.addButton(new Button(25, increaseMat, changeIncreaseValueLore, "&3" + rankName + " " + val + " + 100" ));
        gui.addButton(new Button(34, increaseMat, changeIncreaseValueLore, "&3" + rankName + " " + val + " + 1000" ));
        gui.addButton(new Button(43, increaseMat, changeIncreaseValueLore, "&3" + rankName + " " + val + " + 10000" ));

        gui.open();
    }
}

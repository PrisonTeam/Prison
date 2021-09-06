package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
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


    public SellAllDelayGUI(Player p, int val){
        this.p = p;
        this.val = val;
    }

    public void open() {

        updateSellAllConfig();

        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3SellAll -> Delay");

        ButtonLore changeDecreaseValueLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_decrease), null);
        ButtonLore confirmButtonLore = new ButtonLore(createLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_left_to_confirm), newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_to_cancel)), createLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_delay) + " " + val + "s"));
        ButtonLore changeIncreaseValueLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_increase), null);

        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;
        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3Delay " + + val + " - 1" )));
        gui.addButton(new Button(10, decreaseMat, 10, changeDecreaseValueLore, SpigotPrison.format("&3Delay " + val + " - 10")));
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

        gui.open();
    }
}

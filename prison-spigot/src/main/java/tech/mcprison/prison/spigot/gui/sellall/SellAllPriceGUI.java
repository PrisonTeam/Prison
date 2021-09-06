package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SellAllPriceGUI extends SpigotGUIComponents {

    private final Player p;
    private final String itemID;
    private final Double val;

    public SellAllPriceGUI(Player p, Double val, String itemID){
        this.p = p;
        this.val = val;
        this.itemID = itemID;
    }

    public void open() {

        updateSellAllConfig();

        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3SellAll -> ItemValue");

        ButtonLore changeDecreaseValueLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_decrease), null);
        ButtonLore confirmButtonLore = new ButtonLore(createLore(
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_left_to_confirm), newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_to_cancel)), createLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_price) + " " + val));
        ButtonLore changeIncreaseValueLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_increase), null);

        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;
        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 1" )));
        gui.addButton(new Button(10, decreaseMat, 10, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 10")));
        gui.addButton(new Button(19, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 100")));
        gui.addButton(new Button(28, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 1000")));
        gui.addButton(new Button(37, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 10000")));

        // Create a button and set the position
        gui.addButton(new Button(22, XMaterial.TRIPWIRE_HOOK, confirmButtonLore, SpigotPrison.format("&3Confirm: " + itemID + " " + val)));

        // Increase button
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 1" )));
        gui.addButton(new Button(16, increaseMat, 10, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 10")));
        gui.addButton(new Button(25, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 100")));
        gui.addButton(new Button(34, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 1000")));
        gui.addButton(new Button(43, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 10000")));

        gui.open();
    }
}

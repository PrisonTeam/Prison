package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SellAllPrestigesSetMultiplierGUI extends SpigotGUIComponents {

    private final Player p;
    private final double val;
    private final String prestigeName;

    public SellAllPrestigesSetMultiplierGUI(Player p, double val, String prestigeName){
        this.p = p;
        this.val = val;
        this.prestigeName = prestigeName;
    }

    public void open() {

        updateSellAllConfig();

        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Edit -> Multiplier");

        ButtonLore changeDecreaseValueLore = new ButtonLore( guiClickToDecreaseMsg(), null);
//        ButtonLore changeDecreaseValueLore = new ButtonLore(
//        		messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_decrease), null);

        ButtonLore confirmButtonLore = new ButtonLore(createLore(
        		guiLeftClickToConfirmMsg(),
        		guiRightClickToCancelMsg() ), 
        		createLore(
        				messages.getString(MessagesConfig.StringID.spigot_gui_lore_multiplier) + " " + "x" + val));

        ButtonLore changeIncreaseValueLore = new ButtonLore( guiClickToIncreaseMsg(), null);
//        ButtonLore changeIncreaseValueLore = new ButtonLore(
//        		messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_increase), null);

        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;
        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " - 0.1" )));
        gui.addButton(new Button(10, decreaseMat, 10, changeDecreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " - 0.5")));
        gui.addButton(new Button(19, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " - 1")));
        gui.addButton(new Button(28, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " - 2")));
        gui.addButton(new Button(37, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " - 5")));


        // Create a button and set the position
        gui.addButton(new Button(22, XMaterial.CLOCK, confirmButtonLore, SpigotPrison.format("&3Confirm: "  + prestigeName + " " + val)));

        // Increase button
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " + 0.1" )));
        gui.addButton(new Button(16, increaseMat, 10, changeIncreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " + 0.5")));
        gui.addButton(new Button(25, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " + 1")));
        gui.addButton(new Button(43, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " + 2")));
        gui.addButton(new Button(54, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + prestigeName + " " + val + " + 5")));

        gui.open();
    }
}

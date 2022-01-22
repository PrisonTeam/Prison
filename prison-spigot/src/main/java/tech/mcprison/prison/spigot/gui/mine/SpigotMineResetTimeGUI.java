package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMineResetTimeGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private final Integer val;

    public SpigotMineResetTimeGUI(Player p, Integer val, String mineName){
        this.p = p;
        this.val = val;
        this.mineName = mineName;
    }

    public void open() {

        // Create GUI.
        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3MineInfo -> ResetTime");


        ButtonLore changeDecreaseValueLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_decrease), null);
        ButtonLore confirmButtonLore = new ButtonLore(createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_left_to_confirm), messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_cancel)), createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_reset_time) + " " + val));
        ButtonLore changeIncreaseValueLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_increase), null);

        // XMaterials.
        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;
        XMaterial increaseMat = XMaterial.REDSTONE_BLOCK;
        XMaterial watch = XMaterial.CLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 1"));
        gui.addButton(new Button(10, decreaseMat, 5, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 5"));
        gui.addButton(new Button(19, decreaseMat, 10, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 10"));
        gui.addButton(new Button(28, decreaseMat, 50, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 50"));
        gui.addButton(new Button(37, decreaseMat, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 100"));

        // Create a button and set the position
        gui.addButton(new Button(22, watch, confirmButtonLore, "&3Confirm: " + mineName + " " + val));

        // Increase button
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 1"));
        gui.addButton(new Button(16, increaseMat, 5, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 5"));
        gui.addButton(new Button(25, increaseMat, 10, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 10"));
        gui.addButton(new Button(34, increaseMat, 50, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 50"));
        gui.addButton(new Button(43, increaseMat, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 100"));

        // Open GUI.
        gui.open();
    }
}

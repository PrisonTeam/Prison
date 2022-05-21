package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA (AnonymousGCA)
 */
public class SpigotMineNotificationRadiusGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private final long val;
    private final String typeNotification;

    public SpigotMineNotificationRadiusGUI(Player p, Long val, String typeNotification, String mineName){
        this.p = p;
        this.val = val;
        this.mineName = mineName;
        this.typeNotification = typeNotification;
    }

    public void open() {

        // Create GUI.
        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3MineNotifications -> Radius");

        ButtonLore changeDecreaseValueLore = new ButtonLore( guiClickToDecreaseMsg(), null);
        
        ButtonLore confirmButtonLore = new ButtonLore(createLore(
        		guiLeftClickToConfirmMsg() ), 
        		createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_radius) + " " + val,
                guiRightClickToCancelMsg() ));
        
        ButtonLore changeIncreaseValueLore = new ButtonLore( guiClickToIncreaseMsg(), null);

        // XMaterials.
        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;
        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;
        XMaterial watch = XMaterial.CLOCK;

        // Decrease buttons.
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 1 " + typeNotification));
        gui.addButton(new Button(10, decreaseMat, 5, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 5 " + typeNotification));
        gui.addButton(new Button(19, decreaseMat, 10, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 10 " + typeNotification));
        gui.addButton(new Button(28, decreaseMat, 50, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 50 " + typeNotification));
        gui.addButton(new Button(37, decreaseMat, changeDecreaseValueLore, "&3" + mineName + " " + val + " - 100 " + typeNotification));

        // Confirm button.
        gui.addButton(new Button(22, watch, confirmButtonLore, "&3Confirm: " + mineName + " " + val + " " + typeNotification));

        // Increase buttons.
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 1 " + typeNotification));
        gui.addButton(new Button(16, increaseMat, 5, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 5 " + typeNotification));
        gui.addButton(new Button(25, increaseMat, 10, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 10 " + typeNotification));
        gui.addButton(new Button(34, increaseMat, 50, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 50 " + typeNotification));
        gui.addButton(new Button(43, increaseMat, 100, changeIncreaseValueLore, "&3" + mineName + " " + val + " + 100 " + typeNotification));

        // Open GUI.
        gui.open();
    }
}

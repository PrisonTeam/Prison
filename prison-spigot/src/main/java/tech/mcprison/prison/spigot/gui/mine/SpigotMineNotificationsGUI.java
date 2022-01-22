package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMineNotificationsGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;

    public SpigotMineNotificationsGUI(Player p, String mineName){
        this.p = p;
        this.mineName = mineName;
    }

    public void open() {

        // Create GUI.
        int dimension = 27;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3MineInfo -> MineNotifications");

        // Init variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);
        String enabledOrDisabled = m.getNotificationMode().name();

        ButtonLore modeWithinLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_select), messages.getString(MessagesConfig.StringID.spigot_gui_lore_enable_within_mode));
        ButtonLore modeRadiusLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_select), messages.getString(MessagesConfig.StringID.spigot_gui_lore_enable_radius_mode));
        ButtonLore disabledModeLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_select), messages.getString(MessagesConfig.StringID.spigot_gui_lore_disable_notifications));
        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);

        // Add button.
        gui.addButton(new Button(26, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, "&cClose"));

        // Add the selected lore to the mode used
        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            // Add the selected lore
            disabledModeLore.addLineLoreDescription(SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_gui_lore_selected)));

        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            // Add the selected lore
            modeWithinLore.addLineLoreDescription(SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_gui_lore_selected)));

        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            // Add the selected lore
            modeRadiusLore.addLineLoreDescription(SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_gui_lore_selected)));

        }

        // Create a button
        Button modeWithin = new Button(11, XMaterial.IRON_DOOR, modeWithinLore, "&3Within_Mode: " + mineName);
        Button radiusMode = new Button(13, XMaterial.OAK_FENCE, modeRadiusLore, "&3Radius_Mode: " + mineName);
        Button disabledMode = new Button(15, XMaterial.REDSTONE_BLOCK, disabledModeLore, "&3Disabled_Mode: " + mineName);

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            // Add a button to the inventory
            gui.addButton(modeWithin);
            gui.addButton(radiusMode);
            // Add an enchantment effect to the button
            disabledMode.addUnsafeEnchantment(Enchantment.LUCK, 1);
            gui.addButton(disabledMode);

            // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            // Add a button to the inventory
            modeWithin.addUnsafeEnchantment(Enchantment.LUCK, 1);
            gui.addButton(modeWithin);
            gui.addButton(radiusMode);
            gui.addButton(disabledMode);

            // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            // Add a button to the inventory
            gui.addButton(modeWithin);
            radiusMode.addUnsafeEnchantment(Enchantment.LUCK, 1);
            gui.addButton(radiusMode);
            gui.addButton(disabledMode);
        }

        // Open GUI.
        gui.open();
    }
}

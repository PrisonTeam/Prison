package tech.mcprison.prison.spigot.gui.autofeatures;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotAutoFeaturesGUI extends SpigotGUIComponents {

    private final Player p;
    private final AutoFeaturesFileConfig afConfig = afConfig();

    public SpigotAutoFeaturesGUI(Player p){
        this.p = p;
    }

    public void open() {

        int dimension = 27;

        PrisonGUI gui;

        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);

        if (afConfig != null && afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {
            
            ButtonLore disable = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_disable), messages.getString(MessagesConfig.StringID.spigot_gui_lore_enabled));
            ButtonLore enable = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_enable), messages.getString(MessagesConfig.StringID.spigot_gui_lore_disabled));

            gui = new PrisonGUI(p, dimension, "&3PrisonManager -> AutoFeatures");
            gui.addButton(new Button(dimension -1,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));

            if (afConfig.isFeatureBoolean(AutoFeatures.playSoundIfInventoryIsFull)) {
                gui.addButton(new Button(0, XMaterial.LIME_STAINED_GLASS_PANE, disable, SpigotPrison.format("&aFull-Inventory-Sound Enabled")));
            } else {
                gui.addButton(new Button(0, XMaterial.RED_STAINED_GLASS_PANE, enable, SpigotPrison.format("&cFull-Inventory-Sound Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.hologramIfInventoryIsFull)) {
                gui.addButton(new Button(8, XMaterial.LIME_STAINED_GLASS_PANE, disable, SpigotPrison.format("&aFull-Inventory-Hologram Enabled")));
            } else {
                gui.addButton(new Button(8, XMaterial.RED_STAINED_GLASS_PANE, enable, SpigotPrison.format("&cFull-Inventory-Hologram Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {
                gui.addButton(new Button(18, XMaterial.LIME_STAINED_GLASS_PANE, disable, SpigotPrison.format("&aAll Enabled")));
            } else {
                gui.addButton(new Button(18, XMaterial.RED_STAINED_GLASS_PANE, enable, SpigotPrison.format("&cAll Disabled")));
            }

            disable.setLoreAction(createLore(
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_left_to_open),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_disable)
            ));

            enable.setLoreAction(createLore(
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_left_to_open),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_enable)
            ));

            if (afConfig.isFeatureBoolean(AutoFeatures.autoPickupEnabled)) {
                gui.addButton(new Button(11, XMaterial.CHEST, disable, SpigotPrison.format("&3AutoPickup Enabled")));
            } else {
                gui.addButton(new Button(11, XMaterial.CHEST, enable, SpigotPrison.format("&cAutoPickup Disabled")));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoSmeltEnabled)) {
                gui.addButton(new Button(13, XMaterial.FURNACE, disable, SpigotPrison.format("&3AutoSmelt Enabled")));
            } else {
                gui.addButton(new Button(13, XMaterial.FURNACE, enable, SpigotPrison.format("&cAutoSmelt Disabled")));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockEnabled)) {
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, disable, SpigotPrison.format("&3AutoBlock Enabled")));
            } else {
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, enable, SpigotPrison.format("&cAutoBlock Disabled")));
            }

        } else {

            gui = new PrisonGUI(p, 9, "&3PrisonManager -> AutoFeatures");

            ButtonLore lore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_enable), messages.getString(MessagesConfig.StringID.spigot_gui_lore_disabled));
            Button enabledOrDisabled = new Button(2, XMaterial.LIME_STAINED_GLASS_PANE, lore, SpigotPrison.format("&cAll Disabled"));
            gui.addButton(enabledOrDisabled);
            gui.addButton(new Button(6,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));
        }

        gui.open();
    }
}
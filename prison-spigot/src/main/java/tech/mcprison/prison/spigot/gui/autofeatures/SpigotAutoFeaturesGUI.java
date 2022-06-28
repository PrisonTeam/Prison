package tech.mcprison.prison.spigot.gui.autofeatures;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
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

        ButtonLore closeGUILore = new ButtonLore( guiClickToCloseMsg(), null);

        if (afConfig != null && afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {
            
            ButtonLore disable = new ButtonLore(
            		guiRightClickShiftToDisableMsg(), 
            		messages.getString(MessagesConfig.StringID.spigot_gui_lore_enabled));
            ButtonLore enable = new ButtonLore(
            		guiRightClickToEnableMsg(), messages.getString(MessagesConfig.StringID.spigot_gui_lore_disabled));

            gui = new PrisonGUI(p, dimension, "&3PrisonManager -> AutoFeatures");
            gui.addButton(new Button(dimension -1,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, "&cClose" ));

            if (afConfig.isFeatureBoolean(AutoFeatures.playSoundIfInventoryIsFull)) {
                gui.addButton(new Button(0, XMaterial.LIME_STAINED_GLASS_PANE, disable, "&aFull-Inventory-Sound Enabled" ));
            } else {
                gui.addButton(new Button(0, XMaterial.RED_STAINED_GLASS_PANE, enable, "&cFull-Inventory-Sound Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.actionBarMessageIfInventoryIsFull)) {
                gui.addButton(new Button(8, XMaterial.LIME_STAINED_GLASS_PANE, disable, "&aFull-Inventory-ActionBar Enabled" ));
            } else {
                gui.addButton(new Button(8, XMaterial.RED_STAINED_GLASS_PANE, enable, "&cFull-Inventory-ActionBar Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {
                gui.addButton(new Button(18, XMaterial.LIME_STAINED_GLASS_PANE, disable, "&aAll Enabled" ));
            } else {
                gui.addButton(new Button(18, XMaterial.RED_STAINED_GLASS_PANE, enable, "&cAll Disabled" ));
            }

            disable.setLoreAction(createLore(
            		guiLeftClickToOpenMsg(),
            		guiRightClickToDisableMsg()
            ));

            enable.setLoreAction(createLore(
            		guiLeftClickToOpenMsg(),
            		guiRightClickToEnableMsg()
            ));

            if (afConfig.isFeatureBoolean(AutoFeatures.autoPickupEnabled)) {
                gui.addButton(new Button(11, XMaterial.CHEST, disable, "&3AutoPickup Enabled" ));
            } else {
                gui.addButton(new Button(11, XMaterial.CHEST, enable, "&cAutoPickup Disabled" ));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoSmeltEnabled)) {
                gui.addButton(new Button(13, XMaterial.FURNACE, disable, "&3AutoSmelt Enabled" ));
            } else {
                gui.addButton(new Button(13, XMaterial.FURNACE, enable, "&cAutoSmelt Disabled" ));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockEnabled)) {
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, disable, "&3AutoBlock Enabled" ));
            } else {
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, enable, "&cAutoBlock Disabled" ));
            }

        } else {

            gui = new PrisonGUI(p, 9, "&3PrisonManager -> AutoFeatures");

            ButtonLore lore = new ButtonLore(
            		guiRightClickToEnableMsg(), 
            		messages.getString(MessagesConfig.StringID.spigot_gui_lore_disabled));
            Button enabledOrDisabled = new Button(2, XMaterial.LIME_STAINED_GLASS_PANE, lore, "&cAll Disabled" );
            gui.addButton(enabledOrDisabled);
            gui.addButton(new Button(6,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, "&cClose" ));
        }

        gui.open();
    }
}
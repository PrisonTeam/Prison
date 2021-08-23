package tech.mcprison.prison.spigot.gui.autofeatures;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.spigot.SpigotPrison;
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

        ButtonLore closeGUILore = new ButtonLore(messages.getString("Lore.ClickToClose"), null);

        if (afConfig != null && afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {
            
            ButtonLore disable = new ButtonLore(messages.getString("Lore.ShiftAndRightClickToDisable"), null);
            ButtonLore enable = new ButtonLore(messages.getString("Lore.RightClickToEnable"), null);

            gui = new PrisonGUI(p, dimension, "&3PrisonManager -> AutoFeatures");

            gui.addButton(new Button(dimension -1,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));

            if (afConfig.isFeatureBoolean(AutoFeatures.playSoundIfInventoryIsFull)) {

                disable.setLoreDescription(messages.getString("Lore.FullSoundEnabled"));
                gui.addButton(new Button(0, XMaterial.LIME_STAINED_GLASS_PANE, disable, SpigotPrison.format("&a" + "Full-Inventory-Sound Enabled")));

            } else {

                enable.setLoreDescription(messages.getString("Lore.FullSoundDisabled"));
                gui.addButton(new Button(0, XMaterial.RED_STAINED_GLASS_PANE, enable, SpigotPrison.format("&c" + "Full-Inventory-Sound Disabled")));

            }

            if (afConfig.isFeatureBoolean(AutoFeatures.hologramIfInventoryIsFull)) {

                disable.setLoreDescription(messages.getString("Lore.FullHologramEnabled"));
                gui.addButton(new Button(8, XMaterial.LIME_STAINED_GLASS_PANE, disable, SpigotPrison.format("&a" + "Full-Inventory-Hologram Enabled")));

            } else {

                enable.setLoreDescription(messages.getString("Lore.FullHologramDisabled"));
                gui.addButton(new Button(8, XMaterial.RED_STAINED_GLASS_PANE, enable, SpigotPrison.format("&c" + "Full-Inventory-Hologram Disabled")));

            }

            if (afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {

                disable.setLoreDescription(messages.getString("Lore.EnabledAll"));
                gui.addButton(new Button(18, XMaterial.LIME_STAINED_GLASS_PANE, disable, SpigotPrison.format("&a" + "All Enabled")));

            } else {

                enable.setLoreDescription(messages.getString("Lore.DisabledAll"));
                gui.addButton(new Button(18, XMaterial.RED_STAINED_GLASS_PANE, enable, SpigotPrison.format("&c" + "All Disabled")));

            }

            disable.setLoreAction(createLore(
                    messages.getString("Lore.LeftClickToOpen"),
                    messages.getString("Lore.ShiftAndRightClickToDisable")
            ));

            enable.setLoreAction(createLore(
                    messages.getString("Lore.LeftClickToOpen"),
                    messages.getString("Lore.RightClickToEnable")
            ));

            if (afConfig.isFeatureBoolean(AutoFeatures.autoPickupEnabled)) {

                disable.setLoreDescription(messages.getString("Lore.AutoPickupGuiManager"));
                gui.addButton(new Button(11, XMaterial.CHEST, disable, SpigotPrison.format("&3" + "AutoPickup Enabled")));
            } else {

                enable.setLoreDescription(messages.getString("Lore.AutoPickupGuiManager"));
                gui.addButton(new Button(11, XMaterial.CHEST, enable, SpigotPrison.format("&c" + "AutoPickup Disabled")));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoSmeltEnabled)) {

                disable.setLoreDescription(messages.getString("Lore.AutoSmeltGuiManager"));
                gui.addButton(new Button(13, XMaterial.FURNACE, disable, SpigotPrison.format("&3" + "AutoSmelt Enabled")));
            } else {

                enable.setLoreDescription(messages.getString("Lore.AutoSmeltGuiManager"));
                gui.addButton(new Button(13, XMaterial.FURNACE, enable, SpigotPrison.format("&c" + "AutoSmelt Disabled")));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockEnabled)) {

                disable.setLoreDescription(messages.getString("Lore.AutoBlockGuiManager"));
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, disable, SpigotPrison.format("&3" + "AutoBlock Enabled")));

            } else {

                enable.setLoreDescription(messages.getString("Lore.AutoBlockGuiManager"));
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, enable, SpigotPrison.format("&c" + "AutoBlock Disabled")));
            }

        } else {

            gui = new PrisonGUI(p, 9, "&3PrisonManager -> AutoFeatures");

            ButtonLore lore = new ButtonLore(messages.getString("Lore.RightClickToEnable"), messages.getString("Lore.DisabledAll"));

            Button enabledOrDisabled = new Button(2, XMaterial.LIME_STAINED_GLASS_PANE, lore, SpigotPrison.format("&c" + "All Disabled"));

            gui.addButton(enabledOrDisabled);
            gui.addButton(new Button(6,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));
        }

        gui.open();
    }
}
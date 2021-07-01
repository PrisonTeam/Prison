package tech.mcprison.prison.spigot.gui.autofeatures;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
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

        // Close GUI button lore.
        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );


        if (afConfig != null && afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {

            gui = new PrisonGUI(p, dimension, "&3PrisonManager -> AutoFeatures");

            gui.addButton(new Button(dimension -1,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));

            if (afConfig.isFeatureBoolean(AutoFeatures.playSoundIfInventoryIsFull)) {

                List<String> EnabledOrDisabledLore = createLore(
                        messages.getString("Lore.FullSoundEnabled"),
                        messages.getString("Lore.ShiftAndRightClickToDisable"));
                gui.addButton(new Button(0, XMaterial.LIME_STAINED_GLASS_PANE, EnabledOrDisabledLore, SpigotPrison.format("&a" + "Full-Inventory-Sound Enabled")));

            } else {

                List<String> EnabledOrDisabledLore = createLore(
                        messages.getString("Lore.FullSoundDisabled"),
                        messages.getString("Lore.RightClickToEnable"));
                gui.addButton(new Button(0, XMaterial.RED_STAINED_GLASS_PANE, EnabledOrDisabledLore, SpigotPrison.format("&c" + "Full-Inventory-Sound Disabled")));

            }

            if (afConfig.isFeatureBoolean(AutoFeatures.hologramIfInventoryIsFull)) {

                List<String> EnabledOrDisabledLore = createLore(
                        messages.getString("Lore.FullHologramEnabled"),
                        messages.getString("Lore.ShiftAndRightClickToDisable"));
                gui.addButton(new Button(8, XMaterial.LIME_STAINED_GLASS_PANE, EnabledOrDisabledLore, SpigotPrison.format("&a" + "Full-Inventory-Hologram Enabled")));

            } else {

                List<String> EnabledOrDisabledLore = createLore(
                        messages.getString("Lore.FullHologramDisabled"),
                        messages.getString("Lore.RightClickToEnable"));
                gui.addButton(new Button(8, XMaterial.RED_STAINED_GLASS_PANE, EnabledOrDisabledLore, SpigotPrison.format("&c" + "Full-Inventory-Hologram Disabled")));

            }

            if (afConfig.isFeatureBoolean(AutoFeatures.isAutoManagerEnabled)) {

                List<String> EnabledOrDisabledLore = createLore(
                        messages.getString("Lore.EnabledAll"),
                        messages.getString("Lore.ShiftAndRightClickToDisable"));
                gui.addButton(new Button(18, XMaterial.LIME_STAINED_GLASS_PANE, EnabledOrDisabledLore, SpigotPrison.format("&a" + "All Enabled")));

            } else {

                List<String> EnabledOrDisabledLore = createLore(
                        messages.getString("Lore.DisabledAll"),
                        messages.getString("Lore.RightClickToEnable"));
                gui.addButton(new Button(18, XMaterial.RED_STAINED_GLASS_PANE, EnabledOrDisabledLore, SpigotPrison.format("&c" + "All Disabled")));

            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoPickupEnabled)) {
                // Lore of the button
                List<String> autoPickupLore = createLore(
                        "&8-----------------------",
                        " ",
                        messages.getString("Lore.AutoPickupGuiManager"),
                        messages.getString("Lore.ShiftAndRightClickToDisable"),
                        messages.getString("Lore.LeftClickToOpen"),
                        " ",
                        "&8-----------------------"
                );
                gui.addButton(new Button(11, XMaterial.CHEST, autoPickupLore, SpigotPrison.format("&3" + "AutoPickup Enabled")));
            } else {
                // Lore of the button
                List<String> autoPickupLore = createLore(
                        "&8-----------------------",
                        " ",
                        messages.getString("Lore.AutoPickupGuiManager"),
                        messages.getString("Lore.RightClickToEnable"),
                        messages.getString("Lore.LeftClickToOpen"),
                        " ",
                        "&8-----------------------"
                );
                gui.addButton(new Button(11, XMaterial.CHEST, autoPickupLore, SpigotPrison.format("&c" + "AutoPickup Disabled")));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoSmeltEnabled)) {
                // Lore of the button
                List<String> autoSmeltLore = createLore(
                        "&8-----------------------",
                        " ",
                        messages.getString("Lore.AutoSmeltGuiManager"),
                        messages.getString("Lore.ShiftAndRightClickToDisable"),
                        messages.getString("Lore.LeftClickToOpen"),
                        " ",
                        "&8-----------------------"
                );
                gui.addButton(new Button(13, XMaterial.FURNACE, autoSmeltLore, SpigotPrison.format("&3" + "AutoSmelt Enabled")));
            } else {
                // Lore of the button
                List<String> autoSmeltLore = createLore(
                        "&8-----------------------",
                        " ",
                        messages.getString("Lore.AutoSmeltGuiManager"),
                        messages.getString("Lore.RightClickToEnable"),
                        messages.getString("Lore.LeftClickToOpen"),
                        " ",
                        "&8-----------------------"
                );
                gui.addButton(new Button(13, XMaterial.FURNACE, autoSmeltLore, SpigotPrison.format("&c" + "AutoSmelt Disabled")));
            }


            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockEnabled)) {
                // Lore of the button
                List<String> autoBlockLore = createLore(
                        "&8-----------------------",
                        " ",
                        messages.getString("Lore.AutoBlockGuiManager"),
                        messages.getString("Lore.ShiftAndRightClickToDisable"),
                        messages.getString("Lore.LeftClickToOpen"),
                        " ",
                        "&8-----------------------"
                );
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, autoBlockLore, SpigotPrison.format("&3" + "AutoBlock Enabled")));

            } else {
                // Lore of the button
                List<String> autoBlockLore = createLore(
                        "&8-----------------------",
                        " ",
                        messages.getString("Lore.AutoBlockGuiManager"),
                        messages.getString("Lore.RightClickToEnable"),
                        messages.getString("Lore.LeftClickToOpen"),
                        " ",
                        "&8-----------------------"
                );
                gui.addButton(new Button(15, XMaterial.CRAFTING_TABLE, autoBlockLore, SpigotPrison.format("&c" + "AutoBlock Disabled")));
            }

        } else {

            gui = new PrisonGUI(p, 9, "&3PrisonManager -> AutoFeatures");

            List<String> EnabledOrDisabledLore = createLore(
                    messages.getString("Lore.DisabledAll"),
                    messages.getString("Lore.RightClickToEnable"));
            Button enabledOrDisabled = new Button(2, XMaterial.LIME_STAINED_GLASS_PANE, EnabledOrDisabledLore, SpigotPrison.format("&c" + "All Disabled"));

            gui.addButton(enabledOrDisabled);
            gui.addButton(new Button(6,XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));
        }

        gui.open();
    }
}
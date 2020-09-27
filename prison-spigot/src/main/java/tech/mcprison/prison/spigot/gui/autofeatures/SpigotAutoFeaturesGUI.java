package tech.mcprison.prison.spigot.gui.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotAutoFeaturesGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotAutoFeaturesGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3PrisonManager -> AutoFeatures"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Config
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();

        if (guiBuilder(inv, GuiConfig, afConfig)) return;

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv, Configuration guiConfig, AutoFeaturesFileConfig afConfig) {
        try {
            buttonsSetup(inv, guiConfig, afConfig);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, Configuration guiConfig, AutoFeaturesFileConfig afConfig) {

        // Declare buttons
        ItemStack autoPickup;
        ItemStack autoSmelt;
        ItemStack autoBlock;
        ItemStack enabledOrDisabled;
        ItemStack playSound;
        ItemStack hologram;

        if ( afConfig.isFeatureBoolean( AutoFeatures.playSoundIfInventoryIsFull ) ){

            List<String> EnabledOrDisabledLore = createLore(
                    guiConfig.getString("Gui.Lore.FullSoundEnabled"),
                    guiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable"));
            playSound = createButton(Material.EMERALD_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&a" + "Full_Inv_Play_Sound Enabled"));

        } else {

            List<String> EnabledOrDisabledLore = createLore(
                    guiConfig.getString("Gui.Lore.FullSoundDisabled"),
                    guiConfig.getString("Gui.Lore.RightClickToEnable"));
            playSound = createButton(Material.REDSTONE_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&c" + "Full_Inv_Play_Sound Disabled"));

        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.hologramIfInventoryIsFull ) ){

            List<String> EnabledOrDisabledLore = createLore(
                    guiConfig.getString("Gui.Lore.FullHologramEnabled"),
                    guiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable"));
            hologram = createButton(Material.EMERALD_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&a" + "Full_Inv_Hologram Enabled"));

        } else {

            List<String> EnabledOrDisabledLore = createLore(
                    guiConfig.getString("Gui.Lore.FullHologramDisabled"),
                    guiConfig.getString("Gui.Lore.RightClickToEnable"));
            hologram = createButton(Material.REDSTONE_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&c" + "Full_Inv_Hologram Disabled"));

        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.isAutoManagerEnabled ) ){

            List<String> EnabledOrDisabledLore = createLore(
                    guiConfig.getString("Gui.Lore.EnabledAll"),
                    guiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable"));
            enabledOrDisabled = createButton(Material.EMERALD_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&a" + "All Enabled"));

        } else {

            List<String> EnabledOrDisabledLore = createLore(
                    guiConfig.getString("Gui.Lore.DisabledAll"),
                    guiConfig.getString("Gui.Lore.RightClickToEnable"));
            enabledOrDisabled = createButton(Material.REDSTONE_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&c" + "All Disabled"));

        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupEnabled ) ) {
            // Lore of the button
            List<String> autoPickupLore = createLore(
                    guiConfig.getString("Gui.Lore.AutoPickupGuiManager"),
                    guiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable"),
                    guiConfig.getString("Gui.Lore.LeftClickToOpen"));
            autoPickup = createButton(Material.EMERALD_BLOCK, 1, autoPickupLore, SpigotPrison.format("&3" + "AutoPickup Enabled"));
        } else {
            // Lore of the button
            List<String> autoPickupLore = createLore(
                    guiConfig.getString("Gui.Lore.AutoPickupGuiManager"),
                    guiConfig.getString("Gui.Lore.RightClickToEnable"),
                    guiConfig.getString("Gui.Lore.LeftClickToOpen"));
            autoPickup = createButton(Material.REDSTONE_BLOCK, 1, autoPickupLore, SpigotPrison.format("&c" + "AutoPickup Disabled"));
        }


        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltEnabled ) ) {
            // Lore of the button
            List<String> autoSmeltLore = createLore(
                    guiConfig.getString("Gui.Lore.AutoSmeltGuiManager"),
                    guiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable"),
                    guiConfig.getString("Gui.Lore.LeftClickToOpen"));
            autoSmelt = createButton(Material.EMERALD_BLOCK, 1, autoSmeltLore, SpigotPrison.format("&3" + "AutoSmelt Enabled"));
        } else {
            // Lore of the button
            List<String> autoSmeltLore = createLore(
                    guiConfig.getString("Gui.Lore.AutoSmeltGuiManager"),
                    guiConfig.getString("Gui.Lore.RightClickToEnable"),
                    guiConfig.getString("Gui.Lore.LeftClickToOpen"));
            autoSmelt = createButton(Material.REDSTONE_BLOCK, 1, autoSmeltLore, SpigotPrison.format("&c" + "AutoSmelt Disabled"));
        }


        if ( afConfig.isFeatureBoolean( AutoFeatures.autoBlockEnabled ) ) {
            // Lore of the button
            List<String> autoBlockLore = createLore(
                    guiConfig.getString("Gui.Lore.AutoBlockGuiManager"),
                    guiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable"),
                    guiConfig.getString("Gui.Lore.LeftClickToOpen"));
            autoBlock = createButton(Material.EMERALD_BLOCK, 1, autoBlockLore, SpigotPrison.format("&3" + "AutoBlock Enabled"));

        } else {
            // Lore of the button
            List<String> autoBlockLore = createLore(
                    guiConfig.getString("Gui.Lore.AutoBlockGuiManager"),
                    guiConfig.getString("Gui.Lore.RightClickToEnable"),
                    guiConfig.getString("Gui.Lore.LeftClickToOpen"));
            autoBlock = createButton(Material.REDSTONE_BLOCK, 1, autoBlockLore, SpigotPrison.format("&c" + "AutoBlock Disabled"));
        }

        //Position of the button
        inv.setItem(2, playSound);

        //Position of the button
        inv.setItem(6, hologram);

        //Position of the button
        inv.setItem(10, autoPickup);

        //Position of the button
        inv.setItem(13, autoSmelt);

        //Position of the button
        inv.setItem(16, autoBlock);

        //Position of the button
        inv.setItem(19, enabledOrDisabled);

        //Position of the button
        inv.setItem(22, enabledOrDisabled);

        //Position of the button
        inv.setItem(25, enabledOrDisabled);
    }


}

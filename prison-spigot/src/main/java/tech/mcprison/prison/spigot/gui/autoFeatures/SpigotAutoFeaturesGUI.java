package tech.mcprison.prison.spigot.gui.autoFeatures;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

public class SpigotAutoFeaturesGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;

    public SpigotAutoFeaturesGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3PrisonManager -> AutoFeatures"));

        // Config
        Configuration configThings = SpigotPrison.getInstance().getAutoFeaturesConfig();

        // Declare buttons
        ItemStack autoPickup;
        ItemStack autoSmelt;
        ItemStack autoBlock;
        ItemStack enabledOrDisabled;

        if (configThings.getBoolean("Options.General.AreEnabledFeatures")){

            List<String> EnabledOrDisabledLore = createLore(
                    "&aAll features enabled",
                    "&cPress Shift + Right click to disable");
            enabledOrDisabled = createButton(Material.EMERALD_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&a" + "All Enabled"));

        } else {

            List<String> EnabledOrDisabledLore = createLore(
                    "&cAll features disabled",
                    "&aRight click to enable");
            enabledOrDisabled = createButton(Material.REDSTONE_BLOCK, 1, EnabledOrDisabledLore, SpigotPrison.format("&c" + "All Disabled"));

        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupEnabled")) {
            // Lore of the button
            List<String> autoPickupLore = createLore(
                    "&8AutoPickup GUI manager.",
                    "&cPress Shift + Right click to disable.",
                    "&8Left Click to open.");
            autoPickup = createButton(Material.EMERALD_BLOCK, 1, autoPickupLore, SpigotPrison.format("&3" + "AutoPickup Enabled"));
        } else {
            // Lore of the button
            List<String> autoPickupLore = createLore(
                    "&8AutoPickup GUI manager.",
                    "&aRight Click to enable.",
                    "&8Left Click to open.");
            autoPickup = createButton(Material.REDSTONE_BLOCK, 1, autoPickupLore, SpigotPrison.format("&3" + "AutoPickup Disabled"));
        }



        if (configThings.getBoolean("Options.AutoSmelt.AutoSmeltEnabled")) {
            // Lore of the button
            List<String> autoSmeltLore = createLore(
                    "&8AutoSmelt GUI manager.",
                    "&cPress Shift + Right click to disable.",
                    "&8Left Click to open.");
            autoSmelt = createButton(Material.EMERALD_BLOCK, 1, autoSmeltLore, SpigotPrison.format("&3" + "AutoSmelt Enabled"));
        } else {
            // Lore of the button
            List<String> autoSmeltLore = createLore(
                    "&8AutoSmelt GUI manager.",
                    "&aRight Click to enable.",
                    "&8Left Click to open.");
            autoSmelt = createButton(Material.REDSTONE_BLOCK, 1, autoSmeltLore, SpigotPrison.format("&3" + "AutoSmelt Disabled"));
        }



        if (configThings.getBoolean("Options.AutoBlock.AutoBlockEnabled")) {
            // Lore of the button
            List<String> autoBlockLore = createLore(
                    "&8AutoBlock GUI manager.",
                    "&cPress Shift + Right click to disable.",
                    "&8Left Click to open.");
            autoBlock = createButton(Material.EMERALD_BLOCK, 1, autoBlockLore, SpigotPrison.format("&3" + "AutoBlock Enabled"));

        } else {
            // Lore of the button
            List<String> autoBlockLore = createLore(
                    "&8AutoBlock GUI manager.",
                    "&aRight Click to enable.",
                    "&8Left Click to open.");
            autoBlock = createButton(Material.REDSTONE_BLOCK, 1, autoBlockLore, SpigotPrison.format("&3" + "AutoBlock Disabled"));
        }

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

        // Open the inventory
        this.p.openInventory(inv);
    }


}

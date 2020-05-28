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

/**
 * @author GABRYCA
 */
public class SpigotAutoPickupGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotAutoPickupGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3AutoFeatures -> AutoPickup"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Config
        Configuration configThings = SpigotPrison.getInstance().getAutoFeaturesConfig();


        List<String> enabledLore = createLore(
                GuiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable")
                );

        List<String> disabledLore = createLore(
                GuiConfig.getString("Gui.Lore.RightClickToEnable")
        );

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupAllBlocks")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "All_Blocks Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "All_Blocks Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupGoldOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Gold_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Gold_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupIronOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Iron_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Iron_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupCoalOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Coal_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Coal_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupDiamondOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Diamond_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Diamond_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupRedstoneOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Redstone_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Redstone_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupEmeraldOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Emerald_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Emerald_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupQuartzOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Quartz_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Quartz_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupLapisOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Lapis_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Lapis_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupSnowBall")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Snow_Ball Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Snow_Ball Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoPickup.AutoPickupGlowstoneDust")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Glowstone_Dust Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Glowstone_Dust Disabled"));
            inv.addItem(Disabled);
        }

        this.p.openInventory(inv);

    }

}

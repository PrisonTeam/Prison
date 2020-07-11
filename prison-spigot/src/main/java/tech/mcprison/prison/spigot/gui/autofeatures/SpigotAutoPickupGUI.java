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
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();

        try {
            buttonsSetup(inv,GuiConfig,afConfig);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return;
        }

        this.p.openInventory(inv);

    }

    private void buttonsSetup(Inventory inv, Configuration guiConfig, AutoFeaturesFileConfig afConfig) {

        List<String> enabledLore = createLore(
                guiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable")
                );

        List<String> disabledLore = createLore(
                guiConfig.getString("Gui.Lore.RightClickToEnable")
        );

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupAllBlocks ) ) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "All_Blocks Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "All_Blocks Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupCobbleStone ) ) {
            ItemStack Enabled = createButton(Material.COBBLESTONE, 1, enabledLore, SpigotPrison.format("&a" + "Cobblestone Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.COBBLESTONE, 1, disabledLore, SpigotPrison.format("&c" + "Cobblestone Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupStone ) ) {
        	ItemStack Enabled = createButton(Material.STONE, 1, enabledLore, SpigotPrison.format("&a" + "Stone Enabled"));
        	inv.addItem(Enabled);
        } else {
        	ItemStack Disabled = createButton(Material.STONE, 1, disabledLore, SpigotPrison.format("&c" + "Stone Disabled"));
        	inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupGoldOre ) ) {
        	ItemStack Enabled = createButton(Material.GOLD_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Gold_Ore Enabled"));
        	inv.addItem(Enabled);
        } else {
        	ItemStack Disabled = createButton(Material.GOLD_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Gold_Ore Disabled"));
        	inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupIronOre ) ) {
            ItemStack Enabled = createButton(Material.IRON_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Iron_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.IRON_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Iron_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupCoalOre ) ) {
            ItemStack Enabled = createButton(Material.COAL_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Coal_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.COAL_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Coal_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupDiamondOre ) ) {
            ItemStack Enabled = createButton(Material.DIAMOND_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Diamond_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.DIAMOND_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Diamond_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupRedStoneOre ) ) {
            ItemStack Enabled = createButton(Material.REDSTONE_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Redstone_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Redstone_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupEmeraldOre ) ) {
            ItemStack Enabled = createButton(Material.EMERALD_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Emerald_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.EMERALD_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Emerald_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupQuartzOre ) ) {
            ItemStack Enabled = createButton(Material.QUARTZ_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Quartz_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.QUARTZ_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Quartz_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupLapisOre ) ) {
            ItemStack Enabled = createButton(Material.LAPIS_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Lapis_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.LAPIS_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Lapis_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupSnowBall ) ) {
            ItemStack Enabled = createButton(Material.SNOW_BALL, 1, enabledLore, SpigotPrison.format("&a" + "Snow_Ball Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.SNOW_BALL, 1, disabledLore, SpigotPrison.format("&c" + "Snow_Ball Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoPickupGlowstoneDust ) ) {
            ItemStack Enabled = createButton(Material.GLOWSTONE_DUST, 1, enabledLore, SpigotPrison.format("&a" + "Glowstone_Dust Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.GLOWSTONE_DUST, 1, disabledLore, SpigotPrison.format("&c" + "Glowstone_Dust Disabled"));
            inv.addItem(Disabled);
        }
    }

}

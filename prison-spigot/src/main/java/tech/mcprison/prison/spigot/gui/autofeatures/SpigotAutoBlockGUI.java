package tech.mcprison.prison.spigot.gui.autofeatures;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
public class SpigotAutoBlockGUI extends SpigotGUIComponents {

    private final Player p;
    private final AutoFeaturesFileConfig afConfig = afConfig();

    public SpigotAutoBlockGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        PrisonGUI gui = new PrisonGUI(p, 36, "&3AutoFeatures -> AutoBlock");

        List<String> enabledLore = createLore(
                messages.getString("Lore.ShiftAndRightClickToDisable")
        );
        List<String> disabledLore = createLore(
                messages.getString("Lore.RightClickToEnable")
        );
        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        gui.addButton(new Button(35, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));

        if (afConfig != null) {

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockAllBlocks)) {
                gui.addButton(new Button(null, XMaterial.LIME_STAINED_GLASS_PANE, enabledLore, SpigotPrison.format("&a" + "All_Blocks Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.RED_STAINED_GLASS_PANE, disabledLore, SpigotPrison.format("&c" + "All_Blocks Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockGoldBlock)) {
                gui.addButton(new Button(null, XMaterial.GOLD_BLOCK, enabledLore, SpigotPrison.format("&a" + "Gold_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GOLD_BLOCK, disabledLore, SpigotPrison.format("&c" + "Gold_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockIronBlock)) {
                gui.addButton(new Button(null, XMaterial.IRON_BLOCK, enabledLore, SpigotPrison.format("&a" + "Iron_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.IRON_BLOCK, disabledLore, SpigotPrison.format("&c" + "Iron_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockCoalBlock)) {
                gui.addButton(new Button(null, XMaterial.COAL_BLOCK, enabledLore, SpigotPrison.format("&a" + "Coal_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.COAL_BLOCK, disabledLore, SpigotPrison.format("&c" + "Coal_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockDiamondBlock)) {
                gui.addButton(new Button(null, XMaterial.DIAMOND_BLOCK, enabledLore, SpigotPrison.format("&a" + "Diamond_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.DIAMOND_BLOCK, disabledLore, SpigotPrison.format("&c" + "Diamond_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockRedstoneBlock)) {
                gui.addButton(new Button(null, XMaterial.REDSTONE_BLOCK, enabledLore, SpigotPrison.format("&a" + "Redstone_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.REDSTONE_BLOCK, disabledLore, SpigotPrison.format("&c" + "Redstone_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockEmeraldBlock)) {
                gui.addButton(new Button(null, XMaterial.EMERALD_BLOCK, enabledLore, SpigotPrison.format("&a" + "Emerald_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.EMERALD_BLOCK, disabledLore, SpigotPrison.format("&c" + "Emerald_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockQuartzBlock)) {
                gui.addButton(new Button(null, XMaterial.QUARTZ_BLOCK, enabledLore, SpigotPrison.format("&a" + "Quartz_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.QUARTZ_BLOCK, disabledLore, SpigotPrison.format("&c" + "Quartz_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockPrismarineBlock)) {
                gui.addButton(new Button(null, XMaterial.PRISMARINE, enabledLore, SpigotPrison.format("&a" + "Prismarine_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.PRISMARINE, disabledLore, SpigotPrison.format("&c" + "Prismarine_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockLapisBlock)) {
                gui.addButton(new Button(null, XMaterial.LAPIS_BLOCK, enabledLore, SpigotPrison.format("&a" + "Lapis_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.LAPIS_BLOCK, disabledLore, SpigotPrison.format("&c" + "Lapis_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockSnowBlock)) {
                gui.addButton(new Button(null, XMaterial.SNOW_BLOCK, enabledLore, SpigotPrison.format("&a" + "Snow_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.SNOW_BLOCK, disabledLore, SpigotPrison.format("&c" + "Snow_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.autoBlockGlowstone)) {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE, enabledLore, SpigotPrison.format("&a" + "Glowstone_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE, disabledLore, SpigotPrison.format("&c" + "Glowstone_Block Disabled")));
            }
        }

        gui.open();
    }
}

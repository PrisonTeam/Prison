package tech.mcprison.prison.spigot.gui.autofeatures;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
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

        // Lores
        ButtonLore enabledLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_disable), null);
        ButtonLore disabledLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_to_enable), null);
        gui.addButton(new Button(35, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_close), null), "&cClose"));

        if (afConfig != null) {

            if (afConfig.isFeatureBoolean(AutoFeatures.blockAllBlocks)) {
                gui.addButton(new Button(null, XMaterial.LIME_STAINED_GLASS_PANE, enabledLore, SpigotPrison.format("&aAll_Blocks Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.RED_STAINED_GLASS_PANE, disabledLore, SpigotPrison.format("&cAll_Blocks Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockGoldBlock)) {
                gui.addButton(new Button(null, XMaterial.GOLD_BLOCK, enabledLore, SpigotPrison.format("&aGold_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GOLD_BLOCK, disabledLore, SpigotPrison.format("&cGold_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockIronBlock)) {
                gui.addButton(new Button(null, XMaterial.IRON_BLOCK, enabledLore, SpigotPrison.format("&aIron_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.IRON_BLOCK, disabledLore, SpigotPrison.format("&cIron_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockCoalBlock)) {
                gui.addButton(new Button(null, XMaterial.COAL_BLOCK, enabledLore, SpigotPrison.format("&aCoal_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.COAL_BLOCK, disabledLore, SpigotPrison.format("&cCoal_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockDiamondBlock)) {
                gui.addButton(new Button(null, XMaterial.DIAMOND_BLOCK, enabledLore, SpigotPrison.format("&aDiamond_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.DIAMOND_BLOCK, disabledLore, SpigotPrison.format("&cDiamond_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockRedstoneBlock)) {
                gui.addButton(new Button(null, XMaterial.REDSTONE_BLOCK, enabledLore, SpigotPrison.format("&aRedstone_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.REDSTONE_BLOCK, disabledLore, SpigotPrison.format("&cRedstone_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockEmeraldBlock)) {
                gui.addButton(new Button(null, XMaterial.EMERALD_BLOCK, enabledLore, SpigotPrison.format("&aEmerald_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.EMERALD_BLOCK, disabledLore, SpigotPrison.format("&cEmerald_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockQuartzBlock)) {
                gui.addButton(new Button(null, XMaterial.QUARTZ_BLOCK, enabledLore, SpigotPrison.format("&aQuartz_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.QUARTZ_BLOCK, disabledLore, SpigotPrison.format("&cQuartz_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockPrismarineBlock)) {
                gui.addButton(new Button(null, XMaterial.PRISMARINE, enabledLore, SpigotPrison.format("&aPrismarine_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.PRISMARINE, disabledLore, SpigotPrison.format("&cPrismarine_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockLapisBlock)) {
                gui.addButton(new Button(null, XMaterial.LAPIS_BLOCK, enabledLore, SpigotPrison.format("&aLapis_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.LAPIS_BLOCK, disabledLore, SpigotPrison.format("&cLapis_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockSnowBlock)) {
                gui.addButton(new Button(null, XMaterial.SNOW_BLOCK, enabledLore, SpigotPrison.format("&aSnow_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.SNOW_BLOCK, disabledLore, SpigotPrison.format("&cSnow_Block Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockGlowstone)) {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE, enabledLore, SpigotPrison.format("&aGlowstone_Block Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE, disabledLore, SpigotPrison.format("&cGlowstone_Block Disabled")));
            }
        }

        gui.open();
    }
}

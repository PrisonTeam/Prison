package tech.mcprison.prison.spigot.gui.autofeatures;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
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
        ButtonLore enabledLore = new ButtonLore(
        		guiRightClickShiftToDisableMsg(), null);
        ButtonLore disabledLore = new ButtonLore(
        		guiRightClickToEnableMsg(), null);
        gui.addButton(new Button(35, XMaterial.RED_STAINED_GLASS_PANE, 
        		new ButtonLore( guiClickToCloseMsg(), null), "&cClose"));

        if (afConfig != null) {

            if (afConfig.isFeatureBoolean(AutoFeatures.blockAllBlocks)) {
                gui.addButton(new Button(null, XMaterial.LIME_STAINED_GLASS_PANE, enabledLore, "&aAll_Blocks Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.RED_STAINED_GLASS_PANE, disabledLore, "&cAll_Blocks Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockGoldBlock)) {
                gui.addButton(new Button(null, XMaterial.GOLD_BLOCK, enabledLore, "&aGold_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.GOLD_BLOCK, disabledLore, "&cGold_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockIronBlock)) {
                gui.addButton(new Button(null, XMaterial.IRON_BLOCK, enabledLore, "&aIron_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.IRON_BLOCK, disabledLore, "&cIron_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockCoalBlock)) {
                gui.addButton(new Button(null, XMaterial.COAL_BLOCK, enabledLore, "&aCoal_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.COAL_BLOCK, disabledLore, "&cCoal_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockDiamondBlock)) {
                gui.addButton(new Button(null, XMaterial.DIAMOND_BLOCK, enabledLore, "&aDiamond_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.DIAMOND_BLOCK, disabledLore, "&cDiamond_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockRedstoneBlock)) {
                gui.addButton(new Button(null, XMaterial.REDSTONE_BLOCK, enabledLore, "&aRedstone_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.REDSTONE_BLOCK, disabledLore, "&cRedstone_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockEmeraldBlock)) {
                gui.addButton(new Button(null, XMaterial.EMERALD_BLOCK, enabledLore, "&aEmerald_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.EMERALD_BLOCK, disabledLore, "&cEmerald_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockQuartzBlock)) {
                gui.addButton(new Button(null, XMaterial.QUARTZ_BLOCK, enabledLore, "&aQuartz_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.QUARTZ_BLOCK, disabledLore, "&cQuartz_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockPrismarineBlock)) {
                gui.addButton(new Button(null, XMaterial.PRISMARINE, enabledLore, "&aPrismarine_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.PRISMARINE, disabledLore, "&cPrismarine_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockLapisBlock)) {
                gui.addButton(new Button(null, XMaterial.LAPIS_BLOCK, enabledLore, "&aLapis_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.LAPIS_BLOCK, disabledLore, "&cLapis_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockSnowBlock)) {
                gui.addButton(new Button(null, XMaterial.SNOW_BLOCK, enabledLore, "&aSnow_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.SNOW_BLOCK, disabledLore, "&cSnow_Block Disabled" ));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.blockGlowstone)) {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE, enabledLore, "&aGlowstone_Block Enabled" ));
            } else {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE, disabledLore, "&cGlowstone_Block Disabled" ));
            }
        }

        gui.open();
    }
}

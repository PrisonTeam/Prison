package tech.mcprison.prison.spigot.gui.autofeatures;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotAutoPickupGUI extends SpigotGUIComponents {

    private final Player p;
    private final AutoFeaturesFileConfig afConfig = afConfig();

    public SpigotAutoPickupGUI(Player p){
        this.p = p;
    }

    public void open() {

        int dimension = 36;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3AutoFeatures -> AutoPickup");

        ButtonLore enabledLore = new ButtonLore( guiRightClickShiftToDisableMsg(), null);
        ButtonLore disabledLore = new ButtonLore( guiRightClickToEnableMsg(), null);
        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);

        gui.addButton(new Button(35, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));

        if (afConfig != null) {
            if (afConfig.isFeatureBoolean(AutoFeatures.pickupAllBlocks)) {
                gui.addButton(new Button(null, XMaterial.LIME_STAINED_GLASS_PANE, enabledLore, SpigotPrison.format("&aAll_Blocks Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.RED_STAINED_GLASS_PANE, disabledLore, SpigotPrison.format("&cAll_Blocks Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupCobbleStone)) {
                gui.addButton(new Button(null, XMaterial.COBBLESTONE, enabledLore, SpigotPrison.format("&aCobblestone Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.COBBLESTONE, disabledLore, SpigotPrison.format("&cCobblestone Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupStone)) {
                gui.addButton(new Button(null, XMaterial.STONE, enabledLore, SpigotPrison.format("&aStone Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.STONE, disabledLore, SpigotPrison.format("&cStone Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupGoldOre)) {
                gui.addButton(new Button(null, XMaterial.GOLD_ORE, enabledLore, SpigotPrison.format("&aGold_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GOLD_ORE, disabledLore, SpigotPrison.format("&cGold_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupIronOre)) {
                gui.addButton(new Button(null, XMaterial.IRON_ORE, enabledLore, SpigotPrison.format("&aIron_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.IRON_ORE, disabledLore, SpigotPrison.format("&cIron_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupCoalOre)) {
                gui.addButton(new Button(null, XMaterial.COAL_ORE, enabledLore, SpigotPrison.format("&aCoal_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.COAL_ORE, disabledLore, SpigotPrison.format("&cCoal_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupDiamondOre)) {
                gui.addButton(new Button(null, XMaterial.DIAMOND_ORE, enabledLore, SpigotPrison.format("&aDiamond_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.DIAMOND_ORE, disabledLore, SpigotPrison.format("&cDiamond_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupRedStoneOre)) {
                gui.addButton(new Button(null, XMaterial.REDSTONE_ORE, enabledLore, SpigotPrison.format("&aRedstone_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.REDSTONE_ORE, disabledLore, SpigotPrison.format("&cRedstone_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupEmeraldOre)) {
                gui.addButton(new Button(null, XMaterial.EMERALD_ORE, enabledLore, SpigotPrison.format("&aEmerald_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.EMERALD_ORE, disabledLore, SpigotPrison.format("&cEmerald_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupQuartzOre)) {
                gui.addButton(new Button(null, XMaterial.NETHER_QUARTZ_ORE, 1, enabledLore, SpigotPrison.format("&aQuartz_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.NETHER_QUARTZ_ORE, 1, disabledLore, SpigotPrison.format("&cQuartz_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupLapisOre)) {
                gui.addButton(new Button(null, XMaterial.LAPIS_ORE, enabledLore, SpigotPrison.format("&aLapis_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.LAPIS_ORE, disabledLore, SpigotPrison.format("&cLapis_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupSnowBall)) {
                gui.addButton(new Button(null, XMaterial.SNOWBALL, 1, enabledLore, SpigotPrison.format("&aSnow_Ball Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.SNOWBALL, 1, disabledLore, SpigotPrison.format("&cSnow_Ball Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.pickupGlowstoneDust)) {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE_DUST, enabledLore, SpigotPrison.format("&aGlowstone_Dust Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GLOWSTONE_DUST, disabledLore, SpigotPrison.format("&cGlowstone_Dust Disabled")));
            }
        } else {
            Output.get().sendError(new SpigotPlayer(p), "An error occurred, the AutoFeatures Config is broken or missing!");
        }

        gui.open();
    }
}

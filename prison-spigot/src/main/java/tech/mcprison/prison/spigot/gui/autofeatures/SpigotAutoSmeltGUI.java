package tech.mcprison.prison.spigot.gui.autofeatures;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotAutoSmeltGUI extends SpigotGUIComponents {

    private final Player p;
    private final AutoFeaturesFileConfig afConfig = afConfig();

    public SpigotAutoSmeltGUI(Player p){
        this.p = p;
    }

    public void open() {

        int dimension = 36;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3AutoFeatures -> AutoSmelt");

        ButtonLore enabledLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_disable), null);
        ButtonLore disabledLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_to_enable), null);
        ButtonLore closeGUILore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_close), null);

        gui.addButton(new Button(35, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));

        if(afConfig != null) {
            if (afConfig.isFeatureBoolean(AutoFeatures.smeltAllBlocks)) {
                gui.addButton(new Button(null, XMaterial.LIME_STAINED_GLASS_PANE, enabledLore, SpigotPrison.format("&aAll_Ores Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.RED_STAINED_GLASS_PANE, disabledLore, SpigotPrison.format("&cAll_Ores Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.smeltGoldOre)) {
                gui.addButton(new Button(null, XMaterial.GOLD_ORE, enabledLore, SpigotPrison.format("&aGold_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GOLD_ORE, disabledLore, SpigotPrison.format("&cGold_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.smeltIronOre)) {
                gui.addButton(new Button(null, XMaterial.IRON_ORE, enabledLore, SpigotPrison.format("&aIron_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.IRON_ORE, disabledLore, SpigotPrison.format("&cIron_Ore Disabled")));
            }
        } else {
            Output.get().sendError(new SpigotPlayer(p), "An error occurred, the AutoFeatures Config is broken or missing!");
        }

        gui.open();
    }
}

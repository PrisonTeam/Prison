package tech.mcprison.prison.spigot.gui.autofeatures;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
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

        ButtonLore enabledLore = new ButtonLore(messages.getString("Lore.ShiftAndRightClickToDisable"), null);
        ButtonLore disabledLore = new ButtonLore(messages.getString("Lore.RightClickToEnable"), null);
        ButtonLore closeGUILore = new ButtonLore(messages.getString("Lore.ClickToClose"), null);

        gui.addButton(new Button(35, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));

        if(afConfig != null) {
            if (afConfig.isFeatureBoolean(AutoFeatures.smeltAllBlocks)) {
                gui.addButton(new Button(null, XMaterial.LIME_STAINED_GLASS_PANE, enabledLore, SpigotPrison.format("&a" + "All_Ores Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.RED_STAINED_GLASS_PANE, disabledLore, SpigotPrison.format("&c" + "All_Ores Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.smeltGoldOre)) {
                gui.addButton(new Button(null, XMaterial.GOLD_ORE, enabledLore, SpigotPrison.format("&a" + "Gold_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.GOLD_ORE, disabledLore, SpigotPrison.format("&c" + "Gold_Ore Disabled")));
            }

            if (afConfig.isFeatureBoolean(AutoFeatures.smeltIronOre)) {
                gui.addButton(new Button(null, XMaterial.IRON_ORE, enabledLore, SpigotPrison.format("&a" + "Iron_Ore Enabled")));
            } else {
                gui.addButton(new Button(null, XMaterial.IRON_ORE, disabledLore, SpigotPrison.format("&c" + "Iron_Ore Disabled")));
            }
        } else {
            Output.get().sendError(new SpigotPlayer(p), "An error occurred, the AutoFeatures Config is broken or missing!");
        }

        gui.open();
    }
}

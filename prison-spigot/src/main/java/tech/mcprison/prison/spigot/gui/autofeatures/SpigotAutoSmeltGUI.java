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
public class SpigotAutoSmeltGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotAutoSmeltGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3AutoFeatures -> AutoSmelt"));

        // Config
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();

        if (guiBuilder(inv, afConfig)) return;

        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv, AutoFeaturesFileConfig afConfig) {
        try {
            buttonsSetup(inv, afConfig);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, AutoFeaturesFileConfig afConfig) {

        Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

        List<String> enabledLore = createLore(
                messages.getString("Gui.Lore.ShiftAndRightClickToDisable")
        );

        List<String> disabledLore = createLore(
                messages.getString("Gui.Lore.RightClickToEnable")
        );

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltAllBlocks ) ) {
            ItemStack Enabled = createButton(Material.LIME_STAINED_GLASS_PANE, 1, enabledLore, SpigotPrison.format("&a" + "All_Ores Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.RED_STAINED_GLASS_PANE, 1, disabledLore, SpigotPrison.format("&c" + "All_Ores Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltGoldOre ) ) {
            ItemStack Enabled = createButton(Material.GOLD_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Gold_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.GOLD_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Gold_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltIronOre ) ) {
            ItemStack Enabled = createButton(Material.IRON_ORE, 1, enabledLore, SpigotPrison.format("&a" + "Iron_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.IRON_ORE, 1, disabledLore, SpigotPrison.format("&c" + "Iron_Ore Disabled"));
            inv.addItem(Disabled);
        }
    }

}

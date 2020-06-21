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
public class SpigotAutoSmeltGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotAutoSmeltGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3AutoFeatures -> AutoSmelt"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Config
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();


        List<String> enabledLore = createLore(
                GuiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable")
        );

        List<String> disabledLore = createLore(
                "&8Right Click to enable"
        );

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltAllBlocks ) ) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "All_Ores Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "All_Ores Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltGoldOre ) ) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Gold_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Gold_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltIronOre ) ) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Iron_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Iron_Ore Disabled"));
            inv.addItem(Disabled);
        }

        this.p.openInventory(inv);

    }

}

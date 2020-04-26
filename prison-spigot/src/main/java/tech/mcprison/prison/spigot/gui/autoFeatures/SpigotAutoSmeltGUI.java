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

public class SpigotAutoSmeltGUI extends SpigotGUIComponents {

    private int dimension = 27;
    private Player p;

    public SpigotAutoSmeltGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3AutoFeatures -> AutoSmelt"));

        // Config
        Configuration configThings = SpigotPrison.getAutoFeaturesConfig();


        List<String> enabledLore = createLore(
                "&cPress Shift + Right click to disable."
        );

        List<String> disabledLore = createLore(
                "&8Right Click to enable"
        );

        if (configThings.getBoolean("Options.AutoSmelt.AutoSmeltAllBlocks")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "All_Ores Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "All_Ores Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoSmelt.AutoSmeltGoldOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Gold_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Gold_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoSmelt.AutoSmeltIronOre")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Iron_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Iron_Ore Disabled"));
            inv.addItem(Disabled);
        }

        this.p.openInventory(inv);

    }

}

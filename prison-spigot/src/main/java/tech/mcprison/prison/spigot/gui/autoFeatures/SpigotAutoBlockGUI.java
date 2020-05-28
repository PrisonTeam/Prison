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

/**
 * @author GABRYCA
 */
public class SpigotAutoBlockGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotAutoBlockGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3AutoFeatures -> AutoBlock"));

        // Config
        Configuration configThings = SpigotPrison.getInstance().getAutoFeaturesConfig();

        List<String> enabledLore = createLore(
                GuiConfig.getString("Gui.Lore.ShiftAndRightClickToDisable")
        );

        List<String> disabledLore = createLore(
                GuiConfig.getString("Gui.Lore.RightClickToEnable")
        );

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockAllBlocks")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "All_Blocks Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "All_Blocks Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockGoldBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Gold_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Gold_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockIronBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Iron_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Iron_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockCoalBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Coal_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Coal_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockDiamondBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Diamond_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Diamond_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockRedstoneBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Redstone_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Redstone_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockEmeraldBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Emerald_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Emerald_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockQuartzBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Quartz_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Quartz_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockPrismarineBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Prismarine_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Prismarine_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockLapisBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Lapis_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Lapis_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockSnowBlock")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Snow_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Snow_Block Disabled"));
            inv.addItem(Disabled);
        }

        if (configThings.getBoolean("Options.AutoBlock.AutoBlockGlowstone")) {
            ItemStack Enabled = createButton(Material.EMERALD_BLOCK, 1, enabledLore, SpigotPrison.format("&a" + "Glowstone_Block Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(Material.REDSTONE_BLOCK, 1, disabledLore, SpigotPrison.format("&c" + "Glowstone_Block Disabled"));
            inv.addItem(Disabled);
        }

        this.p.openInventory(inv);

    }

}

package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMineNotificationsGUI extends SpigotGUIComponents {

    private final Player p;
    private final String minename;

    public SpigotMineNotificationsGUI(Player p, String minename){
        this.p = p;
        this.minename = minename;
    }

    public void open() {

        // Create a new inventory
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineInfo -> MineNotifications"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Init variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(minename);
        String enabledOrDisabled = m.getNotificationMode().name();

        // Create a new lore
        List<String> modeWithinLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToChoose"),
                GuiConfig.getString("Gui.Lore.ActivateWithinMode"));

        // Create a new lore
        List<String> modeRadiusLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToChoose"),
                GuiConfig.getString("Gui.Lore.ActivateRadiusMode"));

        // Create a new lore
        List<String> disabledModeLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToChoose"),
                GuiConfig.getString("Gui.Lore.DisableNotifications"));

        // Add the selected lore to the mode used
        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            // Add the selected lore
            disabledModeLore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Selected")));

        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            // Add the selected lore
            modeWithinLore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Selected")));

        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            // Add the selected lore
            modeRadiusLore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Selected")));

        }

        // Create a button
        ItemStack modeWithin = createButton(Material.IRON_DOOR, 1, modeWithinLore, SpigotPrison.format("&3Within_Mode: " + minename));

        // Create a button
        ItemStack radiusMode = createButton(Material.FENCE, 1, modeRadiusLore, SpigotPrison.format("&3Radius_Mode: " + minename));

        // Create a button
        ItemStack disabledMode = createButton(Material.REDSTONE_BLOCK, 1, disabledModeLore, SpigotPrison.format("&3Disabled_Mode: " + minename));

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            // Add a button to the inventory
            inv.setItem( 11, modeWithin);

            // Add a button to the inventory
            inv.setItem(13, radiusMode);

            // Add a button to the inventory
            disabledMode.addUnsafeEnchantment(Enchantment.LUCK, 1);
            inv.setItem(15, disabledMode);

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            // Add a button to the inventory
            modeWithin.addUnsafeEnchantment(Enchantment.LUCK, 1);
            inv.setItem(11, modeWithin);

            // Add a button to the inventory
            inv.setItem(13, radiusMode);

            // Add a button to the inventory
            inv.setItem(15, disabledMode);

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            // Add a button to the inventory
            inv.setItem( 11, modeWithin);

            // Add a button to the inventory
            radiusMode.addUnsafeEnchantment(Enchantment.LUCK, 1);
            inv.setItem( 13, radiusMode);

            // Add a button to the inventory
            inv.setItem(15, disabledMode);

        }

        // Opens the inventory
        this.p.openInventory(inv);

    }

}

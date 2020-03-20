package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.util.List;


public class SpigotMineNotificationsGUI extends SpigotGUIComponents{

    private int dimension = 27;
    private Player p;
    private String minename;

    public SpigotMineNotificationsGUI(Player p, String minename){
        this.p = p;
        this.minename = minename;
    }

    public void open() {

        // Create a new inventory
        Inventory inv = Bukkit.createInventory(null, dimension, "3MineInfo -> MineNotifications");

        // Init variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMineManager().getMine(minename).get();
        String enabledOrDisabled = m.getNotificationMode().name();

        // Create a new lore
        List<String> modeWithinLore = createLore(
                "&8Click to choose.",
                "&3Activate Within mode.");

        // Create a button
        ItemStack modeWithin = createButton(Material.CHEST, 1, modeWithinLore, SpigotPrison.format("&3Within_Mode: " + minename));

        // Create a new lore
        List<String> modeRadiusLore = createLore(
                "&8Click to choose.",
                "&3Activate Radius mode.");

        // Create a button
        ItemStack radiusMode = createButton(Material.FENCE, 1, modeRadiusLore, SpigotPrison.format("&3Radius_Mode: " + minename));

        // Create a new lore
        List<String> disabledModeLore = createLore(
                "&8Click to choose.",
                "&3Disable notifications.");

        // Create a button
        ItemStack disabledMode = createButton(Material.REDSTONE_BLOCK, 1, disabledModeLore, SpigotPrison.format("&3Disabled_Mode: " + minename));

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            // Add a button to the inventory
            inv.setItem( 11, modeWithin);

            // Add a button to the inventory
            inv.setItem(15, radiusMode);

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            // Add a button to the inventory
            inv.setItem(11, disabledMode);

            // Add a button to the inventory
            inv.setItem(15, radiusMode);

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            // Add a button to the inventory
            inv.setItem(11, disabledMode);

            // Add a button to the inventory
            inv.setItem( 15, modeWithin);

        }

        // Opens the inventory
        this.p.openInventory(inv);

    }

}

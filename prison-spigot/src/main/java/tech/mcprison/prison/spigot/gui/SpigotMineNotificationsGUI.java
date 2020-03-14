package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;

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

        Inventory inv = Bukkit.createInventory(null, dimension, "§3MineInfo -> MineNotifications");

        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMineManager().getMine(minename).get();
        String enabledOrDisabled = m.getNotificationMode().name();

        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            List<String> modeWithinLore = createLore(
                    "§8Click to choose",
                    "§3Activate Within mode");

            ItemStack modeWithin = createButton(Material.CHEST, 1, modeWithinLore, "§3Within_Mode: " + minename);

            inv.setItem( 11, modeWithin);



            List<String> modeRadiusLore = createLore(
                    "§8Click to choose",
                    "§3Activate Radius mode");

            ItemStack radiusMode = createButton(Material.FENCE, 1, modeRadiusLore, "§3Radius_Mode: " + minename);

            inv.setItem(15, radiusMode);

        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            List<String> disabledModeLore = createLore(
                    "§8Click to choose",
                    "§3Disable notifications");

            ItemStack disabledMode = createButton(Material.REDSTONE_BLOCK, 1, disabledModeLore, "§3Disabled_Mode" + minename);

            inv.setItem(11, disabledMode);



            List<String> modeRadiusLore = createLore(
                    "§8Click to choose",
                    "§3Activate Radius mode");

            ItemStack radiusMode = createButton(Material.FENCE, 1, modeRadiusLore, "§3Radius_Mode: " + minename);

            inv.setItem(15, radiusMode);

        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            List<String> disabledModeLore = createLore(
                    "§8Click to choose",
                    "§3Disable notifications");

            ItemStack disabledMode = createButton(Material.REDSTONE_BLOCK, 1, disabledModeLore, "§3Disabled_Mode" + minename);

            inv.setItem(11, disabledMode);



            List<String> modeWithinLore = createLore(
                    "§8Click to choose",
                    "§3Activate Within mode");

            ItemStack modeWithin = createButton(Material.CHEST, 1, modeWithinLore, "§3Within_Mode: " + minename);

            inv.setItem( 11, modeWithin);

        } else {



        }

        // Opens the inventory
        this.p.openInventory(inv);

    }

}

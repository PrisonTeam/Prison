package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMineInfoGUI extends SpigotGUIComponents {

    private int dimension = 45;
    private Player p;
	private Mine mine;
    private String minename;

    public SpigotMineInfoGUI(Player p, Mine mine, String minename){
        this.p = p;
        this.mine = mine;
        this.minename = minename;
    }

    public void open(){

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> MineInfo"));

        // The Reset Mine button and lore
        List<String> resetminelore = createLore(
                "&8Click to use.",
                "&8Resets the mine.");

        // Set the Mine spawn at your location
        List<String> MineSpawnlore = createLore(
                "&8Click to use.",
                "&8Set the mine spawn point at your location."
        );

        // Lore and button
        List<String> MinesNotificationsLore = createLore(
                "&8Click to open.",
                "&8Change Mines notifications."
        );

        // Lore and button
        List<String> MinesTpLore = createLore(
                "&8Click to teleport.",
                "&8Tp to the mine."
        );

        // Blocks of the mine button and lore
        List<String> blocksoftheminelore = createLore(
                "&8Click to open.",
                "&8Manage the blocks of the Mine.");

        // Blocks of the mine button and lore
        List<String> mineResetTimeLore = createLore(
                "&8Click to manage.",
                "&8Manage the reset time of the Mine.",
                "&3Reset time: &7" + mine.getResetTime());

        // Create the button, set up the material, amount, lore and name
        ItemStack resetmine = createButton(Material.EMERALD_BLOCK, 1, resetminelore, SpigotPrison.format("&3" + "Reset_Mine: " + minename));

        // Create the button
        ItemStack MineSpawn = createButton(Material.COMPASS, 1, MineSpawnlore, SpigotPrison.format("&3" + "Mine_Spawn: " + minename));

        // Create the button
        ItemStack MinesNotifications = createButton(Material.SIGN, 1, MinesNotificationsLore, SpigotPrison.format("&3" + "Mine_notifications: " + minename));

        // Create the button
        ItemStack MinesTP = createButton(Material.BED, 1, MinesTpLore, SpigotPrison.format("&3" + "TP_to_the_Mine: " + minename));

        // Create the button, set up the material, amount, lore and name
        ItemStack blocksofthemine = createButton(Material.COAL_ORE, 1, blocksoftheminelore, SpigotPrison.format("&3" + "Blocks_of_the_Mine: " + minename));

        // Create the button, set up the material, amount, lore and name
        ItemStack mineResetTime = createButton(Material.WATCH, 1, mineResetTimeLore, SpigotPrison.format("&3" + "Reset_Time: " + minename));

        // Position of the button
        inv.setItem(10, resetmine);

        // Position of the button
        inv.setItem(13, MineSpawn);

        // Position of the button
        inv.setItem(16, MinesNotifications);

        // Position of the button
        inv.setItem(28, MinesTP);

        // Position of the button
        inv.setItem(31, blocksofthemine);

        // Position of the button
        inv.setItem(34, mineResetTime);

        // Opens the inventory
        this.p.openInventory(inv);

    }

}

package tech.mcprison.prison.spigot.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.mines.data.Mine;

public class SpigotMineInfoGUI extends SpigotGUIComponents{

    private int dimension = 27;
    private Player p;
    @SuppressWarnings( "unused" )
	private Mine m;
    private String minename;

    public SpigotMineInfoGUI(Player p, Mine m, String minename){
        this.p = p;
        this.m = m;
        this.minename = minename;
    }

    public void open(){

        Inventory inv = Bukkit.createInventory(null, dimension, "§3Mines -> MineInfo");

        // The Reset Mine button and lore
        List<String> resetminelore = createLore(
                "§8Click to use",
                "§8Resets the mine");

        // Create the button, set up the material, amount, lore and name
        ItemStack resetmine = createButton(Material.EMERALD_BLOCK, 1, resetminelore, "§a" + "Reset_Mine: " + minename);

        // Position of the button
        inv.setItem(dimension - 17, resetmine);



        // Set the Mine spawn at your location
        List<String> MineSpawnlore = createLore(
                "§8Click to use",
                "§8Set the mine spawn point at your location"
        );

        // Create the button
        ItemStack MineSpawn = createButton(Material.COMPASS, 1, MineSpawnlore, "§7" + "MineSpawn: " + minename);

        // Position of the button
        inv.setItem(dimension - 15, MineSpawn);



        // Lore and button
        List<String> MinesTpLore = createLore(
                "§8Click to tp",
                "§8Tp to the mine"
        );

        // Create the button
        ItemStack MinesTP = createButton(Material.BED, 1, MinesTpLore, "§4" + "TP_to_the_Mine: " + minename);

        inv.setItem(dimension - 13, MinesTP);



        // Blocks of the mine button and lore
        List<String> blocksoftheminelore = createLore(
                "§8Click to open",
                "§8Manage the blocks of the Mine");

        // Create the button, set up the material, amount, lore and name
        ItemStack blocksofthemine = createButton(Material.COMPASS, 1, blocksoftheminelore, "§3" + "Blocks_of_the_Mine: " + minename);

        // Position of the button
        inv.setItem(dimension - 11, blocksofthemine);

        // Opens the inventory
        this.p.openInventory(inv);

    }

}

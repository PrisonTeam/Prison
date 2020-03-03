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

        // Blocks of the mine
        List<String> blocksoftheminelore = createLore(
        		"§8Click to open",
        		"§d§6Coming Soon");

        // Create the button, set up the material, amount, lore and name
        ItemStack blocksofthemine = createButton(Material.COMPASS, 1, blocksoftheminelore, "§3" + "Blocks_of_the_Mine: " + minename);

        // Position of the button
        inv.setItem(dimension - 12, blocksofthemine);



        // The Reset Mine button and lore
        List<String> resetminelore = createLore(
                "§8Click to use",
                "§8Resets the mine");

        // Create the button, set up the material, amount, lore and name
        ItemStack resetmine = createButton(Material.EMERALD_BLOCK, 1, resetminelore, "§a" + "Reset_Mine: " + minename);

        // Position of the button
        inv.setItem(dimension - 16, resetmine);

        this.p.openInventory(inv);

    }

}

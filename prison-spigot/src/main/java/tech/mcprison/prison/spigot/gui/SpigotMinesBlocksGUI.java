package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.managers.MineManager;

import java.util.List;

public class SpigotMinesBlocksGUI extends SpigotGUIComponents{

    private int dimension = 27;
    private Player p;
    @SuppressWarnings( "unused" )
    private String minename;

    public SpigotMinesBlocksGUI(Player p, String minename){
        this.p = p;
        this.minename = minename;
    }

    public void open(){

        // Get the variables
        PrisonMines pMines = PrisonMines.getInstance();
        MineManager mMan = pMines.getMineManager();
        Mine m = mMan.getMine(minename).get();

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil(m.getBlocks().size() / 9D)*9;

        // Create the inventory
        Inventory inv = Bukkit.createInventory(null, dimension, "§3MineInfo -> Blocks");

        // For every block makes a button
        for (Block block : m.getBlocks()) {

            List<String> blockslore = createLore(
                    "§cPress Shift + Right click to remove.",
                    "",
                    "§8§l|§3Info§8|");

            // Add a lore
            blockslore.add("§3Chance: " + block.getChance() + "%");

            // Get the block material as a string
            String blockmaterial = block.getType().name();

            // Add a lore
            blockslore.add("§3BlockType: " + blockmaterial);

            // Display title of the item
            String blockmaterialdisplay = blockmaterial;

            // Check if a block's air and changed the item of it to BARRIER
            if (blockmaterial.equalsIgnoreCase("air")){
                blockmaterial = "BARRIER";
            }

            // Make the item
            ItemStack block1 = createButton(Material.valueOf(blockmaterial), 1, blockslore, "§3" + blockmaterialdisplay + ": " + minename);

            // Add the item to the inventory
            inv.addItem(block1);

        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

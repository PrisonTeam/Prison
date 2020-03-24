package tech.mcprison.prison.spigot.gui.Mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.MaterialType;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMinesBlocksGUI extends SpigotGUIComponents {

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

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format("&cSorry, but there're too many Blocks and the max's 54 for the GUI"));
            p.closeInventory();
            return;
        }

        // Create the inventory
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineInfo -> Blocks"));

        // For every block makes a button
        for (Block block : m.getBlocks()) {

            // Get the block material as a string
            String blockmaterial = block.getType().name();

            // Display title of the item
            String blockmaterialdisplay = blockmaterial;

            // Check if a block's air and changed the item of it to BARRIER
            if (blockmaterial.equalsIgnoreCase("air")){
                blockmaterial = "BARRIER";
                blockmaterialdisplay = blockmaterial;
            }

            // Create the lore
            List<String> blockslore = createLore(
                    "&cPress Shift + Right click to remove.",
                    "",
                    "&8&l|&3Info&8|");


            boolean isEnum = true;
            try {
                Material.valueOf(blockmaterial);
            } catch (Exception e) {
                isEnum = false;
            }

            if (!(isEnum)) {
                blockmaterial = "BARRIER";
            }

            // Add a lore
            blockslore.add(SpigotPrison.format("&3Chance: " + block.getChance() + "%"));

            // Add a lore
            blockslore.add(SpigotPrison.format("&3BlockType: " + blockmaterial));

            // Make the item
            ItemStack block1 = createButton(Material.valueOf(blockmaterial), 1, blockslore, SpigotPrison.format("&3" + blockmaterialdisplay + ": " + minename));

            // Add the item to the inventory
            inv.addItem(block1);

        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

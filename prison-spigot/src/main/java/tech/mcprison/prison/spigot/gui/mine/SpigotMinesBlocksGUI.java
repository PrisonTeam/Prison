package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMinesBlocksGUI extends SpigotGUIComponents {

    private final Player p;
    private final String minename;

    public SpigotMinesBlocksGUI(Player p, String minename){
        this.p = p;
        this.minename = minename;
    }

    public void open(){

        // Get the variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(minename);

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(m.getBlocks().size() / 9D) * 9;

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // If the inventory is empty
        if (dimension == 0){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.EmptyGui")));
            if (p.getOpenInventory() != null){
                p.closeInventory();
                return;
            }
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.TooManyBlocks")));
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
                    GuiConfig.getString("Gui.Lore.ShiftAndRightClickToDelete"),
                    "",
                    GuiConfig.getString("Gui.Lore.Info"));


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
            blockslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.Chance") + block.getChance() + "%"));

            // Add a lore
            blockslore.add(SpigotPrison.format(GuiConfig.getString("Gui.Lore.BlockType") + blockmaterial));

            // Make the item
            ItemStack block1 = createButton(Material.valueOf(blockmaterial), 1, blockslore, SpigotPrison.format("&3" + blockmaterialdisplay + ": " + minename));

            // Add the item to the inventory
            inv.addItem(block1);

        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

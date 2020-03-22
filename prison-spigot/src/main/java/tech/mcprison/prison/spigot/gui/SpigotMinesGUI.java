package tech.mcprison.prison.spigot.gui;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMinesGUI extends SpigotGUIComponents{

    private int dimension = 27;
    private Player p;

    public SpigotMinesGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Init the ItemStack
        ItemStack itemines;

        // Get the mines
        PrisonMines pMines = PrisonMines.getInstance();

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil( pMines.getMines().size() / 9D)*9;

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format("&cSorry, but there're too many mines and the max's 54 for the GUI"));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MinesManager -> Mines"));

        // Make the buttons for every Mine with info
        for (Mine m : pMines.getMines()) {

            // Init the lore array with default values for ladders
            List<String> mineslore = createLore(
                    "&8Left Click to open.",
                    "&cPress Shift + Right click to delete.",
                    "",
                    "&8&l|&3Info&8|");

            // Add a lore
            mineslore.add(SpigotPrison.format("&3World: &7" +  m.getWorldName()));

            // Init a variable and add it to the lore
            String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "&cnot set";
            mineslore.add(SpigotPrison.format("&3Spawnpoint: &7" + spawnPoint));

            // Add a lore
            mineslore.add(SpigotPrison.format("&3Reset time in seconds: &7" + m.getResetTime()));

            // Add a lore
            mineslore.add(SpigotPrison.format("&3Size of Mine: &7" + m.getBounds().getDimensions()));

            // Add a lore
            mineslore.add(SpigotPrison.format("&3Volume in Blocks: &7" + m.getBounds().getTotalBlockCount()));

            // Add a lore
            mineslore.add(SpigotPrison.format("&3Blocks:"));

            // Init some variables and do the actions
            DecimalFormat dFmt = new DecimalFormat("##0.00");
            double totalChance = 0.0d;
            for (Block block : m.getBlocks()) {
                double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
                totalChance += chance;

                String blockName =
                        StringUtils.capitalize(block.getType().name().replaceAll("_", " ").toLowerCase());
                mineslore.add(SpigotPrison.format("&7" + chance + "% - " + block.getType().name() + "   (" + blockName + ")"));
            }

            if (totalChance < 100.0d) {
                mineslore.add(SpigotPrison.format("&e " + dFmt.format(100.0d - totalChance) + "%  - Air"));
            }

            // Create the button
            itemines = createButton(Material.COAL_ORE, 1, mineslore, SpigotPrison.format("&3" + m.getName()));

            // Add the button to the inventory
            inv.addItem(itemines);
        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SpigotMinesGUI extends SpigotGUIComponents{

    private int dimension = 27;
    private Player p;

    public SpigotMinesGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Init the ItemStack
        ItemStack itemines;

        // Init the lore array with default values for ladders
        List<String> mineslore = new ArrayList<>();
        mineslore.add("§8Left Click to open");
        mineslore.add("§cPress Shift + Right click to delete");
        mineslore.add("");
        mineslore.add("§8§l|§3Info§8|");

        // Get the mines
        PrisonMines pMines = PrisonMines.getInstance();

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil( pMines.getMines().size() / 9D)*9;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, "§3MinesManager -> Mines");

        // Make the buttons for every Mine with info
        for (Mine m : pMines.getMines()) {

            mineslore.add("§6World: §7" +  m.getWorldName());

            String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "§cnot set";
            mineslore.add("§6Spawnpoint: §7" + spawnPoint);

            mineslore.add("§6Reset time in seconds: §7" + m.getResetTime());

            mineslore.add("§6Size of Mine: §7" + ChatColor.translateAlternateColorCodes('&' , m.getBounds().getDimensions()));

            mineslore.add("§6Volume in Blocks: §7" + m.getBounds().getTotalBlockCount());

            mineslore.add("§3Blocks:");

            DecimalFormat dFmt = new DecimalFormat("##0.00");
            double totalChance = 0.0d;
            for (Block block : m.getBlocks()) {
                double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
                totalChance += chance;

                String blockName =
                        StringUtils.capitalize(block.getType().name().replaceAll("_", " ").toLowerCase());
                mineslore.add("§7% - " + block.getType().name() + "   (" + blockName + ")");
            }

            if (totalChance < 100.0d) {
                mineslore.add("§e " + dFmt.format(100.0d - totalChance) + "%  - Air");
            }

            // Create the button
            itemines = createButton(Material.COAL_ORE, 1, mineslore, "§6" + m.getName());

            // Add the button to the inventory
            inv.addItem(itemines);
        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

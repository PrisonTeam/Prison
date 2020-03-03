package tech.mcprison.prison.spigot.gui;

import jdk.internal.util.xml.impl.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.mines.PrisonMines;
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

        List<String> blockslore = createLore(
                "§cPress Shift + Right click to remove",
                "",
                "§8§l|§3Info§8|"
        );

        // Get the dimensions and if needed increases them
        dimension = (int) Math.ceil(m.getBlocks().size() / 9D)*9;

        // Create the inventory
        Inventory inv = Bukkit.createInventory(null, dimension, "§3MineInfo -> Blocks");

        // For every block makes a button
        for (int i = 0 ; m.getBlocks().size() >= i ; i++){

            blockslore.add("§3Chance: " + m.getBlocks().get(i).getChance());

            String blockmaterial = m.getBlocks().get(i).getType().toString();

            blockslore.add("§3BlockType: " + blockmaterial);

            if (Material.valueOf(blockmaterial) == Material.AIR){
                blockmaterial = "BARRIER";
            }

            String blockmaterialdisplay = blockmaterial;

            if (blockmaterialdisplay.equals("BARRIER")){
                blockmaterialdisplay = "AIR";
            }

            ItemStack block = createButton(Material.valueOf(blockmaterial), 1, blockslore, "§3" + blockmaterialdisplay + ": " + minename);

            inv.addItem(block);

        }

        // Open the inventory
        this.p.openInventory(inv);

    }

}

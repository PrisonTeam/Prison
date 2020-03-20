package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SpigotMinesConfirmGUI extends SpigotGUIComponents{

    private int dimension = 9;
    private Player p;
    private String minename;

    public SpigotMinesConfirmGUI(Player p, String minename) {
        this.p = p;
        this.minename = minename;
    }

    public void open(){

        Inventory inv = Bukkit.createInventory(null, dimension, "§3Mines -> Delete");

        // Blocks of the mine
        List<String> confirmlore = createLore(
                "§8Click to §aconfirm.");

        // Create the button, set up the material, amount, lore and name
        ItemStack confirm = createButton(Material.EMERALD_BLOCK, 1, confirmlore, "§3" + "Confirm: " + minename);

        // Position of the button
        inv.setItem(dimension - 7, confirm);

        // Blocks of the mine
        List<String> cancelore = createLore(
                "§8Click to §ccancel.");

        // Create the button, set up the material, amount, lore and name
        ItemStack cancel = createButton(Material.REDSTONE_BLOCK, 1, cancelore, "§3" + "Cancel: " + minename);

        // Position of the button
        inv.setItem(dimension - 3, cancel);

        this.p.openInventory(inv);

    }

}

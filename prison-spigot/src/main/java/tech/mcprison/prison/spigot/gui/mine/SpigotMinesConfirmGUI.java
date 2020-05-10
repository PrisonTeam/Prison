package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMinesConfirmGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;

    public SpigotMinesConfirmGUI(Player p, String minename) {
        this.p = p;
        this.mineName = minename;
    }

    public void open(){

        // Create the inventory
        int dimension = 9;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> Delete"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Blocks of the mine
        List<String> confirmlore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToConfirm"));

        // Blocks of the mine
        List<String> cancelore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToCancel"));

        // Create the button, set up the material, amount, lore and name
        ItemStack confirm = createButton(Material.EMERALD_BLOCK, 1, confirmlore, SpigotPrison.format("&3" + "Confirm: " + mineName));

        // Create the button, set up the material, amount, lore and name
        ItemStack cancel = createButton(Material.REDSTONE_BLOCK, 1, cancelore, SpigotPrison.format("&3" + "Cancel: " + mineName));

        // Position of the button
        inv.setItem(2, confirm);

        // Position of the button
        inv.setItem(6, cancel);

        // Open the inventory
        this.p.openInventory(inv);

    }

}

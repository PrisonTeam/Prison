package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMinesConfirmGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private final Configuration messages = configs("messages");

    public SpigotMinesConfirmGUI(Player p, String mineName) {
        this.p = p;
        this.mineName = mineName;
    }

    public void open(){

        // Create the inventory
        int dimension = 9;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> Delete"));

        if (guiBuilder(inv)) return;

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {


        // Blocks of the mine
        List<String> confirmlore = createLore(
                messages.getString("Gui.Lore.ClickToConfirm"));
        List<String> cancelore = createLore(
                messages.getString("Gui.Lore.ClickToCancel"));

        // Create the button, set up the material, amount, lore and name
        ItemStack confirm = createButton(Material.EMERALD_BLOCK, 1, confirmlore, SpigotPrison.format("&3" + "Confirm: " + mineName));
        ItemStack cancel = createButton(Material.REDSTONE_BLOCK, 1, cancelore, SpigotPrison.format("&3" + "Cancel: " + mineName));

        // Position of the button
        inv.setItem(2, confirm);
        inv.setItem(6, cancel);
    }
}

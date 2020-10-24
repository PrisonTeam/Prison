package tech.mcprison.prison.spigot.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class SpigotPrisonGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotPrisonGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3PrisonManager"));

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

        Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

        // Lore of the button
        List<String> rankslore = createLore(
        		messages.getString("Gui.Lore.RanksButton"),
                messages.getString("Gui.Lore.ClickToOpen"));

        // Lore of the button
        List<String> prisontaskslore = createLore(
                messages.getString("Gui.Lore.PrisonTasksButton"),
                messages.getString("Gui.Lore.ClickToOpen"));

        // Lore of the button
        List<String> mineslore = createLore(
                messages.getString("Gui.Lore.MinesButton"),
                messages.getString("Gui.Lore.ClickToOpen"));

        // Lore of the button
        List<String> sellallLore = createLore(
                messages.getString("Gui.Lore.ClickToOpen"));

        // Lore of the button
        List<String> closeGUILore = createLore(
                messages.getString("Gui.Lore.ClickToClose")
        );

        ItemStack closeGUI = createButton(Material.RED_STAINED_GLASS, 1, closeGUILore, SpigotPrison.format("&c" + "Close"));

        // Create the button, set up the material, amount, lore and name
        ItemStack ranks = createButton(Material.TRIPWIRE_HOOK, 1, rankslore, SpigotPrison.format("&3" + "Ranks"));

        // Create the button, set up the material, amount, lore and name
        ItemStack prisontasks = createButton(Material.IRON_PICKAXE, 1, prisontaskslore, SpigotPrison.format("&3" + "AutoManager"));

        // Create the button, set up the material, amount, lore and name
        ItemStack mines = createButton(Material.DIAMOND_ORE, 1, mineslore, SpigotPrison.format("&3" + "Mines"));

        // Create the button, set up the material, amount, lore and name
        ItemStack sellall = createButton(Material.CHEST, 1 ,  sellallLore, SpigotPrison.format("&3" + "SellAll"));

        // Position of the button
        inv.setItem(10, ranks);

        // Position of the button
        inv.setItem(12, prisontasks);

        // Position of the button
        inv.setItem(14, mines);

        // Position of the button
        inv.setItem(16, sellall);

        // Position of the button
        inv.setItem(26, closeGUI);
    }

}

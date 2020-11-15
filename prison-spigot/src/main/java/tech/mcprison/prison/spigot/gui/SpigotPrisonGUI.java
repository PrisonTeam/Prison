package tech.mcprison.prison.spigot.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class SpigotPrisonGUI extends SpigotGUIComponents {

    private final Player p;
    private final Configuration messages = messages();

    public SpigotPrisonGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3PrisonManager"));

        if (guiBuilder(inv)) return;

        // Open the inventory, I don't remember why I did add the this.p
        openGUI(p,inv);
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


        List<String> ranksLore = createLore(
        		messages.getString("Gui.Lore.RanksButton"),
                messages.getString("Gui.Lore.ClickToOpen"));
        List<String> prisonTasksLore = createLore(
                messages.getString("Gui.Lore.PrisonTasksButton"),
                messages.getString("Gui.Lore.ClickToOpen"));
        List<String> minesLore = createLore(
                messages.getString("Gui.Lore.MinesButton"),
                messages.getString("Gui.Lore.ClickToOpen"));
        List<String> sellAllLore = createLore(
                messages.getString("Gui.Lore.ClickToOpen"));
        List<String> closeGUILore = createLore(
                messages.getString("Gui.Lore.ClickToClose")
        );

        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));

        // Create the button, set up the material, amount, lore and name
        ItemStack ranks = createButton(Material.TRIPWIRE_HOOK, 1, ranksLore, SpigotPrison.format("&3" + "Ranks"));
        ItemStack autoManager = createButton(Material.IRON_PICKAXE, 1, prisonTasksLore, SpigotPrison.format("&3" + "AutoManager"));
        ItemStack mines = createButton(Material.DIAMOND_ORE, 1, minesLore, SpigotPrison.format("&3" + "Mines"));
        ItemStack sellAll = createButton(Material.CHEST, 1 ,  sellAllLore, SpigotPrison.format("&3" + "SellAll"));

        // Position of the button
        inv.setItem(10, ranks);
        inv.setItem(12, autoManager);
        inv.setItem(14, mines);
        inv.setItem(16, sellAll);
        inv.setItem(26, closeGUI);
    }
}

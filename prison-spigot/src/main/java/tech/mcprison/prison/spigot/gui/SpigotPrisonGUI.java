package tech.mcprison.prison.spigot.gui;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.util.List;

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

        // Open the inventory, I don't remember why I did add the this.p
        openGUI(p,inv);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {


        List<String> ranksLore = createLore(
        		messages.getString("Lore.RanksButton"),
                messages.getString("Lore.ClickToOpen"));
        List<String> prisonTasksLore = createLore(
                messages.getString("Lore.PrisonTasksButton"),
                messages.getString("Lore.ClickToOpen"));
        List<String> minesLore = createLore(
                messages.getString("Lore.MinesButton"),
                messages.getString("Lore.ClickToOpen"));
        List<String> sellAllLore = createLore(
                messages.getString("Lore.ClickToOpen"));
        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));

        // Create the button, set up the material, amount, lore and name
        ItemStack ranks = createButton(XMaterial.TRIPWIRE_HOOK.parseItem(), ranksLore, SpigotPrison.format("&3" + "Ranks - Ladders"));
        ItemStack autoManager = createButton(XMaterial.IRON_PICKAXE.parseItem(), prisonTasksLore, SpigotPrison.format("&3" + "AutoManager"));
        ItemStack mines = createButton(XMaterial.DIAMOND_ORE.parseItem(), minesLore, SpigotPrison.format("&3" + "Mines"));
        ItemStack sellAll = createButton(XMaterial.CHEST.parseItem(), sellAllLore, SpigotPrison.format("&3" + "SellAll"));

        // Position of the button
        inv.setItem(10, ranks);
        inv.setItem(12, autoManager);
        inv.setItem(14, mines);
        inv.setItem(16, sellAll);
        inv.setItem(26, closeGUI);
    }
}

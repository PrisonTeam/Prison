package tech.mcprison.prison.spigot.gui;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotPrisonGUI extends SpigotGUIComponents {

    private final Player p;
    private final int dimension = 45;

    public SpigotPrisonGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create PrisonGUI, it requires Player (who will open the GUI), size and title.
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3PrisonManager");

        // Create and add buttons.
        gui.addButton(new Button(10, XMaterial.TRIPWIRE_HOOK, createLore(
                messages.getString("Lore.RanksButton"),
                messages.getString("Lore.ClickToOpen")), "&3" + "Ranks - Ladders"));

        gui.addButton(new Button(13, XMaterial.IRON_PICKAXE, createLore(
                messages.getString("Lore.PrisonTasksButton"),
                messages.getString("Lore.ClickToOpen")), SpigotPrison.format("&3" + "AutoManager")));

        gui.addButton(new Button(16, XMaterial.DIAMOND_ORE, createLore(
                messages.getString("Lore.MinesButton"),
                messages.getString("Lore.ClickToOpen")), SpigotPrison.format("&3" + "Mines")));

        gui.addButton(new Button(29, XMaterial.CHEST, createLore(
                messages.getString("Lore.ClickToOpen")), SpigotPrison.format("&3" + "SellAll")));

        gui.addButton(new Button(33, XMaterial.CHEST_MINECART, createLore(
                messages.getString("Lore.ClickToOpen")), SpigotPrison.format("&3" + "Backpacks")));

        gui.addButton(new Button(44, XMaterial.RED_STAINED_GLASS_PANE, createLore(
                messages.getString("Lore.ClickToClose")), SpigotPrison.format("&c" + "Close")));

        gui.open();
    }
}

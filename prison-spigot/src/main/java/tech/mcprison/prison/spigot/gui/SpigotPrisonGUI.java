package tech.mcprison.prison.spigot.gui;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotPrisonGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotPrisonGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create PrisonGUI, it requires Player (who will open the GUI), size and title.
        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3PrisonManager");

        // Create and add buttons.
        gui.addButton(new Button(10, XMaterial.TRIPWIRE_HOOK, new ButtonLore(messages.getString("Lore.ClickToOpen"), messages.getString("Lore.RanksButton")), "&3" + "Ranks - Ladders"));
        gui.addButton(new Button(13, XMaterial.IRON_PICKAXE, new ButtonLore(messages.getString("Lore.ClickToOpen"), messages.getString("Lore.PrisonTasksButton")), SpigotPrison.format("&3" + "AutoManager")));
        gui.addButton(new Button(16, XMaterial.DIAMOND_ORE, new ButtonLore(messages.getString("Lore.ClickToOpen"), messages.getString("Lore.MinesButton")), SpigotPrison.format("&3" + "Mines")));
        gui.addButton(new Button(29, XMaterial.CHEST, new ButtonLore(messages.getString("Lore.ClickToOpen"), null), SpigotPrison.format("&3" + "SellAll")));
        gui.addButton(new Button(33, XMaterial.CHEST_MINECART, new ButtonLore(messages.getString("Lore.ClickToOpen"), null), SpigotPrison.format("&3" + "Backpacks")));
        gui.addButton(new Button(44, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(messages.getString("Lore.ClickToClose"), null), SpigotPrison.format("&c" + "Close")));

        gui.open();
    }
}

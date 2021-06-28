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
public class PrisonSetupGUI extends SpigotGUIComponents {

    private final Player p;

    public PrisonSetupGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Create Prison GUI.
        PrisonGUI gui = new PrisonGUI(p, 9, "&3Prison Setup -> Confirmation");

        // Add button.
        gui.addButton(new Button(2, XMaterial.EMERALD_BLOCK, createLore(
                messages.getString("Lore.ClickToConfirm"),
                messages.getString("Lore.noRanksFoundSetup"),
                messages.getString("Lore.noRanksFoundSetup1"),
                messages.getString("Lore.noRanksFoundSetup2"),
                messages.getString("Lore.noRanksFoundSetup3"),
                messages.getString("Lore.noRanksFoundSetup4"),
                messages.getString("Lore.noRanksFoundSetup5"),
                messages.getString("Lore.noRanksFoundSetup6"),
                messages.getString("Lore.noRanksFoundSetup7"),
                messages.getString("Lore.noRanksFoundSetup8")
        ), "&3" + "Confirm: Setup"));

        // Add button.
        gui.addButton(new Button(6, XMaterial.REDSTONE_BLOCK, createLore(
                messages.getString("Lore.ClickToCancel")), "&3" + "Cancel: Setup"));

        // Open Prison GUI.
        gui.open();
    }
}

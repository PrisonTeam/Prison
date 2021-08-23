package tech.mcprison.prison.spigot.gui.rank;

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
public class SpigotConfirmPrestigeGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotConfirmPrestigeGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Create the inventory
        int dimension = 9;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Prestige -> Confirmation");

        ButtonLore confirmLore = new ButtonLore(createLore(messages.getString("Lore.ClickToConfirm")), createLore(
                messages.getString("Lore.PrestigeWarning"),
                messages.getString("Lore.PrestigeWarning2"),
                messages.getString("Lore.PrestigeWarning3")));

        ButtonLore cancelLore = new ButtonLore(messages.getString("Lore.ClickToCancel"), null);


        // Create the button, set up the material, amount, lore and name
        gui.addButton(new Button(2, XMaterial.EMERALD_BLOCK, confirmLore, SpigotPrison.format("&3" + "Confirm: Prestige")));
        gui.addButton(new Button(6, XMaterial.REDSTONE_BLOCK, cancelLore, SpigotPrison.format("&3" + "Cancel: Don't Prestige")));

        gui.open();
    }
}

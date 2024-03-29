package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMinesConfirmGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;

    public SpigotMinesConfirmGUI(Player p, String mineName) {
        this.p = p;
        this.mineName = mineName;
    }

    public void open(){

        // Create GUI.
        int dimension = 9;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Mines -> Delete");

        ButtonLore confirmLore = new ButtonLore( guiClickToConfirmMsg(), null);
        ButtonLore cancelLore = new ButtonLore( guiClickToCancelMsg(), null);


        // Position of the button
        gui.addButton(new Button(2, XMaterial.EMERALD_BLOCK, confirmLore, "&3Confirm: " + mineName));
        gui.addButton(new Button(6, XMaterial.REDSTONE_BLOCK, cancelLore, "&3Cancel: " + mineName));

        // Open GUI.
        gui.open();
    }
}

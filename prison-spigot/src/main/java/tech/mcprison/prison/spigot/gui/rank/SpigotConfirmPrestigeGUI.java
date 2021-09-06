package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
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

        ButtonLore confirmLore = new ButtonLore(createLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_confirm)), createLore(
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_prestige_warning_1),
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_prestige_warning_2),
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_prestige_warning_3)));

        ButtonLore cancelLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_cancel), null);


        // Create the button, set up the material, amount, lore and name
        gui.addButton(new Button(2, XMaterial.EMERALD_BLOCK, confirmLore, SpigotPrison.format("&3Confirm: Prestige")));
        gui.addButton(new Button(6, XMaterial.REDSTONE_BLOCK, cancelLore, SpigotPrison.format("&3Cancel: Don't Prestige")));

        gui.open();
    }
}

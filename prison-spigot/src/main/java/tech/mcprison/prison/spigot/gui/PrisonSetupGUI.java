package tech.mcprison.prison.spigot.gui;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

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

        // Create lore.
        ButtonLore lore = new ButtonLore(createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_confirm)), createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_1),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_2),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_3),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_4),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_5),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_6),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_7),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_ranks_setup_8)));

        // Add button.
        gui.addButton(new Button(2, XMaterial.EMERALD_BLOCK, lore, "&3Confirm: Setup"));

        // Add button.
        gui.addButton(new Button(6, XMaterial.REDSTONE_BLOCK, createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_cancel)), "&3Cancel: Setup"));

        // Open Prison GUI.
        gui.open();
    }
}

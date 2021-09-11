package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author anonymousGCA (GABRYCA)
 * */
public class BackpacksAdminGUI extends SpigotGUIComponents {

    private final Player p;

    public BackpacksAdminGUI(Player p) {
        this.p = p;
    }

    public void open(){

        if (!BackpacksUtil.isEnabled()){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_backpack_disabled));
            p.closeInventory();
            return;
        }

        int dimension = 27;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Backpacks-Admin");

        ButtonLore lore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), null);

        gui.addButton(new Button(11, XMaterial.CHEST, lore, "&3Backpacks-List"));
        gui.addButton(new Button(15, XMaterial.PAPER, lore, "&3Backpack-Settings"));
        gui.addButton(new Button(dimension -1, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null), "&cClose"));

        gui.open();
    }
}

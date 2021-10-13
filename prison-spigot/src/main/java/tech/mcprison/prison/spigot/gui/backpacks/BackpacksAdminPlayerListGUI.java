package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.Set;

/**
 * @author AnonymousGCA (GABRYCA)
 * */
public class BackpacksAdminPlayerListGUI extends SpigotGUIComponents {

    private final Player p;
    private final String clickToOpen = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open);

    public BackpacksAdminPlayerListGUI(Player p){
        this.p = p;
    }

    /**
     * This GUI will be disabled for now.
     * */
    //TODO
    // Add a PlayerCache to get Backpacks even if the original player Owner is offline.
    public void open(){

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        Output.get().sendWarn(new SpigotPlayer(p), "Not implemented yet.");

        /*
        int dimension = 54;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Backpacks-Admin-Players");

        if (backpacksData.getConfigurationSection("Inventories") == null){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_backpack_empty));
            return;
        }

        Set<String> playerUUID;
        try {
            playerUUID = backpacksData.getConfigurationSection("Inventories").getKeys(false);
        } catch (NullPointerException ex){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_backpack_empty));
            return;
        }

        ButtonLore backpackLore = new ButtonLore(clickToOpen, null);

        int playersFound = 0;
        for (String uuid : playerUUID) {

            String name = null;
            if (playersFound <= 54) {
                if (uuid.equalsIgnoreCase(backpacksData.getString("Inventories." + uuid + ".UniqueID"))) {
                    name = backpacksData.getString("Inventories." + uuid + ".PlayerName");
                }
            }

            if (playersFound < 54) {

                if (name != null) {
                    gui.addButton(new Button(playersFound, XMaterial.PAPER, backpackLore, "&3Backpacks " + name));
                    playersFound++;
                }

            } else {
                gui.open();
                return;
            }
        }
        gui.open();
        */
    }
}

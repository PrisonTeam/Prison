package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
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
    private final Configuration backpacksData = BackpacksUtil.get().getBackpacksData();
    private final String clickToOpen = messages.getString("Lore.ClickToOpen");

    public BackpacksAdminPlayerListGUI(Player p){
        this.p = p;
    }

    public void open(){

        int dimension = 54;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3" + "Backpacks-Admin-Players");

        if (backpacksData.getConfigurationSection("Inventories") == null){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.BackPackListEmpty"));
            return;
        }

        Set<String> playerUUID;
        try {
            playerUUID = backpacksData.getConfigurationSection("Inventories").getKeys(false);
        } catch (NullPointerException ex){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.BackPackListEmpty"));
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
    }
}

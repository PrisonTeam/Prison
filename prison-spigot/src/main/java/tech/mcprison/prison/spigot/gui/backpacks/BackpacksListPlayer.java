package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

public class BackpacksListPlayer extends SpigotGUIComponents {

    private final Player p;
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

    public BackpacksListPlayer(Player p) {
        this.p = p;
    }

    public void open(){

        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + p.getName() + " -> Backpacks"));

        List<String> lore = createLore(
          messages.getString("Lore.ClickToOpen")
        );

        if (BackpacksUtil.get().getBackpacksIDs(p).isEmpty()){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.BackPackDoNotOwnAny")));
            return;
        }

        for (String id : BackpacksUtil.get().getBackpacksIDs(p)){
            if (id != null){
                inv.addItem(createButton(XMaterial.CHEST.parseItem(), lore, SpigotPrison.format("&3Backpack-" + id)));
            } else {
                inv.addItem(createButton(XMaterial.CHEST.parseItem(), lore, SpigotPrison.format("&3Backpack")));
            }
        }

        openGUI(p, inv);
    }
}

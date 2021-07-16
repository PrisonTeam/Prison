package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

public class BackpacksAdminGUI extends SpigotGUIComponents {

    private final Player p;

    public BackpacksAdminGUI(Player p) {
        this.p = p;
    }

    public void open(){

        if (!BackpacksUtil.isEnabled()){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.BackPackIsDisabled"));
            p.closeInventory();
            return;
        }

        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Backpacks-Admin"));

        // List Button.
        List<String> lore = createLore(
                messages.getString("Lore.ClickToOpen")
        );
        ItemStack listButton = createButton(XMaterial.CHEST.parseItem(), lore, "&3Backpacks-List");

        ItemStack settingsButton = createButton(XMaterial.PAPER.parseItem(), lore, "&3Backpack-Settings");

        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));
        inv.setItem(dimension-1, closeGUI);

        inv.setItem(11, listButton);
        inv.setItem(15, settingsButton);
        openGUI(p, inv);
    }
}

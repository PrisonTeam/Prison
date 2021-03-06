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

public class BackpacksListPlayerGUI extends SpigotGUIComponents {

    private final Player p;

    public BackpacksListPlayerGUI(Player p) {
        this.p = p;
    }

    public void open(){

        if (BackpacksUtil.get().getBackpacksLimit(p) == 0){
            Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.BackPackCantOwn")));
            return;
        }

        int dimension = 54;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + p.getName() + " -> Backpacks"));

        List<String> loreAddBackpackButton = createLore(
                messages.getString("Lore.ClickToAddBackpack"),
                " ",
                "&8-----------------------",
                " ",
                messages.getString("Lore.ClickToAddBackpackInst0"),
                messages.getString("Lore.ClickToAddBackpackInst1"),
                messages.getString("Lore.ClickToAddBackpackInst2"),
                messages.getString("Lore.ClickToAddBackpackInst3"),
                " ",
                "&8-----------------------"
        );

        // Global Strings.
        String loreClickToOpen = messages.getString("Lore.ClickToOpen");

        if (!BackpacksUtil.get().getBackpacksIDs(p).isEmpty()) {
            int slot = 0;
            for (String id : BackpacksUtil.get().getBackpacksIDs(p)) {
                List<String> lore = createLore(
                        loreClickToOpen,
                        " "
                );
                if (slot < 45) {
                    if (id != null) {
                        lore.add(SpigotPrison.format("&3/backpack " + id));
                        inv.setItem(slot, createButton(XMaterial.CHEST.parseItem(), lore, SpigotPrison.format("&3Backpack-" + id)));
                    } else {
                        lore.add(SpigotPrison.format("&3/backpack"));
                        inv.setItem(slot, createButton(XMaterial.CHEST.parseItem(), lore, SpigotPrison.format("&3Backpack")));
                    }
                    slot++;
                }
            }
        }

        if (BackpacksUtil.get().getBackpacksIDs(p).isEmpty() || !BackpacksUtil.get().reachedBackpacksLimit(p)) {
            inv.setItem(49, createButton(XMaterial.EMERALD_BLOCK.parseItem(), loreAddBackpackButton, SpigotPrison.format("&aNew Backpack")));
        }

        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));
        inv.setItem(dimension-1, closeGUI);

        openGUI(p, inv);
    }
}

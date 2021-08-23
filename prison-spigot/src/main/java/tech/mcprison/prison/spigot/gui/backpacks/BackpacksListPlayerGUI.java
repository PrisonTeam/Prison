package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author AnonymousGCA (GABRYCA)
 * */
public class BackpacksListPlayerGUI extends SpigotGUIComponents {

    private final Player p;

    public BackpacksListPlayerGUI(Player p) {
        this.p = p;
    }

    public void open(){

        if (BackpacksUtil.get().getBackpacksLimit(p) == 0){
            Output.get().sendInfo(new SpigotPlayer(p), messages.getString("Message.BackPackCantOwn"));
            return;
        }

        int dimension = 54;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3" + p.getName() + " -> Backpacks");

        ButtonLore loreAddBackpackButton = new ButtonLore(createLore(messages.getString("Lore.ClickToAddBackpack")), createLore(messages.getString("Lore.ClickToAddBackpackInst0"),
                messages.getString("Lore.ClickToAddBackpackInst1"),
                messages.getString("Lore.ClickToAddBackpackInst2"),
                messages.getString("Lore.ClickToAddBackpackInst3")));

        // Global Strings.
        String loreClickToOpen = messages.getString("Lore.ClickToOpen");

        if (!BackpacksUtil.get().getBackpacksIDs(p).isEmpty()) {
            int slot = 0;
            for (String id : BackpacksUtil.get().getBackpacksIDs(p)) {

                ButtonLore lore = new ButtonLore(loreClickToOpen, null);

                if (slot < 45) {
                    if (id != null) {
                        lore.setLoreDescription(SpigotPrison.format("&3/backpack " + id));
                        gui.addButton(new Button(slot, XMaterial.CHEST, lore, "&3Backpack-" + id));
                    } else {
                        lore.setLoreDescription(SpigotPrison.format("&3/backpack"));
                        gui.addButton(new Button(slot, XMaterial.CHEST, lore, "&3Backpack"));
                    }
                    slot++;
                }
            }
        }

        if (BackpacksUtil.get().getBackpacksIDs(p).isEmpty() || !BackpacksUtil.get().reachedBackpacksLimit(p)) {
            gui.addButton(new Button(49, XMaterial.EMERALD_BLOCK, loreAddBackpackButton, "&aNew Backpack"));
        }

        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(messages.getString("Lore.ClickToClose"), null), "&c" + "Close"));

        gui.open();
    }
}

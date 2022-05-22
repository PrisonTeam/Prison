package tech.mcprison.prison.spigot.gui.backpacks;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
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
            Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_backpack_cant_own));
            return;
        }

        int dimension = 54;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3" + p.getName() + " -> Backpacks");

        ButtonLore loreAddBackpackButton = new ButtonLore(createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_add_backpack)), createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_add_backpack_instruction_1),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_add_backpack_instruction_2),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_add_backpack_instruction_3)));

        // Global Strings.
        String loreClickToOpen = guiLeftClickToOpenMsg();

        if (!BackpacksUtil.get().getBackpacksIDs(p).isEmpty()) {
            int slot = 0;
            for (String id : BackpacksUtil.get().getBackpacksIDs(p)) {

                ButtonLore lore = new ButtonLore(loreClickToOpen, null);

                if (slot < 45) {
                    if (id != null) {
                        lore.setLoreDescription( "&3/backpack " + id );
                        gui.addButton(new Button(slot, XMaterial.CHEST, lore, "&3Backpack-" + id));
                    } else {
                        lore.setLoreDescription( "&3/backpack" );
                        gui.addButton(new Button(slot, XMaterial.CHEST, lore, "&3Backpack"));
                    }
                    slot++;
                }
            }
        }

        if (BackpacksUtil.get().getBackpacksIDs(p).isEmpty() || !BackpacksUtil.get().reachedBackpacksLimit(p)) {
            gui.addButton(new Button(49, XMaterial.EMERALD_BLOCK, loreAddBackpackButton, "&aNew Backpack"));
        }

        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null), "&cClose"));

        gui.open();
    }
}

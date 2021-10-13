package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
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

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        if (bUtil.getBackpacksLimit(p) == 0){
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
        String loreClickToOpen = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open);

        if (!bUtil.getBackpacks(p).isEmpty()) {
            int slot = 0;
            for (int i = 0; i < bUtil.getBackpacks(p).size(); i++){

                ButtonLore lore = new ButtonLore(loreClickToOpen, null);

                if (slot < 45) {
                    lore.setLoreDescription(SpigotPrison.format("&3/backpack " + i));
                    gui.addButton(new Button(slot, XMaterial.CHEST, lore, "&3Backpack-" + i));
                    slot++;
                }
            }
        } else if (!bUtil.reachedBackpacksLimit(p)) {
            gui.addButton(new Button(49, XMaterial.EMERALD_BLOCK, loreAddBackpackButton, "&aNew Backpack"));
        }

        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null), "&cClose"));

        gui.open();
    }
}

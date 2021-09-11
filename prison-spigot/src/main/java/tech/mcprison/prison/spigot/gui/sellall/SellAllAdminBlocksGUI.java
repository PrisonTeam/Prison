package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.Set;

/**
 * @author GABRYCA
 */
public class SellAllAdminBlocksGUI extends SpigotGUIComponents {

    private final Player p;
    private final int startingItem;

    public SellAllAdminBlocksGUI(Player p, int startingItem){
        this.p = p;
        this.startingItem = startingItem;
    }

    public void open() {

        updateSellAllConfig();

        PrisonGUI gui = new PrisonGUI(p, 54, "&3SellAll -> Blocks");

        boolean emptyInv = false;

        try {
            if (sellAllConfig.getConfigurationSection("Items") == null) {
                emptyInv = true;
            }
            if (sellAllConfig.getConfigurationSection("Items").getKeys(false).size() == 0){
                emptyInv = true;
            }
        } catch (NullPointerException e){
            emptyInv = true;
        }

        if (emptyInv){
            SpigotPlayer spigotPlayer = new SpigotPlayer(p);
            Output.get().sendWarn(spigotPlayer, messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_empty));
            return;
        }

        // Get the Items config section
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);

        // Global strings.
        String loreLine1 = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_delete);
        String loreLine2 = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_left_to_edit);
        String lorePermission = messages.getString(MessagesConfig.StringID.spigot_gui_lore_permission);
        String permissionSellAllBlock = sellAllConfig.getString("Options.Sell_Per_Block_Permission");
        String loreValue = messages.getString(MessagesConfig.StringID.spigot_gui_lore_value);

        boolean sellAllPerBlockPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"));

        int itemsAdded = 0, itemsRead = 0;
        for (String key : items) {
            itemsRead++;

            if (itemsRead >= startingItem) {

                if (startingItem != 0){
                    gui.addButton(new Button(45, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior " + (startingItem - 45)));
                }

                if (itemsAdded >= 45){
                    gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (startingItem + itemsAdded)));
                }

                if (itemsAdded < 45) {
                    ButtonLore itemsLore = new ButtonLore(createLore(loreLine1, loreLine2), createLore(loreValue + sellAllConfig.getString("Items." + key + ".ITEM_VALUE")));

                    if (sellAllPerBlockPermissionEnabled) {
                        itemsLore.addLineLoreDescription(SpigotPrison.format(lorePermission + "&7" + permissionSellAllBlock + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
                    }

                    gui.addButton(new Button(null, SpigotUtil.getXMaterial(sellAllConfig.getString("Items." + key + ".ITEM_ID")), itemsLore, "&3" + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
                    itemsAdded++;
                }
            }
        }
        gui.open();
    }
}

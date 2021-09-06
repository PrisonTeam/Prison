package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.Set;

/**
 * @author GABRYCA
 */
public class SellAllPlayerGUI extends SpigotGUIComponents {

    private final Player p;
    private final int startingItem;

    public SellAllPlayerGUI(Player p, int startingItem){
        this.p = p;
        this.startingItem = startingItem;
    }

    public void open() {

        PrisonGUI gui = new PrisonGUI(p, 54, "&3Prison -> SellAll-Player");

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
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.NoSellAllItems"));
            return;
        }

        // Get the Items config section
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);

        // Global strings.
        String loreValue = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_value);

        int itemsAdded = 0, itemsRead = 0;
        for (String key : items) {
            itemsRead++;

            if (itemsRead >= startingItem) {

                if (startingItem != 0){
                    gui.addButton(new Button(45, XMaterial.BOOK, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior " + (startingItem - 45)));
                }

                if (itemsAdded >= 45){
                    gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (startingItem + itemsAdded)));
                }

                if (itemsAdded < 45) {
                    gui.addButton(new Button(null, SpigotUtil.getXMaterial(sellAllConfig.getString("Items." + key + ".ITEM_ID")), new ButtonLore(null, loreValue + " " + sellAllConfig.getString("Items." + key + ".ITEM_VALUE")), "&3" + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
                    itemsAdded++;
                }
            }
        }
        gui.open();
    }
}

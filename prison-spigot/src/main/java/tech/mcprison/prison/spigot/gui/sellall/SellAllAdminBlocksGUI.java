package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;
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
            Output.get().sendWarn(spigotPlayer, messages.getString("Message.SellAllGUIEmpty"));
            Output.get().sendWarn(spigotPlayer, messages.getString("Message.SellAllGUIEmpty2"));
            return;
        }

        // Get the Items config section
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);

        // Global strings.
        String loreLine1 = messages.getString("Lore.RightClickToDelete");
        String loreLine2 = messages.getString("Lore.LeftClickToEdit");
        String lorePermission = messages.getString("Lore.Permission");
        String permissionSellAllBlock = sellAllConfig.getString("Options.Sell_Per_Block_Permission");
        String loreValue = messages.getString("Lore.Value");

        boolean sellAllPerBlockPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"));

        int itemsAdded = 0, itemsRead = 0;
        for (String key : items) {
            itemsRead++;

            if (itemsRead >= startingItem) {

                if (startingItem != 0){

                    List<String> priorPageLore = createLore(messages.getString("Lore.ClickToPriorPage"));
                    gui.addButton(new Button(45, XMaterial.BOOK, priorPageLore, "&7Prior " + (startingItem - 45)));

                }

                if (itemsAdded >= 45){

                    List<String> nextPageLore = createLore(messages.getString("Lore.ClickToNextPage"));
                    gui.addButton(new Button(53, XMaterial.BOOK, nextPageLore, "&7Next " + (startingItem + itemsAdded)));

                }

                if (itemsAdded < 45) {
                    List<String> itemsLore = createLore(
                            loreLine1,
                            loreLine2,
                            loreValue + sellAllConfig.getString("Items." + key + ".ITEM_VALUE")
                    );

                    if (sellAllPerBlockPermissionEnabled) {
                        itemsLore.add("");
                        itemsLore.add(SpigotPrison.format(lorePermission + "&7" + permissionSellAllBlock + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
                    }

                    gui.addButton(new Button(null, SpigotUtil.getXMaterial(sellAllConfig.getString("Items." + key + ".ITEM_ID")), itemsLore, "&3" + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
                    itemsAdded++;
                }
            }
        }
        gui.open();
    }
}

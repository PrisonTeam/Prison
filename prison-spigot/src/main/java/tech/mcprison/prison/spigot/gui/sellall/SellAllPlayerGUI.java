package tech.mcprison.prison.spigot.gui.sellall;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;
import java.util.Set;

/**
 * @author GABRYCA
 */
public class SellAllPlayerGUI extends SpigotGUIComponents {

    private final Player p;

    public SellAllPlayerGUI(Player p){
        this.p = p;
    }

    public void open() {

        if (guiBuilder()) return;

        Inventory inv = buttonsSetup();
        if (inv == null) return;

        openGUI(p, inv);
    }

    private Inventory buttonsSetup() {

        boolean emptyInv = false;

        try {
            if (sellAllConfig.getConfigurationSection("Items") == null) {
                emptyInv = true;
            }
        } catch (NullPointerException e){
            emptyInv = true;
        }

        if (emptyInv){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.NoSellAllItems")));
            p.closeInventory();
            return null;
        }

        // Get the Items config section
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(items.size() / 9D) * 9;

        if (dimension > 54){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.TooManySellAllItems")));
            return null;
        }

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Prison -> SellAll-Player"));

        for (String key : items) {
            List<String> itemsLore = createLore(
                    messages.getString("Lore.Value") + sellAllConfig.getString("Items." + key + ".ITEM_VALUE")
            );

            ItemStack item = createButton(SpigotUtil.getItemStack(SpigotUtil.getXMaterial(sellAllConfig.getString("Items." + key + ".ITEM_ID")), 1), itemsLore, SpigotPrison.format("&3" + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
            inv.addItem(item);
        }
        return inv;
    }

    private boolean guiBuilder() {
        try {
            buttonsSetup();
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

}

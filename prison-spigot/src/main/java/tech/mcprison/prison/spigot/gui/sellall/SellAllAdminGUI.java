package tech.mcprison.prison.spigot.gui.sellall;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * @author GABRYCA
 */
public class SellAllAdminGUI extends SpigotGUIComponents {

    private final Player p;

    public SellAllAdminGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Load configs
        Configuration conf = SpigotPrison.getSellAllConfig();
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (conf.getConfigurationSection("Items") == null){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.EmptyGui")));
            p.closeInventory();
            return;
        }

        // Get the Items config section
        Set<String> items = Objects.requireNonNull(conf.getConfigurationSection("Items")).getKeys(false);

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(items.size() / 9D) * 9;

        if (dimension > 54){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.TooManySellAllItems")));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3PrisonManager -> SellAll-Admin"));

        for (String key : items) {
            List<String> itemsLore = createLore(
                    "&cRight-Click to delete",
                    "&aLeft-Click to edit value",
                    "&3value: &a$" + conf.getString("Items." + key + ".ITEM_VALUE")
            );
            ItemStack item = createButton(Material.valueOf(conf.getString("Items." + key + ".ITEM_ID")), 1, itemsLore, SpigotPrison.format("&3" + conf.getString("Items." + key + ".ITEM_ID")));
            inv.addItem(item);
        }

        this.p.openInventory(inv);

    }

}

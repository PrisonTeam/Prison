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

        // Load configs
        Configuration conf = SpigotPrison.getSellAllConfig();
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (conf.getConfigurationSection("Items") == null){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.EmptyGui")));
            if (p.getOpenInventory() != null){
                p.closeInventory();
                return;
            }
            return;
        }

        // Get the Items config section
        Set<String> items = conf.getConfigurationSection("Items").getKeys(false);

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(items.size() / 9D) * 9;

        if (dimension < 9){
            p.sendMessage(SpigotPrison.format("&3[PRISON WARN] &cThere aren't items! An empty GUI's useless."));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3PrisonManager -> SellAll-Player"));

        for (String key : items) {
            List<String> itemsLore = createLore(
                    "&3value: &a$" + conf.getString("Items." + key + ".ITEM_VALUE")
            );
            ItemStack item = createButton(Material.valueOf(conf.getString("Items." + key + ".ITEM_ID")), 1, itemsLore, SpigotPrison.format("&3" + conf.getString("Items." + key + ".ITEM_ID")));
            inv.addItem(item);
        }

        this.p.openInventory(inv);

    }

}

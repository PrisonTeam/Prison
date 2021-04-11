package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;
import java.util.Set;

public class BackpacksAdminPlayerListGUI extends SpigotGUIComponents {

    private final Player p;
    private final Configuration backpacksData = BackpacksUtil.get().getBackpacksData();
    private final String clickToOpen = messages.getString("Lore.ClickToOpen");

    public BackpacksAdminPlayerListGUI(Player p){
        this.p = p;
    }

    public void open(){

        int dimension = 54;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Backpacks-Admin-Players"));

        Set<String> playerUUID = backpacksData.getConfigurationSection("Inventories").getKeys(false);

        List<String> backpackLore = createLore(
                clickToOpen
        );

        int playersFound = 0;
        for (String uuid : playerUUID) {

            String name = null;
            Set<String> items = backpacksData.getConfigurationSection("Inventories." + uuid).getKeys(false);
            for (String inventoryID : items) {
                if (playersFound <= 54) {
                    if (uuid.equalsIgnoreCase(backpacksData.getString("Inventories." + uuid + "." + inventoryID + ".UniqueID"))) {
                        name = backpacksData.getString("Inventories." + uuid + "." + inventoryID + ".PlayerName");
                    }
                }
            }

            if (playersFound < 54) {

                if (name != null) {

                    ItemStack playerButton = createButton(XMaterial.PAPER.parseItem(), backpackLore, "&3Backpacks " + name);

                    inv.setItem(playersFound, playerButton);
                    playersFound++;
                }
            } else {
                openGUI(p, inv);
                return;
            }
        }
        openGUI(p, inv);
    }
}

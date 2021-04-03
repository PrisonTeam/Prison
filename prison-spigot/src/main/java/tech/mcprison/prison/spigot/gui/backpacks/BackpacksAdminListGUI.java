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

public class BackpacksAdminListGUI extends SpigotGUIComponents {

    private final Player p;
    private final String playerBackpackName;
    private final Configuration backpacksData = BackpacksUtil.get().getBackpacksData();
    private final String loreShiftAndRightClickToDelete = messages.getString("Lore.ShiftAndRightClickToDelete");
    private final String loreInfo = messages.getString("Lore.Info");
    private final String lorePlayerOwner = messages.getString("Lore.PlayerOwner");
    private final String loreBackpackID = messages.getString("Lore.BackpackID");

    public BackpacksAdminListGUI(Player p, String playerBackpackName){
        this.p = p;
        this.playerBackpackName = playerBackpackName;
    }

    public void open(){

        int dimension = 54;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Backpacks-Admin-List"));

        Set<String> playerUUID = backpacksData.getConfigurationSection("Inventories").getKeys(false);

        int backpacksFound = 0;
        for (String uuid : playerUUID){

            Set<String> items = backpacksData.getConfigurationSection("Inventories." + uuid).getKeys(false);
            for (String inventoryID : items) {

                String name;
                name = backpacksData.getString("Inventories." + uuid + "." + inventoryID + ".PlayerName");
                if (name != null && name.equalsIgnoreCase(playerBackpackName)){

                    if (backpacksFound < 54) {
                        String id;
                        if (!inventoryID.equalsIgnoreCase("items")) {
                            id = inventoryID.substring(6);
                        } else {
                            id = "default";
                        }
                        List<String> backpacksLore = createLore(
                                loreShiftAndRightClickToDelete,
                                "&8----------------",
                                " ",
                                loreInfo,
                                lorePlayerOwner + name,
                                loreBackpackID + id,
                                " ",
                                "&8----------------"
                        );

                        ItemStack backpackButton = createButton(XMaterial.CHEST.parseItem(), backpacksLore, "&3" + "Backpack " + name + " " + id);
                        inv.setItem(backpacksFound, backpackButton);
                        backpacksFound++;
                    } else {
                        openGUI(p, inv);
                        return;
                    }
                }
            }
        }
        openGUI(p, inv);
    }
}

package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

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
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3" + "Backpacks-Admin-List");

        Set<String> playerUUID = backpacksData.getConfigurationSection("Inventories").getKeys(false);

        int backpacksFound = 0;
        for (String uuid : playerUUID){

            Set<String> items = backpacksData.getConfigurationSection("Inventories." + uuid).getKeys(false);
            for (String inventoryID : items) {

                String name;
                name = backpacksData.getString("Inventories." + uuid + ".PlayerName");
                if (name != null && name.equalsIgnoreCase(playerBackpackName)){

                    if (backpacksFound < 54) {
                        String id = null;
                        if (!inventoryID.equalsIgnoreCase("items") && !inventoryID.equalsIgnoreCase("PlayerName") && !inventoryID.equalsIgnoreCase("UniqueID") && !inventoryID.equalsIgnoreCase("Limit")) {
                            id = inventoryID.substring(6);
                        } else {
                            if (!inventoryID.equalsIgnoreCase("PlayerName") && !inventoryID.equalsIgnoreCase("UniqueID") && !inventoryID.equalsIgnoreCase("Limit")){
                                id = "default";
                            }
                        }
                        if (id != null) {
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

                            gui.addButton(new Button(backpacksFound, XMaterial.CHEST, backpacksLore, "&3" + "Backpack " + name + " " + id));
                            backpacksFound++;
                        }
                    } else {
                        gui.open();
                        return;
                    }
                }
            }
        }
        gui.open();
    }
}

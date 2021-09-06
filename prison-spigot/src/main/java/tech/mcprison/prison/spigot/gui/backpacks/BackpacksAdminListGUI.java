package tech.mcprison.prison.spigot.gui.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.Set;

/**
 * @author AnonymousGCA (GABRYCA)
 * */
public class BackpacksAdminListGUI extends SpigotGUIComponents {

    private final Player p;
    private final String playerBackpackName;
    private final Configuration backpacksData = BackpacksUtil.get().getBackpacksData();
    private final String loreShiftAndRightClickToDelete = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_delete);
    private final String loreInfo = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_info);
    private final String lorePlayerOwner = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_owner);
    private final String loreBackpackID = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_backpack_id);

    public BackpacksAdminListGUI(Player p, String playerBackpackName){
        this.p = p;
        this.playerBackpackName = playerBackpackName;
    }

    public void open(){

        int dimension = 54;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Backpacks-Admin-List");

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

                            ButtonLore backpacksLore = new ButtonLore(createLore(loreShiftAndRightClickToDelete), createLore(
                                    loreInfo,
                                    lorePlayerOwner + " " + name,
                                    loreBackpackID + " " + id));

                            gui.addButton(new Button(backpacksFound, XMaterial.CHEST, backpacksLore, "&3Backpack " + name + " " + id));
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

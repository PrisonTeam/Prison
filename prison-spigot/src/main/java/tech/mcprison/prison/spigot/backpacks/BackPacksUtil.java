package tech.mcprison.prison.spigot.backpacks;


import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.util.Set;

public class BackPacksUtil {

    private static BackPacksUtil instance;
    private Configuration backPacksConfig = SpigotPrison.getInstance().getBackPacksConfig();
    private File backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
    private FileConfiguration backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);

    /**
     * Check if BackPacks's enabled.
     * */
    public static boolean isEnabled(){
        return SpigotPrison.getInstance().getConfig().getString("backpacks").equalsIgnoreCase("true");
    }

    private void backPacksConfigUpdater(){
        backPacksConfig = SpigotPrison.getInstance().getBackPacksConfig();
    }

    /**
     * Get SellAll instance.
     * */
    public static BackPacksUtil get() {
        if (instance == null && isEnabled()){
            instance = new BackPacksUtil();
        }

        return instance;
    }

    /**
     * Get Prison Backpacks Inventory.
     *
     * @param p player
     * */
    public Inventory getInventory(Player p){

        Inventory inv = Bukkit.createInventory(p, 54, SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return null;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backPacksDataConfig.getItemStack("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
                if (finalItem != null) {
                    inv.setItem(Integer.parseInt(slot), finalItem);
                }
            }
        }

        return inv;
    }


    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){

        if (string == null){
            return false;
        }

        if (string.equalsIgnoreCase("true")){
            return true;
        } else if (string.equalsIgnoreCase("false")){
            return false;
        }

        return false;
    }
}

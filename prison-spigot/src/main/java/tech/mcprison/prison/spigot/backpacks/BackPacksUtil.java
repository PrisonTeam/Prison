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
import java.io.IOException;
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
     * Also if you need to modify it you can do it just by using BackPacksUtil getInventory(player)
     * modify your inventory as you'd usually with spigot inventories
     * and then use BackPacksUtil setInventory(player, inventory) is recommended.
     *
     * EXAMPLE for adding item:
     * Inventory inv = BackPacksUtil.getInventory(p);
     * ItemStack item = new ItemStack(Material.COAL_ORE, 1);
     * inv.addItem(item)
     * BackPacksUtil.setInventory(inv);
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
     * Merge another inventory into the backpack inventory.
     *
     * @param p - player
     * @param inv - Inventory
     * */
    public void setInventory(Player p, Inventory inv){

        if (inv.getContents() != null){
            int slot = 0;

            try {
                backPacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
                backPacksDataConfig.save(backPacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }

            backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
            backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);

            for (ItemStack item : inv.getContents()){
                if (item != null){

                    backPacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK", item);

                    slot++;
                }
            }
            if (slot != 0){
                try {
                    backPacksDataConfig.save(backPacksFile);
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Add an item to the backpack inventory
     * NOT TESTED!
     *
     * RECOMMENDED WAY:
     * If you need to modify the inventory you can do it just by using BackPacksUtil getInventory(player),
     * modify your inventory as you'd usually with spigot inventories,
     * and then use BackPacksUtil setInventory(player, inventory) is recommended.
     *
     * EXAMPLE for adding item:
     * Inventory inv = BackPacksUtil.getInventory(p);
     * ItemStack item = new ItemStack(Material.COAL_ORE, 1);
     * inv.addItem(item);
     * BackPacksUtil.setInventory(inv);
     *
     * @param p - player
     * @param item - itemstack
     * */
    public void addItem(Player p, ItemStack item){

        /*backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
        backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);

        boolean hasError = false;
        int slot = 0;
        // Get the Items config section
        Set<String> slots = null;
        try {
            slots = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ignored){}
        if (slots != null) {
            slot = slots.size();
        }

        if (item != null){
            backPacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK", item);
        }

        try{
            backPacksDataConfig.save(backPacksFile);
        } catch (IOException ex){
            hasError = true;
        }*/

        Inventory inv = p.getInventory();
        inv.addItem(item);
        setInventory(p, inv);
    }


    /**
     * Remove item from backpack
     * NOT TESTED!
     *
     * RECOMMENDED WAY:
     * If you need to modify the inventory you can do it just by using BackPacksUtil getInventory(player),
     * modify your inventory as you'd usually with spigot inventories,
     * and then use BackPacksUtil setInventory(player, inventory) is recommended.
     *
     * EXAMPLE for removing item:
     * Inventory inv = BackPacksUtil.getInventory(p);
     * ItemStack item = new ItemStack(Material.COAL_ORE, 1);
     * inv.removeItem(item);
     * BackPacksUtil.setInventory(inv);
     *
     * @param p - player
     * @param item - itemstack
     * */
    public void removeItem(Player p, ItemStack item){
        Inventory inv = p.getInventory();
        inv.removeItem(item);
        setInventory(p, inv);
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

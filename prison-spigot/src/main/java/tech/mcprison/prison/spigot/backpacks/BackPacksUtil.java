package tech.mcprison.prison.spigot.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BackPacksUtil {

    private static BackPacksUtil instance;
    private Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
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

    public void updateCachedBackpack(){
        backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
        backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);
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
     * Set default backpack for new players.
     *
     * @param p - Player
     * */
    public void setDefaultBackpackPlayer(Player p) {

        updateCachedBackpack();

        int size = 54;
        try {
            size = Integer.parseInt(backPacksConfig.getString("Options.BackPack_Default_Size"));
        } catch (NumberFormatException ignored){}

        if (backPacksConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName") == null){
            try {
                backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
                backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);
                backPacksDataConfig.set("Inventories." + p.getUniqueId() + ".PlayerName", p.getName());
                backPacksDataConfig.set("Inventories." + p.getUniqueId() + ".Size", size);
                backPacksDataConfig.save(backPacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }
        }

        if (getBoolean(backPacksConfig.getString("Options.BackPack_Item_OnJoin"))) {
            Bukkit.dispatchCommand(p, "backpack item");
        }

        updateCachedBackpack();
    }

    /**
     * Reset a player's inventory.
     * */
    public void resetBackpack(Player p) {

        updateCachedBackpack();

        try {
            backPacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
            backPacksDataConfig.save(backPacksFile);
        } catch (IOException ex){
            ex.printStackTrace();
            return;
        }

        updateCachedBackpack();
    }

    /**
     * Set Player Backpacks max dimensions.
     * NOTE: Should be a multiple of 9.
     * Max dimension for now's 54.
     *
     * @param p - Player
     * @param size - Size
     *
     * */
    public void setBackpackSize(Player p, int size){

        updateCachedBackpack();

        // Must be multiple of 9.
        if (size % 9 != 0 || size > 54){
            return;
        }

        updateCachedBackpack();

        backPacksDataConfig.set("Inventories." + p.getUniqueId() + ".Size", size);

        try {
            backPacksDataConfig.save(backPacksFile);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        updateCachedBackpack();
    }

    public int getBackpackSize(Player p){

        updateCachedBackpack();

        int backPackSize = 0;

        try {
            backPackSize = Integer.parseInt(backPacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Size"));
        } catch (NumberFormatException ignored){}
        return backPackSize;
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
     *
     * @return inv - Inventory
     * */
    public Inventory getInventory(Player p){

        Inventory inv = Bukkit.createInventory(p, 54, SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return inv;
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
     * Adding the extra inventory argument will just modify your already
     * made inventory with the backpack content.
     * */
    public Inventory getInventory(Player p, Inventory inv){

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backPacksDataConfig.getItemStack("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
                if (finalItem != null) {
                    try {
                        inv.setItem(Integer.parseInt(slot), finalItem);
                    } catch (ArrayIndexOutOfBoundsException ex){
                        return inv;
                    }
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

        updateCachedBackpack();

        if (inv.getContents() != null){
            int slot = 0;

            try {
                backPacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
                backPacksDataConfig.save(backPacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }

            updateCachedBackpack();

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
        } else {
            // If it's null just delete the whole stored inventory.
            try {
                backPacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
                backPacksDataConfig.save(backPacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

        updateCachedBackpack();
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
     * Give backpack item to a player.
     **/
    public void giveBackPackToPlayer(Player p){
        backPackItem(p);
    }

    private void backPackItem(Player p) {

        if (!getBoolean(backPacksConfig.getString("Options.Back_Pack_GUI_Opener_Item"))) {
            return;
        }

        boolean playerHasItemBackPack = false;

        Inventory inv = p.getInventory();

        for (ItemStack item : inv.getContents()) {
            if (item != null){

                ItemStack materialConf = SpigotUtil.getXMaterial(backPacksConfig.getString("Options.BackPack_Item")).parseItem();

                if (materialConf != null && item.getType() == materialConf.getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotPrison.format(backPacksConfig.getString("Options.BackPack_Item_Title")))){
                    playerHasItemBackPack = true;
                }
            }
        }

        if (!playerHasItemBackPack){

            ItemStack item;

            List<String> itemLore = createLore(
                    messages.getString("Lore.ClickToOpenBackpack")
            );

            item = createButton(SpigotUtil.getXMaterial(backPacksConfig.getString("Options.BackPack_Item")).parseItem(), itemLore, backPacksConfig.getString("Options.BackPack_Item_Title"));

            if (item != null) {
                inv.addItem(item);
            }
        }
    }

    /**
     * Create a Lore for an Item in the GUI.
     *
     * @param lores
     * */
    private List<String> createLore( String... lores ) {
        List<String> results = new ArrayList<>();
        for ( String lore : lores ) {
            results.add( SpigotPrison.format(lore) );
        }
        return results;
    }

    /**
     * Create a button for the GUI using ItemStack.
     *
     * @param item
     * @param lore
     * @param display
     * */
    private ItemStack createButton(ItemStack item, List<String> lore, String display) {

        if (item == null){
            item = XMaterial.BARRIER.parseItem();
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null){
            meta = XMaterial.BARRIER.parseItem().getItemMeta();
        }

        return getItemStack(item, lore, display, meta);
    }

    /**
     * Get ItemStack of an Item.
     *
     * @param item
     * @param lore
     * @param display
     * @param meta
     * */
    private ItemStack getItemStack(ItemStack item, List<String> lore, String display, ItemMeta meta) {
        if (meta != null) {
            meta.setDisplayName(SpigotPrison.format(display));
            try {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } catch (NoClassDefFoundError ignored){}
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
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

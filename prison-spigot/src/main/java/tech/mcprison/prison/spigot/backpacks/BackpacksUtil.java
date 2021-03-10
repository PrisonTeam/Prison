package tech.mcprison.prison.spigot.backpacks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.configs.BackpacksConfig;
import tech.mcprison.prison.spigot.configs.SpigotConfigComponents;


public class BackpacksUtil extends SpigotConfigComponents {

    private static BackpacksUtil instance;
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
    private Configuration backpacksConfig = SpigotPrison.getInstance().getBackpacksConfig();
    private File backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml");
    private FileConfiguration backpacksDataConfig = YamlConfiguration.loadConfiguration(backpacksFile);
    public static List<String> openBackpacks = new ArrayList<>();
    public static List<String> backpackEdited = new ArrayList<>();
    private final Compatibility compat = SpigotPrison.getInstance().getCompatibility();

    /**
     * Check if BackPacks's enabled.
     * */
    public static boolean isEnabled(){
        return SpigotPrison.getInstance().getConfig().getString("backpacks").equalsIgnoreCase("true");
    }

    private void backpacksConfigUpdater(){
        backpackConfigUpdater();
    }

    public void removeFromOpenBackpacks(Player p){
        openBackpacks.remove(p.getName());
    }

    public void updateCachedBackpack(){
        backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml");
        backpacksDataConfig = YamlConfiguration.loadConfiguration(backpacksFile);
    }

    /**
     * Get SellAll instance.
     * */
    public static BackpacksUtil get() {
        return getInstance();
    }

    /**
     * Add player to edited backpacks.
     *
     * @param p - Player
     * */
    public void addToEditedBackpack(Player p){
        if (!backpackEdited.contains(p.getName())){
            backpackEdited.add(p.getName());
        }
    }

    /**
     * Remove from edited backpacks.
     *
     * @param p - Player
     * */
    public void removeFromEditedBackpack(Player p){
        backpackEdited.remove(p.getName());
    }

    /**
     * Check if player own backpack at first join
     *
     * @param p - Player
     *
     * @return boolean - true/false
     * */
    public boolean isPlayerOwningBackpack(Player p){
        return checkOwnBackpack(p);
    }

    /**
     * Set default backpack for new players.
     *
     * @param p - Player
     * */
    public void setDefaultBackpackPlayer(Player p) {
        setDefaultBackpack(p);
    }

    /**
     * Set default backpack for new players by ID.
     *
     * @param p - Player
     * @param id - String id
     * */
    public void setDefaultBackpackPlayer(Player p, String id) {
        setDefaultBackpack(p, id);
    }

    /**
     * Reset a player's backpack.
     *
     * @param p - Player
     * */
    public void resetBackpack(Player p) {
        resetBackpackMethod(p);
    }
    
    /**
     * Reset a player's backpack by ID.
     *
     * @param p - Player
     * @param id - String ID
     * */
    public void resetBackpack(Player p, String id){
        resetBackpackMethod(p, id);
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
        backpackResize(p, size);
    }

    /**
     * Get a backpack size depending on the owner.
     *
     * @param p - Player
     *
     * @return backPackSize - Integer
     * */
    public int getBackpackSize(Player p){
        return getSize(p);
    }

    /**
     * Get a backpack size depending on the owner.
     *
     * @param p - Player
     * @param id - String ID
     *
     * @return backPackSize - Integer
     * */
    public int getBackpackSize(Player p, String id){
        return getSize(p, id);
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
     * @param p - player
     *
     * @return inv - Inventory/Backpack.
     * */
    public Inventory getBackpack(Player p){
        return getBackpackOwn(p);
    }

    /**
     * Get Prison backpack of a Player + ID, because a player can have more than one Inventory.
     *
     * @param p - Player
     * @param id - String ID
     *
     * @return inv - Inventory/Backpack.
     * */
    public Inventory getBackpack(Player p, String id){
        return getBackpackOwn(p, id);
    }

    /**
     * Open backpack.
     *
     * @param p - Player
     * */
    public void openBackpack(Player p){
        openBackpackDefault(p);
    }

    /**
     * Open backpack by ID.
     *
     * @param p - Player
     * @param id - String ID
     * */
    public void openBackpack(Player p, String id){
        openBackpackByID(p, id);
    }

    /**
     * Get backpack config
     * */
    public Configuration getBackpacksConfig(){
        return backpacksConfig;
    }

    /**
     * Adding the extra inventory argument will just modify your already
     * made inventory and add the backpack content if possible.
     * */
    public Inventory getBackpack(Player p, Inventory inv){
        return getBackpackCustom(p, inv);
    }

    /**
     * Merge another inventory into the backpack inventory.
     *
     * @param p - player
     * @param inv - Inventory
     * */
    public void setInventory(Player p, Inventory inv){
        saveInventory(p, inv);
    }

    /**
     * Merge another inventory into the backpack inventory by ID.
     *
     * @param p - Player
     * @param inv - Inventory
     * @param id - String ID
     * */
    public void setInventory(Player p, Inventory inv, String id){
        saveInventory(p, inv, id);
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
     *
     * @return HashMap with items that didn't fit.
     * */
    public HashMap<Integer, ItemStack> addItem(Player p, ItemStack item){
        return addItemToBackpack(p, item);
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
     * @param id - String id
     *
     * @return HashMap with items that didn't fit.
     * */
    public HashMap<Integer, ItemStack> addItem(Player p, ItemStack item, String id){
        return addItemToBackpack(p, item, id);
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
     *
     * @return HashMap with items that couldn't be removed.
     * */
    public HashMap<Integer, ItemStack> removeItem(Player p, ItemStack item){
        return removeItemFromBackpack(p, item);
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
     * @param id - String ID
     *
     * @return HashMap with items that couldn't be removed.
     * */
    public HashMap<Integer, ItemStack> removeItem(Player p, ItemStack item, String id){
        return removeItemFromBackpak(p, item, id);
    }

    /**
     * Give backpack item to a Player.
     *
     * @param p - Player
     **/
    public void giveBackpackToPlayer(Player p){
        backPackItem(p);
    }

    /**
     * PLay close backpack sound to a Player.
     * Must be enabled in the backpack config.
     *
     * @param p - Player
     * */
    public void playBackpackCloseSound(Player p){
        playCloseBackpackSoundToPlayer(p);
    }

    /**
     * Play backpack close sound to a PLayer.
     * Must be enabled in the backpack config.
     *
     * @param p - Player
     * */
    public void playOpenBackpackSound(Player p) {
        playOpenBackpackSoundToPlayer(p);
    }

    /**
     * Get an array of backpacks of a player.
     *
     * @param p - Player
     *
     * @return List<String> with IDs.
     * */
    public List<String> getBackpacksIDs(Player p){
        return getBackpacksIDsList(p);
    }

    /**
     * Get number of backpacks own by player.
     *
     * @param p - Player
     *
     * @return int - Integer number of backpacks own.
     * */
    public int getNumberOwnedBackpacks(Player p){
        return getNumberOfBackpacksOwnedByPlayer(p);
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

    private void backpackConfigUpdater() {
        BackpacksConfig bpTemp = new BackpacksConfig();
        bpTemp.initialize();
        backpacksConfig = bpTemp.getFileBackpacksConfig();
    }

    private static BackpacksUtil getInstance() {
        if (instance == null && isEnabled()){
            instance = new BackpacksUtil();
        }

        return instance;
    }

    private boolean checkOwnBackpack(Player p) {
        updateCachedBackpack();

        String playerName = backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Playername");
        return playerName != null;
    }

    private void setDefaultBackpack(Player p) {
        if (!isPlayerOwningBackpack(p)) {
            int size = 54;
            try {
                size = Integer.parseInt(backpacksConfig.getString("Options.BackPack_Default_Size"));
            } catch (NumberFormatException ignored) {
            }

            if (backpacksConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName") == null) {
                try {
                    backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
                    backpacksDataConfig = YamlConfiguration.loadConfiguration(backpacksFile);
                    backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".PlayerName", p.getName());
                    backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items.Size", size);
                    backpacksDataConfig.save(backpacksFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            updateCachedBackpack();
        }

        if (getBoolean(backpacksConfig.getString("Options.BackPack_Item_OnJoin"))) {
        	String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "backpack item" );
            Bukkit.dispatchCommand(p, registeredCmd);
        }
    }

    private void setDefaultBackpack(Player p, String id) {
        if (!isPlayerOwningBackpack(p)) {
            int size = 54;
            try {
                size = Integer.parseInt(backpacksConfig.getString("Options.BackPack_Default_Size"));
            } catch (NumberFormatException ignored) {
            }

            if (backpacksConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName") == null) {
                try {
                    backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
                    backpacksDataConfig = YamlConfiguration.loadConfiguration(backpacksFile);
                    backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".PlayerName", p.getName());
                    backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size", size);
                    backpacksDataConfig.save(backpacksFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            updateCachedBackpack();
        }

        if (getBoolean(backpacksConfig.getString("Options.BackPack_Item_OnJoin"))) {
            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "backpack item" );
            Bukkit.dispatchCommand(p, registeredCmd);
        }
    }

    private void resetBackpackMethod(Player p) {
        updateCachedBackpack();

        try {
            backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
            backpacksDataConfig.save(backpacksFile);
        } catch (IOException ex){
            ex.printStackTrace();
            return;
        }

        updateCachedBackpack();
    }

    private void resetBackpackMethod(Player p, String id) {
        updateCachedBackpack();

        try {
            backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items-" + id, null);
            backpacksDataConfig.save(backpacksFile);
        } catch (IOException ex){
            ex.printStackTrace();
            return;
        }

        updateCachedBackpack();
    }

    private void backpackResize(Player p, int size) {
        updateCachedBackpack();

        // Must be multiple of 9.
        if (size % 9 != 0 || size > 54){
            return;
        }

        updateCachedBackpack();

        backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items.Size", size);

        try {
            backpacksDataConfig.save(backpacksFile);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        updateCachedBackpack();
    }

    private void backpackResize(Player p, int size, String id) {
        updateCachedBackpack();

        // Must be multiple of 9.
        if (size % 9 != 0 || size > 54){
            return;
        }

        updateCachedBackpack();

        backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size", size);

        try {
            backpacksDataConfig.save(backpacksFile);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        updateCachedBackpack();
    }

    private int getSize(Player p) {
        updateCachedBackpack();

        int backPackSize = 0;

        try {
            backPackSize = Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items.Size"));
        } catch (NumberFormatException ignored){}
        return backPackSize;
    }

    private int getSize(Player p, String id) {
        updateCachedBackpack();

        int backPackSize = 0;

        try {
            backPackSize = Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size"));
        } catch (NumberFormatException ignored){}
        return backPackSize;
    }

    @NotNull
    private Inventory getBackpackOwn(Player p) {

        updateCachedBackpack();

        Inventory inv = Bukkit.createInventory(p, getBackpackSize(p), SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backpacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksDataConfig.getItemStack("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
                if (finalItem != null) {
                    inv.setItem(Integer.parseInt(slot), finalItem);
                }
            }
        }

        return inv;
    }

    @NotNull
    private Inventory getBackpackOwn(Player p, String id) {
        updateCachedBackpack();

        Inventory inv = Bukkit.createInventory(p, getBackpackSize(p, id), SpigotPrison.format("&3" + p.getName() + " -> Backpack-" + id));

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backpacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items-" + id).getKeys(false);
        } catch (NullPointerException ex){
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksDataConfig.getItemStack("Inventories. " + p.getUniqueId() + ".Items-" + id + "." + slot + ".ITEMSTACK");
                if (finalItem != null) {
                    inv.setItem(Integer.parseInt(slot), finalItem);
                }
            }
        }

        return inv;
    }

    private void openBackpackDefault(Player p) {
        playOpenBackpackSound(p);
        Inventory inv = getBackpack(p);
        p.openInventory(inv);
        if (!openBackpacks.contains(p.getName())){
            openBackpacks.add(p.getName());
        }
    }

    private void openBackpackByID(Player p, String id) {
        playOpenBackpackSound(p);
        Inventory inv = getBackpack(p, id);
        p.openInventory(inv);
        if (!openBackpacks.contains(p.getName())){
            openBackpacks.add(p.getName());
        }
    }

    private Inventory getBackpackCustom(Player p, Inventory inv) {
        // Get the Items config section
        Set<String> slots;
        try {
            slots = backpacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksDataConfig.getItemStack("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
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

    private void saveInventory(Player p, Inventory inv) {
        updateCachedBackpack();

        if (inv.getContents() != null){
            int slot = 0;

            try {
                backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }

            // Set dimensions if null or error.
            boolean needToSetNewDimensions = checkDimensionError(p);
            if (needToSetNewDimensions){
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items" + ".Size", backpacksConfig.getString("Options.BackPack_Default_Size"));
            }

            updateCachedBackpack();

            for (ItemStack item : inv.getContents()){
                if (item != null){

                    backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK", item);

                    slot++;
                }
            }
            if (slot != 0){
                try {
                    backpacksDataConfig.save(backpacksFile);
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        } else {
            // If it's null just delete the whole stored inventory.
            try {
                backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

        updateCachedBackpack();
    }

    private void saveInventory(Player p, Inventory inv, String id) {
        updateCachedBackpack();

        if (inv.getContents() != null){
            int slot = 0;

            try {
                backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items-" + id, null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }

            // Set dimensions if null or error.
            boolean needToSetNewDimensions = checkDimensionError(p, id);
            if (needToSetNewDimensions){
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size", backpacksConfig.getString("Options.BackPack_Default_Size"));
            }

            updateCachedBackpack();

            for (ItemStack item : inv.getContents()){
                if (item != null){

                    backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items-" + id + "." + slot + ".ITEMSTACK", item);

                    slot++;
                }
            }
            if (slot != 0){
                try {
                    backpacksDataConfig.save(backpacksFile);
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        } else {
            // If it's null just delete the whole stored inventory.
            try {
                backpacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items-" + id, null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

        updateCachedBackpack();
    }

    private HashMap<Integer, ItemStack> addItemToBackpack(Player p, ItemStack item) {
        Inventory inv = getBackpack(p);
        HashMap<Integer, ItemStack> overflow = inv.addItem(item);
        setInventory(p, inv);
        return overflow;
    }

    @NotNull
    private HashMap<Integer, ItemStack> addItemToBackpack(Player p, ItemStack item, String id) {
        Inventory inv = getBackpack(p, id);
        HashMap<Integer, ItemStack> overflow = inv.addItem(item);
        setInventory(p, inv, id);
        return overflow;
    }

    private HashMap<Integer, ItemStack> removeItemFromBackpack(Player p, ItemStack item) {
        Inventory inv = getBackpack(p);
        HashMap<Integer, ItemStack> underflow = inv.removeItem(item);
        setInventory(p, inv);
        return underflow;
    }

    @NotNull
    private HashMap<Integer, ItemStack> removeItemFromBackpak(Player p, ItemStack item, String id) {
        Inventory inv = getBackpack(p, id);
        HashMap<Integer, ItemStack> underflow = inv.removeItem(item);
        setInventory(p, inv, id);
        return underflow;
    }

    private void backPackItem(Player p) {

        if (!getBoolean(backpacksConfig.getString("Options.Back_Pack_GUI_Opener_Item"))) {
            return;
        }

        boolean playerHasItemBackPack = false;

        Inventory inv = p.getInventory();

        for (ItemStack item : inv.getContents()) {
            if (item != null){

                ItemStack materialConf = SpigotUtil.getXMaterial(backpacksConfig.getString("Options.BackPack_Item")).parseItem();

                if (materialConf != null && item.getType() == materialConf.getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotPrison.format(backpacksConfig.getString("Options.BackPack_Item_Title")))){
                    playerHasItemBackPack = true;
                }
            }
        }

        if (!playerHasItemBackPack){

            ItemStack item;

            List<String> itemLore = createLore(
                    messages.getString("Lore.ClickToOpenBackpack")
            );

            item = createButton(SpigotUtil.getXMaterial(backpacksConfig.getString("Options.BackPack_Item")).parseItem(), itemLore, backpacksConfig.getString("Options.BackPack_Item_Title"));

            if (item != null) {
                inv.addItem(item);
            }
        }
    }

    private void playCloseBackpackSoundToPlayer(Player p) {
        if (getBoolean(backpacksConfig.getString("Options.BackPack_Close_Sound_Enabled"))) {
            Sound sound = compat.getCloseChestSound();
            try {
                sound = Sound.valueOf(backpacksConfig.getString("Options.BackPack_Close_Sound"));
            } catch (IllegalArgumentException ignored) {}
            p.playSound(p.getLocation(), sound, 3, 1);
        }
    }

    private void playOpenBackpackSoundToPlayer(Player p) {
        if (getBoolean(backpacksConfig.getString("Options.BackPack_Open_Sound_Enabled"))){
            Sound sound = compat.getOpenChestSound();
            try{
                sound = Sound.valueOf(backpacksConfig.getString("Options.BackPack_Open_Sound"));
            } catch (IllegalArgumentException ignored){}
            p.playSound(p.getLocation(), sound,3, 1);
        }
    }

    @NotNull
    private List<String> getBackpacksIDsList(Player p) {
        List<String> backpacksIDs = new ArrayList<>();

        // Items
        //
        updateCachedBackpack();

        // Items can be -> Items- or just Items in the config, the default and old backpacks will have Items only, newer will be like
        // Items-1 or anyway an ID, I'm just getting the ID with this which's what I need.
        for (String key : backpacksDataConfig.getConfigurationSection("Inventories." + p.getUniqueId()).getKeys(false)){
            if (!key.equalsIgnoreCase("Items")){
                backpacksIDs.add(key.substring(6));
            } else {
                backpacksIDs.add(null);
            }
        }

        return backpacksIDs;
    }

    private int getNumberOfBackpacksOwnedByPlayer(Player p) {
        int backpacksNumber = 0;

        // Items
        //
        updateCachedBackpack();

        // Items can be -> Items- or just Items in the config, the default and old backpacks will have Items only, newer will be like
        // Items-1 or anyway an ID, I'm just getting the ID with this which's what I need.
        for (String key : backpacksDataConfig.getConfigurationSection("Inventories." + p.getUniqueId()).getKeys(false)){
            backpacksNumber++;
        }

        return backpacksNumber;
    }

    private boolean checkDimensionError(Player p) {
        boolean needToSetNewDimensions = false;
        try{
            if (backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items" + ".Size") == null){
                needToSetNewDimensions = true;
            }
            Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items" + ".Size"));
        } catch (NumberFormatException ex){
            needToSetNewDimensions = true;
        }
        return needToSetNewDimensions;
    }

    private boolean checkDimensionError(Player p, String id) {
        boolean needToSetNewDimensions = false;
        try{
            if (backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size") == null){
                needToSetNewDimensions = true;
            }
            Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size"));
        } catch (NumberFormatException ex){
            needToSetNewDimensions = true;
        }
        return needToSetNewDimensions;
    }

}

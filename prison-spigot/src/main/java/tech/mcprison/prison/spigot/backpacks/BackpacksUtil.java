package tech.mcprison.prison.spigot.backpacks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.gui.PrisonCoreGuiMessages;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.configs.BackpacksConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * @author GABRYCA
 * */
public class BackpacksUtil 
	extends PrisonCoreGuiMessages {

    private static BackpacksUtil instance;
    private Configuration backpacksConfig = SpigotPrison.getInstance().getBackpacksConfig();
    private File backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml");
    private FileConfiguration backpacksDataConfig = YamlConfiguration.loadConfiguration(backpacksFile);
    public static List<String> openBackpacks = new ArrayList<>();
    public static List<String> backpackEdited = new ArrayList<>();
    private final Compatibility compat = SpigotCompatibility.getInstance();
    private final int backpackDefaultSize = Integer.parseInt(backpacksConfig.getString("Options.BackPack_Default_Size"));

    /**
     * Check if Backpacks's enabled.
     * */
    public static boolean isEnabled(){
        if (SpigotPrison.getInstance().getConfig().getString("backpacks") != null){
            return SpigotPrison.getInstance().getConfig().getString("backpacks").equalsIgnoreCase("true");
        }
        return false;
    }

    /**
     * Get Backpacks DATA config.
     * */
    public Configuration getBackpacksData(){
        updateCachedBackpack();
        return backpacksDataConfig;
    }

    /**
     * Check if multiple Backpacks's enabled.
     * */
    public boolean isMultipleBackpacksEnabled(){
        return getBoolean(backpacksConfig.getString("Options.Multiple-BackPacks-For-Player-Enabled"));
    }

//    private void backpacksConfigUpdater(){
//        backpackConfigUpdater();
//    }

    public void removeFromOpenBackpacks(Player p){
        openBackpacks.remove(p.getName());
    }

    public void updateCachedBackpack(){
        backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml");
        backpacksDataConfig = YamlConfiguration.loadConfiguration(backpacksFile);
    }

    /**
     * Check if player reached limit of own backpacks.
     *
     * @return boolean - True if reached, false if not.
     * */
    public boolean reachedBackpacksLimit(Player p){
        return getBoolean(backpacksConfig.getString("Options.Multiple-BackPacks-For-Player-Enabled")) && (getBackpacksLimit(p) <= getNumberOwnedBackpacks(p));
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
     * Get Backpack OfflinePlayer Player by name.
     *
     * @param name - PlayerName.
     *
     * @return OfflinePlayer
     * */
    public OfflinePlayer getBackpackOwnerOffline(String name){
        return getOfflinePlayer(name);
    }

    /**
     * Get Backpack owner OfflinePlayer by name and backpack ID.
     *
     * @param name - PlayerName
     * @param id - InventoryID
     *
     * @return OfflinePlayer
     * */
    public OfflinePlayer getBackpackOwnerOffline(String name, String id){
        return getOfflinePlayer(name, id);
    }

    /**
     * Get Backpack Player by name.
     *
     * @param name - Playername
     *
     * @return Player
     * */
    public Player getBackpackOwnerOnline(String name){
        return getOnlinePlayer(name);
    }

    /**
     * Get Backpack Player by name and Backpack id.
     *
     * @param name - Playername
     * @param id - BackpackID
     *
     * @return Player
     * */
    public Player getBackpackOwnerOnline(String name, String id){
        return getOnlinePlayer(name, id);
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
     * Check if player owns backpack for single backpack mode (default).
     *
     * @param p - Player
     *
     * @return boolean - true/false
     * */
    public boolean isPlayerOwningBackpack(Player p){
        return checkOwnBackpack(p);
    }

    /**
     * Check if player owns backpack for multiple backpacks mode.
     *
     * @param p - Player
     *
     * @return boolean - true or false
     * */
    public boolean isPlayerOwningBackpacksMultipleMode(Player p){
        return checkOwnBackpackMultiples(p);
    }



    /**
     * Set default backpack config file.
     * */
    public void setDefaultDataConfig() {
        setDefaultBackpackDataConfigMethod();
    }

    /**
     * Set default backpack config file.
     *
     * @param p - Player
     * */
    public void giveBackpackItem(Player p) {
        giveBackpackToPlayerOnJoinItem(p);
    }

    /**
     * Reset a player's backpack.
     *
     * @param p - Player
     *
     * @return success - true or false
     * */
    public boolean resetBackpack(Player p) {
        return resetBackpackMethod(p);
    }

    /**
     * Reset a player's backpack by ID.
     *
     * @param p - Player
     * @param id - String ID
     *
     * @return success - true or false
     * */
    public boolean resetBackpack(Player p, String id){
        return resetBackpackMethod(p, id);
    }

    /**
     * Reset a player's backpack.
     *
     * @param p - OfflinePlayer
     *
     * @return success - true or false
     * */
    public boolean resetBackpack(OfflinePlayer p) {
        return resetBackpackMethod(p);
    }

    /**
     * Reset a player's backpack by ID.
     *
     * @param p - OfflinePlayer
     * @param id - String ID
     *
     * @return success - true or false
     * */
    public boolean resetBackpack(OfflinePlayer p, String id){
        return resetBackpackMethod(p, id);
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
     * Set Player Backpacks max dimensions.
     * NOTE: Should be a multiple of 9.
     * Max dimension for now's 54.
     *
     * @param p - OfflinePlayer
     * @param size - Size
     *
     * */
    public void setBackpackSize(OfflinePlayer p, int size){
        backpackResize(p, size);
    }

    /**
     * Set Backpack size with ID.
     * NOTE: Must be a multiple of 9.
     * Max dimension's 54, minimum 9.
     *
     * @param p - Player
     * @param size - Size
     * @param id - ID
     * */
    public void setBackpackSize(Player p, int size, String id){
        backpackResize(p, size, id);
    }

    /**
     * Set Backpack size with ID.
     * NOTE: Must be a multiple of 9.
     * Max dimension's 54, minimum 9.
     *
     * @param p - OfflinePlayer
     * @param size - Size
     * @param id - ID
     * */
    public void setBackpackSize(OfflinePlayer p, int size, String id){
        backpackResize(p, size, id);
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
        openBackpackMethod(p);
    }

    /**
     * Open backpack by ID.
     *
     * @param p - Player
     * @param id - String ID
     * */
    public void openBackpack(Player p, String id){
        openBackpackMethod(p, id);
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
        return removeItemFromBackpack(p, item, id);
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
     * Set limit of backpacks that a user can own.
     *
     * @param p - Player
     * @param limit - Integer limit.
     *
     * @return success - true if success, false if error.
     * */
    public boolean setBackpacksLimit(Player p, int limit){
        return backpacksLimitSet(p, limit);
    }

    /**
     * Set limit of backpacks that a user can own.
     *
     * @param p - OfflinePlayer
     * @param limit - Integer limit.
     *
     * @return success - true if success, false if error.
     * */
    public boolean setBackpacksLimit(OfflinePlayer p, int limit){
        return backpacksLimitSet(p, limit);
    }

    /**
     * Get Backpacks Limit of a Player
     *
     * @param p - Player
     *
     * @return limit - Integer.
     * */
    public int getBackpacksLimit(Player p){
        return backpacksLimitGet(p);
    }

    /**
     * Get Backpacks Limit of a Player
     *
     * @param p - OfflinePlayer
     *
     * @return limit - Integer.
     * */
    public int getBackpacksLimit(OfflinePlayer p){
        return backpacksLimitGet(p);
    }


    /**
     * Check if a Player can own a Backpack depending on limit and config options even if the limit is set to 0.
     * Backpacksconfig option -> Options.BackPack_Access_And_Item_If_Limit_Is_0 is false by default, so a player can't own
     * a backpack if the limit is 0 and won't get the item to open one, also he won't have access to the /gui backpack command.
     *
     * @param p - Player.
     *
     * @return boolean
     * */
    public boolean canOwnBackpack(Player p){
        return playerCanOwnBackpack(p);
    }

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){
        return string != null && string.equalsIgnoreCase("true");
    }

    /**
     * Create a Lore for an Item in the GUI.
     *
     * @param lores
     * */
    private List<String> createLore(String... lores) {
        List<String> results = new ArrayList<>();
        for (String lore : lores) {
            results.add( SpigotPrison.format(lore));
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

    private int backpacksLimitGet(OfflinePlayer p) {
        updateCachedBackpack();

        // If the value if null, for example from an older version of Prison, this will read the default value from the
        // backpacksconfig.yml and set it.
        if (backpacksConfig.getString("Inventories." + p.getUniqueId() + ".Limit") == null){
            setBackpacksLimit(p, Integer.parseInt(backpacksConfig.getString("Options.Multiple-BackPacks-For-Player")));
        }

        return backpacksConfig.getInt("Inventories." + p.getUniqueId() + ".Limit");
    }

    private int backpacksLimitGet(Player p) {
        updateCachedBackpack();

        // If the value if null, for example from an older version of Prison, this will read the default value from the
        // backpacksconfig.yml and set it.
        if (backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Limit") == null){
            setBackpacksLimit(p, Integer.parseInt(backpacksConfig.getString("Options.Multiple-BackPacks-For-Player")));
        }

        return Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Limit"));
    }

    private boolean backpacksLimitSet(Player p, int limit) {
        updateCachedBackpack();

        try{
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Limit", limit);
            backpacksDataConfig.save(backpacksFile);
        } catch (IOException ex){
            return false;
        }

        updateCachedBackpack();
        return true;
    }

    private boolean backpacksLimitSet(OfflinePlayer p, int limit) {
        updateCachedBackpack();

        try {
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Limit", limit);
            backpacksDataConfig.save(backpacksFile);
        } catch (IOException ex) {
            return false;
        }

        updateCachedBackpack();
        return true;
    }

    private void backpackConfigUpdater() {
        BackpacksConfig bpTemp = new BackpacksConfig();
        bpTemp.initialize();
        backpacksConfig = bpTemp.getFileBackpacksConfig();
    }

    private static BackpacksUtil getInstance() {
        if (instance == null && SpigotPrison.getInstance().getConfig().getString("backpacks") != null && SpigotPrison.getInstance().getConfig().getString("backpacks").equalsIgnoreCase("true")){
            instance = new BackpacksUtil();
        }

        return instance;
    }

    private boolean checkOwnBackpack(Player p) {
        updateCachedBackpack();

        String playerName = backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName");
        return playerName != null;
    }

    private boolean checkOwnBackpackMultiples(Player p){
        updateCachedBackpack();

        return getNumberOwnedBackpacks(p) != 0;
    }

    private void setDefaultBackpackDataConfigMethod() {
        if (backpacksConfig.getConfigurationSection("Inventories") == null) {
            try {
                backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml");
                backpacksDataConfig = YamlConfiguration.loadConfiguration(backpacksFile);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }
        updateCachedBackpack();
    }

    private void giveBackpackToPlayerOnJoinItem(Player p) {
        if (getBoolean(backpacksConfig.getString("Options.BackPack_Item_OnJoin")) && playerCanOwnBackpack(p)) {
            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "backpack item" );
            Bukkit.dispatchCommand(p, registeredCmd);
        }
    }

    private boolean playerCanOwnBackpack(Player p){
        return !(!getBoolean(backpacksConfig.getString("Options.BackPack_Access_And_Item_If_Limit_Is_0")) && getBackpacksLimit(p) == 0);
    }

    private boolean resetBackpackMethod(Player p) {
        updateCachedBackpack();

        try {
            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items", null);
                backpacksDataConfig.save(backpacksFile);
            } catch (NullPointerException ex){
                return false;
            }
        } catch (IOException ex){
            ex.printStackTrace();
            return false;
        }

        updateCachedBackpack();
        return true;
    }

    private boolean resetBackpackMethod(Player p, String id) {
        updateCachedBackpack();

        try {
            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id, null);
                backpacksDataConfig.save(backpacksFile);
            } catch (NullPointerException ex){
                return false;
            }
        } catch (IOException ex){
            ex.printStackTrace();
            return false;
        }

        updateCachedBackpack();
        return true;
    }

    private boolean resetBackpackMethod(OfflinePlayer p) {
        updateCachedBackpack();

        try {
            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items", null);
                backpacksDataConfig.save(backpacksFile);
            } catch (NullPointerException ex){
                return false;
            }
        } catch (IOException ex){
            ex.printStackTrace();
            return false;
        }

        updateCachedBackpack();
        return true;
    }

    private boolean resetBackpackMethod(OfflinePlayer p, String id) {
        updateCachedBackpack();

        try {
            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id, null);
                backpacksDataConfig.save(backpacksFile);
            } catch (NullPointerException ex){
                return false;
            }
        } catch (IOException ex){
            ex.printStackTrace();
            return false;
        }

        updateCachedBackpack();
        return true;
    }

    private void backpackResize(Player p, int size) {
        updateCachedBackpack();

        // Must be multiple of 9.
        if ((size % 9 != 0 || size > 54) && size != 0){
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

    private void backpackResize(OfflinePlayer p, int size) {
        updateCachedBackpack();

        // Must be multiple of 9.
        if ((size % 9 != 0 || size > 54) && size != 0){
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

    private void backpackResize(OfflinePlayer p, int size, String id) {
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

        int backPackSize = backpackDefaultSize;

        try {
            backPackSize = Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items.Size"));
        } catch (NumberFormatException ignored){}

        if (backPackSize % 9 != 0){
            backPackSize = (int) Math.ceil((float)backPackSize / 9) * 9;
        }

        if (backPackSize == 0) backPackSize = 9;

        return getBackpackPermSize(p, backPackSize);
    }

    private int getSize(Player p, String id) {
        updateCachedBackpack();

        int backPackSize = backpackDefaultSize;

        try {
            backPackSize = Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size"));
        } catch (NumberFormatException ignored){}

        if (backPackSize % 9 != 0){
            backPackSize = (int) Math.ceil((float)backPackSize / 9) * 9;
        }

        if (backPackSize == 0) backPackSize = 9;

        return getBackpackPermSize(p, backPackSize);
    }

    private int getBackpackPermSize(Player p, int backPackSize) {
        SpigotPlayer sPlayer = new SpigotPlayer(p);
        List<String> perms = sPlayer.getPermissions("prison.backpack.size.");
        int value = 0;
        for (String permNumber : perms){
            int newValue = Integer.parseInt(permNumber.substring(21));
            if (newValue > value){
                value = (int) Math.ceil((float)newValue / 9) * 9;
            }
        }

        if (value != 0){
            return value;
        }
        return backPackSize;
    }

    private Inventory getBackpackOwn(Player p) {

        updateCachedBackpack();

        int size = getBackpackSize(p);
        Inventory inv = Bukkit.createInventory(p, size, SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backpacksDataConfig.getConfigurationSection("Inventories." + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksDataConfig.getItemStack("Inventories." + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
                if (finalItem != null) {
                    int slotNumber = Integer.parseInt(slot);
                    if (size > slotNumber) {
                        inv.setItem(slotNumber, finalItem);
                    }
                }
            }
        }

        return inv;
    }

    private Inventory getBackpackOwn(Player p, String id) {
        updateCachedBackpack();

        int size = getBackpackSize(p, id);
        Inventory inv = Bukkit.createInventory(p, size, SpigotPrison.format("&3" + p.getName() + " -> Backpack-" + id));

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backpacksDataConfig.getConfigurationSection("Inventories." + p.getUniqueId() + ".Items-" + id).getKeys(false);
        } catch (NullPointerException ex){
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksDataConfig.getItemStack("Inventories." + p.getUniqueId() + ".Items-" + id + "." + slot + ".ITEMSTACK");
                if (finalItem != null) {
                    int slotNumber = Integer.parseInt(slot);
                    if (size > slotNumber) {
                        inv.setItem(slotNumber, finalItem);
                    }
                }
            }
        }

        return inv;
    }

    private void openBackpackMethod(Player p) {
        playOpenBackpackSound(p);
        Inventory inv = getBackpack(p);
        p.openInventory(inv);
        if (!openBackpacks.contains(p.getName())){
            openBackpacks.add(p.getName());
        }
    }

    private void openBackpackMethod(Player p, String id) {
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
            slots = backpacksDataConfig.getConfigurationSection("Inventories." + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksDataConfig.getItemStack("Inventories." + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
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

        // Set dimensions if null or error.
        boolean needToSetNewDimensions = checkDimensionError(p);
        boolean needToSetNewOwner = checkBackpackOwnerMissing(p);
        boolean needToSetNewOwnerID = checkBackpackOwnerIDMissing(p);

        if (inv.getContents() != null){

            int backpackSize = getBackpackSize(p);
            int slot = 0;

            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items", null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }

            updateCachedBackpack();

            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items.Size", backpackSize);

            for (ItemStack item : inv.getContents()){
                if (item != null){

                    backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK", item);

                    slot++;
                }
            }

            oldDataVersionUpdater(p, false, true, true);

            try {
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        } else {
            // If it's null just delete the whole stored inventory.
            oldDataVersionUpdater(p, needToSetNewDimensions, needToSetNewOwner, needToSetNewOwnerID);
            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId().toString() + ".Items", null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

        updateCachedBackpack();
    }

    private void saveInventory(Player p, Inventory inv, String id) {
        updateCachedBackpack();

        // Set dimensions if null or error.
        boolean needToSetNewDimensions = checkDimensionError(p, id);
        boolean needToSetNewOwner = checkBackpackOwnerMissing(p, id);
        boolean needToSetNewOwnerID = checkBackpackOwnerIDMissing(p, id);

        if (inv.getContents() != null){

            int backpackSize = getBackpackSize(p, id);
            int slot = 0;

            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id, null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }

            updateCachedBackpack();

            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size", backpackSize);

            oldDataVersionUpdater(p, id, false, true, true);

            for (ItemStack item : inv.getContents()){
                if (item != null){

                    backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id + "." + slot + ".ITEMSTACK", item);

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
            oldDataVersionUpdater(p, id, needToSetNewDimensions, needToSetNewOwner, needToSetNewOwnerID);
            try {
                backpacksDataConfig.set("Inventories." + p.getUniqueId().toString() + ".Items-" + id, null);
                backpacksDataConfig.save(backpacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

        updateCachedBackpack();
    }

    private void oldDataVersionUpdater(Player p, boolean needToSetNewDimensions, boolean needToSetNewOwner, boolean needToSetNewOwnerID) {
        if (needToSetNewDimensions){
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items.Size", Integer.parseInt(backpacksConfig.getString("Options.BackPack_Default_Size")));
        }
        if (needToSetNewOwner){
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".PlayerName", p.getName());
        }
        if (needToSetNewOwnerID){
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".UniqueID", p.getUniqueId().toString());
        }
    }

    private void oldDataVersionUpdater(Player p, String id, boolean needToSetNewDimensions, boolean needToSetNewOwner, boolean needToSetNewOwnerID) {
        if (needToSetNewDimensions){
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size", Integer.parseInt(backpacksConfig.getString("Options.BackPack_Default_Size")));
        }
        if (needToSetNewOwner){
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".PlayerName", p.getName());
        }
        if (needToSetNewOwnerID){
            backpacksDataConfig.set("Inventories." + p.getUniqueId() + ".UniqueID", p.getUniqueId().toString());
        }
    }

    private HashMap<Integer, ItemStack> addItemToBackpack(Player p, ItemStack item) {
        Inventory inv = getBackpack(p);
        HashMap<Integer, ItemStack> overflow = inv.addItem(item);
        setInventory(p, inv);
        return overflow;
    }

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

    private HashMap<Integer, ItemStack> removeItemFromBackpack(Player p, ItemStack item, String id) {
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
        ItemStack materialConf = SpigotUtil.getXMaterial(backpacksConfig.getString("Options.BackPack_Item")).parseItem();

        for (ItemStack item : inv.getContents()) {
            if (item != null){

                if (materialConf != null && item.getType() == materialConf.getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotPrison.format(backpacksConfig.getString("Options.BackPack_Item_Title")))){
                    playerHasItemBackPack = true;
                }
            }
        }

        if (!playerHasItemBackPack){

            ItemStack item;

            List<String> itemLore = createLore(
            		guiClickToOpenMsg()
            );

            item = createButton(SpigotUtil.getXMaterial(backpacksConfig.getString("Options.BackPack_Item")).parseItem(), itemLore, backpacksConfig.getString("Options.BackPack_Item_Title"));

            if (item != null) {
                inv.addItem(item);
                p.updateInventory();
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

    private List<String> getBackpacksIDsList(Player p) {
        List<String> backpacksIDs = new ArrayList<>();

        // Items
        //
        updateCachedBackpack();

        // Items can be -> Items- or just Items in the config, the default and old backpacks will have Items only, newer will be like
        // Items-1 or anyway an ID, I'm just getting the ID with this which's what I need.
        try {
            for (String key : backpacksDataConfig.getConfigurationSection("Inventories." + p.getUniqueId()).getKeys(false)) {
                if (!key.equalsIgnoreCase("Items") && !key.equalsIgnoreCase("Limit") && !key.equalsIgnoreCase("PlayerName") && !key.equalsIgnoreCase("UniqueID")) {
                    backpacksIDs.add(key.substring(6));
                } else {
                    if (!backpacksIDs.contains(null) && !key.equalsIgnoreCase("PlayerName") && !key.equalsIgnoreCase("UniqueID") && !key.equalsIgnoreCase("Limit")) {
                        backpacksIDs.add(null);
                    }
                }
            }
        } catch (NullPointerException ignored){}

        return backpacksIDs;
    }

    private int getNumberOfBackpacksOwnedByPlayer(Player p) {
        int backpacksNumber = 0;

        updateCachedBackpack();

        // Items can be -> Items- or just Items in the config, the default and old backpacks will have Items only, newer will be like
        // Items-1 or anyway an ID, I'm just getting the ID with this which's what I need.
        try {
            for (String string : backpacksDataConfig.getConfigurationSection("Inventories." + p.getUniqueId()).getKeys(false)) {
                if (!string.equalsIgnoreCase("PlayerName") && !string.equalsIgnoreCase("Limit") && !string.equalsIgnoreCase("UniqueID")) {
                    backpacksNumber++;
                }
            }
        } catch (NullPointerException ignored){}

        return backpacksNumber;
    }

    private boolean checkDimensionError(Player p) {
        try{
            if (backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items" + ".Size") == null){
                return true;
            }
            Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items" + ".Size"));
        } catch (NumberFormatException ex){
            return true;
        }
        return false;
    }

    private boolean checkDimensionError(Player p, String id) {
        try{
            if (backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size") == null){
                return true;
            }
            Integer.parseInt(backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size"));
        } catch (NumberFormatException ex){
            return true;
        }
        return false;
    }

    private boolean checkBackpackOwnerMissing(Player p) {
        return backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName") == null;
    }

    private boolean checkBackpackOwnerMissing(Player p, String id) {
        return backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName") == null;
    }

    private boolean checkBackpackOwnerIDMissing(Player p) {
        return backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".UniqueID") == null;
    }

    private boolean checkBackpackOwnerIDMissing(Player p, String id) {
        return backpacksDataConfig.getString("Inventories." + p.getUniqueId() + ".UniqueID") == null;
    }

    private OfflinePlayer getOfflinePlayer(String name) {
        if (name != null) {
            updateCachedBackpack();
            if (backpacksDataConfig.getConfigurationSection("Inventories") != null) {
                for (String uniqueID : backpacksDataConfig.getConfigurationSection("Inventories").getKeys(false)) {
                    if (backpacksDataConfig.getString("Inventories." + uniqueID + ".PlayerName").equalsIgnoreCase(name) && backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID") != null) {
                        return Bukkit.getOfflinePlayer(UUID.fromString(backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID")));
                    }
                }
            }
        }
        return null;
    }

    private OfflinePlayer getOfflinePlayer(String name, String id) {
        if (name != null) {
            updateCachedBackpack();
            if (backpacksDataConfig.getConfigurationSection("Inventories") != null) {
                for (String uniqueID : backpacksDataConfig.getConfigurationSection("Inventories").getKeys(false)) {
                    if (backpacksDataConfig.getString("Inventories." + uniqueID + ".PlayerName") != null && backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID") != null){
                        if (backpacksDataConfig.getString("Inventories." + uniqueID + ".PlayerName").equalsIgnoreCase(name)){
                            return Bukkit.getOfflinePlayer(UUID.fromString(backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID")));
                        }
                    }
                }
            }
        }
        return null;
    }

    private Player getOnlinePlayer(String name) {
        if (name != null) {
            updateCachedBackpack();
            if (backpacksDataConfig.getConfigurationSection("Inventories") != null) {
                for (String uniqueID : backpacksDataConfig.getConfigurationSection("Inventories").getKeys(false)) {
                    if (backpacksDataConfig.getString("Inventories." + uniqueID + ".PlayerName").equalsIgnoreCase(name) && backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID") != null) {
                        return Bukkit.getPlayer(UUID.fromString(backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID")));
                    }
                }
            }
        }
        return null;
    }

    private Player getOnlinePlayer(String name, String id) {
        if (name != null) {
            updateCachedBackpack();
            if (backpacksDataConfig.getConfigurationSection("Inventories") != null) {
                for (String uniqueID : backpacksDataConfig.getConfigurationSection("Inventories").getKeys(false)) {
                    if (backpacksDataConfig.getString("Inventories." + uniqueID + ".PlayerName") != null && backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID") != null){
                        if (backpacksDataConfig.getString("Inventories." + uniqueID + ".PlayerName").equalsIgnoreCase(name)){
                            return Bukkit.getPlayer(UUID.fromString(backpacksDataConfig.getString("Inventories." + uniqueID + ".UniqueID")));
                        }
                    }
                }
            }
        }
        return null;
    }
}

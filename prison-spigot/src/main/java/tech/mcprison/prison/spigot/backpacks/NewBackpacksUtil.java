package tech.mcprison.prison.spigot.backpacks;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.inventory.SpigotInventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author AnonymousGCA (GABRYCA)
 */
public class NewBackpacksUtil {

    private static NewBackpacksUtil instance;
    private MessagesConfig messages;
    private Configuration backpacksConfig;
    private Configuration backpacksData;
    private boolean isFoundDeprecatedData;
    private boolean isBackpackUsePermissionEnabled;
    private boolean isBackpackAutopickupEnabled;
    private boolean isBackpackOpenItemEnabled;
    private boolean isBackpackOpenItemGivenOnJoin;
    private boolean isBackpackEnabledIfLimitZero;
    private boolean isBackpackLostOnDeath;
    private boolean isBackpackOpenSoundEnabled;
    private boolean isBackpackCloseSoundEnabled;
    private boolean isMultipleBackpacksEnabled;
    private String backpackUsePermission;
    private String backpackOpenItemTitle;
    private XSound backpackOpenSound = XSound.BLOCK_CHEST_OPEN;
    private XSound backpackCloseSound = XSound.BLOCK_CHEST_CLOSE;
    private int defaultBackpackSize;
    private int defaultBackpackLimitForPlayer;
    private XMaterial backpackOpenItem = XMaterial.CHEST;


    /**
     * Get an instance of Backpacks to get full access to the API.
     * <p>
     * If Backpacks are disabled, this will return null.
     *
     * @return BackpacksUtil
     */
    public NewBackpacksUtil get() {

        if (!getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks"))) {
            return null;
        }

        if (instance == null) {
            instance = new NewBackpacksUtil();
            instance.initCachedData();
        }

        return instance;
    }

    /**
     * Return boolean value from String.
     *
     * @param string - Boolean string.
     * @return boolean.
     */
    public static boolean getBoolean(String string) {
        return string != null && string.equalsIgnoreCase("true");
    }

    /**
     * Init options that will be cached.
     */
    private void initCachedData() {
        messages = SpigotPrison.getInstance().getMessagesConfig();
        backpacksConfig = SpigotPrison.getInstance().getBackpacksConfig();
        if (new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml").exists()) {
            backpacksData = YamlConfiguration.loadConfiguration(new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml"));
            isFoundDeprecatedData = true;
        } else {
            isFoundDeprecatedData = false;
        }
        isBackpackUsePermissionEnabled = getBoolean(backpacksConfig.getString("Options.BackPack_Use_Permission_Enabled"));
        isBackpackAutopickupEnabled = getBoolean(backpacksConfig.getString("Options.BackPack_AutoPickup_Usable"));
        isBackpackOpenItemEnabled = getBoolean(backpacksConfig.getString("Options.Back_Pack_GUI_Opener_Item"));
        isBackpackOpenItemGivenOnJoin = getBoolean(backpacksConfig.getString("Options.BackPack_Item_OnJoin"));
        isBackpackEnabledIfLimitZero = getBoolean(backpacksConfig.getString("Options.BackPack_Access_And_Item_If_Limit_Is_0"));
        isBackpackLostOnDeath = getBoolean(backpacksConfig.getString("Options.BackPack_Lose_Items_On_Death"));
        isBackpackOpenSoundEnabled = getBoolean(backpacksConfig.getString("Options.BackPack_Open_Sound_Enabled"));
        isBackpackCloseSoundEnabled = getBoolean(backpacksConfig.getString("Options.BackPack_Close_Sound_Enabled"));
        isMultipleBackpacksEnabled = getBoolean(backpacksConfig.getString("Options.Multiple-BackPacks-For-Player-Enabled"));
        if (XSound.matchXSound(backpacksConfig.getString("Options.BackPack_Open_Sound")).isPresent()) {
            backpackOpenSound = XSound.matchXSound(backpacksConfig.getString("Options.BackPack_Open_Sound")).get();
        }
        if (XSound.matchXSound(backpacksConfig.getString("Options.BackPack_Close_Sound")).isPresent()) {
            backpackCloseSound = XSound.matchXSound(backpacksConfig.getString("Options.BackPack_Close_Sound")).get();
        }
        if (XMaterial.matchXMaterial(backpacksConfig.getString("Options.BackPack_Item")).isPresent()) {
            backpackOpenItem = XMaterial.matchXMaterial(backpacksConfig.getString("Options.BackPack_Item")).get();
        }
        backpackUsePermission = backpacksConfig.getString("Options.BackPack_Use_Permission");
        backpackOpenItemTitle = backpacksConfig.getString("Options.BackPack_Item_Title");
        defaultBackpackSize = Integer.parseInt(backpacksConfig.getString("Options.BackPack_Default_Size"));
        defaultBackpackLimitForPlayer = Integer.parseInt(backpacksConfig.getString("Options.Multiple-BackPacks-For-Player"));
    }

    /**
     * Save a Player's Backpack.
     *
     * @param p - Player.
     * @param inv - Backpack.
     */
    public void setBackpack(Player p, Inventory inv, int id) {

        //TODO
        // Add conditions here to check if Player can own backpacks.
        // This may also be a method with a boolean return value.

        PlayerCachePlayerData pData = PlayerCache.getInstance().getOnlinePlayer(new SpigotPlayer(p));

        if (pData == null) {
            Output.get().sendInfo(new SpigotPlayer(p), "Sorry, unable to find cached data on you, this may be a bug! Please report it.");
            return;
        }

        List<Inventory> inventories = prisonInventoryToNormalConverter(pData.getBackpacks());

        if (inventories.get(id) != null){
            // No need to check more conditions here, Player already owns a backpack here.
            inventories.set(id, inv);

            pData.setBackpacks(normalInventoryToPrisonConverter(inventories));
        } else {

            //TODO
            // Add conditions to check if player can own a new backpack, maybe he reached the limit.
            // This may be also a method with a boolean return value.

            inventories.add(inv);

            pData.setBackpacks(normalInventoryToPrisonConverter(inventories));
        }
    }

    /**
     * Get a Player's Backpack, there may be multiple ones.
     *
     * @param p  - Player.
     * @param id - int.
     * @return Inventory - Backpack.
     */
    public Inventory getBackpack(Player p, int id) {

        PlayerCachePlayerData pData = PlayerCache.getInstance().getOnlinePlayer(new SpigotPlayer(p));

        if (pData == null) {
            return null;
        }

        oldBackpacksConverter(p);

        if (pData.getBackpacks() == null || pData.getBackpacks().get(id) == null) {
            return null;
        }
        SpigotInventory sInv = (SpigotInventory) pData.getBackpacks().get(id);

        return sInv.getWrapper();
    }

    /**
     * Get Backpacks of a Player in a List of Inventories.
     *
     * @param p - Player.
     *
     * @return Inventory - List.
     */
    public List<Inventory> getBackpacks(Player p) {

        PlayerCachePlayerData pData = PlayerCache.getInstance().getOnlinePlayer(new SpigotPlayer(p));

        if (pData == null) {
            return null;
        }

        oldBackpacksConverter(p);

        if (pData.getBackpacks() == null) {
            return null;
        }

        return prisonInventoryToNormalConverter(pData.getBackpacks());
    }

    /**
     * Converts a Prison's Inventory List to the spigot standard one.
     *
     * @param pInv - PrisonInventory List.
     *
     * @return List - Inventory.
     * */
    private List<Inventory> prisonInventoryToNormalConverter(List<tech.mcprison.prison.internal.inventory.Inventory> pInv) {
        List<Inventory> inventories = new ArrayList<>();
        for (tech.mcprison.prison.internal.inventory.Inventory pInvRead : pInv) {
            SpigotInventory sInv = (SpigotInventory) pInvRead;
            inventories.add(sInv.getWrapper());
        }

        return inventories;
    }

    /**
     * Converts a Spigot's normal Inventory List to the Prison one.
     *
     * @param inventories - Inventories.
     *
     * @return List - Prison Inventories.
     * */
    private List<tech.mcprison.prison.internal.inventory.Inventory> normalInventoryToPrisonConverter(List<Inventory> inventories) {
        List<tech.mcprison.prison.internal.inventory.Inventory> pInv = new ArrayList<>();
        for (Inventory readInv : inventories){
            pInv.add(SpigotInventory.fromWrapper(readInv));
        }
        return pInv;
    }

    /**
     * Converts old Backpacks storage to the new one, deleting the deprecated one for this player.
     *
     * @param p - Player.
     */
    private void oldBackpacksConverter(Player p) {
        if (isFoundDeprecatedData) {
            if (backpacksData.getString("Inventories." + p.getUniqueId() + ".PlayerName") != null) {

                PlayerCachePlayerData pData = PlayerCache.getInstance().getOnlinePlayer(new SpigotPlayer(p));

                List<tech.mcprison.prison.internal.inventory.Inventory> prisonBackpacks = pData.getBackpacks();
                List<Inventory> readBackpacks = new ArrayList<>();

                Output.get().sendWarn(new SpigotPlayer(p), "The Backpack data got updated, conversion started...");
                File backpacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksData.yml");
                FileConfiguration backpacksFileData = YamlConfiguration.loadConfiguration(backpacksFile);

                // Check if multiple backpacks is enabled and get the backpacks.
                if (isMultipleBackpacksEnabled) {
                    for (String backpackIds : getBackpacksIDsList(p)) {
                        readBackpacks.add(getOldBackpackOwn(p, backpackIds));
                    }
                } else {
                    readBackpacks.add(getOldBackpackOwn(p));
                }

                // Set to null this data and save it in the old config.
                backpacksFileData.set("Inventories." + p.getUniqueId(), null);
                try {
                    backpacksFileData.save(backpacksFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int numberConverted = 0;
                for (Inventory inv : readBackpacks) {
                    numberConverted++;
                    prisonBackpacks.add(SpigotInventory.fromWrapper(inv));
                }

                pData.setBackpacks(prisonBackpacks);
                pData.isDirty();
                Output.get().sendInfo(new SpigotPlayer(p), numberConverted + " Backpacks converted with success, will open soon...");
            }
        }
    }

    /**
     * This method exists only for the conversion of the old deprecated one.
     */
    @Deprecated
    private Inventory getOldBackpackOwn(Player p) {

        int size = getOldSize(p);
        Inventory inv = Bukkit.createInventory(p, size, SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        // Get the Items config section.
        Set<String> slots;
        try {
            slots = backpacksData.getConfigurationSection("Inventories." + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex) {
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksData.getItemStack("Inventories." + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
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

    /**
     * This method exists only for the conversion of the old deprecated one.
     */
    @Deprecated
    private Inventory getOldBackpackOwn(Player p, String id) {

        int size = getOldSize(p, id);
        Inventory inv = Bukkit.createInventory(p, size, SpigotPrison.format("&3" + p.getName() + " -> Backpack-" + id));

        // Get the Items config section
        Set<String> slots;
        try {
            slots = backpacksData.getConfigurationSection("Inventories." + p.getUniqueId() + ".Items-" + id).getKeys(false);
        } catch (NullPointerException ex) {
            return inv;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backpacksData.getItemStack("Inventories." + p.getUniqueId() + ".Items-" + id + "." + slot + ".ITEMSTACK");
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

    /**
     * Don't use this method.
     */
    @Deprecated
    private int getOldSize(Player p) {

        int backPackSize = defaultBackpackSize;

        try {
            backPackSize = Integer.parseInt(backpacksData.getString("Inventories." + p.getUniqueId() + ".Items.Size"));
        } catch (NumberFormatException ignored) {
        }

        if (backPackSize % 9 != 0) {
            backPackSize = (int) Math.ceil((float) backPackSize / 9) * 9;
        }

        if (backPackSize == 0) backPackSize = 9;

        return getBackpackPermSize(p, backPackSize);
    }

    /**
     * Don't use this method.
     */
    @Deprecated
    private int getOldSize(Player p, String id) {
        int backPackSize = defaultBackpackSize;

        try {
            backPackSize = Integer.parseInt(backpacksData.getString("Inventories." + p.getUniqueId() + ".Items-" + id + ".Size"));
        } catch (NumberFormatException ignored) {
        }

        if (backPackSize % 9 != 0) {
            backPackSize = (int) Math.ceil((float) backPackSize / 9) * 9;
        }

        if (backPackSize == 0) backPackSize = 9;

        return getBackpackPermSize(p, backPackSize);
    }

    /**
     * Get backpack size of a Player Backpack with the permission for a custom one.
     */
    private int getBackpackPermSize(Player p, int backPackSize) {
        SpigotPlayer sPlayer = new SpigotPlayer(p);
        List<String> perms = sPlayer.getPermissions("prison.backpack.size.");
        int value = 0;
        for (String permNumber : perms) {
            int newValue = Integer.parseInt(permNumber.substring(21));
            if (newValue > value) {
                value = (int) Math.ceil((float) newValue / 9) * 9;
            }
        }

        if (value != 0) {
            return value;
        }
        return backPackSize;
    }

    /**
     * This method exists only for the conversion of the old deprecated one.
     */
    @Deprecated
    private List<String> getBackpacksIDsList(Player p) {
        List<String> backpacksIDs = new ArrayList<>();

        // Items can be -> Items- or just Items in the config, the default and old backpacks will have Items only, newer will be like
        // Items-1 or anyway an ID, I'm just getting the ID with this which's what I need.
        try {
            for (String key : backpacksData.getConfigurationSection("Inventories." + p.getUniqueId()).getKeys(false)) {
                if (!key.equalsIgnoreCase("Items") && !key.equalsIgnoreCase("Limit") && !key.equalsIgnoreCase("PlayerName") && !key.equalsIgnoreCase("UniqueID")) {
                    backpacksIDs.add(key.substring(6));
                } else {
                    if (!backpacksIDs.contains(null) && !key.equalsIgnoreCase("PlayerName") && !key.equalsIgnoreCase("UniqueID") && !key.equalsIgnoreCase("Limit")) {
                        backpacksIDs.add(null);
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }

        return backpacksIDs;
    }
}

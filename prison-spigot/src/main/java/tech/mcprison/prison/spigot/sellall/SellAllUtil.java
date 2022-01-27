package tech.mcprison.prison.spigot.sellall;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;

import at.pcgamingfreaks.Minepacks.Bukkit.API.Backpack;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllPlayerGUI;
import tech.mcprison.prison.spigot.integrations.IntegrationMinepacksPlugin;

/**
 * @author AnonymousGCA (GABRYCA)
 * */
public class SellAllUtil {

    private static SellAllUtil instance;
    private static final boolean isEnabled = getBoolean(SpigotPrison.getInstance().getConfig().getString("sellall"));
    private final Compatibility compat = SpigotPrison.getInstance().getCompatibility();
    private final ItemStack lapisLazuli = compat.getLapisItemStack();
    public Configuration sellAllConfig;
    private HashMap<XMaterial, Double> sellAllBlocks;
    private HashMap<String, Double> sellAllPrestigeMultipliers;
    private HashMap<Player, Double> autoSellEarningsNotificationWaiting = new HashMap<>();
    private ArrayList<XMaterial> sellAllItemTriggers;
    private ArrayList<Player> activePlayerDelay = new ArrayList<>();
    private List<String> sellAllDisabledWorlds;
    private MessagesConfig messages;
    private double defaultMultiplier;
    private int defaultSellAllDelay;
    private int defaultAutoSellEarningNotificationDelay;
    private Sound sellAllSoundSuccess;
    private Sound sellAllSoundFail;
    public String sellAllSignTag;
    public String sellAllCurrency;
    public String permissionSellAllSell;
    public String permissionBypassSign;
    public String permissionGUI;
    public String permissionPlayerGUI;
    public String permissionPrefixBlocks;
    public String permissionUseSign;
    public String permissionAutoSellPerUserToggleable;
    public String permissionItemTrigger;
    public boolean isPerBlockPermissionEnabled;
    public boolean isAutoSellEnabled;
    public boolean isAutoSellPerUserToggleable;
    public boolean isAutoSellPerUserToggleablePermEnabled;
    public boolean isAutoSellNotificationEnabled;
    public boolean isAutoSellEarningNotificationDelayEnabled;
    public boolean isSellAllDelayEnabled;
    public boolean isSellAllSellPermissionEnabled;
    public boolean isSellAllItemTriggerEnabled;
    public boolean isSellAllItemTriggerPermissionEnabled;
    public boolean isSellAllGUIEnabled;
    public boolean isSellAllPlayerGUIEnabled;
    public boolean isSellAllGUIPermissionEnabled;
    public boolean isSellAllPlayerGUIPermissionEnabled;
    public boolean isSellAllMultiplierEnabled;
    public boolean isSellAllPermissionMultiplierOnlyHigherEnabled;
    public boolean isSellAllSignEnabled;
    public boolean isSellAllBySignOnlyEnabled;
    public boolean isSellAllSignNotifyEnabled;
    public boolean isSellAllSignPermissionToUseEnabled;
    public boolean isSellAllNotificationEnabled;
    public boolean isSellAllSoundEnabled;
    public boolean isSellAllBackpackItemsEnabled;
    public boolean isSellAllMinesBackpacksPluginEnabled;
    public boolean isSellAllHandEnabled;

    /**
     * Get cached instance of SellAllUtil, if present, if not then Initialize it, if SellAll is disabled return null.
     *
     * @return SellAllUtil.
     * */
    public static SellAllUtil get() {
        if (!isEnabled){
            return null;
        }
        if (instance == null){
            instance = new SellAllUtil();
            instance.initCachedData();
        }
        return instance;
    }

    /**
     * Return boolean value from String.
     *
     * @return boolean.
     * */
    public static boolean getBoolean(String string) {
        return string != null && string.equalsIgnoreCase("true");
    }

    /**
     * Get an ArrayList of SellAllItemTriggers as XMaterials.
     * These items will trigger sellall when a player hold them and then do shift+right click in the air.
     *
     * @return ArrayList of XMaterials.
     * */
    public ArrayList<XMaterial> getItemTriggerXMaterials(){
        return sellAllItemTriggers;
    }

//    /**
//     * BUG: With Spigot versions less than 1.13 bukkit's Material will not work on all Materials since 
//     * varient data is stored in the ItemStack.  SO must use the XMaterial version of this function.
//     * 
//     * Get an ArrayList of SellAllItemTriggers as Materials.
//     * This will essentially do a conversion of the cached internal XMaterial ArrayList of SellAllItemTriggers.
//     *
//     * @return ArrayList of Materials.
//     * */
//    public ArrayList<Material> getItemTriggerMaterials(){
//        if (sellAllItemTriggers == null || sellAllItemTriggers.isEmpty()){
//            return new ArrayList<>();
//        }
//
//        ArrayList<Material> materials = new ArrayList<>();
//
//        for (XMaterial xMaterial : sellAllItemTriggers){
//            materials.add(xMaterial.parseMaterial());
//        }
//
//        return materials;
//    }

    /**
     * Get SellAll XMaterial or Lapis (As Lapis is weird) from an ItemStack.
     *
     * @param itemStack - ItemStack.
     *
     * @return XMaterial.
     * */
    private XMaterial getXMaterialOrLapis(ItemStack itemStack) {
        if (itemStack.isSimilar(lapisLazuli)) {
            return XMaterial.LAPIS_LAZULI;
        }
        XMaterial results = null;
        try
		{
			results = XMaterial.matchXMaterial(itemStack);
		}
		catch ( Exception e )
		{
			// ignore... it is not normal matertial so it cannot be sold
		}
        
        return results;
    }

    /**
     * Return SellAll Blocks HashMap cached values.
     *
     * @return HashMap of XMaterial-Double.
     * */
    public HashMap<XMaterial, Double> getSellAllBlocks() {
        return sellAllBlocks;
    }

    /**
     * Return SellAll Prestige Multiplier HashMap read before from config on init.
     *
     * HashMap details:
     * String is the name of the Prestige.
     * Double is the multiplier (default 1).
     *
     * @return HashMap of String-Double.
     * */
    public HashMap<String, Double> getPrestigeMultipliers() {
        return sellAllPrestigeMultipliers;
    }

    /**
     * Get HashMap of XMaterials and amounts from an Inventory.
     *
     * @param inv - Inventory.
     *
     * @return HashMap of XMaterials and Integers.
     * */
    public HashMap<XMaterial, Integer> getXMaterialsHashMapFromInventory(Inventory inv){

        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
        for (ItemStack itemStack : inv.getContents()){
            if (itemStack != null){
                XMaterial xMaterial = getXMaterialOrLapis(itemStack);
                if ( xMaterial != null ) {
                	
                	if (xMaterialIntegerHashMap.containsKey(xMaterial) && xMaterialIntegerHashMap.get(xMaterial) != 0){
                		xMaterialIntegerHashMap.put(xMaterial, xMaterialIntegerHashMap.get(xMaterial) + itemStack.getAmount());
                	} 
                	else {
                		xMaterialIntegerHashMap.put(xMaterial, itemStack.getAmount());
                	}
                }
            }
        }

        return xMaterialIntegerHashMap;
    }


    /**
     * get HashMap of XMaterials and Amounts from an ArrayList of ItemStacks.
     *
     * @param itemStacks - ArrayList of ItemStacks.
     *
     * @return HashMap of XMaterials and Integers.
     * */
    public HashMap<XMaterial, Integer> getXMaterialsHashMapFromArrayList(ArrayList<ItemStack> itemStacks){

        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
        for (ItemStack itemStack : itemStacks){
            if (itemStack != null){
                try {
                    XMaterial xMaterial = getXMaterialOrLapis(itemStack);
                    if ( xMaterial != null ) {
                    	
                    	if (xMaterialIntegerHashMap.containsKey(xMaterial) && xMaterialIntegerHashMap.get(xMaterial) != 0) {
                    		xMaterialIntegerHashMap.put(xMaterial, xMaterialIntegerHashMap.get(xMaterial) + itemStack.getAmount());
                    	} 
                    	else {
                    		xMaterialIntegerHashMap.put(xMaterial, itemStack.getAmount());
                    	}
                    }
                } catch (IllegalArgumentException ignored){}
            }
        }

        return xMaterialIntegerHashMap;
    }

    /**
     * Get SellAll Player Multiplier.
     *
     * @param p - Player.
     *
     * @return double.
     * */
    public double getPlayerMultiplier(Player p){

        if (!isSellAllMultiplierEnabled){
            return 1;
        }

        // Get Ranks module.
        ModuleManager modMan = Prison.get().getModuleManager();
        Module module = modMan == null ? null : modMan.getModule(PrisonRanks.MODULE_NAME).orElse(null);
        SpigotPlayer sPlayer = new SpigotPlayer(p);
        double multiplier = defaultMultiplier;

        // Get multiplier depending on Player + Prestige. NOTE that prestige multiplier will replace
        // the actual default multiplier.
        if (module != null) {
            PrisonRanks rankPlugin = (PrisonRanks) module;
            if (rankPlugin.getPlayerManager().getPlayer(sPlayer) != null) {
                String playerRankName;
                try {
                	
                	RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
                	
                    RankPlayer rankPlayer = rankPlugin.getPlayerManager().getPlayer(sPlayer);
                    PlayerRank pRank = rankPlayer == null ? null : rankPlayerFactory.getRank( rankPlayer, "prestiges");
                    Rank rank = pRank == null ? null : pRank.getRank();

                    playerRankName = rank == null ? null : rank.getName();
                } catch (NullPointerException ex) {
                    playerRankName = null;
                }
                if (playerRankName != null) {
                    String multiplierRankString = sellAllConfig.getString("Multiplier." + playerRankName + ".MULTIPLIER");
                    if (multiplierRankString != null && sellAllPrestigeMultipliers.containsKey(playerRankName)){
                        multiplier = sellAllPrestigeMultipliers.get(playerRankName);
                    }
                }
            }
        }

        // Get Multiplier from multipliers permission's if there's any.
        List<String> perms = sPlayer.getPermissions("prison.sellall.multiplier.");
        double multiplierExtraByPerms = 0;
        for (String multByPerm : perms) {
            double multByPermDouble = Double.parseDouble(multByPerm.substring(26));
            if (!isSellAllPermissionMultiplierOnlyHigherEnabled) {
                multiplierExtraByPerms += multByPermDouble;
            } else if (multByPermDouble > multiplierExtraByPerms) {
                multiplierExtraByPerms = multByPermDouble;
            }
        }
        multiplier += multiplierExtraByPerms;

        return multiplier;
    }

    /**
     * Get SellAll Money to give, it requires Player because of SellAll perBlockPermission as an option.
     * NOTE: This WON'T remove blocks from the HashMap when sold, and won't edit Inventories of Players,
     * but only return the amount of money that the Player would get if he sells now everything from the
     * specified HashMap of XMaterials and Integers.
     *
     * Will also calculate the Multiplier of the Player.
     *
     * Get SellAll Sell value of HashMap values.
     * NOTE: If there aren't blocks in the SellAll shop this will return 0.
     * NOTE: This method WON'T remove blocks from HashMap, but only return a double value.
     *
     * @param p - Player.
     * @param xMaterialIntegerHashMap - HashMap of XMaterial-Integer (Blocks of origin).
     *
     * @return double.
     * */
    public double getSellMoney(Player p, HashMap<XMaterial, Integer> xMaterialIntegerHashMap){

        if (sellAllBlocks.isEmpty()){
            return 0;
        }

        double multiplier = getPlayerMultiplier(p);
        double earned = 0;
        for (HashMap.Entry<XMaterial, Integer> xMaterialIntegerEntry : xMaterialIntegerHashMap.entrySet()){
            if (sellAllBlocks.containsKey(xMaterialIntegerEntry.getKey())){
                // This is stupid but right now I'm too confused, sorry.
                if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + xMaterialIntegerEntry.getKey().name())){
                    // Nothing will change.
                } else {
                    earned += xMaterialIntegerEntry.getValue() * sellAllBlocks.get(xMaterialIntegerEntry.getKey());
                }
            }
        }

        return earned * multiplier;
    }

    /**
     * Get SellAll Money to give, it requires Player because of SellAll perBlockPermission as an option.
     * NOTE: This WON'T remove blocks from the ArrayList when sold, and won't edit Inventories of Players,
     * but only return the amount of money that the Player would get if he sells now everything from the
     * specified ArrayList of ItemStacks.
     *
     * Will also calculate the Multiplier of the Player.
     *
     * Get SellAll Sell value of ArrayList of itemstack.
     * NOTE: If there aren't blocks in the SellAll shop this will return 0.
     * NOTE: This method WON'T remove blocks from HashMap, but only return a double value.
     *
     * @param p - Player.
     * @param itemStacks - ArrayList of ItemStacks (Blocks of origin).
     *
     * @return double.
     * */
    public double getSellMoney(Player p, ArrayList<ItemStack> itemStacks){
        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = getXMaterialsHashMapFromArrayList(itemStacks);
        return getSellMoney(p, xMaterialIntegerHashMap);
    }

    /**
     * Get SellAll Sell Money, calculated from all the enabled backpacks (from sellAll config and integrations) and
     * main inventory.
     * NOTE: This WON'T remove blocks from Inventories/Backpacks, but only return their value.
     *
     * Will also calculate the Multiplier of the Player.
     *
     * NOTE: If there aren't blocks in the SellAll shop this will return 0.
     * NOTE: This method WON'T remove blocks from HashMap, but only return a double value.
     *
     * @param p - Player.
     *
     * @return double.
     * */
    public double getSellMoney(Player p){

        if (sellAllBlocks.isEmpty()){
            return 0;
        }

        return getSellMoney(p, getHashMapOfPlayerInventories(p));
    }
    
    /**
     * <p>This sells a specific item stack.  Actually it returns the value of an item stack,
     * its up to the calling function to dispose of the contents if the result is non-zero.
     * </p>
     * @param p
     * @param itemStack
     * @return
     */
    private double getSellMoney( Player p, SpigotItemStack itemStack )
	{
    	double results = 0d;
    	
    	HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
    	
    	XMaterial xMat = XMaterial.matchXMaterial( itemStack.getBukkitStack() );
    	
    	if ( xMat != null ) {
    		xMaterialIntegerHashMap.put( xMat, itemStack.getAmount() );
    		
    		results = getSellMoney( p, xMaterialIntegerHashMap );
    	}
    	
		return results;
	}

    /**
     * Get HashMap with all the items of a Player.
     *
     * Return HashMap of XMaterial-Integer.
     *
     * @param p - Player.
     *
     * @return HashMap - XMaterial-Integer.
     * */
    private HashMap<XMaterial, Integer> getHashMapOfPlayerInventories(Player p) {
        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
        if (isSellAllBackpackItemsEnabled && getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks"))){
            BackpacksUtil backpacksUtil = BackpacksUtil.get();
            if (backpacksUtil.isMultipleBackpacksEnabled()){
                for (String id : backpacksUtil.getBackpacksIDs(p)){
                    xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, backpacksUtil.getBackpack(p, id));
                }
            } else {
                xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, backpacksUtil.getBackpack(p));
            }
        }

        if (isSellAllMinesBackpacksPluginEnabled && IntegrationMinepacksPlugin.getInstance().isEnabled()){
            Backpack backpack = IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p);
            if (backpack != null) {
                xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, backpack.getInventory());
            }
        }

        xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, p.getInventory());
        return xMaterialIntegerHashMap;
    }

    /**
     * Get AutoSell Player toggle if available.
     * If he enabled it, AutoSell will work, otherwise it won't.
     * If he never used the toggle command, this will return true, just like if he enabled it in the first place.
     *
     * @param p - Player.
     *
     * @return boolean.
     * */
    public boolean isPlayerAutoSellEnabled(Player p){

        if (isAutoSellPerUserToggleablePermEnabled && !p.hasPermission(permissionAutoSellPerUserToggleable)){
            return false;
        }

        if (sellAllConfig.getString("Users." + p.getUniqueId() + ".isEnabled") == null){
            return true;
        }

        return getBoolean(sellAllConfig.getString("Users." + p.getUniqueId() + ".isEnabled"));
    }

    /**
     * Check if Player is in a disabled world, where he can't use sellall sell.
     *
     * Return True if he's in a disabled world, False if not.
     *
     * @return boolean.
     * */
    public boolean isPlayerInDisabledWorld(Player p){
        return sellAllDisabledWorlds.contains(p.getWorld().getName());
    }

    /**
     * Check if Player is waiting for the end of SellAll Sell Delay.
     *
     * Return True if he's still waiting, False if not.
     *
     * @param p - Player.
     *
     * @return boolean.
     * */
    public boolean isPlayerWaitingSellAllDelay(Player p){
        return activePlayerDelay.contains(p);
    }

    /**
     * Check if Player is waiting for the end of the AutoSell Delay notification.
     *
     * Return true if Player is found.
     *
     * @param p - Player.
     *
     * @return boolean.
     * */
    public boolean isPlayerWaitingAutoSellNotification(Player p){
        return autoSellEarningsNotificationWaiting.containsKey(p);
    }

    /**
     * Check if specified string item name is a valid item.
     *
     * @param item - String.
     *
     * @return boolean.
     * */
    public boolean isValidItem(String item){
        return XMaterial.matchXMaterial(item).isPresent();
    }

    /**
     * Update SellAll Cached config.
     * */
    public void updateConfig(){
        sellAllConfig = SpigotPrison.getInstance().updateSellAllConfig();
    }

    /**
     * Init options that will be cached.
     * */
    private void initCachedData() {
        sellAllConfig = SpigotPrison.getInstance().updateSellAllConfig();
        messages = SpigotPrison.getInstance().getMessagesConfig();
        permissionSellAllSell = sellAllConfig.getString("Options.Sell_Permission");
        permissionBypassSign = sellAllConfig.getString("Options.SellAll_By_Sign_Bypass_Permission");
        permissionUseSign = sellAllConfig.getString("Options.SellAll_Sign_Use_Permission");
        permissionGUI = sellAllConfig.getString("Options.GUI_Permission");
        permissionPlayerGUI = sellAllConfig.getString("Options.Player_GUI_Permission");
        permissionPrefixBlocks = sellAllConfig.getString("Options.Sell_Per_Block_Permission");
        permissionAutoSellPerUserToggleable = sellAllConfig.getString("Options.Full_Inv_AutoSell_PerUserToggleable_Permission");
        permissionItemTrigger = sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Permission");
        sellAllCurrency = sellAllConfig.getString("Options.SellAll_Currency");
        sellAllSoundSuccess = XSound.matchXSound("Options.Sell_Sound_Success_Name").orElse(XSound.ENTITY_PLAYER_LEVELUP).parseSound();
        sellAllSoundFail = XSound.matchXSound("Options.Sell_Sound_Fail_Name").orElse(XSound.BLOCK_ANVIL_PLACE).parseSound();
        sellAllSignTag = SpigotPrison.format(sellAllConfig.getString("Options.SellAll_Sign_Visible_Tag"));
        sellAllBlocks = initSellAllBlocks();
        sellAllPrestigeMultipliers = initPrestigeMultipliers();
        sellAllItemTriggers = initSellAllItemTrigger();
        sellAllDisabledWorlds = initSellAllDisabledWorlds();
        defaultMultiplier = Double.parseDouble(sellAllConfig.getString("Options.Multiplier_Default"));
        defaultSellAllDelay = Integer.parseInt(sellAllConfig.getString("Options.Sell_Delay_Seconds"));
        defaultAutoSellEarningNotificationDelay = Integer.parseInt(sellAllConfig.getString("Options.Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Delay_Seconds"));
        isPerBlockPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"));
        isAutoSellEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell"));
        isAutoSellNotificationEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_Notification"));
        isAutoSellEarningNotificationDelayEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Enabled"));
        isAutoSellPerUserToggleable = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable"));
        isAutoSellPerUserToggleablePermEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable_Need_Perm"));
        isSellAllNotificationEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Notify_Enabled"));
        isSellAllSoundEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Sound_Enabled"));
        isSellAllBackpackItemsEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Prison_BackPack_Items"));
        isSellAllMinesBackpacksPluginEnabled = getBoolean(sellAllConfig.getString("Options.Sell_MinesBackPacks_Plugin_Backpack"));
        isSellAllDelayEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Delay_Enabled"));
        isSellAllSellPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Permission_Enabled"));
        isSellAllItemTriggerEnabled = getBoolean(sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Enabled"));
        isSellAllItemTriggerPermissionEnabled = getBoolean(sellAllConfig.getString("Options.ShiftAndRightClickSellAll.PermissionEnabled"));
        isSellAllGUIEnabled = getBoolean(sellAllConfig.getString("Options.GUI_Enabled"));
        isSellAllPlayerGUIEnabled = getBoolean(sellAllConfig.getString("Options.Player_GUI_Enabled"));
        isSellAllGUIPermissionEnabled = getBoolean(sellAllConfig.getString("Options.GUI_Permission_Enabled"));
        isSellAllPlayerGUIPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Player_GUI_Permission_Enabled"));
        isSellAllMultiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        isSellAllPermissionMultiplierOnlyHigherEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Permission_Only_Higher"));
        isSellAllSignEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_Sign_Enabled"));
        isSellAllSignNotifyEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_Sign_Notify"));
        isSellAllSignPermissionToUseEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_Sign_Use_Permission_Enabled"));
        isSellAllBySignOnlyEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_By_Sign_Only"));
        isSellAllHandEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_Hand_Enabled"));
    }

    /**
     * Loads sellAll blocks from SellAllConfig.yml
     * With XMaterials and double values (money).
     *
     * @return HashMap XMaterial-Double.
     * */
    public HashMap<XMaterial, Double> initSellAllBlocks(){

        HashMap<XMaterial, Double> sellAllXMaterials = new HashMap<>();

        if (sellAllConfig.getConfigurationSection("Items") == null){
            return sellAllXMaterials;
        }

        for (String key : sellAllConfig.getConfigurationSection("Items").getKeys(false)) {

            String itemID = sellAllConfig.getString("Items." + key + ".ITEM_ID");

            Optional<XMaterial> iMatOptional = XMaterial.matchXMaterial(itemID);
            XMaterial itemMaterial = iMatOptional.orElse(null);

            if (itemMaterial != null) {
                String valueString = sellAllConfig.getString("Items." + key + ".ITEM_VALUE");
                if (valueString != null) {
                    try {
                        double value = Double.parseDouble(valueString);
                        sellAllXMaterials.put(itemMaterial, value);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return sellAllXMaterials;
    }

    /**
     * Init sellAll Prestige Multipliers.
     *
     * @return HashMap String-Double.
     * */
    public HashMap<String, Double> initPrestigeMultipliers(){

        HashMap<String, Double> prestigeMultipliers = new HashMap<>();

        if (sellAllConfig.getConfigurationSection("Multiplier") == null){
            return prestigeMultipliers;
        }

        for (String key : sellAllConfig.getConfigurationSection("Multiplier").getKeys(false)){
            prestigeMultipliers.put(sellAllConfig.getString("Multiplier." + key + ".PRESTIGE_NAME"), sellAllConfig.getDouble("Multiplier." + key + ".MULTIPLIER"));
        }
        return prestigeMultipliers;
    }

    /**
     * Read SellAll Item Triggers and return them in an ArrayList of XMaterials.
     * NOTE: This needs to be enabled, otherwise will return null.
     *
     * @return ArrayList of XMaterials.
     * */
    public ArrayList<XMaterial> initSellAllItemTrigger(){

        ArrayList<XMaterial> xMaterials = new ArrayList<>();

        if (!isSellAllItemTriggerEnabled){
            return xMaterials;
        }

        if (sellAllConfig.getConfigurationSection("ShiftAndRightClickSellAll.Items") == null){
            return xMaterials;
        }

        for (String xMaterialID : sellAllConfig.getConfigurationSection("ShiftAndRightClickSellAll.Items").getKeys(false)){
            Optional<XMaterial> xMaterialRead = XMaterial.matchXMaterial(sellAllConfig.getString("ShiftAndRightClickSellAll.Items." + xMaterialID + ".ITEM_ID"));
            xMaterialRead.ifPresent(xMaterials::add);
        }

        return xMaterials;
    }

    /**
     * Get List of names of disabled worlds.
     * If a Player is in one of these worlds, he won't be able to use SellAll.
     * */
    public List<String> initSellAllDisabledWorlds(){
        return sellAllConfig.getStringList("Options.DisabledWorlds");
    }

    /**
     * Add a block to SellAll config.
     *
     * Return True if no error occurred, false if error.
     *
     * @param xMaterial - XMaterial of block to add.
     * @param value - Value of the block to add.
     *
     * @return boolean.
     * */
    public boolean addSellAllBlock(XMaterial xMaterial, double value){
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + xMaterial.name() + ".ITEM_ID", xMaterial.name());
            conf.set("Items." + xMaterial.name() + ".ITEM_VALUE", value);
            if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                conf.set("Items." + xMaterial.name() + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + xMaterial.name());
            }
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        updateConfig();
        sellAllBlocks.put(xMaterial, value);
        return true;
    }

//    /**
//     * 
//     * BUG: With Spigot versions less than 1.13 bukkit's Material will not work on all Materials since 
//     *      varient data is stored in the ItemStack.  SO must use the XMaterial version of this function.
//     * 
//     * Add a block to SellAll config.
//     *
//     * Return True if no error occurred, false if error.
//     *
//     * @param material - Material of block to add.
//     * @param value - Value of the block to add.
//     *
//     * @return boolean.
//     * */
//    public boolean addSellAllBlock(Material material, double value){
//        return addSellAllBlock(XMaterial.matchXMaterial(material), value);
//    }

    /**
     * Add Multiplier to SellAll depending on the Prestige Rank (Rank from the prestiges ladder).
     *
     * Return true if edited with success, false if error or Prestige not found.
     *
     * @param prestigeName - Name of the Prestige as String.
     * @param multiplier - Double value.
     *
     * @return boolean.
     * */
    public boolean addPrestigeMultiplier(String prestigeName, double multiplier){

        PrisonRanks rankPlugin = (PrisonRanks) (Prison.get().getModuleManager() == null ? null : Prison.get().getModuleManager().getModule(PrisonRanks.MODULE_NAME).orElse(null));
        if (rankPlugin == null) {
            return false;
        }

        boolean isPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges") != null;
        if (!isPrestigeLadder) {
            return false;
        }

        boolean isARank = rankPlugin.getRankManager().getRank(prestigeName) != null;
        if (!isARank) {
            return false;
        }

        boolean isInPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges").containsRank(rankPlugin.getRankManager().getRank(prestigeName));
        if (!isInPrestigeLadder) {
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestigeName + ".PRESTIGE_NAME", prestigeName);
            conf.set("Multiplier." + prestigeName + ".MULTIPLIER", multiplier);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        sellAllPrestigeMultipliers.put(prestigeName, multiplier);
        updateConfig();
        return true;
    }

    /**
     * Add SellAll Item Trigger.
     *
     * Return true if run with success, false if error or item already added.
     *
     * @param xMaterial - XMaterial to add.
     *
     * @return boolean.
     * */
    public boolean addItemTrigger(XMaterial xMaterial){
        if (sellAllItemTriggers.contains(xMaterial)){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + xMaterial.name() + ".ITEM_ID", xMaterial.name());
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        sellAllItemTriggers.add(xMaterial);
        updateConfig();
        return true;
    }

//    /**
//     * BUG: With Spigot versions less than 1.13 bukkit's Material will not work on all Materials since 
//     * varient data is stored in the ItemStack.  SO must use the XMaterial version of this function.
//     * 
//     * Add SellAll Item Trigger.
//     *
//     * Return true if success, false if error or already added item.
//     *
//     * @param material - Material to add.
//     *
//     * @return boolean.
//     * */
//    public boolean addItemTrigger(Material material){
//        return addItemTrigger(XMaterial.matchXMaterial(material));
//    }

    /**
     * Add Player to active delay.
     * He'll be removed at the end of it.
     * Also start delay.
     *
     * @param p - Player.
     * */
    public void addToDelay(Player p){
        if (!isPlayerWaitingSellAllDelay(p)){
            activePlayerDelay.add(p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> removeFromDelay(p), 20L * defaultSellAllDelay);
        }
    }

    /**
     * Add Player to active delay for AutoSell Notification.
     *
     * Return true if added with success, False if error or Player already added or disabled.
     *
     * @param p - Player.
     *
     * @return boolean.
     * */
    public boolean addToAutoSellNotificationDelay(Player p){

        if (!isAutoSellEarningNotificationDelayEnabled){
            return false;
        }

        if (!isPlayerWaitingAutoSellNotification(p)){
            autoSellEarningsNotificationWaiting.put(p, 0.00);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> removeFromAutoSellDelayAndNotify(p), 20L * defaultAutoSellEarningNotificationDelay);
            return true;
        }

        return false;
    }

    /**
     * Add balance to Delayed earnings of AutoSell Notification.
     *
     * @param p - Player.
     * @param value - double.
     * */
    public void addDelayedEarningAutoSellNotification(Player p, double value){
        if (isPlayerWaitingAutoSellNotification(p)){
            autoSellEarningsNotificationWaiting.put(p, autoSellEarningsNotificationWaiting.get(p) + value);
        }
    }

    private HashMap<XMaterial, Integer> addInventoryToHashMap(HashMap<XMaterial, Integer> xMaterialIntegerHashMap, Inventory inv) {
        for (ItemStack itemStack : inv.getContents()){
            if (itemStack != null){
                XMaterial xMaterial = getXMaterialOrLapis(itemStack);
                if ( xMaterial != null ) {
                	
                	if (xMaterialIntegerHashMap.containsKey(xMaterial)){
                		xMaterialIntegerHashMap.put(xMaterial, xMaterialIntegerHashMap.get(xMaterial) + itemStack.getAmount());
                	} 
                	else {
                		xMaterialIntegerHashMap.put(xMaterial, itemStack.getAmount());
                	}
                }
            }
        }
        return xMaterialIntegerHashMap;
    }

    /**
     * Check if Player meets requirements to use SellAll.
     *
     * This will return true if everything is meet, False if even only isn't.
     * What will be checked is:
     * - Is in a world where SellAll Sell isn't locked by config.
     * - Check if SellAll Signs and SellAll by Sign only is enabled and Player isn't selling through a Sign right now or
     * doesn't have the bypass permission (To tell to this method if Player is selling through a sign, please set the boolean
     * parameter to true).
     * - Check if a permission to sell is required and then if the Player has it.
     * - Check if SellAll Delay is enabled and Player did wait until the end of it.
     *
     * @param p - Player.
     * @param isUsingSign - boolean.
     *
     * @return boolean.
     * */
    public boolean canPlayerSell(Player p, boolean isUsingSign){

        if (isSellAllSellPermissionEnabled && !p.hasPermission(permissionSellAllSell)){
            return false;
        }

        if (isPlayerInDisabledWorld(p)){
            return false;
        }

        if (isSellAllSignEnabled && isSellAllBySignOnlyEnabled && !isUsingSign && !p.hasPermission(permissionBypassSign)){
            return false;
        }

        if (isSellAllDelayEnabled && isPlayerWaitingSellAllDelay(p)){
            return false;
        }

        return true;
    }


    /**
     * Edit price of a block.
     *
     * Return true if edited with success, false if error or block not found.
     *
     * @param xMaterial - XMaterial to edit.
     * @param value - New price as a double.
     *
     * @return boolean.
     * */
    public boolean editPrice(XMaterial xMaterial, double value){

        if (!sellAllBlocks.containsKey(xMaterial)){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + xMaterial.name() + ".ITEM_ID", xMaterial.name());
            conf.set("Items." + xMaterial.name() + ".ITEM_VALUE", value);
            if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                conf.set("Items." + xMaterial + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + xMaterial.name());
            }
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        updateConfig();
        sellAllBlocks.put(xMaterial, value);
        return true;
    }

//    /**
//     * BUG: With Spigot versions less than 1.13 bukkit's Material will not work on all Materials since 
//     * varient data is stored in the ItemStack.  SO must use the XMaterial version of this function.
//     * 
//     * Edit price of a block using Material.
//     *
//     * Return true if edited with success, false if error or not found.
//     *
//     * @param material - Material.
//     * @param value - New price as a double.
//     *
//     * @return boolean,
//     * */
//    public boolean editPrice(Material material, double value){
//        return editPrice(XMaterial.matchXMaterial(material), value);
//    }

    /**
     * Edit Prestige Multiplier value.
     *
     * Return true if success, false if error or not found.
     *
     * @param prestigeName - String.
     * @param multiplier - Double value.
     * */
    public boolean editPrestigeMultiplier(String prestigeName, double multiplier) {

        if (!sellAllPrestigeMultipliers.containsKey(prestigeName)) {
            return false;
        }

        return addPrestigeMultiplier(prestigeName, multiplier);
    }

    /**
     * Remove block by XMaterial name.
     *
     * Return true if removed with success, false if not found or error.
     *
     * @param xMaterial - XMaterial to remove.
     *
     * @return boolean.
     * */
    public boolean removeSellAllBlock(XMaterial xMaterial){

        if (!sellAllBlocks.containsKey(xMaterial)){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + xMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        updateConfig();
        sellAllBlocks.remove(xMaterial);
        return true;
    }

//    /**
//     * BUG: With Spigot versions less than 1.13 bukkit's Material will not work on all Materials since 
//     * varient data is stored in the ItemStack.  SO must use the XMaterial version of this function.
//     * 
//     * Remove block by XMaterial name.
//     *
//     * Return true if removed with success, false if not found or error.
//     *
//     * @param material - XMaterial to remove.
//     *
//     * @return boolean.
//     * */
//    public boolean removeSellAllBlock(Material material){
//        return removeSellAllBlock(XMaterial.matchXMaterial(material));
//    }

    /**
     * Remove a Prestige Multiplier by name.
     *
     * Return true if success, false if error or not found.
     *
     * @param prestigeName - String.
     *
     * @return boolean.
     * */
    public boolean removePrestigeMultiplier(String prestigeName){

        if (!sellAllPrestigeMultipliers.containsKey(prestigeName)){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestigeName, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            return false;
        }
        sellAllPrestigeMultipliers.remove(prestigeName);
        updateConfig();
        return true;
    }

    /**
     * Delete SellAll Item Trigger.
     *
     * Return true if success, false if error or item not found.
     *
     * @param xMaterial - XMaterial to remove.
     *
     * @return boolean.
     * */
    public boolean removeItemTrigger(XMaterial xMaterial){

        if (!sellAllItemTriggers.contains(xMaterial)){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + xMaterial.name() + ".ITEM_ID", null);
            conf.set("ShiftAndRightClickSellAll.Items." + xMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        sellAllItemTriggers.remove(xMaterial);
        updateConfig();
        return true;
    }

//    /**
//     * BUG: With Spigot versions less than 1.13 bukkit's Material will not work on all Materials since 
//     * varient data is stored in the ItemStack.  SO must use the XMaterial version of this function.
//     * 
//     * Delete SellAll Item Trigger.
//     *
//     * Return true if success, false if error or item not found.
//     *
//     * @param material - Material to remove.
//     *
//     * @return boolean.
//     * */
//    public boolean removeItemTrigger(Material material){
//        return removeItemTrigger(XMaterial.matchXMaterial(material));
//    }

    /**
     * Remove Sellable Blocks from HashMap of XMaterial-Integer given as a parameter.
     * NOTE: If there aren't blocks in the SellAll shop, nothing will change.
     * NOTE: This method will remove blocks from the HashMap, but it WON'T return the value of money, for that please use
     * the getSellAllSellMoney method.
     *
     * @param p - Player.
     * @param xMaterialIntegerHashMap - HashMap of XMaterial-Integer (Blocks of origin).
     *
     *
     * @return HashMap - XMaterial-Integer.
     * */
    public HashMap<XMaterial, Integer> removeSellableItems(Player p, HashMap<XMaterial, Integer> xMaterialIntegerHashMap){

        if (sellAllBlocks.isEmpty()){
            return xMaterialIntegerHashMap;
        }

        /* Not necessary now, as this only removes what's sellable, this should be checked in another place before.
        if (isPlayerInDisabledWorld(p)){
            return xMaterialIntegerHashMap;
        }
         */

        for (HashMap.Entry<XMaterial, Integer> xMaterialIntegerEntry : xMaterialIntegerHashMap.entrySet()){
            if (sellAllBlocks.containsKey(xMaterialIntegerEntry.getKey())){
                // This is stupid but right now I'm too confused, sorry.
                if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + xMaterialIntegerEntry.getKey().name())){
                    // Nothing will happen.
                } else {
                    xMaterialIntegerHashMap.remove(xMaterialIntegerEntry.getKey());
                }
            }
        }

        return xMaterialIntegerHashMap;
    }

    /**
     * Remove Sellable Blocks from ArrayList of ItemStacks given as a parameter.
     * NOTE: If there aren't blocks in the SellAll shop, nothing will change.
     * NOTE: This method will remove blocks from the HashMap, but it WON'T return the value of money, for that please use
     * the getSellAllSellMoney method.
     *
     * @param p - Player.
     * @param itemStacks - ItemStacks.
     *
     * @return ArrayList - ItemStacks.
     * */
    public ArrayList<ItemStack> removeSellableItems(Player p, ArrayList<ItemStack> itemStacks){
        
        if (sellAllBlocks.isEmpty()){
            return itemStacks;
        }
        
        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = getXMaterialsHashMapFromArrayList(itemStacks);

        xMaterialIntegerHashMap = removeSellableItems(p, xMaterialIntegerHashMap);

        ArrayList<ItemStack> newItemStacks = new ArrayList<>();
        for (HashMap.Entry<XMaterial, Integer> xMaterialIntegerEntry : xMaterialIntegerHashMap.entrySet()){
        	
        	// WARNING: Cannot convert XMaterial to a Material then ItemStack or it will fail on variants 
        	//          for spigot versions less than 1.13:
        	
        	ItemStack iStack = xMaterialIntegerEntry.getKey().parseItem();
        	if ( iStack != null ) {
        		iStack.setAmount( xMaterialIntegerEntry.getValue() );
        		newItemStacks.add( iStack );
        	}
        	
//            Material material = xMaterialIntegerEntry.getKey().parseMaterial();
//            if (material != null) {
//                newItemStacks.add(new ItemStack(material, xMaterialIntegerEntry.getValue()));
//            }
        }

        return newItemStacks;
    }
    
    /**
     * Remove Sellable blocks from an Inventory of a Player.
     * 
     * Return an Inventory with the unsold items.
     * 
     * @param p - Player.
     * @param inv - Inventory.
     *            
     * @return inv - Inventory.           
     * */
    public Inventory removeSellableItems(Player p, Inventory inv){
        if (sellAllBlocks.isEmpty()){
            return inv;
        }
        
        for (ItemStack itemStack : inv.getContents()){
            if (itemStack != null){
                try {
                    XMaterial xMaterial = getXMaterialOrLapis(itemStack);
                    if ( xMaterial != null && sellAllBlocks.containsKey(xMaterial)) {
                        if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + xMaterial.name())) {
                            // Nothing will change.
                        } else {
                            inv.remove(itemStack);
                        }
                    }
                } catch (IllegalArgumentException ignored){}
            }
        }
        
        return inv;
    }
    
    /**
     * Remove Sellable blocks from all Player inventories directly hooked.
     * 
     * @param p - Player.
     * */
    public void removeSellableItems(Player p){

        if (sellAllBlocks.isEmpty()){
            return;
        }

        if (isSellAllBackpackItemsEnabled && getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks"))){
            BackpacksUtil backpacksUtil = BackpacksUtil.get();
            if (backpacksUtil.isMultipleBackpacksEnabled()){
                for (String id : backpacksUtil.getBackpacksIDs(p)){
                    backpacksUtil.setInventory(p, removeSellableItems(p, backpacksUtil.getBackpack(p, id)), id);
                }
            } else {
                backpacksUtil.setInventory(p, removeSellableItems(p, backpacksUtil.getBackpack(p)));
            }
        }

        if (isSellAllMinesBackpacksPluginEnabled && IntegrationMinepacksPlugin.getInstance().isEnabled()){
            Backpack backpack = IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p);
            if (backpack != null) {
                removeSellableItems(p, backpack.getInventory());
            }
        }

        removeSellableItems(p, p.getInventory());
    }

    /**
     * Remove Player from delay.
     *
     * @param p - Player.
     * */
    public void removeFromDelay(Player p){
        activePlayerDelay.remove(p);
    }

    /**
     * Remove Player from AutoSell Delay Notification and also send a notification to him with the balance until then.
     *
     * @param p - Player.
     * */
    public void removeFromAutoSellDelayAndNotify(Player p){
        if (autoSellEarningsNotificationWaiting.containsKey(p) && autoSellEarningsNotificationWaiting.get(p) > 0.00){
        	
        	DecimalFormat fFmt = new DecimalFormat("#,##0.00");
        	String amt = fFmt.format( autoSellEarningsNotificationWaiting.get(p) );
            Output.get().send( new SpigotPlayer(p),
            		messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt );
        }
        autoSellEarningsNotificationWaiting.remove(p);
    }

    /**
     * Set SellAll item trigger.
     *
     * Return true if run with success, false if error or already enabled/disabled.
     *
     * @param isEnabled - True to enable, False to disable.
     *
     * @return boolean.
     * */
    public boolean setItemTrigger(Boolean isEnabled){
        if (isSellAllItemTriggerEnabled == isEnabled){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.ShiftAndRightClickSellAll.Enabled", isEnabled);
            conf.save(sellAllFile);
        } catch (IOException e) {
            return true;
        }
        isSellAllItemTriggerEnabled = isEnabled;
        updateConfig();
        return true;
    }

    /**
     * Set SellAll AutoSell.
     *
     * Return true if success, false if error or already enabled/disabled.
     *
     * @param isEnabled - True to enable, False to disable.
     *
     * @return boolean.
     * */
    public boolean setAutoSell(Boolean isEnabled){
        if (isAutoSellEnabled == isEnabled){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell", isEnabled);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        isAutoSellEnabled = isEnabled;
        updateConfig();
        return true;
    }

    /**
     * Set AutoSell Player.
     *
     * Return True if success, False if error or already enabled/disabled or missing permission.
     *
     * @param p - Player.
     * @param enable - boolean.
     *
     * @return boolean.
     * */
    public boolean setAutoSellPlayer(Player p, boolean enable){
        if (!isAutoSellEnabled || !isAutoSellPerUserToggleable){
            return false;
        }

        if (isAutoSellPerUserToggleablePermEnabled && !p.hasPermission(permissionAutoSellPerUserToggleable)){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Users." + p.getUniqueId() + ".isEnabled", enable);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        updateConfig();
        return true;
    }

    /**
     * Set SellAllAutoSellPerUserToggleable.
     *
     * Return true if success, false if error or already enabled/disabled.
     *
     * @param isEnabled - True to enable, False to disable.
     *
     * @return boolean.
     * */
    public boolean setAutoSellPerUserToggleable(boolean isEnabled){
        if (isAutoSellPerUserToggleable == isEnabled){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell_perUserToggleable", isEnabled);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isAutoSellPerUserToggleable = isEnabled;
        updateConfig();
        return true;
    }

    /**
     * Set SellAll Currency by name.
     *
     * Return True if success, False if error or invalid currency.
     *
     * @param currency - String.
     *
     * @return boolean.
     * */
    public boolean setCurrency(String currency){
        EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager().getEconomyForCurrency(currency);
        if (currencyEcon == null && !currency.equalsIgnoreCase("default")) {
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.SellAll_Currency", currency);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        sellAllCurrency = currency;
        updateConfig();
        return true;
    }

    /**
     * Enable or disable SellAll delay.
     *
     * Return True if success, False if error.
     *
     * @param enabled - Boolean.
     *
     * @return boolean.
     * */
    public boolean setDelayEnable(boolean enabled){
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Enabled", enabled);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isSellAllDelayEnabled = enabled;
        updateConfig();
        return true;
    }

    /**
     * Set SellAll Delay.
     *
     * Return True if success, False if error.
     *
     * @param delay - Integer.
     *
     * @return boolean.
     * */
    public boolean setDelay(int delay){
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Seconds", delay);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        defaultSellAllDelay = delay;
        updateConfig();
        return true;
    }

    /**
     * Sell removing items from Inventories and checking all the possible conditions that a Player must meet to sell
     * items, this includes method parameters like:
     * - Is using SellAll Sign.
     * - If tell the Player how much did he earn (if this's disabled by config, the parameter will be ignored).
     * - If do this action without making the player notice it, disabling sounds and all messages.
     * - If tell the Player to wait the end of SellAll Delay if not ended (if this's disabled by config, the parameter will be ignored).
     * - If tell the Player how much did he earn only after a delay (AutoSell Delay Earnings will use this option for example).
     * - If play sound on SellAll Sell (If sounds are disabled from the config, this parameter will be ignored.
     *
     * Return True if success, False if error or nothing changed or Player not meeting requirements.
     *
     * Default usage of this method: sellAllSell(p, false, false, true, true, false, true);
     *
     * @param p - Player.
     * @param isUsingSign - boolean.
     * @param completelySilent - boolean.
     * @param notifyPlayerEarned - boolean.
     * @param notifyPlayerDelay - boolean.
     * @param notifyPlayerEarningDelay - boolean.
     * @param playSoundOnSellAll - boolean.
     *
     * @return boolean.
     * */
    public boolean sellAllSell(Player p, boolean isUsingSign, boolean completelySilent, boolean notifyPlayerEarned, boolean notifyPlayerDelay, boolean notifyPlayerEarningDelay, boolean playSoundOnSellAll){
        if (!isUsingSign && isSellAllSignEnabled && isSellAllBySignOnlyEnabled && !p.hasPermission(permissionBypassSign)){
            if (!completelySilent) {
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_sign_only));
            }
            return false;
        }

        if (isSellAllDelayEnabled && isPlayerWaitingSellAllDelay(p)){
            if (notifyPlayerDelay && !completelySilent) {
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_wait));
            }
            return false;
        }

        if (sellAllBlocks.isEmpty()){
            if (!completelySilent){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_empty));
            }
            return false;
        }

        double money = getSellMoney(p);
        if (money != 0){

            SpigotPlayer sPlayer = new SpigotPlayer(p);
            RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
            if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) sellAllCurrency = null;
            removeSellableItems(p);
            rankPlayer.addBalance(sellAllCurrency, money);

            if (isSellAllDelayEnabled){
                addToDelay(p);
            }

            if (!completelySilent) {
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundSuccess, 3, 1);
                }

                if (notifyPlayerEarningDelay && isAutoSellEarningNotificationDelayEnabled){
                    if (!isPlayerWaitingAutoSellNotification(p)){
                        addToAutoSellNotificationDelay(p);
                    } 
                    
                    addDelayedEarningAutoSellNotification(p, money);
                } else if (notifyPlayerEarned){
                	DecimalFormat fFmt = new DecimalFormat("#,##0.00");
                	String amt = fFmt.format( money );
                	
                   Output.get().sendInfo(sPlayer, messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt );
                }
            }
            return true;
        } else {
            if (!completelySilent){
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundFail, 3, 1);
                }
                Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_nothing_sellable));
            }
            return false;
        }
    }
    
    
    /**
     * <p>This function enables the selling of just one ItemStack and can be used outside of 
     * the player's inventory, such as processing the drops of tens of thousands of blocks
     * worth of drops.  This would be much faster than processing the player's inventory.
     * </p>
     * 
     * <p>This function is used by auto features.
     * </p>
     * 
     * @param p
     * @param itemStack
     * @param completelySilent
     * @param notifyPlayerEarned
     * @param notifyPlayerEarningDelay
     * @return Amount of money earned from the sale of the item stack
     */
    public double sellAllSell(Player p, SpigotItemStack itemStack, 
    		boolean completelySilent, boolean notifyPlayerEarned, boolean notifyPlayerEarningDelay){

    	double money = getSellMoney(p, itemStack);
    	
    	if (money != 0) {
    		
    		SpigotPlayer sPlayer = new SpigotPlayer(p);
    		RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());

    		if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) sellAllCurrency = null;
    		rankPlayer.addBalance(sellAllCurrency, money);
    		
    		
    		if (!completelySilent) {
    			
    			if (notifyPlayerEarningDelay // && isAutoSellEarningNotificationDelayEnabled
    					){
    				
    				if (!isPlayerWaitingAutoSellNotification(p)){
    					// Initialize && Force delayed notifications, even if delayed is disabled:
    					autoSellEarningsNotificationWaiting.put(p, 0.00);
    					Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> removeFromAutoSellDelayAndNotify(p), 20L * defaultAutoSellEarningNotificationDelay);

    				} 
    				
    				addDelayedEarningAutoSellNotification(p, money);
    			} 
    			else if (notifyPlayerEarned){
    				DecimalFormat fFmt = new DecimalFormat("#,##0.00");
    	        	String amt = fFmt.format( money );
    	        	
    				Output.get().sendInfo(sPlayer, messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt );
    			}
    		}
    	} 
    	
    	return money;
    }



	/**
     * Sell removing items from Inventories and checking all the possible conditions that a Player must meet to sell
     * items, this includes method parameters like:
     * - Is using SellAll Sign.
     * - If tell the Player how much did he earn (if this's disabled by config, the parameter will be ignored).
     * - If do this action without making the player notice it, disabling sounds and all messages.
     * - If tell the Player to wait the end of SellAll Delay if not ended (if this's disabled by config, the parameter will be ignored).
     * - If tell the Player how much did he earn only after a delay (AutoSell Delay Earnings will use this option for example).
     * - If play sound on SellAll Sell (If sounds are disabled from the config, this parameter will be ignored.
     * - If Sell only stuff from the input arrayList and not sell what is in the many Player inventories and supported backpacks.
     *
     * NOTE: With this method you can add an ArrayList of ItemStacks to sell, remove sold items (this will return the ArrayList without
     * sold items), and give money to the player, also note that this will also trigger the usual sellall sell and sell everything sellable
     * from all inventories and enabled backpacks of the Player.
     *
     * Return True if success, False if error or nothing changed or Player not meeting requirements.
     *
     * Default usage of this method: sellAllSell(p, itemStacks, false, false, true, false, false, true, false);
     *
     * @param p - Player.
     * @param itemStacks - ArrayList of ItemStacks.
     * @param isUsingSign - boolean.
     * @param completelySilent - boolean.
     * @param notifyPlayerEarned - boolean.
     * @param notifyPlayerDelay - boolean.
     * @param notifyPlayerEarningDelay - boolean.
     * @param playSoundOnSellAll - boolean.
     * @param sellInputArrayListOnly - boolean.
     *
     * @return boolean.
     * */
    public ArrayList<ItemStack> sellAllSell(Player p, ArrayList<ItemStack> itemStacks, boolean isUsingSign, boolean completelySilent, boolean notifyPlayerEarned, boolean notifyPlayerDelay, boolean notifyPlayerEarningDelay, boolean playSoundOnSellAll, boolean sellInputArrayListOnly){
        if (!isUsingSign && isSellAllSignEnabled && isSellAllBySignOnlyEnabled && !p.hasPermission(permissionBypassSign)){
            if (!completelySilent) {
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_sign_only));
            }
            return itemStacks;
        }

        if (isSellAllDelayEnabled && isPlayerWaitingSellAllDelay(p)){
            if (notifyPlayerDelay && !completelySilent) {
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_wait));
            }
            return itemStacks;
        }

        if (sellAllBlocks.isEmpty()){
            if (!completelySilent){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_empty));
            }
            return itemStacks;
        }

        double arrayListMoney = getSellMoney(p, itemStacks);
        if (arrayListMoney != 0.00){
            itemStacks = removeSellableItems(p, itemStacks);
        }

        double money;
        if (sellInputArrayListOnly){
            money = arrayListMoney;
        } else {
            money = getSellMoney(p) + arrayListMoney;
        }

        if (money != 0){

            SpigotPlayer sPlayer = new SpigotPlayer(p);
            RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
            if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) sellAllCurrency = null;

            if (!sellInputArrayListOnly) {
                removeSellableItems(p);
            }
            rankPlayer.addBalance(sellAllCurrency, money);

            if (isSellAllDelayEnabled){
                addToDelay(p);
            }

            if (!completelySilent) {
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundSuccess, 3, 1);
                }

                if (notifyPlayerEarningDelay && isAutoSellEarningNotificationDelayEnabled){
                    if (!isPlayerWaitingAutoSellNotification(p)){
                        addToAutoSellNotificationDelay(p);
                    } else {
                        addDelayedEarningAutoSellNotification(p, money);
                    }
                } else if (notifyPlayerEarned){
                	DecimalFormat fFmt = new DecimalFormat("#,##0.00");
                	String amt = fFmt.format( money );
                	
                    Output.get().sendInfo(sPlayer, messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt );
                }
            }
        } else {
            if (!completelySilent){
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundFail, 3, 1);
                }
                Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_nothing_sellable));
            }
        }
        return itemStacks;
    }

    /**
     * Open SellAll GUI to the specified Player.
     * NOTE: SellAll GUI must be enabled from the config or nothing will happen.
     * NOTE #2: A Player if admin will open another GUI if meets requirements, if not it will try to check
     * if it meets the normal Player GUI requirements and open that one instead.
     *
     * Return true if success, false if error, disabled or missing permission.
     *
     * @param p - Player.
     *
     * @return boolean.
     * */
    public boolean openSellAllGUI( Player p, int page, String cmdPage, String cmdReturn ){
        if (!isSellAllGUIEnabled){
            return false;
        }

        if (isSellAllGUIPermissionEnabled && !p.hasPermission(permissionGUI)){

            if (!isSellAllPlayerGUIEnabled){
                return false;
            }

            if (isSellAllPlayerGUIPermissionEnabled && !p.hasPermission(permissionPlayerGUI)){
                return false;
            }

            SellAllPlayerGUI gui = new SellAllPlayerGUI( p, page, cmdPage, cmdReturn );
            gui.open();
            return true;
        }

        SellAllAdminGUI gui = new SellAllAdminGUI( p, page, cmdPage, cmdReturn );
        gui.open();
        return true;
    }
}
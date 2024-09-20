package tech.mcprison.prison.spigot.sellall;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.sellall.events.PrePlayerSellAllEvent;
import tech.mcprison.prison.sellall.messages.SpigotVariousGuiMessages;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
//import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotCommandSender;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllPlayerGUI;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;
import tech.mcprison.prison.util.Text;

/**
 * @author AnonymousGCA (GABRYCA)
 * */
public class SellAllUtil 
	extends SpigotVariousGuiMessages {

    private static SellAllUtil instance;
    
//    private final Compatibility compat = SpigotPrison.getInstance().getCompatibility();

//    private final ItemStack lapisLazuli = compat.getLapisItemStack();
    public Configuration sellAllConfig;
    
    private HashMap<String, PrisonBlock> sellAllItems;
//    private HashMap<XMaterial, Double> sellAllBlocks;
    
    private HashMap<String, Double> sellAllPrestigeMultipliers;
    private HashMap<Player, Double> autoSellEarningsNotificationWaiting = new HashMap<>();
    private ArrayList<XMaterial> sellAllItemTriggers;
    
    private ArrayList<Player> activePlayerDelay = new ArrayList<>();
//    private List<String> sellAllDisabledWorlds;
//    private MessagesConfig messages;
    private double defaultMultiplier;
    private int defaultSellAllDelay;
    private int defaultAutoSellEarningNotificationDelay;
    public Sound sellAllSoundSuccess;
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
    public boolean isSellAllNotificationByActionBar;
    
    public boolean isSellAllSoundEnabled;
    public boolean isSellAllBackpackItemsEnabled;
    public boolean isSellAllMinesBackpacksPluginEnabled;
    public boolean isSellAllHandEnabled;
    
    public boolean isSellAllIgnoreCustomNames = false;

    /**
     * Get cached instance of SellAllUtil, if present, if not then Initialize it, if SellAll is disabled return null.
     *
     * @return SellAllUtil.
     * */
    public static SellAllUtil get() {
        if ( !SpigotPrison.getInstance().isSellAllEnabled() ){
            return null;
        }
        if (instance == null){
            instance = new SellAllUtil();
            instance.initCachedData();
        }
        return instance;
    }
    
    /**
     * Init options that will be cached.
     * */
    private void initCachedData() {
        sellAllConfig = SpigotPrison.getInstance().updateSellAllConfig();
//        messages = SpigotPrison.getInstance().getMessagesConfig();
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
        sellAllSignTag = Text.translateAmpColorCodes(sellAllConfig.getString("Options.SellAll_Sign_Visible_Tag") );
//        sellAllBlocks = initSellAllBlocks();
        sellAllItems = initSellAllItems();
        
        sellAllPrestigeMultipliers = initPrestigeMultipliers();
        sellAllItemTriggers = initSellAllItemTrigger();
//        sellAllDisabledWorlds = initSellAllDisabledWorlds();
        defaultMultiplier = Double.parseDouble(sellAllConfig.getString("Options.Multiplier_Default"));
        defaultSellAllDelay = Integer.parseInt(sellAllConfig.getString("Options.Sell_Delay_Seconds"));
        defaultAutoSellEarningNotificationDelay = Integer.parseInt(sellAllConfig.getString("Options.Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Delay_Seconds"));
        isPerBlockPermissionEnabled = getBooleanValue("Options.Sell_Per_Block_Permission_Enabled");
        isAutoSellEnabled = getBooleanValue("Options.Full_Inv_AutoSell");
        isAutoSellNotificationEnabled = getBooleanValue("Options.Full_Inv_AutoSell_Notification");
        isAutoSellEarningNotificationDelayEnabled = getBooleanValue("Options.Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Enabled");
        isAutoSellPerUserToggleable = getBooleanValue("Options.Full_Inv_AutoSell_perUserToggleable");
        isAutoSellPerUserToggleablePermEnabled = getBooleanValue("Options.Full_Inv_AutoSell_perUserToggleable_Need_Perm");
        isSellAllNotificationEnabled = getBooleanValue("Options.Sell_Notify_Enabled");
        isSellAllNotificationByActionBar = getBooleanValue("Options.Sell_Notify_by_ActionBar");
        
        isSellAllSoundEnabled = getBooleanValue("Options.Sell_Sound_Enabled");
        isSellAllBackpackItemsEnabled = getBooleanValue("Options.Sell_Prison_BackPack_Items");
        isSellAllMinesBackpacksPluginEnabled = getBooleanValue("Options.Sell_MinesBackPacks_Plugin_Backpack");
        isSellAllDelayEnabled = getBooleanValue("Options.Sell_Delay_Enabled");
        
        isSellAllSellPermissionEnabled = getBooleanValue("Options.Sell_Permission_Enabled");
        isSellAllItemTriggerEnabled = getBooleanValue("Options.ShiftAndRightClickSellAll.Enabled");
        isSellAllItemTriggerPermissionEnabled = getBooleanValue("Options.ShiftAndRightClickSellAll.PermissionEnabled");
        isSellAllGUIEnabled = getBooleanValue("Options.GUI_Enabled");
        isSellAllPlayerGUIEnabled = getBooleanValue("Options.Player_GUI_Enabled");
        isSellAllGUIPermissionEnabled = getBooleanValue("Options.GUI_Permission_Enabled");
        isSellAllPlayerGUIPermissionEnabled = getBooleanValue("Options.Player_GUI_Permission_Enabled");
        isSellAllMultiplierEnabled = getBooleanValue("Options.Multiplier_Enabled");
        isSellAllPermissionMultiplierOnlyHigherEnabled = getBooleanValue("Options.Multiplier_Permission_Only_Higher");
        isSellAllSignEnabled = getBooleanValue("Options.SellAll_Sign_Enabled");
        isSellAllSignNotifyEnabled = getBooleanValue("Options.SellAll_Sign_Notify");
        isSellAllSignPermissionToUseEnabled = getBooleanValue("Options.SellAll_Sign_Use_Permission_Enabled");
        isSellAllBySignOnlyEnabled = getBooleanValue("Options.SellAll_By_Sign_Only");
        isSellAllHandEnabled = getBooleanValue("Options.SellAll_Hand_Enabled");
        
        isSellAllIgnoreCustomNames = getBooleanValue("Options.SellAll_ignoreCustomNames", false);
    }
    
    private boolean getBooleanValue( String configName ) {
    	return getBooleanValue(configName, false);
    }
    private boolean getBooleanValue( String configName, Boolean defaultValue ) {
    	boolean results = (defaultValue == null ? false : defaultValue.booleanValue() );
    	
    	if ( configName != null ) {
    		if ( sellAllConfig.isString(configName) ) {
    			String boolVal = sellAllConfig.getString(configName);
    			if ( boolVal != null ) {
    				// Boolean.parseBoolean() also supports yes and no so don't pretest for true/false.
    				try {
						results = Boolean.parseBoolean(boolVal);
					} catch (Exception e) {
						// Not a boolean value, so ignore and let the "defaut" value stand
					}
    			}
    			else {
    				// ignore since it's not a boolean value and let the "default" value stand
    			}
    		}
    		else if ( sellAllConfig.isBoolean(configName) ) {
    			results = sellAllConfig.getBoolean(configName, results);
    		}
    		else {
    			// Ignore since the config is not boolean or a String that "could" be a boolean
    		}
    	}
    	
    	return results;
    }

//    /**
//     * Return boolean value from String.
//     *
//     * @return boolean.
//     * */
//    private static boolean getBoolean(String string) {
//        return string != null && string.equalsIgnoreCase("true");
//    }

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

//    /**
//     * Get SellAll XMaterial or Lapis (As Lapis is weird) from an ItemStack.
//     *
//     * @param itemStack - ItemStack.
//     *
//     * @return XMaterial.
//     * */
//    private XMaterial getXMaterialOrLapis(ItemStack itemStack) {
//    	XMaterial results = null;
//    	
//    	String altName = null;
//    	
//    	if ( itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ) {
//    		altName = itemStack.getItemMeta().getDisplayName();
//    	}
//    	
//    	if ( altName == null || isSellAllIgnoreCustomNames ) {
//    		XMaterial xMat = null;
//    		
//    		if (itemStack.isSimilar(lapisLazuli)) {
//    			xMat = XMaterial.LAPIS_LAZULI;
//    		}
//    		else {
//    			
//    			try
//    			{
//    				xMat = XMaterial.matchXMaterial(itemStack);
//    			}
//    			catch ( Exception e )
//    			{
//    				// ignore... it is not normal matertial so it cannot be sold
//    			}
//    		}
//    		if ( xMat != null ) {
//    			
//    			// When converted over to be based upon a String name, instead of XMaterial, 
//    			// the altName will take priority over the XMaterial name.
//    			results = xMat;
//    		}
//    	}
//        
//        return results;
//    }

//    /**
//     * Return SellAll Blocks HashMap cached values.
//     *
//     * @return HashMap of XMaterial-Double.
//     * */
//    public HashMap<XMaterial, Double> getSellAllBlocks() {
//        return sellAllBlocks;
//    }

    /**
     * 
     * @return HashMap<String, PrisonBlock>
     */
    public HashMap<String, PrisonBlock> getSellAllItems() {
		return sellAllItems;
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

//    /**
//     * Get HashMap of XMaterials and amounts from an Inventory.
//     *
//     * @param inv - Inventory.
//     *
//     * @return HashMap of XMaterials and Integers.
//     * */
//    private HashMap<XMaterial, Integer> getXMaterialsHashMapFromInventory(Inventory inv){
//
//        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
//        for (ItemStack itemStack : inv.getContents()){
//            if (itemStack != null){
//                XMaterial xMaterial = getXMaterialOrLapis(itemStack);
//                if ( xMaterial != null ) {
//                	
//                	if (xMaterialIntegerHashMap.containsKey(xMaterial) && xMaterialIntegerHashMap.get(xMaterial) != 0){
//                		xMaterialIntegerHashMap.put(xMaterial, xMaterialIntegerHashMap.get(xMaterial) + itemStack.getAmount());
//                	} 
//                	else {
//                		xMaterialIntegerHashMap.put(xMaterial, itemStack.getAmount());
//                	}
//                }
//            }
//        }
//
//        return xMaterialIntegerHashMap;
//    }


//    /**
//     * get HashMap of XMaterials and Amounts from an ArrayList of ItemStacks.
//     *
//     * @param itemStacks - ArrayList of ItemStacks.
//     *
//     * @return HashMap of XMaterials and Integers.
//     * */
//    private HashMap<XMaterial, Integer> getXMaterialsHashMapFromArrayList(ArrayList<ItemStack> itemStacks){
//
//        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
//        for (ItemStack itemStack : itemStacks){
//            if (itemStack != null){
//                try {
//                    XMaterial xMaterial = getXMaterialOrLapis(itemStack);
//                    if ( xMaterial != null ) {
//                    	
//                    	if (xMaterialIntegerHashMap.containsKey(xMaterial) && xMaterialIntegerHashMap.get(xMaterial) != 0) {
//                    		xMaterialIntegerHashMap.put(xMaterial, xMaterialIntegerHashMap.get(xMaterial) + itemStack.getAmount());
//                    	} 
//                    	else {
//                    		xMaterialIntegerHashMap.put(xMaterial, itemStack.getAmount());
//                    	}
//                    }
//                } catch (IllegalArgumentException ignored){}
//            }
//        }
//
//        return xMaterialIntegerHashMap;
//    }

    /**
     * Get SellAll Player Multiplier.
     *
     * @param p - Player.
     *
     * @return double.
     * */
    public double getPlayerMultiplier(Player p){

        if (!isSellAllMultiplierEnabled){
            return 1d;
        }

//        long tPoint1 = System.nanoTime();
        
        SpigotPlayer sPlayer = new SpigotPlayer(p);

        double multiplier = 0d;
        
        if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
        	
        	RankPlayer rPlayer = sPlayer.getRankPlayer();
        	
        	Set<RankLadder> keys = rPlayer.getLadderRanks().keySet();
        	for (RankLadder ladderKey : keys) {
        		PlayerRank pRank = rPlayer.getLadderRanks().get(ladderKey);
        		String rankName = pRank.getRank().getName();
        		
        		String multiplierRankString = sellAllConfig.getString("Multiplier." + rankName + ".MULTIPLIER");
        		if (multiplierRankString != null && sellAllPrestigeMultipliers.containsKey( rankName )){
        			multiplier += sellAllPrestigeMultipliers.get( rankName );
        		}
			}
        }
        
        if ( multiplier == 0 ) {
        	multiplier = defaultMultiplier;
        }
        
        
        

//        long tPoint2 = System.nanoTime();
        
        // Get multiplier depending on Player + Prestige. NOTE that prestige multiplier will replace
        // the actual default multiplier.
//        if (module != null) {
//            PrisonRanks rankPlugin = (PrisonRanks) module;
//            if (rankPlugin.getPlayerManager().getPlayer(sPlayer) != null) {
//                String playerRankName;
//                try {
//                	
//                	RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
//                	
//                    RankPlayer rankPlayer = rankPlugin.getPlayerManager().getPlayer(sPlayer);
//                    PlayerRank pRank = rankPlayer == null ? null : rankPlayerFactory.getRank( rankPlayer, "prestiges");
//                    Rank rank = pRank == null ? null : pRank.getRank();
//
//                    playerRankName = rank == null ? null : rank.getName();
//                } catch (NullPointerException ex) {
//                    playerRankName = null;
//                }
//                if (playerRankName != null) {
//                    String multiplierRankString = sellAllConfig.getString("Multiplier." + playerRankName + ".MULTIPLIER");
//                    if (multiplierRankString != null && sellAllPrestigeMultipliers.containsKey(playerRankName)){
//                        multiplier = sellAllPrestigeMultipliers.get(playerRankName);
//                    }
//                }
//            }
//        }

        // Get Multiplier from multipliers permission's if there's any.
        List<String> perms = sPlayer.getPermissions("prison.sellall.multiplier.");
        double multiplierExtraByPerms = 0;
        for (String multByPerm : perms) {
        	
            double multByPermDouble = Double.parseDouble(multByPerm.substring(26));
            
            if ( !isSellAllPermissionMultiplierOnlyHigherEnabled ) {
                multiplierExtraByPerms += multByPermDouble;
            } 
            else if (multByPermDouble > multiplierExtraByPerms) {
                multiplierExtraByPerms = multByPermDouble;
            }
        }
        
        multiplier += multiplierExtraByPerms;

//        long tPoint3 = System.nanoTime();
//        DecimalFormat dFmt = Prison.get().getDecimalFormat( "0.0000" );
//        String debugMsg = "{sellallMult::" + dFmt.format( multiplier ) + ":t1=" + 
//        				dFmt.format( (tPoint2 - tPoint1)/1000000d ) +
//        				":t2=" + dFmt.format( (tPoint3 - tPoint2)/1000000 ) + "}";
//        Output.get().logDebug( debugMsg );
        
        return multiplier;
    }

    
    public List<String> getPlayerMultiplierList( Player p ) {
    	List<String> results = new ArrayList<>();
    	

        if (!isSellAllMultiplierEnabled) {
            results.add( "1.0 - Sellall multipler is disabled. Default value." );
            
            return results;
        }

        DecimalFormat dFmt = Prison.getDecimalFormatStaticDouble();

        SpigotPlayer sPlayer = new SpigotPlayer(p);

        if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
        	
        	RankPlayer rPlayer = sPlayer.getRankPlayer();
        	
        	Set<RankLadder> keys = rPlayer.getLadderRanks().keySet();
        	for (RankLadder ladderKey : keys) {
        		PlayerRank pRank = rPlayer.getLadderRanks().get(ladderKey);
        		String rankName = pRank.getRank().getName();
        		
        		String multiplierRankString = sellAllConfig.getString("Multiplier." + rankName + ".MULTIPLIER");
        		if (multiplierRankString != null && sellAllPrestigeMultipliers.containsKey( rankName )){
        			double mult = sellAllPrestigeMultipliers.get( rankName );
        			String msg = String.format( 
        					"%s - %s rank Sellall multiplier",
        					dFmt.format(mult), rankName
        					);
        			results.add( msg );
        		}
			}
        }
        
        if ( results.size() == 0 ) {
        	String msg = String.format( 
        			"%s - default Sellall multiplier - no rank mulitiplers",
        			dFmt.format(defaultMultiplier)
        			);
        	results.add(msg);
        }
        

        // Get Multiplier from multipliers permission's if there's any.
        List<String> perms = sPlayer.getPermissions("prison.sellall.multiplier.");
        double multiplierExtraByPerms = 0;
        int count = 0;
        String greatestPerm = "";
        for (String multByPerm : perms) {
        	
            double multByPermDouble = Double.parseDouble(multByPerm.substring(26));
            
            if ( !isSellAllPermissionMultiplierOnlyHigherEnabled ) {
                multiplierExtraByPerms += multByPermDouble;
                
                String msg = String.format( 
                		"%s - Sellall permission multiplier - %s",
                		dFmt.format(multByPermDouble), multByPerm
                		);
                results.add(msg);
            } 
            else if (multByPermDouble > multiplierExtraByPerms) {
                multiplierExtraByPerms = multByPermDouble;
                count++;
                greatestPerm = multByPerm;
            }
        }
        
        if ( isSellAllPermissionMultiplierOnlyHigherEnabled ) {
        	
        	String msg = String.format( 
        			"%s - Sellall permission multiplier - greatest out of %d - %s",
        			dFmt.format(multiplierExtraByPerms), count, greatestPerm
        			);
        	results.add(msg);
        }
    	
    	return results;
    }
    
    
//    /**
//     * Get SellAll Money to give, it requires Player because of SellAll perBlockPermission as an option.
//     * NOTE: This WON'T remove blocks from the HashMap when sold, and won't edit Inventories of Players,
//     * but only return the amount of money that the Player would get if he sells now everything from the
//     * specified HashMap of XMaterials and Integers.
//     *
//     * Will also calculate the Multiplier of the Player.
//     *
//     * Get SellAll Sell value of HashMap values.
//     * NOTE: If there aren't blocks in the SellAll shop this will return 0.
//     * NOTE: This method WON'T remove blocks from HashMap, but only return a double value.
//     *
//     * @param p - Player.
//     * @param xMaterialIntegerHashMap - HashMap of XMaterial-Integer (Blocks of origin).
//     *
//     * @return double.
//     * */
//    private double getSellMoney(Player p, HashMap<XMaterial, Integer> xMaterialIntegerHashMap){
//    	StringBuilder sb = new StringBuilder();
//    	boolean debug = Output.get().isDebug();
//
//        if (sellAllItems.isEmpty()){
//            return 0;
//        }
//
//        double multiplier = getPlayerMultiplier(p);
//        
//        
//        double earned = 0;
//        for (HashMap.Entry<XMaterial, Integer> xMatEntry : xMaterialIntegerHashMap.entrySet()){
//        	
//        	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( xMatEntry.getKey().name() );
//
//        	String key = pBlockKey.getBlockName();
//        	
//            if (sellAllItems.containsKey( key )){
//                // This is stupid but right now I'm too confused, sorry.
//                if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + key )){
//                    // Nothing will change.
//                } 
//                else {
//                	PrisonBlock pBlock = sellAllItems.get(key);
//                	
////                	XMaterial xMat = xMatEntry.getKey();
//                	int qty = xMatEntry.getValue();
//                	double value = pBlock.getSalePrice();
//                	
//                	if ( debug ) {
//                		
//                		if ( sb.length() > 0 ) {
//                			sb.append(", ");
//                		}
//                		sb.append( key.toLowerCase() ).append(":")
//                			.append( qty ).append("@").append(value);
//                	}
//                	
//                    earned += qty * value;
//                }
//            }
//        }
//
//        double total = earned * multiplier;
//        
//        if ( debug ) {
//        	DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.00" );
//        	sb.append( " earned: " ).append( dFmt.format(earned) )
//        	  .append( " mult: " ).append( dFmt.format(multiplier) )
//        	  .append( " total: " ).append( dFmt.format(total) );
//        	String message = String.format( 
//        			"Sellall.getSellMoney: %s %s", 
//        			p.getName(), sb.toString() );
//        	Output.get().logInfo(message);
//        }
//        
//        return total;
//    }

//    /**
//     * Get SellAll Money to give, it requires Player because of SellAll perBlockPermission as an option.
//     * NOTE: This WON'T remove blocks from the ArrayList when sold, and won't edit Inventories of Players,
//     * but only return the amount of money that the Player would get if he sells now everything from the
//     * specified ArrayList of ItemStacks.
//     *
//     * Will also calculate the Multiplier of the Player.
//     *
//     * Get SellAll Sell value of ArrayList of itemstack.
//     * NOTE: If there aren't blocks in the SellAll shop this will return 0.
//     * NOTE: This method WON'T remove blocks from HashMap, but only return a double value.
//     *
//     * @param p - Player.
//     * @param itemStacks - ArrayList of ItemStacks (Blocks of origin).
//     *
//     * @return double.
//     * */
//    private double getSellMoney(Player p, ArrayList<ItemStack> itemStacks){
//        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = getXMaterialsHashMapFromArrayList(itemStacks);
//        return getSellMoney(p, xMaterialIntegerHashMap);
//    }
    
    
    public double getPlayerInventoryValue( SpigotPlayer sPlayer ) {
    	double value = 0;
    	
    	double multiplier = getPlayerMultiplier(sPlayer.getWrapper());

    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();

    	List<SellAllData> soldItems = valueOfInventoryItems( spInventory, multiplier );
    	for (SellAllData soldItem : soldItems) {
			value += soldItem.getTransactionAmount();
		}
    	
    	return value;
    }
    
    public String getPlayerInventoryValueReport( SpigotPlayer sPlayer ) {
    	
    	double multiplier = getPlayerMultiplier(sPlayer.getWrapper());
    	
    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();
    	
    	List<SellAllData> soldItems = valueOfInventoryItems( spInventory, multiplier );
    	
    	String report = SellAllData.itemsSoldReport(soldItems, sPlayer, multiplier);
    	
    	return report;
    }
    
    public List<SellAllData> getPlayerInventoryValueTransactions( SpigotPlayer sPlayer ) {
    	
    	double multiplier = getPlayerMultiplier(sPlayer.getWrapper());
    	
    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();
    	
    	List<SellAllData> soldItems = valueOfInventoryItems( spInventory, multiplier );
    	
    	return soldItems;
    }
    
    
    
    public double getItemStackValue( SpigotPlayer player, SpigotItemStack itemStack ) {
    	
    	double multiplier = getPlayerMultiplier(player.getWrapper());
    	
    	SellAllData sad = sellItemStack( itemStack, multiplier );
    	
    	return sad == null ? 0 : sad.getTransactionAmount();
    }
    
    public String getItemStackValueReport( SpigotPlayer sPlayer, SpigotItemStack itemStack ) {
    	
    	double multiplier = getPlayerMultiplier(sPlayer.getWrapper());
    	
    	List<SellAllData> soldItems = new ArrayList<>();
    	
    	SellAllData sad = sellItemStack( itemStack, multiplier );
    	if ( sad != null ) {
    		
    		soldItems.add( sad );
    	}
    	
    	String report = SellAllData.itemsSoldReport(soldItems, sPlayer, multiplier);
    	
    	return report;
    }
    
    public List<SellAllData> getItemStackValueTransactions( SpigotPlayer sPlayer, SpigotItemStack itemStack ) {
    	
    	double multiplier = getPlayerMultiplier(sPlayer.getWrapper());
    	
    	List<SellAllData> soldItems = new ArrayList<>();
    	
    	SellAllData sad = sellItemStack( itemStack, multiplier );
    	if ( sad != null ) {
    		
    		soldItems.add( sad );
    	}
    	
    	return soldItems;
    }
    
    /** 
     * <p>This sells the players inventory, and removes what is sold.
     * This returns the transaction logs within the SellAllData obects.
     * </p>
     * 
     * @param p
     * @return
     */
    public List<SellAllData> sellPlayerItems(Player p) {
    	
    	double multiplier = getPlayerMultiplier(p);
    	
    	SpigotPlayer sPlayer = new SpigotPlayer( p );
    	
    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();

        // Pass onto event bus for any plugins to edit/cancel the sale.
        PrePlayerSellAllEvent preSaleEvent = new PrePlayerSellAllEvent( sPlayer, spInventory );
        Prison.get().getEventBus().post(preSaleEvent);

        if ( preSaleEvent.isCanceled() ) {
            return new ArrayList<>();
        }

        return sellInventoryItems( spInventory, multiplier );
    }

    /**
     * <p>This function sells the Item Stacks and returns a transaction log of items sold.
     * This is an internal function. No messages are sent, most sellall options are bypassed,
     * etc... Use at your own risk.
     * </p>
     * 
     * <p>The parameter itemStacks will have the items sold removed.  The sold items will 
     * be reflected in the returned transaction logs.
     * </p>
     * 
     * @param p
     * @param itemStacks
     * @return
     */
    public List<SellAllData> sellPlayerItemStacks(Player p, 
    					List<SpigotItemStack> itemStacks ) {
    	
    	double multiplier = getPlayerMultiplier(p);
    	
    	List<SellAllData> soldItems = new ArrayList<>();

    	if ( itemStacks != null ) {
    		
    		List<tech.mcprison.prison.internal.ItemStack> removable = new ArrayList<>();
    		
    		// Go through all of the player's inventory and identify what can be sold.
    		// 1. if it can be sold, then create a SellAllData "receipt"
    		// 2. Add sad to the soldItems List
    		// 3. Add the ItemStack to the removable List to be removed later
    		for ( SpigotItemStack inv : itemStacks ) {
    			
    			SellAllData sad = sellItemStack( inv, multiplier );
    				
    			if ( sad != null ) {
    				
    				sad.setItemsSold( true );
    				
    				soldItems.add(sad);
    				
    				removable.add(inv);
    			}
    		}
    		
    		// We've identified all that could be sold, now remove them from the player's inventory
    		for (tech.mcprison.prison.internal.ItemStack itemStack : removable) {
    			itemStacks.remove( itemStack );
    		}
    	}
    	
		return soldItems;
    }
    
    
    private List<SellAllData> sellInventoryItems( tech.mcprison.prison.internal.inventory.Inventory inventory, 
    					double multiplier ) {
    	List<SellAllData> soldItems = new ArrayList<>();

    	if ( inventory != null ) {
    		
    		List<tech.mcprison.prison.internal.ItemStack> removable = new ArrayList<>();
    		
    		// Go through all of the player's inventory and identify what can be sold.
    		// 1. if it can be sold, then create a SellAllData "receipt"
    		// 2. Add sad to the soldItems List
    		// 3. Add the ItemStack to the removable List to be removed later
    		for ( tech.mcprison.prison.internal.ItemStack inv : inventory.getItems() ) {
    			
    			SellAllData sad = sellItemStack( (SpigotItemStack)inv, multiplier );
    				
    			if ( sad != null ) {
    				
    				sad.setItemsSold( true );
    				
    				soldItems.add(sad);
    				
    				removable.add(inv);
    			}
    		}
    		
    		// We've identified all that could be sold, now remove them from the player's inventory
    		for (tech.mcprison.prison.internal.ItemStack itemStack : removable) {
    			inventory.removeItem( itemStack );
    		}
    	}
    	
		return soldItems;
    }
    
    private List<SellAllData> valueOfInventoryItems( 
    		tech.mcprison.prison.internal.inventory.Inventory inventory, double multiplier ) {
    	List<SellAllData> soldItems = new ArrayList<>();
    	
    	if ( inventory != null ) {
    		
    		for ( tech.mcprison.prison.internal.ItemStack inv : inventory.getItems() ) {
    			
    			SellAllData sad = sellItemStack( (SpigotItemStack)inv, multiplier );
    			
    			if ( sad != null ) {
    				soldItems.add(sad);
    			}
    		}
    	}
    	
    	return soldItems;
    }
    
    /**
     * <p>This takes an SpigotItemStack and generates a SellAllData items with
     * the transaction amount based upon how many items are in the itemStack, 
     * times the salePrice, times the player's multiplier.
     * This function DOES NOT remove anything from any ItemStack, any Inventory,
     * or any player's inventory. This function does not pay the player
     * anything.  This just generates the transaction.
     * </p>
     * 
     * @param iStack
     * @param multiplier
     * @return
     */
    private SellAllData sellItemStack( SpigotItemStack iStack, double multiplier ) {
    	SellAllData soldItem = null;
    	
    	if ( iStack != null ) {
    		
    		// This converts a bukkit ItemStackk to a PrisonBlock, and it also sets up the
    		// displayName if that is set on the itemStack.
    		PrisonBlock pBlockInv = iStack.getMaterial();
    		
    		PrisonBlock pBlockSellAll = sellAllItems.get( pBlockInv.getBlockNameSearch() );
    		
    		if ( pBlockSellAll != null ) {
    			
    			if ( !pBlockSellAll.isLoreAllowed() && iStack.getLore().size() > 0 ) {
    				String msg = String.format(
    						"Sellall: Cannot sell item '%s' (qty %s) because it has lore which is not allowed. ",
    						iStack.getDisplayName(), 
    						Integer.toString( iStack.getAmount() )
    						);
    				if ( Output.get().isDebug() ) {
    					Output.get().logInfo( msg );
    				}
    				
    			}
    			else {
    				
    				double amount = iStack.getAmount() * pBlockSellAll.getSalePrice() * multiplier;
    				soldItem = new SellAllData( pBlockSellAll, iStack.getAmount(), amount );
    			}
    		}
    	}
    	
    	return soldItem;
    }
    
//    /**
//     * Get SellAll Sell Money, calculated from all the enabled backpacks (from sellAll config and integrations) and
//     * main inventory.
//     * NOTE: This WON'T remove blocks from Inventories/Backpacks, but only return their value.
//     *
//     * Will also calculate the Multiplier of the Player.
//     *
//     * NOTE: If there aren't blocks in the SellAll shop this will return 0.
//     * NOTE: This method WON'T remove blocks from HashMap, but only return a double value.
//     *
//     * @param p - Player.
//     *
//     * @return double.
//     * */
//    public double getSellMoney(Player p){
//
//        if (sellAllItems.isEmpty()){
//            return 0;
//        }
//
//        return getSellMoney(p, getHashMapOfPlayerInventories(p));
//    }
    
//    /**
//     * <p>This sells a specific item stack.  Actually it returns the value of an item stack,
//     * its up to the calling function to dispose of the contents if the result is non-zero.
//     * </p>
//     * @param p
//     * @param itemStack
//     * @return
//     */
//    private double getSellMoney( Player p, SpigotItemStack itemStack )
//	{
//    	double results = 0d;
//    	
//    	// Either ignore custom names, or if isSellAllIgnoreCustomNames is set, then allow them
//    	// to be processed as they used to be processed.
//    	
//    	// For now, do not sell custom blocks since this sellall is based upon
//    	// XMaterial and custom blocks cannot be represented by XMaterial so
//    	// it will sell it as the wrong material
//    	if ( isSellAllIgnoreCustomNames ||
//    			itemStack.getMaterial().getBlockType() == null ||
//    			itemStack.getMaterial().getBlockType() == PrisonBlockType.minecraft ) {
//    		
//    		HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
//    		
//    		PrisonBlock pBlock = itemStack.getMaterial();
//    		
//    		XMaterial xMat = SpigotCompatibility.getInstance().getXMaterial( pBlock );
//    		
//    		if ( xMat != null ) {
//    			xMaterialIntegerHashMap.put( xMat, itemStack.getAmount() );
//    			
//    			results = getSellMoney( p, xMaterialIntegerHashMap );
//    		}
//    	}
//    	
//    	
//		return results;
//	}

    
//    /**
//     * <p>This gets the player's inventory, ignoring the armor slots.</p>
//     * 
//     * @param p
//     * @return
//     */
//    private List<ItemStack> getPlayerInventory( Player p ) {
//    	
//    	return getPlayerInventory( p.getInventory() );
//    	
////    	List<ItemStack> results = new ArrayList<>();
////    	
////    	PlayerInventory inv = p.getInventory();
////    	
////    	for ( ItemStack iStack : inv.getStorageContents() ) {
////    		if ( iStack != null ) {
////    			results.add(iStack);
////    		}
////    	}
////    	for ( ItemStack iStack : inv.getExtraContents() ) {
////    		if ( iStack != null ) {
////    			results.add(iStack);
////    		}
////    	}
////    	
////    	return results;
//    }
//    private List<ItemStack> getPlayerInventory( PlayerInventory inv ) {
//    	List<ItemStack> results = new ArrayList<>();
//    	
//    	for ( ItemStack iStack : inv.getContents() ) {
//    		if ( iStack != null ) {
//    			results.add(iStack);
//    		}
//    	}
//    	
//    	try {
//			for ( ItemStack iStack : inv.getExtraContents() ) {
//				if ( iStack != null ) {
//					results.add(iStack);
//				}
//			}
//		} catch (NoSuchMethodError e) {
//			// Ignore on older versions of spigot... Spigot 1.8.8 does not have this function.
//		}
//    	
//    	// then remove the armor ItemStacks:
//    	for ( ItemStack iStack : inv.getArmorContents() ) {
//    		if ( iStack != null ) {
//    			results.remove(iStack);
//    		}
//    	}
//    	
//    	return results;
//    }
    
//    /**
//     * Get HashMap with all the items of a Player.
//     *
//     * Return HashMap of XMaterial-Integer.
//     *
//     * @param p - Player.
//     *
//     * @return HashMap - XMaterial-Integer.
//     * */
//    private HashMap<XMaterial, Integer> getHashMapOfPlayerInventories(Player p) {
//    	
//        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = new HashMap<>();
//        
//        if (isSellAllBackpackItemsEnabled && getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks"))){
//        	
//            BackpacksUtil backpacksUtil = BackpacksUtil.get();
//            if (backpacksUtil.isMultipleBackpacksEnabled()){
//                for (String id : backpacksUtil.getBackpacksIDs(p)){
//                    xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, backpacksUtil.getBackpack(p, id));
//                }
//            } else {
//            	String id = null;
//                xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, 
//                		backpacksUtil.getBackpack(p, id));
//            }
//        }
//
//        if (isSellAllMinesBackpacksPluginEnabled && IntegrationMinepacksPlugin.getInstance().isEnabled()){
//            Backpack backpack = IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p);
//            if (backpack != null) {
//                xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, backpack.getInventory());
//                backpack.setChanged();
//                backpack.save();
//            }
//        }
//
//        xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, getPlayerInventory( p ));
////        xMaterialIntegerHashMap = addInventoryToHashMap(xMaterialIntegerHashMap, p.getInventory());
//        return xMaterialIntegerHashMap;
//    }

//    /**
//     * If autosell is enabled, and if user toggleable is enabled, then
//     * it will check to see if the player has the perm or 
//     * 
//     * Get AutoSell Player toggle if available.
//     * If he enabled it, AutoSell will work, otherwise it won't.
//     * If he never used the toggle command, this will return true, just like if he enabled it in the first place.
//     *
//     * @param p - Player.
//     *
//     * @return boolean.
//     * */
//    public boolean isPlayerAutoSellEnabled(Player p){
//    	boolean results = false;
//    	
//    	// If autosell isn't enabled, then return false
//    	if ( isAutoSellEnabled ) {
//    		
//    		results =  isSellallPlayerUserToggleEnabled( p );
////    		if ( !isAutoSellPerUserToggleablePermEnabled ||
////    			 isAutoSellPerUserToggleablePermEnabled && 
////    				p.hasPermission(permissionAutoSellPerUserToggleable)){
////    			
////    			String settingName = "Users." + p.getUniqueId() + ".isEnabled";
////    			
////    			results = sellAllConfig.getString(settingName) == null ||
////    					getBooleanValue( settingName );
////    		}
//    	}
//    		
//    	
////        if (isAutoSellPerUserToggleablePermEnabled && 
////        		!p.hasPermission(permissionAutoSellPerUserToggleable)){
////            return false;
////        }
////
////        if (sellAllConfig.getString("Users." + p.getUniqueId() + ".isEnabled") == null){
////            return true;
////        }
//
////        return getBooleanValue("Users." + p.getUniqueId() + ".isEnabled");
//	    return results;
//    }
    
    /**
     * <p>This function only checks to see if the user can toggle autosell
     * on or off.  If they can, then it checks the state to see if it's on
     * or off.  It does not matter if autosell is enabled within sellall or not,
     * since this can be used with the auto features autosell too.
     * </p>
     * 
     * 
     * 
     * @param p
     * @return
     */
    public boolean isSellallPlayerUserToggleEnabled( Player p ) {
    	boolean results = false;
    	
    	// If autosell isn't enabled, then return false
    	if ( isAutoSellEnabled ) {
    		
    		if ( isAutoSellPerUserToggleable ) {
    			
    			if ( !isAutoSellPerUserToggleablePermEnabled ||
    					isAutoSellPerUserToggleablePermEnabled && 
    					p.hasPermission(permissionAutoSellPerUserToggleable)){
    				
    				String settingName = "Users." + p.getUniqueId() + ".isEnabled";
    				
    				results = sellAllConfig.getString( settingName ) == null ||
    						getBooleanValue( settingName );
    			}
    		}
    	}
    	
    	return results;
    }
    
//    public boolean checkIfPlayerAutosellIsActive(Player p) {
//    	boolean results = isAutoSellEnabled;
//    	
//    	if ( isAutoSellPerUserToggleable ) { 
//    		results = isPlayerAutoSellEnabled(p);
//    	}
//    	
//    	return results;
//    }

//    /**
//     * WARNING: Obsolete because disabled worlds are set in config.yml and 
//     *          the command handler shuts down in those worlds.  So it will
//     *          never run any sellall commands in a diabled world.
//     *          
//     * Check if Player is in a disabled world, where he can't use sellall sell.
//     *
//     * Return True if he's in a disabled world, False if not.
//     *
//     * @return boolean.
//     * */
//    public boolean isPlayerInDisabledWorld(Player p){
//        return sellAllDisabledWorlds.contains(p.getWorld().getName());
//    }

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
    	
    	initCachedData();
    	
//        sellAllConfig = SpigotPrison.getInstance().updateSellAllConfig();
    }

 

    /**
     * Loads sellAll blocks from SellAllConfig.yml
     * With XMaterials and double values (money).
     *
     * @return HashMap<String, PrisonBlock>
     * */
    public HashMap<String, PrisonBlock> initSellAllItems(){

    	HashMap<String, PrisonBlock> sellAllItems = new HashMap<>();
    	
//        HashMap<XMaterial, Double> sellAllXMaterials = new HashMap<>();

        if (sellAllConfig.getConfigurationSection("Items") == null){
            return sellAllItems;
        }

        for (String key : sellAllConfig.getConfigurationSection("Items").getKeys(false)) {

        	String itemName = key.trim().toUpperCase();
        	String itemPrefix = "Items." + itemName;
        	
            String itemID = sellAllConfig.getString( itemPrefix + ".ITEM_ID");

            PrisonBlock pBlock = Prison.get().getPlatform().getPrisonBlock(itemID);

            if ( pBlock != null ) {
            	
            	String itemDisplayName = sellAllConfig.getString( itemPrefix + ".ITEM_DISPLAY_NAME");
            	if ( itemDisplayName != null ) {
            		pBlock.setDisplayName( itemDisplayName );
            	}
            	
            	String saleValueString = sellAllConfig.getString( itemPrefix + ".ITEM_VALUE");
            	if ( saleValueString != null ) {
            		
            		try {
            			double value = Double.parseDouble(saleValueString);
            			pBlock.setSalePrice( value );
            		} catch (NumberFormatException ignored) {
            		}
            	}

            	String purchaseValueString = sellAllConfig.getString( itemPrefix + ".PURCHASE_PRICE");
            	if ( purchaseValueString != null ) {
            		
            		try {
            			double value = Double.parseDouble(purchaseValueString);
            			pBlock.setPurchasePrice( value );
            		} catch (NumberFormatException ignored) {
            		}
            	}
            	
            	String isLoreAllowedString = sellAllConfig.getString( itemPrefix + ".IS_LORE_ALLOWED");
            	boolean isLoreAllowed = false;
            	if ( isLoreAllowedString != null ) {
            		
            		try {
            			isLoreAllowed = Boolean.parseBoolean(isLoreAllowedString);
            		} 
            		catch (NumberFormatException ignored) {
            		}
            	}
            	pBlock.setLoreAllowed( isLoreAllowed );
            	
            	
            	sellAllItems.put( pBlock.getBlockNameSearch(), pBlock );
            }
            
//            Optional<XMaterial> iMatOptional = XMaterial.matchXMaterial(itemID);
//            XMaterial itemMaterial = iMatOptional.orElse(null);

//            if (itemMaterial != null) {
//                String valueString = sellAllConfig.getString("Items." + key.trim().toUpperCase() + ".ITEM_VALUE");
//                if (valueString != null) {
//                    try {
//                        double value = Double.parseDouble(valueString);
//                        sellAllXMaterials.put(itemMaterial, value);
//                    } catch (NumberFormatException ignored) {
//                    }
//                }
//            }
        }
        return sellAllItems;
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

        // NOTE: They key is the same as the rank name... so no need to read the PRESTIGE_NAME or RANK_NAME:
        for (String key : sellAllConfig.getConfigurationSection("Multiplier").getKeys(false)){
            prestigeMultipliers.put( key, 
            		sellAllConfig.getDouble("Multiplier." + key + ".MULTIPLIER"));
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

//    /**
//     * Get List of names of disabled worlds.
//     * If a Player is in one of these worlds, he won't be able to use SellAll.
//     * */
//    public List<String> initSellAllDisabledWorlds(){
//        return sellAllConfig.getStringList("Options.DisabledWorlds");
//    }

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
    public boolean addSellAllBlock(XMaterial xMaterial, double value) {
    	return addSellAllBlock( xMaterial, null, value );
    }
    
    public boolean addSellAllBlock(XMaterial xMaterial, String displayName, double value) {
    	
    	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( xMaterial.name() );
    	if ( pBlockKey == null ) {
    		Output.get().logDebug( "sellall add: invalid block name (%s)", xMaterial.name());
    		return false;
    	}
    	String key = pBlockKey.getBlockNameSearch();
//    	String key = pBlockKey.getBlockName().toLowerCase();
    	
    	PrisonBlock pBlock = sellAllItems.get( key );

    	// If that is an invalid PrisonBlock, then exit
    	if ( pBlock != null ) {
    		Output.get().logDebug( "sellall add: block already exists (%s)", xMaterial.name());
    		return false;
    	}
    	
    	
    	// pBlock is null, but it's being used below. So clone the key:
    	pBlock = pBlockKey.clone();
    	
        try {
        	
        	String itemName = pBlockKey.getBlockName().toUpperCase();
        	
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + itemName + ".ITEM_ID", xMaterial.name());
            conf.set("Items." + itemName + ".ITEM_VALUE", value);
            conf.set("Items." + itemName + ".IS_LORE_ALLOWED", pBlock.isLoreAllowed() );
            
            if ( displayName != null ) {
            	conf.set("Items." + itemName + ".ITEM_DISPLAY_NAME", displayName );
            }
            
            if (getBooleanValue("Options.Sell_Per_Block_Permission_Enabled")) {
            	String itemPerm = "Items." + itemName + ".ITEM_PERMISSION";
                conf.set( itemPerm, sellAllConfig.getString("Options.Sell_Per_Block_Permission") + xMaterial.name());
            }
            conf.save(sellAllFile);
            updateConfig();

            pBlockKey.setSalePrice( value );
            sellAllItems.put( pBlockKey.getBlockNameSearch(), pBlockKey );

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        
//        sellAllBlocks.put(xMaterial, value);
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
     * Add Multiplier to SellAll depending on the Rank (Rank from any ladder).
     *
     * Return true if edited with success, false if error or the rank is not found.
     *
     * @param rankName - Name of the Rank as String.
     * @param multiplier - Double value.
     *
     * @return boolean.
     * */
    public boolean addSellallRankMultiplier(String rankName, double multiplier){

        PrisonRanks rankPlugin = (PrisonRanks) (Prison.get().getModuleManager() == null ? 
        		null : Prison.get().getModuleManager().getModule(PrisonRanks.MODULE_NAME) );
        if (rankPlugin == null) {
            return false;
        }

        
//        boolean isPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges") != null;
//        if (!isPrestigeLadder) {
//            return false;
//        }

        Rank rank = rankPlugin.getRankManager().getRank(rankName);
        
        if ( rank == null ) {
        	// Invalid rank!
            return false;
        }

//        if ( !pRank.getLadder().isPrestiges() ) {
//        	// Rank is not in the prestiges ladder:
//        	return false;
//        }
        
//        boolean isInPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges").containsRank(rankPlugin.getRankManager().getRank(prestigeName));
//        if (!isInPrestigeLadder) {
//            return false;
//        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            
            if ( rank.getLadder().isPrestiges() ) {
            	conf.set("Multiplier." + rank.getName() + ".PRESTIGE_NAME", rank.getName());
            }
            else {
            	conf.set("Multiplier." + rank.getName() + ".RANK_NAME", rank.getName());
            }
            
            conf.set("Multiplier." + rank.getName() + ".MULTIPLIER", multiplier);
            
            conf.save(sellAllFile);
        } 
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        sellAllPrestigeMultipliers.put( rank.getName(), multiplier);
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
//     * variant data is stored in the ItemStack.  SO must use the XMaterial version of this function.
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(
            			SpigotPrison.getInstance(), () -> removeFromDelay(p), 20L * defaultSellAllDelay);
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(
            		SpigotPrison.getInstance(), 
            		() -> removeFromAutoSellDelayAndNotify(p), 20L * defaultAutoSellEarningNotificationDelay);
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

//    private HashMap<XMaterial, Integer> addInventoryToHashMap(HashMap<XMaterial, Integer> xMaterialIntegerHashMap, Inventory inv) {
//    	
//    	List<ItemStack> inventory = new ArrayList<>();
//    	
//    	for (ItemStack itemStack : inv.getContents()){
//            if (itemStack != null){
//            	inventory.add(itemStack);
//            }
//    	}
//    	
//    	return addInventoryToHashMap( xMaterialIntegerHashMap, inventory );
//    	
////        for (ItemStack itemStack : inv.getContents()){
////            if (itemStack != null){
////                XMaterial xMaterial = getXMaterialOrLapis(itemStack);
////                
////                if ( xMaterial != null ) {
////                	
////                	if (xMaterialIntegerHashMap.containsKey(xMaterial)){
////                		xMaterialIntegerHashMap.put(xMaterial, xMaterialIntegerHashMap.get(xMaterial) + itemStack.getAmount());
////                	} 
////                	else {
////                		xMaterialIntegerHashMap.put(xMaterial, itemStack.getAmount());
////                	}
////                }
////            }
////        }
////        return xMaterialIntegerHashMap;
//    }
    
//    private HashMap<XMaterial, Integer> addInventoryToHashMap(HashMap<XMaterial, Integer> xMaterialIntegerHashMap, List<ItemStack> inv) {
//    	for (ItemStack itemStack : inv){
//    		if (itemStack != null){
//    			XMaterial xMaterial = getXMaterialOrLapis(itemStack);
//    			
//    			if ( xMaterial != null ) {
//    				
//    				if (xMaterialIntegerHashMap.containsKey(xMaterial)){
//    					xMaterialIntegerHashMap.put(xMaterial, xMaterialIntegerHashMap.get(xMaterial) + itemStack.getAmount());
//    				} 
//    				else {
//    					xMaterialIntegerHashMap.put(xMaterial, itemStack.getAmount());
//    				}
//    			}
//    		}
//    	}
//    	return xMaterialIntegerHashMap;
//    }

    /**
     * Check if Player meets requirements to use SellAll.
     *
     * This will return true if everything is meet, False if even only isn't.
     * What will be checked is:
     * - OBSOLETE: ~Is in a world where SellAll Sell isn't locked by config.~
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

//        if (isPlayerInDisabledWorld(p)){
//            return false;
//        }

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
    public boolean editPrice(XMaterial xMaterial, double value) {
    	return editPrice( xMaterial, null, value );
    }
    public boolean editPrice(XMaterial xMaterial, String displayName, double value) {

    	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( xMaterial.name() );
    	String key = pBlockKey.getBlockNameSearch();
    	
    	PrisonBlock pBlock = sellAllItems.get( key );
    	
    	// Do not allow an edit price if the material does not exist, or if the value has not changed:
        if ( pBlock == null ){

        	Output.get().logDebug( "sellall edit: item does not exist in shop so it cannot be edited (%s)", pBlockKey.getBlockName());
            return false;
        }
        if ( pBlock.getSalePrice() == value ){
        	DecimalFormat dFmt = new DecimalFormat("#,##0.00");
        	Output.get().logDebug( "sellall edit: No change in price (%s:%s)", 
        			pBlockKey.getBlockName(), dFmt.format(value) );
        	return false;
        }
        
        pBlock.setSalePrice( value );

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            
            String itemName = key.toUpperCase();
            conf.set("Items." + itemName + ".ITEM_ID", key );
            conf.set("Items." + itemName + ".ITEM_VALUE", value);
            conf.set("Items." + itemName + ".IS_LORE_ALLOWED", pBlock.isLoreAllowed() );
            
            if ( displayName != null ) {
            	conf.set("Items." + itemName + ".ITEM_DISPLAY_NAME", value);
            }
            else {
            	//conf.set("Items." + itemName + ".ITEM_DISPLAY_NAME", null);
            }
            
            
            if ( pBlock.getPurchasePrice() != null ) {
            	
            	conf.set("Items." + itemName + ".PURCHASE_PRICE", pBlock.getPurchasePrice().doubleValue() );
            }
            
            if (getBooleanValue("Options.Sell_Per_Block_Permission_Enabled")) {
                conf.set("Items." + itemName + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + itemName );
            }
            conf.save(sellAllFile);

            // Update only if successful
            updateConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        // pBlock is still in the sellAllItems collection so no need to readd it
//        sellAllBlocks.put(xMaterial, value);
        
        return true;
    }
    
    
    public boolean editAllowLore(XMaterial xMaterial, boolean value) {
    	
    	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( xMaterial.name() );
    	String key = pBlockKey.getBlockNameSearch();
    	
    	PrisonBlock pBlock = sellAllItems.get( key );
    	
    	// Do not allow an edit price if the material does not exist, or if the value has not changed:
    	if ( pBlock == null ){
    		
    		Output.get().logDebug( "sellall edit: item does not exist in shop so it cannot be edited (%s)", pBlockKey.getBlockName());
    		return false;
    	}
    	if ( pBlock.isLoreAllowed() == value ){
    		Output.get().logDebug( "sellall edit: No change in 'allow lore' (%s %s)", 
    				pBlockKey.getBlockName(), Boolean.toString( pBlock.isLoreAllowed() ) );
    		return false;
    	}
    	
    	pBlock.setLoreAllowed( value );
    	
    	try {
    		File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
    		FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
    		
    		String itemName = key.toUpperCase();
//    		conf.set("Items." + itemName + ".ITEM_ID", key );
    		conf.set("Items." + itemName + ".IS_LORE_ALLOWED", pBlock.isLoreAllowed() );
    		
    		conf.save(sellAllFile);
    		
    		// Update only if successful
    		updateConfig();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	// pBlock is still in the sellAllItems collection so no need to readd it
//        sellAllBlocks.put(xMaterial, value);
    	
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

        return addSellallRankMultiplier(prestigeName, multiplier);
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

    	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( xMaterial.name() );
    	String key = pBlockKey.getBlockNameSearch();
    	
    	PrisonBlock pBlock = sellAllItems.get( key );
    
        if ( pBlock == null ){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + pBlock.getBlockName().toUpperCase(), null);
            conf.save(sellAllFile);

            updateConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        sellAllItems.remove( key );
//        sellAllBlocks.remove(xMaterial);
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
     * Remove a Rank Multiplier by name.
     *
     * Return true if success, false if error or not found.
     *
     * @param rankName - String.
     *
     * @return boolean.
     * */
    public boolean removeSellallRankMultiplier(String rankName){

        if (!sellAllPrestigeMultipliers.containsKey(rankName)){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + rankName, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            return false;
        }
        sellAllPrestigeMultipliers.remove(rankName);
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

//    /**
//     * Remove Sellable Blocks from HashMap of XMaterial-Integer given as a parameter.
//     * NOTE: If there aren't blocks in the SellAll shop, nothing will change.
//     * NOTE: This method will remove blocks from the HashMap, but it WON'T return the value of money, for that please use
//     * the getSellAllSellMoney method.
//     *
//     * @param p - Player.
//     * @param xMaterialIntegerHashMap - HashMap of XMaterial-Integer (Blocks of origin).
//     *
//     *
//     * @return HashMap - XMaterial-Integer.
//     * */
//    public HashMap<XMaterial, Integer> removeSellableItems(Player p, HashMap<XMaterial, Integer> xMaterialIntegerHashMap){
//
//        if (sellAllItems.isEmpty()){
//            return xMaterialIntegerHashMap;
//        }
//
//        /* Not necessary now, as this only removes what's sellable, this should be checked in another place before.
//        if (isPlayerInDisabledWorld(p)){
//            return xMaterialIntegerHashMap;
//        }
//         */
//
//        for (HashMap.Entry<XMaterial, Integer> xMaterialIntegerEntry : xMaterialIntegerHashMap.entrySet()){
//        	
//        	XMaterial xMaterial = xMaterialIntegerEntry.getKey();
//        	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( xMaterial.name() );
//        	String key = pBlockKey.getBlockName();
//        	
//        	PrisonBlock pBlock = sellAllItems.get( key );
//        	
//            if ( pBlock != null ){
//                // This is stupid but right now I'm too confused, sorry.
//                if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + pBlock.getBlockName().toUpperCase() )){
//                    // Nothing will happen.
//                } else {
//                    xMaterialIntegerHashMap.remove( xMaterial );
//                }
//            }
//        }
//
//        return xMaterialIntegerHashMap;
//    }

//    /**
//     * Remove Sellable Blocks from ArrayList of ItemStacks given as a parameter.
//     * NOTE: If there aren't blocks in the SellAll shop, nothing will change.
//     * NOTE: This method will remove blocks from the HashMap, but it WON'T return the value of money, for that please use
//     * the getSellAllSellMoney method.
//     *
//     * @param p - Player.
//     * @param itemStacks - ItemStacks.
//     *
//     * @return ArrayList - ItemStacks.
//     * */
//    private ArrayList<ItemStack> removeSellableItems(Player p, ArrayList<ItemStack> itemStacks){
//        
//        if (sellAllItems.isEmpty()){
//            return itemStacks;
//        }
//        
//        HashMap<XMaterial, Integer> xMaterialIntegerHashMap = getXMaterialsHashMapFromArrayList(itemStacks);
//
//        xMaterialIntegerHashMap = removeSellableItems(p, xMaterialIntegerHashMap);
//
//        ArrayList<ItemStack> newItemStacks = new ArrayList<>();
//        for (HashMap.Entry<XMaterial, Integer> xMaterialIntegerEntry : xMaterialIntegerHashMap.entrySet()){
//        	
//        	// WARNING: Cannot convert XMaterial to a Material then ItemStack or it will fail on variants 
//        	//          for spigot versions less than 1.13:
//        	
//        	ItemStack iStack = xMaterialIntegerEntry.getKey().parseItem();
//        	if ( iStack != null ) {
//        		iStack.setAmount( xMaterialIntegerEntry.getValue() );
//        		newItemStacks.add( iStack );
//        	}
//        	
////            Material material = xMaterialIntegerEntry.getKey().parseMaterial();
////            if (material != null) {
////                newItemStacks.add(new ItemStack(material, xMaterialIntegerEntry.getValue()));
////            }
//        }
//
//        return newItemStacks;
//    }
    
//    /**
//     * Remove Sellable blocks from an Inventory of a Player.
//     * 
//     * Return an Inventory with the unsold items.
//     * 
//     * @param p - Player.
//     * @param inv - Inventory.
//     *            
//     * @return inv - Inventory.           
//     * */
//    private Inventory removeSellableItems(Player p, Inventory inv){
//        if (sellAllItems.isEmpty()){
//            return inv;
//        }
//        
//        // ?? why?
////        SpigotCompatibility.getInstance().getItemInOffHand( p );
//        
//        List<ItemStack> removeable = new ArrayList<>();
//        
//        SpigotInventory sInv = new SpigotInventory( inv );
//        
//        for (tech.mcprison.prison.internal.ItemStack itemStack : sInv.getItems()) {
//            if (itemStack != null){
//                try {
//                	PrisonBlock pBlock = itemStack.getMaterial();
//                	
//                	
////                    XMaterial xMaterial = getXMaterialOrLapis(itemStack);
//                    
//                    if ( pBlock != null && sellAllItems.containsKey( pBlock.getBlockName() )) {
//                        if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + pBlock.getBlockName().toUpperCase() )) {
//                            // Nothing will change.
//                        } else {
//                        	removeable.add( ((SpigotItemStack) itemStack).getBukkitStack() );
////                            inv.remove(itemStack);
//                        }
//                    }
//                } catch (IllegalArgumentException ignored){}
//            }
//		}
//        
////        for (ItemStack itemStack : inv.getContents()){
////            if (itemStack != null){
////                try {
////                	PrisonBlock
////                	
////                	
////                    XMaterial xMaterial = getXMaterialOrLapis(itemStack);
////                    
////                    if ( xMaterial != null && sellAllBlocks.containsKey(xMaterial)) {
////                        if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + xMaterial.name())) {
////                            // Nothing will change.
////                        } else {
////                        	removeable.add( itemStack );
//////                            inv.remove(itemStack);
////                        }
////                    }
////                } catch (IllegalArgumentException ignored){}
////            }
////        }
//        
//        for ( ItemStack remove : removeable )
//		{
//        	inv.remove(remove);
//		}
//        
//        return inv;
//    }
    
//    private void removeSellableItemsInOffHand(Player p){
//    	
//    	
//    	
//    	SpigotItemStack sItemStack = SpigotCompatibility.getInstance().getPrisonItemInOffHand( p );
//    	
//    	if ( sItemStack != null ) {
//    		
//    		PrisonBlock pBlock = sItemStack.getMaterial();
//        	
//          if ( pBlock != null && sellAllItems.containsKey( pBlock.getBlockName() )) {
//              if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + pBlock.getBlockName().toUpperCase() )) {
//                  // Nothing will change.
//              } else {
//            	  
//            	  SpigotPlayer sPlayer = new SpigotPlayer( p );
//            	  SpigotPlayerInventory spInventory = (SpigotPlayerInventory) sPlayer.getInventory();
//            	  
//            	  SpigotItemStack sItemStackAir = (SpigotItemStack) PrisonBlock.AIR.getItemStack( 1 );
//            	  
//            	  SpigotCompatibility.getInstance().setItemStackInOffHand( spInventory, sItemStackAir );
//              }
//          }
//    	}
//    	
//    	
//    	
////    	ItemStack itemStack = SpigotCompatibility.getInstance().getItemInOffHand( p );
////    	
////    	if ( itemStack != null ) {
////    		
////    		if (itemStack != null){
////    			try {
////    				XMaterial xMaterial = getXMaterialOrLapis(itemStack);
////    				
////    				if ( xMaterial != null && sellAllBlocks.containsKey(xMaterial)) {
////    					if (isPerBlockPermissionEnabled && !p.hasPermission(permissionPrefixBlocks + xMaterial.name())) {
////    						// Nothing will change.
////    					}
////    					else {
////    						
////    						SpigotPlayerInventory spInventory = new SpigotPlayerInventory( p.getInventory() ) ;
////    						SpigotItemStack sItemStack = new SpigotItemStack( new ItemStack( Material.AIR ) );
////    						
////    						SpigotCompatibility.getInstance().setItemStackInOffHand( spInventory, sItemStack );
////    						
////    					}
////    				}
////    			} catch (IllegalArgumentException ignored){}
////    		}
////    	}
//    		
//    
//    }
    
//    /**
//     * Remove Sellable blocks from all Player inventories directly hooked.
//     * 
//     * @param p - Player.
//     * */
//    private void removeSellableItems(Player p){
//
//        if (sellAllItems.isEmpty()){
//            return;
//        }
//
//        if (isSellAllBackpackItemsEnabled && getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks"))){
//            BackpacksUtil backpacksUtil = BackpacksUtil.get();
//            if (backpacksUtil.isMultipleBackpacksEnabled()){
//                for (String id : backpacksUtil.getBackpacksIDs(p)){
//                    backpacksUtil.setInventory(p, removeSellableItems(p, backpacksUtil.getBackpack(p, id)), id);
//                }
//            } else {
//            	String id = null;
//                backpacksUtil.setInventory(p, removeSellableItems(p, backpacksUtil.getBackpack(p, id)), id);
//            }
//        }
//
//        if (isSellAllMinesBackpacksPluginEnabled && IntegrationMinepacksPlugin.getInstance().isEnabled()){
//            Backpack backpack = IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p);
//            if (backpack != null) {
//                removeSellableItems(p, backpack.getInventory());
//                backpack.setChanged();
//                backpack.save();
//            }
//        }
//
//        removeSellableItems(p, p.getInventory());
//        
//        removeSellableItemsInOffHand( p );
//    }

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
        	
        	DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
        	String amt = fFmt.format( autoSellEarningsNotificationWaiting.get(p) );
        	
        	String message = sellallAmountEarnedMsg( amt );
        	
        	SpigotPlayer sPlayer = new SpigotPlayer(p);
        	
        	if ( isSellAllNotificationByActionBar ) {
        		
        		sPlayer.setActionBar( message );
        	}
        	else {
        		
        		Output.get().send( sPlayer, message );
        	}
        	
//        	String message = messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt;
//        	new SpigotPlayer(p).setActionBar( message );
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
        if (!(isAutoSellEnabled || 
        		AutoFeaturesWrapper.getInstance().isBoolean(AutoFeatures.isAutoSellPerBlockBreakEnabled)) || 
        		!isAutoSellPerUserToggleable){
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
     * <p>This function will remove all sellable items from the player's Inventories. It will first ensure that a 
     * Player can sell the items. Some of the conditions that are checked are, along with some of the behaviors:
     * </p>
     * 
     * <ul>
     *   <li>If player has access to use SellAll signs.</li>
     *   <li>Provide the amount the player earned if this is not disabled.</li>
     *   <li>If this actions is silenced, then text and audio notifications are suppressed.<li>
     *   <li>If configured, the reported earnings amount may be delayed and added to other earnings, 
     *   		which will reduce flooding the player with notifications.</li>
     *   <li>If sound notifications are enabled, then they will be played.</li>
     *
     *</ul>
     *
     * <p>Default usage of this method: 
     * </p>
     * <pre>sellAllSell(p, false, false, true, true, false, true);</pre>
     *
     * @param p - Player.
     * @param isUsingSign - boolean.
     * @param completelySilent - boolean.
     * @param notifyPlayerEarned - boolean.
     * @param notifyPlayerDelay - boolean.
     * @param notifyPlayerEarningDelay - boolean.
     * @param playSoundOnSellAll - boolean.
     *
     * @return boolean If successful
     * */
    public boolean sellAllSell(Player p, 
    		boolean isUsingSign, 
    		boolean completelySilent, 
    		boolean notifyPlayerEarned, 
			boolean notifyPlayerDelay, 
			boolean notifyPlayerEarningDelay, 
			boolean playSoundOnSellAll) {
    	return sellAllSell( p, isUsingSign, completelySilent, 
    			notifyPlayerEarned, notifyPlayerDelay, 
    			notifyPlayerEarningDelay, playSoundOnSellAll, 
    			true,
    			null );
    }
    
    public boolean sellAllSell(Player p, 
    		boolean isUsingSign, 
    		boolean completelySilent, 
    		boolean notifyPlayerEarned, 
    		boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, 
    		boolean playSoundOnSellAll, 
    		List<Double> amounts ) {
    	
    	return sellAllSell( p, isUsingSign, completelySilent, 
    			notifyPlayerEarned, notifyPlayerDelay, 
    			notifyPlayerEarningDelay, playSoundOnSellAll, 
    			true,
    			amounts );
    }
    
    public boolean sellAllSell(Player p, 
    		boolean isUsingSign, 
    		boolean completelySilent, 
    		boolean notifyPlayerEarned, 
    		boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, 
    		boolean playSoundOnSellAll, 
    		boolean notifyNothingToSell,
    		List<Double> amounts ) {
    	
        if (!isUsingSign && isSellAllSignEnabled && isSellAllBySignOnlyEnabled && !p.hasPermission(permissionBypassSign)){
            if (!completelySilent) {
            	
            	sellallCanOnlyUseSignsMsg( new SpigotCommandSender(p) );
            }
            return false;
        }

        if (isSellAllDelayEnabled && isPlayerWaitingSellAllDelay(p)){
            if (notifyPlayerDelay && !completelySilent) {

            	sellallRateLimitExceededMsg( new SpigotCommandSender(p) );
            }
            return false;
        }

        if (sellAllItems.isEmpty()){
            if (!completelySilent){
            	
            	sellallShopIsEmptyMsg( new SpigotCommandSender(p) );
            }
            return false;
        }
        
        
        
    	double money = 0;
    	double multiplier = getPlayerMultiplier(p);
    	
        List<SellAllData> soldItems = sellPlayerItems(p);

        for (SellAllData soldItem : soldItems) {
			money += soldItem.getTransactionAmount();
		}

        SellAllData.debugItemsSold( soldItems, new SpigotPlayer( p ), multiplier );
  
        

        //TODO inventory access: getHashMapOfPlayerInventories() && removeSellableItems(p, p.getInventory());
//        double money = getSellMoney(p);
        if (money != 0) {
        	
        	if ( amounts != null ) {
        		
        		amounts.add( money );
        	}

        	if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) {
        		sellAllCurrency = null;
        	}
        	
        	
        	//TODO inventory access: getHashMapOfPlayerInventories() && removeSellableItems(p, p.getInventory());
        	//removeSellableItems(p);

        	SpigotPlayer sPlayer = new SpigotPlayer(p);
        	
        	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
        		
        		RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
        		
        		
        		rankPlayer.addBalance(sellAllCurrency, money);
        	}
        	else {
        		
        		// Ranks are not enabled, so use a non-cached way to pay the player:
        		sPlayer.addBalance(sellAllCurrency, money);
        	}

            if (isSellAllDelayEnabled){
                addToDelay(p);
            }

            if (!completelySilent) {
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundSuccess, 3, 1);
                }

                if (notifyPlayerEarningDelay && isAutoSellEarningNotificationDelayEnabled){
                    if (!isPlayerWaitingAutoSellNotification(p)) {
                        addToAutoSellNotificationDelay(p);
                    } 
                    
                    addDelayedEarningAutoSellNotification(p, money);
                } 
                else if (notifyPlayerEarned) {
                	DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
                	String amt = fFmt.format( money );
                	
                	String message = sellallAmountEarnedMsg( amt );
                	
                	if ( isSellAllNotificationByActionBar ) {
                		sPlayer.setActionBar( message );
                	}
                	else {
                		
                		Output.get().send( sPlayer, message );
                	}
                	
//                	String message = messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt;
//                	new SpigotPlayer(p).setActionBar( message );
//                    Output.get().send( sPlayer, message );

                }
            }
            return true;
        } 
        else {
            if (!completelySilent && notifyNothingToSell) {
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundFail, 3, 1);
                }
                
                sellallYouHaveNothingToSellMsg( new SpigotCommandSender(p) );
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

//    	long tPoint1 = System.nanoTime();
//    	long tPoint2 = tPoint1;
//    	long tPoint3 = tPoint1;
//    	long tPoint4 = tPoint1;
//    	long tPoint5 = tPoint1;
    	
    	
    	double money = 0;
    	double multiplier = getPlayerMultiplier(p);
    	
        List<SellAllData> soldItems = new ArrayList<>();
        		
        SellAllData sad = sellItemStack( itemStack, multiplier );
        if ( sad != null ) {
        	soldItems.add(sad);
        }
        
        for (SellAllData soldItem : soldItems) {
			money += soldItem.getTransactionAmount();
		}

        SellAllData.debugItemsSold(soldItems, new SpigotPlayer(p), multiplier);
    	
//    	double money = getSellMoney(p, itemStack);

//    	tPoint2 = System.nanoTime();
    	
    	if (money != 0) {
    		
    		SpigotPlayer sPlayer = new SpigotPlayer(p);
    		RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());

//    		tPoint3 = System.nanoTime();

    		if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) { 
    			sellAllCurrency = null;
    		}
    		rankPlayer.addBalance(sellAllCurrency, money);
    		
//    		tPoint4 = System.nanoTime();
    		
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
    				DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
    	        	String amt = fFmt.format( money );
    	        	
    	        	String message = sellallAmountEarnedMsg( amt ) ;
    	        	
                	if ( isSellAllNotificationByActionBar ) {
                		sPlayer.setActionBar( message );
                	}
                	else {
                		
                		Output.get().send( sPlayer, message );
                	}
                	
//    	        	String message = messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt;
//    	        	new SpigotPlayer(p).setActionBar( message );
//    	            Output.get().send( sPlayer, message );

    			}
    		}
    		
//    		tPoint5 = System.nanoTime();
    	}
    	

//        DecimalFormat dFmt = Prison.get().getDecimalFormat( "0.0000" );
//        String debugMsg = "{sellAllSell::" + dFmt.format( money ) + 
//        				":t1=" + dFmt.format( (tPoint2 - tPoint1)/1000000d ) +
//        				":t2=" + dFmt.format( (tPoint3 - tPoint2)/1000000d ) + 
//        				":t3=" + dFmt.format( (tPoint4 - tPoint3)/1000000d ) + 
//        				":t4=" + dFmt.format( (tPoint5 - tPoint4)/1000000d ) + 
//        				"}";
//        Output.get().logDebug( debugMsg );
        
    	
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
    public ArrayList<ItemStack> sellAllSell(Player p, ArrayList<ItemStack> itemStacks, 
    		boolean isUsingSign, boolean completelySilent, 
    		boolean notifyPlayerEarned, boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, boolean playSoundOnSellAll, 
    		boolean sellInputArrayListOnly){
        if (!isUsingSign && isSellAllSignEnabled && isSellAllBySignOnlyEnabled && !p.hasPermission(permissionBypassSign)){
            if (!completelySilent) {
            	
            	sellallCanOnlyUseSignsMsg( new SpigotCommandSender(p) );
//                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_sign_only));
            }
            return itemStacks;
        }

        if (isSellAllDelayEnabled && isPlayerWaitingSellAllDelay(p)){
            if (notifyPlayerDelay && !completelySilent) {
            	
            	sellallRateLimitExceededMsg( new SpigotCommandSender(p) );
//                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_wait));
            }
            return itemStacks;
        }

        if (sellAllItems.isEmpty()){
            if (!completelySilent){
            	
            	sellallShopIsEmptyMsg( new SpigotCommandSender(p) );
//                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_empty));
            }
            return itemStacks;
        }

        
    	double money = 0;
    	double multiplier = getPlayerMultiplier(p);
    	
        List<SellAllData> soldItems = new ArrayList<>();
        
        ArrayList<ItemStack> itemsNotSold = new ArrayList<>();
        
        for (ItemStack itemStack : itemStacks ) {
			
        	SellAllData sad = sellItemStack( new SpigotItemStack( itemStack ), multiplier );
        	if ( sad != null ) {
        		soldItems.add(sad);
        	}
        	else {
        		itemsNotSold.add( itemStack );
        	}
		}
        
        // "return" all items not sold:
        itemStacks.clear();
        itemStacks.addAll( itemsNotSold );
        
        
        // Sell the player's inventory if requested
        if ( !sellInputArrayListOnly ) {
        	soldItems.addAll( sellPlayerItems(p) );
        }
        
        for (SellAllData soldItem : soldItems) {
			money += soldItem.getTransactionAmount();
		}


        SellAllData.debugItemsSold( soldItems, new SpigotPlayer( p ), multiplier );
        
        
        // Sell only the itemStacks and then remove the ones that were sold:
//        double arrayListMoney = getSellMoney(p, itemStacks);
//        if (arrayListMoney != 0.00){
//            itemStacks = removeSellableItems(p, itemStacks);
//        }

//        double money;
//        
//        // If to include the player's inventory, then add that too:
//        if (sellInputArrayListOnly){
//            money = arrayListMoney;
//        } else {
//        	// Add the players inventory
//            money = getSellMoney(p) + arrayListMoney;
//        }

        if (money != 0){

            SpigotPlayer sPlayer = new SpigotPlayer(p);
            RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
            if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) sellAllCurrency = null;

//            if (!sellInputArrayListOnly) {
//                removeSellableItems(p);
//            }
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
                	DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
                	String amt = fFmt.format( money );
                	
                	String message = sellallAmountEarnedMsg( amt );
                	
                	if ( isSellAllNotificationByActionBar ) {
                		sPlayer.setActionBar( message );
                	}
                	else {
                		
                		Output.get().send( sPlayer, message );
                	}
                	
//                	String message = messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt;
//                	new SpigotPlayer(p).setActionBar( message );

                }
            }
        } else {
            if (!completelySilent){
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundFail, 3, 1);
                }
                
                sellallYouHaveNothingToSellMsg( new SpigotCommandSender(p) );
//                Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_sell_nothing_sellable));
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
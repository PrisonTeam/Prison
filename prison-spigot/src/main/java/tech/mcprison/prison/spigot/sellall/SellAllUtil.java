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
import tech.mcprison.prison.sellall.messages.SpigotVariousGuiMessages;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
//import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotCommandSender;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllPlayerGUI;
import tech.mcprison.prison.spigot.integrations.IntegrationBackpackAPI;
import tech.mcprison.prison.spigot.integrations.IntegrationMinepacksPlugin;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;
import tech.mcprison.prison.util.Text;

/**
 * @author AnonymousGCA (GABRYCA)
 * */
public class SellAllUtil 
	extends SpigotVariousGuiMessages {

    private static SellAllUtil instance;
    
    
    public Configuration sellAllConfig;

    private static Boolean isAutoSellEnabled = null;
    
    private HashMap<String, PrisonBlock> sellAllItems;
    
    private HashMap<String, Double> sellAllPrestigeMultipliers;
    private HashMap<Player, Double> autoSellEarningsNotificationWaiting = new HashMap<>();
    private ArrayList<XMaterial> sellAllItemTriggers;
    
    private ArrayList<Player> activePlayerDelay = new ArrayList<>();

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
    
    public static boolean isAutoSellEnabled() {
	    
	    	if ( isAutoSellEnabled == null ) {
	    		
	    		isAutoSellEnabled = SpigotPrison.getInstance().isSellAllEnabled();
	    	}
	    	
	    	return isAutoSellEnabled == null ? false : isAutoSellEnabled;
    }
    
    /**
     * Init options that will be cached.
     * @return 
     * */
    public Configuration initCachedData() {
    	
        sellAllConfig = SpigotPrison.getInstance().updateSellAllConfig();
        
      
        refreshClassVariablesFromConfig();
        
        return sellAllConfig;
    }
    
    private void refreshClassVariablesFromConfig() {
      
        
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
        sellAllItems = initSellAllItems();
        
        sellAllPrestigeMultipliers = initPrestigeMultipliers();
        sellAllItemTriggers = initSellAllItemTrigger();
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
    
    
    
    
    
    public ArrayList<XMaterial> getSellAllItemTriggers() {
		return sellAllItemTriggers;
	}
	public void setSellAllItemTriggers(ArrayList<XMaterial> sellAllItemTriggers) {
		this.sellAllItemTriggers = sellAllItemTriggers;
	}

	public PrisonBlock getSellallItem( PrisonBlock blockKey ) {
    	
	    	PrisonBlock pBlockSellAll = sellAllItems.get( blockKey.getBlockNameSearch() );
	    	
	    	return pBlockSellAll;
    }
	public void putSellallItem( PrisonBlock pBlockKey ) {
		sellAllItems.put( pBlockKey.getBlockNameSearch(), pBlockKey );
	}
	
	public HashMap<String, PrisonBlock> getSellAllItems() {
		return sellAllItems;
	}
	public void setSellAllItems(HashMap<String, PrisonBlock> sellAllItems) {
		this.sellAllItems = sellAllItems;
	}
	
	public PrisonBlock getPrisonBlock( String itemName ) {
		PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( itemName );
		return pBlockKey;
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


    
    public PrisonBlock getSellAllPrisonBlockKey( String itemID ) {
    	
	    	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( itemID );
	    	if ( pBlockKey == null ) {
	    		Output.get().logDebug( "sellall add: invalid block name (%s)", itemID);
	    		return null;
	    	}
	
	    	return pBlockKey;
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





    /**
     * Get SellAll Player Multiplier.
     *
     * @param p - Player.
     *
     * @return double.
     * */
    public double getPlayerMultiplier( Player player ) {
    		return getPlayerMultiplier( new SpigotPlayer( player ));
    }
    	
    public double getPlayerMultiplier( tech.mcprison.prison.internal.Player sPlayer ) {
    		

        if (!isSellAllMultiplierEnabled) {
        	
            return 1d;
        }
        
        
        double multiplier = 0d;
        double rankMultiplers = 0;
        
        
        if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
        	
        	
        	RankPlayer rPlayer = sPlayer.getRankPlayer();
        	
        	Set<RankLadder> keys = rPlayer.getLadderRanks().keySet();
        	for (RankLadder ladderKey : keys) {
        		PlayerRank pRank = rPlayer.getLadderRanks().get(ladderKey);
        		String rankName = pRank.getRank().getName();
        		
        		String multiplierRankString = sellAllConfig.getString("Multiplier." + rankName + ".MULTIPLIER");
        		
        		if (multiplierRankString != null && sellAllPrestigeMultipliers.containsKey( rankName )){
        		
        			double rankMult = sellAllPrestigeMultipliers.get( rankName );
        			rankMultiplers += rankMult;
        			
        		}
			}
        }
        
        if ( rankMultiplers == 0 ) {
        		multiplier = defaultMultiplier;
        }
        else {
        	multiplier  = rankMultiplers;
        }
        

        // Get Multiplier from multipliers permission's if there's any.
        String permPattern = "prison.sellall.multiplier.";
        List<String> perms = sPlayer.getPermissions( permPattern );
        
        
        // If the player is off line, then try to use the snapshot of the last group of perms:
	    	if ( perms.size() == 0 && sPlayer.getRankPlayer() != null ) {
	    		perms.addAll( sPlayer.getPermissions( permPattern, sPlayer.getRankPlayer().getPermsSnapShot() ));
	    	}
        
    	
        double multiplierExtraByPerms = 0;
        for (String multByPerm : perms) {
        	
	        	String multStr = multByPerm.replace(permPattern, "");
	        	if ( multStr.contains( "_" ) ) {
	        		multStr = multStr.substring( 0, multStr.indexOf("_") );
	        	}
	        	
            double multByPermDouble = Double.parseDouble( multStr );
            
            boolean highest = multByPermDouble > multiplierExtraByPerms;
	            
            if ( !isSellAllPermissionMultiplierOnlyHigherEnabled ) {
                multiplierExtraByPerms += multByPermDouble;
                
            } 
            else {
	            	
	            	if ( highest ) {
	            		multiplierExtraByPerms = multByPermDouble;
	            	}
            }
            	
        }
        
        
        multiplier += multiplierExtraByPerms;
        
        return multiplier;
    }
    
    public double getPlayerMultiplierDebug( tech.mcprison.prison.internal.Player sPlayer ) {
    	
	    	StringBuilder sb = new StringBuilder();
	
	    	long tPoint1 = System.nanoTime();
	    	
	    	sb.append( "&dCalculate Sellall Player Multipliers: &6" )
	    	  .append( sPlayer.getName() );
	    	
	    	if (!isSellAllMultiplierEnabled) {
	    		
	    		sb.append( " &cSellall Multipliers are disabled." );
	    		if ( Output.get().isDebug() ) {
	    			Output.get().logInfo( sb.toString() );
	    		}
	    		
	    		return 1d;
	    	}
	    	
	    	
	    	sb.append( " &aEnabled &6DefaultMult: &c" )
	    	  .append( defaultMultiplier );
	    	
	    	if ( isSellAllPermissionMultiplierOnlyHigherEnabled ) {
	    		sb.append( " &a(Use only the highest perm multiplier) " );
	    	}
	    	
	    	
	    	
	    	double multiplier = 0d;
	    	double rankMultiplers = 0;
	    	
	    	sb.append( "&6RankMulipliers: &a[ &6" );
	    	
	    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
	    		
	    		
	    		RankPlayer rPlayer = sPlayer.getRankPlayer();
	    		
	    		Set<RankLadder> keys = rPlayer.getLadderRanks().keySet();
	    		for (RankLadder ladderKey : keys) {
	    			PlayerRank pRank = rPlayer.getLadderRanks().get(ladderKey);
	    			String rankName = pRank.getRank().getName();
	    			
	    			String multiplierRankString = sellAllConfig.getString("Multiplier." + rankName + ".MULTIPLIER");
	    			
	    			if (multiplierRankString != null && sellAllPrestigeMultipliers.containsKey( rankName )){
	    				
	    				double rankMult = sellAllPrestigeMultipliers.get( rankName );
	    				rankMultiplers += rankMult;
	    				
	    				sb.append( rankMult ).append( " " );
	    			}
	    		}
	    	}
	    	
	    	if ( rankMultiplers == 0 ) {
	    		multiplier = defaultMultiplier;
	    		sb.append( "&cnone " );
	    	}
	    	else {
	    		multiplier  = rankMultiplers;
	    	}
	    	
	    	sb.append( "&a] " );
	    	
	    	
	    	sb.append( "&6Base multiplier: &c" )
	    	  .append( multiplier ).append( " " );
	    	
	    	
	    	long tPoint2 = System.nanoTime();
	    	
	    	
	    	// Get Multiplier from multipliers permission's if there's any.
	    	String permPattern = "prison.sellall.multiplier.";
	    	List<String> perms = sPlayer.getPermissions( permPattern );
	    	List<String> debugRejected = new ArrayList<>();
	    	String debugHighest = null;
	    	
	    	if ( perms.size() == 0 && sPlayer.getRankPlayer() != null ) {
	    		sb.append( "&cNoBukkkitPerms " );
	    		
	    		perms.addAll( sPlayer.getPermissions( permPattern, sPlayer.getRankPlayer().getPermsSnapShot() ));
	    		
	    		if ( perms.size() > 0 ) {
	    			
	    			sb.append( "&c(Using &6" );
	    			sb.append( perms.size() );
	    			sb.append( " &csnapshotPerms) " );
	    		}
	    	}
	    	
	    	sb.append( "&6PermissionMulipliers: &a[ &6" );
	    	
	    	
	    	double multiplierExtraByPerms = 0;
	    	for (String multByPerm : perms) {
	    		
	    		String multStr = multByPerm.replace(permPattern, "");
	    		if ( multStr.contains( "_" ) ) {
	    			multStr = multStr.substring( 0, multStr.indexOf("_") );
	    		}
	    		
	    		double multByPermDouble = Double.parseDouble( multStr );
	    		
	    		boolean highest = multByPermDouble > multiplierExtraByPerms;
	    		
	    		String debugMult = multByPermDouble + ":" + multByPerm + "&r ";
	    		
	    		if ( !isSellAllPermissionMultiplierOnlyHigherEnabled ) {
	    			multiplierExtraByPerms += multByPermDouble;
	    			
	    			sb.append( "&6" )
	    			  .append( debugMult );
	    		} 
	    		else {
	    			
	    			if ( highest ) {
	    				multiplierExtraByPerms = multByPermDouble;
	    				
	    				if ( debugHighest != null ) {
	    					debugRejected.add( debugHighest );
	    				}
	    				debugHighest = debugMult;
	    			}
	    			else {
	    				debugRejected.add( debugMult );
	    			}
	    			
	    		}
	    		
	    	}
	    	
	    	if ( debugHighest != null ) {
	    		sb.append( "&6" ) // Green
	    		  .append( debugHighest );
	    	}
	    	
	    	for (String rejected : debugRejected) {
	    		sb.append( "&m&c" ) // Strike through & red
	    		  .append( rejected );
	    	}
	    	
	    	sb.append( " &a] " );
	    	
	    	
	    	multiplier += multiplierExtraByPerms;
	    	
	    	sb.append( "&6PermissionTotal: &a[ &6" )
	    	  .append( multiplierExtraByPerms )
	    	  .append( " &a] " );
	    	
	    	sb.append( "&6TotalMultiplier: &a[ &6" )
	    	  .append( multiplier )
	    	  .append( " &a] " );
	    	
	        long tPoint3 = System.nanoTime();
	        DecimalFormat dFmt = Prison.get().getDecimalFormat( "0.0000" );
	        String debugMsg = " :t1=" + 
	        				dFmt.format( (tPoint2 - tPoint1)/1000000d ) +
	        				" :t2=" + 
	        				dFmt.format( (tPoint3 - tPoint2)/1000000 ) + "}";
	    	
	        sb.append( debugMsg );
	        
	       
	    	Output.get().logInfo( sb.toString() );
	    	
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
    
    
    
    public double getPlayerInventoryValue( SpigotPlayer sPlayer ) {
	    	double value = 0;
	    	
	    	double multiplier = sPlayer.getSellAllMultiplier();
	
	    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();
	
	    	List<SellAllData> soldItems = valueOfInventoryItems( spInventory, multiplier );
	    	for (SellAllData soldItem : soldItems) {
				value += soldItem.getTransactionAmount();
			}
	    	
	    	return value;
    }
    
    public String getPlayerInventoryValueReport( SpigotPlayer sPlayer ) {
    	
	    	double multiplier = sPlayer.getSellAllMultiplier();
	    	
	    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();
	    	
	    	List<SellAllData> soldItems = valueOfInventoryItems( spInventory, multiplier );
	    	
	    	String report = SellAllData.itemsSoldReport(soldItems, sPlayer, multiplier);
	    	
	    	return report;
    }
    
    public List<SellAllData> getPlayerInventoryValueTransactions( SpigotPlayer sPlayer ) {
    	
	    	double multiplier = sPlayer.getSellAllMultiplier();
	    	
	    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();
	    	
	    	List<SellAllData> soldItems = valueOfInventoryItems( spInventory, multiplier );
	    	
	    	return soldItems;
    }
    
    
    
    public double getItemStackValue( SpigotPlayer sPlayer, SpigotItemStack itemStack ) {
    	
	    	double multiplier = sPlayer.getSellAllMultiplier();
	    	
	    	SellAllData sad = sellItemStack( itemStack, multiplier );
	    	
	    	return sad == null ? 0 : sad.getTransactionAmount();
    }
    
    public String getItemStackValueReport( SpigotPlayer sPlayer, SpigotItemStack itemStack ) {
    	
	    	double multiplier = sPlayer.getSellAllMultiplier();
	    	
	    	List<SellAllData> soldItems = new ArrayList<>();
	    	
	    	SellAllData sad = sellItemStack( itemStack, multiplier );
	    	if ( sad != null ) {
	    		
	    		soldItems.add( sad );
	    	}
	    	
	    	String report = SellAllData.itemsSoldReport(soldItems, sPlayer, multiplier);
	    	
	    	return report;
    }
    
    public List<SellAllData> getItemStackValueTransactions( SpigotPlayer sPlayer, SpigotItemStack itemStack ) {
    	
	    	double multiplier = sPlayer.getSellAllMultiplier();
	    	
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
    	
	    	SpigotPlayer sPlayer = new SpigotPlayer( p );
	    	
	    	double multiplier = sPlayer.getSellAllMultiplier();
	    	
	    	
	    	// Remove player's off-hand and helmet slots since those don't always sell correctly:
	    	boolean invDirty = false;
	    	ItemStack offHandItemStack = SpigotCompatibility.getInstance().getItemInOffHandStrict( p );
	    	
	    	if ( offHandItemStack != null ) {
	    		// 1.8 will always be null, so it will never hit this.
	    		ItemStack itemStack = null;
	    		SpigotCompatibility.getInstance().setItemStackInOffHandStrict( p, itemStack );
	    		
	    		invDirty = true;
	    	}
	    	
	    	ItemStack helmetItemStack = p.getInventory().getHelmet();
	    	
	    	if ( helmetItemStack != null ) {
	    		p.getInventory().setHelmet( null );
	
	    		invDirty = true;
	    	}
	    	
	    	
    	
	    	SpigotPlayerInventory spInventory = sPlayer.getSpigotPlayerInventory();
	    	
	    	
	    	List<SellAllData> soldData = sellInventoryItems( spInventory, multiplier );
	
	    	
	    	
	    	// Since the inventory is done being sold, add back the offHand and helmet:
	    	if ( offHandItemStack != null ) {
	    		// 1.8 will always be null, so it will never hit this.
	    		SpigotCompatibility.getInstance().setItemStackInOffHandStrict( p, offHandItemStack );
	    	}
	    	
	    	if ( helmetItemStack != null ) {
	    		p.getInventory().setHelmet( helmetItemStack );
	    	}
	    	
	    	if ( invDirty ) {
	    		p.updateInventory();
	    	}
    	
    	
	    	if ( isSellAllBackpackItemsEnabled && BackpacksUtil.getInstance().isEnabled() ) {
	    		BackpacksUtil bpUtil = BackpacksUtil.get();
				
				soldData.addAll( bpUtil.sellInventoryItems( p, multiplier ) );
	    	}
	    	
	    	// Enable sellall for the Minepacks plugin:
		if ( isSellAllMinesBackpacksPluginEnabled && IntegrationMinepacksPlugin.getInstance().isEnabled()  ) {
			soldData.addAll( IntegrationMinepacksPlugin.getInstance().sellInventoryItems( p, multiplier ) );						
		}
    	
		// Enable sellall for the backpack API:
		if ( isSellAllMinesBackpacksPluginEnabled && IntegrationBackpackAPI.getInstance().isEnabled()  ) {
			soldData.addAll( IntegrationBackpackAPI.getInstance().sellInventoryItems( p, multiplier ) );						
		}
		
		return soldData;
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
    
    
    public List<SellAllData> sellInventoryItems( tech.mcprison.prison.internal.inventory.Inventory inventory, 
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
	    		
	    		// This converts a bukkit ItemStack to a PrisonBlock, and it also sets up the
	    		// displayName if that is set on the itemStack.
	    		PrisonBlock pBlockInv = iStack.getMaterial();
	    		
	    		PrisonBlock pBlockSellAll = getSellallItem( pBlockInv );
	    		
	    		if ( pBlockSellAll != null ) {
	    			
	    			if ( iStack != null && iStack.getEnchantments() != null && iStack.getEnchantments().size() > 0 ) {
	    				// Cannot sell enchanted items:
	    				if ( Output.get().isDebug() ) {
	    					String msg = String.format(
	    							"Sellall: Cannot sell item '%s' (qty %s) because it has enchantments which is not allowed. ",
	    							(iStack.getDisplayName() != null ? iStack.getDisplayName() : iStack.getName() ), 
	    							Integer.toString( iStack.getAmount() )
	    							);
	    					Output.get().logInfo( msg );
	    				}
	    			}
	    			
	    			else if ( !pBlockSellAll.isLoreAllowed() && iStack.getLore().size() > 0 ) {
	    				if ( Output.get().isDebug() ) {
	    					String msg = String.format(
	    							"Sellall: Cannot sell item '%s' (qty %s) because it has lore which is not allowed. ",
	    							(iStack.getDisplayName() != null ? iStack.getDisplayName() : iStack.getName() ), 
	    							Integer.toString( iStack.getAmount() )
	    							);
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
    	if ( isAutoSellEnabled() ) {
    		
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
     * Loads sellAll blocks from SellAllConfig.yml
     * With XMaterials and double values (money).
     *
     * @return HashMap<String, PrisonBlock>
     * */
    public HashMap<String, PrisonBlock> initSellAllItems(){

    		HashMap<String, PrisonBlock> sellAllItems = new HashMap<>();
    	

        if (sellAllConfig.getConfigurationSection("Items") == null){
            return sellAllItems;
        }

        for (String key : sellAllConfig.getConfigurationSection("Items").getKeys(false)) {

        	String itemName = key.trim().toUpperCase();
        	String itemPrefix = "Items." + itemName;
        	
            String itemID = sellAllConfig.getString( itemPrefix + ".ITEM_ID");

            PrisonBlock pBlock = Prison.get().getPlatform().getPrisonBlock(itemID);
            
            if ( pBlock == null ) {
	            	// create a prison block for this sellall item:
	            	pBlock = new PrisonBlock( key.trim().toLowerCase() );
	            	
	            	
	            	// Prevent this item from being used within mines:
	            	pBlock.setSellallOnly( true );
	            	
	            	// Add it:
	            	List<PrisonBlock> newBlockTypes = new ArrayList<>();
	            	newBlockTypes.add( pBlock );
	            	Prison.get().getPlatform().getPrisonBlockTypes().addBlockTypes( newBlockTypes );
            	
            }

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
	            		} 
	            		catch (NumberFormatException ignored) {
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
    		return addSellAllBlock( xMaterial.name(), null, value );
    }
    
    /**
     * <p>Note: This function was flawed (now fixed).  When searching for the sellall item, it uses the 
     * 'getBlockNameSearch()' which includes the displayName, but when setting the sellall 
     * item, it ignores the displayName.
     * </p>
     * 
     * @param xMaterial
     * @param displayName
     * @param value
     * @return
     */
    public boolean addSellAllBlock( String itemID, String displayName, double value) {
    	
	    	PrisonBlock pBlockKey = Prison.get().getPlatform().getPrisonBlock( itemID );
	    	if ( pBlockKey == null ) {
	    		Output.get().logDebug( "sellall add: invalid block name (%s)", itemID);
	    		return false;
	    	}
	    	
	    	// Add the displayName to the block on it can be properly setup in sellall:
	    	if ( displayName != null && displayName.trim().length() > 0 ) {
	    		pBlockKey.setDisplayName( displayName );
	    	}
	    	
	    	PrisonBlock pBlock = getSellallItem( pBlockKey );
	
	    	// If that is an invalid PrisonBlock, then exit
	    	if ( pBlock != null ) {
	    		Output.get().logDebug( "sellall add: block already exists (%s)", itemID);
	    		return false;
	    	}
	    	
    	
	    	// pBlock is null, but it's being used below. So clone the key:
	    	pBlock = pBlockKey.clone();
	    	
        try {
        	
	        	File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
	        	FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
	    		
	    		String itemName = pBlockKey.getBlockNameSearch().toUpperCase();
	    		String confKey = "Items." + itemName;
	        	
	        	
	    		conf.set( confKey + ".ITEM_ID", itemID );
	    		conf.set( confKey + ".ITEM_VALUE", value);
            conf.set( confKey + ".IS_LORE_ALLOWED", pBlock.isLoreAllowed() );
            
            if ( displayName != null ) {
            	conf.set( confKey + ".ITEM_DISPLAY_NAME", displayName );
            }
            
            if (getBooleanValue("Options.Sell_Per_Block_Permission_Enabled")) {
            	conf.set( confKey + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + itemID);
            }
            
            conf.save(sellAllFile);
            refreshClassVariablesFromConfig();

            pBlockKey.setSalePrice( value );
            putSellallItem( pBlockKey );

        } 
        catch (IOException e) {
        	
            e.printStackTrace();
            return false;
        }
        
        
        return true;
    }
    
    /**
     * 
     * <p>Warning: use with caution since this does not validate against XMaterial.
     * </p>
     * 
     * <p>Note: This function was flawed (now fixed).  When searching for the sellall item, it uses the 
     * 'getBlockNameSearch()' which includes the displayName, but when setting the sellall 
     * item, it ignores the displayName.
     * </p>
     * 
     * @param xMaterial
     * @param displayName
     * @param value
     * @return
     */
    public boolean addSellAllBlock( String itemNameSearch, double value, PrisonBlock pBlock ) {
    	
	    	PrisonBlock pBlockKey = getPrisonBlock( itemNameSearch );
	    	
	    	PrisonBlock pBlockSellall = pBlockKey == null ? null : getSellallItem( pBlockKey );
	    	
	    	// If that is an invalid PrisonBlock, then exit
	    	if ( pBlockSellall != null ) {
	    		Output.get().logDebug( "sellall add: block already exists (%s)", itemNameSearch );
	    		return false;
	    	}
    	
    	
	    	try {
	    		
	    		if ( pBlockSellall == null ) {
	    			
	    			pBlockSellall = pBlock.clone();
	    		}
	    		
	    		String displayName = pBlockSellall.getDisplayName();
	
	    		
	    		File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
	    		FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
	    		
	    		String itemName = pBlockSellall.getBlockNameSearch().toUpperCase();
	    		String confKey = "Items." + itemName;
	    		
	    		conf.set(confKey + ".ITEM_ID", pBlockSellall.getBlockNameSearch() );
	    		conf.set(confKey + ".ITEM_VALUE", value);
	    		conf.set(confKey + ".IS_LORE_ALLOWED", pBlockSellall.isLoreAllowed() );
	    		
	    		if ( displayName != null ) {
	    			conf.set(confKey + ".ITEM_DISPLAY_NAME", displayName );
	    		}
	    		
	    		if (getBooleanValue("Options.Sell_Per_Block_Permission_Enabled")) {
	    			conf.set(confKey + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + itemNameSearch );
	    		}
	    		
	    		conf.save(sellAllFile);
	    		refreshClassVariablesFromConfig();
	    		
	    		pBlockSellall.setSalePrice( value );
	
	    		
	    		// NOTE: If PrisonBlock Key was null, then that means that prison does not have this setup as a 
	    		//       valid block, so we must create a new prison block and add it.
	    		if ( pBlockKey == null ) {
	    			List<PrisonBlock> pbList = new ArrayList<>();
	    			
	    			pbList.add( pBlockSellall.clone() );
	    			
	    			Prison.get().getPlatform().getPrisonBlockTypes().addBlockTypes( pbList );
	    		}
	    		
	    		putSellallItem( pBlockSellall );
	    		
	    	} 
	    	catch (IOException e) {
	    		
	    		String msg = String.format( 
	    				"SellAllUtil.addSellAllBlock: Failed to add a new block to SellAll. " +
	    				    "itemSearch: [%s]  value: %s   PrisonBlock: %s  [%s]", 
	    				    itemNameSearch, Prison.get().getDecimalFormatDouble().format( value ), 
	    				    pBlock.getBlockNameSearch(), e.getMessage() );
	    		Output.get().logWarn( msg );
	    		
	    		return false;
	    	}
	    	
	    	
	    	return true;
    }


    public boolean addSellallRankMultiplier(String rankName, double multiplier){
    		return addSellallRankMultiplier(rankName, multiplier, false);
    }
    
    /**
     * Add Multiplier to SellAll depending on the Rank (Rank from any ladder).
     *
     * Return true if edited with success, false if error or the rank is not found.
     *
     * @param rankName - Name of the Rank as String.
     * @param multiplier - Double value.
     * @param applyToHigherRanks 
     *
     * @return boolean.
     * */
    public boolean addSellallRankMultiplier(String rankName, double multiplier, boolean applyToHigherRanks) {

        PrisonRanks rankPlugin = (PrisonRanks) (Prison.get().getModuleManager() == null ? 
        		null : Prison.get().getModuleManager().getModule(PrisonRanks.MODULE_NAME) );
        if (rankPlugin == null) {
            return false;
        }

        Rank rank = rankPlugin.getRankManager().getRank(rankName);
        
        if ( rank == null ) {
        	// Invalid rank!
            return false;
        }


        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            
            setRankMultiplier(rank, multiplier, conf);
            
            if ( applyToHigherRanks ) {
            	
	            	Rank nextRank = rank.getRankNext();
	            	while ( nextRank != null && !getPrestigeMultipliers().containsKey(nextRank.getName()) ) {
	
	            		setRankMultiplier(nextRank, multiplier, conf);
	            		nextRank = nextRank.getRankNext();
	            	}
            }
            
            conf.save(sellAllFile);
        } 
        catch (IOException e) {
    		
	    		String msg = String.format( 
	    				"SellAllUtil.addSellAllMultiplier: Failed to add a new rank multiplier to SellAll. " +
	    				    "rankName: [%s]  multiplier: %s  applyToHigherRanks: %s  [%s]", 
	    				    rankName, Prison.get().getDecimalFormatDouble().format( multiplier ),
	    				    Boolean.toString( applyToHigherRanks ),
	    				    e.getMessage() );
	    		Output.get().logWarn( msg );
    		
            return false;
        }
        
        sellAllPrestigeMultipliers.put( rank.getName(), multiplier);
        refreshClassVariablesFromConfig();
        
        return true;
    }

	private void setRankMultiplier(Rank rank, double multiplier, FileConfiguration conf) {
		if ( rank.getLadder().isPrestiges() ) {
			conf.set("Multiplier." + rank.getName() + ".PRESTIGE_NAME", rank.getName());
		}
		else {
			conf.set("Multiplier." + rank.getName() + ".RANK_NAME", rank.getName());
		}
		
		conf.set("Multiplier." + rank.getName() + ".MULTIPLIER", multiplier);
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
        
        refreshClassVariablesFromConfig();
        return true;
    }


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
    	
    		return editPrice( xMaterial.name(), displayName, value );
    }
    
    public boolean editPrice( String itemID, String displayName, double value) {
    	
	    	PrisonBlock pBlockKey = getPrisonBlock( itemID );
	    	PrisonBlock pBlock = getSellallItem( pBlockKey );
	    	
	    	
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
	    		
	    		String itemName = pBlockKey.getBlockNameSearch().toUpperCase();
	    		String confKey = "Items." + itemName;
	    		
	    		conf.set( confKey + ".ITEM_ID", pBlockKey.getBlockNameSearch() );
	    		conf.set( confKey + ".ITEM_VALUE", value);
	    		conf.set( confKey + ".IS_LORE_ALLOWED", pBlock.isLoreAllowed() );
	    		
	    		if ( displayName != null && displayName.trim().length() > 0 ) {
	    			conf.set( confKey + ".ITEM_DISPLAY_NAME", displayName.trim() );
	    		}
	    		else {
	    			//confSection.set("ITEM_DISPLAY_NAME", null);
	    		}
	    		
	    		
	    		if ( pBlock.getPurchasePrice() != null ) {
	    			
	    			conf.set( confKey + ".PURCHASE_PRICE", pBlock.getPurchasePrice().doubleValue() );
	    		}
	    		
	    		if (getBooleanValue("Options.Sell_Per_Block_Permission_Enabled")) {
	    			conf.set( confKey + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + itemName );
	    		}
	    		conf.save(sellAllFile);
	    		
	    		// Update only if successful
	    		refreshClassVariablesFromConfig();
	    	} 
	    	catch (IOException e) {
	    		
	    		String msg = String.format( 
	    				"SellAllUtil.editPrice: Failed to edit an item price. " +
	    				    "itemID: [%s]  DisplayName: %s&r  value: %s  [%s]", 
	    				    itemID, (displayName == null ? "" : displayName ),
	    				    Prison.get().getDecimalFormatDouble().format( value ), 
	    				    e.getMessage() );
	    		Output.get().logWarn( msg );
	    		
	    		return false;
	    	}
    	
	    	return true;
    }
    
    
    public boolean editAllowLore( PrisonBlock pBlock, boolean value) {

	    	// Use the block that is stored in sellall:
	    	PrisonBlock saBlock = getSellallItem( pBlock );
	    	
	    	// Do not allow an edit price if the material does not exist, or if the value has not changed:
	    	if ( saBlock == null ) {
	    		
	    		Output.get().logDebug( "sellall edit: item does not exist in shop so it cannot be edited (%s)", pBlock.getBlockNameSearch() );
	    		return false;
	    	}
	    	if ( saBlock.isLoreAllowed() == value ){
	    		Output.get().logDebug( "sellall edit: No change in 'allow lore' (%s %s)", 
	    				saBlock.getBlockName(), Boolean.toString( saBlock.isLoreAllowed() ) );
	    		return false;
	    	}
	    	
	    	saBlock.setLoreAllowed( value );
	    	
	    	try {
	    		File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
	    		FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
	    		
	    		String itemName = saBlock.getBlockNameSearch().toUpperCase();
	    		conf.set("Items." + itemName + ".IS_LORE_ALLOWED", saBlock.isLoreAllowed() );
	    		
	    		conf.save(sellAllFile);
	    		
	    		// Update only if successful
	    		refreshClassVariablesFromConfig();
	    	} 
	    	catch (IOException e) {
	    		
	    		String msg = String.format( 
	    				"SellAllUtil.editAllowLore: Failed to edit the allow lore setting. " +
	    				    "itemID: [%s]  allow Lore?: %s  [%s]", 
	    				    saBlock.getBlockNameSearch(),
	    				    Boolean.toString( value ), 
	    				    e.getMessage() );
	    		Output.get().logWarn( msg );
	    		
	    		return false;
	    	}
    	
	    	return true;
    }


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
    public boolean removeSellAllBlock( String itemID ) {

    		PrisonBlock pBlockKey = getSellallItem( getPrisonBlock( itemID ));

    
        if ( pBlockKey == null ){
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + pBlockKey.getBlockName().toUpperCase(), null);
            conf.save(sellAllFile);

            refreshClassVariablesFromConfig();
        } 
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        getSellAllItems().remove( pBlockKey.getBlockNameSearch() );
        return true;
    }


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
        } 
        catch (IOException e) {
            return false;
        }
        sellAllPrestigeMultipliers.remove(rankName);
        refreshClassVariablesFromConfig();
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
        } 
        catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        sellAllItemTriggers.remove(xMaterial);
        refreshClassVariablesFromConfig();
        return true;
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
        } 
        catch (IOException e) {
        	
        	String msg = String.format( 
    				"SellAllUtil.setItemTrigger: Failed to set the config setting " +
    				"'Options.ShiftAndRightClickSellAll.Enabled' to a value of %s " +
    				"since there was a problem trying to save the SellAllConfig.yml file. " +
    				    "[%s]", 
    				    Boolean.toString( isEnabled ),
    				    e.getMessage() );
    		Output.get().logWarn( msg );
    		
            return true;
        }
        isSellAllItemTriggerEnabled = isEnabled;
        refreshClassVariablesFromConfig();
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
        if ( isAutoSellEnabled() == isEnabled ) {
            return false;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell", isEnabled);
            conf.save(sellAllFile);
        } 
        catch (IOException e) {
        	
	        	String msg = String.format( 
	    				"SellAllUtil.setAutoSell: Failed to set 'Options.Full_Inv_AutoSell' to a value of %s since " +
	    				"there was a problem trying to save the SellAllConfig.yml file. " +
	    				    "[%s]", 
	    				    Boolean.toString( isAutoSellEnabled() ),
	    				    e.getMessage() );
	    		Output.get().logWarn( msg );
	
	    		return false;
        }

        isAutoSellEnabled = isEnabled;
        refreshClassVariablesFromConfig();
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
        if (!( isAutoSellEnabled() || 
        		AutoFeaturesWrapper.getInstance().isBoolean(AutoFeatures.isAutoSellPerBlockBreakEnabled)) || 
        		!isAutoSellPerUserToggleable ) {
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
        } 
        catch (IOException e) {
        	
	        	String msg = String.format( 
	    				"SellAllUtil.setAutoSellPlayer: Failed to set user %s autoSell Per Bbock toggle to a value " +
	    				"of %s since there was a problem trying to save the SellAllConfig.yml file. " +
	    				    "[%s]", 
	    				    p.getName(),
	    				    Boolean.toString( enable ),
	    				    e.getMessage() );
	    		Output.get().logWarn( msg );
    		
            return false;
        }

        refreshClassVariablesFromConfig();
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
        refreshClassVariablesFromConfig();
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
        refreshClassVariablesFromConfig();
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
        refreshClassVariablesFromConfig();
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
        refreshClassVariablesFromConfig();
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

        if ( getSellAllItems().isEmpty() ) {
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
        		
        		RankPlayer rankPlayer = sPlayer.getRankPlayer();
        		
        		
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
    	
    	
	    	if (money != 0) {
	    		
	    		SpigotPlayer sPlayer = new SpigotPlayer(p);
	    		RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
	
	    		if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) { 
	    			sellAllCurrency = null;
	    		}
	    		rankPlayer.addBalance(sellAllCurrency, money);
	    		
	    		if (!completelySilent) {
	    			
	    			if (notifyPlayerEarningDelay // && isAutoSellEarningNotificationDelayEnabled
	    					){
	    				
	    				if (!isPlayerWaitingAutoSellNotification(p)){
	    					// Initialize && Force delayed notifications, even if delayed is disabled:
	    					autoSellEarningsNotificationWaiting.put(p, 0.00);
	    					
	    					Bukkit.getScheduler().scheduleSyncDelayedTask(
	    							SpigotPrison.getInstance(), () -> 
	    							removeFromAutoSellDelayAndNotify(p), 20L * defaultAutoSellEarningNotificationDelay);
	
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
    public ArrayList<ItemStack> sellAllSell(Player p, ArrayList<ItemStack> itemStacks, 
    		boolean isUsingSign, boolean completelySilent, 
    		boolean notifyPlayerEarned, boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, boolean playSoundOnSellAll, 
    		boolean sellInputArrayListOnly){
        if (!isUsingSign && isSellAllSignEnabled && isSellAllBySignOnlyEnabled && !p.hasPermission(permissionBypassSign)){
            if (!completelySilent) {
            	
            	sellallCanOnlyUseSignsMsg( new SpigotCommandSender(p) );
            }
            return itemStacks;
        }

        if (isSellAllDelayEnabled && isPlayerWaitingSellAllDelay(p)){
            if (notifyPlayerDelay && !completelySilent) {
            	
            	sellallRateLimitExceededMsg( new SpigotCommandSender(p) );
            }
            return itemStacks;
        }

        if ( getSellAllItems().isEmpty()){
            if (!completelySilent){
            	
            	sellallShopIsEmptyMsg( new SpigotCommandSender(p) );
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
        
        
        if (money != 0){

            SpigotPlayer sPlayer = new SpigotPlayer(p);
            RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
            if (sellAllCurrency != null && sellAllCurrency.equalsIgnoreCase("default")) sellAllCurrency = null;

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
                	
                }
            }
        } else {
            if (!completelySilent){
                if (isSellAllSoundEnabled && playSoundOnSellAll) {
                    p.playSound(p.getLocation(), sellAllSoundFail, 3, 1);
                }
                
                sellallYouHaveNothingToSellMsg( new SpigotCommandSender(p) );
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

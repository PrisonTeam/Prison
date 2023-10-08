package tech.mcprison.prison.spigot.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.spigot.SpigotPlatform;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.sellall.SellAllBlockData;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.spigot.utils.tasks.PlayerAutoRankupTask;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class PrisonSpigotSellAllCommands extends PrisonSpigotBaseCommands {

    private static PrisonSpigotSellAllCommands instance;
    private final MessagesConfig messages = SpigotPrison.getInstance().getMessagesConfig();
    private final Compatibility compat = SpigotPrison.getInstance().getCompatibility();

    /**
     * Check if SellAll's enabled.
     * */
    public static boolean isEnabled(){
    	
    	return SpigotPrison.getInstance().isSellAllEnabled();
    }

    /**
     * Get SellAll Commands instance.
     * */
    public static PrisonSpigotSellAllCommands get() {
        if (instance == null && isEnabled()) {
            instance = new PrisonSpigotSellAllCommands();
        }
        if (instance == null){
            return null;
        }
        return instance;
    }

    @Command(identifier = "sellall set currency", description = "SellAll set currency command", 
    		onlyPlayers = false, permissions = "prison.sellall.currency")
    private void sellAllCurrency(CommandSender sender,
                                 @Arg(name = "currency", description = "Currency name.", def = "default") @Wildcard String currency){

        EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager().getEconomyForCurrency(currency);
        if (currencyEcon == null && !currency.equalsIgnoreCase("default")) {
            Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_currency_not_found), currency);
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (sellAllUtil.setCurrency(currency)){
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_currency_edit_success) + " [" + currency + "]");
        }
    }

    @Command(identifier = "sellall", 
    		description = "SellAll main command", 
    		altPermissions = {"-none-", "prison.admin"},
    		onlyPlayers = false)
    private void sellAllCommands(CommandSender sender) {

        if (!isEnabled()) return;

        if (sender.hasPermission("prison.admin")) {
        	sender.dispatchCommand("sellall help");
//            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall help" );
//            sender.dispatchCommand(registeredCmd);
        } else {
        	sender.dispatchCommand("sellall sell");
//            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall sell" );
//            sender.dispatchCommand(registeredCmd);
        }
    }

    @Command(identifier = "sellall set delay", 
    		description = "Enables or disables a SellAll cooldown delay to prevent "
    				+ "players from spamming the sellall command. "
    				+ "See `/sellall set delayTime`.", 
    		onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelay(CommandSender sender,
           @Arg(name = "boolean", 
           		description = "True to enable or false to disable.", 
           		def = "false") String enable){

        if (!isEnabled()) return;

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_boolean_input_invalid));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();

        if (sellAllUtil == null){
            return;
        }

        boolean enableBoolean = getBoolean(enable);
        if (sellAllUtil.isSellAllDelayEnabled == enableBoolean){
            if (enableBoolean){
                Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_already_enabled));
            } else {
                Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_already_disabled));
            }
            return;
        }

        if (sellAllUtil.setDelayEnable(enableBoolean)){
            if (enableBoolean){
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_enabled));
            } else {
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_disabled));
            }
        }
    }

    @Command(identifier = "sellall set delayTime", 
    		description = "This sets the sellall cooldown delay to the specified number of seconds.", 
    		onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelaySet(CommandSender sender,
            @Arg(name = "delay", 
            description = "Set delay value in seconds. Defaults to a value of 15 seconds.", def = "15") String delay){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        int delayValue;
        try {
            delayValue = Integer.parseInt(delay);
        } catch (NumberFormatException ex){
            Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_not_number));
            return;
        }

        if (sellAllUtil.setDelay(delayValue)){
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_delay_edit_success) + " [" + delayValue + "s]");
        }
    }

    @Command(identifier = "sellall set autosell", 
    		description = "Enable SellAll AutoSell. "
    				+ "For AutoFeatures based sellall, please see the AutoFeatures configs.", 
    		onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSell(CommandSender sender,
         @Arg(name = "boolean", 
         	description = "'True' to enable or 'false' to disable. Use 'perUserToggleable' to "
         			+ "enable with users being able to turn on/off.  Default value is 'true'. "
         			+ "[true false perUserToggleable]", 
         		def = "true") String enable){

        if (!isEnabled()) return;
        
        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
        	return;
        }

        if (enable.equalsIgnoreCase("perusertoggleable")){
            sellAllAutoSellPerUserToggleable(sender, enable);
            return;
        }

        if (!enable.equalsIgnoreCase("true") && !enable.equalsIgnoreCase("false") ){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_boolean_input_invalid));
            return;
        }

        boolean enableBoolean = getBoolean(enable);
        if (sellAllUtil.isAutoSellEnabled == enableBoolean){
            if (enableBoolean){
                Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_already_enabled));
            } else {
                Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_already_disabled));
            }
            return;
        }

        if (sellAllUtil.setAutoSell(enableBoolean)){
            if (enableBoolean){
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_enabled));
            } else {
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_disabled));
            }
        }
    }

    @Command(identifier = "sellall set autosellPerUserToggleable", 
    		description = "Enable AutoSell perUserToggleable.  This will "
    				+ "enable autosell if it's not already enabled, plus it will allow players to "
    				+ "turn autosell on/off when they need to using the "
    				+ "command `/sellall autoSellToggle` or the alias `/autosell`. "
    				+ "If user toggleable is disabled through this command, it will also disable "
    				+ "autosell, so you may need to turn it back on with `/sellall set autosell true`.", 
    		onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSellPerUserToggleable(CommandSender sender,
        @Arg(name = "boolean", 
        description = "'True' to enable or 'false' to disable. [true false]", 
        def = "") String enable){

        if (!isEnabled()) return;
        
        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
        	return;
        }

        if ( !enable.equalsIgnoreCase("true") && !enable.equalsIgnoreCase("false") ) {
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_boolean_input_invalid));
            return;
        }

        boolean enableBoolean = getBoolean(enable);
        if (sellAllUtil.isAutoSellPerUserToggleable == enableBoolean){
            if (enableBoolean){
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_perusertoggleable_already_enabled));
            } else {
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_perusertoggleable_already_disabled));
            }
            return;
        }

        if (sellAllUtil.setAutoSellPerUserToggleable(enableBoolean)){
            if (enableBoolean){
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_perusertoggleable_enabled));
            } else {
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_perusertoggleable_disabled));
            }
        }
    }

    @Command(identifier = "sellall sell", description = "SellAll sell command.", onlyPlayers = false)
    public void sellAllSellCommand(CommandSender sender,
    		@Arg(name = "player", def = "", description = "An online player name to sell their inventory - " +
    				"Only console or prison commands can include this parameter") String playerName,
            @Arg(name = "notification", def="",
                    description = "Notification behavior for the sellall transaction. Defaults to normal. " +
                    "'silent' suppresses all notifications. [silent]") String notification ){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);
        

        boolean isOp = sender.isOp();
        
        tech.mcprison.prison.internal.Player sPlayerAlt = getOnlinePlayer( sender, playerName );
        if ( sPlayerAlt == null ){
        	// If sPlayerAlt is null then the value in playerName is really intended for notification:
        	notification = playerName;
        }

        if ( isOp && !sender.isPlayer() && sPlayerAlt != null ) {
        	// Only if OP and a valid player name was provided, then OP is trying to run this
        	// for another player
        	
        	if ( !sPlayerAlt.isOnline() ) {
        		sender.sendMessage( "Player is not online." );
        		return;
        	}
        	
        	// Set the active player to who OP specified:
        	p = ((SpigotPlayer) sPlayerAlt).getWrapper();
        }
        

        else if (p == null){
        	
        	if ( getPlayer( sender, playerName ) != null ) {
        		
        		Output.get().sendInfo(sender, "&cSorry but the specified player must be online "
        				+ "[/sellall sell %s]", playerName );
        	}
        	else {
        		
        		Output.get().sendInfo(sender, "&cSorry but you can't use that from the console!");
        	}
            
            
            return;
        }
        

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

//        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (sellAllUtil.isSellAllSellPermissionEnabled){
            String permission = sellAllUtil.permissionSellAllSell;
            if (permission == null || !p.hasPermission(permission)){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
                return;
            }
        }

        boolean notifications = (notification != null && "silent".equalsIgnoreCase( notification ));

        sellAllUtil.sellAllSell(p, false, notifications, true, true, false, true);
        
        SpigotPlayer sPlayer = new SpigotPlayer( p );
        PlayerAutoRankupTask.autoSubmitPlayerRankupTask( sPlayer, null );
    }

    @Command(identifier = "sellall sellHand", 
    		description = "Sell only what is in your hand if it is sellable.", 
    		onlyPlayers = true)
    public void sellAllSellHandCommand(CommandSender sender){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();

        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllHandEnabled){
            Output.get().sendWarn(sender, "The command /sellall hand is disabled from the config!");
            return;
        }

        Player p = getSpigotPlayer(sender);

        if (p == null){
            Output.get().sendInfo(sender, "&cSorry but you can't use that from the console!");
            return;
        }

//        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (sellAllUtil.isSellAllSellPermissionEnabled){
            String permission = sellAllUtil.permissionSellAllSell;
            if (permission == null || !p.hasPermission(permission)){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
                return;
            }
        }

        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(compat.getItemInMainHand(p));

        itemStacks = sellAllUtil.sellAllSell(p, itemStacks, false, false, true, true, false, true, true);
        if (itemStacks.isEmpty()){
            compat.setItemInMainHand(p, XMaterial.AIR.parseItem());
        } else {
            compat.setItemInMainHand(p, itemStacks.get(0));
        }
    }

    public void sellAllSell(Player p){
        if (!isEnabled()) return;

        if (p == null){
            Output.get().sendInfo(new SpigotPlayer(p), "&cSorry but you can't use that from the console!");
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

//        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (sellAllUtil.isSellAllSellPermissionEnabled){
            String permission = sellAllUtil.permissionSellAllSell;
            if (permission == null || !p.hasPermission(permission)){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
                return;
            }
        }

        sellAllUtil.sellAllSell(p, true, false, true, true, false, true);
        
        
        SpigotPlayer sPlayer = new SpigotPlayer( p );
        PlayerAutoRankupTask.autoSubmitPlayerRankupTask( sPlayer, null );
		
    }

    

    @Command(identifier = "sellall valueOf", 
    		description = "SellAll valueOf command will report the total value of the player's inventory "
    				+ "without selling anything.", onlyPlayers = false)
    public void sellAllValueOfCommand(CommandSender sender,
    		@Arg(name = "player", def = "", description = "An online player name to get the value of their inventory - " +
    				"Only console or prison commands can include this parameter") String playerName
             ){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);
        

        boolean isOp = sender.isOp();
        
        tech.mcprison.prison.internal.Player sPlayerAlt = getOnlinePlayer( sender, playerName );
//        if ( sPlayerAlt == null ){
//        	// If sPlayerAlt is null then the value in playerName is really intended for notification:
//        	notification = playerName;
//        }

        if ( isOp && !sender.isPlayer() && sPlayerAlt != null ) {
        	// Only if OP and a valid player name was provided, then OP is trying to run this
        	// for another player
        	
        	if ( !sPlayerAlt.isOnline() ) {
        		sender.sendMessage( "Player is not online." );
        		return;
        	}
        	
        	// Set the active player to who OP specified:
        	p = ((SpigotPlayer) sPlayerAlt).getWrapper();
        }
        

        else if (p == null){
        	
        	if ( getPlayer( sender, playerName ) != null ) {
        		
        		Output.get().sendInfo(sender, "&cSorry but the specified player must be online "
        				+ "[/sellall valueOf %s]", playerName );
        	}
        	else {
        		
        		Output.get().sendInfo(sender, "&cSorry but you can't use that from the console!");
        	}
            
            
            return;
        }
        

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

//        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (sellAllUtil.isSellAllSellPermissionEnabled){
            String permission = sellAllUtil.permissionSellAllSell;
            if (permission == null || !p.hasPermission(permission)){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
                return;
            }
        }

        SpigotPlayer sPlayer = new SpigotPlayer( p );

        String report = sellAllUtil.getPlayerInventoryValueReport( sPlayer );


        sender.sendMessage(report);
        
        PlayerAutoRankupTask.autoSubmitPlayerRankupTask( sPlayer, null );
    }

    @Command(identifier = "sellall valueOfHand", 
    		description = "Get the value of what is in your hand if sellable.", 
    		onlyPlayers = true)
    public void sellAllValueOfHandCommand(CommandSender sender){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();

        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllHandEnabled){
            Output.get().sendWarn(sender, "The command `/sellall valueOfHand` is disabled from the config! (SellAllHandEnabled)");
            return;
        }

        Player p = getSpigotPlayer(sender);

        if (p == null){
            Output.get().sendInfo(sender, "&cSorry but you can't use that from the console!");
            return;
        }

//        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (sellAllUtil.isSellAllSellPermissionEnabled){
            String permission = sellAllUtil.permissionSellAllSell;
            if (permission == null || !p.hasPermission(permission)){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
                return;
            }
        }


        String report = sellAllUtil.getItemStackValueReport( 
        			new SpigotPlayer(p), new SpigotItemStack( compat.getItemInMainHand(p)) );

        sender.sendMessage(report);
    }

    
    @Command(identifier = "sellall delaysell", 
    		description = "Like SellAll Sell command but this will be delayed for some " +
            "seconds and if sellall sell commands are triggered during this delay, " +
            "they will sum up to the total value that will be visible in a notification at the end of the delay. " +
            "Running more of these commands before a delay have been completed won't reset it and will do the same of /sellall sell " +
            "without a notification (silently).", 
            onlyPlayers = true)
    public void sellAllSellWithDelayCommand(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);

        if (p == null){
            Output.get().sendInfo(sender, "&cSorry but you can't use that from the console!");
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

//        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (sellAllUtil.isSellAllSellPermissionEnabled){
            String permission = sellAllUtil.permissionSellAllSell;
            if (permission == null || !p.hasPermission(permission)){
                Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
                return;
            }
        }

        if (!sellAllUtil.isAutoSellEarningNotificationDelayEnabled){
            sellAllSellCommand(sender, null, "silent");
            return;
        }

        sellAllUtil.sellAllSell(p, false, false, false, false, true, false);
        
        SpigotPlayer sPlayer = new SpigotPlayer( p );
        PlayerAutoRankupTask.autoSubmitPlayerRankupTask( sPlayer, null );
    }


    @Command(identifier = "sellall autoSellToggle", 
    		aliases = {"autosell"}, 
    		description = "When `perUserToggleable` is enabled with autosell, this "
    				+ "command allow the players to enable or disable their own use of autosell.  "
    				+ "When using this command, autosell will be toggled on/off for the player. "
    				+ "Can also use the alias `/autosell` to toggle this setting", 
    		altPermissions = "prison.sellall.toggle", onlyPlayers = true)
    private void sellAllAutoEnableUser(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);

        // Sender must be a Player, not something else like the Console.
        if (p == null) {
            Output.get().sendError(sender, getMessages().getString(MessagesConfig.StringID.spigot_message_console_error));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

//        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (!sellAllUtil.isAutoSellPerUserToggleable){
            return;
        }

        String permission = sellAllUtil.permissionAutoSellPerUserToggleable;
        if (sellAllUtil.isAutoSellPerUserToggleablePermEnabled && (permission != null && !p.hasPermission(permission))){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
            return;
        }

        boolean isplayerAutosellEnabled = sellAllUtil.isPlayerAutoSellEnabled(p);
        if (sellAllUtil.setAutoSellPlayer(p, !isplayerAutosellEnabled)){
            if ( !isplayerAutosellEnabled ){ // Note this variable was negated then saved, so we need to check the negative:
                Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_disabled));
            } else {
            	Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_enabled));
            }
        }
    }
//
//    @Command(identifier = "sellall gui", 
//    		description = "SellAll GUI command", 
////    		aliases = "gui sellall",
//    		permissions = "prison.admin", onlyPlayers = true)
//    private void sellAllGuiCommand(CommandSender sender,
//    		@Arg(name = "page", description = "If there are more than 45 items, then they " +
//    				"will be shown on multiple pages.  The page parameter starts with " +
//    				"page 1.", def = "1" ) int page){
//
//        if (!isEnabled()) return;
//
//        Player p = getSpigotPlayer(sender);
//
//        // Sender must be a Player, not something else like the Console.
//        if (p == null) {
//            Output.get().sendError(sender, getMessages().getString(MessagesConfig.StringID.spigot_message_console_error));
//            return;
//        }
//
//        SellAllUtil sellAllUtil = SellAllUtil.get();
//        if (sellAllUtil == null){
//            return;
//        }
//
//        if (!sellAllUtil.openSellAllGUI( p, page, "sellall gui", "close" )){
//            // If the sender's an admin (OP or have the prison.admin permission) it'll send an error message.
//            if (p.hasPermission("prison.admin")) {
//            	
//            	new SpigotVariousGuiMessages().sellallGUIIsDisabledMsg(sender);
////                Output.get().sendError(sender, 
////                		messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_disabled));
//            }
//        }
//    }
//    
//    @Command(identifier = "sellall gui blocks", 
//    		description = "SellAll GUI Blocks command", 
//    		aliases = "gui sellall",
//    		permissions = "prison.admin", onlyPlayers = true)
//    private void sellAllGuiBlocksCommand(CommandSender sender,
//    		@Arg(name = "page", description = "If there are more than 45 items, then they " +
//    				"will be shown on multiple pages.  The page parameter starts with " +
//    				"page 1.", def = "1" ) int page){
//    	
//    	if (!isEnabled()) return;
//    	
//    	Player p = getSpigotPlayer(sender);
//    	
//    	// Sender must be a Player, not something else like the Console.
//    	if (p == null) {
//    		Output.get().sendError(sender, getMessages().getString(MessagesConfig.StringID.spigot_message_console_error));
//    		return;
//    	}
//    	
//    	SellAllAdminBlocksGUI saBlockGui = new SellAllAdminBlocksGUI( p, page, "sellall gui blocks", "sellall gui" );
//    	saBlockGui.open();
//    	
//    }

    @Command(identifier = "sellall items add", 
    		description = "This will add an item to the SellAll shop. "
    				+ "Use `/mines block search help` to find the correct names "
    				+ "for blocks to add to the sellall shop.  Use "
    				+ "`/mines block searchAll help` to search for items too.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllAddCommand(CommandSender sender,
                   @Arg(name = "Item_ID", 
                   		description = "The Item_ID or block to add to the sellAll Shop.") String itemID,
                   @Arg(name = "Value", description = "The value of the item.") Double value){

        if (!isEnabled()) return;

        if (itemID == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_name));
            return;
        }
        itemID = itemID.toUpperCase();

        if (value == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_price));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (sellAllUtil.sellAllConfig.getConfigurationSection("Items." + itemID) != null){
            Output.get().sendWarn(sender, itemID + " " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_already_added));
            return;
        }

        try {
            XMaterial blockAdd;
            try {
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
                return;
            }

            if (sellAllUtil.addSellAllBlock(blockAdd, value)){
                Output.get().sendInfo(sender, "&3 ITEM [" + itemID + ", " + value + "] " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_add_success));
            }

        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
        }
    }

    /**
     * <p>This will add the XMaterial and value to the sellall.
     * This will update even if the sellall has not been enabled.
     * </p>
     *
     * @param blockAdd
     * @param value
     */
    public void sellAllAddCommand(XMaterial blockAdd, Double value){

        String itemID = blockAdd.name();

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        // If the block or item was already configured, then skip this:
        if (sellAllUtil.sellAllConfig.getConfigurationSection("Items." + itemID) != null){
            return;
        }

        if (sellAllUtil.addSellAllBlock(blockAdd, value)) return;

        Output.get().logInfo("&3 ITEM [" + itemID + ", " + value + " " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_add_success));
    }

    @Command(identifier = "sellall items delete", 
    		description = "This command will delete an item from the "
    				+ "sellall shop. Use `/sellall list` to identify which items to revmove.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllDeleteCommand(CommandSender sender, 
    		@Arg(name = "Item_ID", description = "The Item_ID you want to remove.") String itemID){

        if (!isEnabled()) return;

        if (itemID == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_name));
            return;
        }
        itemID = itemID.toUpperCase();

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (sellAllUtil.sellAllConfig.getConfigurationSection("Items." + itemID) == null){
            Output.get().sendWarn(sender, itemID + " " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_cant_find_item_config));
            return;
        }

        if (XMaterial.matchXMaterial(itemID).isPresent()) {
            if (sellAllUtil.removeSellAllBlock(XMaterial.matchXMaterial(itemID).get())) {
                Output.get().sendInfo(sender, itemID + " " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_delete_success));
            }
        }
    }

    @Command(identifier = "sellall items edit", 
    		description = "Edits an existing SellAll shop item.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllEditCommand(CommandSender sender,
                @Arg(name = "Item_ID", description = "The Item_ID or block to add to the sellAll Shop.") String itemID,
                @Arg(name = "Value", description = "The value of the item.") Double value){

        if (!isEnabled()) return;

        if (itemID == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_name));
            return;
        }
        itemID = itemID.toUpperCase();

        if (value == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_price));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (sellAllUtil.sellAllConfig.getConfigurationSection("Items." + itemID) == null){
            Output.get().sendWarn(sender, itemID + " " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_not_found));
            return;
        }

        try {
            XMaterial blockAdd;
            try{
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
                return;
            }

            if (sellAllUtil.editPrice(blockAdd, value)){
                Output.get().sendInfo(sender, "&3ITEM [" + itemID + ", " + value + "] " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_edit_success));
            }

        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
        }
    }

    
    @Command(identifier = "sellall multiplier list", 
    		description = "Lists all of the SellAll Rank multipliers", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllMultiplierCommand(CommandSender sender, 
    		@Wildcard(join=true)
    		@Arg(name = "options", 
    			description = "Optionaly, you can control which ladder is displayed.  By default, it's "
    					+ "all ladders. Just use the ladder's name.  You can also change the number of "
    					+ "columns in the output by using 'cols=16', where the default is 10 columns.  ")
    		String options ) {

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllMultiplierEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
            return;
        }

    	
    	if ( !PrisonRanks.getInstance().isEnabled() ) {
    		Output.get().sendWarn(sender, "Cannot use command `/sellall multiplier addLadder` since ranks are disabled" );
    		return;
    	}
    	
    	int displayColumns = 10;
    	String ladderName = "";
    	RankLadder rLadder = null;
    	
    	
    	// pull columns out of the options, if it has been specified:
		String colsStr = extractParameter("cols=", options);
		if ( colsStr != null ) {
			options = options.replace( colsStr, "" ).trim();
			colsStr = colsStr.replace( "cols=", "" ).trim();
			
			try {
				displayColumns = Integer.parseInt( colsStr );
			}
			catch ( NumberFormatException e ) {
				// Not a valid int number:
			}
		}
		
		
		// Check to see if the remaining options is a ladder name, if so, then only load that ladder:
		if ( options.length() > 0 ) {
			rLadder = PrisonRanks.getInstance().getLadderManager().getLadder( options );
		}
		
    	
    	List<RankLadder> ladders = new ArrayList<>();
    	
    	if ( rLadder == null ) {
    		ladders = PrisonRanks.getInstance().getLadderManager().getLadders();
    	}
    	else {
    		ladders.add( rLadder );
    	}
    	
    	

        
//        String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall multiplier help" );
//        sender.dispatchCommand(registeredCmd);
        
        TreeMap<String, Double> mults = new TreeMap<>( sellAllUtil.getPrestigeMultipliers() );

//        TreeMap<XMaterial, Double> items = new TreeMap<>( sellAllUtil.getSellAllBlocks() );
        DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
        DecimalFormat iFmt = Prison.get().getDecimalFormat("#,##0");
        
        Set<String> keys = mults.keySet();
        
        // Need to calculate the maxLenVal so we can properly space all columns:
        int maxLenKey = 0;
        int maxLenVal = 0;
        for ( String key : keys ) {
			if ( key.length() > maxLenKey ) {
				maxLenKey = key.length();
			}
			String val = dFmt.format( mults.get( key ) );
			if ( val.length() > maxLenVal ) {
				maxLenVal = val.length();
			}
		}
        String multiplierLayout = "%-" + maxLenKey + "s %" + maxLenVal + "s";
        
        
        ChatDisplay chatDisplay = new ChatDisplay("&bSellall Rank Multipliers list: &3(&b" + keys.size() + "&3)" );


        for (RankLadder ladder : ladders ) {
			
        	StringBuilder sb = new StringBuilder();

        	int lines = 0;
        	int columns = 0;
        	for ( Rank rank : ladder.getRanks() ) {
        		String key = rank.getName();
        		
				if ( mults.containsKey( key ) ) {
					
					if ( lines == 0 && sb.length() == 0 ) {
						chatDisplay.addText( "&3Ladder: &7%s  &3Ranks: &7%s", 
								ladder.getName(),
								iFmt.format( ladder.getRanks().size() ));
					}
					
		        	Double cost = mults.get( key );
		        	
		        	if ( columns++ > 0 ) {
		        		sb.append( "    " );
		        	}
		        	
		        	sb.append( String.format( multiplierLayout, 
		        			key, dFmt.format( cost ) ) );
		        	
		        	if ( columns >= displayColumns ) {
		        		chatDisplay.addText( sb.toString() );
		        		
		        		if ( ++lines % 10 == 0 && lines > 1 ) {
		        			chatDisplay.addText( " " );
		        		}
		        		
		        		sb.setLength( 0 );
		        		columns = 0;
		        	}

				}
			}
            if ( sb.length() > 0 ) {
            	chatDisplay.addText( sb.toString() );
            }
        	
		}
        
        
//        int columns = 0;
//        for ( String key : keys ) {
////        	boolean first = sb.length() == 0;
//
//        	Double cost = mults.get( key );
//        	
//        	if ( columns++ > 0 ) {
//        		sb.append( "    " );
//        	}
//        	
//        	sb.append( String.format( "%-" + maxLenKey + "s %" + maxLenVal + "s", 
//        			key, fFmt.format( cost ) ) );
////        	sb.append( String.format( "%-" + maxLenKey + "s  %" + maxLenVal + "s  %-" + maxLenCode + "s", 
////        			key.toString(), fFmt.format( cost ), key.name() ) );
//        	
//        	if ( columns > 7 ) {
//        		chatDisplay.addText( sb.toString() );
//        		
//        		if ( ++lines % 10 == 0 && lines > 1 ) {
//        			chatDisplay.addText( " " );
//        		}
//        		
//        		sb.setLength( 0 );
//        		columns = 0;
//        	}
//        }
//        if ( sb.length() > 0 ) {
//        	chatDisplay.addText( sb.toString() );
//        }
        
        chatDisplay.send( sender );

    }

	private String extractParameter( String key, String options ) {
		return extractParameter( key, options, true );
	}
	private String extractParameter( String key, String options, boolean tryLowerCase ) {
		String results = null;
		int idx = options.indexOf( key );
		if ( idx != -1 ) {
			int idxEnd = options.indexOf( " ", idx );
			if ( idxEnd == -1 ) {
				idxEnd = options.length();
			}
			results = options.substring( idx, idxEnd );
		}
		else if ( tryLowerCase ) {
			// try again, but lowercase the key
			results = extractParameter( key.toLowerCase(), options, false );
		}
		return results;
	}
    
    @Command(identifier = "sellall multiplier add", 
    		description = "Add a sellall multiplier based upon the player's rank. " +
    				"All ranks that a player has, could have their own multiplier, that can " +
    				"be combined to help the player increase the value of what they are selling. " +
    				"These multipliers will be combined with all other player mulitpliers to " +
    				"adjust the sales price within sellall.  Multipliers can be from these " +
    				"rank multipliers, and also from permission multipliers. " +
    				"All players start off with a default value of a multiplier with a value of 1, which " +
    				"equates to 100% of what they sell: no loss and no gain.  From there, multipliers that " +
    				"you give to players can be integers, or doubles. They can be greater than 1, or less than " +
    				"1, or even negative.  All multipliers are added together to provide the player's total " +
    				"amount. " +
    				"Permission multipliers for player's `prison.sellall.multiplier.<value>`, "
    				+ "example `prison.sellall.multiplier.2` will add a 2x multiplier. "
    				+ "The multiplier values do not have to integers, but can be less than one, or "
    				+ "doubles. " +
            "There's also another kind of Multiplier called permission multipliers, they're permissions "
            + "that you can give to players to give them a multiplier, remember that their format is "
            + "'prison.sellall.multiplier.2.5' (for example), and this example will give you a " +
            "total of 3.5x multiplier (1x default + 2.5x permission = 3.5x).",
            permissions = "prison.admin", onlyPlayers = false)
    private void sellAllAddMultiplierCommand(CommandSender sender,
           @Arg(name = "rank", description = "The rank name for the multiplier.") String rank,
           @Arg(name = "multiplier", description = "Multiplier value.") Double multiplier) {

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllMultiplierEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
            return;
        }

        if (sellAllUtil.addSellallRankMultiplier(rank, multiplier)){
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_add_success));
        }
        else {
        	Output.get().sendInfo( sender, "Failed to add sellall rank multiplier." );
        }
    }

    @Command(identifier = "sellall multiplier delete", 
    		description = "Remove a SellAll rank multiplier.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllDeleteMultiplierCommand(CommandSender sender,
         @Arg(name = "Rank", description = "The rank name of the multiplier.") String rank){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllMultiplierEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
            return;
        }

        if (rank == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_command_wrong_format));
            return;
        }

        if (sellAllUtil.sellAllConfig.getConfigurationSection("Multiplier." + rank) == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_cant_find) + " [" + rank + "]");
            return;
        }

        if (sellAllUtil.removeSellallRankMultiplier(rank)){
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_delete_success));
        }
    }
    
    
    

    @Command(identifier = "sellall multiplier deleteLadder", 
    		description = "Deletes all SellAll Rank multipliers for a ladder.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllMultiplierDeleteLadderCommand(
    		CommandSender sender,
    		@Arg(name = "ladder", 
    			description = "All ranks with multipliers for this ladder will be removed.") String ladderName
    		
    		) {

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllMultiplierEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
            return;
        }
        
        if ( !PrisonRanks.getInstance().isEnabled() ) {
        	Output.get().sendWarn(sender, "Cannot use command `/sellall multiplier deleteLadder` since ranks are disabled" );
        	return;
        }

        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        
        
        if ( ladder == null ) {
        	Output.get().sendWarn(sender, 
        			"A ladder with the name of '%s' does not exist. Use '/ranks ladder list' "
        			+ "to find the correct ladder name.", ladderName );
        	
        	return;
        }
        
        DecimalFormat iFmt = Prison.get().getDecimalFormat("#,##0");

        int removed = 0;
        for (Rank rank : ladder.getRanks() ) {
			
            if (sellAllUtil.removeSellallRankMultiplier( rank.getName() )) {
                removed++;
            }
		}
        
        sender.sendMessage( 
        		String.format( 
        				"For ladder %s, there were %s multipliers removed.",
        				ladderName, iFmt.format(removed)) );
        
    }

    
    
    @Command(identifier = "sellall multiplier addLadder", 
    		description = "Adds multipliers for all ranks on the ladder. "
    				+ "If they already exist, they will be replaced. The formula used to "
    				+ "calculate the multiplier is "
    				+ "'multiplier = baseMultiplier + ((rankPosition - 1) * rankMultiplier)'. "
    				+ "Example: '/sellall multiplier addLadder presetiges 1.0 0.1' will result in "
    				+ "p1=1.0 p2=1.1 p3=1.2 p4=1.3 etc...", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllMultiplierAddLadderCommand(
    		CommandSender sender,
    		@Arg(name = "ladder",
    			def = "prestiges",
    			description = "Add multipliers for all ranks on this ladder. "
    					+ "If multipliers already exist, they will be replaced.") 
    			String ladderName,
    		@Arg(name = "baseMultiplier",
    				def = "1.0",
    				description = "The baseMultiplier that will have the rank's "
    						+ "position multiplier added to it.")
    			double baseMultiplier,
    		@Arg(name = "rankMultiplier", 
    				def = "0.1",
    				description = "The multiplier applied to the rank position.")
    			double rankMultiplier
    		
    		) {
    	
    	if (!isEnabled()) return;
    	
    	SellAllUtil sellAllUtil = SellAllUtil.get();
    	if (sellAllUtil == null){
    		return;
    	}
    	
    	if (!sellAllUtil.isSellAllMultiplierEnabled){
    		Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
    		return;
    	}
    	
    	if ( !PrisonRanks.getInstance().isEnabled() ) {
    		Output.get().sendWarn(sender, "Cannot use command `/sellall multiplier addLadder` since ranks are disabled" );
    		return;
    	}
    	
    	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
    	
    	
    	if ( ladder == null ) {
    		Output.get().sendWarn(sender, 
    				"A ladder with the name of '%s' does not exist. Use '/ranks ladder list' "
    						+ "to find the correct ladder name.", ladderName );
    		
    		return;
    	}
    	
    	DecimalFormat iFmt = Prison.get().getDecimalFormat("#,##0");
    	
    	int added = 0;
    	int failed = 0;
    	for (Rank rank : ladder.getRanks() ) {
    		
    		int rankPos = rank.getPosition();
    		
    		double multi = baseMultiplier + (rankPos * rankMultiplier);
    		
    		if ( sellAllUtil.addSellallRankMultiplier(rank.getName(), multi) ) {
    			// No message should be sent for each rank, since there could be thousands of prestige ranks
    			added++;
    		}
            else {
            	failed++;
            }
    		
    	}
    	
    	sender.sendMessage( 
    			String.format( 
    					"For ladder %s, there were %s multipliers added, and %s failed to be added.",
    					ladderName, 
    					iFmt.format(added),
    					iFmt.format(failed)
    					) );
    	
    }
    

    

    @Command(identifier = "sellall set trigger", 
    		description = "Toggle SellAll trigger to enable/disable the Shift+Right Clicking "
    				+ "on a tool to trigger the `/sellall sell` command "
    				+ "true = enable, false = disable. "
    				+ "[true false].", 
    				permissions = "prison.admin", onlyPlayers = false)
    private void sellAllToolsTriggerToggle(CommandSender sender,
    		@Arg(name = "Boolean", description = "Enable or disable", def = "true") String enable){

        if (!isEnabled()) return;

        if (enable.equalsIgnoreCase("null")){
        	sender.dispatchCommand("sellall toolsTrigger help");
//            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall toolsTrigger help" );
//            sender.dispatchCommand(registeredCmd);
            return;
        }

        if (!enable.equalsIgnoreCase("true") && !enable.equalsIgnoreCase("false")){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_boolean_input_invalid));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        boolean enableInput = getBoolean(enable);
        if (sellAllUtil.isSellAllItemTriggerEnabled == enableInput) {
            if (sellAllUtil.isSellAllItemTriggerEnabled) {
                Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_already_enabled));
            } else {
                Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_already_disabled));
            }
            return;
        }

        if (sellAllUtil.setItemTrigger(enableInput)){
            if (enableInput){
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_enabled));
            } else {
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_disabled));
            }
        }
    }

    @Command(identifier = "sellall set trigger add", 
    		description = "Add an Item to trigger the Shift+Right Click -> /sellall sell command.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllTriggerAdd(CommandSender sender,
                                   @Arg(name = "Item", description = "Item name") String itemID){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllItemTriggerEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_is_disabled));
            return;
        }

        if (itemID == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_name));
            return;
        }
        itemID = itemID.toUpperCase();

        try {
            XMaterial blockAdd;
            try{
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
                return;
            }

            if (sellAllUtil.addItemTrigger(blockAdd)){
                Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_item_add_success) + " [" + itemID + "]");
            }
        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
        }
    }

    @Command(identifier = "sellall set trigger delete", 
    		description = "Delete an Item from the Shift+Right Click trigger -> /sellall sell command.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllTriggerDelete(CommandSender sender,
                                      @Arg(name = "Item", description = "Item name") String itemID){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllItemTriggerEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_is_disabled));
            return;
        }

        if (itemID == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_name));
            return;
        }
        itemID = itemID.toUpperCase();

        if (!XMaterial.matchXMaterial(itemID).isPresent()){
            Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
            return;
        }
        XMaterial xMaterial = XMaterial.matchXMaterial(itemID).get();

        if (!sellAllUtil.getItemTriggerXMaterials().contains(xMaterial)){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_missing_name));
            return;
        }

        if (sellAllUtil.removeItemTrigger(xMaterial)) {
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_trigger_item_delete_success) + " [" + itemID + "]");
        }
    }

    @Command(identifier = "sellall items setdefaults", 
    		description = "This command will setup all of the shop items with "
    				+ "the SellAll default items.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllSetDefaultCommand(CommandSender sender){

        if (!isEnabled()) return;

        // Setup all the prices in sellall:
        SpigotPlatform platform = (SpigotPlatform) Prison.get().getPlatform();
        for ( SellAllBlockData xMatCost : platform.buildBlockListXMaterial() ) {

            // Add blocks to sellall:
            sellAllAddCommand(sender, xMatCost.getBlock().name(), xMatCost.getPrice() );
        }

        Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_default_values_success));
    }
    
    
    @Command(identifier = "sellall list", description = "SellAll list all items", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllListItems( CommandSender sender ) {

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }
        
        TreeMap<String, PrisonBlock> items = new TreeMap<>( sellAllUtil.getSellAllItems() );
        DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
        
        Set<String> keys = items.keySet();
        
        int maxLenKey = 0;
        int maxLenVal = 0;
        int maxLenCode = 0;
        for ( String key : keys ) {
//			if ( key.toString().length() > maxLenKey ) {
//				maxLenKey = key.toString().length();
//			}
			if ( key.length() > maxLenCode ) {
				maxLenCode = key.length();
			}
			String val = fFmt.format( items.get( key ).getSalePrice() );
			if ( val.length() > maxLenVal ) {
				maxLenVal = val.length();
			}
		}
        
        
        ChatDisplay chatDisplay = new ChatDisplay("&bSellall Item list: &3(&b" + keys.size() + "&3)" );

        int lines = 0;
        StringBuilder sb = new StringBuilder();
        for ( String key : keys ) {
        	boolean first = sb.length() == 0;

        	Double cost = items.get( key ).getSalePrice();
        	
        	if ( !first ) {
        		sb.append( "    " );
        	}
        	
        	sb.append( String.format( "%-" + maxLenCode + "s %" + maxLenVal + "s", 
        			key, fFmt.format( cost ) ) );
//        	sb.append( String.format( "%-" + maxLenKey + "s  %" + maxLenVal + "s  %-" + maxLenCode + "s", 
//        			key.toString(), fFmt.format( cost ), key.name() ) );
        	
        	if ( !first ) {
        		chatDisplay.addText( sb.toString() );
        		
        		if ( ++lines % 10 == 0 && lines > 1 ) {
        			chatDisplay.addText( " " );
        		}
        		
        		sb.setLength( 0 );
        	}
        }
        if ( sb.length() > 0 ) {
        	chatDisplay.addText( sb.toString() );
        }
        
        chatDisplay.send( sender );
        
    }
    
}
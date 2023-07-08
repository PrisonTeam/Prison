package tech.mcprison.prison.spigot.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.sellall.messages.SpigotVariousGuiMessages;
import tech.mcprison.prison.spigot.SpigotPlatform;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotOfflinePlayer;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminBlocksGUI;
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

    @Command(identifier = "sellall", description = "SellAll main command", 
    		altPermissions = {"-none-", "prison.admin"},
    		onlyPlayers = false)
    private void sellAllCommands(CommandSender sender) {

        if (!isEnabled()) return;

        if (sender.hasPermission("prison.admin")) {
            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall help" );
            sender.dispatchCommand(registeredCmd);
        } else {
            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall sell" );
            sender.dispatchCommand(registeredCmd);
        }
    }

    @Command(identifier = "sellall delay", description = "SellAll delay.", 
    		onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelay(CommandSender sender,
                              @Arg(name = "boolean", description = "True to enable or false to disable.", def = "null") String enable){

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

    @Command(identifier = "sellall set delay", description = "Edit SellAll delay.", 
    		onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelaySet(CommandSender sender,
                                 @Arg(name = "delay", description = "Set delay value in seconds.", def = "0") String delay){

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

    @Command(identifier = "sellall autosell", description = "Enable SellAll AutoSell.", 
    		onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSell(CommandSender sender,
                                 @Arg(name = "boolean", description = "True to enable or false to disable.", def = "null") String enable){

        if (!isEnabled()) return;

        if (enable.equalsIgnoreCase("perusertoggleable")){
            sellAllAutoSellPerUserToggleable(sender, enable);
            return;
        }

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_boolean_input_invalid));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
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

    @Command(identifier = "sellall autosell perUserToggleable", description = "Enable AutoSell perUserToggleable.", 
    		onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSellPerUserToggleable(CommandSender sender,
                                                  @Arg(name = "boolean", description = "True to enable or false to disable", def = "null") String enable){

        if (!isEnabled()) return;

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_boolean_input_invalid));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
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
                    description = "Notification about the sellall transaction. Defaults to normal. " +
                    "'silent' suppresses results. [silent]") String notification ){

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

        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

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

    @Command(identifier = "sellall hand", description = "Sell only what is in your hand if sellable.", 
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

        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

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

        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

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

    @Command(identifier = "sellall delaysell", description = "Like SellAll Sell command but this will be delayed for some " +
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

        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

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


    @Command(identifier = "sellall auto toggle", description = "Let the user enable or disable sellall auto", 
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

        if (sellAllUtil.isPlayerInDisabledWorld(p)) return;

        if (!sellAllUtil.isAutoSellPerUserToggleable){
            return;
        }

        String permission = sellAllUtil.permissionAutoSellPerUserToggleable;
        if (sellAllUtil.isAutoSellPerUserToggleablePermEnabled && (permission != null && !p.hasPermission(permission))){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + permission + "]");
            return;
        }

        if (sellAllUtil.setAutoSellPlayer(p, !sellAllUtil.isPlayerAutoSellEnabled(p))){
            if (sellAllUtil.isPlayerAutoSellEnabled(p)){
                Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_enabled));
            } else {
                Output.get().sendInfo(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_sellall_auto_disabled));
            }
        }
    }

    @Command(identifier = "sellall gui", 
    		description = "SellAll GUI command", 
//    		aliases = "gui sellall",
    		permissions = "prison.admin", onlyPlayers = true)
    private void sellAllGuiCommand(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 items, then they " +
    				"will be shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page){

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

        if (!sellAllUtil.openSellAllGUI( p, page, "sellall gui", "close" )){
            // If the sender's an admin (OP or have the prison.admin permission) it'll send an error message.
            if (p.hasPermission("prison.admin")) {
            	
            	new SpigotVariousGuiMessages().sellallGUIIsDisabledMsg(sender);
//                Output.get().sendError(sender, 
//                		messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_disabled));
            }
        }
    }
    
    @Command(identifier = "sellall gui blocks", 
    		description = "SellAll GUI Blocks command", 
    		aliases = "gui sellall",
    		permissions = "prison.admin", onlyPlayers = true)
    private void sellAllGuiBlocksCommand(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 items, then they " +
    				"will be shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page){
    	
    	if (!isEnabled()) return;
    	
    	Player p = getSpigotPlayer(sender);
    	
    	// Sender must be a Player, not something else like the Console.
    	if (p == null) {
    		Output.get().sendError(sender, getMessages().getString(MessagesConfig.StringID.spigot_message_console_error));
    		return;
    	}
    	
    	SellAllAdminBlocksGUI saBlockGui = new SellAllAdminBlocksGUI( p, page, "sellall gui blocks", "sellall gui" );
    	saBlockGui.open();
    	
    }

    @Command(identifier = "sellall add", description = "SellAll add an item to the sellAll shop.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllAddCommand(CommandSender sender,
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
                Output.get().sendInfo(sender, "&3 ITEM [" + itemID + ", " + value + " " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_add_success));
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

    @Command(identifier = "sellall delete", description = "SellAll delete command, remove an item from shop.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllDeleteCommand(CommandSender sender, @Arg(name = "Item_ID", description = "The Item_ID you want to remove.") String itemID){

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

    @Command(identifier = "sellall edit", description = "SellAll edit command, edit an item of Shop.", 
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
                Output.get().sendInfo(sender, "&3ITEM [" + itemID + ", " + value + " " + messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_edit_success));
            }

        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_item_id_not_found) + " [" + itemID + "]");
        }
    }

    @Command(identifier = "sellall multiplier list", description = "SellAll multiplier command list", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllMultiplierCommand(CommandSender sender){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllMultiplierEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
            return;
        }

        
//        String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall multiplier help" );
//        sender.dispatchCommand(registeredCmd);
        
        TreeMap<String, Double> mults = new TreeMap<>( sellAllUtil.getPrestigeMultipliers() );

//        TreeMap<XMaterial, Double> items = new TreeMap<>( sellAllUtil.getSellAllBlocks() );
        DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
        
        Set<String> keys = mults.keySet();
        
        int maxLenKey = 0;
        int maxLenVal = 0;
        for ( String key : keys ) {
			if ( key.length() > maxLenKey ) {
				maxLenKey = key.length();
			}
			String val = fFmt.format( mults.get( key ) );
			if ( val.length() > maxLenVal ) {
				maxLenVal = val.length();
			}
		}
        
        
        ChatDisplay chatDisplay = new ChatDisplay("&bSellall Prestige Multipliers list: &3(&b" + keys.size() + "&3)" );

        int lines = 0;
        int columns = 0;
        StringBuilder sb = new StringBuilder();
        for ( String key : keys ) {
//        	boolean first = sb.length() == 0;

        	Double cost = mults.get( key );
        	
        	if ( columns++ > 0 ) {
        		sb.append( "    " );
        	}
        	
        	sb.append( String.format( "%-" + maxLenKey + "s %" + maxLenVal + "s", 
        			key, fFmt.format( cost ) ) );
//        	sb.append( String.format( "%-" + maxLenKey + "s  %" + maxLenVal + "s  %-" + maxLenCode + "s", 
//        			key.toString(), fFmt.format( cost ), key.name() ) );
        	
        	if ( columns > 4 ) {
        		chatDisplay.addText( sb.toString() );
        		
        		if ( ++lines % 10 == 0 && lines > 1 ) {
        			chatDisplay.addText( " " );
        		}
        		
        		sb.setLength( 0 );
        		columns = 0;
        	}
        }
        if ( sb.length() > 0 ) {
        	chatDisplay.addText( sb.toString() );
        }
        
        chatDisplay.send( sender );

        
        
        
    }

    
    @Command(identifier = "sellall multiplier add", description = "SellAll add a multiplier. Permission multipliers for player's prison.sellall.multiplier.<valueHere>, example prison.sellall.multiplier.2 will add a 2x multiplier," +
            "There's also another kind of Multiplier called permission multipliers, they're permissions that you can give to players to give them a multiplier, remember that their format is prison.sellall.multiplier.2 (for example), and this example will give you a " +
            "total of 3x multiplier (1x default + 2x permission = 3x).",
            permissions = "prison.admin", onlyPlayers = false)
    private void sellAllAddMultiplierCommand(CommandSender sender,
                                             @Arg(name = "Prestige", description = "Prestige to hook to the multiplier.") String prestige,
                                             @Arg(name = "multiplier", description = "Multiplier value.") Double multiplier){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllMultiplierEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
            return;
        }

        if (sellAllUtil.addPrestigeMultiplier(prestige, multiplier)){
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_add_success));
        }
        else {
        	Output.get().sendInfo( sender, "Failed to add sellall prestige multiplier." );
        }
    }

    @Command(identifier = "sellall multiplier delete", description = "SellAll delete a multiplier.", 
    		permissions = "prison.admin", onlyPlayers = false)
    private void sellAllDeleteMultiplierCommand(CommandSender sender,
                                                @Arg(name = "Prestige", description = "Prestige hooked to the multiplier.") String prestige){

        if (!isEnabled()) return;

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.isSellAllMultiplierEnabled){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_are_disabled));
            return;
        }

        if (prestige == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_command_wrong_format));
            return;
        }

        if (sellAllUtil.sellAllConfig.getConfigurationSection("Multiplier." + prestige) == null){
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_cant_find) + " [" + prestige + "]");
            return;
        }

        if (sellAllUtil.removePrestigeMultiplier(prestige)){
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_sellall_multiplier_delete_success));
        }
    }

    @Command(identifier = "sellall Trigger", 
    		description = "Toggle SellAll Shift+Right Click on a tool to trigger the /sellall sell command, "
    				+ "true -> Enabled or False -> Disabled.", 
    				permissions = "prison.admin", onlyPlayers = false)
    private void sellAllToolsTriggerToggle(CommandSender sender,
                                           @Arg(name = "Boolean", description = "Enable or disable", def = "null") String enable){

        if (!isEnabled()) return;

        if (enable.equalsIgnoreCase("null")){
            String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall toolsTrigger help" );
            sender.dispatchCommand(registeredCmd);
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

    @Command(identifier = "sellall Trigger add", 
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

    @Command(identifier = "sellall Trigger delete", 
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

    @Command(identifier = "sellall setdefault", description = "SellAll default values ready to go.", 
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
        
        TreeMap<XMaterial, Double> items = new TreeMap<>( sellAllUtil.getSellAllBlocks() );
        DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
        
        Set<XMaterial> keys = items.keySet();
        
        int maxLenKey = 0;
        int maxLenVal = 0;
        int maxLenCode = 0;
        for ( XMaterial key : keys ) {
			if ( key.toString().length() > maxLenKey ) {
				maxLenKey = key.toString().length();
			}
			if ( key.name().length() > maxLenCode ) {
				maxLenCode = key.name().length();
			}
			String val = fFmt.format( items.get( key ) );
			if ( val.length() > maxLenVal ) {
				maxLenVal = val.length();
			}
		}
        
        
        ChatDisplay chatDisplay = new ChatDisplay("&bSellall Item list: &3(&b" + keys.size() + "&3)" );

        int lines = 0;
        StringBuilder sb = new StringBuilder();
        for ( XMaterial key : keys ) {
        	boolean first = sb.length() == 0;

        	Double cost = items.get( key );
        	
        	if ( !first ) {
        		sb.append( "    " );
        	}
        	
        	sb.append( String.format( "%-" + maxLenCode + "s %" + maxLenVal + "s", 
        			key.name(), fFmt.format( cost ) ) );
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
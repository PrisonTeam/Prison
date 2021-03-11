package tech.mcprison.prison.spigot.sellall;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import at.pcgamingfreaks.Minepacks.Bukkit.API.Backpack;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPlatform;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.commands.PrisonSpigotBaseCommands;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllPlayerGUI;
import tech.mcprison.prison.spigot.integrations.IntegrationMinepacksPlugin;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SellAllPrisonCommands extends PrisonSpigotBaseCommands {

    private Configuration sellAllConfig = SpigotPrison.getInstance().getSellAllConfig();
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
    private static SellAllPrisonCommands instance;
    private String idBeingProcessedBackpack = null;
    public static List<String> activePlayerDelay = new ArrayList<>();
    public boolean signUsed = false;
    public inventorySellMode mode = inventorySellMode.PlayerInventory;


    /**
     * SellAll mode.
     * */
    public enum inventorySellMode{
        PlayerInventory,
        MinesBackPack,
        PrisonBackPackSingle,
        PrisonBackPackMultiples
    }

    /**
     * Get SellAll instance.
     * */
    public static SellAllPrisonCommands get() {
        if (instance == null && isEnabled()) {
            instance = new SellAllPrisonCommands();
        }
        if (instance == null){
            return null;
        }
        return instance;
    }

    /**
     * Check if SellAll's enabled.
     * */
    public static boolean isEnabled(){
        return SpigotPrison.getInstance().getConfig().getString("sellall").equalsIgnoreCase("true");
    }

    /**
     * Use this to toggle the SellAllSign, essentially this will tell to the SellAll Sell command that you're using a sign
     * for SellAll.
     * */
    public void toggleSellAllSign(){
        if (!signUsed){
            signUsed = true;
        }
    }

    /**
     * Get the money to give to the Player depending on the multiplier.
     *
     * @param player Player
     * @param removeItems boolean
     *
     * @return money
     * */
    public double getMoneyWithMultiplier(Player player, boolean removeItems){

        SpigotPlayer sPlayer = new SpigotPlayer(player);

        // Get the Items config section
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);

        // Get money to give
        double moneyToGive = getNewMoneyToGive( sPlayer.getWrapper(), items, removeItems);
        boolean multiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        if (multiplierEnabled) {
            moneyToGive = moneyToGive * getMultiplier(sPlayer);;
        }

        return moneyToGive;
    }

    /**
     * Get the player multiplier, requires SpigotPlayer.
     *
     * @param sPlayer SpigotPlayer
     *
     * @return multiplier
     * */
    public double getMultiplier(SpigotPlayer sPlayer) {

        // Get Ranks module.
        ModuleManager modMan = Prison.get().getModuleManager();
        Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );

        // Get default multiplier
        String multiplierString = sellAllConfig.getString("Options.Multiplier_Default");
        double multiplier = 0;
        if (multiplierString != null) {
            multiplier = Double.parseDouble(multiplierString);
        }

        // Get multiplier depending on Player + Prestige. NOTE that prestige multiplier will replace
        // the actual default multiplier.
        multiplier = getMultiplierByRank(sPlayer, module, multiplier);

        // Get Multiplier from multipliers permission's if there's any.
        List<String> perms = sPlayer.getPermissions("prison.sellall.multiplier.");
        double multiplierExtraByPerms = 0;
        multiplierExtraByPerms = getMultiplierExtraByPerms(perms, multiplierExtraByPerms);
        multiplier += multiplierExtraByPerms;

        return multiplier;
    }

    @Command(identifier = "sellall set currency", description = "SellAll set currency command", onlyPlayers = false, permissions = "prison.sellall.currency")
    private void sellAllCurrency(CommandSender sender,
    @Arg(name = "currency", description = "Currency name.", def = "default") @Wildcard String currency){

        EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager().getEconomyForCurrency(currency);
        if (currencyEcon == null && !currency.equalsIgnoreCase("default")) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllCurrencyNotFound")), currency);
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.SellAll_Currency", currency);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        sellAllConfigUpdater();
        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllCurrencyEditedSuccess") + " [" + sellAllConfig.getString("Options.SellAll_Currency") + "]"));
    }

    @Command(identifier = "sellall", description = "SellAll main command", onlyPlayers = false)
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

    @Command(identifier = "sellall delay", description = "SellAll delay.", onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelay(CommandSender sender,
                              @Arg(name = "boolean", description = "True to enable or false to disable.", def = "null") String enable){

        if (!isEnabled()) return;

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.InvalidBooleanInput")));
            return;
        }

        boolean enableBoolean = getBoolean(enable);
        boolean sellDelayEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Delay_Enabled"));
        if (sellDelayEnabled == enableBoolean){
            if (enableBoolean){
                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllDelayAlreadyEnabled")));
            } else {
                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllDelayAlreadyDisabled")));
            }
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Enabled", enableBoolean);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        if (enableBoolean){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllDelayEnabled")));
        } else {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllDelayDisabled")));
        }
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall set delay", description = "Edit SellAll delay.", onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelaySet(CommandSender sender,
                              @Arg(name = "delay", description = "Set delay value in seconds.", def = "0") String delay){

        if (!isEnabled()) return;

        int delayValue;
        try {
            delayValue = Integer.parseInt(delay);
        } catch (NumberFormatException ex){
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllDelayNotNumber")));
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Seconds", delayValue);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllDelayEditedWithSuccess") + " [" + delayValue + "s]"));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall autosell", description = "Enable SellAll AutoSell.", onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSell(CommandSender sender,
                                 @Arg(name = "boolean", description = "True to enable or false to disable.", def = "null") String enable){

        if (!isEnabled()) return;

        if (enable.equalsIgnoreCase("perusertoggleable")){
            sellAllAutoSellPerUserToggleable(sender, enable);
            return;
        }

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.InvalidBooleanInput")));
            return;
        }

        boolean enableBoolean = getBoolean(enable);
        boolean fullInvAutoSellEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell"));
        if (fullInvAutoSellEnabled == enableBoolean){
            if (enableBoolean){
                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoSellAlreadyEnabled")));
            } else {
                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoSellAlreadyDisabled")));
            }
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell", enableBoolean);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        if (enableBoolean){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoSellEnabled")));
        } else {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoSellDisabled")));
        }
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall autosell perUserToggleable", description = "Enable AutoSell perUserToggleable", onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSellPerUserToggleable(CommandSender sender,
                                                  @Arg(name = "boolean", description = "True to enable or false to disable", def = "null") String enable){


        if (!isEnabled()) return;

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.InvalidBooleanInput")));
            return;
        }

        boolean enableBoolean = getBoolean(enable);
        boolean perUserToggleableEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable"));
        if (perUserToggleableEnabled == enableBoolean){
            if (enableBoolean){
                Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoPerUserToggleableAlreadyEnabled")));
            } else {
                Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoPerUserToggleableAlreadyDisabled")));
            }
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell_perUserToggleable", enableBoolean);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        if (enableBoolean){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoPerUserToggleableEnabled")));
        } else {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllAutoPerUserToggleableDisabled")));
        }

        sellAllConfigUpdater();
    }
    
    @Command(identifier = "sellall sell", description = "SellAll sell command", onlyPlayers = true)
    private void sellAllSellCommand(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);

        if (p == null){
            Output.get().sendInfo(sender, SpigotPrison.format("&cSorry but you can't use that from the console!"));
            return;
        }

        boolean sellPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Permission_Enabled"));
        if (sellPermissionEnabled){
            String permission = sellAllConfig.getString("Options.Sell_Permission");
            if (permission == null || !p.hasPermission(permission)){
                Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + " [" + permission + "]"));
                return;
            }
        }

        boolean sellAllSignEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_Sign_Enabled"));
        boolean sellAllBySignOnlyEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_By_Sign_Only"));
        String byPassPermission = sellAllConfig.getString("Options.SellAll_By_Sign_Bypass_Permission");
        if (sellAllSignEnabled && sellAllBySignOnlyEnabled && (byPassPermission != null && !p.hasPermission(byPassPermission))){
            if (!signUsed){
                Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllSignOnly")));
                return;
            }
        }

        if (signUsed) signUsed = false;

        boolean sellSoundEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Sound_Enabled"));
        Compatibility compat = SpigotPrison.getInstance().getCompatibility();
        if (!(sellAllConfig.getConfigurationSection("Items.") == null)) {

            if (sellAllCommandDelay(p)) return;

            // Get Spigot Player.
            SpigotPlayer sPlayer = new SpigotPlayer(p);

            // Get money to give + multiplier.
            double moneyToGive = getMoneyWithMultiplier(p, true);

            RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
            String currency = sellAllConfig.getString("Options.SellAll_Currency");
            if (currency != null && currency.equalsIgnoreCase("default")) currency = null;

            rankPlayer.addBalance(currency, moneyToGive);

            boolean sellNotifyEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Notify_Enabled"));
            if (moneyToGive < 0.001) {
                if (sellSoundEnabled){
                    Sound sound;
                    try {
                        sound = Sound.valueOf(sellAllConfig.getString("Options.Sell_Sound_Fail_Name"));
                    } catch (IllegalArgumentException ex){
                        sound = compat.getAnvilSound();
                    }
                    p.playSound(p.getLocation(), sound, 3, 1);
                }
                if (sellNotifyEnabled) {
                    Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllNothingToSell")));
                }
            } else {
                if (sellSoundEnabled){
                    Sound sound;
                    try {
                        sound = Sound.valueOf(sellAllConfig.getString("Options.Sell_Sound_Success_Name"));
                    } catch (IllegalArgumentException ex){
                        sound = compat.getLevelUpSound();
                    }
                    p.playSound(p.getLocation(), sound, 3, 1);
                }
                if (sellNotifyEnabled) {
                    DecimalFormat formatDecimal = new DecimalFormat("###,##0.00");
                    Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllYouGotMoney") + PlaceholdersUtil.formattedKmbtSISize(moneyToGive, formatDecimal, "")));
                }
            }
        } else {
            if (sellSoundEnabled){
                Sound sound;
                try {
                    sound = Sound.valueOf(sellAllConfig.getString("Options.Sell_Sound_Fail_Name"));
                } catch (IllegalArgumentException ex){
                    sound = compat.getAnvilSound();
                }
                p.playSound(p.getLocation(), sound, 3, 1);
            }
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllEmpty")));
        }
    }

    @Command(identifier = "sellall auto toggle", description = "Let the user enable or disable sellall auto", onlyPlayers = true)
    private void sellAllAutoEnableUser(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);
        sellAllConfigUpdater();

        // Sender must be a Player, not something else like the Console.
        if (p == null) {
            Output.get().sendError(sender, SpigotPrison.format(getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        boolean perUserToggleableEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable"));
        if (!perUserToggleableEnabled){
            return;
        }

        boolean perUserToggleablePermEnabled = getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable_Need_Perm"));
        String permission = sellAllConfig.getString("Options.Full_Inv_AutoSell_PerUserToggleable_Permission");
        if (perUserToggleablePermEnabled && (permission != null && !p.hasPermission(permission))){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMissingPermissionToToggleAutoSell") + " [" + permission + "]"));
            return;
        }

        // Get Player UUID.
        UUID playerUUID = p.getUniqueId();

        if (sellAllConfig.getString("Users." + playerUUID + ".isEnabled") != null){

            boolean isEnabled = getBoolean(sellAllConfig.getString("Users." + playerUUID + ".isEnabled"));
            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Users." + playerUUID + ".isEnabled", !isEnabled);
                conf.save(sellAllFile);
            } catch (IOException e) {
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }

            if (isEnabled){
                Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllAutoDisabled")));
            } else {
                Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllAutoEnabled")));
            }

        } else {

            // Enable it for the first time
            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Users." + playerUUID + ".isEnabled", true);
                conf.set("Users." + playerUUID + ".name", p.getName());
                conf.save(sellAllFile);
            } catch (IOException e) {
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }

            Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllAutoEnabled")));
        }

        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall gui", description = "SellAll GUI command", altPermissions = "prison.admin", onlyPlayers = true)
    private void sellAllGuiCommand(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);

        // Sender must be a Player, not something else like the Console.
        if (p == null) {
            Output.get().sendError(sender, SpigotPrison.format(getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        // If the Admin GUI's enabled will enter do this, if it isn't it'll try to open the Player GUI.
        boolean guiEnabled = getBoolean(sellAllConfig.getString("Options.GUI_Enabled"));
        if (guiEnabled){
            // Check if a permission's required, if it isn't it'll open immediately the GUI.
            boolean guiPermissionEnabled = getBoolean(sellAllConfig.getString("Options.GUI_Permission_Enabled"));
            if (guiPermissionEnabled){
                // Check if the sender have the required permission.
                String guiPermission = sellAllConfig.getString("Options.GUI_Permission");
               if (guiPermission != null && p.hasPermission(guiPermission)) {
                   SellAllAdminGUI gui = new SellAllAdminGUI(p);
                   gui.open();
                   return;
               // Try to open the Player GUI anyway.
               } else if (sellAllPlayerGUI(p)) return;
            // Open the Admin GUI because a permission isn't required.
            } else {
               SellAllAdminGUI gui = new SellAllAdminGUI(p);
               gui.open();
               return;
           }
        }

        // If the admin GUI's disabled, it'll try to use the Player GUI anyway.
        if (sellAllPlayerGUI(p)) return;

        // If the sender's an admin (OP or have the prison.admin permission) it'll send an error message.
        if (p.hasPermission("prison.admin")) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllGUIDisabled")));
        }
    }

    @Command(identifier = "sellall add", description = "SellAll add an item to the sellAll shop.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllAddCommand(CommandSender sender,
                                   @Arg(name = "Item_ID", description = "The Item_ID or block to add to the sellAll Shop.") String itemID,
                                   @Arg(name = "Value", description = "The value of the item.") Double value){

        if (!isEnabled()) return;

        if (itemID == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllPleaseAddItem")));
            return;
        }
        itemID = itemID.toUpperCase();

        if (value == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllAddPrice")));
            return;
        }

        if (sellAllConfig.getConfigurationSection("Items." + itemID) != null){
            Output.get().sendWarn(sender, SpigotPrison.format(itemID + messages.getString("Message.SellAllAlreadyAdded")));
            return;
        }

        try {
            XMaterial blockAdd;
            try {
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
                return;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + itemID + ".ITEM_ID", blockAdd.name());
                conf.set("Items." + itemID + ".ITEM_VALUE", value);
                if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                    conf.set("Items." + itemID + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + blockAdd.name());
                }
                conf.save(sellAllFile);
            } catch (IOException e) {
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }
        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format("&3 ITEM [" + itemID + ", " + value + messages.getString("Message.SellAllAddSuccess")));
        sellAllConfigUpdater();
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
    	
    	// If the block or item was already cnfigured, then skip this:
        if (sellAllConfig.getConfigurationSection("Items." + itemID) != null){
            return;
        }

        try {
        	File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
        	FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
        	conf.set("Items." + itemID + ".ITEM_ID", blockAdd.name());
        	conf.set("Items." + itemID + ".ITEM_VALUE", value);
        	if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                conf.set("Items." + itemID + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + blockAdd.name());
            }
        	conf.save(sellAllFile);
        } catch (IOException e) {
        	Output.get().logError( SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")), e);
        	return;
        }

        Output.get().logInfo(SpigotPrison.format("&3 ITEM [" + itemID + ", " + value + messages.getString("Message.SellAllAddSuccess")));
        sellAllConfigUpdater();
    }
    
    @Command(identifier = "sellall delete", description = "SellAll delete command, remove an item from shop.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllDeleteCommand(CommandSender sender, @Arg(name = "Item_ID", description = "The Item_ID you want to remove.") String itemID){

        if (!isEnabled()) return;

        if (itemID == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMissingID")));
            return;
        }

        if (sellAllConfig.getConfigurationSection("Items." + itemID) == null){
            Output.get().sendWarn(sender, SpigotPrison.format(itemID + messages.getString("Message.SellAllNotFoundStringConfig")));
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + itemID, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format(itemID + messages.getString("Message.SellAllDeletedSuccess")));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall edit", description = "SellAll edit command, edit an item of Shop.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllEditCommand(CommandSender sender,
                                    @Arg(name = "Item_ID", description = "The Item_ID or block to add to the sellAll Shop.") String itemID,
                                    @Arg(name = "Value", description = "The value of the item.") Double value){

        if (!isEnabled()) return;

        if (itemID == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllPleaseAddItem")));
            return;
        }
        itemID = itemID.toUpperCase();

        if (value == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllAddPrice")));
            return;
        }

        if (sellAllConfig.getConfigurationSection("Items." + itemID) == null){
            Output.get().sendWarn(sender, SpigotPrison.format(itemID + messages.getString("Message.SellAllNotFoundEdit")));
            return;
        }

        try {
            XMaterial blockAdd;
            try{
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
                return;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + itemID + ".ITEM_ID", blockAdd.name());
                conf.set("Items." + itemID + ".ITEM_VALUE", value);
                if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                    conf.set("Items." + itemID + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + blockAdd.name());
                }
                conf.save(sellAllFile);
            } catch (IOException e) {
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }
        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format("&3ITEM [" + itemID + ", " + value + messages.getString("Message.SellAllCommandEditSuccess")));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall multiplier", description = "SellAll multiplier command list", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllMultiplierCommand(CommandSender sender){

        if (!isEnabled()) return;

        boolean multiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        if (!multiplierEnabled){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMultipliersAreDisabled")));
            return;
        }

        String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall multiplier help" );
        sender.dispatchCommand(registeredCmd);
    }

    @Command(identifier = "sellall multiplier add", description = "SellAll add a multiplier. Permission multipliers for player's prison.sellall.multiplier.<valueHere>, example prison.sellall.multiplier.2 will add a 2x multiplier",
            permissions = "prison.admin", onlyPlayers = false)
    private void sellAllAddMultiplierCommand(CommandSender sender,
                                             @Arg(name = "Prestige", description = "Prestige to hook to the multiplier.") String prestige,
                                             @Arg(name = "multiplier", description = "Multiplier value.") Double multiplier){

        if (!isEnabled()) return;

        boolean multiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        if (!multiplierEnabled){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMultipliersAreDisabled")));
            return;
        }

        if (addMultiplierConditions(sender, prestige, multiplier)) return;

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestige + ".PRESTIGE_NAME", prestige);
            conf.set("Multiplier." + prestige + ".MULTIPLIER", multiplier);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllMultiplierEditSaveSuccess")));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall multiplier delete", description = "SellAll delete a multiplier.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllDeleteMultiplierCommand(CommandSender sender,
                                                @Arg(name = "Prestige", description = "Prestige hooked to the multiplier.") String prestige){

        if (!isEnabled()) return;

        boolean multiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        if (!multiplierEnabled){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMultipliersAreDisabled")));
            return;
        }

        String permission = sellAllConfig.getString("Options.Multiplier_Command_Permission");
        if (permission != null && !sender.hasPermission(permission)){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + " [" + permission + "]"));
            return;
        }

        if (prestige == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMultiplierFormat")));
            return;
        }

        if (sellAllConfig.getConfigurationSection("Multiplier." + prestige) == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllCantFindMultiplier") + prestige + messages.getString("Message.SellAllCantFindMultiplier2")));
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestige, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllMultiplierDeleteSuccess")));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall Trigger", description = "Toggle SellAll Shift+Right Click on a tool to trigger the /sellall sell command, true -> Enabled or False -> Disabled.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllToolsTriggerToggle(CommandSender sender,
                                           @Arg(name = "Boolean", description = "Enable or disable", def = "null") String enable){

        if (!isEnabled()) return;

        if (enable.equalsIgnoreCase("null")){
        	String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall toolsTrigger help" );
            sender.dispatchCommand(registeredCmd);
            return;
        }

        if (!enable.equalsIgnoreCase("true") && !enable.equalsIgnoreCase("false")){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.InvalidBooleanInput")));
            return;
        }

        boolean sellAllTriggerStatus = getBoolean(sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Enabled"));
        boolean enableInput = getBoolean(enable);
        if (sellAllTriggerStatus == enableInput) {
            if (sellAllTriggerStatus) {

                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerAlreadyEnabled")));
            } else {

                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerAlreadyDisabled")));
            }
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.ShiftAndRightClickSellAll.Enabled", enableInput);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            return;
        }

        if (enableInput){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerEnabled")));
        } else {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerDisabled")));
        }

        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall Trigger add", description = "Add an Item to trigger the Shift+Right Click -> /sellall sell command.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllTriggerAdd(CommandSender sender,
                                   @Arg(name = "Item", description = "Item name") String itemID){

        if (!isEnabled()) return;

        boolean sellAllTriggerStatus = getBoolean(sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Enabled"));
        if (!sellAllTriggerStatus){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerIsDisabled")));
            return;
        }

        if (itemID == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerMissingItem")));
            return;
        }
        itemID = itemID.toUpperCase();

        try {
            XMaterial blockAdd;
            try{
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
                return;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID", blockAdd.name());
                conf.save(sellAllFile);
            } catch (IOException e) {
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }
        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerItemAddSuccess") + " [" + itemID + " ]"));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall Trigger edit", description = "Edit an Item of the Shift+Right Click trigger -> /sellall sell command.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllTriggerEdit(CommandSender sender,
                                   @Arg(name = "Item", description = "Item name") String itemID){

        if (!isEnabled()) return;

        boolean sellAllTriggerStatus = getBoolean(sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Enabled"));
        if (!sellAllTriggerStatus){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerIsDisabled")));
            return;
        }

        if (itemID == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerMissingItem")));
            return;
        }
        itemID = itemID.toUpperCase();

        if (sellAllConfig.getString("ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID") == null){

            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerMissingItem")));
            return;
        }

        try {
            XMaterial blockAdd;
            try{
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
                return;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID", blockAdd.name());
                conf.save(sellAllFile);
            } catch (IOException e) {
                Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }
        } catch (IllegalArgumentException ex){
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerItemEditSuccess") + " [" + itemID + " ]"));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall Trigger delete", description = "Delete an Item from the Shift+Right Click trigger -> /sellall sell command.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllTriggerDelete(CommandSender sender,
                                    @Arg(name = "Item", description = "Item name") String itemID){

        if (!isEnabled()) return;

        boolean sellAllTriggerStatus = getBoolean(sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Enabled"));
        if (!sellAllTriggerStatus){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerIsDisabled")));
            return;
        }

        if (itemID == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerMissingItem")));
            return;
        }
        itemID = itemID.toUpperCase();

        if (sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID") == null){

            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerMissingItem")));
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID", null);
            conf.set("ShiftAndRightClickSellAll.Items." + itemID, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllTriggerItemDeleteSuccess") + " [" + itemID + " ]"));
        sellAllConfigUpdater();
    }

    @Command(identifier = "sellall setdefault", description = "SellAll default values ready to go.", permissions = "prison.admin", onlyPlayers = false)
    private void sellAllSetDefaultCommand(CommandSender sender){

        if (!isEnabled()) return;
        
		// Setup all the prices in sellall:
        SpigotPlatform platform = (SpigotPlatform) Prison.get().getPlatform();
		for ( SellAllBlockData xMatCost : platform.buildBlockListXMaterial() ) {
			
			// Add blocks to sellall:
			sellAllAddCommand( xMatCost.getBlock(), xMatCost.getPrice() );
		}

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.SellAllDefaultSuccess")));
    }

    private double getNewMoneyToGive(Player p, Set<String> items, boolean removeItems){

        // Money to give value
        double moneyToGive = 0;

        // Get the player inventory
        Inventory inv = p.getInventory();

        // Get the items from the player inventory and for each of them check the conditions.
        mode = inventorySellMode.PlayerInventory;
        for (ItemStack itemStack : inv.getContents()){
            moneyToGive += getNewMoneyToGiveManager(p, items, itemStack, removeItems);
        }

        // Check option and if enabled.
        if (IntegrationMinepacksPlugin.getInstance().isEnabled() &&
        			getBoolean(sellAllConfig.getString("Options.Sell_MinesBackPacks_Plugin_Backpack"))) {

            // Set mode and get backpack
            mode = inventorySellMode.MinesBackPack;
            Backpack backPack = IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p);

            if (backPack != null) {
                for (ItemStack itemStack : backPack.getInventory().getContents()) {
                    if (itemStack != null) {
                        moneyToGive += getNewMoneyToGiveManager(p, items, itemStack, removeItems);
                    }
                }
            }
        }

        // Check if enabled Prison backpacks and sellall on it.
        if (getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks")) &&
                getBoolean(sellAllConfig.getString("Options.Sell_Prison_BackPack_Items"))) {

            if (BackpacksUtil.get().isMultipleBackpacksEnabled()) {

                if (!BackpacksUtil.get().getBackpacksIDs(p).isEmpty()) {
                    for (String id : BackpacksUtil.get().getBackpacksIDs(p)) {
                        // If the backpack's the default one with a null ID then use this, if not get something else.
                        if (id == null){

                            Inventory backPack = BackpacksUtil.get().getBackpack(p);
                            mode = inventorySellMode.PrisonBackPackSingle;

                            if (backPack != null) {
                                for (ItemStack itemStack : backPack.getContents()) {
                                    if (itemStack != null) {
                                        moneyToGive += getNewMoneyToGiveManager(p, items, itemStack, removeItems);
                                    }
                                }
                            }

                        } else {

                            Inventory backPack = BackpacksUtil.get().getBackpack(p, id);
                            mode = inventorySellMode.PrisonBackPackMultiples;
                            idBeingProcessedBackpack = id;

                            if (backPack != null) {
                                for (ItemStack itemStack : backPack.getContents()) {
                                    if (itemStack != null) {
                                        moneyToGive += getNewMoneyToGiveManager(p, items, itemStack, removeItems);
                                    }
                                }
                            }

                        }
                    }
                }

            } else {
                // Set mode and get Prison Backpack inventory
                mode = inventorySellMode.PrisonBackPackSingle;
                Inventory backPack = BackpacksUtil.get().getBackpack(p);

                if (backPack != null) {
                    for (ItemStack itemStack : backPack.getContents()) {
                        if (itemStack != null) {
                            moneyToGive += getNewMoneyToGiveManager(p, items, itemStack, removeItems);
                        }
                    }
                }
            }
        }

        return moneyToGive;
    }

    private double getNewMoneyToGiveManager(Player p, Set<String> items, ItemStack itemStack, boolean removeItems) {

        double moneyToGive = 0;

        if (itemStack != null) {
            // Get the items strings from config and for each of them get the Material and value.
            for (String key : items) {

                // ItemID
                String itemID = sellAllConfig.getString("Items." + key + ".ITEM_ID");

                // Flag variable and XMaterials.
                boolean hasError = false;
                XMaterial itemMaterial = null;
                XMaterial invMaterial = null;
                try {
                    itemMaterial = XMaterial.valueOf(itemID);
                    invMaterial = XMaterial.matchXMaterial(itemStack);
                } catch (IllegalArgumentException ex){
                    hasError = true;
                }

                // Get value
                double value = 0;
                try {
                    String valueString = sellAllConfig.getString("Items." + key + ".ITEM_VALUE");
                    if (valueString != null) {
                        value = Double.parseDouble(valueString);
                    } else {
                        hasError = true;
                    }
                } catch (NumberFormatException ex){
                    if (!hasError) hasError = true;
                }

                // Get amount and remove items if enabled
                int amount = 0;
                // Check if the item from the player inventory's on the config of items sellable
                // So it gets the amount and then remove it from the inventory
                if (!hasError && itemMaterial == invMaterial) {

                    // Check if per-block permission's enabled and if player has permission.
                    if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))){
                        String permission = sellAllConfig.getString("Options.Sell_Per_Block_Permission");
                        permission = permission + invMaterial.name();

                        // Check if player have this permission, if not return 0 money earned for this item and don't remove it.
                        if (!p.hasPermission(permission)){
                            return 0;
                        }
                    }

                    amount = itemStack.getAmount();
                    if (removeItems) {
                        if (mode == inventorySellMode.PlayerInventory) {
                            p.getInventory().remove(itemStack);
                        } else if (IntegrationMinepacksPlugin.getInstance().isEnabled() && mode == inventorySellMode.MinesBackPack){
                            IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p).getInventory().remove(itemStack);
                        } else if (mode == inventorySellMode.PrisonBackPackSingle){
                            BackpacksUtil.get().removeItem(p, itemStack);
                        } else if (mode == inventorySellMode.PrisonBackPackMultiples){
                            if (idBeingProcessedBackpack != null){
                                BackpacksUtil.get().removeItem(p, itemStack, idBeingProcessedBackpack);
                            }
                        }
                    }
                }
                // Get the new amount of money to give
                if (amount != 0) {
                    moneyToGive += value * amount;
                }
            }
        }
        return moneyToGive;
    }

    /**
     * Open SellAll GUI for Players if enabled.
     *
     * @param p Player
     *
     * @return boolean
     * */
    private boolean sellAllPlayerGUI(Player p) {

        // Check if the Player GUI's enabled.
        boolean playerGUIEnabled = getBoolean(sellAllConfig.getString("Options.Player_GUI_Enabled"));
        if (playerGUIEnabled){
            // Check if a permission's required, if not it'll open directly the Player's GUI.
            boolean playerGUIPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Player_GUI_Permission_Enabled"));
            if (playerGUIPermissionEnabled){
                // Check if the sender has the required permission.
                String permission = sellAllConfig.getString("Options.Player_GUI_Permission");
                if (permission != null && p.hasPermission(permission)){
                    SellAllPlayerGUI gui = new SellAllPlayerGUI(p);
                    gui.open();
                    // If missing will send a missing permission error message.
                } else {
                    Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + " [" + permission + "]"));
                }
                // Because a permission isn't required, it'll open directly the GUI.
            } else {
                SellAllPlayerGUI gui = new SellAllPlayerGUI(p);
                gui.open();
            }
            return true;
        }
        return false;
    }

    /**
     * Check if a player's waiting for his delay to end.
     * Check if SellAllDelay's enabled.
     *
     * @param p Player
     *
     * @return boolean
     * */
    private boolean sellAllCommandDelay(Player p) {

        boolean sellDelayEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Delay_Enabled"));
        if (sellDelayEnabled) {

            if (activePlayerDelay.contains(p.getName())){
                Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllWaitDelay")));
                return true;
            }

            addPlayerToDelay(p);

            String delayInSeconds = sellAllConfig.getString("Options.Sell_Delay_Seconds");
            if (delayInSeconds == null){
                delayInSeconds = "1";
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> removePlayerFromDelay(p), 20L * Integer.parseInt(delayInSeconds));
        }

        return false;
    }

    private boolean addMultiplierConditions(CommandSender sender, String prestige, Double multiplier) {

        boolean multiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        if (!multiplierEnabled){

            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMultipliersAreDisabled")));
            return true;
        }

        if (prestige == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMultiplierWrongFormat")));
            return true;
        }
        if (multiplier == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllMultiplierWrongFormat")));
            return true;
        }

        PrisonRanks rankPlugin = (PrisonRanks) (Prison.get().getModuleManager() == null ? null : Prison.get().getModuleManager().getModule(PrisonRanks.MODULE_NAME).orElse(null));
        if (rankPlugin == null) {
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllRanksDisabled")));
            return true;
        }

        boolean isPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges") != null;
        if (!isPrestigeLadder) {
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllPrestigeLadderNotFound")));
            return true;
        }

        boolean isARank = rankPlugin.getRankManager().getRank(prestige) != null;
        if (!isARank) {
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllCantFindPrestigeOrRank") + prestige));
            return true;
        }

        boolean isInPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges").containsRank(rankPlugin.getRankManager().getRank(prestige).getId());
        if (!isInPrestigeLadder) {
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.SellAllRankNotFoundInPrestigeLadder") + prestige));
            return true;
        }
        return false;
    }

    private double getMultiplierByRank(SpigotPlayer sPlayer, Module module, double multiplier) {
        if (module != null) {
            PrisonRanks rankPlugin = (PrisonRanks) module;
            if (rankPlugin.getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName()) != null) {
                String playerRankName;
                try {
                    playerRankName = rankPlugin.getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName()).getRank("prestiges").getName();
                } catch (NullPointerException ex) {
                    playerRankName = null;
                }
                if (playerRankName != null) {
                    String multiplierRankString = sellAllConfig.getString("Multiplier." + playerRankName + ".MULTIPLIER");
                    if (multiplierRankString != null) {
                        try {
                            multiplier = Double.parseDouble(multiplierRankString);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        }
        return multiplier;
    }

    private double getMultiplierExtraByPerms(List<String> perms, double multiplierExtraByPerms) {
        for (String multByPerm : perms){
            double multByPermDouble = Double.parseDouble(multByPerm.substring(26));
            boolean multiplierPermissionHighOption = getBoolean(sellAllConfig.getString("Options.Multiplier_Permission_Only_Higher"));
            if (!multiplierPermissionHighOption) {
                multiplierExtraByPerms += multByPermDouble;
            } else if (multByPermDouble > multiplierExtraByPerms){
                multiplierExtraByPerms = multByPermDouble;
            }
        }
        return multiplierExtraByPerms;
    }

    /**
     * Get sellAllConfig updated.
     * */
    private void sellAllConfigUpdater(){
        sellAllConfig = SpigotPrison.getInstance().getSellAllConfig();
    }

    /**
     * Add a Player to delay.
     *
     * @param p Player
     * */
    private void addPlayerToDelay(Player p){

        if (!isEnabled()) return;

        if (!activePlayerDelay.contains(p.getName())){
            activePlayerDelay.add(p.getName());
        }
    }

    /**
     * Removes a Player from delay.
     *
     * @param p Player
     * */
    private void removePlayerFromDelay(Player p){

        if (!isEnabled()) return;

        activePlayerDelay.remove(p.getName());
    }


}

package tech.mcprison.prison.spigot.commands.sellall;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.commands.PrisonSpigotBaseCommands;
import tech.mcprison.prison.spigot.configs.SellAllConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllPlayerGUI;

public class SellAllPrisonCommands extends PrisonSpigotBaseCommands {

    private Configuration sellAllConfig = SpigotPrison.getInstance().getSellAllConfig();
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
    private File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
    private FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
    private static SellAllPrisonCommands instance;
    private double moneyToGive;
    public static List<String> activePlayerDelay = new ArrayList<>();

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

    public static boolean isEnabled(){
        return SpigotPrison.getInstance().getConfig().getString("sellall").equalsIgnoreCase("true");
    }

    private void addPlayerToDelay(Player p){

        if (!isEnabled()) return;

        if (!activePlayerDelay.contains(p.getName())){
            activePlayerDelay.add(p.getName());
        }
    }

    private void removePlayerFromDelay(Player p){

        if (!isEnabled()) return;

        activePlayerDelay.remove(p.getName());
    }

    @Command(identifier = "sellall", description = "SellAll main command", onlyPlayers = false)
    private void sellAllCommands(CommandSender sender) {

        if (!isEnabled()) return;

        if (sender.hasPermission("prison.admin")) {
            sender.dispatchCommand("sellall help");
        } else {
            sender.dispatchCommand("sellall sell");
        }
    }

    @Command(identifier = "sellall delay", description = "SellAll delay.", onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelay(CommandSender sender,
                              @Arg(name = "boolean", description = "True to enable or false to disable.", def = "null") String enable){

        if (!isEnabled()) return;

        if (!sender.hasPermission("prison.sellall.delay")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.MissingPermission")));
            return;
        }

        SellAllConfig sellAllConfigClass = new SellAllConfig();
        sellAllConfigClass.initialize();
        sellAllConfig = sellAllConfigClass.getFileSellAllConfig();

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.InvalidBooleanInput")));
            return;
        }

        if (sellAllConfig.getString("Options.Sell_Delay_Enabled").equalsIgnoreCase(enable)){
            if (enable.equalsIgnoreCase("true")){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllDelayAlreadyEnabled")));
            } else {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllDelayAlreadyDisabled")));
            }
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Enabled", enable);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
        }

        if (enable.equalsIgnoreCase("true")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllDelayEnabled")));
        } else if (enable.equalsIgnoreCase("false")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllDelayDisabled")));
        }
    }

    @Command(identifier = "sellall delay set", description = "Edit SellAll delay.", onlyPlayers = false, permissions = "prison.sellall.delay")
    private void sellAllDelaySet(CommandSender sender,
                              @Arg(name = "delay", description = "Set delay value in seconds.", def = "null") String delay){

        if (!isEnabled()) return;

        if (!sender.hasPermission("prison.sellall.delay")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.MissingPermission")));
            return;
        }

        SellAllConfig sellAllConfigClass = new SellAllConfig();
        sellAllConfigClass.initialize();
        sellAllConfig = sellAllConfigClass.getFileSellAllConfig();

        int delayValue;

        try {
            delayValue = Integer.parseInt(delay);
        } catch (NumberFormatException ex){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllDelayNotNumber")));
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Seconds", delayValue);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllDelayEditedWithSuccess") + " [" + delayValue + "s]"));
    }

    @Command(identifier = "sellall autosell", description = "Enable SellAll AutoSell.", onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSell(CommandSender sender,
                                 @Arg(name = "boolean", description = "True to enable or false to disable.", def = "null") String enable){

        if (!isEnabled()) return;

        if (!sender.hasPermission("prison.autosell.edit")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellMissingPermission")) + " [prison.autosell.edit]");
            return;
        }

        if (enable.equalsIgnoreCase("perusertoggleable")){
            sellAllAutoSellPerUserToggleable(sender, enable);
            return;
        }

        SellAllConfig sellAllConfigClass = new SellAllConfig();
        sellAllConfigClass.initialize();
        sellAllConfig = sellAllConfigClass.getFileSellAllConfig();

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.InvalidBooleanInput")));
            return;
        }

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase(enable)){
            if (enable.equalsIgnoreCase("true")){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellAlreadyEnabled")));
            } else if (enable.equalsIgnoreCase("false")){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellAlreadyDisabled")));
            }
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell", enable);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        if (enable.equalsIgnoreCase("true")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellEnabled")));
        } else if (enable.equalsIgnoreCase("false")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellDisabled")));
        }
    }

    @Command(identifier = "sellall autosell perUserToggleable", description = "Enable AutoSell perUserToggleable", onlyPlayers = false, permissions = "prison.autosell.edit")
    private void sellAllAutoSellPerUserToggleable(CommandSender sender,
                                                  @Arg(name = "boolean", description = "True to enable or false to disable", def = "null") String enable){

        if (!sender.hasPermission("prison.autosell.edit")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellMissingPermission")) + " [prison.autosell.edit]");
            return;
        }

        if (!isEnabled()) return;

        SellAllConfig sellAllConfigClass = new SellAllConfig();
        sellAllConfigClass.initialize();
        sellAllConfig = sellAllConfigClass.getFileSellAllConfig();

        if (!(enable.equalsIgnoreCase("true") || enable.equalsIgnoreCase("false"))){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.InvalidBooleanInput")));
            return;
        }

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable").equalsIgnoreCase(enable)){
            if (enable.equalsIgnoreCase("true")){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoPerUserToggleableAlreadyEnabled")));
            } else if (enable.equalsIgnoreCase("false")){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoPerUserToggleableAlreadyDisabled")));
            }
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell_perUserToggleable", enable);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        if (enable.equalsIgnoreCase("true")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellEnabled")));
        } else if (enable.equalsIgnoreCase("false")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoSellDisabled")));
        }
    }

    @Command(identifier = "sellall sell", description = "SellAll sell command", onlyPlayers = true)
    private void sellAllSellCommand(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);
        SellAllConfig sellAllConfigClass = new SellAllConfig();
        sellAllConfigClass.initialize();
        sellAllConfig = sellAllConfigClass.getFileSellAllConfig();

        if (p == null){
            sender.sendMessage(SpigotPrison.format("&7[&3PRISON&7] &cSorry but you can't use that from the console!"));
            return;
        }
        
        if (sellAllConfig.getString("Options.Sell_Permission_Enabled").equalsIgnoreCase("true")){
            if (!p.hasPermission("Options.Sell_Permission")){
                p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Sell_Permission") + "]"));
            }
        } else if (!(sellAllConfig.getConfigurationSection("Items.") == null)){

            if (sellAllConfig.getString("Options.Sell_Delay_Enabled").equalsIgnoreCase("true")) {

                if (activePlayerDelay.contains(p.getName())){
                    p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllWaitDelay")));
                    return;
                }

                addPlayerToDelay(p);
                Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> removePlayerFromDelay(p), 20L * Integer.parseInt(sellAllConfig.getString("Options.Sell_Delay_Seconds")));
            }

            // Get Spigot Player
            SpigotPlayer sPlayer = new SpigotPlayer(p);

            // Get money to give + multiplier
            moneyToGive = getMoneyWithMultiplier(sPlayer);

            // Get economy and add balance
            EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();
            economy.addBalance(sPlayer, moneyToGive);

            if (moneyToGive<0.001){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllNothingToSell")));
            } else {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllYouGotMoney") + moneyToGive));
            }
        } else {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllEmpty")));
        }
    }

    /**
     * Get the money to give to the Player depending on the multiplier.
     * */
    private double getMoneyWithMultiplier(SpigotPlayer sPlayer) {

        // Get the Items config section
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);
        moneyToGive = 0;

        // Get money to give
        moneyToGive = getMoneyToGiveRemoveItems( sPlayer.getWrapper(), items, moneyToGive);

        if (sellAllConfig.getString("Options.Multiplier_Enabled").equalsIgnoreCase("true")) {

            moneyToGive = moneyToGive * getMultiplier(sPlayer);;
        }

        return moneyToGive;
    }

    /**
     * Get the money to give to the Player depending on the multiplier.
     * */
    public double getMoneyWithMultiplier(Player player){

        SpigotPlayer sPlayer = new SpigotPlayer(player);

        // Get the Items config section
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);
        moneyToGive = 0;

        // Get money to give
        moneyToGive = getMoneyToGive( sPlayer.getWrapper(), items, moneyToGive);

        if (sellAllConfig.getString("Options.Multiplier_Enabled").equalsIgnoreCase("true")) {

            moneyToGive = moneyToGive * getMultiplier(sPlayer);;
        }

        return moneyToGive;
    }

    /**
     * Get player multiplier, this uses the normal Player and actually return a double.
     * */
    public double getMultiplier(Player player){

        // Get Spigot Player
        SpigotPlayer sPlayer = new SpigotPlayer(player);

        return getMultiplier(sPlayer);
    }

    /**
     * Get the player multiplier, requires SpigotPlayer.
     * */
    public double getMultiplier(SpigotPlayer sPlayer) {

        // Get Ranks module.
        ModuleManager modMan = Prison.get().getModuleManager();
        Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );

        // Get default multiplier
        double multiplier = Double.parseDouble(sellAllConfig.getString("Options.Multiplier_Default"));

        // Get multiplier depending on Player + Prestige. NOTE that prestige multiplier will replace
        // the actual default multiplier.
        if (module != null) {
        	PrisonRanks rankPlugin = (PrisonRanks) module;
        	
            if (rankPlugin.getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName()).isPresent()) {
                String playerRankName;
                try {
                    playerRankName = rankPlugin.getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName()).get().getRank("prestiges").getName();
                } catch (NullPointerException ex){
                    playerRankName = null;
                }
                if ((playerRankName != null) && (sellAllConfig.getString("Multiplier." + playerRankName + ".MULTIPLIER") != null)) {
                    multiplier = Double.parseDouble(sellAllConfig.getString("Multiplier." + playerRankName + ".MULTIPLIER"));
                }
            }
        }
        List<String> perms = sPlayer.getPermissions("prison.sellall.multiplier.");
        double multiplierExtraByPerms = 0;
        for (String multByPerm : perms){
            double multByPermDouble = Double.parseDouble(multByPerm.substring(26));
            if (!sellAllConfig.getString("Options.Multiplier_Permission_Only_Higher").equalsIgnoreCase("true")) {
                multiplierExtraByPerms += multByPermDouble;
            } else if (sellAllConfig.getString("Options.Multiplier_Permission_Only_Higher").equalsIgnoreCase("true") && multByPermDouble > multiplierExtraByPerms){
                multiplierExtraByPerms = multByPermDouble;
            }
        }
        multiplier += multiplierExtraByPerms;
        
        return multiplier;
    }

    /**
     * Get value of the items in Player's Inventory if SellAll's triggered now.
     * */
    public double getMoneyToGive(Player p, Set<String> items, double moneyToGive) {

        // Get the player inventory
        Inventory inv = p.getInventory();

        // Get the items from the player inventory and for each of them check the conditions.
        for (ItemStack itemStack : inv.getContents()){

            if (itemStack != null) {
                // Get the items strings from config and for each of them get the Material and value.
                for (String key : items) {
                    ItemStack itemMaterial = XMaterial.valueOf(sellAllConfig.getString("Items." + key + ".ITEM_ID")).parseItem();
                    double value = Double.parseDouble(sellAllConfig.getString("Items." + key + ".ITEM_VALUE"));
                    int amount = 0;

                    // Check if the item from the player inventory's on the config of items sellable
                    // So it gets the amount and then remove it from the inventory
                    if (itemMaterial != null && itemMaterial == itemStack) {
                        amount = itemStack.getAmount();
                    }
                    // Get the new amount of money to give
                    if (amount != 0) {
                        moneyToGive = moneyToGive + (value * amount);
                    }
                }
            }
        }
        return moneyToGive;
    }

    public double getMoneyToGiveRemoveItems(Player p, Set<String> items, double moneyToGive){
        // Get the player inventory
        Inventory inv = p.getInventory();

        // Get the items from the player inventory and for each of them check the conditions.
        for (ItemStack itemStack : inv.getContents()){

            if (itemStack != null) {
                // Get the items strings from config and for each of them get the Material and value.
                for (String key : items) {
                    ItemStack itemMaterial = XMaterial.valueOf(sellAllConfig.getString("Items." + key + ".ITEM_ID")).parseItem();
                    double value = Double.parseDouble(sellAllConfig.getString("Items." + key + ".ITEM_VALUE"));
                    int amount = 0;

                    // Check if the item from the player inventory's on the config of items sellable
                    // So it gets the amount and then remove it from the inventory
                    if (itemMaterial != null && itemMaterial == itemStack) {
                        amount = itemStack.getAmount();
                        p.getInventory().remove(itemStack);
                    }
                    // Get the new amount of money to give
                    if (amount != 0) {
                        moneyToGive = moneyToGive + (value * amount);
                    }
                }
            }
        }
        return moneyToGive;
    }



    @Command(identifier = "sellall auto toggle", description = "Let the user enable or disable sellall auto", onlyPlayers = true)
    private void sellAllAutoEnableUser(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);
        SellAllConfig sellAllConfigClass = new SellAllConfig();
        sellAllConfigClass.initialize();
        sellAllConfig = sellAllConfigClass.getFileSellAllConfig();

        if (!sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable").equalsIgnoreCase("true")){
            return;
        }

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable_Need_Perm").equalsIgnoreCase("true") && !p.hasPermission(sellAllConfig.getString("Options.Full_Inv_AutoSell_PerUserToggleable_Permission"))){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermissionToToggleAutoSell") + " [" + sellAllConfig.getString("Options.Full_Inv_AutoSell_PerUserToggleable_Permission") + "]"));
            return;
        }

        UUID playerUUID = p.getUniqueId();

        if (sellAllConfig.getString("Users." + playerUUID + ".isEnabled") != null){
            if (sellAllConfig.getString("Users." + playerUUID + ".isEnabled").equalsIgnoreCase("true")){
                // Disable it

                try {
                    sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                    conf = YamlConfiguration.loadConfiguration(sellAllFile);
                    conf.set("Users." + playerUUID + ".isEnabled", false);
                    conf.save(sellAllFile);
                } catch (IOException e) {
                    sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                    e.printStackTrace();
                    return;
                }

                p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoDisabled")));
            } else if (sellAllConfig.getString("Users." + playerUUID + ".isEnabled").equalsIgnoreCase("false")){
                // Enable it

                try {
                    sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                    conf = YamlConfiguration.loadConfiguration(sellAllFile);
                    conf.set("Users." + playerUUID + ".isEnabled", "true");
                    conf.save(sellAllFile);
                } catch (IOException e) {
                    sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                    e.printStackTrace();
                    return;
                }

                p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoEnabled")));
            }
        } else {
            // Enable it for the first time

            try {
                sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Users." + playerUUID + ".isEnabled", "true");
                conf.set("Users." + playerUUID + ".name", p.getName());
                conf.save(sellAllFile);
            } catch (IOException e) {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }

            p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoEnabled")));
        }
    }

    @Command(identifier = "sellall gui", description = "SellAll GUI command", onlyPlayers = true)
    private void sellAllGuiCommand(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            sender.sendMessage(getMessages().getString("Message.CantRunGUIFromConsole"));
            return;
        }

        // If the Admin GUI's enabled will enter do this, if it isn't it'll try to open the Player GUI.
        if (sellAllConfig.getString("Options.GUI_Enabled").equalsIgnoreCase("true")){
            // Check if a permission's required, if it isn't it'll open immediately the GUI.
            if (sellAllConfig.getString("Options.GUI_Permission_Enabled").equalsIgnoreCase("true")){
                // Check if the sender have the required permission.
               if (p.hasPermission(sellAllConfig.getString("Options.GUI_Permission"))) {
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
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllGUIDisabled")));
        }
    }

    private boolean sellAllPlayerGUI(Player p) {
        // Check if the Player GUI's enabled.
        if (sellAllConfig.getString("Options.Player_GUI_Enabled").equalsIgnoreCase("true")){
            // Check if a permission's required, if not it'll open directly the Player's GUI.
            if (sellAllConfig.getString("Options.Player_GUI_Permission_Enabled").equalsIgnoreCase("true")){
                // Check if the sender has the required permission.
                if (p.hasPermission("Options.Player_GUI_Permission")){
                    SellAllPlayerGUI gui = new SellAllPlayerGUI(p);
                    gui.open();
                // If missing will send a missing permission error message.
                } else {
                    p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Player_GUI_Permission") + "]"));
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

    @Command(identifier = "sellall add", description = "SellAll add an item to the sellAll shop.", onlyPlayers = false)
    private void sellAllAddCommand(CommandSender sender,
                                   @Arg(name = "Item_ID", description = "The Item_ID or block to add to the sellAll Shop.") String itemID,
                                   @Arg(name = "Value", description = "The value of the item.") Double value){

        if (!isEnabled()) return;
        if (sellAllConfig.getString("Options.Add_Permission_Enabled").equalsIgnoreCase("true")) {
            if (!sender.hasPermission("Options.Add_Permission")) {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Add_Permission") + "]"));
                return;
            }
        }
        if (itemID == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllPleaseAddItem")));
            return;
        }
        itemID = itemID.toUpperCase();

        if (value == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAddPrice")));
            return;
        }

        try {
            XMaterial blockAdd;
            try {
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
                return;
            }

            try {
                sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + itemID + ".ITEM_ID", blockAdd.name());
                conf.set("Items." + itemID + ".ITEM_VALUE", value);
                conf.save(sellAllFile);
            } catch (IOException e) {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }
        } catch (IllegalArgumentException ex){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }

        sender.sendMessage(SpigotPrison.format("&3[PRISON]&a ITEM [" + itemID + ", " + value + messages.getString("Message.SellAllAddSuccess")));
    }

    @Command(identifier = "sellall delete", description = "SellAll delete command, remove an item from shop.", onlyPlayers = false)
    private void sellAllDeleteCommand(CommandSender sender, @Arg(name = "Item_ID", description = "The Item_ID you want to remove.") String itemID){

        if (!isEnabled()) return;

        if (sellAllConfig.getString("Options.Delete_Permission_Enabled").equalsIgnoreCase("true")) {
            if (!sender.hasPermission("Options.Delete_Permission")) {
                return;
            }
        }
        if (itemID == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingID")));
        }
        if (sellAllConfig.getConfigurationSection("Items." + itemID) == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllTagWarn") + itemID + messages.getString("Message.SellAllNotFoundStringConfig")));
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + itemID + ".ITEM_ID", null);
            conf.set("Items." + itemID + ".ITEM_VALUE", null);
            conf.set("Items." + itemID, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return;
        }

        sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllPrisonTag") + itemID + messages.getString("Message.SellAllDeletedSuccess")));
    }

    @Command(identifier = "sellall edit", description = "SellAll edit command, edit an item of Shop.", onlyPlayers = false)
    private void sellAllEditCommand(CommandSender sender,
                                    @Arg(name = "Item_ID", description = "The Item_ID or block to add to the sellAll Shop.") String itemID,
                                    @Arg(name = "Value", description = "The value of the item.") Double value){

        if (!isEnabled()) return;

        if (sellAllConfig.getString("Options.Add_Permission_Enabled").equalsIgnoreCase("true")) {
            if (!sender.hasPermission("Options.Add_Permission")) {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Add_Permission") + "]"));
                return;
            }
        }
        if (itemID == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllPleaseAddItem")));
            return;
        }
        itemID = itemID.toUpperCase();

        if (value == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAddPrice")));
            return;
        }

        try {
            XMaterial blockAdd;
            try{
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
                return;
            }

            try {
                sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + itemID + ".ITEM_ID", blockAdd.name());
                conf.set("Items." + itemID + ".ITEM_VALUE", value);
                conf.save(sellAllFile);
            } catch (IOException e) {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return;
            }
        } catch (IllegalArgumentException ex){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }


        sender.sendMessage(SpigotPrison.format("&3[PRISON]&a ITEM [" + itemID + ", " + value + messages.getString("Message.SellAllCommandEditSuccess")));
    }

    @Command(identifier = "sellall multiplier", description = "SellAll multiplier command list", onlyPlayers = false)
    private void sellAllMultiplierCommand(CommandSender sender){

        if (!isEnabled()) return;

        if (sellAllConfig.getString("Options.Multiplier_Command_Permission_Enabled").equalsIgnoreCase("true")){
            if (!(sender.hasPermission(sellAllConfig.getString("Options.Multiplier_Command_Permission")))){
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Multiplier_Command_Permission") + "]"));
                return;
            }
        }
        if (!(sellAllConfig.getString("Options.Multiplier_Enabled").equalsIgnoreCase("true"))){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMultipliersAreDisabled")));
            return;
        }

        sender.dispatchCommand("sellall multiplier help");
    }

    @Command(identifier = "sellall multiplier add", description = "SellAll add a multiplier.", onlyPlayers = false)
    private void sellAllAddMultiplierCommand(CommandSender sender,
                                             @Arg(name = "Prestige", description = "Prestige to hook to the multiplier.") String prestige,
                                             @Arg(name = "multiplier", description = "Multiplier value.") Double multiplier){

        if (!isEnabled()) return;
        if (addMultiplierConditions(sender, prestige, multiplier)) return;

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestige + ".PRESTIGE_NAME", prestige);
            conf.set("Multiplier." + prestige + ".MULTIPLIER", multiplier);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            return;
        }

        sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMultiplierEditSaveSuccess")));
    }

    private boolean addMultiplierConditions(CommandSender sender, String prestige, Double multiplier) {

        if (sellAllConfig.getString("Options.Multiplier_Command_Permission_Enabled").equalsIgnoreCase("true")){
            if (!(sender.hasPermission(sellAllConfig.getString("Options.Multiplier_Command_Permission")))){

                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Multiplier_Command_Permission") + "]"));
                return true;
            }
        }
        if (!(sellAllConfig.getString("Options.Multiplier_Enabled").equalsIgnoreCase("true"))){

            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMultipliersAreDisabled")));
            return true;
        }
        if (prestige == null){

            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMultiplierWrongFormat")));
            return true;
        }
        if (multiplier == null){

            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMultiplierWrongFormat")));
            return true;
        }

        PrisonRanks rankPlugin = (PrisonRanks) (Prison.get().getModuleManager() == null ? null : Prison.get().getModuleManager().getModule(PrisonRanks.MODULE_NAME).orElse(null));
        if (rankPlugin == null) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllRanksDisabled")));
            return true;
        }

        boolean isPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges").isPresent();
        if (!isPrestigeLadder) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllPrestigeLadderNotFound")));
            return true;
        }

        boolean isARank = rankPlugin.getRankManager().getRank(prestige) != null;
        if (!isARank) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllCantFindPrestigeOrRank") + prestige));
            return true;
        }

        boolean isInPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges").get().containsRank(rankPlugin.getRankManager().getRank(prestige).getId());
        if (!isInPrestigeLadder) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllRankNotFoundInPrestigeLadder") + prestige));
            return true;
        }
        return false;
    }

    @Command(identifier = "sellall multiplier delete", description = "SellAll delete a multiplier.", onlyPlayers = false)
    private void sellAllDeleteMultiplierCommand(CommandSender sender,
                                                @Arg(name = "Prestige", description = "Prestige hooked to the multiplier.") String prestige){

        if (!isEnabled()) return;

        if (prestige == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMultiplierFormat")));
            return;
        }
        if (sellAllConfig.getConfigurationSection("Multiplier." + prestige) == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllCantFindMultiplier") + prestige + messages.getString("Message.SellAllCantFindMultiplier2")));
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestige, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            return;
        }
        sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMultiplierDeleteSuccess")));
    }

    @Command(identifier = "sellall setdefault", description = "SellAll default values ready to go.", onlyPlayers = false)
    private void sellAllSetDefaultCommand(CommandSender sender){

        if (!isEnabled()) return;

        if (sender.hasPermission("prison.admin")){
            valueSaver("COBBLESTONE", 50, sender);
            valueSaver("STONE", 50, sender);
            valueSaver("COAL",50, sender);
            valueSaver("COAL_BLOCK", 450, sender);
            valueSaver("IRON_INGOT",75, sender);
            valueSaver("IRON_BLOCK",600, sender);
            valueSaver("REDSTONE", 101, sender);
            valueSaver("REDSTONE_BLOCK", 909, sender);
            valueSaver("GOLD_INGOT", 122, sender);
            valueSaver("GOLD_BLOCK", 1100, sender);
            valueSaver("DIAMOND", 200, sender);
            valueSaver("DIAMOND_BLOCK", 1800, sender);
            valueSaver("EMERALD", 300, sender);
            valueSaver("EMERALD_BLOCK", 2700, sender);
            valueSaver("LAPIS_LAZULI", 70, sender);
            valueSaver("LAPIS_BLOCK", 630, sender);

            sender.sendMessage(SpigotPrison.format("&7[&3Prison&7] &aDefault Values added with success!"));
        } else {
            sender.sendMessage(SpigotPrison.format("&7[&3Prison Error&7] &cSorry but you're missing the permission [prison.admin]!"));
        }
    }

    private void valueSaver(String material, int value, CommandSender sender){
        Material materialM = Material.matchMaterial(material);
        if (materialM == null){
            return;
        } else {
            material = materialM.name();
        }
        sender.dispatchCommand( "sellall add " + material + " " + value);
    }
}

package tech.mcprison.prison.spigot.sellall;


import at.pcgamingfreaks.Minepacks.Bukkit.API.Backpack;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllPlayerGUI;
import tech.mcprison.prison.spigot.integrations.IntegrationMinepacksPlugin;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * SellAllUtil class, this will replace the whole SellAll mess of a code of SellAllPrisonCommands.
 *
 * @author GABRYCA
 * */
public class SellAllUtil {

    private SellAllUtil instance;
    private final boolean isEnabled = isEnabled();
    private File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
    public Configuration sellAllConfig = SpigotPrison.getInstance().getSellAllConfig();
    public static List<String> activePlayerDelay = new ArrayList<>();
    public boolean signUsed = false;
    private final Compatibility compat = SpigotPrison.getInstance().getCompatibility();
    private final ItemStack lapisLazuli = compat.getLapisItemStack();
    private String idBeingProcessedBackpack = null;
    public inventorySellMode mode = inventorySellMode.PlayerInventory;
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

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
    public SellAllUtil get(){
        return instanceUpdater();
    }

    /**
     * Use this to toggle the SellAllSign, essentially this will tell to the SellAll Sell command that you're using a sign
     * for SellAll.
     * */
    public void toggleSellAllSign(){
        sellAllSignToggler();
    }

    /**
     * Get the money to give to the Player depending on the multiplier.
     * This can be a bit like the SellAll Sell command, but without sellall sounds and options.
     *
     * @param player - Player
     * @param removeItems - True to remove the items from the Player, False to get only the value of the Money + Multiplier
     *                    - Without touching the Player's inventory.
     *
     * @return money
     * */
    public double getMoneyWithMultiplier(Player player, boolean removeItems){
        return getMoneyFinal(player, removeItems);
    }

    /**
     * Get the player multiplier, requires SpigotPlayer.
     *
     * @param sPlayer SpigotPlayer
     *
     * @return multiplier
     * */
    public double getMultiplier(SpigotPlayer sPlayer) {
        return getMultiplierMethod(sPlayer);
    }

    /**
     * Set sellSll currency by name
     *
     * @param sender - CommandSender (Prison)
     * @param currency - String currency name
     *
     * @return error - true if an error occurred.
     * */
    public boolean setSellAllCurrency(CommandSender sender, String currency) {
        return sellAllCurrencySaver(sender, currency);
    }

    /**
     * Check if SellAll's enabled.
     * */
    public boolean isEnabled(){
        return getBoolean(SpigotPrison.getInstance().getConfig().getString("sellall"));
    }

    /**
     * SellAll config updater.
     * */
    public void updateSellAllConfig(){
        sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
        sellAllConfig = YamlConfiguration.loadConfiguration(sellAllFile);
    }

    /**
     * Enable or disable SellAllDelay
     *
     * @param enableBoolean - True to enable and False to disable.
     *
     * @return error - True if an error occurred.
     * */
    public boolean sellAllDelayEnable(boolean enableBoolean) {
        return sellAllDelayToggle(enableBoolean);
    }

    /**
     * Set SellAll delay.
     *
     * @param delayValue
     *
     * @return error - True if error occurred.
     * */
    public boolean setSellAllDelay(int delayValue) {
        return sellAllDelaySave(delayValue);
    }

    /**
     * Enable or disable SellAll AutoSell.
     *
     * @param enableBoolean - True to enable or False to disable
     *
     * @return error - True if error occurred.
     * */
    public boolean sellAllAutoSellEnable(boolean enableBoolean) {
        return sellAllAutoSellToggle(enableBoolean);
    }

    /**
     * Enable or disable perUserToggleable autoSell.
     *
     * @param enableBoolean - True to enable or False to disable
     *
     * @return error - True if error occurred.
     * */
    public boolean sellAllAutoSellPerUserToggleableEnable(boolean enableBoolean) {
        return sellAllAutoSelPerUserToggleableTottle(enableBoolean);
    }

    /**
     * SellAll Sell command essentially, but in a method.
     *
     * NOTE: It applies sellall options from the config, except the sellall permission one.
     *
     * @param p - Player affected by sellall.
     * */
    public void sellAllSellAction(Player p) {
        sellAllSellPlayer(p);
    }

    /**
     * Open SellAll GUI for Players or Admins depending on their status and/or permissions.
     *
     * @param p - Player that should open the GUI
     *
     * @return boolean - True if a GUI got open with success, false if Disabled or missing all permissions.
     * */
    public boolean sellAllGUI(Player p) {
        // If the Admin GUI's enabled will enter do this, if it isn't it'll try to open the Player GUI.
        boolean guiEnabled = getBoolean(sellAllConfig.getString("Options.GUI_Enabled"));
        if (guiEnabled){
            // Check if a permission's required, if it isn't it'll open immediately the GUI.
            if (getBoolean(sellAllConfig.getString("Options.GUI_Permission_Enabled"))){
                // Check if the sender have the required permission.
                String guiPermission = sellAllConfig.getString("Options.GUI_Permission");
                if (guiPermission != null && p.hasPermission(guiPermission)) {
                    SellAllAdminGUI gui = new SellAllAdminGUI(p);
                    gui.open();
                    return true;
                    // Try to open the Player GUI anyway.
                } else if (sellAllPlayerGUI(p)) return true;
                // Open the Admin GUI because a permission isn't required.
            } else {
                SellAllAdminGUI gui = new SellAllAdminGUI(p);
                gui.open();
                return true;
            }
        }

        // If the admin GUI's disabled, it'll try to use the Player GUI anyway.
        return sellAllPlayerGUI(p);
    }

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){
        return string != null && string.equalsIgnoreCase("true");
    }

    private void sellAllSellPlayer(Player p) {
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
                    Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllNothingToSell")));
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
                    Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllYouGotMoney") + PlaceholdersUtil.formattedKmbtSISize(moneyToGive, formatDecimal, "")));
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
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllEmpty")));
        }
    }

    private SellAllUtil instanceUpdater() {
        if (isEnabled && instance == null){
            instance = new SellAllUtil();
        }

        return instance;
    }

    private boolean sellAllAutoSelPerUserToggleableTottle(boolean enableBoolean) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell_perUserToggleable", enableBoolean);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private boolean sellAllAutoSellToggle(boolean enableBoolean) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell", enableBoolean);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        sellAllConfigUpdater();
        return false;
    }

    private boolean sellAllDelaySave(int delayValue) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Seconds", delayValue);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        sellAllConfigUpdater();
        return false;
    }

    private boolean sellAllDelayToggle(boolean enableBoolean) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Sell_Delay_Enabled", enableBoolean);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        sellAllConfigUpdater();
        return false;
    }

    private boolean sellAllCurrencySaver(CommandSender sender, String currency) {

        EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager().getEconomyForCurrency(currency);
        if (currencyEcon == null && !currency.equalsIgnoreCase("default")) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllCurrencyNotFound")), currency);
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.SellAll_Currency", currency);
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().sendError(sender, SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
            return true;
        }

        sellAllConfigUpdater();
        return false;
    }

    private double getNewMoneyToGive(Player p, boolean removeItems){

        // Money to give value, Player Inventory, Items config section.
        double moneyToGive = 0;
        Inventory inv = p.getInventory();
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);

        // Get values and XMaterials from config.
        HashMap<XMaterial, Double> sellAllXMaterials = getDoubleXMaterialHashMap(items);

        // Get the items from the player inventory and for each of them check the conditions.
        mode = inventorySellMode.PlayerInventory;
        for (ItemStack itemStack : inv.getContents()){
            moneyToGive += getNewMoneyToGiveManager(p, itemStack, removeItems, sellAllXMaterials);
        }

        // Check option and if enabled.
        if (IntegrationMinepacksPlugin.getInstance().isEnabled() &&
                getBoolean(sellAllConfig.getString("Options.Sell_MinesBackPacks_Plugin_Backpack"))) {

            // Get money to give depending on Mines Backpacks plugin.
            moneyToGive = sellAllGetMoneyToGiveMinesBackpacksPlugin(p, removeItems, moneyToGive, sellAllXMaterials);
        }

        // Check if enabled Prison backpacks and sellall on it.
        if (getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks")) &&
                getBoolean(sellAllConfig.getString("Options.Sell_Prison_BackPack_Items"))) {

            // Get money to give from the Prison backpacks.
            moneyToGive = sellAllGetMoneyToGivePrisonBackpacks(p, removeItems, moneyToGive, sellAllXMaterials);
        }

        return moneyToGive;
    }

    private double sellAllGetMoneyToGivePrisonBackpacks(Player p, boolean removeItems, double moneyToGive, HashMap<XMaterial, Double> sellAllXMaterials) {

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
                                    moneyToGive += getNewMoneyToGiveManager(p, itemStack, removeItems, sellAllXMaterials);
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
                                    moneyToGive += getNewMoneyToGiveManager(p, itemStack, removeItems, sellAllXMaterials);
                                }
                            }
                        }
                    }
                }
                idBeingProcessedBackpack = null;
            }

        } else {
            // Set mode and get Prison Backpack inventory
            mode = inventorySellMode.PrisonBackPackSingle;
            Inventory backPack = BackpacksUtil.get().getBackpack(p);

            if (backPack != null) {
                for (ItemStack itemStack : backPack.getContents()) {
                    if (itemStack != null) {
                        moneyToGive += getNewMoneyToGiveManager(p, itemStack, removeItems, sellAllXMaterials);
                    }
                }
            }
        }
        return moneyToGive;
    }

    private double sellAllGetMoneyToGiveMinesBackpacksPlugin(Player p, boolean removeItems, double moneyToGive, HashMap<XMaterial, Double> sellAllXMaterials) {
        // Set mode and get backpack
        mode = inventorySellMode.MinesBackPack;
        Backpack backPack = IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p);

        if (backPack != null) {
            for (ItemStack itemStack : backPack.getInventory().getContents()) {
                if (itemStack != null) {
                    moneyToGive += getNewMoneyToGiveManager(p, itemStack, removeItems, sellAllXMaterials);
                }
            }
        }
        return moneyToGive;
    }

    @NotNull
    private HashMap<XMaterial, Double> getDoubleXMaterialHashMap(Set<String> items) {
        HashMap<XMaterial, Double> sellAllXMaterials = new HashMap<>();
        for (String key : items) {
            // ItemID
//            XMaterial itemMaterial = null;
            String itemID = sellAllConfig.getString("Items." + key + ".ITEM_ID");

            // NOTE: XMaterial is an exhaustive matching algorythem and will match on more than just the XMaterial enum name.
            // WARNING: Do not use XMaterial.valueOf() since that only matches on enum name and appears to fail if the internal cache is empty?
            Optional<XMaterial> iMatOptional = XMaterial.matchXMaterial( itemID );
            XMaterial itemMaterial = iMatOptional.orElse( null );

            if ( itemMaterial != null ) {
                String valueString = sellAllConfig.getString("Items." + key + ".ITEM_VALUE");
                if (valueString != null) {
                    try {
                        // If we cannot get a valid value, then there is no point in adding the
                        // itemMaterial to the hash since it will be zero anyway:
                        double value = Double.parseDouble(valueString);
                        sellAllXMaterials.put(itemMaterial, value);
                    }
                    catch ( NumberFormatException ignored ) {
                    }
                }

            }
        }
        return sellAllXMaterials;
    }

    private double getNewMoneyToGiveManager(Player p, ItemStack itemStack, boolean removeItems, HashMap<XMaterial, Double> sellAllXMaterials) {

        double moneyToGive = 0;

        if (itemStack != null) {

            boolean perBlockPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"));
            String permission = sellAllConfig.getString("Options.Sell_Per_Block_Permission");



            // First map itemStack to XMaterial:
            try {
                XMaterial invMaterial = getXMaterialOrLapis(itemStack);

                if (invMaterial != null && sellAllXMaterials.containsKey(invMaterial)) {
                    Double itemValue = sellAllXMaterials.get(invMaterial);
                    int amount = itemStack.getAmount();

                    // Check if per-block permission's enabled and if player has permission.
                    if (perBlockPermissionEnabled) {
                        permission = permission + invMaterial.name();

                        // Check if player have this permission, if not return 0 money earned for this item and don't remove it.
                        if (!p.hasPermission(permission)){
                            return 0;
                        }
                    }

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

                    if ( itemValue != null && amount > 0 ) {
                        moneyToGive += itemValue * amount;
                    }
                }
            }
            catch (IllegalArgumentException ignored) {}
        }
        return moneyToGive;
    }

    @NotNull
    private XMaterial getXMaterialOrLapis(ItemStack itemStack) {
        if (itemStack.isSimilar(lapisLazuli)){
            return XMaterial.LAPIS_LAZULI;
        }
        return XMaterial.matchXMaterial(itemStack);
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
        boolean multiplierPermissionHighOption = getBoolean(sellAllConfig.getString("Options.Multiplier_Permission_Only_Higher"));
        for (String multByPerm : perms){
            double multByPermDouble = Double.parseDouble(multByPerm.substring(26));
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

        // Get updated config.
        sellAllConfig = YamlConfiguration.loadConfiguration(new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml"));
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

    private void sellAllSignToggler() {
        if (!signUsed){
            signUsed = true;
        }
    }

    private double getMoneyFinal(Player player, boolean removeItems) {
        SpigotPlayer sPlayer = new SpigotPlayer(player);

        // Get money to give
        double moneyToGive = getNewMoneyToGive(sPlayer.getWrapper(), removeItems);
        boolean multiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        if (multiplierEnabled) {
            moneyToGive = moneyToGive * getMultiplier(sPlayer);;
        }

        return moneyToGive;
    }

    private double getMultiplierMethod(SpigotPlayer sPlayer) {
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

}

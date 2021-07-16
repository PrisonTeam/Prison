package tech.mcprison.prison.spigot.sellall;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
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

/**
 * SellAllUtil class, this will replace the whole SellAll mess of a code of SellAllPrisonCommands.
 *
 * @author GABRYCA
 */
public class SellAllUtil {

    private static SellAllUtil instance;
    private final boolean isEnabled = isEnabled();
    private File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
    public Configuration sellAllConfig = SpigotPrison.getInstance().updateSellAllConfig();
    public static List<String> activePlayerDelay = new ArrayList<>();
    public static Map<Player, Double> activeAutoSellPlayers = new HashMap<>();
//    public boolean signUsed = false;
    private final Compatibility compat = SpigotPrison.getInstance().getCompatibility();
    private final ItemStack lapisLazuli = compat.getLapisItemStack();
    private String idBeingProcessedBackpack = null;
    public inventorySellMode mode = inventorySellMode.PlayerInventory;
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

    /**
     * SellAll mode.
     */
    public enum inventorySellMode {
        PlayerInventory,
        MinesBackPack,
        PrisonBackPackSingle,
        PrisonBackPackMultiples
    }

    /**
     * Get SellAll instance.
     */
    public static SellAllUtil get() {
        return instanceUpdater();
    }

    public Configuration getSellAllConfig(){
        return sellAllConfig;
    }

//    /**
//     * Use this to toggle the SellAllSign, essentially this will tell to the SellAll Sell command that you're using a sign
//     * for SellAll.
//     */
//    public void toggleSellAllSign() {
//        sellAllSignToggler();
//    }

    /**
     * Get the money to give to the Player depending on the multiplier.
     * This can be a bit like the SellAll Sell command, but without sellall sounds and options.
     *
     * @param player      - Player
     * @param removeItems - True to remove the items from the Player, False to get only the value of the Money + Multiplier
     *                    - Without touching the Player's inventory.
     * @return money
     */
    public double getMoneyWithMultiplier(Player player, boolean removeItems) {
        return getMoneyFinal(player, removeItems);
    }

    /**
     * Get the player multiplier, requires SpigotPlayer.
     *
     * @param sPlayer SpigotPlayer
     * @return multiplier
     */
    public double getMultiplier(SpigotPlayer sPlayer) {
        return getMultiplierMethod(sPlayer);
    }

    /**
     * Set sellSll currency by name
     *
     * @param currency - String currency name
     * @return error - true if an error occurred.
     */
    public boolean setCurrency(String currency) {
        return sellAllCurrencySaver(currency);
    }

    /**
     * Check if SellAll's enabled.
     */
    public boolean isEnabled() {
        return getBoolean(SpigotPrison.getInstance().getConfig().getString("sellall"));
    }

    /**
     * SellAll config updater.
     */
    public void updateSellAllConfig() {
        sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
        sellAllConfig = YamlConfiguration.loadConfiguration(sellAllFile);
    }

    /**
     * Enable or disable SellAllDelay
     *
     * @param enableBoolean - True to enable and False to disable.
     * @return error - True if an error occurred.
     */
    public boolean enableDelay(boolean enableBoolean) {
        return sellAllDelayToggle(enableBoolean);
    }

    /**
     * Set SellAll delay.
     *
     * @param delayValue
     * @return error - True if error occurred.
     */
    public boolean setDelay(int delayValue) {
        return sellAllDelaySave(delayValue);
    }

    /**
     * Enable or disable SellAll AutoSell.
     *
     * @param enableBoolean - True to enable or False to disable
     * @return error - True if error occurred.
     */
    public boolean enableAutoSell(boolean enableBoolean) {
        return sellAllAutoSellToggle(enableBoolean);
    }

    /**
     * Enable or disable perUserToggleable autoSell.
     *
     * @param enableBoolean - True to enable or False to disable
     * @return error - True if error occurred.
     */
    public boolean enableAutoSellPerUserToggleable(boolean enableBoolean) {
        return sellAllAutoSellPerUserToggleableToggle(enableBoolean);
    }

    /**
     * Enable or disable autosell to a player.
     *
     * @param p - Player
     *
     * @return error - True if error occurred, false if success.
     * */
    public boolean autoSellPlayerToggle(Player p) {
        return sellAllAutoSellPlayerToggle(p);
    }

    /**
     * SellAll Sell command essentially, but in a method.
     * NOTE: It applies sellAll options from the config, except the sellAll permission one.
     *
     * @param p - Player affected by sellall.
     */
    public void sellAllSell(Player p) {
        sellAllSellPlayer(p);
    }
    
    public void sellAllSell(Player p, boolean notifications, boolean bySignOnly) {
    	sellAllSellPlayer(p, notifications, bySignOnly);
    }

    /**
     * Open SellAll GUI for Players or Admins depending on their status and/or permissions.
     *
     * @param p - Player that should open the GUI.
     * @return boolean - True if a GUI got open with success, false if Disabled or missing all permissions.
     */
    public boolean openGUI(Player p) {
        return sellAllOpenGUI(p);
    }

    /**
     * Add SellAll Block by ID.
     *
     * @param itemID - String name of Block.
     * @param value  - Value of the block when sold.
     * @return error - True if an error occurred, false if success.
     */
    public boolean addBlock(String itemID, Double value) {
        return sellAllAddBlockAction(itemID, value);
    }

    /**
     * Add SellAll Block by Material.
     *
     * @param material - Material of Block.
     * @param value    - Value of the block when sold.
     * @return error - True if an error occurred, false if success.
     */
    public boolean addBlock(Material material, Double value) {
        return sellAllAddBlockAction(material, value);
    }

    /**
     * Add SellAll Block by ItemStack.
     *
     * @param itemStack - ItemStack of Block.
     * @param value     - Value of the block when sold.
     * @return error - True if an error occurred, false if success.
     */
    public boolean addBlock(ItemStack itemStack, Double value) {
        return sellAllAddBlockAction(itemStack, value);
    }

    /**
     * Add SellAll Block by ItemStack.
     *
     * @param xMaterial - XMaterial of Block.
     * @param value     - Value of the block when sold.
     * @return error - True if an error occurred, false if success.
     */
    public boolean addBlock(XMaterial xMaterial, Double value) {
        return sellAllAddBlockAction(xMaterial, value);
    }

    /**
     * <p>This will add the XMaterial and value to the sellall.
     * This will update even if the sellall has not been enabled.
     * </p>
     *
     * @param blockAdd - XMaterial of the Block to add.
     * @param value    - Double value when sold.
     */
    public void sellAllAddCommand(XMaterial blockAdd, Double value) {
        sellAllAddCommandMethod(blockAdd, value);
    }

    /**
     * Remove/Delete SellAll Block from the config.
     *
     * @param itemID - String name of Block.
     * @return error - True if an error occurred or missing item in the config, False if success.
     */
    public boolean deleteBlock(String itemID) {
        return sellAllDeleteAction(itemID);
    }

    /**
     * Remove/Delete SellAll Block from the config.
     *
     * @param material - Material of Block.
     * @return error - True if an error occurred or missing item in the config, False if success.
     */
    public boolean deleteBlock(Material material) {
        return sellAllDeleteAction(material);
    }

    /**
     * Remove/Delete SellAll Block from the config.
     *
     * @param itemStack - ItemStack of Block.
     * @return error - True if an error occurred or missing item in the config, False if success.
     */
    public boolean deleteBlock(ItemStack itemStack) {
        return sellAllDeleteAction(itemStack);
    }

    /**
     * Remove/Delete SellAll Block from the config.
     *
     * @param xMaterial - XMaterial of Block.
     * @return error - True if an error occurred or missing item in the config, False if success.
     */
    public boolean deleteBlock(XMaterial xMaterial) {
        return sellAllDeleteAction(xMaterial);
    }

    /**
     * Edit SellAll Block.
     *
     * @param itemID - String name of Block.
     * @param value  - Double value when Sold.
     *
     * @return error - True if error occurred or block is missing in the config, False if success.
     */
    public boolean editBlock(String itemID, Double value) {
        return sellAllEditBlockAction(itemID, value);
    }

    /**
     * Edit SellAll Block.
     *
     * @param material - Material of Block.
     * @param value  - Double value when Sold.
     *
     * @return error - True if error occurred or block is missing in the config, False if success.
     */
    public boolean editBlock(Material material, Double value) {
        return sellAllEditBlockAction(material, value);
    }

    /**
     * Edit SellAll Block.
     *
     * @param itemStack - ItemStack of Block.
     * @param value  - Double value when Sold.
     *
     * @return error - True if error occurred or block is missing in the config, False if success.
     */
    public boolean editBlock(ItemStack itemStack, Double value) {
        return sellAllEditBlockAction(itemStack, value);
    }

    /**
     * Edit SellAll Block.
     *
     * @param xMaterial - XMaterial of Block.
     * @param value  - Double value when Sold.
     *
     * @return error - True if error occurred or block is missing in the config, False if success.
     */
    public boolean editBlock(XMaterial xMaterial, Double value) {
        return sellAllEditBlockAction(xMaterial, value);
    }

    /**
     * Add SellAll Multiplier.
     *
     * @param prestige - Prestige name to hook.
     * @param multiplier - Multiplier Double Value.
     *
     * @return error - True if error occurred and false if success.
     * */
    public boolean addMultiplier(String prestige, Double multiplier) {
        return sellAllAddMultiplierAction(prestige, multiplier);
    }

    /**
     * Delete SellAll Multiplier.
     *
     * @param prestige - Prestige with the multiplier
     *
     * @return error - True if error occurred or the Multiplier wasn't found, false if success.
     * */
    public boolean deleteMultiplier(String prestige) {
        return sellAllDeleteMultiplierAction(prestige);
    }

    /**
     * Enable or disable SellAll Item Trigger feature.
     * This's a feature that makes the player able to Shift and Right click
     * On an item in his hand to trigger sellall.
     *
     * @param enableInput - True to enable, False to disable.
     *
     * @return error - True if error occurred, False if success.
     * */
    public boolean toggleItemTrigger(boolean enableInput) {
        return sellAllItemToggle(enableInput);
    }

    /**
     * Add an Item to the SellAll item Triggers.
     *
     * @param itemID - String name of the Item
     *
     * @return error - True if an error occurred, False if success.
     * */
    public boolean addItemTrigger(String itemID) {
        return sellAllAddTrigger(itemID);
    }

    /**
     * Add an Item to the SellAll item Triggers.
     *
     * @param itemMaterial - XMaterial of the Item
     *
     * @return error - True if an error occurred, False if success.
     * */
    public boolean addItemTrigger(XMaterial itemMaterial) {
        return sellAllAddTrigger(itemMaterial);
    }

    /**
     * Add an Item to the SellAll item Triggers.
     *
     * @param itemMaterial - Material of the Item
     *
     * @return error - True if an error occurred, False if success.
     * */
    public boolean addItemTrigger(Material itemMaterial) {
        return sellAllAddTrigger(itemMaterial);
    }

    /**
     * Add an Item to the SellAll item Triggers.
     *
     * @param itemStack - ItemStack of the Item
     *
     * @return error - True if an error occurred, False if success.
     * */
    public boolean addItemTrigger(ItemStack itemStack) {
        return sellAllAddTrigger(itemStack);
    }

    /**
     * Delete a sellAll Item trigger.
     *
     * @param itemID - Name of the item.
     *
     * @return error - True if error occurred or item not found, False if success.
     * */
    public boolean deleteItemTrigger(String itemID) {
        return sellAllDeleteTrigger(itemID);
    }

    /**
     * Delete a sellAll Item trigger.
     *
     * @param itemMaterial - Material of the item.
     *
     * @return error - True if error occurred or item not found, False if success.
     * */
    public boolean deleteItemTrigger(XMaterial itemMaterial) {
        return sellAllDeleteTrigger(itemMaterial);
    }

    /**
     * Delete a sellall Item trigger.
     *
     * @param itemMaterial - Material of the item.
     *
     * @return error - True if error occurred or item not found, False if success.
     * */
    public boolean deleteItemTrigger(Material itemMaterial) {
        return sellAllDeleteTrigger(itemMaterial);
    }

    /**
     * Delete a sellAll Item trigger.
     *
     * @param itemStack - ItemStack of the item.
     *
     * @return error - True if error occurred or item not found, False if success.
     * */
    public boolean deleteItemTrigger(ItemStack itemStack) {
        return sellAllDeleteTrigger(itemStack);
    }

    /**
     * Check if the world the player is in is disabled.
     *
     * @param p - Player
     *
     * @return boolean - true if the player is in a disabled world, false if not.
     * */
    public boolean isDisabledWorld(Player p) {
        return playerPositionIsDisabledWorld(p);
    }

    /**
     * Add Player to AutoSell delay for earned money notification.
     * This will add the player only if missing, if already added then will do nothing.
     * */
    public void addToAutoSellTask(Player p) {
        if (!activeAutoSellPlayers.containsKey(p) && getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Enabled"))) {
            activeAutoSellPlayers.put(p, 0.00);
            int delay = Integer.parseInt(sellAllConfig.getString("Options.Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Delay_Seconds"));
            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> taskSellAllAutoActions(p), 20L * delay);
        }
    }

    /**
     * Remove Player from AutoSell delay for earned money notification.
     * This's usually handled only internally and should be used only for some rare exceptions.
     * */
    public void removeFromAutoSellTask(Player p){
        activeAutoSellPlayers.remove(p);
    }

    /**
     * Java getBoolean's broken so I made my own.
     */
    public boolean getBoolean(String string) {
        return string != null && string.equalsIgnoreCase("true");
    }



    private boolean playerPositionIsDisabledWorld(Player p) {
        updateSellAllConfig();
        String worldName = p.getWorld().getName();
        List<String> disabledWorlds = sellAllConfig.getStringList("Options.DisabledWorlds");
        return disabledWorlds.contains(worldName);
    }

    private boolean sellAllDeleteTrigger(ItemStack itemStack) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + itemStack.getType().name() + ".ITEM_ID", null);
            conf.set("ShiftAndRightClickSellAll.Items." + itemStack.getType().name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteTrigger(Material itemMaterial) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + itemMaterial.name() + ".ITEM_ID", null);
            conf.set("ShiftAndRightClickSellAll.Items." + itemMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteTrigger(XMaterial itemMaterial) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + itemMaterial.name() + ".ITEM_ID", null);
            conf.set("ShiftAndRightClickSellAll.Items." + itemMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteTrigger(String itemID) {
        if (sellAllConfig.getString("Options.ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID") == null){
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID", null);
            conf.set("ShiftAndRightClickSellAll.Items." + itemID, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddTrigger(ItemStack blockAdd) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + blockAdd.getType().name() + ".ITEM_ID", blockAdd.getType().name());
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddTrigger(Material blockAdd) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + blockAdd.name() + ".ITEM_ID", blockAdd.name());
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddTrigger(XMaterial blockAdd) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("ShiftAndRightClickSellAll.Items." + blockAdd.name() + ".ITEM_ID", blockAdd.name());
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddTrigger(String itemID) {
        try {
            XMaterial blockAdd;
            try {
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex) {
                return true;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("ShiftAndRightClickSellAll.Items." + itemID + ".ITEM_ID", blockAdd.name());
                conf.save(sellAllFile);
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllItemToggle(boolean enableInput) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.ShiftAndRightClickSellAll.Enabled", enableInput);
            conf.save(sellAllFile);
        } catch (IOException e) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteMultiplierAction(String prestige) {
        if (sellAllConfig.getConfigurationSection("Multiplier." + prestige) == null){
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestige, null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddMultiplierAction(String prestige, Double multiplier) {
        if (addMultiplierConditions(prestige)) return true;

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Multiplier." + prestige + ".PRESTIGE_NAME", prestige);
            conf.set("Multiplier." + prestige + ".MULTIPLIER", multiplier);
            conf.save(sellAllFile);
        } catch (IOException e) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllEditBlockAction(XMaterial xMaterial, Double value) {
        if (sellAllConfig.getConfigurationSection("Items." + xMaterial.name()) == null) {
            return true;
        }

        try {
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
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllEditBlockAction(ItemStack itemStack, Double value) {
        if (sellAllConfig.getConfigurationSection("Items." + itemStack.getType().name()) == null) {
            return true;
        }

        try {
            XMaterial xMaterial;
            try {
                xMaterial = XMaterial.matchXMaterial(itemStack);
            } catch (IllegalArgumentException ex) {
                return true;
            }

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
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllEditBlockAction(Material material, Double value) {
        if (sellAllConfig.getConfigurationSection("Items." + material.name()) == null) {
            return true;
        }

        try {
            XMaterial xMaterial;
            try {
                xMaterial = XMaterial.matchXMaterial(material);
            } catch (IllegalArgumentException ex) {
                return true;
            }

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
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllEditBlockAction(String itemID, Double value) {
        if (sellAllConfig.getConfigurationSection("Items." + itemID) == null) {
            return true;
        }

        try {
            XMaterial xMaterial;
            try {
                xMaterial = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex) {
                return true;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + itemID + ".ITEM_ID", xMaterial.name());
                conf.set("Items." + itemID + ".ITEM_VALUE", value);
                if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                    conf.set("Items." + itemID + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + xMaterial.name());
                }
                conf.save(sellAllFile);
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteAction(XMaterial xMaterial) {
        if (sellAllConfig.getConfigurationSection("Items." + xMaterial.name()) == null) {
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + xMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteAction(ItemStack itemStack) {

        XMaterial xMaterial;
        try {
            xMaterial = XMaterial.matchXMaterial(itemStack);
        } catch (IllegalArgumentException ex) {
            return true;
        }

        if (sellAllConfig.getConfigurationSection("Items." + xMaterial.name()) == null) {
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + xMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteAction(Material material) {

        XMaterial xMaterial;
        try {
            xMaterial = XMaterial.matchXMaterial(material);
        } catch (IllegalArgumentException ex) {
            return true;
        }

        if (sellAllConfig.getConfigurationSection("Items." + xMaterial.name()) == null) {
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + xMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllDeleteAction(String itemID) {

        XMaterial xMaterial;
        try {
            xMaterial = XMaterial.valueOf(itemID);
        } catch (IllegalArgumentException ex) {
            return true;
        }

        if (sellAllConfig.getConfigurationSection("Items." + xMaterial.name()) == null) {
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + xMaterial.name(), null);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddBlockAction(XMaterial xMaterial, Double value) {
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
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddBlockAction(ItemStack itemStack, Double value) {
        try {
            XMaterial blockAdd;
            try {
                blockAdd = XMaterial.matchXMaterial(itemStack);
            } catch (IllegalArgumentException ex) {
                return true;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + blockAdd.name() + ".ITEM_ID", blockAdd.name());
                conf.set("Items." + blockAdd.name() + ".ITEM_VALUE", value);
                if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                    conf.set("Items." + blockAdd.name() + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + blockAdd.name());
                }
                conf.save(sellAllFile);
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddBlockAction(Material material, Double value) {
        try {
            XMaterial blockAdd;
            try {
                blockAdd = XMaterial.matchXMaterial(material);
            } catch (IllegalArgumentException ex) {
                return true;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + blockAdd.name() + ".ITEM_ID", blockAdd.name());
                conf.set("Items." + blockAdd.name() + ".ITEM_VALUE", value);
                if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                    conf.set("Items." + blockAdd.name() + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + blockAdd.name());
                }
                conf.save(sellAllFile);
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private boolean sellAllAddBlockAction(String itemID, Double value) {
        try {
            XMaterial blockAdd;
            try {
                blockAdd = XMaterial.valueOf(itemID);
            } catch (IllegalArgumentException ex) {
                return true;
            }

            try {
                File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
                FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
                conf.set("Items." + blockAdd.name() + ".ITEM_ID", blockAdd.name());
                conf.set("Items." + blockAdd.name() + ".ITEM_VALUE", value);
                if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                    conf.set("Items." + blockAdd.name() + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + blockAdd.name());
                }
                conf.save(sellAllFile);
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        } catch (IllegalArgumentException ex) {
            return true;
        }
        updateSellAllConfig();
        return false;
    }

    private void sellAllAddCommandMethod(XMaterial xMaterial, Double value) {
        String itemID = xMaterial.name();

        // If the block or item was already configured, then skip this:
        if (sellAllConfig.getConfigurationSection("Items." + itemID) != null) {
            return;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + itemID + ".ITEM_ID", xMaterial.name());
            conf.set("Items." + itemID + ".ITEM_VALUE", value);
            if (getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"))) {
                conf.set("Items." + itemID + ".ITEM_PERMISSION", sellAllConfig.getString("Options.Sell_Per_Block_Permission") + xMaterial.name());
            }
            conf.save(sellAllFile);
        } catch (IOException e) {
            Output.get().logError(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")), e);
            return;
        }

        Output.get().logInfo(SpigotPrison.format("&3 ITEM [" + itemID + ", " + value + messages.getString("Message.SellAllAddSuccess")));
        sellAllConfigUpdater();
    }

    private boolean sellAllOpenGUI(Player p) {
        updateSellAllConfig();
        // If the Admin GUI's enabled will enter do this, if it isn't it'll try to open the Player GUI.
        boolean guiEnabled = getBoolean(sellAllConfig.getString("Options.GUI_Enabled"));
        if (guiEnabled) {
            // Check if a permission's required, if it isn't it'll open immediately the GUI.
            if (getBoolean(sellAllConfig.getString("Options.GUI_Permission_Enabled"))) {
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

    private void sellAllSellPlayer(Player p) {
    	sellAllSellPlayer( p, true, false );
    }
    private void sellAllSellPlayer(Player p, boolean notifications, boolean bySignOnly) {

        updateSellAllConfig();

        boolean sellAllSignEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_Sign_Enabled"));
        boolean sellAllBySignOnlyEnabled = getBoolean(sellAllConfig.getString("Options.SellAll_By_Sign_Only"));
        String byPassPermission = sellAllConfig.getString("Options.SellAll_By_Sign_Bypass_Permission");
        if (sellAllSignEnabled && sellAllBySignOnlyEnabled && (byPassPermission == null || byPassPermission != null && !p.hasPermission(byPassPermission))) {
            if (!bySignOnly) {
                Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllSignOnly")));
                return;
            }
        }

//        if (signUsed) signUsed = false;

        boolean sellSoundEnabled = notifications && getBoolean(sellAllConfig.getString("Options.Sell_Sound_Enabled"));
        Compatibility compat = SpigotPrison.getInstance().getCompatibility();
        if (!(sellAllConfig.getConfigurationSection("Items.") == null)) {

            if (sellAllCommandDelay(p)) return;

            // Get Spigot Player.
            SpigotPlayer sPlayer = new SpigotPlayer(p);

            // Get money to give + multiplier.
            double moneyToGive = getMoneyWithMultiplier(p, true);

            // Check if Ranks are enabled.
            ModuleManager modMan = Prison.get().getModuleManager();
            Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );
            PrisonRanks rankPlugin = (PrisonRanks) module;
            if (rankPlugin == null){
                Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("Ranks are disabled, you can't use this without Ranks enabled!"));
                return;
            }

            RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
            String currency = sellAllConfig.getString("Options.SellAll_Currency");
            if (currency != null && currency.equalsIgnoreCase("default")) currency = null;

            rankPlayer.addBalance(currency, moneyToGive);

            boolean sellNotifyEnabled = notifications && getBoolean(sellAllConfig.getString("Options.Sell_Notify_Enabled"));
            if (moneyToGive < 0.001) {
                if (sellSoundEnabled) {
                    Sound sound;
                    try {
                        sound = Sound.valueOf(sellAllConfig.getString("Options.Sell_Sound_Fail_Name"));
                    } catch (IllegalArgumentException ex) {
                        sound = compat.getAnvilSound();
                    }
                    p.playSound(p.getLocation(), sound, 3, 1);
                }
                if (sellNotifyEnabled) {
                    Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllNothingToSell")));
                }
            } else {
                if (activeAutoSellPlayers.containsKey(p) && getBoolean(sellAllConfig.getString("Options.Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Enabled"))) {
                    activeAutoSellPlayers.put(p, activeAutoSellPlayers.get(p) + moneyToGive);
                } else {
                    if (sellSoundEnabled) {
                        Sound sound;
                        try {
                            sound = Sound.valueOf(sellAllConfig.getString("Options.Sell_Sound_Success_Name"));
                        } catch (IllegalArgumentException ex) {
                            sound = compat.getLevelUpSound();
                        }
                        p.playSound(p.getLocation(), sound, 3, 1);
                    }
                    if (sellNotifyEnabled) {
                        DecimalFormat formatDecimal = new DecimalFormat("###,##0.00");
                        Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllYouGotMoney") + PlaceholdersUtil.formattedKmbtSISize(moneyToGive, formatDecimal, "")));
                    }
                }
            }
        } else {
            if (sellSoundEnabled) {
                Sound sound;
                try {
                    sound = Sound.valueOf(sellAllConfig.getString("Options.Sell_Sound_Fail_Name"));
                } catch (IllegalArgumentException ex) {
                    sound = compat.getAnvilSound();
                }
                p.playSound(p.getLocation(), sound, 3, 1);
            }
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllEmpty")));
        }
    }

    private static SellAllUtil instanceUpdater() {
        if (SpigotPrison.getInstance().getConfig().getString("sellall").equalsIgnoreCase("true") && instance == null) {
            instance = new SellAllUtil();
        }

        return instance;
    }

    private boolean sellAllAutoSellPerUserToggleableToggle(boolean enableBoolean) {
        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.Full_Inv_AutoSell_perUserToggleable", enableBoolean);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        updateSellAllConfig();
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

    private boolean sellAllAutoSellPlayerToggle(Player p) {
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
                Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return true;
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
                Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
                e.printStackTrace();
                return true;
            }

            Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllAutoEnabled")));
        }
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

    private boolean sellAllCurrencySaver(String currency) {

        EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager().getEconomyForCurrency(currency);
        if (currencyEcon == null && !currency.equalsIgnoreCase("default")) {
            return true;
        }

        try {
            File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Options.SellAll_Currency", currency);
            conf.save(sellAllFile);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        sellAllConfigUpdater();
        return false;
    }

    private double getNewMoneyToGive(Player p, boolean removeItems) {
        // Money to give value, Player Inventory, Items config section.
        double moneyToGive = 0;
        Inventory inv = p.getInventory();
        Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);

        // Get values and XMaterials from config.
        HashMap<XMaterial, Double> sellAllXMaterials = getDoubleXMaterialHashMap(items);

        // Get the items from the player inventory and for each of them check the conditions.
        mode = inventorySellMode.PlayerInventory;
        for (ItemStack itemStack : inv.getContents()) {
            moneyToGive += getNewMoneyToGiveManager(p, inv, itemStack, removeItems, sellAllXMaterials);
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
                    if (id == null) {

                        Inventory backPack = BackpacksUtil.get().getBackpack(p);
                        mode = inventorySellMode.PrisonBackPackSingle;

                        if (backPack != null) {
                            for (ItemStack itemStack : backPack.getContents()) {
                                if (itemStack != null) {
                                    moneyToGive += getNewMoneyToGiveManager(p, backPack, itemStack, removeItems, sellAllXMaterials);
                                }
                            }
                            BackpacksUtil.get().setInventory(p, backPack);
                        }

                    } else {

                        Inventory backPack = BackpacksUtil.get().getBackpack(p, id);
                        mode = inventorySellMode.PrisonBackPackMultiples;
                        idBeingProcessedBackpack = id;
                        if (backPack != null) {
                            for (ItemStack itemStack : backPack.getContents()) {
                                if (itemStack != null) {
                                    moneyToGive += getNewMoneyToGiveManager(p, backPack, itemStack, removeItems, sellAllXMaterials);
                                }
                            }
                            BackpacksUtil.get().setInventory(p, backPack, id);
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
                        moneyToGive += getNewMoneyToGiveManager(p, backPack, itemStack, removeItems, sellAllXMaterials);
                    }
                }
                BackpacksUtil.get().setInventory(p, backPack);
            }
        }
        return moneyToGive;
    }

    private double sellAllGetMoneyToGiveMinesBackpacksPlugin(Player p, boolean removeItems, double moneyToGive, HashMap<XMaterial, Double> sellAllXMaterials) {
        // Set mode and get backpack
        mode = inventorySellMode.MinesBackPack;
        Backpack backPack = IntegrationMinepacksPlugin.getInstance().getMinepacks().getBackpackCachedOnly(p);

        if (backPack != null) {
            Inventory inv = backPack.getInventory();
            for (ItemStack itemStack : inv.getContents()) {
                if (itemStack != null) {
                    moneyToGive += getNewMoneyToGiveManager(p, inv, itemStack, removeItems, sellAllXMaterials);
                }
            }
        }
        return moneyToGive;
    }

    private HashMap<XMaterial, Double> getDoubleXMaterialHashMap(Set<String> items) {
        HashMap<XMaterial, Double> sellAllXMaterials = new HashMap<>();
        for (String key : items) {
            // ItemID
//            XMaterial itemMaterial = null;
            String itemID = sellAllConfig.getString("Items." + key + ".ITEM_ID");

            // NOTE: XMaterial is an exhaustive matching algorythem and will match on more than just the XMaterial enum name.
            // WARNING: Do not use XMaterial.valueOf() since that only matches on enum name and appears to fail if the internal cache is empty?
            Optional<XMaterial> iMatOptional = XMaterial.matchXMaterial(itemID);
            XMaterial itemMaterial = iMatOptional.orElse(null);

            if (itemMaterial != null) {
                String valueString = sellAllConfig.getString("Items." + key + ".ITEM_VALUE");
                if (valueString != null) {
                    try {
                        // If we cannot get a valid value, then there is no point in adding the
                        // itemMaterial to the hash since it will be zero anyway:
                        double value = Double.parseDouble(valueString);
                        sellAllXMaterials.put(itemMaterial, value);
                    } catch (NumberFormatException ignored) {
                    }
                }

            }
        }
        return sellAllXMaterials;
    }

    private double getNewMoneyToGiveManager(Player p, Inventory inv, ItemStack itemStack, boolean removeItems, HashMap<XMaterial, Double> sellAllXMaterials) {

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
                        if (!p.hasPermission(permission)) {
                            return 0;
                        }
                    }

                    if (removeItems) {
                        if (mode == inventorySellMode.PlayerInventory) {
                            inv.remove(itemStack);
                        } else if (IntegrationMinepacksPlugin.getInstance().isEnabled() && mode == inventorySellMode.MinesBackPack) {
                            inv.remove(itemStack);
                        } else if (mode == inventorySellMode.PrisonBackPackSingle) {
                            inv.remove(itemStack);
                        } else if (mode == inventorySellMode.PrisonBackPackMultiples) {
                            if (idBeingProcessedBackpack != null) {
                                inv.remove(itemStack);
                            }
                        }
                    }

                    if (itemValue != null && amount > 0) {
                        moneyToGive += itemValue * amount;
                    }
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        return moneyToGive;
    }

    private XMaterial getXMaterialOrLapis(ItemStack itemStack) {
        if (itemStack.isSimilar(lapisLazuli)) {
            return XMaterial.LAPIS_LAZULI;
        }
        return XMaterial.matchXMaterial(itemStack);
    }

    /**
     * Open SellAll GUI for Players if enabled.
     *
     * @param p Player
     * @return boolean
     */
    private boolean sellAllPlayerGUI(Player p) {

        // Check if the Player GUI's enabled.
        boolean playerGUIEnabled = getBoolean(sellAllConfig.getString("Options.Player_GUI_Enabled"));
        if (playerGUIEnabled) {
            // Check if a permission's required, if not it'll open directly the Player's GUI.
            boolean playerGUIPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Player_GUI_Permission_Enabled"));
            if (playerGUIPermissionEnabled) {
                // Check if the sender has the required permission.
                String permission = sellAllConfig.getString("Options.Player_GUI_Permission");
                if (permission != null && p.hasPermission(permission)) {
                    SellAllPlayerGUI gui = new SellAllPlayerGUI(p, 0);
                    gui.open();
                    // If missing will send a missing permission error message.
                } else {
                    Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + " [" + permission + "]"));
                }
                // Because a permission isn't required, it'll open directly the GUI.
            } else {
                SellAllPlayerGUI gui = new SellAllPlayerGUI(p, 0);
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
     * @return boolean
     */
    private boolean sellAllCommandDelay(Player p) {

        boolean sellDelayEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Delay_Enabled"));
        if (sellDelayEnabled) {

            if (activePlayerDelay.contains(p.getName())) {
                Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllWaitDelay")));
                return true;
            }

            addPlayerToDelay(p);

            String delayInSeconds = sellAllConfig.getString("Options.Sell_Delay_Seconds");
            if (delayInSeconds == null) {
                delayInSeconds = "1";
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> removePlayerFromDelay(p), 20L * Integer.parseInt(delayInSeconds));
        }

        return false;
    }

    private boolean addMultiplierConditions(String prestige) {

        PrisonRanks rankPlugin = (PrisonRanks) (Prison.get().getModuleManager() == null ? null : Prison.get().getModuleManager().getModule(PrisonRanks.MODULE_NAME).orElse(null));
        if (rankPlugin == null) {
            return true;
        }

        boolean isPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges") != null;
        if (!isPrestigeLadder) {
            return true;
        }

        boolean isARank = rankPlugin.getRankManager().getRank(prestige) != null;
        if (!isARank) {
            return true;
        }

        boolean isInPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges").containsRank(rankPlugin.getRankManager().getRank(prestige));
        if (!isInPrestigeLadder) {
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
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }
        return multiplier;
    }

    private double getMultiplierExtraByPerms(List<String> perms, double multiplierExtraByPerms) {
        boolean multiplierPermissionHighOption = getBoolean(sellAllConfig.getString("Options.Multiplier_Permission_Only_Higher"));
        for (String multByPerm : perms) {
            double multByPermDouble = Double.parseDouble(multByPerm.substring(26));
            if (!multiplierPermissionHighOption) {
                multiplierExtraByPerms += multByPermDouble;
            } else if (multByPermDouble > multiplierExtraByPerms) {
                multiplierExtraByPerms = multByPermDouble;
            }
        }
        return multiplierExtraByPerms;
    }

    /**
     * Get sellAllConfig updated.
     */
    private void sellAllConfigUpdater() {

        // Get updated config.
        sellAllConfig = YamlConfiguration.loadConfiguration(new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml"));
    }

    /**
     * Add a Player to delay.
     *
     * @param p Player
     */
    private void addPlayerToDelay(Player p) {

        if (!isEnabled()) return;

        if (!activePlayerDelay.contains(p.getName())) {
            activePlayerDelay.add(p.getName());
        }
    }

    /**
     * Removes a Player from delay.
     *
     * @param p Player
     */
    private void removePlayerFromDelay(Player p) {

        if (!isEnabled()) return;

        activePlayerDelay.remove(p.getName());
    }

//    private void sellAllSignToggler() {
//        if (!signUsed) {
//            signUsed = true;
//        }
//    }

    private double getMoneyFinal(Player player, boolean removeItems) {
        SpigotPlayer sPlayer = new SpigotPlayer(player);

        // Get money to give
        double moneyToGive = getNewMoneyToGive(sPlayer.getWrapper(), removeItems);
        boolean multiplierEnabled = getBoolean(sellAllConfig.getString("Options.Multiplier_Enabled"));
        if (multiplierEnabled) {
            moneyToGive = moneyToGive * getMultiplier(sPlayer);
        }

        return moneyToGive;
    }

    private double getMultiplierMethod(SpigotPlayer sPlayer) {
        // Get Ranks module.
        ModuleManager modMan = Prison.get().getModuleManager();
        Module module = modMan == null ? null : modMan.getModule(PrisonRanks.MODULE_NAME).orElse(null);

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

    private void taskSellAllAutoActions(Player p) {
        if (activeAutoSellPlayers.containsKey(p)) {
            if (activeAutoSellPlayers.get(p) != 0.00) {
                Output.get().sendInfo(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllAutoSellEarnedMoney") + activeAutoSellPlayers.get(p) + messages.getString("Message.SellAllAutoSellEarnedMoneyCurrency")));
            }
            activeAutoSellPlayers.remove(p);
        }
    }
}
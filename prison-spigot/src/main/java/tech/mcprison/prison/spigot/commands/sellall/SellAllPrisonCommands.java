package tech.mcprison.prison.spigot.commands.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class SellAllPrisonCommands extends PrisonSpigotBaseCommands {

    private Configuration sellAllConfig = SpigotPrison.getInstance().getSellAllConfig();
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
    private File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
    private FileConfiguration conf = YamlConfiguration.loadConfiguration(sellAllFile);

    public static boolean isEnabled(){
        return SpigotPrison.getInstance().getConfig().getString("sellall").equalsIgnoreCase("true");
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

    @Command(identifier = "sellall sell", description = "SellAll sell command", onlyPlayers = true)
    private void SellAllSellCommand(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);
        SellAllConfig sellAllConfigClass = new SellAllConfig();
        sellAllConfigClass.initialize();
        sellAllConfig = sellAllConfigClass.getFileSellAllConfig();
        
        if (sellAllConfig.getString("Options.Sell_Permission_Enabled").equalsIgnoreCase("true")){
            if (!p.hasPermission("Options.Sell_Permission")){
                p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Sell_Permission") + "]"));
            }
        } else if (!(sellAllConfig.getConfigurationSection("Items.") == null)){

            // Get the Items config section
            Set<String> items = sellAllConfig.getConfigurationSection("Items").getKeys(false);
            double moneyToGive = 0;

            // Get money to give
            moneyToGive = getMoneyToGive(p, items, moneyToGive);

            // Get Spigot Player
            SpigotPlayer sPlayer = new SpigotPlayer(p);
            ModuleManager modMan = Prison.get().getModuleManager();
            Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );
            PrisonRanks rankPlugin = (PrisonRanks) module;

            // Get money to give + multiplier
            moneyToGive = getMoneyWithMultiplier(moneyToGive, sPlayer, rankPlugin);

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

    private double getMoneyWithMultiplier(double moneyToGive, SpigotPlayer sPlayer, PrisonRanks rankPlugin) {
        if (sellAllConfig.getString("Options.Multiplier_Enabled").equalsIgnoreCase("true")) {

            boolean hasPlayerPrestige = false;
            double multiplier = Double.parseDouble(sellAllConfig.getString("Options.Multiplier_Default"));

            if (rankPlugin != null) {
                if (rankPlugin.getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName()).isPresent()) {
                    String playerRankName;
                    try {
                        playerRankName = rankPlugin.getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName()).get().getRank("prestiges").name;
                    } catch (NullPointerException ex){
                        playerRankName = null;
                    }
                    if (playerRankName != null) {
                        hasPlayerPrestige = true;
                        multiplier = Double.parseDouble(sellAllConfig.getString("Multiplier." + playerRankName + ".MULTIPLIER"));
                        moneyToGive = moneyToGive * multiplier;
                    }
                }
            }
            if (!hasPlayerPrestige) {
                moneyToGive = moneyToGive * multiplier;
            }
        }
        return moneyToGive;
    }

    private double getMoneyToGive(Player p, Set<String> items, double moneyToGive) {

        // Get the player inventory
        Inventory inv = p.getInventory();

        // Get the items from the player inventory and for each of them check the conditions.
        for (ItemStack itemStack : inv.getContents()){

            if (itemStack != null) {
                // Get the items strings from config and for each of them get the Material and value.
                for (String key : items) {
                    String itemMaterial = sellAllConfig.getString("Items." + key + ".ITEM_ID");
                    double value = Double.parseDouble(sellAllConfig.getString("Items." + key + ".ITEM_VALUE"));
                    int amount = 0;

                    // Check if the item from the player inventory's on the config of items sellable
                    // So it gets the amount and then remove it from the inventory
                    if (itemMaterial != null && itemMaterial.equalsIgnoreCase(itemStack.getType().toString())) {
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

        // Old but improved SellAll
        //for (String key : items) {
        //    double amount = 0;
        //    Material item = XMaterial.valueOf(sellAllConfig.getString("Items." + key + ".ITEM_ID")).parseMaterial();
        //
        //    if (item != null) {
        //
        //        ItemStack itemToRemove = new ItemStack(item, 1);
        //
        //        while (p.getInventory().contains(item)) {
        //            p.getInventory().removeItem(itemToRemove);
        //            amount++;
        //        }
        //        moneyToGive = moneyToGive + (Double.parseDouble(sellAllConfig.getString("Items." + key + ".ITEM_VALUE")) * amount);
        //    }
        //}
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
            }

            p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAutoEnabled")));
        }
    }

    @Command(identifier = "sellall gui", description = "SellAll GUI command", onlyPlayers = true)
    private void sellAllGuiCommand(CommandSender sender){

        if (!isEnabled()) return;

        Player p = getSpigotPlayer(sender);

        if (p.isOp() || p.hasPermission("prison.admin")){
            SellAllAdminGUI gui = new SellAllAdminGUI(p);
            gui.open();
            return;
        }

        if (!sellAllConfig.getString("Options.GUI_Enabled").equalsIgnoreCase("true")){
            if (p.isOp() || p.hasPermission("prison.admin")) {
                sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllGUIDisabled")));
                return;
            }
        }

        if (sellAllConfig.getString("Options.GUI_Permission_Enabled").equalsIgnoreCase("true")){
            if (!p.hasPermission(sellAllConfig.getString("Options.GUI_Permission"))){
                p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.GUI_Permission") + "]"));
            } else if (sellAllConfig.getString("Options.Player_GUI_Enabled").equalsIgnoreCase("true")){
                if (sellAllConfig.getString("Options.Player_GUI_Permission_Enabled").equalsIgnoreCase("true")) {
                    if (!p.hasPermission(sellAllConfig.getString("Options.Player_GUI_Permission"))){
                        p.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllMissingPermission") + sellAllConfig.getString("Options.Player_GUI_Permission") + "]"));
                        return;
                    }
                }
                SellAllPlayerGUI gui = new SellAllPlayerGUI(p);
                gui.open();
            }
        }
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
        if (value == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAddPrice")));
            return;
        }

        ItemStack blockAdd = XMaterial.valueOf(itemID).parseItem();
        if (blockAdd == null) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + itemID + ".ITEM_ID", blockAdd.getType().toString());
            conf.set("Items." + itemID + ".ITEM_VALUE", value);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
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
        if (value == null){
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllAddPrice")));
            return;
        }

        ItemStack blockAdd = XMaterial.valueOf(itemID).parseItem();
        if (blockAdd == null) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllWrongID") + " [" + itemID + "]"));
            return;
        }

        try {
            sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            conf = YamlConfiguration.loadConfiguration(sellAllFile);
            conf.set("Items." + itemID + ".ITEM_ID", blockAdd.getType().toString());
            conf.set("Items." + itemID + ".ITEM_VALUE", value);
            conf.save(sellAllFile);
        } catch (IOException e) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Message.SellAllConfigSaveFail")));
            e.printStackTrace();
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

        boolean isInPrestigeLadder = rankPlugin.getLadderManager().getLadder("prestiges").get().containsRank(rankPlugin.getRankManager().getRank(prestige).id);
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

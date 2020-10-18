package tech.mcprison.prison.spigot.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.gui.autofeatures.SpigotAutoBlockGUI;
import tech.mcprison.prison.spigot.gui.autofeatures.SpigotAutoFeaturesGUI;
import tech.mcprison.prison.spigot.gui.autofeatures.SpigotAutoPickupGUI;
import tech.mcprison.prison.spigot.gui.autofeatures.SpigotAutoSmeltGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMineBlockPercentageGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMineInfoGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMineNotificationRadiusGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMineNotificationsGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMineResetTimeGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMinesBlocksGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMinesConfirmGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotLaddersGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotRankManagerGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotRankPriceGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotRankUPCommandsGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotRanksGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminGUI;
import tech.mcprison.prison.spigot.gui.sellall.SellAllPriceGUI;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class ListenersPrisonManager implements Listener {

    private static ListenersPrisonManager instance;
    public static List<String> activeGui = new ArrayList<>();
    public boolean isChatEventActive = false;
    public int id;
    public String rankNameOfChat = null;
    public String mineNameOfChat = null;

    public ListenersPrisonManager(){}


    public static ListenersPrisonManager get() {
        if (instance == null) {
            instance = new ListenersPrisonManager();
        }
        return instance;
    }

    @EventHandler
    public void onGuiClosing(InventoryCloseEvent e){

        if (!(Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prison-gui-enabled")).equalsIgnoreCase("true"))){
            return;
        }

        Player p = (Player) e.getPlayer();

        activeGui.remove(p.getName());
    }

    public void addToGUIBlocker(Player p){
        if(!activeGui.contains(p.getName()))
            activeGui.add(p.getName());
    }

    // On chat event to rename the a Rank Tag
    @EventHandler (priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (isChatEventActive){
            Player p = e.getPlayer();
            String message = e.getMessage();
            Bukkit.getScheduler().cancelTask(id);
            if (rankNameOfChat != null) {
                if (message.equalsIgnoreCase("close")) {
                    isChatEventActive = false;
                    p.sendMessage(SpigotPrison.format("&cRename tag closed, nothing got changed"));
                    e.setCancelled(true);
                } else {
                    Bukkit.getScheduler().runTask(SpigotPrison.getInstance(), () -> Bukkit.getServer().dispatchCommand(p, "ranks set tag " + rankNameOfChat + " " + message));
                    e.setCancelled(true);
                    isChatEventActive = false;
                }
            } else if (mineNameOfChat != null){
                if (message.equalsIgnoreCase("close")) {
                    isChatEventActive = false;
                    p.sendMessage(SpigotPrison.format("&cRename mine closed, nothing got changed"));
                    e.setCancelled(true);
                } else {
                    Bukkit.getScheduler().runTask(SpigotPrison.getInstance(), () -> Bukkit.getServer().dispatchCommand(p, "mines rename " + mineNameOfChat + " " + message));
                    e.setCancelled(true);
                    isChatEventActive = false;
                }
            }
        }
    }

    // Cancel the events of the active GUI opened from the player
    private void activeGuiEventCanceller(Player p, InventoryClickEvent e){
        if(activeGui.contains(p.getName())) {
            e.setCancelled(true);
        }
    }


    // InventoryClickEvent
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e){

        // Check if GUIs are enabled
        if (!(Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prison-gui-enabled")).equalsIgnoreCase("true"))){
            return;
        }

        // Get the player
        Player p = (Player) e.getWhoClicked();

        
        // If you click an empty slot, this should avoid the error.
        // Also if there is no button that was clicked, then it may not be a Prison GUI on click event?
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ||
                e.getCurrentItem().getItemMeta() == null || !e.getCurrentItem().hasItemMeta() || 
                e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            activeGuiEventCanceller(p, e);
            return;
        } else {
            e.getCurrentItem().getItemMeta().getDisplayName();
        }

        // Get action of the Inventory from the event
        InventoryAction action = e.getAction();

        // If an action equals one of these, and the inventory is open from the player equals
        // one of the Prison Title, it'll cancel the event
        if (action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || action.equals(InventoryAction.HOTBAR_SWAP) ||
        	action.equals(InventoryAction.HOTBAR_MOVE_AND_READD) || action.equals(InventoryAction.NOTHING) ||
        	action.equals(InventoryAction.CLONE_STACK) || action.equals(InventoryAction.COLLECT_TO_CURSOR) ||
        	action.equals(InventoryAction.DROP_ONE_SLOT) || action.equals(InventoryAction.DROP_ONE_CURSOR) ||
        	action.equals(InventoryAction.DROP_ALL_SLOT) || action.equals(InventoryAction.DROP_ALL_CURSOR) ||
        	action.equals(InventoryAction.PICKUP_ALL) || action.equals(InventoryAction.PICKUP_HALF) ||
        	action.equals(InventoryAction.PICKUP_ONE) || action.equals(InventoryAction.PICKUP_SOME) ||
        	action.equals(InventoryAction.PLACE_ALL) || action.equals(InventoryAction.PLACE_ONE) ||
        	action.equals(InventoryAction.PLACE_SOME) || action.equals(InventoryAction.SWAP_WITH_CURSOR) ||
        	action.equals(InventoryAction.UNKNOWN)) {
            activeGuiEventCanceller(p, e);
        }

        // ensure the item has itemMeta and a display name
        if (!e.getCurrentItem().hasItemMeta()){
            return;
        }
// WARNING DO NOT USE Objects.requireNonNull() since it will throw a NullPointerException!!
// NEVER should we want that to happen.  If displayName is null then this is not our event 
// and we should not be screwing with it!
//        else {
//            Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
//        }

        // Get the button name
        String buttonNameMain = e.getCurrentItem().getItemMeta().getDisplayName();

        // If the buttonmain have a name longer than 2 characters (should be with colors), it won't take care about the color ids
        if ( buttonNameMain.length() > 2 ) {
        	buttonNameMain = buttonNameMain.substring(2);
        }

        // Split the button name in parts
        String[] parts = buttonNameMain.split(" ");

        // Get ranks module
        Module module = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME ).orElse( null );

        // Get compat
        Compatibility compat = SpigotPrison.getInstance().getCompatibility();

        // Get title
        String title = compat.getGUITitle(e).substring(2);

        // Check if the GUI have the right title and do the actions
        switch (title) {

            // Check the title and do the actions
            case "PrisonManager":

                // Call the method
                PrisonManagerGUI(e, p, buttonNameMain);

                break;

            // Check the title
            case "RanksManager -> Ladders": {

                // Call the method
                LaddersGUI(e, p, buttonNameMain, module);

                break;
            }

            // Check the title of the inventory and do the actions
            case "Ladders -> Ranks": {

                // Call the method
                RanksGUI(e, p, buttonNameMain);

                break;
            }
            // Check the title and do the actions
            case "Prestiges -> PlayerPrestiges": {

                // Call the method
                PlayerPrestigesGUI(e, p, buttonNameMain);

                break;
            }
            // Check the title and do the actions
            case "Prestige -> Confirmation": {

                // Call the method
                PrestigeConfirmationGUI(e, p, buttonNameMain);

                break;
            }
            // Check the title of the inventory and do things
            case "Ranks -> RankManager": {

                // Call the method
                RankManagerGUI(e, p, parts);

                break;
            }
            // Check the title and do the actions
            case "Ranks -> PlayerRanks":{

                // Call the method
                PlayerRanksGUI(e, p, buttonNameMain);

                break;
            }
            // Check the title and do the actions
            case "RankManager -> RankUPCommands": {

                // Call the method
                RankUPCommandsGUI(e, p, buttonNameMain);

                break;
            }
            // Check the inventory name and do the actions
            case "RankManager -> RankPrice": {

                // Call the method
                RankPriceGUI(e, p, parts);

                break;
            }
            // Check the title and do the actions
            case "MinesManager -> Mines": {

                // Call the method
                MinesGUI(e, p, buttonNameMain);

                break;
            }
            // Check the title and do the actions
            case "Mines -> PlayerMines": {

                // Call the method
                PlayerMinesGUI(p, buttonNameMain);

                break;
            }
            case "Mines -> MineInfo": {

                // Call the method
                MineInfoGUI(e, p, parts);

                break;
            }

            // Check the title of the inventory and do the actions
            case "Mines -> Delete": {

                // Call the method
                MinesDeleteGUI(p, parts);

                break;
            }

            // Check the title of the inventory and do the actions
            case "MineInfo -> Blocks": {

                // Call the method
                BlocksGUI(e, p, parts);

                break;
            }

            // Check the inventory name and do the actions
            case "MineInfo -> ResetTime": {

                // Call the method
                ResetTimeGUI(e, p, parts);

                break;
            }

            // Check the inventory title and do the actions
            case "MineInfo -> MineNotifications": {

                // Call the method
                MineNotificationsGUI(e, p, parts);

                break;
            }

            case "MineInfo -> BlockPercentage":{

                mineBlockPercentage(e, p, parts);

                break;
            }

            // Check the inventory title and do the actions
            case "MineNotifications -> Radius": {

                // Call the method
                RadiusGUI(e, p, parts);

                break;
            }
            // Check the inventory title and do the actions
            case "PrisonManager -> AutoFeatures": {

                // Call the method
                AutoFeaturesGUI(e, p, parts);

                break;
            }

            // Check the title and do the actions
            case "AutoFeatures -> AutoPickup":{

                // Call the method
                AutoPickupGUI(e, p, parts);

                break;
            }

            // Check the title and do the actions
            case "AutoFeatures -> AutoSmelt":{

                // Call the method
                AutoSmeltGUI(e, p, parts);

                break;
            }

            // Check the title and do the actions
            case "AutoFeatures -> AutoBlock":{

                // Call the method
                AutoBlockGUI(e, p, parts);

                break;
            }

            // Check the title and do the actions
            case "PrisonManager -> SellAll-Admin":{

                SellAllAdminGUI(e, p, buttonNameMain);

                break;
            }

            // Check the title and do the actions
            case "SellAll -> ItemValue":{

                SellAllItemValue(e, p, parts);

                break;
            }

            // Check the title and do the actions
            case "PrisonManager -> SellAll-Player":{

                p.closeInventory();
                e.setCancelled(true);

                break;
            }
            // Check the title and do the actions
            case "Prison Setup -> Confirmation":{

                Configuration messages = SpigotPrison.getMessagesConfig();

                if (parts[0].equalsIgnoreCase("Confirm:")){
                    Bukkit.dispatchCommand(p, "ranks autoConfigure");
                } else if (parts[0].equalsIgnoreCase("Cancel:")){
                    p.sendMessage(SpigotPrison.format(messages.getString("Setup.Message.Aborted")));
                }
                p.closeInventory();
                e.setCancelled(true);

                break;
            }
        }
    }

    private void mineBlockPercentage(InventoryClickEvent e, Player p, String[] parts) {

        // Rename the parts
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        String part4 = parts[3];

        // Initialize the variable
        double decreaseOrIncreaseValue = 0;

        // If there're enough parts init another variable
        if (parts.length == 5){
            decreaseOrIncreaseValue = Double.parseDouble(parts[4]);
        }

        // Check the button name and do the actions
        if (part1.equalsIgnoreCase("Confirm:")) {

            // Check the click type and do the actions
            if (e.isLeftClick()){

                // Execute the command
                Bukkit.dispatchCommand(p,"mines block set " + part2 + " " + part3 + " " + part4);

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;

                // Check the click type and do the actions
            } else if (e.isRightClick()){

                // Send a message to the player
                p.sendMessage(SpigotPrison.format("&cEvent cancelled."));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            } else {

                // Cancel the event
                e.setCancelled(true);
                return;
            }
        }

        // Give to val a value
        double val = Double.parseDouble(part3);

        // Check the calculator symbol
        if (part4.equals("-")){

            // Check if the value's already too low
            if (!((val -  decreaseOrIncreaseValue) < 0)) {

                // If it isn't too low then decrease it
                val = val - decreaseOrIncreaseValue;

                // If it is too low
            } else {

                // Tell to the player that the value's too low
                p.sendMessage(SpigotPrison.format("&cToo low, under 0%!"));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            }

            // Open an updated GUI after the value changed
            SpigotMineBlockPercentageGUI gui = new SpigotMineBlockPercentageGUI(p, val, part1, part2);
            gui.open();

            // Check the calculator symbol
        } else if (part4.equals("+")) {

            // Check if the value isn't too high
            if (!((val + decreaseOrIncreaseValue) > 100)) {

                // Increase the value
                val = val + decreaseOrIncreaseValue;

                // If the value's too high then do the action
            } else {

                // Close the GUI and tell it to the player
                p.sendMessage(SpigotPrison.format("&cToo high, exceed 100%!"));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            }

            // Open a new updated GUI with new values
            SpigotMineBlockPercentageGUI gui = new SpigotMineBlockPercentageGUI(p, val, part1, part2);
            gui.open();

            // Cancel the event
            e.setCancelled(true);
        }
    }

    private void SellAllItemValue(InventoryClickEvent e, Player p, String[] parts) {

        // Rename the parts
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];

        // Initialize the variable
        int decreaseOrIncreaseValue = 0;

        // If there're enough parts init another variable
        if (parts.length == 4){
            decreaseOrIncreaseValue = Integer.parseInt(parts[3]);
        }

        // Check the button name and do the actions
        if (part1.equalsIgnoreCase("Confirm:")) {

            // Check the click type and do the actions
            if (e.isLeftClick()){

                // Execute the command
                Bukkit.dispatchCommand(p,"sellall edit " + part2 + " " + part3);

                // Close the inventory
                p.closeInventory();

                return;

                // Check the click type and do the actions
            } else if (e.isRightClick()){

                // Send a message to the player
                p.sendMessage(SpigotPrison.format("&cEvent cancelled."));

                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            } else {

                // Cancel the event
                e.setCancelled(true);
                return;
            }
        }

        // Give to val a value
        double val = Double.parseDouble(part2);

        // Check the calculator symbol
        if (part3.equals("-")){

            // Check if the value's already too low
            if (!((val -  decreaseOrIncreaseValue) < 0)) {

                // If it isn't too low then decrease it
                val = val - decreaseOrIncreaseValue;

                // If it is too low
            } else {

                // Tell to the player that the value's too low
                p.sendMessage(SpigotPrison.format("&cToo low value."));

                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();
                return;
            }

            // Open an updated GUI after the value changed
            SellAllPriceGUI gui = new SellAllPriceGUI(p, val, part1);
            gui.open();

            // Check the calculator symbol
        } else if (part3.equals("+")){

            // Check if the value isn't too high
            if (!((val + decreaseOrIncreaseValue) > 2147483646)) {

                // Increase the value
                val = val + decreaseOrIncreaseValue;

                // If the value's too high then do the action
            } else {

                // Close the GUI and tell it to the player
                p.sendMessage(SpigotPrison.format("&cToo high value."));
                e.setCancelled(true);
                p.closeInventory();
                return;
            }

            // Open a new updated GUI with new values
            SellAllPriceGUI gui = new SellAllPriceGUI(p, val, part1);
            gui.open();

        }
    }

    private void SellAllAdminGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        if (e.isRightClick()){

            Bukkit.dispatchCommand(p, "sellall delete " + buttonNameMain);
            p.closeInventory();

        } else if (e.isLeftClick()){

            File file = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(file);

            SellAllPriceGUI gui = new SellAllPriceGUI(p,Double.parseDouble(Objects.requireNonNull(conf.getString("Items." + buttonNameMain + ".ITEM_VALUE"))), buttonNameMain);
            gui.open();

        }

        e.setCancelled(true);
    }

    private void PrisonManagerGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        // Check the Item display name and do open the right GUI
        switch (buttonNameMain) {
            case "Ranks": {
                SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
                gui.open();
                break;
            }

            // Check the Item display name and do open the right GUI
            case "AutoManager": {
                SpigotAutoFeaturesGUI gui = new SpigotAutoFeaturesGUI(p);
                gui.open();
                break;
            }

            // Check the Item display name and do open the right GUI
            case "Mines": {
                SpigotMinesGUI gui = new SpigotMinesGUI(p);
                gui.open();
                break;
            }

            // Check the Item display name and do open the right GUI
            case "SellAll": {
                SellAllAdminGUI gui = new SellAllAdminGUI(p);
                gui.open();
                break;
            }
        }

        // Cancel the event
        e.setCancelled(true);
    }

    private void LaddersGUI(InventoryClickEvent e, Player p, String buttonNameMain, Module module) {

        // Check if the Ranks module's loaded
        if(!(module instanceof PrisonRanks)){
            p.sendMessage(SpigotPrison.format("&cThe GUI can't open because the &3Rank module &cisn't loaded"));
            p.closeInventory();
            e.setCancelled(true);
            return;
        }

        // Get the ladder by the name of the button got before
        Optional<RankLadder> ladder = PrisonRanks.getInstance().getLadderManager().getLadder(buttonNameMain);

        // Check if the ladder exist, everything can happen but this shouldn't
        if (!ladder.isPresent()) {
            p.sendMessage("What did you actually click? Sorry ladder not found.");
            return;
        }

        // When the player click an item with shift and right click, e.isShiftClick should be enough but i want
        // to be sure's a right click
        if (e.isShiftClick() && e.isRightClick()) {

            // Execute the command
            Bukkit.dispatchCommand(p, "ranks ladder delete " + buttonNameMain);
            e.setCancelled(true);
            p.closeInventory();
            SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
            gui.open();
            return;

        }

        // Open the GUI of ranks
        SpigotRanksGUI gui = new SpigotRanksGUI(p, ladder);
        gui.open();

        // Cancel the event
        e.setCancelled(true);
    }

    private void RanksGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        // Get the rank
        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRankOptional(buttonNameMain);

        // Check if the rank exist
        if (!rankOptional.isPresent()) {
            p.sendMessage(SpigotPrison.format("&cThe rank " + buttonNameMain + " does not exist."));
            return;
        }

        // Get the rank
        Rank rank = rankOptional.get();

        // Check clicks
        if (e.isShiftClick() && e.isRightClick()) {

            // Execute the command
            Bukkit.dispatchCommand(p, "ranks delete " + buttonNameMain);
            e.setCancelled(true);
            p.closeInventory();
            return;

        } else {

            // Open a GUI
            SpigotRankManagerGUI gui = new SpigotRankManagerGUI(p, rank);
            gui.open();

        }

        // Cancel the event
        e.setCancelled(true);
    }

    private void PlayerPrestigesGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        // Check the button name and do the actions
        if (buttonNameMain.equalsIgnoreCase("Prestige")){
            // Close the inventory
            p.closeInventory();
            // Execute the command
            Bukkit.dispatchCommand(p, "prestige");
        }

        // Cancel the event
        e.setCancelled(true);
    }

    private void PrestigeConfirmationGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        // Check the button name and do the actions
        if (buttonNameMain.equalsIgnoreCase("Confirm: Prestige")){
            // Execute the command
            Bukkit.dispatchCommand(p, "rankup prestiges");
            // Close the inventory
            p.closeInventory();
        } else if (buttonNameMain.equalsIgnoreCase("Cancel: Don't Prestige")){
            // Send a message to the player
            p.sendMessage(SpigotPrison.format("&7[&3Info&7] &cCancelled"));
            // Close the inventory
            p.closeInventory();
        }

        // Cancel the event
        e.setCancelled(true);
    }

    private void RankManagerGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Output finally the buttonname and the minename explicit out of the array
        String buttonname = parts[0];
        String rankName = parts[1];

        // Get the rank
        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRankOptional(rankName);

        // Check the button name and do the actions
        if (buttonname.equalsIgnoreCase("RankupCommands")){

            // Check if the rank exist
            if (!rankOptional.isPresent()) {
                // Send a message to the player
                p.sendMessage(SpigotPrison.format("&c[ERROR] The rank " + rankName + " does not exist."));
                return;
            }

            // Get the rank
            Rank rank = rankOptional.get();

            // Check the rankupCommand of the Rank
            if (rank.rankUpCommands == null) {
                // Send a message to the player
                p.sendMessage(SpigotPrison.format("&c[ERROR] There aren't commands for this rank anymore."));
            }

            // Open the GUI of commands
            else {
                SpigotRankUPCommandsGUI gui = new SpigotRankUPCommandsGUI(p, rank);
                gui.open();
            }

        // Check the button name and do the actions
        } else if (buttonname.equalsIgnoreCase("RankPrice")){

            // Check and open a GUI
            if(rankOptional.isPresent()) {
                SpigotRankPriceGUI gui = new SpigotRankPriceGUI(p, (int) rankOptional.get().cost, rankOptional.get().name);
                gui.open();
            }

        // Check the button name and do the actions
        } else if (buttonname.equalsIgnoreCase("RankTag")){

            Configuration messages = SpigotPrison.getMessagesConfig();

            // Send messages to the player
            p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.rankTagRename")));
            p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.rankTagRenameClose")));
            // Start the async task
            isChatEventActive = true;
            rankNameOfChat = rankName;
            id = Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> {
                isChatEventActive = false;
                p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.OutOfTimeNoChanges")));
            }, 20L * 30);
            p.closeInventory();
        }

        // Cancel the event
        e.setCancelled(true);
    }

    private void PlayerRanksGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Check the buttonName and do the actions
        if (buttonNameMain.equals(SpigotPrison.format(Objects.requireNonNull(GuiConfig.getString("Gui.Lore.Rankup")).substring(2)))){
            Bukkit.dispatchCommand(p, "rankup " + GuiConfig.getString("Options.Ranks.Ladder"));
            p.closeInventory();
        }

        // Cancel the event
        e.setCancelled(true);
    }

    private void RankUPCommandsGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        // Check the clickType
        if (e.isShiftClick() && e.isRightClick()) {

            // Execute the command
            Bukkit.dispatchCommand(p, "ranks command remove " + buttonNameMain);
            // Cancel the event
            e.setCancelled(true);
            // Close the inventory
            p.closeInventory();
            return;

        }

        // Cancel the event
        e.setCancelled(true);
    }

    private void RankPriceGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Rename the parts
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];

        // Initialize the variable
        int decreaseOrIncreaseValue = 0;

        // If there're enough parts init another variable
        if (parts.length == 4){
            decreaseOrIncreaseValue = Integer.parseInt(parts[3]);
        }

        // Check the button name and do the actions
        if (part1.equalsIgnoreCase("Confirm:")) {

            // Check the click type and do the actions
            if (e.isLeftClick()){

                // Execute the command
                Bukkit.dispatchCommand(p,"ranks set cost " + part2 + " " + part3);

                // Close the inventory
                p.closeInventory();

                return;

                // Check the click type and do the actions
            } else if (e.isRightClick()){

                // Send a message to the player
                p.sendMessage(SpigotPrison.format("&cEvent cancelled."));

                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            } else {

                // Cancel the event
                e.setCancelled(true);
                return;
            }
        }

        // Give to val a value
        int val = Integer.parseInt(part2);

        // Check the calculator symbol
        if (part3.equals("-")){

            // Check if the value's already too low
            if (!((val -  decreaseOrIncreaseValue) < 0)) {

                // If it isn't too low then decrease it
                val = val - decreaseOrIncreaseValue;

                // If it is too low
            } else {

                // Tell to the player that the value's too low
                p.sendMessage(SpigotPrison.format("&cToo low value."));

                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();
                return;
            }

            // Open an updated GUI after the value changed
            SpigotRankPriceGUI gui = new SpigotRankPriceGUI(p, val, part1);
            gui.open();

            // Check the calculator symbol
        } else if (part3.equals("+")){

            // Check if the value isn't too high
            if (!((val + decreaseOrIncreaseValue) > 2147483646)) {

                // Increase the value
                val = val + decreaseOrIncreaseValue;

                // If the value's too high then do the action
            } else {

                // Close the GUI and tell it to the player
                p.sendMessage(SpigotPrison.format("&cToo high value."));
                e.setCancelled(true);
                p.closeInventory();
                return;
            }

            // Open a new updated GUI with new values
            SpigotRankPriceGUI gui = new SpigotRankPriceGUI(p, val, part1);
            gui.open();

        }
    }

    private void MinesGUI(InventoryClickEvent e, Player p, String buttonNameMain) {

        // Variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(buttonNameMain);

        // Check the clicks
        if (e.isShiftClick() && e.isRightClick()) {
            // Execute the command
            Bukkit.dispatchCommand(p, "mines delete " + buttonNameMain);
            // Cancel the event
            e.setCancelled(true);
            // Close the inventory
            p.closeInventory();
            // Open a GUI
            SpigotMinesConfirmGUI gui = new SpigotMinesConfirmGUI(p, buttonNameMain);
            gui.open();
            return;
        }

        // Open the GUI of mines info
        SpigotMineInfoGUI gui = new SpigotMineInfoGUI(p, m, buttonNameMain);
        gui.open();

        // Cancel the event
        e.setCancelled(true);
    }

    private void PlayerMinesGUI(Player p, String buttonNameMain) {

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();
        String permission = SpigotPrison.format(GuiConfig.getString("Options.Mines.PermissionWarpPlugin"));

        if (p.hasPermission(permission + buttonNameMain) || p.hasPermission(permission.substring(0, permission.length() - 1))){
            Bukkit.dispatchCommand(p, SpigotPrison.format(GuiConfig.getString("Options.Mines.CommandWarpPlugin") + " " + buttonNameMain));
        }
    }

    private void MineInfoGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Output finally the buttonName and the mineName explicit out of the array
        String buttonName = parts[0];
        String mineName = parts[1];

        // Check the name of the button and do the actions
        switch (buttonName) {
            case "Blocks_of_the_Mine:":

                // Open the GUI
                SpigotMinesBlocksGUI gui = new SpigotMinesBlocksGUI(p, mineName);
                gui.open();

                break;

            // Check the name of the button and do the actions
            case "Reset_Mine:":

                // Check the clickType and do the actions
                if (e.isLeftClick()) {
                    // Execute the command
                    Bukkit.dispatchCommand(p, "mines reset " + mineName);
                } else if (e.isRightClick()){
                    // Execute the command
                    Bukkit.dispatchCommand(p, "mines set skipReset " + mineName);
                } else if (e.isRightClick() && e.isShiftClick()){
                    // Execute the command
                    Bukkit.dispatchCommand(p, "mines set zeroBlockResetDelay " + mineName);
                }

                // Cancel the event
                e.setCancelled(true);

                break;

            // Check the name of the button and do the actions
            case "Mine_Spawn:":

                // Execute the command
                Bukkit.dispatchCommand(p, "mines set spawn " + mineName);

                // Cancel the event
                e.setCancelled(true);
                break;

            // Check the name of the button and do the actions
            case "Mine_notifications:":

                // Open the GUI
                SpigotMineNotificationsGUI gui1 = new SpigotMineNotificationsGUI(p, mineName);
                gui1.open();

                break;

            // Check the name of the button and do the actions
            case "TP_to_the_Mine:":

                // Close the inventory
                p.closeInventory();

                // Execute the Command
                Bukkit.dispatchCommand(p, "mines tp " + mineName);

                break;

            // Check the name of the button and do the actions
            case "Reset_Time:":

                // Initialize the variables
                PrisonMines pMines = PrisonMines.getInstance();
                Mine m = pMines.getMine(mineName);
                int val = m.getResetTime();

                // Open the GUI
                SpigotMineResetTimeGUI gui2 = new SpigotMineResetTimeGUI(p, val, mineName);
                gui2.open();

                break;
            case "Mine_Name": {

                Configuration messages = SpigotPrison.getMessagesConfig();

                // Send messages to the player
                p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.mineNameRename")));
                p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.mineNameRenameClose")));
                // Start the async task
                isChatEventActive = true;
                mineNameOfChat = mineName;
                id = Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> {
                    isChatEventActive = false;
                    p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.OutOfTimeNoChanges")));
                }, 20L * 30);
                p.closeInventory();

                break;
            }
        }
    }

    private void MinesDeleteGUI(Player p, String[] parts) {

        // Output finally the buttonname and the minename explicit out of the array
        String buttonname = parts[0];
        String mineName = parts[1];

        // Check the name of the button and do the actions
        if (buttonname.equals("Confirm:")) {

            // Confirm
            Bukkit.dispatchCommand(p, "mines delete " + mineName + " confirm");

            // Close the Inventory
            p.closeInventory();

        // Check the name of the button and do the actions
        } else if (buttonname.equals("Cancel:")) {

            // Cancel
            Bukkit.dispatchCommand(p, "mines delete " + mineName + " cancel");

            // Close the inventory
            p.closeInventory();

        }
    }

    private void BlocksGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Output finally the buttonname and the minename explicit out of the array
        String buttonname = parts[0];
        String mineName = parts[1];
        double percentage = Double.parseDouble(parts[2]);

        // Check the click Type and do the actions
        if (e.isShiftClick() && e.isRightClick()) {

            // Execute the command
            Bukkit.dispatchCommand(p, "mines block remove " + mineName + " " + buttonname);

            // Cancel the event
            e.setCancelled(true);

            // Close the GUI so it can be updated
            p.closeInventory();

            // Open the GUI
            SpigotMinesBlocksGUI gui = new SpigotMinesBlocksGUI(p, mineName);
            gui.open();
        } else {

            SpigotMineBlockPercentageGUI gui = new SpigotMineBlockPercentageGUI(p, percentage, mineName, buttonname);
            gui.open();

        }
    }

    private void ResetTimeGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Rename the parts
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];

        // Initialize the variable
        int decreaseOrIncreaseValue = 0;

        // If there're enough parts init another variable
        if (parts.length == 4){
            decreaseOrIncreaseValue = Integer.parseInt(parts[3]);
        }

        // Check the button name and do the actions
        if (part1.equalsIgnoreCase("Confirm:")) {

            // Check the click type and do the actions
            if (e.isLeftClick()){

                // Execute the command
                Bukkit.dispatchCommand(p,"mines set resettime " + part2 + " " + part3);

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;

            // Check the click type and do the actions
            } else if (e.isRightClick()){

                // Send a message to the player
                p.sendMessage(SpigotPrison.format("&cEvent cancelled."));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            } else {

                // Cancel the event
                e.setCancelled(true);
                return;
            }
        }

        // Give to val a value
        int val = Integer.parseInt(part2);

        // Check the calculator symbol
        if (part3.equals("-")){

            // Check if the value's already too low
            if (!((val -  decreaseOrIncreaseValue) < 0)) {

                // If it isn't too low then decrease it
                val = val - decreaseOrIncreaseValue;

            // If it is too low
            } else {

                // Tell to the player that the value's too low
                p.sendMessage(SpigotPrison.format("&cToo low value."));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            }

            // Open an updated GUI after the value changed
            SpigotMineResetTimeGUI gui = new SpigotMineResetTimeGUI(p, val, part1);
            gui.open();

        // Check the calculator symbol
        } else if (part3.equals("+")){

            // Check if the value isn't too high
            if (!((val + decreaseOrIncreaseValue) > 999999)) {

                // Increase the value
                val = val + decreaseOrIncreaseValue;

            // If the value's too high then do the action
            } else {

                // Close the GUI and tell it to the player
                p.sendMessage(SpigotPrison.format("&cToo high value."));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            }

            // Open a new updated GUI with new values
            SpigotMineResetTimeGUI gui = new SpigotMineResetTimeGUI(p, val, part1);
            gui.open();

            // Cancel the event
            e.setCancelled(true);

        }
    }

    private void MineNotificationsGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Output finally the buttonname and the minename explicit out of the array
        String buttonname = parts[0];
        String mineName = parts[1];
        String typeNotification;
        long val;

        // Init variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);

        // Check the button name and do the actions
        if (buttonname.equalsIgnoreCase("Within_Mode:")){

            // Change the value of the variable
            typeNotification = "within";

            // Execute command
            Bukkit.dispatchCommand(p, "mines set notification " + mineName + " " + typeNotification + " " + "0");

            // Cancel the event and close the inventory
            e.setCancelled(true);
            p.closeInventory();

            // Check the button name and do the actions
        } else if (buttonname.equalsIgnoreCase("Radius_Mode:")){

            // Change the value of the variable
            typeNotification = "radius";

            // Get the variable value
            val = m.getNotificationRadius();

            // Open the GUI
            SpigotMineNotificationRadiusGUI gui = new SpigotMineNotificationRadiusGUI(p, val,  typeNotification, mineName);
            gui.open();

        // Check the button name and do the actions
        } else if (buttonname.equalsIgnoreCase("Disabled_Mode:")){

            // Change the value of the variable
            typeNotification = "disabled";

            // Execute the command
            Bukkit.dispatchCommand(p, "mines set notification " + mineName + " " + typeNotification + " " + "0");

            // Cancel the event and close the inventory
            e.setCancelled(true);
            p.closeInventory();

        }
    }

    private void RadiusGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Rename the variables
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        String typeNotification;

        // Init the variable
        int decreaseOrIncreaseValue = 0;

        // Check the button name and do the actions
        if (!(part1.equalsIgnoreCase("Confirm:"))){

            // Give them a value
            decreaseOrIncreaseValue = Integer.parseInt(parts[3]);
            typeNotification = parts[4];

        // Do others actions
        } else {

            // Give it a value
            typeNotification = parts[3];
        }

        // Check the button name and do the actions
        if (part1.equalsIgnoreCase("Confirm:")) {

            // Check the click type
            if (e.isLeftClick()){

                // Execute the command
                Bukkit.dispatchCommand(p,"mines set notification " + part2 + " " + typeNotification + " " + part3);

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            } else if (e.isRightClick()){

                // Close the inventory
                p.sendMessage(SpigotPrison.format("&cEvent cancelled."));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();

                return;
            } else {

                // Cancel the event
                e.setCancelled(true);
                return;
            }
        }

        // Init a new value
        long val = Integer.parseInt(part2);

        // Check the calculator symbol
        if (part3.equals("-")){

            // Check if the value's too low
            if (!((val -  decreaseOrIncreaseValue) < 0)) {

                // Decrease the value
                val = val - decreaseOrIncreaseValue;

            // If the value's too low
            } else {

                // Close the inventory and tell it the player
                p.sendMessage(SpigotPrison.format("&cToo low value."));

                // Cancel the event
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();
                return;
            }

            // Open a new updated GUI with new values
            SpigotMineNotificationRadiusGUI gui = new SpigotMineNotificationRadiusGUI(p, val,  typeNotification, part1);
            gui.open();

        // Check the calculator symbol
        } else if (part3.equals("+")){

            // Check if the value's too high
            if (!((val + decreaseOrIncreaseValue) > 9999999)) {

                // Increase the value
                val = val + decreaseOrIncreaseValue;

            // If the value's too high
            } else {

                // Close the inventory and tell it to the player
                p.sendMessage(SpigotPrison.format("&cToo high value."));

                // Cancel the inventory
                e.setCancelled(true);

                // Close the inventory
                p.closeInventory();
                return;
            }

            // Open a new updated GUI with new values
            SpigotMineNotificationRadiusGUI gui = new SpigotMineNotificationRadiusGUI(p, val,  typeNotification, part1);
            gui.open();

        }
    }

    private void AutoFeaturesGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Get the config
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();

        // Output finally the buttonname and the mode explicit out of the array
        String buttonname = parts[0];
        String mode = parts[1];
        
        boolean enabled = mode.equalsIgnoreCase("Enabled");

        // Check the clickType and do the actions
        if ( enabled && e.isRightClick() && e.isShiftClick() ||
                !enabled && e.isRightClick()){

            if (buttonname.equalsIgnoreCase("Full_Inv_Play_Sound")){
                afConfig.setFeature( AutoFeatures.playSoundIfInventoryIsFull, !enabled );
                saveConfigAutoFeatures(e, p);
            }

            if (buttonname.equalsIgnoreCase("Full_Inv_Hologram")){
                afConfig.setFeature(AutoFeatures.hologramIfInventoryIsFull, !enabled);
                saveConfigAutoFeatures(e,p);
            }

            if (buttonname.equalsIgnoreCase("All")){
                afConfig.setFeature(AutoFeatures.isAutoManagerEnabled, !enabled);
                saveConfigAutoFeatures(e,p);
            }

        }

        // Check the clickType and do the actions
        if (enabled && e.isRightClick() && e.isShiftClick() || !enabled && e.isRightClick() || enabled && e.isLeftClick()){
            if (buttonname.equalsIgnoreCase("AutoPickup")){
                if (e.isLeftClick()){
                    SpigotAutoPickupGUI gui = new SpigotAutoPickupGUI(p);
                    gui.open();
                    return;
                }
                afConfig.setFeature(AutoFeatures.autoPickupEnabled, !enabled);
                saveConfigAutoFeatures(e,p);
            }

            if (buttonname.equalsIgnoreCase("AutoSmelt")){
                if (e.isLeftClick()){
                    SpigotAutoSmeltGUI gui = new SpigotAutoSmeltGUI(p);
                    gui.open();
                    return;
                }
                afConfig.setFeature(AutoFeatures.autoSmeltEnabled, !enabled);
                saveConfigAutoFeatures(e,p);
            }

            if (buttonname.equalsIgnoreCase("AutoBlock")){
                if (e.isLeftClick()){
                    SpigotAutoBlockGUI gui = new SpigotAutoBlockGUI(p);
                    gui.open();
                    return;
                }
                afConfig.setFeature(AutoFeatures.autoBlockEnabled, !enabled);
                saveConfigAutoFeatures(e,p);
            }
        }
    }

    private void AutoPickupGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Get the config
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();

        // Output finally the buttonname and the mode explicit out of the array
        String buttonname = parts[0];
        String mode = parts[1];

        boolean enabled = mode.equalsIgnoreCase("Enabled");
        
        // Check the click and do the actions, also the buttonName
        if ( enabled && e.isRightClick() && e.isShiftClick() ||
        		!enabled && e.isRightClick() ){
        	
        	if (buttonname.equalsIgnoreCase("All_Blocks")){
        		afConfig.setFeature( AutoFeatures.autoPickupAllBlocks, !enabled );
        		saveConfigPickup(e, p);
        	}

        	if (buttonname.equalsIgnoreCase("Cobblestone")){
        	    afConfig.setFeature(AutoFeatures.autoPickupCobbleStone, !enabled);
        	    saveConfigPickup(e,p);
            }
        	
        	if (buttonname.equalsIgnoreCase("Gold_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupGoldOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Iron_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupIronOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Coal_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupCoalOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Diamond_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupDiamondOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Redstone_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupRedStoneOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Emerald_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupEmeraldOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Quartz_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupQuartzOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Lapis_Ore")){
        		afConfig.setFeature( AutoFeatures.autoPickupLapisOre, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Snow_Ball")){
        		afConfig.setFeature( AutoFeatures.autoPickupSnowBall, !enabled );
        		saveConfigPickup(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Glowstone_Dust")){
        		afConfig.setFeature( AutoFeatures.autoPickupGlowstoneDust, !enabled );
        		saveConfigPickup(e, p);
        	}
        }


    }

    private void AutoSmeltGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Get the config
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();

        // Output finally the buttonname and the mode explicit out of the array
        String buttonname = parts[0];
        String mode = parts[1];
        
        boolean enabled = mode.equalsIgnoreCase("Enabled");

        // Check the clickType and do the actions
        if ( enabled && e.isRightClick() && e.isShiftClick() ||
        		!enabled && e.isRightClick()){
        	
        	if (buttonname.equalsIgnoreCase("Gold_Ore")){
        		afConfig.setFeature( AutoFeatures.autoSmeltGoldOre, !enabled );
        		saveConfigSmelt(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Iron_Ore")){
        		afConfig.setFeature( AutoFeatures.autoSmeltIronOre, !enabled );
        		saveConfigSmelt(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("All_Ores")){
        		afConfig.setFeature( AutoFeatures.autoSmeltAllBlocks, !enabled );
        		saveConfigSmelt(e, p);
        	}
        	
        }
    }

    private void AutoBlockGUI(InventoryClickEvent e, Player p, String[] parts) {

        // Get the config
        AutoFeaturesFileConfig afConfig = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();

        // Output finally the buttonname and the mode explicit out of the array
        String buttonname = parts[0];
        String mode = parts[1];

        boolean enabled = mode.equalsIgnoreCase("Enabled");

        // Check the clickType and do the actions
        if ( enabled && e.isRightClick() && e.isShiftClick() ||
        		!enabled && e.isRightClick()){
        	
        	if (buttonname.equalsIgnoreCase("Gold_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockGoldBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Iron_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockIronBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Coal_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockCoalBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Diamond_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockDiamondBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Redstone_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockRedstoneBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Emerald_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockEmeraldBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Quartz_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockQuartzBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Prismarine_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockPrismarineBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Lapis_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockLapisBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Snow_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockSnowBlock, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("Glowstone_Block")){
        		afConfig.setFeature( AutoFeatures.autoBlockGlowstone, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        	if (buttonname.equalsIgnoreCase("All_Blocks")){
        		afConfig.setFeature( AutoFeatures.autoBlockAllBlocks, !enabled );
        		saveConfigBlock(e, p);
        	}
        	
        }

    }


    /**
     * Save the auto features, and then cancel the event and close the inventory.
     * 
     * @param e
     * @param player
     */
    private boolean saveAutoFeatures( InventoryClickEvent e, Player player ) {
    	boolean success = SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig().saveConf();
    	e.setCancelled(true);
    	player.closeInventory();
    	return success;
    }

    
    private boolean saveConfigBlock(InventoryClickEvent e, Player p) {
    	boolean success = saveAutoFeatures( e, p );
        SpigotAutoBlockGUI gui = new SpigotAutoBlockGUI(p);
        gui.open();
        return success;
    }

    private boolean saveConfigSmelt(InventoryClickEvent e, Player p) {
    	boolean success = saveAutoFeatures( e, p );
        SpigotAutoSmeltGUI gui = new SpigotAutoSmeltGUI(p);
        gui.open();
        return success;
    }

    private boolean saveConfigPickup(InventoryClickEvent e, Player p) {
    	boolean success = saveAutoFeatures( e, p );
        SpigotAutoPickupGUI gui = new SpigotAutoPickupGUI(p);
        gui.open();
        return success;
    }

    private boolean saveConfigAutoFeatures(InventoryClickEvent e, Player p) {
    	boolean success = saveAutoFeatures( e, p );
        SpigotAutoFeaturesGUI gui = new SpigotAutoFeaturesGUI(p);
        gui.open();
        return success;
    }
}

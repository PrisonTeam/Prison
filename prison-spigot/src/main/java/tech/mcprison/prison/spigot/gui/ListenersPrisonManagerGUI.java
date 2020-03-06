package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListenersPrisonManagerGUI implements Listener {

    SpigotPrison plugin;
    public List <String> activeGui = new ArrayList<String>();


    public ListenersPrisonManagerGUI(){}
    public ListenersPrisonManagerGUI(SpigotPrison instance){
        plugin = instance;
    }

    @EventHandler
    public void onGuiClosing(InventoryCloseEvent e){

        Player p = (Player) e.getPlayer();

        if(activeGui.contains(p.getName()))
            activeGui.remove(p.getName());
    }


    public void addToGUIBlocker(Player p){
        if(!activeGui.contains(p.getName()))
            activeGui.add(p.getName());
    }

    public void removeFromGUIBlocker(Player p){
        if(activeGui.contains(p.getName()))
            activeGui.remove(p.getName());
    }

    public boolean invalidClick(Player player, InventoryClickEvent event){
        if(activeGui.contains(player.getName()))
            if(event.getSlot() == -999 // Checks if player clicks outside the inventory
                    || event.getCurrentItem() == null // Checks for invalid item
                    || event.getCurrentItem().getType() == Material.AIR) // Checks for clicking empty slot
                return true;
        return false;
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e){
        Player p = (Player) e.getPlayer();

        if (e.getView().getTitle().equals("§3" + "PrisonManager") ||
                e.getView().getTitle().equals("§3" + "RanksManager -> Ladders") ||
                e.getView().getTitle().equals("§3" + "Ladders -> Ranks") ||
                e.getView().getTitle().equals("§3" + "Ranks -> RankUPCommands") ||
                e.getView().getTitle().equals("§3" + "MinesManager -> Mines") ||
                e.getView().getTitle().equals("§3" + "Mines -> MineInfo") ||
                e.getView().getTitle().equals("§3" + "Mines -> Delete") ||
                e.getView().getTitle().equals("§3" + "MineInfo -> Blocks")){

            // Add the player to the list of those who can't move items in the inventory
            addToGUIBlocker(p);

        }
    }

    @EventHandler
    public void onDragEvent(InventoryDragEvent e){
        Player p = (Player) e.getWhoClicked();

        // If you click an empty slot, this should avoid the error
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent e){
        Player p = (Player) e.getHandlers();

        // If you click an empty slot, this should avoid the error
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onInteractEvent(InventoryInteractEvent e){

        Player p = (Player) e.getWhoClicked();

            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onPickupItem(InventoryPickupItemEvent e){
        Player p = (Player) e.getHandlers();

        if(activeGui.contains(p.getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreativeEvent(InventoryCreativeEvent e){

        Player p = (Player) e.getWhoClicked();

        // If you click an empty slot, this should avoid the error
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();

        // If you click an empty slot, this should avoid the error
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
            return;
        }

        // check if the item has itemMeta
        if (!(e.getCurrentItem().hasItemMeta())){
            return;
        }

        // Check if the GUI have the right title and do the actions
        switch (e.getView().getTitle()) {
            case "§3" + "PrisonManager":

                // Check the Item display name and do open the right GUI
                if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Ranks")) {
                    SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
                    gui.open();
                }

                // Check the Item display name and do open the right GUI
                else if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Prison Tasks")) {
                    e.setCancelled(true);
                    return;
                }

                // Check the Item display name and do open the right GUI
                else if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Mines")) {
                    SpigotMinesGUI gui = new SpigotMinesGUI(p);
                    gui.open();
                }

                e.setCancelled(true);

                // Check if the GUI have the right title and do the actions
                break;
            case "§3" + "RanksManager -> Ladders": {

                // Get the ladder name or the button name
                String ladderName = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Get the ladder by the name of the button got before
                Optional<RankLadder> ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

                // Check if the ladder exist, everything can happen but this shouldn't
                if (!ladder.isPresent()) {
                    p.sendMessage("What did you actually click? Sorry ladder not found.");
                    return;
                }

                // When the player click an item with shift and right click, e.isShiftClick should be enough but i want
                // to be sure's a right click
                if (e.isShiftClick() && e.isRightClick()) {

                    // Execute the command
                    Bukkit.dispatchCommand(p, "ranks ladder delete " + ladderName);
                    p.closeInventory();
                    SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
                    gui.open();
                    return;

                }

                // Open the GUI of ranks
                SpigotRanksGUI gui = new SpigotRanksGUI(p, ladder);
                gui.open();

                // Check the title of the inventory and do the actions
                break;
            }
            case "§3" + "Ladders -> Ranks":

                // Get the rank name or the button name
                String rankName = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Get the rank
                Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);

                // Check if the rank exist
                if (!rankOptional.isPresent()) {
                    p.sendMessage("§cThe rank " + rankName + " does not exist.");
                    return;
                }

                Rank rank = rankOptional.get();

                if (e.isShiftClick() && e.isRightClick()) {

                    Bukkit.dispatchCommand(p, "ranks delete " + rankName);
                    p.closeInventory();

                } else if (rank.rankUpCommands == null) {

                    p.sendMessage("§cThere aren't commands for this rank anymore");

                }

                // Open the GUI of commands
                else {

                    SpigotRankUPCommandsGUI gui = new SpigotRankUPCommandsGUI(p, rank);
                    gui.open();

                }

                // Check the title of the inventory and do things
                break;
            case "§3" + "Ranks -> RankUPCommands":

                String command = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                if (e.isShiftClick() && e.isRightClick()) {

                    Bukkit.dispatchCommand(p, "ranks command remove " + command);
                    p.closeInventory();

                }

                e.setCancelled(true);

                // Check the title of the inventory and do the actions
                break;
            case "§3" + "MinesManager -> Mines": {

                // Mine name or title of the item
                String minename = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Variables
                @SuppressWarnings("unused")
                Optional<Mine> mine = PrisonMines.getInstance().getMineManager().getMine(minename);
                PrisonMines pMines = PrisonMines.getInstance();
                Mine m = pMines.getMineManager().getMine(minename).get();

                // Check the clicks
                if (e.isShiftClick() && e.isRightClick()) {

                    Bukkit.dispatchCommand(p, "mines delete " + minename);
                    p.closeInventory();
                    SpigotMinesConfirmGUI gui = new SpigotMinesConfirmGUI(p, minename);
                    gui.open();
                    return;

                }

                // Open the GUI of mines info
                SpigotMineInfoGUI gui = new SpigotMineInfoGUI(p, m, minename);
                gui.open();

                e.setCancelled(true);

                // Check the title of the inventory and do the actions
                break;
            }
            case "§3" + "Mines -> MineInfo": {

                // Get the button name without colors but with the minename too
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the minename
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String mineName = parts[1];

                // Check the name of the button and do the actions
                switch (buttonname) {
                    case "Blocks_of_the_Mine:":

                        // Open the GUI
                        SpigotMinesBlocksGUI gui = new SpigotMinesBlocksGUI(p, mineName);
                        gui.open();

                        break;

                    // Check the name of the button and do the actions
                    case "Reset_Mine:":

                        // Execute the command
                        Bukkit.dispatchCommand(p, "mines reset " + mineName);

                        e.setCancelled(true);

                        break;
                    case "MineSpawn:":

                        // Execute the command
                        Bukkit.dispatchCommand(p, "mines set spawn " + mineName);

                        e.setCancelled(true);
                        // Check the name of the button and do the actions
                        break;
                    case "TP_to_the_Mine:":

                        // Close the inventory
                        p.closeInventory();

                        // Execute the Command
                        Bukkit.dispatchCommand(p, "mines tp " + mineName);

                        break;
                }

                // Check the title of the inventory and do the actions
                break;
            }
            case "§3" + "Mines -> Delete": {

                // Get the button name without colors but with the minename too
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the minename
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String mineName = parts[1];

                if (buttonname.equals("Confirm:")) {

                    // Confirm
                    Bukkit.dispatchCommand(p, "mines delete " + mineName + " confirm");

                    // Close the Inventory
                    p.closeInventory();

                } else if (buttonname.equals("Cancel:")) {

                    // Cancel
                    Bukkit.dispatchCommand(p, "mines delete " + mineName + " cancel");

                    // Close the inventory
                    p.closeInventory();

                }

                // If none of them is true, then cancel the event
                break;
            }
            case "§3" + "MineInfo -> Blocks": {

                // Get the button name without colors but with the minename too
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the minename
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String mineName = parts[1];

                // Check the click Type and do the actions
                if (e.isShiftClick() && e.isRightClick()) {

                    // Execute the command
                    Bukkit.dispatchCommand(p, "mines block remove " + mineName + " " + buttonname.substring(0, buttonname.length() - 1));

                    // Close the GUI so it can be updated
                    p.closeInventory();

                    // Open the GUI
                    SpigotMinesBlocksGUI gui = new SpigotMinesBlocksGUI(p, mineName);
                    gui.open();
                }

                e.setCancelled(true);

                break;
            }
        }

        // Deleted the e.setCancelled(true) because'd make every chest impossible to use, sorry.

    }
}

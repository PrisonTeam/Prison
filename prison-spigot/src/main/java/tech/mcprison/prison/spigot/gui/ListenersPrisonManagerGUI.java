package tech.mcprison.prison.spigot.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ListenersPrisonManagerGUI implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();

        // If you click an empty slot, this should avoid the error
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        // check if the item has itemMeta
        if (!(e.getCurrentItem().hasItemMeta())){
            return;
        }

        // Check if the GUI have the right title and do the actions
        if (e.getView().getTitle().equals("§3" + "PrisonManager")) {

            // Check the Item display name and do open the right GUI
            if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Ranks")) {
                SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
                gui.open();
                return;
            }

            // Check the Item display name and do open the right GUI
            if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Prison Tasks")) {
                e.setCancelled(true);
                return;
            }

            // Check the Item display name and do open the right GUI
            if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Mines")) {
                SpigotMinesGUI gui = new SpigotMinesGUI(p);
                gui.open();
            }

            e.setCancelled(true);

            // Check if the GUI have the right title and do the actions
        } else if (e.getView().getTitle().equals("§3" + "RanksManager -> Ladders")) {

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
            if(e.isShiftClick() && e.isRightClick()){

                // Try to remove the ladder and check if has got removed with success or not
                if (PrisonRanks.getInstance().getLadderManager().removeLadder(ladder.get())) {

                    // Send the message that the ladder got removed with success
                    p.sendMessage("§aThe ladder §8[§3"+ ladderName + "§8]§a has been deleted.");

                    // Close the inventory
                    p.closeInventory();

                    // Open a new updated inventory
                    SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
                    gui.open();
                    return;
                } else {

                    // Shouldn't happen, but could anyway, error ladder not removed
                    p.sendMessage("§cAn error occurred while removing your ladder. §8Check the console for details.");

                    // Close the inventory
                    p.closeInventory();
                    return;
                }
            }

            // Open the GUI of ranks
            SpigotRanksGUI gui = new SpigotRanksGUI(p, ladder);
            gui.open();

            // Check the title of the inventory and do the actions
        } else if (e.getView().getTitle().equals("§3" + "Ladders -> Ranks")){

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

            if(e.isShiftClick() && e.isRightClick()) {

                // Check if there's only 1 rank in the ladder default if this is the ladder default
                if (PrisonRanks.getInstance().getDefaultLadder().containsRank(rank.id)
                        && PrisonRanks.getInstance().getDefaultLadder().ranks.size() == 1) {
                    p.sendMessage("§cYou can't remove this rank because it's the only rank in the default ladder.");
                    p.closeInventory();
                    return;
                }

                // Try to remove the rank
                if (PrisonRanks.getInstance().getRankManager().removeRank(rank)) {
                    p.sendMessage("§aThe rank " + rankName + "  has been removed successfully.");

                    // Close the GUI
                    p.closeInventory();
                } else {
                    p.sendMessage("§cThe rank " + rankName + " could not be deleted due to an error.");

                    // Close the GUI
                    p.closeInventory();
                }
            }

            if (rank.rankUpCommands == null) {
                p.sendMessage("§cThere aren't commands for this rank anymore");
                e.setCancelled(true);
                return;
            }

            // Open the GUI of commands
            SpigotRankUPCommandsGUI gui = new SpigotRankUPCommandsGUI(p, rank);
            gui.open();

            // Check the title of the inventory and do things
        } else if (e.getView().getTitle().equals("§3" + "Ranks -> RankUPCommands")) {

            String command = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

            if (e.isShiftClick() && e.isRightClick()){

                if (command.startsWith("/")) {
                    command = command.replaceFirst("/", "");
                }

            // Check every ladder to find the command
            for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()) {

                // Check every rank
                for (RankLadder.PositionRank pos : PrisonRanks.getInstance().getLadderManager().getLadder(ladder.name).get().ranks) {
                    Optional<Rank> rankOptional = PrisonRanks.getInstance().getLadderManager().getLadder(ladder.name).get().getByPosition(pos.getPosition());

                    // Check everything and try to find the right path, rank, ladder where's the command... idk any
                    // Other way with the variable i have
                    for (String command1 : rankOptional.get().rankUpCommands) {
                        if (command1.equals(command)) {
                            Rank rank = rankOptional.get();

                            if (rank.rankUpCommands == null) {
                                rank.rankUpCommands = new ArrayList<>();
                            }

                            if (rank.rankUpCommands.remove(command)) {

                                try {
                                    PrisonRanks.getInstance().getRankManager().saveRank(rank);

                                    p.sendMessage("§aRemoved command " + command + " from the rank " + rank.name);
                                    p.closeInventory();

                                    if (rank.rankUpCommands == null) {
                                        p.sendMessage("§cThere aren't commands for this rank anymore");
                                        e.setCancelled(true);
                                        return;
                                    }

                                    // Open the GUI of commands
                                    SpigotRankUPCommandsGUI gui = new SpigotRankUPCommandsGUI(p, rank);
                                    gui.open();
                                } catch (IOException ex) {
                                    p.sendMessage("§cThe updated rank could not be saved to disk. Check the console for details.");
                                    p.sendMessage("§cRank could not be written to disk.");
                                }
                            } else {
                                p.sendMessage("§cThe rank doesn't contain that command. Nothing was changed.");
                            }
                        }
                    }
                }
            }
        }

            e.setCancelled(true);

        // Check the title of the inventory and do the actions
        } else if (e.getView().getTitle().equals("§3" + "MinesManager -> Mines")){

            // Mine name or title of the item
            String minename = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

            // Variables
            Optional<Mine> Mine = PrisonMines.getInstance().getMineManager().getMine(minename);
            PrisonMines pMines = PrisonMines.getInstance();
            Mine m = pMines.getMineManager().getMine(minename).get();

            // Check the clicks
            if (e.isShiftClick() && e.isRightClick()) {

                pMines.getMineManager().removeMine(pMines.getMineManager().getMine(minename).get());
                p.sendMessage("§aMine deleted with success: §3" + minename);
            }

            // Open the GUI of mines info
            SpigotMineInfoGUI gui = new SpigotMineInfoGUI(p, m, minename);
            gui.open();

        // Check the title of the inventory and do the actions
        } else if (e.getView().getTitle().equals("§3" + "Mines -> MineInfo")){

            // Get the button name without colors but with the minename too
            String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

            // Split the button at the space between the buttonname and the minename
            String[] parts = buttonnamemain.split(" ");

            // Output finally the buttonname and the minename explicit out of the array
            String buttonname = parts[0];
            String mineName = parts[1];

            // Check the name of the button and do the actions
            if (buttonname.equals("Blocks_of_the_Mine:")){

                // Cancel the event
                e.setCancelled(true);

                return;
            }

            // Check the name of the button and do the actions
            if (buttonname.equals("Reset_Mine:")){

                PrisonMines pMines = PrisonMines.getInstance();
                try {
                    pMines.getMineManager().getMine(mineName).get().manualReset();
                } catch (Exception ex) {
                    p.sendMessage("§cThe mine didn't reset, something went wrong, please check the console or logs!");
                    Output.get().logError("Couldn't reset mine " + mineName, ex);
                    p.closeInventory();
                    return;
                }

                e.setCancelled(true);
                return;
            }

            e.setCancelled(true);

            // If none of them is true, then cancel the event
        } else {

            // Cancel the event
            e.setCancelled(true);

        }
    }
}

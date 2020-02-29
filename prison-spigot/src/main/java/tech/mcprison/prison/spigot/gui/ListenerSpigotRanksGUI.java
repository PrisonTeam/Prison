package tech.mcprison.prison.spigot.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.util.Optional;

public class ListenerSpigotRanksGUI implements Listener {

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
            if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("PrisonManager")) {
                return;
            }

            // Check the Item display name and do open the right GUI
            if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Mines")) {
                return;
            }

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

            // Open the GUI of ranks
            SpigotRanksGUI gui = new SpigotRanksGUI(p, ladder);
            gui.open();

            // Check the title of the inventory and do the actions
        } else if ((e.getView().getTitle().equals("§3" + "Ladders -> Ranks"))){

            // Cancel every event in the Ranks gui, remove it if needed and must happen actions
            e.setCancelled(true);

            // If none of them is true, then cancel the event
        } else {

            // Cancel the event
            e.setCancelled(true);

        }
    }
}

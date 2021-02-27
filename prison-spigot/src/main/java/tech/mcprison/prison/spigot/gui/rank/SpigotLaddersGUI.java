package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotLaddersGUI extends SpigotGUIComponents {

    private final Player p;
    private int counter;

    public SpigotLaddersGUI(Player p, int counter){
        this.p = p;
        this.counter = counter;
    }

    public void open(){

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Init variable
        LadderManager lm = PrisonRanks.getInstance().getLadderManager();


        // If the inventory is empty
        if (lm.getLadders().size() == 0){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.NoLadders")));
            p.closeInventory();
            return;
        }

        // Get the dimensions and if needed increases them
        int dimension = 54;
        int pageSize = 45;


        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3RanksManager -> Ladders"));

        // Global ladders lore.
        List<String> laddersLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                messages.getString("Lore.ShiftAndRightClickToDelete"));

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < lm.getLadders().size() && i < counter + pageSize; i++ ) {

            RankLadder ladder = lm.getLadder(i);

            // Create the button
            ItemStack itemLadder = createButton(XMaterial.LADDER.parseItem(), laddersLore, SpigotPrison.format("&3" + ladder.getName()));

            // Add the button to the inventory
            inv.addItem(itemLadder);
        }

        if (i < lm.getLadders().size()) {
            List<String> nextPageLore = createLore(messages.getString("Lore.ClickToNextPage"));

            ItemStack nextPageButton = createButton(Material.BOOK, 1, nextPageLore, "&7Next " + (i + 1));
            inv.setItem(53, nextPageButton);
        }
        if (i >= (pageSize * 2)) {
            List<String> priorPageLore = createLore(messages.getString("Lore.ClickToPriorPage"));

            ItemStack priorPageButton = createButton(Material.BOOK, 1, priorPageLore,
                    "&7Prior " + (i - (pageSize * 2) - 1));
            inv.setItem(51, priorPageButton);
        }

        // Open the inventory
        openGUI(p, inv);
    }
}

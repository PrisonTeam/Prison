package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

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
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.NoLadders"));
            p.closeInventory();
            return;
        }

        // Get the dimensions and if needed increases them
        int dimension = 54;
        int pageSize = 45;

        PrisonGUI gui = new PrisonGUI(p, dimension, "&3RanksManager -> Ladders");

        ButtonLore laddersLore = new ButtonLore(messages.getString("Lore.ClickToOpen"), messages.getString("Lore.ShiftAndRightClickToDelete"));

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < lm.getLadders().size() && i < counter + pageSize; i++ ) {

            RankLadder ladder = lm.getLadder(i);

            // Add the button to the inventory
            gui.addButton(new Button(null, XMaterial.LADDER, laddersLore, SpigotPrison.format("&3" + ladder.getName())));
        }

        if (i < lm.getLadders().size()) {
            gui.addButton(new Button(53, XMaterial.BOOK, 1, new ButtonLore(messages.getString("Lore.ClickToNextPage"), null), "&7Next " + (i + 1)));
        }
        if (i >= (pageSize * 2)) {
            gui.addButton(new Button(51, XMaterial.BOOK, 1, new ButtonLore(messages.getString("Lore.ClickToPriorPage"), null),
                    "&7Prior " + (i - (pageSize * 2) - 1)));
        }

        // Open the inventory
        gui.open();
    }
}

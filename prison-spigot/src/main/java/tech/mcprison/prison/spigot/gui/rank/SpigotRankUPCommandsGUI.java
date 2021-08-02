package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotRankUPCommandsGUI extends SpigotGUIComponents {

    private final Player p;
    private final Rank rank;

    // Global Strings.
    private final String shiftRightClickToDelete = messages.getString("Lore.ShiftAndRightClickToDelete");
    private final String loreInfo = messages.getString("Lore.Info");
    private final String loreCommand = messages.getString("Lore.Command");

    public SpigotRankUPCommandsGUI(Player p, Rank rank) {
        this.p = p;
        this.rank = rank;
    }

    public void open() {

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        if (rank.getRankUpCommands().size() == 0){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.NoRankupCommands"));
            return;
        }

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(rank.getRankUpCommands().size() / 9D) * 9;

        // If the inventory is empty
        if (dimension == 0){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.EmptyGui"));
            p.closeInventory();
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.TooManyRankupCommands"));
            p.closeInventory();
            return;
        }

        PrisonGUI gui = new PrisonGUI(p, dimension, "&3RankManager -> RankUPCommands");

        // For every command make a button
        for (String command : rank.getRankUpCommands()) {

            List<String> commandsLore = createLore(
                    shiftRightClickToDelete,
                    "",
                    loreInfo);
            commandsLore.add(SpigotPrison.format(loreCommand + command));

            // Make the button with materials, amount, lore and name

            // Add the button to the inventory
            gui.addButton(new Button(null, XMaterial.TRIPWIRE_HOOK, commandsLore, SpigotPrison.format("&3" + rank.getName() + " " + command)));

        }

        // Open the GUI.
        gui.open();
    }
}

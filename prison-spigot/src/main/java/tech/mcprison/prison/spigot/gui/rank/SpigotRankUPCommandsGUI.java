package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotRankUPCommandsGUI extends SpigotGUIComponents {

    private final Player p;
    private final Rank rank;

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
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.NoRankupCommands")));
            return;
        }

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(rank.getRankUpCommands().size() / 9D) * 9;



        // If the inventory is empty
        if (dimension == 0){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.EmptyGui")));
            p.closeInventory();
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.TooManyRankupCommands")));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3RankManager -> RankUPCommands"));

        // For every command make a button
        for (String command : rank.getRankUpCommands()) {

            if (guiBuilder(inv, command)) return;

        }

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv, String command) {
        try {
            buttonsSetup(inv, command);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, String command) {

        ItemStack itemCommand;
        // Init the lore array with default values for ladders
        List<String> commandsLore = createLore(
                messages.getString("Lore.ShiftAndRightClickToDelete"),
                "",
                messages.getString("Lore.Info"));
        commandsLore.add(SpigotPrison.format(messages.getString("Lore.Command") + command));

        // Make the button with materials, amount, lore and name
        itemCommand = createButton(XMaterial.TRIPWIRE_HOOK.parseItem(), commandsLore, SpigotPrison.format("&3" + rank.getName() + " " + command));

        // Add the button to the inventory
        inv.addItem(itemCommand);
    }
}

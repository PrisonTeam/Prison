package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
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

        // Init the ItemStack
//        ItemStack itemcommand;

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(rank.rankUpCommands.size() / 9D) * 9;

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // If the inventory is empty
        if (dimension == 0){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.EmptyGui")));
            if (p.getOpenInventory() != null){
                p.closeInventory();
                return;
            }
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.TooManyRankupCommands")));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3RankManager -> RankUPCommands"));

        // For every command make a button
        for (String command : rank.rankUpCommands) {

            try {
                buttonsSetup(GuiConfig, inv, command);
            } catch (NullPointerException ex){
                p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
                ex.printStackTrace();
                return;
            }

        }

        // Open the inventory
        this.p.openInventory(inv);

    }

    private void buttonsSetup(Configuration guiConfig, Inventory inv, String command) {
        ItemStack itemcommand;
        // Init the lore array with default values for ladders
        List<String> commandslore = createLore(
                guiConfig.getString("Gui.Lore.ShiftAndRightClickToDelete"),
                "",
                guiConfig.getString("Gui.Lore.Info"));

        // Adding a lore
        commandslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.Command") + command));

        // Make the button with materials, amount, lore and name
        itemcommand = createButton(Material.TRIPWIRE_HOOK, 1, commandslore, SpigotPrison.format("&3" + rank.name + " " + command));

        // Add the button to the inventory
        inv.addItem(itemcommand);
    }
}

package tech.mcprison.prison.spigot.gui.rank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotRankManagerGUI extends SpigotGUIComponents {

    private final Player p;
    private final Rank rank;

    public SpigotRankManagerGUI(Player p, Rank rank) {
        this.p = p;
        this.rank = rank;
    }

    public void open() {

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3" + "Ranks -> RankManager"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (guiBuilder(inv, GuiConfig)) return;

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv, Configuration guiConfig) {
        try {
            buttonsSetup(inv, guiConfig);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, Configuration guiConfig) {
        // Create the lore
        List<String> rankupCommandsLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToOpen"),
                "",
                guiConfig.getString("Gui.Lore.Info")
        );

        SpigotRanksGUI.getCommands(rankupCommandsLore, rank);

        // Create the lore
        List<String> editPriceLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToOpen"),
                "",
                guiConfig.getString("Gui.Lore.Info"),
                guiConfig.getString("Gui.Lore.Price") + rank.cost
        );

        // Create the lore
        List<String> editTagLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToOpen"),
                "",
                guiConfig.getString("Gui.Lore.Info"),
                guiConfig.getString("Gui.Lore.Tag") + rank.tag
        );

        // Create the button
        Material commandMinecart = Material.matchMaterial( "command_minecart" );
        if ( commandMinecart == null ) {
        	commandMinecart = Material.matchMaterial( "command_block_minecart" );
        }

        ItemStack rankupCommands = createButton(commandMinecart, 1, rankupCommandsLore, SpigotPrison.format("&3" + "RankupCommands" +  " " + rank.name));

        // Create the button
        ItemStack rankPrice = createButton(Material.GOLD_NUGGET, 1, editPriceLore, SpigotPrison.format("&3" + "RankPrice" +  " " + rank.name));

        // Create the button
        ItemStack rankTag = createButton(Material.NAME_TAG, 1, editTagLore, SpigotPrison.format("&3" + "RankTag" +  " " + rank.name));

        // Set the position and add it to the inventory
        inv.setItem(10, rankupCommands);

        // Set the position and add it to the inventory
        inv.setItem(13, rankPrice);

        // Set the position and add it to the inventory
        inv.setItem(16, rankTag);
    }

}

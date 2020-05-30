package tech.mcprison.prison.spigot.gui.rank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
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

        // Create the lore
        List<String> rankupCommandsLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToOpen"),
                "",
                GuiConfig.getString("Gui.Lore.Info")
        );

        SpigotRanksGUI.getCommands(rankupCommandsLore, rank);

        // Create the lore
        List<String> editPriceLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToOpen"),
                "",
                GuiConfig.getString("Gui.Lore.Info"),
                GuiConfig.getString("Gui.Lore.Price") + rank.cost
        );

        // Create the lore
        List<String> editTagLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToOpen"),
                "",
                GuiConfig.getString("Gui.Lore.Info"),
                GuiConfig.getString("Gui.Lore.Tag") + rank.tag
        );

        // Create the button
        ItemStack rankupCommands = createButton(Material.COMMAND_MINECART, 1, rankupCommandsLore, SpigotPrison.format("&3" + "RankupCommands" +  " " + rank.name));

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

        // Open the inventory
        this.p.openInventory(inv);

    }

}

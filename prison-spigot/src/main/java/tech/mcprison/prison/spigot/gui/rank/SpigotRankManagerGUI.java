package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
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

        if (guiBuilder(inv)) return;

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {


        // Create the lore
        List<String> rankupCommandsLore = createLore(
                messages.getString("Lore.ClickToOpen")
        );

        // SpigotRanksGUI.getCommands(rankupCommandsLore, rank);

        // Create the lore
        List<String> editPriceLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                "",
                "&8-----------------------",
                " ",
                messages.getString("Lore.Info"),
                messages.getString("Lore.Price") + rank.getCost(),
                " ",
                "&8-----------------------"
                );
        List<String> editTagLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                "",
                "&8-----------------------",
                " ",
                messages.getString("Lore.Info"),
                messages.getString("Lore.Tag") + rank.getTag(),
                " ",
                "&8-----------------------"
                );

        // Create the button
        Material commandMinecart = Material.matchMaterial( "command_minecart" );
        if ( commandMinecart == null ) {
        	commandMinecart = Material.matchMaterial( "command_block_minecart" );
        }

        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );


        // Create the button
        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));
        ItemStack rankupCommands = createButton(XMaterial.matchXMaterial(commandMinecart).parseItem(), rankupCommandsLore, SpigotPrison.format("&3" + "RankupCommands" +  " " + rank.getName()));
        ItemStack rankPrice = createButton(XMaterial.GOLD_NUGGET.parseItem(), editPriceLore, SpigotPrison.format("&3" + "RankPrice" +  " " + rank.getName()));
        ItemStack rankTag = createButton(XMaterial.NAME_TAG.parseItem(), editTagLore, SpigotPrison.format("&3" + "RankTag" +  " " + rank.getName()));

        // Set the position and add it to the inventory
        inv.setItem(10, rankupCommands);
        inv.setItem(13, rankPrice);
        inv.setItem(16, rankTag);
        inv.setItem(26, closeGUI);
    }

}

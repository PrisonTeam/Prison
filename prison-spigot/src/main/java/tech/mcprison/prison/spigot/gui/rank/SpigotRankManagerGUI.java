package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.text.DecimalFormat;
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

        int dimension = 27;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3" + "Ranks -> RankManager");

        // Create the lore
        List<String> rankupCommandsLore = createLore(
                messages.getString("Lore.ClickToOpen")
        );

        // SpigotRanksGUI.getCommands(rankupCommandsLore, rank);

        // Decimal Rank cost format.
        DecimalFormat formatDecimal = new DecimalFormat("###,##0.00");

        // Create the lore
        List<String> editPriceLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                "",
                "&8-----------------------",
                " ",
                messages.getString("Lore.Info"),
                messages.getString("Lore.Price") + PlaceholdersUtil.formattedKmbtSISize(rank.getCost(), formatDecimal, ""),
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
        gui.addButton(new Button(26, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));
        if (commandMinecart != null) {
            gui.addButton(new Button(10, XMaterial.matchXMaterial(commandMinecart), rankupCommandsLore, SpigotPrison.format("&3" + "RankupCommands" +  " " + rank.getName())));
        }
        gui.addButton(new Button(13, XMaterial.GOLD_NUGGET, editPriceLore, SpigotPrison.format("&3" + "RankPrice" +  " " + rank.getName())));
        gui.addButton(new Button(16, XMaterial.NAME_TAG, editTagLore, SpigotPrison.format("&3" + "RankTag" +  " " + rank.getName())));

        gui.open();
    }
}

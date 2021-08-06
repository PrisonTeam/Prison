package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.text.DecimalFormat;

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

        ButtonLore rankupCommandsLore = new ButtonLore(messages.getString("Lore.ClickToOpen"), null);

        // Decimal Rank cost format.
        DecimalFormat formatDecimal = new DecimalFormat("###,##0.00");

        ButtonLore editPriceLore = new ButtonLore(createLore(messages.getString("Lore.ClickToOpen")), createLore(
                messages.getString("Lore.Info"),
                messages.getString("Lore.Price") + PlaceholdersUtil.formattedKmbtSISize(rank.getCost(), formatDecimal, "")));

        ButtonLore editTagLore = new ButtonLore(createLore(messages.getString("Lore.ClickToOpen")), createLore(
                messages.getString("Lore.Info"),
                messages.getString("Lore.Tag") + rank.getTag()));


        ButtonLore closeGUILore = new ButtonLore(messages.getString("Lore.ClickToClose"), null);

        // Create the button
        gui.addButton(new Button(26, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));
        gui.addButton(new Button(10, XMaterial.COMMAND_BLOCK_MINECART, rankupCommandsLore, SpigotPrison.format("&3" + "RankupCommands" +  " " + rank.getName())));
        gui.addButton(new Button(13, XMaterial.GOLD_NUGGET, editPriceLore, SpigotPrison.format("&3" + "RankPrice" +  " " + rank.getName())));
        gui.addButton(new Button(16, XMaterial.NAME_TAG, editTagLore, SpigotPrison.format("&3" + "RankTag" +  " " + rank.getName())));

        gui.open();
    }
}

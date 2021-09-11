package tech.mcprison.prison.spigot.gui.rank;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

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
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Ranks -> RankManager");

        ButtonLore rankupCommandsLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), null);

        // Decimal Rank cost format.
        DecimalFormat formatDecimal = new DecimalFormat("###,##0.00");

        RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer( new SpigotPlayer(p) );
        PlayerRank pRank = rankPlayer.getRank( rank.getLadder() );


        String rankCost;
        try {
            rankCost = PlaceholdersUtil.formattedKmbtSISize(pRank.getRankCost(), formatDecimal, "");
        } catch (NullPointerException ex){
            rankCost = "Can't get";
        }

        ButtonLore editPriceLore = new ButtonLore(createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open)), createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_info),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_price) + " " + rankCost));

        ButtonLore editTagLore = new ButtonLore(createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open)), createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_info),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_rank_tag) + " " + rank.getTag()));


        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);

        // Create the button
        gui.addButton(new Button(26, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));
        gui.addButton(new Button(10, XMaterial.COMMAND_BLOCK_MINECART, rankupCommandsLore, SpigotPrison.format("&3RankupCommands" +  " " + rank.getName())));
        gui.addButton(new Button(13, XMaterial.GOLD_NUGGET, editPriceLore, SpigotPrison.format("&3RankPrice" +  " " + rank.getName())));
        gui.addButton(new Button(16, XMaterial.NAME_TAG, editTagLore, SpigotPrison.format("&3RankTag" +  " " + rank.getName())));

        gui.open();
    }
}

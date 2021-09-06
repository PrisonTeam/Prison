package tech.mcprison.prison.spigot.gui.rank;

import java.text.DecimalFormat;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotRanksGUI extends SpigotGUIComponents {

    private final Player p;
    private final Optional<RankLadder> ladder;
    private int counter;

    public SpigotRanksGUI(Player p, Optional<RankLadder> ladder, int counter) {
        this.p = p;
        this.ladder = ladder;
        this.counter = counter;
    }

    public void open(){

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Get the dimensions and if needed increases them
        if (!ladder.isPresent() || ladder.get().getRanks().size() == 0) {
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.NoRanksFoundAdmin"));
            return;
        }

        // Get the dimensions and if required increases them
        int dimension = 54;
        int pageSize = 45;

        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Ladders -> Ranks");

        // Global Strings.
        String loreShiftRightClickDelete = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_delete);
        String loreClickToManageRank = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_manage_rank);
        String loreInfo = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_info);
        String loreId = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_id);
        String loreName = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_name);
        String loreTag2 = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_rank_tag);
        String lorePrice3 = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_price);
        String lorePlayersWithRank = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_players_at_rank);

        // Decimal Rank cost format.
        DecimalFormat formatDecimal = new DecimalFormat("###,##0.00");

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < ladder.get().getRanks().size() && i < counter + pageSize; i++ ) {

            ButtonLore ranksLore = new ButtonLore(createLore(
                    loreShiftRightClickDelete,
                    loreClickToManageRank), createLore(loreInfo));

            Rank rank = ladder.get().getRanks().get(i);

            // Can only use the raw rank costs since this is not tied to a player:
            double rawRankCost = PlayerRank.getRawRankCost( rank );
            // NOTE: The following ladderBaseRankMultiplier is just for the current ladder, but the player's 
            //       adjusted rank cost is the sum of all ladder's multipliers applied to each raw rank cost.
//            double ladderBaseRankMultiplier = PlayerRank.getLadderBaseRankdMultiplier( rank );
            
            // Add the RankID Lore
            ranksLore.addLineLoreDescription(SpigotPrison.format(loreId + " " + rank.getId()));
            ranksLore.addLineLoreDescription(SpigotPrison.format(loreName + " " + rank.getName()));
            ranksLore.addLineLoreDescription(SpigotPrison.format(loreTag2 + " " + ChatColor.translateAlternateColorCodes('&', rank.getTag())));
            ranksLore.addLineLoreDescription(SpigotPrison.format(lorePrice3 + " " + PlaceholdersUtil.formattedKmbtSISize(rawRankCost, formatDecimal, "")));

            // Init a variable
            int playerCount = rank.getPlayers().size();
//            List<RankPlayer> players =
//                    PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
//                            .filter(rankPlayer -> rankPlayer.getLadderRanks().containsValue(rank))
//                            .collect(Collectors.toList());

            // Add the number of players with this rank
            ranksLore.addLineLoreDescription(SpigotPrison.format(lorePlayersWithRank + " " + playerCount));

            // Add the button to the inventory
            gui.addButton(new Button(i - counter, XMaterial.TRIPWIRE_HOOK, ranksLore, SpigotPrison.format("&3" + rank.getName())));
        }

        if (i < ladder.get().getRanks().size()) {
            gui.addButton(new Button(53, XMaterial.BOOK, 1, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (i + 1)));
        }
        if (i >= (pageSize * 2)) {
            gui.addButton(new Button(51, XMaterial.BOOK, 1, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_prior_page), null),
                    "&7Prior " + (i - (pageSize * 2) - 1)));
        }

        // Open the GUI.
        gui.open();
    }
}

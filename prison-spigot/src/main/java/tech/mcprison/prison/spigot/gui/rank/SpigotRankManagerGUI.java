package tech.mcprison.prison.spigot.gui.rank;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;

/**
 * @author GABRYCA
 */
public class SpigotRankManagerGUI 
	extends SpigotGUIMessages {

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

        ButtonLore rankupCommandsLore = new ButtonLore( guiLeftClickToOpenMsg(), null);

        // Decimal Rank cost format.
        DecimalFormat formatDecimal = Prison.get().getDecimalFormat("###,##0.00");

        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        
        RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer( new SpigotPlayer(p) );
        PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, rank.getLadder() );


        String rankCost;
        try {
            rankCost = PlaceholdersUtil.formattedKmbtSISize(pRank.getRankCost(), formatDecimal, "");
        } catch (NullPointerException ex){
            rankCost = "Unavailable";
        }

        ButtonLore editPriceLore = new ButtonLore(
        		createLore( guiLeftClickToOpenMsg()), 
        		createLore( guiRanksLoreInfoMsg(),
                guiPriceMsg( rankCost) ));

        ButtonLore editTagLore = new ButtonLore(createLore( guiLeftClickToOpenMsg() ), 
        		createLore( guiRanksLoreInfoMsg(),
        				guiRanksLoreRankTagMsg() + " " + rank.getTag()));


        ButtonLore closeGUILore = new ButtonLore( guiClickToCloseMsg(), null);

        // Create the button
        gui.addButton(new Button(26, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, "&cClose" ));
        gui.addButton(new Button(10, XMaterial.COMMAND_BLOCK_MINECART, rankupCommandsLore, "&3RankupCommands" +  " " + rank.getName() ));
        gui.addButton(new Button(13, XMaterial.GOLD_NUGGET, editPriceLore, "&3RankPrice" +  " " + rank.getName() ));
        gui.addButton(new Button(16, XMaterial.NAME_TAG, editTagLore, "&3RankTag" +  " " + rank.getName() ));

        gui.open();
    }
}

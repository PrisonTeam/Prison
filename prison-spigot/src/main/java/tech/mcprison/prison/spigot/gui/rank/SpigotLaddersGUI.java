package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;

/**
 * @author GABRYCA
 */
public class SpigotLaddersGUI 
	extends SpigotGUIMessages {

    private final Player p;
    
    private int page;
    private String cmdPage;
    private String cmdReturn;

    public SpigotLaddersGUI(Player p, int page, String cmdPage, String cmdReturn ) {
        this.p = p;
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;
    }

    public void open(){

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Init variable
        LadderManager lm = PrisonRanks.getInstance().getLadderManager();

        // If the inventory is empty
        if (lm == null || lm.getLadders().size() == 0){
        		guiRanksLadderIsEmptyMsg( new SpigotPlayer(p), "" );
            p.closeInventory();
            return;
        }
        
        
        int totalArraySize = lm.getLadders().size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


        List<RankLadder> laddersDisplay = lm.getLadders().subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        
        

        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3RanksManager -> Ladders");

        ButtonLore laddersLore = new ButtonLore(
        		guiLeftClickToOpenMsg(), 
        		guiRightClickShiftToDeleteMsg() );

        for ( RankLadder ladder : laddersDisplay ) {
			
        		gui.addButton(new Button(null, XMaterial.LADDER, laddersLore, "&3" + ladder.getName() ));
		}
        
        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );


        
        // Open the inventory
        gui.open();
    }
}

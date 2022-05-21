package tech.mcprison.prison.spigot.gui.rank;

import java.util.List;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotLaddersGUI extends SpigotGUIComponents {

    private final Player p;
    
    private int page;
    private String cmdPage;
    private String cmdReturn;
//    private int counter;

    public SpigotLaddersGUI(Player p, int page, String cmdPage, String cmdReturn ) {
        this.p = p;
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;
//        this.counter = counter;
    }

    public void open(){

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        // Init variable
        LadderManager lm = PrisonRanks.getInstance().getLadderManager();

        // If the inventory is empty
        if (lm.getLadders().size() == 0){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_ladder_empty));
            p.closeInventory();
            return;
        }

        
        
        int totalArraySize = lm.getLadders().size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


        List<RankLadder> laddersDisplay = lm.getLadders().subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        
        
 
        
//        // Get the dimensions and if needed increases them
//        int dimension = 54;
//        int pageSize = 45;

        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3RanksManager -> Ladders");

        ButtonLore laddersLore = new ButtonLore(
        		messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), 
        		guiRightClickShiftToDeleteMsg() );

        for ( RankLadder ladder : laddersDisplay ) {
			
        	gui.addButton(new Button(null, XMaterial.LADDER, laddersLore, "&3" + ladder.getName() ));
		}
        
        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );


        
//        // Only loop over the blocks that we need to show:
//        int i = counter;
//        for ( ; i < lm.getLadders().size() && i < counter + pageSize; i++ ) {
//
//        	// WARNING: This is wrong... a ladder's ID is NOT the same as it's position in a collection.
//        	// The ladder ID is a damn magic number and cannot be predictable!
//        	
////            RankLadder ladder = lm.getLadder(i);
//
//            // Add the button to the inventory
//            gui.addButton(new Button(null, XMaterial.LADDER, laddersLore, "&3" + ladder.getName())));
//        }

//        if (i < lm.getLadders().size()) {
//            gui.addButton(new Button(53, XMaterial.BOOK, 1, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (i + 1)));
//        }
//        if (i >= (pageSize * 2)) {
//            gui.addButton(new Button(51, XMaterial.BOOK, 1, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_prior_page), null),
//                    "&7Prior " + (i - (pageSize * 2) - 1)));
//        }

        // Open the inventory
        gui.open();
    }
}

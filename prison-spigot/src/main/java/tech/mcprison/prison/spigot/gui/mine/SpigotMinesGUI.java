package tech.mcprison.prison.spigot.gui.mine;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.spigot.gui.PrisonSetupGUI;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotGUIMessages;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotMinesGUI 
	extends SpigotGUIMessages {

    private final Player p;
    
    private int page = 0;
    private String cmdPage;
    private String cmdReturn;
        
    public SpigotMinesGUI(Player p, int page, String cmdPage, String cmdReturn ) {
        this.p = p;

        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;

    }

    public void open(){

        // Get the mines - In sort order, minus any marked as suppressed
        PrisonSortableResults mines = PrisonMines.getInstance().getMines( MineSortOrder.sortOrder );

        // If the inventory is empty
        if (mines.getSortedList().size() == 0){
            p.closeInventory();
            PrisonSetupGUI gui = new PrisonSetupGUI(p);
            gui.open();
            return;
        }

        
        
        int totalArraySize = mines.getSortedList().size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


        List<Mine> minesDisplay = mines.getSortedList().subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        



        // Create GUI.
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3MinesManager -> Mines");

        // Global Strings.
        String loreLeftClickOpen = guiLeftClickToOpenMsg();
        String loreShiftRightClickToDelete = guiRightClickShiftToDeleteMsg();
        String loreInfo = guiRanksLoreInfoMsg();
        String loreWorld = guiRanksLoreWorldMsg();
        String loreSpawnPoint = guiRanksLoreSpawnPointMsg();
        String loreResetTime = guiRanksLoreResetTimeMsg();
        String loreSizeOfMine = guiRanksLoreSizeMsg();
        String loreVolume = guiRanksLoreVolumeMsg();
        String loreBlocks = guiRanksLoreBlocksMsg();


        for ( Mine m : minesDisplay )
		{
            ButtonLore minesLore = new ButtonLore(createLore(
                    loreLeftClickOpen,
                    loreShiftRightClickToDelete
            ), createLore(loreInfo));

            // Add a lore
            minesLore.addLineLoreDescription("&7" + loreWorld + " &b" + m.getWorldName());
            String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "&cnot set";
            minesLore.addLineLoreDescription("&7" + loreSpawnPoint + " &b" + spawnPoint);
            minesLore.addLineLoreDescription("&7" + loreResetTime + " &b" + m.getResetTime());

            if (!m.isVirtual()) {
                // Add a lore
                minesLore.addLineLoreDescription("&7" + loreSizeOfMine + " &b" + m.getBounds().getDimensions());
                minesLore.addLineLoreDescription("&7" + loreVolume + " &b" + m.getBounds().getTotalBlockCount());
            }

            // Add a lore
            minesLore.addLineLoreDescription("&7" + loreBlocks);

            // Init some variables and do the actions
            DecimalFormat dFmt = Prison.get().getDecimalFormat("##0.00");
            double totalChance = 0.0d;

            for (PrisonBlock block : m.getPrisonBlocks()) {
	            	double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
	            	totalChance += chance;
	            	
	            	String blockName =
	            			StringUtils.capitalize(block.getBlockName().replaceAll("_", " ").toLowerCase());
	            	minesLore.addLineLoreDescription("&7" + chance + "% - " + block.getBlockName() + "   (" + blockName + ")");
            }

            if (totalChance < 100.0d) {
                minesLore.addLineLoreDescription("&e " + dFmt.format(100.0d - totalChance) + "%  - Air");
            }

            gui.addButton(new Button(null, XMaterial.COAL_ORE, minesLore, "&3" + m.getName()));

		}
        
        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );

        
        // Open the GUI.
        gui.open();
    }
}

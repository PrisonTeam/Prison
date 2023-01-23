package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotGUIMessages;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotMinesBlocksGUI 
	extends SpigotGUIMessages {

    private final Player p;
    private final String mineName;

    // Global Strings.
    private final String loreShiftRightClickToDelete = guiRightClickShiftToDeleteMsg();
    private final String loreClickToEditBlock = guiClickToEditMsg();
    private final String loreInfo = guiRanksLoreInfoMsg();
    private final String loreChance = guiRanksLoreChanceMsg();
    private final String loreBlockType = guiRanksLoreBlockTypeMsg();

    public SpigotMinesBlocksGUI(Player p, String mineName){
        this.p = p;
        this.mineName = mineName;
    }

    public void open(){

        int dimension = 54;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3MineInfo -> Blocks");

        // Get Mine
        Mine m = PrisonMines.getInstance().getMine(mineName);

        ButtonLore addBlockLore = new ButtonLore( guiRanksLoreClickToAddMsg(), null);

        // Add the button to the GUI.
        gui.addButton(new Button(dimension - 1, XMaterial.LIME_STAINED_GLASS_PANE, addBlockLore, "&aAdd " + mineName));

        
        // For every block makes a button
        for (PrisonBlock block : m.getPrisonBlocks()) {
        	
        	// Get the block material as a string and displayname
        	String blockmaterial = block.getBlockName();
        	String blockmaterialdisplay = blockmaterial;
        	
        	// Check if a block's air and changed the item of it to BARRIER
        	if (blockmaterial.equalsIgnoreCase("air")){
        		blockmaterial = "BARRIER";
        		blockmaterialdisplay = blockmaterial;
        	}
        	
        	// Get XMaterial.
        	XMaterial xMat = SpigotUtil.getXMaterial(blockmaterial);
        	if (PrisonBlock.IGNORE.getBlockName().equalsIgnoreCase(blockmaterial)) {
        		xMat = XMaterial.BARRIER;
        	}
        	if (xMat == null ) {
        		xMat = XMaterial.STONE;
        	}
        	
        	ButtonLore blocksLore = new ButtonLore(createLore(loreClickToEditBlock, loreShiftRightClickToDelete), createLore(loreInfo));
        	
        	// Add a lore
        	blocksLore.addLineLoreDescription( loreChance + block.getChance() + "%" );
        	blocksLore.addLineLoreDescription( loreBlockType + blockmaterial );
        	
        	// Add the button to the GUI.
        	gui.addButton(new Button(null, xMat, blocksLore, "&3" + blockmaterialdisplay + " " + mineName + " " + block.getChance()));
        }

        // Open the inventory
        gui.open();
    }
}

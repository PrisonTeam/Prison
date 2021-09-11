package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotBlocksListGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private int counter;

    public SpigotBlocksListGUI(Player p, String mineName, int counter){
        this.p = p;
        this.mineName = mineName;
        this.counter = counter;
    }

    public void open(){

        // Get the dimensions and if needed increases them
        int dimension = 54;
        int pageSize = 45;
        
        // Create the inventory
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Mines -> BlocksList");

        ButtonLore lore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_start_block_setup), null);

        // This will skip all BlockTypes that are invalid for the versions of MC that the server is running:
    	PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
        List<PrisonBlock> blockTypes = prisonBlockTypes.getBlockTypes();
        
        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < blockTypes.size() && i < counter + pageSize; i++ ) {
        	PrisonBlock prisonBlock = blockTypes.get( i );

        	XMaterial xMat = SpigotUtil.getXMaterial( prisonBlock );

        	if ( PrisonBlock.IGNORE.equals( prisonBlock )) {
        		xMat = XMaterial.BARRIER;
        	}
        	if ( xMat == null ) {
        		xMat = XMaterial.STONE;
        	}

        	gui.addButton(new Button(null, xMat, lore, "&a" +
                    prisonBlock.getBlockName().toUpperCase() + " &0" + mineName + " " + counter));
        }
        if ( i < blockTypes.size() ) {
        	gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next &0" + mineName + " " + (i + 1)));
        }
        if ( i >= (pageSize * 2) ) {
        	gui.addButton(new Button(51, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior &0" + mineName + " " + (i - (pageSize * 2) - 1)));
        }

        // Open the inventory
        gui.open();
    }
}


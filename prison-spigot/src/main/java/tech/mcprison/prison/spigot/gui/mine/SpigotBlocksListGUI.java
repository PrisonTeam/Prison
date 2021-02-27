package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

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
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> BlocksList"));

        // Lore of block setup
        List<String> blockLoreSetup = createLore(
                messages.getString("Lore.ClickToStartBlockSetup")
        );

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
        	
        	ItemStack button = createButton( xMat.parseItem(), blockLoreSetup, SpigotPrison.format("&a" + 
        											prisonBlock.getBlockName().toUpperCase() + " &0" + mineName + " " + counter));
        	inv.addItem(button);
        }
        if ( i < blockTypes.size() ) {
        	List<String> nextPageLore = createLore(  messages.getString("Lore.ClickToNextPage") );
        	
        	ItemStack nextPageButton = createButton(Material.BOOK, 1, nextPageLore, "&7Next &0" + mineName + " " + (i + 1) );
        	inv.setItem(53, nextPageButton);
        }
        if ( i >= (pageSize * 2) ) {
        	List<String> priorPageLore = createLore(  messages.getString("Lore.ClickToPriorPage") );
        	
        	ItemStack priorPageButton = createButton(Material.BOOK, 1, priorPageLore, 
        											"&7Prior &0" + mineName + " " + (i - (pageSize * 2) - 1) );
        	inv.setItem(51, priorPageButton);
        }

        // Open the inventory
        openGUI(p, inv);
    }
}


package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.BlockOld;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotMinesBlocksGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;

    public SpigotMinesBlocksGUI(Player p, String mineName){
        this.p = p;
        this.mineName = mineName;
    }

    public void open(){

        // Get Mine
        Mine m = PrisonMines.getInstance().getMine(mineName);

        // Get the dimensions and if needed increases them
        int dimension = 54;
        
		boolean useNewBlockModel = Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" );

        // Create the inventory
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineInfo -> Blocks"));

        List<String> addBlockLore = createLore(
                messages.getString("Lore.ClickToAddBlock")
        );

        // Add the button to the inventory
        ItemStack addBlockButton = createButton(XMaterial.LIME_STAINED_GLASS_PANE.parseItem(), addBlockLore, SpigotPrison.format("&a" + "Add" + " " + mineName));
        inv.setItem(dimension - 1, addBlockButton);

        if (useNewBlockModel) {
        	
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
        		
        		if (guiBuilder(inv, block, blockmaterial, blockmaterialdisplay)) return;
        	}
        } else {
        	
        	// For every block makes a button
        	for (BlockOld block : m.getBlocks()) {
        		
        		// Get the block material as a string and displayname
        		String blockmaterial = block.getType().name();
        		String blockmaterialdisplay = blockmaterial;
        		
        		// Check if a block's air and changed the item of it to BARRIER
        		if (blockmaterial.equalsIgnoreCase("air")){
        			blockmaterial = "BARRIER";
        			blockmaterialdisplay = blockmaterial;
        		}
        		
        		if (guiBuilder(inv, block, blockmaterial, blockmaterialdisplay)) return;
        	}
        }

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv, PrisonBlock block, String blockmaterial, String blockmaterialdisplay) {
        try {
            buttonsSetup(inv, block, blockmaterial, blockmaterialdisplay);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }
    
    private boolean guiBuilder(Inventory inv, BlockOld block, String blockmaterial, String blockmaterialdisplay) {
    	try {
    		buttonsSetup(inv, block, blockmaterial, blockmaterialdisplay);
    	} catch (NullPointerException ex){
    		Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
    		ex.printStackTrace();
    		return true;
    	}
    	return false;
    }

    private void buttonsSetup(Inventory inv, PrisonBlock block, String blockmaterial, String blockmaterialdisplay) {

        // Create the lore
        List<String> blockslore = createLore(
                messages.getString("Lore.ShiftAndRightClickToDelete"),
                messages.getString("Lore.ClickToEditBlock"),
                "",
                messages.getString("Lore.Info"));


        boolean isEnum = true;
        try {
            Material.valueOf(blockmaterial);
        } catch (Exception e) {
            isEnum = false;
        }

        if (!(isEnum)) {
            blockmaterial = "BARRIER";
        }

        // Add a lore
        blockslore.add(SpigotPrison.format(messages.getString("Lore.Chance") + block.getChance() + "%"));
        blockslore.add(SpigotPrison.format(messages.getString("Lore.BlockType") + blockmaterial));

        // Make the item
        ItemStack block1 = createButton(XMaterial.valueOf(blockmaterial).parseItem(), blockslore, SpigotPrison.format("&3" + blockmaterialdisplay + " " + mineName + " " + block.getChance()));
        inv.addItem(block1);
    }

    private void buttonsSetup(Inventory inv, BlockOld block, String blockmaterial, String blockmaterialdisplay) {

        // Create the lore
    	List<String> blockslore = createLore(
    			messages.getString("Lore.ShiftAndRightClickToDelete"),
    			messages.getString("Lore.ClickToEditBlock"),
    			"",
    			messages.getString("Lore.Info"));

    	boolean isEnum = true;
    	try {
    		Material.valueOf(blockmaterial);
    	} catch (Exception e) {
    		isEnum = false;
    	}

    	if (!(isEnum)) {
    		blockmaterial = "BARRIER";
    	}

    	// Add a lore
    	blockslore.add(SpigotPrison.format(messages.getString("Lore.Chance") + block.getChance() + "%"));
    	blockslore.add(SpigotPrison.format(messages.getString("Lore.BlockType") + blockmaterial));

    	// Make the item
    	ItemStack block1 = createButton(XMaterial.valueOf(blockmaterial).parseItem(), blockslore, SpigotPrison.format("&3" + blockmaterialdisplay + " " + mineName + " " + block.getChance()));

    	// Add the item to the inventory
    	inv.addItem(block1);
    }
}

package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMinesBlocksGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private final Configuration messages = configs("messages");

    public SpigotMinesBlocksGUI(Player p, String mineName){
        this.p = p;
        this.mineName = mineName;
    }

    public void open(){

        // Get the variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);

        // Get the dimensions and if needed increases them
        int dimension = 54;
        
		boolean useNewBlockModel = Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" );

        // Create the inventory
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineInfo -> Blocks"));

        List<String> addBlockLore = createLore(
                messages.getString("Gui.Lore.ClickToAddBlock")
        );

        ItemStack addBlockButton = createButton(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, addBlockLore, SpigotPrison.format("&a" + "Add"));
        inv.setItem(dimension - 1, addBlockButton);

        if ( useNewBlockModel ) {
        	
        	// For every block makes a button
        	for (PrisonBlock block : m.getPrisonBlocks()) {
        		
        		// Get the block material as a string
        		String blockmaterial = block.getBlockName();
        		
        		// Display title of the item
        		String blockmaterialdisplay = blockmaterial;
        		
        		// Check if a block's air and changed the item of it to BARRIER
        		if (blockmaterial.equalsIgnoreCase("air")){
        			blockmaterial = "BARRIER";
        			blockmaterialdisplay = blockmaterial;
        		}
        		
        		if (guiBuilder(inv, block, blockmaterial, blockmaterialdisplay)) return;
        		
        	}
        }
        else {
        	
        	// For every block makes a button
        	for (Block block : m.getBlocks()) {
        		
        		// Get the block material as a string
        		String blockmaterial = block.getType().name();
        		
        		// Display title of the item
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
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv, PrisonBlock block, String blockmaterial, String blockmaterialdisplay) {
        try {
            buttonsSetup(inv, block, blockmaterial, blockmaterialdisplay);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }
    
    private boolean guiBuilder(Inventory inv, Block block, String blockmaterial, String blockmaterialdisplay) {
    	try {
    		buttonsSetup(inv, block, blockmaterial, blockmaterialdisplay);
    	} catch (NullPointerException ex){
    		p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
    		ex.printStackTrace();
    		return true;
    	}
    	return false;
    }

    private void buttonsSetup(Inventory inv, PrisonBlock block, String blockmaterial, String blockmaterialdisplay) {

    	// Don't load this every time a button is created.... making it a class variable:
        //Configuration messages = SpigotPrison.getGuiMessagesConfig();

        // Create the lore
        List<String> blockslore = createLore(
                messages.getString("Gui.Lore.ShiftAndRightClickToDelete"),
                messages.getString("Gui.Lore.ClickToEditBlock"),
                "",
                messages.getString("Gui.Lore.Info"));


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
        blockslore.add(SpigotPrison.format(messages.getString("Gui.Lore.Chance") + block.getChance() + "%"));
        blockslore.add(SpigotPrison.format(messages.getString("Gui.Lore.BlockType") + blockmaterial));

        // Make the item
        ItemStack block1 = createButton(Material.valueOf(blockmaterial), 1, blockslore, SpigotPrison.format("&3" + blockmaterialdisplay + " " + mineName + " " + block.getChance()));

        // Add the item to the inventory
        inv.addItem(block1);
    }

    private void buttonsSetup(Inventory inv, Block block, String blockmaterial, String blockmaterialdisplay) {

        //Configuration messages = SpigotPrison.getGuiMessagesConfig();

        // Create the lore
    	List<String> blockslore = createLore(
    			messages.getString("Gui.Lore.ShiftAndRightClickToDelete"),
    			messages.getString("Gui.Lore.ClickToEditBlock"),
    			"",
    			messages.getString("Gui.Lore.Info"));


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
    	blockslore.add(SpigotPrison.format(messages.getString("Gui.Lore.Chance") + block.getChance() + "%"));
    	blockslore.add(SpigotPrison.format(messages.getString("Gui.Lore.BlockType") + blockmaterial));

    	// Make the item
    	ItemStack block1 = createButton(Material.valueOf(blockmaterial), 1, blockslore, SpigotPrison.format("&3" + blockmaterialdisplay + " " + mineName + " " + block.getChance()));

    	// Add the item to the inventory
    	inv.addItem(block1);
    }

}

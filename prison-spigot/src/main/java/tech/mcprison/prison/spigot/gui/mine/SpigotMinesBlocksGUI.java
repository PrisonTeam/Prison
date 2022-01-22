package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotMinesBlocksGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;

    // Global Strings.
    private final String loreShiftRightClickToDelete = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_delete);
    private final String loreClickToEditBlock = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_edit);
    private final String loreInfo = messages.getString(MessagesConfig.StringID.spigot_gui_lore_info);
    private final String loreChance = messages.getString(MessagesConfig.StringID.spigot_gui_lore_chance);
    private final String loreBlockType = messages.getString(MessagesConfig.StringID.spigot_gui_lore_blocktype);

    public SpigotMinesBlocksGUI(Player p, String mineName){
        this.p = p;
        this.mineName = mineName;
    }

    public void open(){

        int dimension = 54;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3MineInfo -> Blocks");

        // Get Mine
        Mine m = PrisonMines.getInstance().getMine(mineName);
		boolean useNewBlockModel = Prison.get().getPlatform().isUseNewPrisonBlockModel();

        ButtonLore addBlockLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_add), null);

        // Add the button to the GUI.
        gui.addButton(new Button(dimension - 1, XMaterial.LIME_STAINED_GLASS_PANE, addBlockLore, "&aAdd " + mineName));

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
                blocksLore.addLineLoreDescription(SpigotPrison.format(loreChance + block.getChance() + "%"));
                blocksLore.addLineLoreDescription(SpigotPrison.format(loreBlockType + blockmaterial));

                // Add the button to the GUI.
                gui.addButton(new Button(null, xMat, blocksLore, "&3" + blockmaterialdisplay + " " + mineName + " " + block.getChance()));
        	}
        } /*else {

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

                // Create the lore
                List<String> blockslore = createLore(
                        loreShiftRightClickToDelete,
                        loreClickToEditBlock,
                        "",
                        loreInfo
                );

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
                blockslore.add(SpigotPrison.format(loreChance + block.getChance() + "%"));
                blockslore.add(SpigotPrison.format(loreBlockType + blockmaterial));

                // Add the button to the GUI.
                gui.addButton(new Button(null, XMaterial.valueOf(blockmaterial), blockslore, "&3" + blockmaterialdisplay + " " + mineName + " " + block.getChance()));
        	}
        }*/

        // Open the inventory
        gui.open();
    }
}

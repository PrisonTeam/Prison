package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotBlocksMineListGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private int counter;

    public SpigotBlocksMineListGUI(Player p, String mineName, int counter){
        this.p = p;
        this.mineName = mineName;
        this.counter = counter;
    }

    public void open(){

        // Get the dimensions and if needed increases them
        int dimension = 54;
        int pageSize = 45;

        // Create the inventory
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Select -> ShowBlock");

        ButtonLore lore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_select), null);

        // This will skip all BlockTypes that are invalid for the versions of MC that the server is running:
        PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
        List<PrisonBlock> blockTypes = prisonBlockTypes.getBlockTypes();

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < blockTypes.size() && i < counter + pageSize; i++ ) {
            PrisonBlock prisonBlock = blockTypes.get(i);

            XMaterial xMat = SpigotUtil.getXMaterial(prisonBlock);

            if ( PrisonBlock.IGNORE.equals( prisonBlock )) {
                xMat = XMaterial.BARRIER;
            }
            if ( xMat == null ) {
                xMat = XMaterial.STONE;
            }

            gui.addButton(new Button(null, xMat, lore, "&3" +
                    prisonBlock.getBlockName().toUpperCase() + " " + mineName + " " + counter));
        }
        if ( i < blockTypes.size() ) {
            gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + mineName + " " + (i + 1)));
        }
        if ( i >= (pageSize * 2) ) {
            gui.addButton(new Button(51, XMaterial.BOOK, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior " + mineName + " " + (i - (pageSize * 2) - 1)));
        }

        // Open the inventory
        gui.open();
    }
}

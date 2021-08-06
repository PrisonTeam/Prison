package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA (AnonymousGCA)
 */
public class SpigotMineBlockPercentageGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private final Double val;
    private final String blockName;
    private int counter;

    public SpigotMineBlockPercentageGUI(Player p, Double val, String mineName, String blockName, int counter){
        this.p = p;
        this.val = val;
        this.mineName = mineName;
        this.blockName = blockName;
        this.counter = counter;
    }

    public void open() {
        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3MineInfo -> BlockPercentage");

        ButtonLore changeDecreaseValueLore = new ButtonLore(messages.getString("Lore.ClickToDecrease"), null);
        ButtonLore confirmButtonLore = new ButtonLore(createLore(messages.getString("Lore.LeftClickToConfirm"), messages.getString("Lore.RightClickToCancel")), createLore(messages.getString("Lore.Percentage") + val));
        ButtonLore changeIncreaseValueLore = new ButtonLore(messages.getString("Lore.ClickToIncrease"), null);

        XMaterial decreaseMaterial = XMaterial.REDSTONE_BLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMaterial, changeDecreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " - 1" + " &0" + counter));
        gui.addButton(new Button(10, decreaseMaterial, 5, changeDecreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " - 5" + " &0" + counter));
        gui.addButton(new Button(19, decreaseMaterial, 10, changeDecreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " - 10" + " &0" + counter));
        gui.addButton(new Button(28, decreaseMaterial, 50, changeDecreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " - 50" + " &0" + counter));
        gui.addButton(new Button(37, decreaseMaterial, changeDecreaseValueLore, "&3" + mineName + " " + blockName +  " " + val +  " - 100" + " &0" + counter));

        gui.addButton(new Button(22, XMaterial.CLOCK, 1, confirmButtonLore, "&3" + "Confirm: " + mineName + " " + blockName +  " " + val + " &0" + counter));

        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;

        // Increase button
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " + 1" + " &0" + counter));
        gui.addButton(new Button(16, increaseMat, 5, changeIncreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " + 5" + " &0" + counter));
        gui.addButton(new Button(25, increaseMat, 10, changeIncreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " + 10" + " &0" + counter));
        gui.addButton(new Button(34, increaseMat, 50, changeIncreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " + 50" + " &0" + counter));
        gui.addButton(new Button(43, increaseMat, changeIncreaseValueLore, "&3" + mineName + " " + blockName +  " " + val + " + 100" + " &0" + counter));

        // Close gui:
        gui.addButton(new Button(40, XMaterial.RED_STAINED_GLASS_PANE, new ButtonLore(messages.getString("Lore.ClickToClose"), null), "&c" + "Close" + " &0" + mineName + " " + counter));

        // Show the selected block at the top center position:
        XMaterial xMat = SpigotUtil.getXMaterial( blockName );
        if ( PrisonBlock.IGNORE.getBlockName().equalsIgnoreCase( blockName )) {
            xMat = XMaterial.BARRIER;
        }
        if ( xMat == null ) {
            xMat = XMaterial.STONE;
        }
        gui.addButton(new Button(4, xMat, 1, "&3" + xMat));

        gui.open();
    }
}

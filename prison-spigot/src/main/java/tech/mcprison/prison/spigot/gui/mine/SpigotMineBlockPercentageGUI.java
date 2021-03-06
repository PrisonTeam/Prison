package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
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

        // Create a new inventory
        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineInfo -> BlockPercentage"));

        if (guiBuilder(inv)) return;

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {


        // Create a new lore
        List<String> changeDecreaseValueLore = createLore(
                messages.getString("Lore.ClickToDecrease")
        );
        List<String> confirmButtonLore = createLore(
                messages.getString("Lore.LeftClickToConfirm"),
                messages.getString("Lore.Percentage") + val,
                messages.getString("Lore.RightClickToCancel")
        );
        List<String> changeIncreaseValueLore = createLore(
                messages.getString("Lore.ClickToIncrease")
        );

        Material decreaseMat = XMaterial.REDSTONE_BLOCK.parseMaterial();
        ItemStack decreaseStack = XMaterial.REDSTONE_BLOCK.parseItem();

        // Decrease button
        ItemStack decreaseOf1 = createButton(decreaseStack, changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " - 1" + " &0" + counter));
        inv.setItem(1, decreaseOf1);
        ItemStack decreaseOf5 = createButton(new ItemStack(decreaseMat, 5), changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " - 5" + " &0" + counter));
        inv.setItem(10, decreaseOf5);
        ItemStack decreaseOf10 = createButton(new ItemStack(decreaseMat, 10), changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " - 10" + " &0" + counter));
        inv.setItem(19, decreaseOf10);
        ItemStack decreaseOf50 = createButton(new ItemStack(decreaseMat, 50), changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " - 50" + " &0" + counter));
        inv.setItem(28, decreaseOf50);
        ItemStack decreaseOf100 = createButton(decreaseStack, changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val +  " - 100" + " &0" + counter));
        inv.setItem(37, decreaseOf100);


        // Create a button and set the position
        Material watch = Material.matchMaterial( "watch" );
        if ( watch == null ) {
            watch = Material.matchMaterial( "legacy_watch" );
        } if ( watch == null ) {
            watch = Material.matchMaterial( "clock" );
        }
        ItemStack confirmButton = createButton(watch, 1, confirmButtonLore, SpigotPrison.format("&3" + "Confirm: " + mineName + " " + blockName +  " " + val + " &0" + counter));
        inv.setItem(22, confirmButton);

        Material increaseMat = XMaterial.EMERALD_BLOCK.parseMaterial();
        ItemStack increaseStack = XMaterial.EMERALD_BLOCK.parseItem();

        // Increase button
        ItemStack increseOf1 = createButton(increaseStack, changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " + 1" + " &0" + counter ));
        inv.setItem(7, increseOf1);
        ItemStack increaseOf5 = createButton(new ItemStack(increaseMat, 5), changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " + 5" + " &0" + counter));
        inv.setItem(16, increaseOf5);
        ItemStack increaseOf10 = createButton(new ItemStack(increaseMat, 10), changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " + 10" + " &0" + counter));
        inv.setItem(25, increaseOf10);
        ItemStack increaseOf50 = createButton(new ItemStack(increaseMat, 50), changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " + 50" + " &0" + counter));
        inv.setItem(34, increaseOf50);
        ItemStack increaseOf100 = createButton(increaseStack, changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + blockName +  " " + val + " + 100" + " &0" + counter));
        inv.setItem(43, increaseOf100);
       
        // Return to prior screen:
        List<String> closeGUILore = createLore( messages.getString("Lore.ClickToClose") );
        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close" + " &0" + mineName + " " + counter));
        inv.setItem(40, closeGUI);


        // Show the selected block at the top center position:
    	XMaterial xMat = SpigotUtil.getXMaterial( blockName );
    	if ( PrisonBlock.IGNORE.getBlockName().equalsIgnoreCase( blockName )) {
    		xMat = XMaterial.BARRIER;
    	}
    	if ( xMat == null ) {
    		xMat = XMaterial.STONE;
    	}
    	inv.setItem(4, xMat.parseItem());
    
    }
}

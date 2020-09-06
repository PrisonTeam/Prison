package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMineBlockPercentageGUI extends SpigotGUIComponents {

    private final Player p;
    private final String minename;
    private final Double val;
    private final String blockName;

    public SpigotMineBlockPercentageGUI(Player p, Double val, String minename, String blockName){
        this.p = p;
        this.val = val;
        this.minename = minename;
        this.blockName = blockName;
    }

    public void open() {

        // Create a new inventory
        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineInfo -> BlockPercentage"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (guiBuilder(inv, GuiConfig)) return;

        // Open the inventory
        this.p.openInventory(inv);
    }

    private boolean guiBuilder(Inventory inv, Configuration guiConfig) {
        try {
            buttonsSetup(inv, guiConfig);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, Configuration guiConfig) {
        // Create a new lore
        List<String> changeDecreaseValueLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToDecrease")
        );

        // Create a new lore
        List<String> confirmButtonLore = createLore(
                guiConfig.getString("Gui.Lore.LeftClickToConfirm"),
                guiConfig.getString("Gui.Lore.Percentage") + val,
                guiConfig.getString("Gui.Lore.RightClickToCancel")
        );

        // Create a new lore
        List<String> changeIncreaseValueLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToIncrease")
        );


        // Decrease button
        ItemStack decreaseOf1 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " - 1" ));
        inv.setItem(1, decreaseOf1);

        // Decrease button
        ItemStack decreaseOf5 = createButton(Material.REDSTONE_BLOCK, 5, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " - 5"));
        inv.setItem(10, decreaseOf5);

        // Decrease button
        ItemStack decreaseOf10 = createButton(Material.REDSTONE_BLOCK, 10, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " - 10"));
        inv.setItem(19, decreaseOf10);

        // Decrease button
        ItemStack decreaseOf50 = createButton(Material.REDSTONE_BLOCK, 50, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " - 50"));
        inv.setItem(28, decreaseOf50);

        // Decrease button
        ItemStack decreaseOf100 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val +  " - 100"));
        inv.setItem(37, decreaseOf100);


        // Create a button and set the position
        Material watch = Material.matchMaterial( "watch" );
        if ( watch == null ) {
            watch = Material.matchMaterial( "legacy_watch" );
        } if ( watch == null ) {
            watch = Material.matchMaterial( "clock" );
        }
        ItemStack confirmButton = createButton(watch, 1, confirmButtonLore, SpigotPrison.format("&3" + "Confirm: " + minename + " " + blockName +  " " + val));
        inv.setItem(22, confirmButton);


        // Increase button
        ItemStack increseOf1 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " + 1" ));
        inv.setItem(7, increseOf1);

        // Increase button
        ItemStack increaseOf5 = createButton(Material.EMERALD_BLOCK, 5, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " + 5"));
        inv.setItem(16, increaseOf5);

        // Increase button
        ItemStack increaseOf10 = createButton(Material.EMERALD_BLOCK, 10, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " + 10"));
        inv.setItem(25, increaseOf10);

        // Increase button
        ItemStack increaseOf50 = createButton(Material.EMERALD_BLOCK, 50, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " + 50"));
        inv.setItem(34, increaseOf50);

        // Increase button
        ItemStack increaseOf100 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + blockName +  " " + val + " + 100"));
        inv.setItem(43, increaseOf100);
    }

}

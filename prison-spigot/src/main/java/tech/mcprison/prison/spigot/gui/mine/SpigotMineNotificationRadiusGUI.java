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
public class SpigotMineNotificationRadiusGUI extends SpigotGUIComponents {

    private final Player p;
    private final String minename;
    private final long val;
    private final String typeNotification;

    public SpigotMineNotificationRadiusGUI(Player p, Long val, String typeNotification, String minename){
        this.p = p;
        this.val = val;
        this.minename = minename;
        this.typeNotification = typeNotification;
    }

    public void open() {

        // Create a new inventory
        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineNotifications -> Radius"));

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
        // Create new lore
        List<String> changeDecreaseValueLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToDecrease")
        );

        // Create a new lore
        List<String> confirmButtonLore = createLore(
                guiConfig.getString("Gui.Lore.LeftClickToConfirm"),
                guiConfig.getString("Gui.Lore.Radius") + val,
                guiConfig.getString("Gui.Lore.RightClickToCancel")
        );

        // Create a new lore
        List<String> changeIncreaseValueLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToIncrease")
        );

        // Decrease buttons
        ItemStack decreaseOf1 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " - 1 " + typeNotification ));
        inv.setItem(1, decreaseOf1);

        // Decrease buttons
        ItemStack decreaseOf5 = createButton(Material.REDSTONE_BLOCK, 5, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " - 5 " + typeNotification));
        inv.setItem(10, decreaseOf5);

        // Decrease buttons
        ItemStack decreaseOf10 = createButton(Material.REDSTONE_BLOCK, 10, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " - 10 " + typeNotification));
        inv.setItem(19, decreaseOf10);

        // Decrease buttons
        ItemStack decreaseOf50 = createButton(Material.REDSTONE_BLOCK, 50, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " - 50 " + typeNotification));
        inv.setItem(28, decreaseOf50);

        // Decrease buttons
        ItemStack decreaseOf100 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " - 100 " + typeNotification));
        inv.setItem(37, decreaseOf100);


        // Create a button and set the position of it
        Material watch = Material.matchMaterial( "watch" );
        if ( watch == null ) {
        	watch = Material.matchMaterial( "legacy_watch" );
        } if ( watch == null ) {
        	watch = Material.matchMaterial( "clock" );
        }
        ItemStack confirmButton = createButton(watch, 1, confirmButtonLore, SpigotPrison.format("&3" + "Confirm: " + minename + " " + val + " " + typeNotification));
        inv.setItem(22, confirmButton);


        // Increase buttons
        ItemStack increseOf1 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " + 1 " + typeNotification));
        inv.setItem(7, increseOf1);

        // Increase buttons
        ItemStack increaseOf5 = createButton(Material.EMERALD_BLOCK, 5, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " + 5 " + typeNotification));
        inv.setItem(16, increaseOf5);

        // Increase buttons
        ItemStack increaseOf10 = createButton(Material.EMERALD_BLOCK, 10, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " + 10 " + typeNotification));
        inv.setItem(25, increaseOf10);

        // Increase buttons
        ItemStack increaseOf50 = createButton(Material.EMERALD_BLOCK, 50, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " + 50 " + typeNotification));
        inv.setItem(34, increaseOf50);

        // Increase buttons
        ItemStack increaseOf100 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + minename + " " + val + " + 100 " + typeNotification));
        inv.setItem(43, increaseOf100);
    }

}

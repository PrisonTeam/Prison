package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMineNotificationRadiusGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private final long val;
    private final String typeNotification;
    private final Configuration messages = configs("messages");

    public SpigotMineNotificationRadiusGUI(Player p, Long val, String typeNotification, String mineName){
        this.p = p;
        this.val = val;
        this.mineName = mineName;
        this.typeNotification = typeNotification;
    }

    public void open() {

        // Create a new inventory
        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineNotifications -> Radius"));

        if (guiBuilder(inv)) return;

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {


        // Create new lore
        List<String> changeDecreaseValueLore = createLore(
                messages.getString("Gui.Lore.ClickToDecrease")
        );
        List<String> confirmButtonLore = createLore(
                messages.getString("Gui.Lore.LeftClickToConfirm"),
                messages.getString("Gui.Lore.Radius") + val,
                messages.getString("Gui.Lore.RightClickToCancel")
        );
        List<String> changeIncreaseValueLore = createLore(
                messages.getString("Gui.Lore.ClickToIncrease")
        );

        // Decrease buttons
        ItemStack decreaseOf1 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " - 1 " + typeNotification ));
        inv.setItem(1, decreaseOf1);
        ItemStack decreaseOf5 = createButton(Material.REDSTONE_BLOCK, 5, changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " - 5 " + typeNotification));
        inv.setItem(10, decreaseOf5);
        ItemStack decreaseOf10 = createButton(Material.REDSTONE_BLOCK, 10, changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " - 10 " + typeNotification));
        inv.setItem(19, decreaseOf10);
        ItemStack decreaseOf50 = createButton(Material.REDSTONE_BLOCK, 50, changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " - 50 " + typeNotification));
        inv.setItem(28, decreaseOf50);
        ItemStack decreaseOf100 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " - 100 " + typeNotification));
        inv.setItem(37, decreaseOf100);


        // Create a button and set the position of it
        Material watch = Material.matchMaterial( "watch" );
        if ( watch == null ) {
        	watch = Material.matchMaterial( "legacy_watch" );
        } if ( watch == null ) {
        	watch = Material.matchMaterial( "clock" );
        }
        ItemStack confirmButton = createButton(watch, 1, confirmButtonLore, SpigotPrison.format("&3" + "Confirm: " + mineName + " " + val + " " + typeNotification));
        inv.setItem(22, confirmButton);


        // Increase buttons
        ItemStack increseOf1 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " + 1 " + typeNotification));
        inv.setItem(7, increseOf1);
        ItemStack increaseOf5 = createButton(Material.EMERALD_BLOCK, 5, changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " + 5 " + typeNotification));
        inv.setItem(16, increaseOf5);
        ItemStack increaseOf10 = createButton(Material.EMERALD_BLOCK, 10, changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " + 10 " + typeNotification));
        inv.setItem(25, increaseOf10);
        ItemStack increaseOf50 = createButton(Material.EMERALD_BLOCK, 50, changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " + 50 " + typeNotification));
        inv.setItem(34, increaseOf50);
        ItemStack increaseOf100 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + mineName + " " + val + " + 100 " + typeNotification));
        inv.setItem(43, increaseOf100);
    }

}

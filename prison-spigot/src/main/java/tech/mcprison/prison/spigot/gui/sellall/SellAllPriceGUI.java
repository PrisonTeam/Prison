package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SellAllPriceGUI extends SpigotGUIComponents {

    private final Player p;
    private final String itemID;
    private final Double val;

    public SellAllPriceGUI(Player p, Double val, String itemID){
        this.p = p;
        this.val = val;
        this.itemID = itemID;
    }

    public void open() {

        // Create a new inventory
        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3SellAll -> ItemValue"));

        if (guiBuilder(inv)) return;

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {

        // Create a new lore
        List<String> changeDecreaseValueLore;
        changeDecreaseValueLore = createLore(
                messages.getString("Lore.ClickToDecrease")
        );
        List<String> confirmButtonLore = createLore(
                messages.getString("Lore.LeftClickToConfirm"),
                messages.getString("Lore.Price2") + val,
                messages.getString("Lore.RightClickToCancel")
        );
        List<String> changeIncreaseValueLore = createLore(
                messages.getString("Lore.ClickToIncrease")
        );

        Material decreaseMat = XMaterial.REDSTONE_BLOCK.parseMaterial();
        ItemStack decreaseStack = XMaterial.REDSTONE_BLOCK.parseItem();

        // Decrease button
        ItemStack decreaseOf1 = createButton(decreaseStack, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 1" ));
        inv.setItem(1, decreaseOf1);
        ItemStack decreaseOf5 = createButton(new ItemStack(decreaseMat, 10), changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 10"));
        inv.setItem(10, decreaseOf5);
        ItemStack decreaseOf10 = createButton(decreaseStack, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 100"));
        inv.setItem(19, decreaseOf10);
        ItemStack decreaseOf50 = createButton(decreaseStack, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 1000"));
        inv.setItem(28, decreaseOf50);
        ItemStack decreaseOf100 = createButton(decreaseStack, changeDecreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " - 10000"));
        inv.setItem(37, decreaseOf100);


        // Create a button and set the position
        ItemStack confirmButton = createButton(XMaterial.TRIPWIRE_HOOK.parseItem(), confirmButtonLore, SpigotPrison.format("&3" + "Confirm: " + itemID + " " + val));
        inv.setItem(22, confirmButton);

        Material increaseMat = XMaterial.EMERALD_BLOCK.parseMaterial();
        ItemStack increaseStack = XMaterial.EMERALD_BLOCK.parseItem();

        // Increase button
        ItemStack increseOf1 = createButton(increaseStack, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 1" ));
        inv.setItem(7, increseOf1);
        ItemStack increaseOf5 = createButton(new ItemStack(increaseMat, 10), changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 10"));
        inv.setItem(16, increaseOf5);
        ItemStack increaseOf10 = createButton(increaseStack, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 100"));
        inv.setItem(25, increaseOf10);
        ItemStack increaseOf50 = createButton(increaseStack, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 1000"));
        inv.setItem(34, increaseOf50);
        ItemStack increaseOf100 = createButton(increaseStack, changeIncreaseValueLore, SpigotPrison.format("&3" + itemID + " " + val + " + 10000"));
        inv.setItem(43, increaseOf100);
    }
}

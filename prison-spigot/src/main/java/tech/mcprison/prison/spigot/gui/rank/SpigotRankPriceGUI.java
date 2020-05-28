package tech.mcprison.prison.spigot.gui.rank;

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
public class SpigotRankPriceGUI extends SpigotGUIComponents {

    private final Player p;
    private final String rankName;
    private final Integer val;

    public SpigotRankPriceGUI(Player p, Integer val, String rankname){
        this.p = p;
        this.val = val;
        this.rankName = rankname;
    }

    public void open() {

        // Create a new inventory
        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3RankManager -> RankPrice"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // Create a new lore
        List<String> changeDecreaseValueLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToDecrease")
        );

        // Create a new lore
        List<String> confirmButtonLore = createLore(
                GuiConfig.getString("Gui.Lore.LeftClickToConfirm"),
                GuiConfig.getString("Gui.Lore.Price2") + val,
                GuiConfig.getString("Gui.Lore.RightClickToCancel")
        );

        // Create a new lore
        List<String> changeIncreaseValueLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToIncrease")
        );


        // Decrease button
        ItemStack decreaseOf1 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 1" ));
        inv.setItem(1, decreaseOf1);

        // Decrease button
        ItemStack decreaseOf5 = createButton(Material.REDSTONE_BLOCK, 5, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 10"));
        inv.setItem(10, decreaseOf5);

        // Decrease button
        ItemStack decreaseOf10 = createButton(Material.REDSTONE_BLOCK, 10, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 100"));
        inv.setItem(19, decreaseOf10);

        // Decrease button
        ItemStack decreaseOf50 = createButton(Material.REDSTONE_BLOCK, 50, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 1000"));
        inv.setItem(28, decreaseOf50);

        // Decrease button
        ItemStack decreaseOf100 = createButton(Material.REDSTONE_BLOCK, 1, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 10000"));
        inv.setItem(37, decreaseOf100);



        // Create a button and set the position
        ItemStack confirmButton = createButton(Material.TRIPWIRE_HOOK, 1, confirmButtonLore, SpigotPrison.format("&3" + "Confirm: " + rankName + " " + val));
        inv.setItem(22, confirmButton);



        // Increase button
        ItemStack increseOf1 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 1" ));
        inv.setItem(7, increseOf1);

        // Increase button
        ItemStack increaseOf5 = createButton(Material.EMERALD_BLOCK, 5, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 10"));
        inv.setItem(16, increaseOf5);

        // Increase button
        ItemStack increaseOf10 = createButton(Material.EMERALD_BLOCK, 10, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 100"));
        inv.setItem(25, increaseOf10);

        // Increase button
        ItemStack increaseOf50 = createButton(Material.EMERALD_BLOCK, 50, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 1000"));
        inv.setItem(34, increaseOf50);

        // Increase button
        ItemStack increaseOf100 = createButton(Material.EMERALD_BLOCK, 1, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 10000"));
        inv.setItem(43, increaseOf100);

        // Open the inventory
        this.p.openInventory(inv);
    }

}

package tech.mcprison.prison.spigot.gui.rank;

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

public class SpigotConfirmPrestigeGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotConfirmPrestigeGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Create the inventory
        int dimension = 9;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Prestige -> Confirmation"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (guiBuilder(inv, GuiConfig)) return;

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
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
        // Blocks of the mine
        List<String> confirmlore = createLore(
                guiConfig.getString("Gui.Lore.ClickToConfirm"),
                guiConfig.getString("Gui.Lore.PrestigeWarning"),
                guiConfig.getString("Gui.Lore.PrestigeWarning2"),
                guiConfig.getString("Gui.Lore.PrestigeWarning3")
        );

        // Blocks of the mine
        List<String> cancelore = createLore(
                guiConfig.getString("Gui.Lore.ClickToCancel"));

        // Create the button, set up the material, amount, lore and name
        ItemStack confirm = createButton(Material.EMERALD_BLOCK, 1, confirmlore, SpigotPrison.format("&3" + "Confirm: Prestige"));

        // Create the button, set up the material, amount, lore and name
        ItemStack cancel = createButton(Material.REDSTONE_BLOCK, 1, cancelore, SpigotPrison.format("&3" + "Cancel: Don't Prestige"));

        // Position of the button
        inv.setItem(2, confirm);

        // Position of the button
        inv.setItem(6, cancel);
    }

}

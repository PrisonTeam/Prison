package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.util.List;

public class PrisonSetupGUI extends SpigotGUIComponents{

    private final Player p;
    private final Configuration messages = messages();

    public PrisonSetupGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Create the inventory
        int dimension = 9;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Prison Setup -> Confirmation"));

        if (guiBuilder(inv)) return;

        // Open the inventory
        openGUI(p, inv);
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


        // Blocks of the mine
        List<String> confirmLore = createLore(
                messages.getString("Gui.Lore.ClickToConfirm"),
                messages.getString("Gui.Lore.noRanksFoundSetup"),
                messages.getString("Gui.Lore.noRanksFoundSetup1"),
                messages.getString("Gui.Lore.noRanksFoundSetup2"),
                messages.getString("Gui.Lore.noRanksFoundSetup3"),
                messages.getString("Gui.Lore.noRanksFoundSetup4"),
                messages.getString("Gui.Lore.noRanksFoundSetup5"),
                messages.getString("Gui.Lore.noRanksFoundSetup6"),
                messages.getString("Gui.Lore.noRanksFoundSetup7"),
                messages.getString("Gui.Lore.noRanksFoundSetup8")
        );

        // Blocks of the mine
        List<String> cancelLore = createLore(
                messages.getString("Gui.Lore.ClickToCancel"));

        // Create the button, set up the material, amount, lore and name
        ItemStack confirm = createButton(Material.EMERALD_BLOCK, 1, confirmLore, SpigotPrison.format("&3" + "Confirm: Setup"));
        ItemStack cancel = createButton(Material.REDSTONE_BLOCK, 1, cancelLore, SpigotPrison.format("&3" + "Cancel: Setup"));

        // Position of the button
        inv.setItem(2, confirm);
        inv.setItem(6, cancel);
    }
}

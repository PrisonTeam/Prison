package tech.mcprison.prison.spigot.gui;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.util.List;

/**
 * @author GABRYCA
 */
public class PrisonSetupGUI extends SpigotGUIComponents{

    private final Player p;

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
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {


        // Blocks of the mine
        List<String> confirmLore = createLore(
                messages.getString("Lore.ClickToConfirm"),
                messages.getString("Lore.noRanksFoundSetup"),
                messages.getString("Lore.noRanksFoundSetup1"),
                messages.getString("Lore.noRanksFoundSetup2"),
                messages.getString("Lore.noRanksFoundSetup3"),
                messages.getString("Lore.noRanksFoundSetup4"),
                messages.getString("Lore.noRanksFoundSetup5"),
                messages.getString("Lore.noRanksFoundSetup6"),
                messages.getString("Lore.noRanksFoundSetup7"),
                messages.getString("Lore.noRanksFoundSetup8")
        );

        // Blocks of the mine
        List<String> cancelLore = createLore(
                messages.getString("Lore.ClickToCancel"));

        // Create the button, set up the material, amount, lore and name
        ItemStack confirm = createButton(XMaterial.EMERALD_BLOCK.parseItem(), confirmLore, SpigotPrison.format("&3" + "Confirm: Setup"));
        ItemStack cancel = createButton(XMaterial.REDSTONE_BLOCK.parseItem(), cancelLore, SpigotPrison.format("&3" + "Cancel: Setup"));

        // Position of the button
        inv.setItem(2, confirm);
        inv.setItem(6, cancel);
    }
}

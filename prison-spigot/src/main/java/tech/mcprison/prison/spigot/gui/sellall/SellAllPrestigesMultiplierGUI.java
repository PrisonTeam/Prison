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
public class SellAllPrestigesMultiplierGUI extends SpigotGUIComponents {

    private final Player p;
    private int counter;

    public SellAllPrestigesMultiplierGUI(Player p, int counter){
        this.p = p;
        this.counter = counter;
    }

    public void open() {

        // Create a new inventory
        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3SellAll -> Multipliers"));

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

        // Page elements.
        int pageSize = 45;

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() && i < counter + pageSize; i++ ) {

            int flagValue = 0;
            for (String prestige : sellAllConfig.getConfigurationSection("Multiplier").getKeys(false)) {

                if (flagValue == i) {
                    List<String> loreMult = createLore(
                            messages.getString("Lore.PrestigeName") + sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME"),
                            messages.getString("Lore.PrestigeMultiplier") + sellAllConfig.getString("Multiplier." + prestige + ".MULTIPLIER"),
                            "",
                            messages.getString("Lore.ClickToEdit"),
                            messages.getString("Lore.RightClickToDelete")
                    );

                    ItemStack prestMultiplierButton = createButton(XMaterial.PAPER.parseItem(), loreMult, sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME"));

                    inv.addItem(prestMultiplierButton);
                }
                flagValue++;
            }
        }

        if (i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size()) {
            List<String> nextPageLore = createLore(messages.getString("Lore.ClickToNextPage"));

            ItemStack nextPageButton = createButton(Material.BOOK, 1, nextPageLore, "&7Next " + (i + 1));
            inv.setItem(53, nextPageButton);
        }
        if (i >= (pageSize * 2)) {
            List<String> priorPageLore = createLore(messages.getString("Lore.ClickToPriorPage"));

            ItemStack priorPageButton = createButton(Material.BOOK, 1, priorPageLore,
                    "&7Prior " + (i - (pageSize * 2) - 1));
            inv.setItem(51, priorPageButton);
        }
    }
}

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
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SellAllPrestigesMultiplierGUI extends SpigotGUIComponents {

    private final Player p;
    private int counter;
    private int dimension = 45;
    private Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3SellAll -> Multipliers"));

    public SellAllPrestigesMultiplierGUI(Player p, int counter){
        this.p = p;
        this.counter = counter;
    }

    public void open() {

        updateSellAllConfig();

        if (guiBuilder()) return;

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder() {
        try {
            buttonsSetup();
        } catch (NullPointerException ex){
            Output.get().sendWarn(new SpigotPlayer(p), "&cThere's a null value in the GuiConfig.yml [broken]");
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup() {

        // Page elements.
        int pageSize = 45;

        // Global strings.
        String lorePrestigeName = messages.getString("Lore.PrestigeName");
        String lorePrestigeMultiplier = messages.getString("Lore.PrestigeMultiplier");
        String loreClickToEdit = messages.getString("Lore.ClickToEdit");
        String loreClickToDelete =  messages.getString("Lore.RightClickToDelete");

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() && i < counter + pageSize; i++ ) {

            int flagValue = 0;
            for (String prestige : sellAllConfig.getConfigurationSection("Multiplier").getKeys(false)) {

                if (flagValue == i) {
                    List<String> loreMult = createLore(
                            lorePrestigeName + sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME"),
                            lorePrestigeMultiplier + sellAllConfig.getString("Multiplier." + prestige + ".MULTIPLIER"),
                            "",
                            loreClickToEdit,
                            loreClickToDelete
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

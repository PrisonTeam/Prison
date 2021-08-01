package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

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

        updateSellAllConfig();

        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3SellAll -> Multipliers");

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

                    gui.addButton(new Button(null, XMaterial.PAPER, loreMult, sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME")));
                }
                flagValue++;
            }
        }

        if (i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size()) {
            List<String> nextPageLore = createLore(messages.getString("Lore.ClickToNextPage"));

            gui.addButton(new Button(53, XMaterial.BOOK, nextPageLore, "&7Next " + (i + 1)));
        }
        if (i >= (pageSize * 2)) {
            List<String> priorPageLore = createLore(messages.getString("Lore.ClickToPriorPage"));


            gui.addButton(new Button(51, XMaterial.BOOK, priorPageLore, "&7Prior " + (i - (pageSize * 2) - 1)));
        }

        gui.open();
    }
}

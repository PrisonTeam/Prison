package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

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
        String lorePrestigeName = messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_name);
        String lorePrestigeMultiplier = messages.getString(MessagesConfig.StringID.spigot_gui_lore_multiplier);
        String loreClickToEdit = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_edit);
        String loreClickToDelete =  messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_delete);

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() && i < counter + pageSize; i++ ) {

            int flagValue = 0;
            for (String prestige : sellAllConfig.getConfigurationSection("Multiplier").getKeys(false)) {

                if (flagValue == i) {

                    ButtonLore loreMult = new ButtonLore(createLore(
                            loreClickToEdit,
                            loreClickToDelete), createLore(
                                    lorePrestigeName + " " + sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME"),
                            lorePrestigeMultiplier + " " + sellAllConfig.getString("Multiplier." + prestige + ".MULTIPLIER")));

                    gui.addButton(new Button(null, XMaterial.PAPER, loreMult, sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME")));
                }
                flagValue++;
            }
        }

        if (i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size()) {
            gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (i + 1)));
        }
        if (i >= (pageSize * 2)) {
            gui.addButton(new Button(51, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior " + (i - (pageSize * 2) - 1)));
        }

        gui.open();
    }
}

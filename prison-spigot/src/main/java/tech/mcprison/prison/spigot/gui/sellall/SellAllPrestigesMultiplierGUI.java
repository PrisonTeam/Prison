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
//        String lorePrestigeName = messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_name);
//        String lorePrestigeMultiplier = messages.getString(MessagesConfig.StringID.spigot_gui_lore_multiplier);
        String loreClickToEdit = guiClickToEditMsg();
        String loreClickToDelete =  guiRightClickToDeleteMsg();

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() && i < counter + pageSize; i++ ) {

            int flagValue = 0;
            for (String prestige : sellAllConfig.getConfigurationSection("Multiplier").getKeys(false)) {

                if (flagValue == i) {

                	String multPrestigeName = sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME");
                	String multPrestigeMultiplier = sellAllConfig.getString("Multiplier." + prestige + ".MULTIPLIER");
                	
                    ButtonLore loreMult = new ButtonLore(
                    		createLore(
                    				loreClickToEdit,
                    				loreClickToDelete), 
                    		createLore(
                            		guiPrestigeNameMsg( multPrestigeName ),
                                    guiMultiplierMsg( multPrestigeMultiplier ) ));

                    gui.addButton(new Button(null, XMaterial.PAPER, loreMult, sellAllConfig.getString("Multiplier." + prestige + ".PRESTIGE_NAME")));
                }
                flagValue++;
            }
        }

        if (i < sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size()) {
            gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(
            		guiPageNextMsg(), null), "&7Next " + (i + 1)));
        }
        if (i >= (pageSize * 2)) {
            gui.addButton(new Button(51, XMaterial.BOOK, new ButtonLore(
            		guiPagePriorMsg(), null), "&7Prior " + (i - (pageSize * 2) - 1)));
        }

        gui.open();
    }
}

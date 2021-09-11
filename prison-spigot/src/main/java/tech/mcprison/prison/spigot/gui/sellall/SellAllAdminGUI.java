package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SellAllAdminGUI extends SpigotGUIComponents {

    private final Player p;
    int dimension = 27;

    public SellAllAdminGUI(Player p) {
        this.p = p;
    }

    public void open() {

        if (!SpigotPrison.getInstance().getConfig().getString("sellall").equalsIgnoreCase("true")){
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_disabled));
            return;
        }

        updateSellAllConfig();

        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Prison -> SellAll-Admin");

        ButtonLore blocksLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), null);
        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);
        ButtonLore setCurrencyLore = new ButtonLore(createLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_edit)), createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_currency) + " " + sellAllConfig.getString("Options.SellAll_Currency"),
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_edit)));
        ButtonLore multipliersLore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open), messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_edit));
        ButtonLore autoSellLore = new ButtonLore();
        ButtonLore sellAllDelayLore = new ButtonLore();

        Button autoSellButton;
        Button sellAllDelayButton;

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")){
            autoSellLore.setLoreAction(createLore(
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_disable)
                    ));

            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&3AutoSell");
        } else {
            autoSellLore.setLoreAction(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_enable));
            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&cAutoSell-Disabled");
        }

        if (sellAllConfig.getString("Options.Sell_Delay_Enabled").equalsIgnoreCase("true")){

            sellAllDelayLore.setLoreAction(createLore(
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_open),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_right_to_disable)));
            sellAllDelayLore.setLoreDescription(createLore(
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_delay) + " " + sellAllConfig.getString("Options.Sell_Delay_Seconds") + "s",
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_1),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_2)));

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&3Delay-Enabled");
        } else {

            sellAllDelayLore.setLoreAction(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_enable));
            sellAllDelayLore.setLoreDescription(createLore(
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_1),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_2)));

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&cDelay-Disabled");
        }

        try {
            if (sellAllConfig.getConfigurationSection("Multiplier") == null) {
                multipliersLore.addLineLoreDescription(SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_gui_lore_no_multipliers)));
            } else if (sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() == 0) {
                multipliersLore.addLineLoreDescription(SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_gui_lore_no_multipliers)));
            }
        } catch (NullPointerException ex){
            multipliersLore.addLineLoreDescription(SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_gui_lore_no_multipliers)));
        }

        gui.addButton(new Button(15, XMaterial.EMERALD, setCurrencyLore, SpigotPrison.format("&3SellAll-Currency")));
        gui.addButton(new Button(8, XMaterial.PAPER, multipliersLore, SpigotPrison.format("&3Prestige-Multipliers")));
        gui.addButton(new Button(0, XMaterial.DIAMOND_ORE, blocksLore, "&3Blocks-Shop"));
        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));
        gui.addButton(sellAllDelayButton);
        gui.addButton(autoSellButton);

        gui.open();
    }
}

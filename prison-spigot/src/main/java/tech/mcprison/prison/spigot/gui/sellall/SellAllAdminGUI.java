package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
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
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString("Message.SellAllIsDisabled"));
            return;
        }

        updateSellAllConfig();

        PrisonGUI gui = new PrisonGUI(p, dimension, "&3Prison -> SellAll-Admin");

        ButtonLore blocksLore = new ButtonLore(messages.getString("Lore.ClickToOpen"), null);
        ButtonLore closeGUILore = new ButtonLore(messages.getString("Lore.ClickToClose"), null);
        ButtonLore setCurrencyLore = new ButtonLore(createLore(messages.getString("Lore.ClickToEdit")), createLore(
                messages.getString("Lore.SellAllActiveCurrency") + sellAllConfig.getString("Options.SellAll_Currency"),
                messages.getString("Lore.SellAllCurrencyInfo")));
        ButtonLore multipliersLore = new ButtonLore(messages.getString("Lore.ClickToOpen"), messages.getString("Lore.PrestigeMultiplierInfoGUI"));
        ButtonLore autoSellLore = new ButtonLore();
        ButtonLore sellAllDelayLore = new ButtonLore();

        Button autoSellButton;
        Button sellAllDelayButton;

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")){
            autoSellLore.setLoreAction(createLore(
                    messages.getString("Lore.ClickToOpen"),
                    messages.getString("Lore.RightClickToDisable")
                    ));

            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&3AutoSell");
        } else {
            autoSellLore.setLoreAction(messages.getString("Lore.RightClickToEnable"));
            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&cAutoSell-Disabled");
        }

        if (sellAllConfig.getString("Options.Sell_Delay_Enabled").equalsIgnoreCase("true")){

            sellAllDelayLore.setLoreAction(createLore(
                    messages.getString("Lore.ClickToOpen"),
                    messages.getString("Lore.RightClickToDisable")));
            sellAllDelayLore.setLoreDescription(createLore(
                    messages.getString("Lore.DelaySellAll") + sellAllConfig.getString("Options.Sell_Delay_Seconds") + "s",
                    messages.getString("Lore.SellAllDelayInfo"),
                    messages.getString("Lore.SellAllDelayInfo2")));

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&3Delay-Enabled");
        } else {

            sellAllDelayLore.setLoreAction(messages.getString("Lore.RightClickToEnable"));
            sellAllDelayLore.setLoreDescription(createLore(
                    messages.getString("Lore.SellAllDelayInfo"),
                    messages.getString("Lore.SellAllDelayInfo2")));

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&cDelay-Disabled");
        }

        try {
            if (sellAllConfig.getConfigurationSection("Multiplier") == null) {
                multipliersLore.addLineLoreDescription(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
            } else if (sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() == 0) {
                multipliersLore.addLineLoreDescription(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
            }
        } catch (NullPointerException ex){
            multipliersLore.addLineLoreDescription(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
        }

        gui.addButton(new Button(15, XMaterial.EMERALD, setCurrencyLore, SpigotPrison.format("&3SellAll-Currency")));
        gui.addButton(new Button(8, XMaterial.PAPER, multipliersLore, SpigotPrison.format("&3Prestige-Multipliers")));
        gui.addButton(new Button(0, XMaterial.DIAMOND_ORE, blocksLore, "&3Blocks-Shop"));
        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));
        gui.addButton(sellAllDelayButton);
        gui.addButton(autoSellButton);

        gui.open();
    }
}

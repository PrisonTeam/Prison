package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

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

        List<String> blocksLore = createLore(
                messages.getString("Lore.ClickToOpen")
        );
        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );
        List<String> setCurrencyLore = createLore(
                messages.getString("Lore.SellAllActiveCurrency") + sellAllConfig.getString("Options.SellAll_Currency"),
                messages.getString("Lore.ClickToEdit"),
                "",
                messages.getString("Lore.SellAllCurrencyInfo")
        );
        List<String> multipliersLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                "",
                messages.getString("Lore.PrestigeMultiplierInfoGUI")
        );
        List<String> autoSellLore;
        List<String> sellAllDelayLore;

        Button autoSellButton;
        Button sellAllDelayButton;

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")){
            autoSellLore = createLore(
                    messages.getString("Lore.ClickToOpen"),
                    messages.getString("Lore.RightClickToDisable")
            );
            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&3AutoSell");
        } else {
            autoSellLore = createLore(
                    messages.getString("Lore.RightClickToEnable")
            );
            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&cAutoSell-Disabled");
        }

        if (sellAllConfig.getString("Options.Sell_Delay_Enabled").equalsIgnoreCase("true")){

            sellAllDelayLore = createLore(
                    messages.getString("Lore.ClickToOpen"),
                    messages.getString("Lore.DelaySellAll") + sellAllConfig.getString("Options.Sell_Delay_Seconds") + "s",
                    messages.getString("Lore.RightClickToDisable"),
                    "",
                    messages.getString("Lore.SellAllDelayInfo"),
                    messages.getString("Lore.SellAllDelayInfo2")
            );

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&3Delay-Enabled");
        } else {

            sellAllDelayLore = createLore(
                    messages.getString("Lore.RightClickToEnable"),
                    "",
                    messages.getString("Lore.SellAllDelayInfo"),
                    messages.getString("Lore.SellAllDelayInfo2")
            );

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&cDelay-Disabled");
        }

        try {
            if (sellAllConfig.getConfigurationSection("Multiplier") == null) {
                multipliersLore.add(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
            } else if (sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() == 0) {
                multipliersLore.add(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
            }
        } catch (NullPointerException ex){
            multipliersLore.add(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
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

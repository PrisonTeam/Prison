package tech.mcprison.prison.spigot.gui.sellall;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
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
public class SellAllAdminGUI extends SpigotGUIComponents {

    private final Player p;
    int dimension = 27;

    public SellAllAdminGUI(Player p) {
        this.p = p;
    }

    public void open() {

        if (!SpigotPrison.getInstance().getConfig().getString("sellall").equalsIgnoreCase("true")){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.SellAllIsDisabled")));
            return;
        }

        if (guiBuilder()) return;

        Inventory inv = buttonsSetup();

        openGUI(p, inv);
    }


    private boolean guiBuilder() {
        try {
            buttonsSetup();
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private Inventory buttonsSetup() {

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Prison -> SellAll-Admin"));

        List<String> blocksLore = createLore(
                messages.getString("Lore.ClickToOpen")
        );

        ItemStack autoSellButton;
        List<String> autoSellLore;
        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")){
            autoSellLore = createLore(
                    messages.getString("Lore.ClickToOpen"),
                    messages.getString("Lore.RightClickToDisable")
            );
            autoSellButton = createButton(XMaterial.CHEST.parseItem(), autoSellLore, "&3AutoSell");
        } else {
            autoSellLore = createLore(
                    messages.getString("Lore.RightClickToEnable")
            );
            autoSellButton = createButton(XMaterial.CHEST.parseItem(), autoSellLore, "&cAutoSell-Disabled");
        }

        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        ItemStack sellAllDelayButton;
        List<String> sellAllDelayLore;
        if (sellAllConfig.getString("Options.Sell_Delay_Enabled").equalsIgnoreCase("true")){

            sellAllDelayLore = createLore(
                    messages.getString("Lore.ClickToOpen"),
                    messages.getString("Lore.DelaySellAll") + sellAllConfig.getString("Options.Sell_Delay_Seconds") + "s",
                    messages.getString("Lore.RightClickToDisable"),
                    "",
                    messages.getString("Lore.SellAllDelayInfo"),
                    messages.getString("Lore.SellAllDelayInfo2")
            );

            sellAllDelayButton = createButton(XMaterial.CLOCK.parseItem(), sellAllDelayLore, "&3Delay-Enabled");
        } else {

            sellAllDelayLore = createLore(
                    messages.getString("Lore.RightClickToEnable"),
                    "",
                    messages.getString("Lore.SellAllDelayInfo"),
                    messages.getString("Lore.SellAllDelayInfo2")
            );

            sellAllDelayButton = createButton(XMaterial.CLOCK.parseItem(), sellAllDelayLore, "&cDelay-Disabled");
        }

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

        try {
            if (sellAllConfig.getConfigurationSection("Multiplier") == null) {
                multipliersLore.add(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
            } else if (sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() == 0) {
                multipliersLore.add(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
            }
        } catch (NullPointerException ex){
            multipliersLore.add(SpigotPrison.format(messages.getString("Lore.EmptyMultiplier")));
        }

        ItemStack setCurrencyButton = createButton(XMaterial.EMERALD.parseItem(), setCurrencyLore, SpigotPrison.format("&3SellAll-Currency"));
        ItemStack multipliersButton = createButton(XMaterial.PAPER.parseItem(), multipliersLore, SpigotPrison.format("&3Prestige-Multipliers"));
        ItemStack blocksButton = createButton(XMaterial.DIAMOND_ORE.parseItem(), blocksLore, "&3Blocks-Shop");
        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));

        inv.setItem(0, blocksButton);
        inv.setItem(8, multipliersButton);
        inv.setItem(11, sellAllDelayButton);
        inv.setItem(13, autoSellButton);
        inv.setItem(15, setCurrencyButton);
        inv.setItem(dimension - 1, closeGUI);

        return inv;
    }
}

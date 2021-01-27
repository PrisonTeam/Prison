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
public class SellAllAdminAutoSellGUI extends SpigotGUIComponents {

    private final Player p;
    int dimension = 27;

    public SellAllAdminAutoSellGUI(Player p) {
        this.p = p;
    }

    public void open() {

        if (guiBuilder()) return;

        Inventory inv = buttonsSetup();

        openGUI(p, inv);
    }

    private Inventory buttonsSetup() {

        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3SellAll -> AutoSell"));

        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        List<String> perUserToggleableLore;
        List<String> enableDisableLore;

        ItemStack perUserToggleableButton;
        ItemStack enableDisableButton;

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable").equalsIgnoreCase("true")){
            perUserToggleableLore = createLore(
                    messages.getString("Lore.ClickToDisable")
            );
            perUserToggleableButton = createButton(XMaterial.LIME_STAINED_GLASS_PANE.parseItem(), perUserToggleableLore, "&3PerUserToggleable");
        } else {
            perUserToggleableLore = createLore(
                    messages.getString("Lore.ClickToEnable")
            );
            perUserToggleableButton = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), perUserToggleableLore, "&cPerUserToggleable-Disabled");
        }

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")){
            enableDisableLore = createLore(
                    messages.getString("Lore.ClickToDisable")
            );
            enableDisableButton = createButton(XMaterial.LIME_STAINED_GLASS_PANE.parseItem(), enableDisableLore, "&3AutoSell");
        } else {
            enableDisableLore = createLore(
                    messages.getString("Lore.ClickToEnable")
            );
            enableDisableButton = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), enableDisableLore, "&cAutoSell-Disabled");
        }

        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));

        inv.setItem(11, perUserToggleableButton);
        inv.setItem(15, enableDisableButton);
        inv.setItem(dimension - 1, closeGUI);
        return inv;
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
}

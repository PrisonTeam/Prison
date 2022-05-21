package tech.mcprison.prison.spigot.gui.sellall;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

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

        updateSellAllConfig();

        PrisonGUI gui = new PrisonGUI(p, dimension, "&3SellAll -> AutoSell");

        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);
        ButtonLore perUserToggleableLore = new ButtonLore();
        ButtonLore enableDisableLore = new ButtonLore();

        Button perUserToggleableButton;
        Button enableDisableButton;

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell_perUserToggleable").equalsIgnoreCase("true")){
            perUserToggleableLore.setLoreAction(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_disable));
            perUserToggleableButton = new Button(11, XMaterial.LIME_STAINED_GLASS_PANE, perUserToggleableLore, "&3PerUserToggleable");
        } else {
            perUserToggleableLore.setLoreAction(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_enable));
            perUserToggleableButton = new Button(11, XMaterial.RED_STAINED_GLASS_PANE, perUserToggleableLore, "&cPerUserToggleable-Disabled");
        }

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")){
            enableDisableLore.setLoreAction(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_disable));
            enableDisableButton = new Button(15, XMaterial.LIME_STAINED_GLASS_PANE, enableDisableLore, "&3AutoSell");
        } else {
            enableDisableLore.setLoreAction(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_enable));
            enableDisableButton = new Button(15, XMaterial.RED_STAINED_GLASS_PANE, enableDisableLore, "&cAutoSell-Disabled");
        }

        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, "&cClose" ));
        gui.addButton(perUserToggleableButton);
        gui.addButton(enableDisableButton);

        gui.open();
    }
}

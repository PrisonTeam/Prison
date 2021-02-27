package tech.mcprison.prison.spigot.gui.autofeatures;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotAutoSmeltGUI extends SpigotGUIComponents {

    private final Player p;
    private final AutoFeaturesFileConfig afConfig = afConfig();

    public SpigotAutoSmeltGUI(Player p){
        this.p = p;
    }

    public void open() {

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        int dimension = 36;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3AutoFeatures -> AutoSmelt"));

        if (guiBuilder(inv)) return;

        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {

        List<String> enabledLore = createLore(
                messages.getString("Lore.ShiftAndRightClickToDisable")
        );
        List<String> disabledLore = createLore(
                messages.getString("Lore.RightClickToEnable")
        );
        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));
        inv.setItem(35, closeGUI);

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltAllBlocks ) ) {
            ItemStack Enabled = createButton(XMaterial.LIME_STAINED_GLASS_PANE.parseItem(), enabledLore, SpigotPrison.format("&a" + "All_Ores Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), disabledLore, SpigotPrison.format("&c" + "All_Ores Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltGoldOre ) ) {
            ItemStack Enabled = createButton(XMaterial.GOLD_ORE.parseItem(), enabledLore, SpigotPrison.format("&a" + "Gold_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(XMaterial.GOLD_ORE.parseItem(), disabledLore, SpigotPrison.format("&c" + "Gold_Ore Disabled"));
            inv.addItem(Disabled);
        }

        if ( afConfig.isFeatureBoolean( AutoFeatures.autoSmeltIronOre ) ) {
            ItemStack Enabled = createButton(XMaterial.IRON_ORE.parseItem(), enabledLore, SpigotPrison.format("&a" + "Iron_Ore Enabled"));
            inv.addItem(Enabled);
        } else {
            ItemStack Disabled = createButton(XMaterial.IRON_ORE.parseItem(), disabledLore, SpigotPrison.format("&c" + "Iron_Ore Disabled"));
            inv.addItem(Disabled);
        }
    }
}

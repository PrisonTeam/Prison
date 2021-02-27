package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotMineNotificationsGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;

    public SpigotMineNotificationsGUI(Player p, String mineName){
        this.p = p;
        this.mineName = mineName;
    }

    public void open() {

        // Create a new inventory
        int dimension = 27;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MineInfo -> MineNotifications"));

        // Init variables
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);
        String enabledOrDisabled = m.getNotificationMode().name();

        if (guiBuilder(inv, enabledOrDisabled)) return;

        // Opens the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv, String enabledOrDisabled) {
        try {
            buttonsSetup(inv, enabledOrDisabled);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p),SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, String enabledOrDisabled) {


        // Create a new lore
        List<String> modeWithinLore = createLore(
                messages.getString("Lore.ClickToChoose"),
                messages.getString("Lore.ActivateWithinMode"));
        List<String> modeRadiusLore = createLore(
                messages.getString("Lore.ClickToChoose"),
                messages.getString("Lore.ActivateRadiusMode"));
        List<String> disabledModeLore = createLore(
                messages.getString("Lore.ClickToChoose"),
                messages.getString("Lore.DisableNotifications"));
        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));
        inv.setItem(26, closeGUI);

        // Add the selected lore to the mode used
        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            // Add the selected lore
            disabledModeLore.add(SpigotPrison.format(messages.getString("Lore.Selected")));

        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            // Add the selected lore
            modeWithinLore.add(SpigotPrison.format(messages.getString("Lore.Selected")));

        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            // Add the selected lore
            modeRadiusLore.add(SpigotPrison.format(messages.getString("Lore.Selected")));

        }

        // Create a button
        ItemStack modeWithin = createButton(XMaterial.IRON_DOOR.parseItem(), modeWithinLore, SpigotPrison.format("&3Within_Mode: " + mineName));

        // Create a button
        Material fence = Material.matchMaterial( "fence" );
        if ( fence == null ) {
        	fence = Material.matchMaterial( "oak_fence" );
        }
        ItemStack radiusMode = createButton(fence, 1, modeRadiusLore, SpigotPrison.format("&3Radius_Mode: " + mineName));

        // Create a button
        ItemStack disabledMode = createButton(XMaterial.REDSTONE_BLOCK.parseItem(), disabledModeLore, SpigotPrison.format("&3Disabled_Mode: " + mineName));

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        if (enabledOrDisabled.equalsIgnoreCase("disabled")){

            // Add a button to the inventory
            inv.setItem( 11, modeWithin);
            inv.setItem(13, radiusMode);
            // Add an enchantment effect to the button
            disabledMode.addUnsafeEnchantment(Enchantment.LUCK, 1);
            inv.setItem(15, disabledMode);

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("within")){

            // Add a button to the inventory
            modeWithin.addUnsafeEnchantment(Enchantment.LUCK, 1);
            inv.setItem(11, modeWithin);
            inv.setItem(13, radiusMode);
            inv.setItem(15, disabledMode);

        // Check which buttons should be added, based on the mode already in use of the Mine Notifications
        } else if (enabledOrDisabled.equalsIgnoreCase("radius")){

            // Add a button to the inventory
            inv.setItem( 11, modeWithin);
            radiusMode.addUnsafeEnchantment(Enchantment.LUCK, 1);
            inv.setItem( 13, radiusMode);
            inv.setItem(15, disabledMode);

        }
    }
}

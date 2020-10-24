package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMineInfoGUI extends SpigotGUIComponents {

    private final Player p;
	private final Mine mine;
    private final String mineName;

    public SpigotMineInfoGUI(Player p, Mine mine, String mineName){
        this.p = p;
        this.mine = mine;
        this.mineName = mineName;
    }

    public void open(){

        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> MineInfo"));

        if (guiBuilder(inv)) return;

        // Opens the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {

        Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

        // The Reset Mine button and lore
        List<String> resetminelore = createLore(
                messages.getString("Gui.Lore.LeftClickToReset"),
                "",
                messages.getString("Gui.Lore.RightClickToToggle"),
                messages.getString("Gui.Lore.SkipReset1"),
                messages.getString("Gui.Lore.SkipReset2"),
                messages.getString("Gui.Lore.SkipReset3"),
                "",
                messages.getString("Gui.Lore.ShiftAndRightClickToToggle"),
                messages.getString("Gui.Message.ZeroBlocksReset1"),
                messages.getString("Gui.Message.ZeroBlocksReset2"),
                messages.getString("Gui.Message.ZeroBlocksReset3")
        );

        // Set the Mine spawn at your location
        List<String> MineSpawnlore = createLore(
                messages.getString("Gui.Lore.ClickToUse"),
                messages.getString("Gui.Lore.SpawnPoint2")
        );

        // Lore and button
        List<String> MinesNotificationsLore = createLore(
                messages.getString("Gui.Lore.ClickToOpen"),
                messages.getString("Gui.Lore.Notifications")
        );

        // Lore and button
        List<String> MinesTpLore = createLore(
                messages.getString("Gui.Lore.ClickToTeleport"),
                messages.getString("Gui.Lore.Tp")
        );

        // Blocks of the mine button and lore
        List<String> blocksOfTheMineLore = createLore(
                messages.getString("Gui.Lore.ClickToOpen"),
                messages.getString("Gui.Lore.Blocks2"));

        // Blocks of the mine button and lore
        List<String> mineResetTimeLore = createLore(
                messages.getString("Gui.Lore.ClickToOpen"),
                messages.getString("Gui.Lore.ManageResetTime"),
                messages.getString("Gui.Lore.ResetTime") + mine.getResetTime());

        List<String> mineRenameLore = createLore(
                messages.getString("Gui.Lore.ClickToRename"),
                messages.getString("Gui.Lore.MineName") + mineName
        );

        List<String> closeGUILore = createLore(
                messages.getString("Gui.Lore.ClickToClose")
        );

        // Create the button, set the material, amount, lore and name
        ItemStack closeGUI = createButton(Material.RED_STAINED_GLASS, 1, closeGUILore, SpigotPrison.format("&c" + "Close"));

        // Create the button, set up the material, amount, lore and name
        ItemStack resetMine = createButton(Material.EMERALD_BLOCK, 1, resetminelore, SpigotPrison.format("&3" + "Reset_Mine: " + mineName));

        // Create the button
        ItemStack MineSpawn = createButton(Material.COMPASS, 1, MineSpawnlore, SpigotPrison.format("&3" + "Mine_Spawn: " + mineName));

        // Create the button
        ItemStack MinesNotifications = createButton(Material.SIGN, 1, MinesNotificationsLore, SpigotPrison.format("&3" + "Mine_notifications: " + mineName));

        // Create the button
        Material bed = Material.matchMaterial( "bed" );
        if ( bed == null ) {
        	bed = Material.matchMaterial( "Red_Bed" );
        }
        ItemStack MinesTP = createButton(bed, 1, MinesTpLore, SpigotPrison.format("&3" + "TP_to_the_Mine: " + mineName));

        // Create the button, set up the material, amount, lore and name
        ItemStack blocksOfTheMine = createButton(Material.COAL_ORE, 1, blocksOfTheMineLore, SpigotPrison.format("&3" + "Blocks_of_the_Mine: " + mineName));

        // Create the button, set up the material, amount, lore and name
        Material watch = Material.matchMaterial( "watch" );
        if ( watch == null ) {
        	watch = Material.matchMaterial( "legacy_watch" );
        } if ( watch == null ) {
        	watch = Material.matchMaterial( "clock" );
        }
        ItemStack mineResetTime = createButton(watch, 1, mineResetTimeLore, SpigotPrison.format("&3" + "Reset_Time: " + mineName));

        ItemStack mineRename = createButton(Material.FEATHER, 1, mineRenameLore, SpigotPrison.format("&3" + "Mine_Name: " + mineName));

        // Position of the button
        inv.setItem(10, resetMine);

        // Position of the button
        inv.setItem(12, MineSpawn);

        // Position of the button
        inv.setItem(14, MinesNotifications);

        // Position of the button
        inv.setItem(16, MinesTP);

        // Position of the button
        inv.setItem(29, blocksOfTheMine);

        // Position of the button
        inv.setItem(31, mineResetTime);

        // Position of the button
        inv.setItem(33, mineRename);

        // Position of the button
        inv.setItem(45, closeGUI);
    }

}

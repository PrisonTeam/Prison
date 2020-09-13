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
    private final String minename;

    public SpigotMineInfoGUI(Player p, Mine mine, String minename){
        this.p = p;
        this.mine = mine;
        this.minename = minename;
    }

    public void open(){

        int dimension = 45;
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> MineInfo"));

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (guiBuilder(inv, GuiConfig)) return;

        // Opens the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Inventory inv, Configuration guiConfig) {
        try {
            buttonsSetup(inv, guiConfig);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, Configuration guiConfig) {
        // The Reset Mine button and lore
        List<String> resetminelore = createLore(
                guiConfig.getString("Gui.Lore.LeftClickToReset"),
                "",
                guiConfig.getString("Gui.Lore.RightClickToToggle"),
                guiConfig.getString("Gui.Lore.SkipReset1"),
                guiConfig.getString("Gui.Lore.SkipReset2"),
                guiConfig.getString("Gui.Lore.SkipReset3"),
                "",
                guiConfig.getString("Gui.Lore.ShiftAndRightClickToToggle"),
                guiConfig.getString("Gui.Message.ZeroBlocksReset1"),
                guiConfig.getString("Gui.Message.ZeroBlocksReset2"),
                guiConfig.getString("Gui.Message.ZeroBlocksReset3")
        );

        // Set the Mine spawn at your location
        List<String> MineSpawnlore = createLore(
                guiConfig.getString("Gui.Lore.ClickToUse"),
                guiConfig.getString("Gui.Lore.SpawnPoint2")
        );

        // Lore and button
        List<String> MinesNotificationsLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToOpen"),
                guiConfig.getString("Gui.Lore.Notifications")
        );

        // Lore and button
        List<String> MinesTpLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToTeleport"),
                guiConfig.getString("Gui.Lore.Tp")
        );

        // Blocks of the mine button and lore
        List<String> blocksoftheminelore = createLore(
                guiConfig.getString("Gui.Lore.ClickToOpen"),
                guiConfig.getString("Gui.Lore.Blocks2"));

        // Blocks of the mine button and lore
        List<String> mineResetTimeLore = createLore(
                guiConfig.getString("Gui.Lore.ClickToOpen"),
                guiConfig.getString("Gui.Lore.ManageResetTime"),
                guiConfig.getString("Gui.Lore.ResetTime") + mine.getResetTime());

        // Create the button, set up the material, amount, lore and name
        ItemStack resetmine = createButton(Material.EMERALD_BLOCK, 1, resetminelore, SpigotPrison.format("&3" + "Reset_Mine: " + minename));

        // Create the button
        ItemStack MineSpawn = createButton(Material.COMPASS, 1, MineSpawnlore, SpigotPrison.format("&3" + "Mine_Spawn: " + minename));

        // Create the button
        ItemStack MinesNotifications = createButton(Material.SIGN, 1, MinesNotificationsLore, SpigotPrison.format("&3" + "Mine_notifications: " + minename));

        // Create the button
        Material bed = Material.matchMaterial( "bed" );
        if ( bed == null ) {
        	bed = Material.matchMaterial( "Red_Bed" );
        }
        ItemStack MinesTP = createButton(bed, 1, MinesTpLore, SpigotPrison.format("&3" + "TP_to_the_Mine: " + minename));

        // Create the button, set up the material, amount, lore and name
        ItemStack blocksofthemine = createButton(Material.COAL_ORE, 1, blocksoftheminelore, SpigotPrison.format("&3" + "Blocks_of_the_Mine: " + minename));

        // Create the button, set up the material, amount, lore and name
        Material watch = Material.matchMaterial( "watch" );
        if ( watch == null ) {
        	watch = Material.matchMaterial( "legacy_watch" );
        } if ( watch == null ) {
        	watch = Material.matchMaterial( "clock" );
        }
        ItemStack mineResetTime = createButton(watch, 1, mineResetTimeLore, SpigotPrison.format("&3" + "Reset_Time: " + minename));

        // Position of the button
        inv.setItem(10, resetmine);

        // Position of the button
        inv.setItem(13, MineSpawn);

        // Position of the button
        inv.setItem(16, MinesNotifications);

        // Position of the button
        inv.setItem(28, MinesTP);

        // Position of the button
        inv.setItem(31, blocksofthemine);

        // Position of the button
        inv.setItem(34, mineResetTime);
    }

}

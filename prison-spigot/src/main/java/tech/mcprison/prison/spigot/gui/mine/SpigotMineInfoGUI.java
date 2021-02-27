package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
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


        // The Reset Mine button and lore
        List<String> resetMineLore = createLore(
                messages.getString("Lore.LeftClickToReset"),
                "",
                messages.getString("Lore.RightClickToToggle"),
                messages.getString("Lore.SkipReset1"),
                messages.getString("Lore.SkipReset2"),
                messages.getString("Lore.SkipReset3"),
                "",
                messages.getString("Lore.ShiftAndRightClickToToggle"),
                messages.getString("Lore.ZeroBlocksReset1"),
                messages.getString("Lore.ZeroBlocksReset2"),
                messages.getString("Lore.ZeroBlocksReset3")
        );
        List<String> MineSpawnLore = createLore(
                messages.getString("Lore.ClickToUse"),
                messages.getString("Lore.SpawnPoint2")
        );
        List<String> MinesNotificationsLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                messages.getString("Lore.Notifications")
        );
        List<String> MinesTpLore = createLore(
                messages.getString("Lore.ClickToTeleport"),
                messages.getString("Lore.Tp")
        );
        List<String> blocksOfTheMineLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                messages.getString("Lore.Blocks2"));
        List<String> mineResetTimeLore = createLore(
                messages.getString("Lore.ClickToOpen"),
                messages.getString("Lore.ManageResetTime"),
                messages.getString("Lore.ResetTime") + mine.getResetTime());
        List<String> mineRenameLore = createLore(
                messages.getString("Lore.ClickToRename"),
                messages.getString("Lore.MineName") + mineName
        );

        List<String> closeGUILore = createLore(
                messages.getString("Lore.ClickToClose")
        );

        // Create the button, set the material, amount, lore and name
        ItemStack closeGUI = createButton(XMaterial.RED_STAINED_GLASS_PANE.parseItem(), closeGUILore, SpigotPrison.format("&c" + "Close"));
        ItemStack resetMine = createButton(XMaterial.EMERALD_BLOCK.parseItem(), resetMineLore, SpigotPrison.format("&3" + "Reset_Mine: " + mineName));
        ItemStack MineSpawn = createButton(XMaterial.COMPASS.parseItem(), MineSpawnLore, SpigotPrison.format("&3" + "Mine_Spawn: " + mineName));
        ItemStack MinesNotifications = createButton(new ItemStack(Material.SIGN, 1), MinesNotificationsLore, SpigotPrison.format("&3" + "Mine_notifications: " + mineName));

        // Create the button
        Material bed = Material.matchMaterial( "bed" );
        if ( bed == null ) {
        	bed = Material.matchMaterial( "Red_Bed" );
        }
        ItemStack MinesTP = createButton(bed, 1, MinesTpLore, SpigotPrison.format("&3" + "TP_to_the_Mine: " + mineName));

        // Create the button, set up the material, amount, lore and name
        ItemStack blocksOfTheMine = createButton(XMaterial.COAL_ORE.parseItem(), blocksOfTheMineLore, SpigotPrison.format("&3" + "Blocks_of_the_Mine: " + mineName));

        // Create the button, set up the material, amount, lore and name
        Material watch = Material.matchMaterial( "watch" );
        if ( watch == null ) {
        	watch = Material.matchMaterial( "legacy_watch" );
        } if ( watch == null ) {
        	watch = Material.matchMaterial( "clock" );
        }
        ItemStack mineResetTime = createButton(watch, 1, mineResetTimeLore, SpigotPrison.format("&3" + "Reset_Time: " + mineName));

        ItemStack mineRename = createButton(XMaterial.FEATHER.parseItem(), mineRenameLore, SpigotPrison.format("&3" + "Mine_Name: " + mineName));

        // Mine show Item of Player's GUI aka /gui mines.
        XMaterial xMaterial = XMaterial.COAL_ORE;
        String customItem = guiConfig.getString("Options.Mines.MaterialType." + mineName);
        if (customItem != null){
            XMaterial mineXMaterial = SpigotUtil.getXMaterial(customItem);
            if (mineXMaterial != null){
                xMaterial = mineXMaterial;
            }
        }

        // Lore
        List<String> mineShowItemLore = createLore(
                messages.getString("Lore.ClickToEdit"),
                messages.getString("Lore.ShowItem") + xMaterial.name(),
                "",
                messages.getString("Lore.ShowItemDescription"),
                messages.getString("Lore.ShowItemDescription2"),
                messages.getString("Lore.ShowItemDescription3")
        );

        // ItemStack
        ItemStack mineShowItem = createButton(xMaterial.parseItem(), mineShowItemLore, SpigotPrison.format("&3Mine_Show_Item: ") + mineName);

        // Position of the button
        inv.setItem(10, resetMine);
        inv.setItem(12, MineSpawn);
        inv.setItem(14, MinesNotifications);
        inv.setItem(16, MinesTP);
        inv.setItem(28, blocksOfTheMine);
        inv.setItem(30, mineResetTime);
        inv.setItem(32, mineRename);
        inv.setItem(34, mineShowItem);
        inv.setItem(44, closeGUI);
    }
}

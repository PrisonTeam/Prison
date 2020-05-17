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

        // The Reset Mine button and lore
        List<String> resetminelore = createLore(
                GuiConfig.getString("Gui.Lore.LeftClickToReset"),
                "",
                GuiConfig.getString("Gui.Lore.RightClickToToggle"),
                GuiConfig.getString("Gui.Lore.SkipReset1"),
                GuiConfig.getString("Gui.Lore.SkipReset2"),
                GuiConfig.getString("Gui.Lore.SkipReset3"),
                "",
                GuiConfig.getString("Gui.Lore.ShiftAndRightClickToToggle"),
                GuiConfig.getString("Gui.Message.ZeroBlocksReset1"),
                GuiConfig.getString("Gui.Message.ZeroBlocksReset2"),
                GuiConfig.getString("Gui.Message.ZeroBlocksReset3")
        );

        // Set the Mine spawn at your location
        List<String> MineSpawnlore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToUse"),
                GuiConfig.getString("Gui.Lore.SpawnPoint2")
        );

        // Lore and button
        List<String> MinesNotificationsLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToOpen"),
                GuiConfig.getString("Gui.Lore.Notifications")
        );

        // Lore and button
        List<String> MinesTpLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToTeleport"),
                GuiConfig.getString("Gui.Lore.Tp")
        );

        // Blocks of the mine button and lore
        List<String> blocksoftheminelore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToOpen"),
                GuiConfig.getString("Gui.Lore.Blocks2"));

        // Blocks of the mine button and lore
        List<String> mineResetTimeLore = createLore(
                GuiConfig.getString("Gui.Lore.ClickToOpen"),
                GuiConfig.getString("Gui.Lore.ManageResetTime"),
                GuiConfig.getString("Gui.Lore.ResetTime") + mine.getResetTime());

        // Create the button, set up the material, amount, lore and name
        ItemStack resetmine = createButton(Material.EMERALD_BLOCK, 1, resetminelore, SpigotPrison.format("&3" + "Reset_Mine: " + minename));

        // Create the button
        ItemStack MineSpawn = createButton(Material.COMPASS, 1, MineSpawnlore, SpigotPrison.format("&3" + "Mine_Spawn: " + minename));

        // Create the button
        ItemStack MinesNotifications = createButton(Material.SIGN, 1, MinesNotificationsLore, SpigotPrison.format("&3" + "Mine_notifications: " + minename));

        // Create the button
        ItemStack MinesTP = createButton(Material.BED, 1, MinesTpLore, SpigotPrison.format("&3" + "TP_to_the_Mine: " + minename));

        // Create the button, set up the material, amount, lore and name
        ItemStack blocksofthemine = createButton(Material.COAL_ORE, 1, blocksoftheminelore, SpigotPrison.format("&3" + "Blocks_of_the_Mine: " + minename));

        // Create the button, set up the material, amount, lore and name
        ItemStack mineResetTime = createButton(Material.WATCH, 1, mineResetTimeLore, SpigotPrison.format("&3" + "Reset_Time: " + minename));

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

        // Opens the inventory
        this.p.openInventory(inv);

    }

}

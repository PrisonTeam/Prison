package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMineInfoGUI extends SpigotGUIComponents {

    private final Player p;
	private final Mine mine;
    private final String mineName;
    int dimension = 45;

    public SpigotMineInfoGUI(Player p, Mine mine, String mineName){
        this.p = p;
        this.mine = mine;
        this.mineName = mineName;
    }

    public void open(){

        PrisonGUI gui = new PrisonGUI(p, dimension, SpigotPrison.format("&3Mines -> MineInfo"));

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
        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&c" + "Close")));
        gui.addButton(new Button(10, XMaterial.EMERALD_BLOCK, resetMineLore, SpigotPrison.format("&3" + "Reset_Mine: " + mineName)));
        gui.addButton(new Button(12, XMaterial.COMPASS, MineSpawnLore, SpigotPrison.format("&3" + "Mine_Spawn: " + mineName)));
        gui.addButton(new Button(14, XMaterial.OAK_SIGN, MinesNotificationsLore, SpigotPrison.format("&3" + "Mine_notifications: " + mineName)));
        gui.addButton(new Button(16, XMaterial.ARROW, MinesTpLore, SpigotPrison.format("&3" + "TP_to_the_Mine: " + mineName)));
        gui.addButton(new Button(28, XMaterial.COAL_ORE, blocksOfTheMineLore, SpigotPrison.format("&3" + "Blocks_of_the_Mine: " + mineName)));
        gui.addButton(new Button(30, XMaterial.CLOCK, 1, mineResetTimeLore, SpigotPrison.format("&3" + "Reset_Time: " + mineName)));
        gui.addButton(new Button(32 ,XMaterial.FEATHER, mineRenameLore, SpigotPrison.format("&3" + "Mine_Name: " + mineName)));

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
        gui.addButton(new Button(34, xMaterial, mineShowItemLore, SpigotPrison.format("&3Mine_Show_Item: ") + mineName));

        // Opens the inventory
        gui.open();
    }
}

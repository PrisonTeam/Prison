package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.GuiConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotPlayerMinesGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotPlayerMinesGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Get the mines - In sort order, minus any marked as suppressed
    	PrisonSortableResults mines = PrisonMines.getInstance().getMines( MineSortOrder.sortOrder );

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(mines.getSortedList().size() / 9D) * 9;

        // Load config

        // If the inventory is empty
        if (dimension == 0){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.NoMines")));
            p.closeInventory();
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format(messages.getString("Message.TooManyMines")));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format(guiConfig.getString("Options.Titles.PlayerMinesGUI")));

        // Make the buttons for every Mine with info
        for (Mine m : mines.getSortedList()) {

            // Init the lore array with default values for ladders
            List<String> minesLore = createLore(
                   );

            if (guiBuilder(inv, m, minesLore)) return;
        }

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv, Mine m, List<String> minesLore) {
        try {
            buttonsSetup(inv, m, minesLore);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p),"&cThere's a null value in the GuiConfig.yml [broken]");
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv, Mine m, List<String> minesLore) {

        ItemStack itemMines;
        Material material;

        GuiConfig guiConfigClass = new GuiConfig();
        guiConfig = guiConfigClass.getFileGuiConfig();
        String permission = SpigotPrison.format(guiConfig.getString("Options.Mines.PermissionWarpPlugin"));

        // Get Mine Name.
        String mineName = m.getName();

        // Add mineName lore for TP.
        minesLore.add(SpigotPrison.format("&3" + mineName));

        // The valid names to use for Options.Mines.MaterialType.<MaterialName> must be
        // based upon the XMaterial enumeration name, or supported past names.
        Material mineMaterial = null;
        String materialTypeStr = guiConfig.getString("Options.Mines.MaterialType." + m.getName());
        if ( materialTypeStr != null && materialTypeStr.trim().length() > 0 ) {
        	XMaterial mineXMaterial = SpigotUtil.getXMaterial( materialTypeStr );
        	if ( mineXMaterial != null ) {
        		mineMaterial = mineXMaterial.parseMaterial();
        	}
        	else {
        		Output.get().logInfo( "Warning: A block was specified for mine %s but it was " +
        				"unable to be mapped to an XMaterial type. Key = " +
        				"[Options.Mines.MaterialType.%s] value = " +
        				"[%s] Please use valid material names as found in the XMaterial " +
        				"source on git hub: " +
        				"https://github.com/CryptoMorin/XSeries/blob/master/src/main/java/" +
        				"com/cryptomorin/xseries/XMaterial.java ", 
        				m.getName(), m.getName(), mineXMaterial );
        	}
        }

        if (p.hasPermission(permission + m.getName()) || p.hasPermission(permission.substring(0, permission.length() - 1))){
            material = ( mineMaterial == null ? Material.COAL_ORE : mineMaterial);
            minesLore.add(SpigotPrison.format(messages.getString("Lore.StatusUnlockedMine")));
            minesLore.add(SpigotPrison.format(messages.getString("Lore.ClickToTeleport")));
        } else {
            material = XMaterial.REDSTONE_BLOCK.parseMaterial();
            minesLore.add(SpigotPrison.format(messages.getString("Lore.StatusLockedMine")));
        }

        // Get mine Tag.
        String mineTag = m.getTag();

        // Check if mineName's null (which shouldn't be) and do actions.
        if (mineName != null) {

            if (mineTag == null || mineTag.equalsIgnoreCase("null")){
                mineTag = mineName;
            }

            if (material == null){
                material = Material.COAL_ORE;
            }

            // Create the button.
            itemMines = createButton(new ItemStack(material, 1), minesLore, SpigotPrison.format("&3" + mineTag));

            // Add the button to the inventory.
            inv.addItem(itemMines);
        }
    }
}

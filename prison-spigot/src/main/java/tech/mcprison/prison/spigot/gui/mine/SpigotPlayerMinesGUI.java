package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.GuiConfig;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;
import tech.mcprison.prison.spigot.gui.rank.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.rank.SpigotGUIMenuTools.GUIMenuPageData;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotPlayerMinesGUI extends SpigotGUIComponents {

    private final Player p;
    private final SpigotPlayer spigotPlayer;
    private final String permissionWarpPlugin = guiConfig.getString("Options.Mines.PermissionWarpPlugin");
    private final String statusUnlockedMine = messages.getString(MessagesConfig.StringID.spigot_gui_lore_unlocked);
    private final String clickToTeleport = messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_teleport);
    private final String statusLockedMine = messages.getString(MessagesConfig.StringID.spigot_gui_lore_locked);

    private int page;
    
    public SpigotPlayerMinesGUI(Player p, int page ) {
        this.p = p;
        
        this.spigotPlayer = new SpigotPlayer(p);
        
        this.page = page;
    }

    public void open(){

        // Get the mines - In sort order, minus any marked as suppressed
    	PrisonSortableResults mines = PrisonMines.getInstance().getMines( MineSortOrder.sortOrder );

        
        int totalArraySize = mines.getSortedList().size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, "gui mines", "gui" );


        List<Mine> minesDisplay = mines.getSortedList().subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        
        
    	
//        // Get the dimensions and if needed increases them
//        int dimension = (int) Math.ceil(mines.getSortedList().size() / 9D) * 9;
//
//        // If the inventory is empty
//        if (dimension == 0){
//            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_mines_empty));
//            p.closeInventory();
//            return;
//        }
//
//        // If the dimension's too big, don't open the GUI
//        if (dimension > 54){
//            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_mines_too_many));
//            p.closeInventory();
//            return;
//        }

        // Create GUI.
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), guiConfig.getString("Options.Titles.PlayerMinesGUI"));

        // Make the buttons for every Mine with info
        for (Mine m : minesDisplay) {
//        	for (Mine m : mines.getSortedList()) {

            // Init the lore array with default values for ladders
            ButtonLore minesLore = new ButtonLore();

            Material material;

            GuiConfig guiConfigClass = new GuiConfig();
            guiConfig = guiConfigClass.getFileGuiConfig();
            String permission = SpigotPrison.format(permissionWarpPlugin);

            // Get Mine Name.
            String mineName = m.getName();

            // Add mineName lore for TP.
            minesLore.addLineLoreAction(SpigotPrison.format("&3" + mineName));

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

            if (m.hasMiningAccess(spigotPlayer) || p.hasPermission(permission + m.getName()) ||
                    p.hasPermission(permission.substring(0, permission.length() - 1))){
                material = ( mineMaterial == null ? Material.COAL_ORE : mineMaterial);
                minesLore.addLineLoreDescription(SpigotPrison.format(statusUnlockedMine));
                minesLore.addLineLoreAction(SpigotPrison.format(clickToTeleport));
            } else {
                material = XMaterial.REDSTONE_BLOCK.parseMaterial();
                minesLore.addLineLoreDescription(SpigotPrison.format(statusLockedMine));
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

                // Add the button to the inventory.
                gui.addButton(new Button(null, XMaterial.matchXMaterial(material), minesLore, "&3" + mineTag));
            }

        }

        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );


        // Open the GUI.
        gui.open();
    }
}

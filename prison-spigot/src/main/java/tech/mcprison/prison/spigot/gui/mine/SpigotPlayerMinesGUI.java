package tech.mcprison.prison.spigot.gui.mine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import me.clip.placeholderapi.PlaceholderAPI;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.GuiConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotGUIMessages;
import tech.mcprison.prison.util.Text;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotPlayerMinesGUI 
	extends SpigotGUIMessages {

    private final Player p;
    private final SpigotPlayer spigotPlayer;
    private final String permissionWarpPlugin = guiConfig.getString("Options.Mines.PermissionWarpPlugin");
    private final String statusUnlockedMine = guiRanksLoreUnlockedMsg();
    private final String clickToTeleport = guiRanksLoreClickToTeleportMsg();
    private final String statusLockedMine = guiRanksLoreLockedMsg();

    private int page;
    private String cmdPage;
    private String cmdReturn;
    
    private final boolean placeholderAPINotNull = 
    		Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null || 
    		Bukkit.getPluginManager().getPlugin("PlaceholdersAPI") != null;

    
    public SpigotPlayerMinesGUI(Player p, int page, String cmdPage, String cmdReturn ) {
        this.p = p;
        
        this.spigotPlayer = new SpigotPlayer(p);
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;
    }

    public void open() {

        // Get the mines - In sort order, minus any marked as suppressed
    	PrisonSortableResults mines = PrisonMines.getInstance().getMines( MineSortOrder.sortOrder );

        
        int totalArraySize = mines.getSortedList().size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


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

        GuiConfig guiConfigClass = new GuiConfig();
        guiConfig = guiConfigClass.getFileGuiConfig();
        String permission = Text.translateAmpColorCodes(permissionWarpPlugin);

        // Create GUI.
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), guiConfig.getString("Options.Titles.PlayerMinesGUI"));

        
        List<String> configCustomLore = guiConfig.getStringList("EditableLore.Mines");
        
        
        // Make the buttons for every Mine with info
        for (Mine m : minesDisplay) {
//        	for (Mine m : mines.getSortedList()) {

            // Init the lore array with default values for ladders
            ButtonLore minesLore = new ButtonLore();
            

        	String mineLoreKey = "EditableLore.Mine." + m.getName();
        	List<String> mineLore = new ArrayList<>( configCustomLore );
        	List<String> mineLore2 = guiConfig.getStringList( mineLoreKey );
        	mineLore.addAll( mineLore2 );


            XMaterial xMat = XMaterial.REDSTONE_BLOCK;
            
            // Bug: Cannot safely use Material due to variants prior to bukkit v1.13:
//            Material material;


            // Get Mine Name.
            String mineName = m.getName();

            // Add mineName lore for TP.
            minesLore.addLineLoreAction( "&3" + mineName );

            
            boolean hasMineAccess = m.hasMiningAccess(spigotPlayer);
            String permMineAccess = permission + m.getName();
            boolean hasPermMine = p.hasPermission( permMineAccess );
            String permAccess = permission.substring(0, permission.length() - 1);
            boolean hasPerm = p.hasPermission( permAccess );

            // If the player has permission to access the mine, then see if there is a custom
            // block set for the mine... otherwise it will use XMaterial.COAL_ORE:
            if ( hasMineAccess || 
            		hasPermMine ||
            		hasPerm ) 
            {
            	
            	if ( !hasMineAccess ) {
            		Output.get().logInfo( 
            				"GUI Player Mines: Has access to mine %s through perms: %s=%s  OR  %s=%s",
            				m.getName(), 
            				permMineAccess, Boolean.toString(hasPermMine),
            				permAccess, Boolean.toString(hasPerm)
            				);
            	}
            	
            	
            	// Default to COAL_ORE since the player has access to the mine:
            	xMat = XMaterial.COAL_ORE;
            	
            	// The valid names to use for Options.Mines.MaterialType.<MaterialName> must be
            	// based upon the XMaterial enumeration name, or supported past names.
//            Material mineMaterial = null;
            	String materialTypeStr = guiConfig.getString("Options.Mines.MaterialType." + m.getName());
            	
            	if ( materialTypeStr != null && materialTypeStr.trim().length() > 0 ) {
            		
            		XMaterial xMatTemp = SpigotUtil.getXMaterial( materialTypeStr );
            		if ( xMatTemp == null ) {
            			Output.get().logInfo( "Warning: A block was specified for mine '%s' but it was " +
            					"unable to be mapped to a valid XMaterial type. Key = " +
            					"[Options.Mines.MaterialType.%s] value = " +
            					"[%s] Please use valid material names as found in the XMaterial " +
            					"source on git hub: " +
            					"https://github.com/CryptoMorin/XSeries/blob/master/src/main/java/" +
            					"com/cryptomorin/xseries/XMaterial.java ",
            					m.getName(), m.getName(), materialTypeStr );
            		}
            		else {
            			xMat = xMatTemp;
            		}
            	}

            	// material = ( mineMaterial == null ? Material.COAL_ORE : mineMaterial);
                minesLore.addLineLoreDescription( statusUnlockedMine );
                minesLore.addLineLoreAction( clickToTeleport );
            } 
            else {
            	xMat = XMaterial.REDSTONE_BLOCK;
            	
//                material = XMaterial.REDSTONE_BLOCK.parseMaterial();
                minesLore.addLineLoreDescription( statusLockedMine );
            }

            // Get mine Tag, but make sure it is valid and the mine's name is not null:
            String mineTag = mineName == null ? "-no mine name-" : mineName;
            if ( m.getTag() != null && 
            		!m.getTag().equalsIgnoreCase("null") && 
            		!m.getTag().equalsIgnoreCase("none") ) {
            	mineTag = m.getTag();
            }
            
            DecimalFormat iFmt = new DecimalFormat( "#,##0" );
            
            for (String stringValue : mineLore) {
            	
            	double volume = ( m.isVirtual() ? 0 : m.getBounds().getTotalBlockCount() );
            	double remaining = volume * m.getPercentRemainingBlockCount() / 100.0;
            	
            	String dimensions = ( m.isVirtual() ? "virtual" : m.getBounds().getDimensions() );
            	
            	stringValue = stringValue.replace( "{mineName}", mineName );
            	stringValue = stringValue.replace( "{mineTag}", mineTag );
            	stringValue = stringValue.replace( "{mineSize}", dimensions );
            	stringValue = stringValue.replace( "{mineVolume}", iFmt.format( volume ));
            	stringValue = stringValue.replace( "{mineRemaining}", iFmt.format( remaining ));
            	stringValue = stringValue.replace( "{mineRemainingPercent}", iFmt.format( m.getPercentRemainingBlockCount() ));
            	
            	
				minesLore.addLineLoreAction( stringValue );
			}
            
            if ( placeholderAPINotNull ) {
            	
            	List<String> lores = PlaceholderAPI.setPlaceholders(
            			Bukkit.getOfflinePlayer( p.getUniqueId()), 
        				minesLore.getLoreAction());
            	
                minesLore.setLoreAction( lores );
            }
            

            // Add the button to the inventory.
            gui.addButton(new Button(null, xMat, minesLore, "&3" + mineTag));
            
            
//            String mineTag = m.getTag();
//
//            // Check if mineName's null (which shouldn't be) and do actions.
//            if (mineName != null) {
//
//                if (mineTag == null || mineTag.equalsIgnoreCase("null")){
//                    mineTag = mineName;
//                }
//
//                // Add the button to the inventory.
//                gui.addButton(new Button(null, xMat, minesLore, "&3" + mineTag));
////                gui.addButton(new Button(null, XMaterial.matchXMaterial(material), minesLore, "&3" + mineTag));
//            }

        }

        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );


        // Open the GUI.
        gui.open();
    }
}

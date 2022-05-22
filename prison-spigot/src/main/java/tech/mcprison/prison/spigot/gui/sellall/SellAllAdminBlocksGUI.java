package tech.mcprison.prison.spigot.gui.sellall;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.sellall.messages.SpigotVariousGuiMessages;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotCommandSender;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SellAllAdminBlocksGUI extends SpigotGUIComponents {

    private final Player p;
//    private final int startingItem;
    
    private int page = 0;
    private String cmdPage;
    private String cmdReturn;
    
    public SellAllAdminBlocksGUI( Player p, int page, String cmdPage, String cmdReturn ){
        this.p = p;
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;
        
//        this.startingItem = startingItem;
    }

    public void open() {

        updateSellAllConfig();


        boolean emptyInv = false;

        try {
            if (sellAllConfig.getConfigurationSection("Items") == null) {
                emptyInv = true;
            }
            if (sellAllConfig.getConfigurationSection("Items").getKeys(false).size() == 0){
                emptyInv = true;
            }
        } catch (NullPointerException e){
            emptyInv = true;
        }

        if (emptyInv){
        	new SpigotVariousGuiMessages().sellallYouHaveNothingToSellMsg( new SpigotCommandSender(p) );
        	
//        	SpigotPlayer spigotPlayer = new SpigotPlayer(p);
//            Output.get().sendWarn(spigotPlayer, messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_empty));
            return;
        }

        // Get the Items config section
        List<String> items = new ArrayList<>(
        						sellAllConfig.getConfigurationSection("Items").getKeys(false) );

        
        int totalArraySize = items.size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


        List<String> itemsDisplay = items.subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        
        
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3SellAll -> Blocks");

        // Global strings.
        String loreLine1 = guiRightClickToDeleteMsg();
        String loreLine2 = guiLeftClickToEditMsg();
//        String lorePermission = messages.getString(MessagesConfig.StringID.spigot_gui_lore_permission);
        String permissionSellAllBlock = sellAllConfig.getString("Options.Sell_Per_Block_Permission");
//        String loreValue = messages.getString(MessagesConfig.StringID.spigot_gui_lore_value);

        boolean sellAllPerBlockPermissionEnabled = getBoolean(sellAllConfig.getString("Options.Sell_Per_Block_Permission_Enabled"));

//        int itemsAdded = 0, itemsRead = 0;
        for (String key : itemsDisplay) {
        	
        	
        	String itemValue = sellAllConfig.getString("Items." + key + ".ITEM_VALUE");
        	String loreValue = guiValueMsg( itemValue );
        	
                ButtonLore itemsLore = new ButtonLore(createLore(loreLine1, loreLine2), 
                		createLore( loreValue ));
                
                String sellallPerm = permissionSellAllBlock + sellAllConfig.getString("Items." + key + ".ITEM_ID");
                String lorePermission = guiPermissionMsg( sellallPerm );
                
                if (sellAllPerBlockPermissionEnabled) {
                	itemsLore.addLineLoreDescription( lorePermission );
                }
                
                String xMatIdName = "Items." + key + ".ITEM_ID";
                String xMatId = sellAllConfig.getString( xMatIdName );
                
                XMaterial xMat = SpigotUtil.getXMaterial( xMatId );
                
                if ( xMat == null ) {
                	// Unable to add match XMaterials:
                	String message = String.format(  
                			"SellAll Admin Blocks: Unable to match '%s' to a valid XMaterial. Use the command " +
                					"'/mines block search' to find the correct name and update the config entry: [%s] " +
                					"Using `COBBLESTONE` as a default. ",
                					(xMatId == null ? "<null>" : xMatId), 
                					xMatIdName
                			);
                	Output.get().logWarn( message );
                	
                	// Default to cobble
                	xMat = XMaterial.COBBLESTONE;
                }
                
                gui.addButton(new Button( null, xMat, itemsLore, "&3" + xMatId ));
                
                
//            itemsRead++;
//
//            if (itemsRead >= startingItem) {
//
//                if (startingItem != 0){
//                    gui.addButton(new Button(45, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior " + (startingItem - 45)));
//                }
//
//                else if (itemsAdded >= 45){
//                    gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (startingItem + itemsAdded)));
//                }
//
//                else if (itemsAdded < 45) {
//                    itemsAdded++;
//                }
//            }
                
        }
        
        
        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );

        
        
        gui.open();
    }
}

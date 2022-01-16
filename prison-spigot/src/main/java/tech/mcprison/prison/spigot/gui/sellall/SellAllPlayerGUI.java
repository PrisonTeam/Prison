package tech.mcprison.prison.spigot.gui.sellall;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools;
import tech.mcprison.prison.spigot.gui.SpigotGUIMenuTools.GUIMenuPageData;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SellAllPlayerGUI extends SpigotGUIComponents {

    private final Player p;
//    private final int startingItem;
    
    private int page = 0;
    private String cmdPage;
    private String cmdReturn;

    public SellAllPlayerGUI( Player p, int page, String cmdPage, String cmdReturn ){
        this.p = p;
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;

//        this.startingItem = startingItem;
    }

    public void open() {


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
            Output.get().sendWarn(new SpigotPlayer(p), messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_empty));
            return;
        }

        // Get the Items config section
        
        
        List<String> items = new ArrayList<>( 
        						sellAllConfig.getConfigurationSection("Items").getKeys(false) );

        
        
        int totalArraySize = items.size();
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );


        List<String> itemsDisplay = items.subList( guiPageData.getPosStart(), guiPageData.getPosEnd() );
        
        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3Prison -> SellAll-Player");

        
        
        // Global strings.
        String loreValue = messages.getString(MessagesConfig.StringID.spigot_gui_lore_value);

//        int itemsAdded = 0, itemsRead = 0;
        for ( String key : itemsDisplay ) {
        	
        	  gui.addButton(new Button(null, SpigotUtil.getXMaterial(sellAllConfig.getString("Items." + key + ".ITEM_ID")), new ButtonLore(null, loreValue + " " + sellAllConfig.getString("Items." + key + ".ITEM_VALUE")), "&3" + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
        	
        	
//            itemsRead++;

//            if (itemsRead >= startingItem) {
//
//                if (startingItem != 0){
//                    gui.addButton(new Button(45, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior " + (startingItem - 45)));
//                }
//
//                if (itemsAdded >= 45){
//                    gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (startingItem + itemsAdded)));
//                }
//
//                if (itemsAdded < 45) {
//                    gui.addButton(new Button(null, SpigotUtil.getXMaterial(sellAllConfig.getString("Items." + key + ".ITEM_ID")), new ButtonLore(null, loreValue + " " + sellAllConfig.getString("Items." + key + ".ITEM_VALUE")), "&3" + sellAllConfig.getString("Items." + key + ".ITEM_ID")));
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

package tech.mcprison.prison.spigot.gui.sellall;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

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
public class SellAllPlayerGUI extends SpigotGUIComponents {

    private final Player p;
    
    private int page = 0;
    private String cmdPage;
    private String cmdReturn;

    public SellAllPlayerGUI( Player p, int page, String cmdPage, String cmdReturn ){
        this.p = p;
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;

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
        	
        		new SpigotVariousGuiMessages().sellallYouHaveNothingToSellMsg( new SpigotCommandSender(p) );
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

        
        
        for ( String key : itemsDisplay ) {
        	
        	String itemId = sellAllConfig.getString("Items." + key + ".ITEM_ID");
        	String itemValue = sellAllConfig.getString("Items." + key + ".ITEM_VALUE");

        	String loreValue = guiValueMsg( itemValue );
        	
        	gui.addButton(
        			new Button(null, SpigotUtil.getXMaterial(itemId), 
        			new ButtonLore(null, loreValue), 
        			  "&3" + itemId));
        	
        }
        
        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsStandard( gui, guiPageData );
        
        
        gui.open();
    }
}

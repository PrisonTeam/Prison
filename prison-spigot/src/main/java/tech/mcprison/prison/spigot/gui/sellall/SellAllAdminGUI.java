package tech.mcprison.prison.spigot.gui.sellall;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.sellall.messages.SpigotVariousGuiMessages;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotCommandSender;
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
public class SellAllAdminGUI extends SpigotGUIComponents {

    private final Player p;
//    int dimension = 27;
    
    private int page = 0;
    private String cmdPage;
    private String cmdReturn;
    

    public SellAllAdminGUI( Player p, int page, String cmdPage, String cmdReturn ) {
        this.p = p;
        
        this.page = page;
        this.cmdPage = cmdPage;
        this.cmdReturn = cmdReturn;

    }

    public void open() {

        if ( !SpigotPrison.getInstance().isSellAllEnabled() ){
        	
        	new SpigotVariousGuiMessages().sellallIsDisabledMsg( new SpigotCommandSender(p) );
        	
//            Output.get().sendWarn(new SpigotPlayer(p), 
//            		messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_disabled));
            return;
        }

        updateSellAllConfig();
        
        
        
        int totalArraySize = 27;
        GUIMenuPageData guiPageData = SpigotGUIMenuTools.getInstance()
        		.createGUIPageObject( totalArraySize, page, cmdPage, cmdReturn );

 

        PrisonGUI gui = new PrisonGUI(p, guiPageData.getDimension(), "&3Prison -> SellAll-Admin");

        ButtonLore blocksLore = new ButtonLore( guiClickToOpenMsg(), null);
//        ButtonLore closeGUILore = new ButtonLore(messages.getString(MessagesConfig.StringID.spigot_gui_lore_click_to_close), null);
        ButtonLore setCurrencyLore = new ButtonLore(createLore( guiClickToEditMsg() ), 
        		createLore(
                messages.getString(MessagesConfig.StringID.spigot_gui_lore_currency) + " " + sellAllConfig.getString("Options.SellAll_Currency"),
                guiClickToEditMsg() ));
        ButtonLore multipliersLore = new ButtonLore( guiClickToOpenMsg(), guiClickToEditMsg() );
        ButtonLore autoSellLore = new ButtonLore();
        ButtonLore sellAllDelayLore = new ButtonLore();

        Button autoSellButton;
        Button sellAllDelayButton;

        if (sellAllConfig.getString("Options.Full_Inv_AutoSell").equalsIgnoreCase("true")){
            autoSellLore.setLoreAction(createLore( guiClickToOpenMsg(),
                    guiRightClickToDisableMsg()
                    ));

            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&3AutoSell");
        } else {
            autoSellLore.setLoreAction(
            		guiRightClickToEnableMsg() );
            autoSellButton = new Button(13, XMaterial.CHEST, autoSellLore, "&cAutoSell-Disabled");
        }

        if (sellAllConfig.getString("Options.Sell_Delay_Enabled").equalsIgnoreCase("true")){

            sellAllDelayLore.setLoreAction(createLore(
            		guiClickToOpenMsg(),
                    guiRightClickToCancelMsg()));
            
            String loreDelay = guiDelayMsg( sellAllConfig.getString("Options.Sell_Delay_Seconds") );
            
            sellAllDelayLore.setLoreDescription(createLore(
                    loreDelay,
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_1),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_2)));

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&3Delay-Enabled");
        } else {

            sellAllDelayLore.setLoreAction( guiClickToEnableMsg() );
            sellAllDelayLore.setLoreDescription(createLore(
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_1),
                    messages.getString(MessagesConfig.StringID.spigot_gui_lore_sellall_delay_use_2)));

            sellAllDelayButton = new Button(11, XMaterial.CLOCK, sellAllDelayLore, "&cDelay-Disabled");
        }

        try {
            if (sellAllConfig.getConfigurationSection("Multiplier") == null) {
                multipliersLore.addLineLoreDescription( messages.getString(MessagesConfig.StringID.spigot_gui_lore_no_multipliers) );
            } else if (sellAllConfig.getConfigurationSection("Multiplier").getKeys(false).size() == 0) {
                multipliersLore.addLineLoreDescription( messages.getString(MessagesConfig.StringID.spigot_gui_lore_no_multipliers) );
            }
        } catch (NullPointerException ex){
            multipliersLore.addLineLoreDescription( messages.getString(MessagesConfig.StringID.spigot_gui_lore_no_multipliers) );
        }

        gui.addButton(new Button(15, XMaterial.EMERALD, setCurrencyLore, "&3SellAll-Currency" ));
        gui.addButton(new Button(8, XMaterial.PAPER, multipliersLore, "&3Prestige-Multipliers" ));
        gui.addButton(new Button(0, XMaterial.DIAMOND_ORE, blocksLore, "&3Blocks-Shop"));
//        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));
        gui.addButton(sellAllDelayButton);
        gui.addButton(autoSellButton);

        
        // Add the page controls: 
        // The controls for the standard menu are in positions: 4, 5, and 6:
        SpigotGUIMenuTools.getInstance().addMenuPageButtonsNoPaging( gui, guiPageData );

        
        gui.open();
    }
}

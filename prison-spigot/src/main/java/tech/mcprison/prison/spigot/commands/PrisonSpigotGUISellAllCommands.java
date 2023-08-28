package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.sellall.messages.SpigotVariousGuiMessages;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.sellall.SellAllAdminBlocksGUI;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;

public class PrisonSpigotGUISellAllCommands
		extends PrisonSpigotBaseCommands {


    @Command(identifier = "sellall gui", 
    		description = "SellAll GUI command", 
//    		aliases = "gui sellall",
    		permissions = "prison.admin", onlyPlayers = true)
    private void sellAllGuiCommand(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 items, then they " +
    				"will be shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page){

        if (!PrisonSpigotSellAllCommands.isEnabled()) return;

        Player p = getSpigotPlayer(sender);

        // Sender must be a Player, not something else like the Console.
        if (p == null) {
            Output.get().sendError(sender, getMessages().getString(MessagesConfig.StringID.spigot_message_console_error));
            return;
        }

        SellAllUtil sellAllUtil = SellAllUtil.get();
        if (sellAllUtil == null){
            return;
        }

        if (!sellAllUtil.openSellAllGUI( p, page, "sellall gui", "close" )){
            // If the sender's an admin (OP or have the prison.admin permission) it'll send an error message.
            if (p.hasPermission("prison.admin")) {
            	
            	new SpigotVariousGuiMessages().sellallGUIIsDisabledMsg(sender);
//                Output.get().sendError(sender, 
//                		messages.getString(MessagesConfig.StringID.spigot_message_gui_sellall_disabled));
            }
        }
    }
    
    @Command(identifier = "sellall gui blocks", 
    		description = "SellAll GUI Blocks command", 
    		aliases = "gui sellall",
    		permissions = "prison.admin", onlyPlayers = true)
    private void sellAllGuiBlocksCommand(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 items, then they " +
    				"will be shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page){
    	
    	if (!PrisonSpigotSellAllCommands.isEnabled()) return;
    	
    	Player p = getSpigotPlayer(sender);
    	
    	// Sender must be a Player, not something else like the Console.
    	if (p == null) {
    		Output.get().sendError(sender, getMessages().getString(MessagesConfig.StringID.spigot_message_console_error));
    		return;
    	}
    	
    	SellAllAdminBlocksGUI saBlockGui = new SellAllAdminBlocksGUI( p, page, "sellall gui blocks", "sellall gui" );
    	saBlockGui.open();
    	
    }
    
}

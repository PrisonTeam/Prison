package tech.mcprison.prison.sellall.commands;

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;

public class SellallCommands
	extends SellallCommandsMessages
{
	
    @Command(identifier = "sellall module", 
    		onlyPlayers = false, permissions = "prison.sellall.admin",
    		description = "Test of new sellall module.")
    public void ranksSetSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "sellall help" );
    }

}

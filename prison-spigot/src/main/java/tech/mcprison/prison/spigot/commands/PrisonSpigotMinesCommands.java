package tech.mcprison.prison.spigot.commands;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotMinesCommands
				extends PrisonSpigotBaseCommands {
	
	@Command(identifier = "mines", onlyPlayers = false,
			altPermissions = {"-none-", "mines.admin"})
	public void minesGUICommand(CommandSender sender, 
			@Arg(name = "page", description = "If there are more than 45 mines, then the " +
    				"mines are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
			) {
		if (!sender.hasPermission("mines.admin")) {

            if (isPrisonConfig("prison-gui-enabled") && isConfig("Options.Mines.GUI_Enabled")){
            	
            	Object regCommand = Prison.get().getCommandHandler()
            				.getRegisteredCommandClass( PrisonSpigotGUICommands.class );
            	if ( regCommand != null ) {
            		PrisonSpigotGUICommands psGUICmd = (PrisonSpigotGUICommands) regCommand;
            		psGUICmd.cmdPrisonManagerMines( sender, page, "mines", "close" );
            	}
//                sender.dispatchCommand("gui mines");
            }
            else {
            	
            	sender.dispatchCommand("mines help");
            }

		} else {
			sender.dispatchCommand("mines help");
		}
	}
}

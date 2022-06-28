package tech.mcprison.prison.spigot.commands;

import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotRanksCommands
				extends PrisonSpigotBaseCommands {

	@Command(identifier = "ranks", onlyPlayers = false,
			altPermissions = {"-none-", "ranks.admin"})
	public void ranksGUICommand(CommandSender sender,
				@Arg(name = "ladder", def = "default",
				description = "Ladder name, such as 'default' or 'prestige'. Use 'all' for " +
						"all ranks on the server. " +
						"If player has no permission to /ranks then /ranks list will be ran instead.")
									String ladderName, 
				@Arg(name = "page", description = "If there are more than 45 entries, then they will " +
	    				"be shown on multiple pages.  The page parameter starts with " +
	    				"page 1.", def = "1" ) int page 
				) {
		
    	// NOTE: This command will NOT be registered if ranks module fails to load, so this is just a fallback 
		// safety measure:
        Optional<Module> ranksModule = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        if ( !ranksModule.isPresent() || ranksModule.isPresent() && !ranksModule.get().isEnabled() ) {
        	
        	Output.get().sendWarn( sender, "The command '/ranks' is disabled because the Ranks module is not active." );
        	return;
        }
		
		if (!sender.hasPermission("ranks.admin")) {
			
			try {
				page = Integer.parseInt( ladderName );
				
				// Ladder name was actually the page number, so set ladderName to "default"
				ladderName = "default";
			}
			catch ( NumberFormatException e ) {
				// Ignore since ladderName was not a page, which is OK
			}

			if ((ladderName.equalsIgnoreCase("default") || ladderName.equalsIgnoreCase("ranks")) &&
					isConfig("Options.Ranks.GUI_Enabled")) {

            	Object regCommand = Prison.get().getCommandHandler()
        				.getRegisteredCommandClass( PrisonSpigotGUICommands.class );
	        	if ( regCommand != null ) {
	        		PrisonSpigotGUICommands psGUICmd = (PrisonSpigotGUICommands) regCommand;
	        		psGUICmd.cmdPrisonManagerRanks( sender, page, "ranks", "close" );
	        		return;
	        	}
				
//				sender.dispatchCommand("gui ranks");
			} else if (ladderName.equalsIgnoreCase("prestiges") && 
					isConfig( "Options.Prestiges.GUI_Enabled")) {
				
				Object regCommand = Prison.get().getCommandHandler()
						.getRegisteredCommandClass( PrisonSpigotGUICommands.class );
				if ( regCommand != null ) {
					PrisonSpigotGUICommands psGUICmd = (PrisonSpigotGUICommands) regCommand;
					psGUICmd.cmdPrisonManagerPrestiges( sender, page, "ranks", "close" );
					return;
				}

//				sender.dispatchCommand("gui prestiges");
			} 

			
			sender.dispatchCommand("ranks list " + ladderName);
		}
		else {
			sender.dispatchCommand("ranks help");
		}
	}
}

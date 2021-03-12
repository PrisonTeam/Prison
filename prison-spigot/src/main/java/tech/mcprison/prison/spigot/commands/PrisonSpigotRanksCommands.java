package tech.mcprison.prison.spigot.commands;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotRanksCommands
				extends PrisonSpigotBaseCommands {

	@Command(identifier = "ranks", onlyPlayers = false,
			altPermissions = {"-none-", "ranks.admin"})
	public void ranksGUICommand(CommandSender sender,
				@Arg(name = "ladder", def = "default",
				description = "If player has no permission to /ranks then /ranks list will be ran instead.")
									String ladderName) {
		if (!sender.hasPermission("ranks.admin")) {

			if ((ladderName.equalsIgnoreCase("default") || ladderName.equalsIgnoreCase("ranks")) &&
					isConfig("Options.Ranks.GUI_Enabled")) {

				sender.dispatchCommand("gui ranks");
			} else if (ladderName.equalsIgnoreCase("prestiges") && isConfig( "Options.Prestiges.GUI_Enabled")) {

				sender.dispatchCommand("gui prestiges");
			} else {
				sender.dispatchCommand("ranks list " + ladderName);
			}
		}
		else {
			sender.dispatchCommand("ranks help");
		}
	}
}

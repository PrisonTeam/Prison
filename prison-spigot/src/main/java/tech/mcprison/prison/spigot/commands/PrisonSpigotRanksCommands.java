package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;

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
					isPrisonConfig("ranks-gui-enabled") ) {

				prisonManagerRanks( sender );
//				sender.dispatchCommand("gui ranks");
			}
			else if (ladderName.equalsIgnoreCase("prestiges") &&
					isPrisonConfig( "ranks-gui-prestiges-enabled") ) {

				sender.dispatchCommand("gui prestiges");
			}
			else {
				sender.dispatchCommand("ranks list " + ladderName);
			}
		}
		else {
			sender.dispatchCommand("ranks help");
		}
	}


    @Command( identifier = "gui ranks", description = "GUI Ranks",
    		  aliases = {"prisonmanager ranks"},
    		  onlyPlayers = true )
    private void prisonManagerRanks(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
        	sender.sendMessage( getMessages().getString("Message.CantRunGUIFromConsole"));
        	return;
        }

        if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
        	sender.sendMessage(String.format(String.format( 
        					getMessages().getString("Message.rankGuiDisabledOrAllGuiDisabled"), 
        					getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") )));
        	return;
        }

        if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
        	String perm = getConfig( "Options.Ranks.Permission_GUI");
        	if (!sender.hasPermission(perm)) {

        		sender.sendMessage( getMessages().getString("Message.rankGuiMissingPermission") + " [" +
        				perm + "]");
        		return;
        	}
        }

        SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player );
        gui.open();
    }

}

package tech.mcprison.prison.spigot.commands;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class PrisonSpigotCommands
				extends PrisonSpigotBaseCommands
				implements Listener {

	private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

	//    CommandSender senderOfCommand;

	@Command(identifier = "mines", onlyPlayers = false,
			altPermissions = {"-none-", "mines.admin"})
	public void minesGUICommand(CommandSender sender) {
		if (!sender.hasPermission("mines.admin") && isPrisonConfig("mines-gui-enabled") ) {

			prisonManagerMines( sender );
//			sender.dispatchCommand("gui mines");
		}
		else {
			sender.dispatchCommand("mines help");
		}
	}


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


    @Command( identifier = "gui mines", description = "GUI Mines",
  		  aliases = {"prisonmanager mines"},
		  onlyPlayers = true )
    private void prisonManagerMines(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
        	sender.sendMessage( messages.getString("Message.CantRunGUIFromConsole"));
        	return;
        }

        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled") ){
            sender.sendMessage(messages.getString("Message.mineOrGuiDisabled"));
            return;
        }


        if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
        	String perm = getConfig( "Options.Mines.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                sender.sendMessage(messages.getString("Message.mineMissingGuiPermission") + " [" +
        				perm + "]");
                return;
            }
        }

        SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI( player );
        gui.open();
    }


    @Command( identifier = "gui ranks", description = "GUI Ranks",
    		  aliases = {"prisonmanager ranks"},
    		  onlyPlayers = true )
    private void prisonManagerRanks(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
        	sender.sendMessage( messages.getString("Message.CantRunGUIFromConsole"));
        	return;
        }

        if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
        	sender.sendMessage(String.format(String.format( 
        					messages.getString("Message.rankGuiDisabledOrAllGuiDisabled"), 
        					getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") )));
        	return;
        }

        if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
        	String perm = getConfig( "Options.Ranks.Permission_GUI");
        	if (!sender.hasPermission(perm)) {

        		sender.sendMessage(messages.getString("Message.rankGuiMissingPermission") + " [" +
        				perm + "]");
        		return;
        	}
        }

        SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player );
        gui.open();
    }


    /**
     * NOTE: onlyPlayers needs to be false so players can use /gui help on the command, even from console.
     *
     * @param sender
     */
    @Command( identifier = "gui", description = "The GUI",
    		  aliases = {"prisonmanager gui", "gui admin"},
    		  permissions = {"prison.admin", "prison.prisonmanagergui"},
    		  onlyPlayers = false
    		)
    private void prisonManagerGUI(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
        	sender.sendMessage( messages.getString("Message.CantRunGUIFromConsole"));
        	return;
        }

    	SpigotPrisonGUI gui = new SpigotPrisonGUI(player);
        gui.open();
    }
}

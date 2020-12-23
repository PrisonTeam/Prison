package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotMinesCommands
				extends PrisonSpigotBaseCommands {
	
	@Command(identifier = "mines", onlyPlayers = false,
			altPermissions = {"-none-", "mines.admin"})
	public void minesGUICommand(CommandSender sender) {
		if (!sender.hasPermission("mines.admin") && isPrisonConfig("mines-gui-enabled") ) {

			prisonManagerMines( sender );

            if ( !(!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled")) ){
                sender.dispatchCommand("gui mines");
            }
		}
		else {
			sender.dispatchCommand("mines help");
		}
	}
	

    @Command( identifier = "gui mines", description = "GUI Mines",
  		  aliases = {"prisonmanager mines"},
		  onlyPlayers = true )
    private void prisonManagerMines(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
        	sender.sendMessage( getMessages().getString("Message.CantRunGUIFromConsole"));
        	return;
        }

        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled") ){
            sender.sendMessage( getMessages().getString("Message.mineOrGuiDisabled"));
            return;
        }


        if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
        	String perm = getConfig( "Options.Mines.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                sender.sendMessage( getMessages().getString("Message.mineMissingGuiPermission") + " [" +
        				perm + "]");
                return;
            }
        }

        SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI( player );
        gui.open();
    }

}

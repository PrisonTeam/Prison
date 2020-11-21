package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class PrisonSpigotCommands
				extends PrisonSpigotBaseCommands
				implements Listener {

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
        	sender.sendMessage( getMessages().getString("Message.CantRunGUIFromConsole"));
        	return;
        }

    	SpigotPrisonGUI gui = new SpigotPrisonGUI(player);
        gui.open();
    }
}

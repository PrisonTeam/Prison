package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotMinesCommands
				extends PrisonSpigotBaseCommands {
	
	@Command(identifier = "mines", onlyPlayers = false,
			altPermissions = {"-none-", "mines.admin"})
	public void minesGUICommand(CommandSender sender) {
		if (!sender.hasPermission("mines.admin")) {

            if (isPrisonConfig("prison-gui-enabled") && isConfig("Options.Mines.GUI_Enabled")){
                sender.dispatchCommand("gui mines");
            }

		} else {
			sender.dispatchCommand("mines help");
		}
	}
}

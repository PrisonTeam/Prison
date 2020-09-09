package tech.mcprison.prison.spigot.commands;

import org.bukkit.event.Listener;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.util.Objects;

/**
 * @author RoyalBlueRanger
 * @author GABRYCA
 */
public class PrisonShortcutCommands implements Listener {

	/**
	 * <p>This command, <b>/Prison gui</b> and many others are more of convenience commands which
	 * allows access to the gui from the base /prison commands. This will allow
	 * the players to find it easier, and it will also be easier to recall.
	 * </p>
	 *
	 * <p>The actual gui command, which is <b>/prisonmanager gui</b> is not able to
	 * be integrated in to the main prison command sets due to the requirement of
	 * the gui being native spigot. Cannot mix the two.  But can have /prison gui
	 * internally call /prisonmanger gui to give the illusion they are connected.
	 * </p>
	 *
	 * @param sender
	 */

    @Command(identifier = "prison gui", description = "Opens the Prison GUI menus.",
			permissions = "prison.gui", onlyPlayers = true)
	public void prisonGui(CommandSender sender) {

    	if (!(Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prison-gui-enabled")).equalsIgnoreCase("true"))){
			sender.sendMessage(SpigotPrison.format("&cThe GUI's disabled, if you want to use it, edit the config.yml!"));
			return;
		}


    	String formatted = "prisonmanager gui";
		Prison.get().getPlatform().dispatchCommand(sender, formatted);
	}

	@Command(identifier = "mines", onlyPlayers = false,
			altPermissions = {"-none-", "mines.admin"})
	public void minesGUICommand(CommandSender sender) {
		if (!sender.hasPermission("mines.admin") && Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("mines-gui-enabled")).equalsIgnoreCase("true")) {
			sender.dispatchCommand("prisonmanager mines");
		} else {
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
			if ((ladderName.equalsIgnoreCase("default") || ladderName.equalsIgnoreCase("ranks")) && Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("ranks-gui-enabled")).equalsIgnoreCase("true")){
				sender.dispatchCommand("prisonmanager ranks");
			} else if (ladderName.equalsIgnoreCase("prestiges") && Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("ranks-gui-prestiges-enabled")).equalsIgnoreCase("true")){
				sender.dispatchCommand("prisonmanager prestiges");
			} else {
				sender.dispatchCommand("ranks list " + ladderName);
			}
		} else {
			sender.dispatchCommand("ranks help");
		}
	}

	@Command(identifier = "prestiges", onlyPlayers = true, altPermissions = {"-none-", "prison.admin"})
	public void prestigesGUICommand(CommandSender sender){

		if (!(Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prestiges")).equalsIgnoreCase("true"))) {
			sender.sendMessage(SpigotPrison.format("&cPrestiges are disabled by default, please edit it in your config.yml!"));
			return;
		}

		if (Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prestiges-gui-enabled")).equalsIgnoreCase("true")) {
			sender.dispatchCommand( "prisonmanager prestiges");
		} else {
			sender.dispatchCommand( "ranks list prestiges");
		}
	}

	@Command(identifier = "prestige", onlyPlayers = true, altPermissions = "-none-")
	public void prestigesPrestigeCommand(CommandSender sender) {
		if (Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prestiges")).equalsIgnoreCase("true")) {
			sender.dispatchCommand("prisonmanager prestige");
		}
	}

}

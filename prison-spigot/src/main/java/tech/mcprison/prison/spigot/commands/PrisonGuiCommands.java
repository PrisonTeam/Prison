package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrisonGuiCommands {
	
	/**
	 * <p>This command, <b>/Prison gui</b> is more of convenience command which
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

    	// Get the version
    	String versionBukkit = Bukkit.getVersion();
    	// Don't open with these versions
		if (versionBukkit.contains("1.8.1") || versionBukkit.contains("1.8.2") || versionBukkit.contains("1.8.3") || versionBukkit.contains("1.8.4") || versionBukkit.contains("1.8.5") || versionBukkit.contains("1.8.6") || versionBukkit.contains("1.8.7") || versionBukkit.contains("1.8.8") || versionBukkit.contains("1.8.9") || versionBukkit.contains("1.8") || versionBukkit.contains("1.8.0")) {
			sender.sendMessage(SpigotPrison.format("&cSorry, but GUIs don't works with 1.8.9 or older version due to issues"));
			return;
		}
    	String formatted = "prisonmanager gui";
        PrisonAPI.dispatchCommand(formatted);
        Prison.get().getPlatform().dispatchCommand( sender, formatted );
	}
}

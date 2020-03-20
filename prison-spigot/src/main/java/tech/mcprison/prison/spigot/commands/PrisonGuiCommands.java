package tech.mcprison.prison.spigot.commands;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;

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
	
    	String formatted = "prisonmanager gui";
        PrisonAPI.dispatchCommand(formatted);
        Prison.get().getPlatform().dispatchCommand( sender, formatted );
	}
}

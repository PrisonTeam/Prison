package tech.mcprison.prison.spigot.autofeatures;


import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.spigot.autofeatures.events.PrisonDebugBlockInspector;
import tech.mcprison.prison.spigot.commands.PrisonSpigotBaseCommands;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.Location;

public class PrisonDebugBlockInspectorCommand
		extends PrisonSpigotBaseCommands {


	@Command( 
			identifier = "mines debugBlockBreak", 
			description = "This will debug the BlockBreakEvent chain of plugins handling the "
			+ "event. Look at a block, while holding the tool of choice, and then "
			+ "issue this command.", 
			altPermissions = "prison.admin", onlyPlayers = true )
	private void mineDebugBlockBreak( CommandSender sender ) {


		PrisonDebugBlockInspector blockInspector = PrisonDebugBlockInspector.getInstance();


		Player player = Prison.get().getPlatform().getPlayer( sender.getPlatformPlayer().getUUID() ).orElse( null );


		if ( player != null && player instanceof SpigotPlayer ) {

			SpigotPlayer sPlayer = (SpigotPlayer) player;

			Location location = sPlayer.getLineOfSightExactLocation();

			boolean isSneaking = true;

			blockInspector.debugBlockBreak( sPlayer, isSneaking, location );
		}


	}

}

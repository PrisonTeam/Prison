package tech.mcprison.prison.spigot.utils;

import org.bukkit.Bukkit;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class PrisonUtilsMessages
	extends PrisonUtils
{
	private boolean enableMessageMsg = false;
	private boolean enableMessageBroadcast = false;
	
	public PrisonUtilsMessages() {
		super();
		
		
	}
	
	/**
	 * <p>There is no initialization needed for these commands.
	 * <p>
	 * 
	 * <p>This function must return a value of true to indicate that this 
	 * set of commands are enabled.  If it is set to false, then these
	 * commands will not be registered when prison is loaded.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	protected Boolean initialize()
	{
		return true;
	}
	
	
	@Command(identifier = "prison utils msg", 
			description = "Send a message to a player without any prefix.  " +
					"This will not be sent to the console.",
		onlyPlayers = false, 
		permissions = "prison.utils.message.msg", 
		altPermissions = "prison.utils.message.msg.others")
	public void utilMessageMsg(CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			
			@Wildcard(join=true)
			@Arg(name = "msg", description = "The message to send", def = "") String msg ) {
		
		if ( !isEnableMessageMsg() ) {
			
			Output.get().logInfo( "Prison's utils command msg is disabled in modules.yml." );
		}
		else {
	
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.message.msg", 
					"prison.utils.message.msg.others" );
	
			// Player cannot be null.  If it is null, then there was a failure.
			if ( player != null && player.isOnline() ) {
				
				player.sendMessage( msg );
			}
		}
	}
	
	@Command(identifier = "prison utils broadcast", 
			description = "Send a message to all players online.  " +
					"This will not be sent to the console.",
					onlyPlayers = false, 
					permissions = "prison.utils.message.broadcast", 
					altPermissions = "prison.utils.message.broadcast.others")
	public void utilMessageBroadcast(CommandSender sender, 
			
			@Wildcard(join=true)
	@Arg(name = "msg", description = "The message to send", def = "") String msg ) {
		
		if ( !isEnableMessageMsg() ) {
			
			Output.get().logInfo( "Prison's utils command msg is disabled in modules.yml." );
		}
		else {
			
			Bukkit.getServer().broadcastMessage( msg );
			
		}
	}

	
	public boolean isEnableMessageMsg() {
		return enableMessageMsg;
	}
	public void setEnableMessageMsg( boolean enableMessageMsg ) {
		this.enableMessageMsg = enableMessageMsg;
	}

	public boolean isEnableMessageBroadcast() {
		return enableMessageBroadcast;
	}
	public void setEnableMessageBroadcast( boolean enableMessageBroadcast ) {
		this.enableMessageBroadcast = enableMessageBroadcast;
	}
	
}

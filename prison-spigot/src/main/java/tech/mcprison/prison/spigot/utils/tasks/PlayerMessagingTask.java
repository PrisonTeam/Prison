package tech.mcprison.prison.spigot.utils.tasks;

import org.bukkit.entity.Player;

import tech.mcprison.prison.autofeatures.PlayerMessageData;
import tech.mcprison.prison.autofeatures.PlayerMessaging;
import tech.mcprison.prison.autofeatures.PlayerMessaging.MessageType;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class PlayerMessagingTask
	implements PrisonRunnable
{
	private Player player;
	private PlayerMessageData messageData = null;
	

	public PlayerMessagingTask( Player player, PlayerMessageData messageData ) {
		super();
		
		this.player = player;
		this.messageData = messageData;
	}
	
	public static void submitTask( Player player, MessageType messageType, String message ) {
		
		SpigotPlayer bPlayer = new SpigotPlayer( player );
		
		PlayerCachePlayerData playerCache = PlayerCache.getInstance().getOnlinePlayer( bPlayer );
		if ( playerCache != null ) {
			
			
			PlayerMessaging messagingData = playerCache.getPlayerMessaging();
			
			PlayerMessageData mData = messagingData.addMessage( messageType, message );
			
			if ( mData.getTaskId() == -1 ) {
				
				PlayerMessagingTask messagingTask = new PlayerMessagingTask( player, mData );
				
				int jobId = PrisonTaskSubmitter.runTaskLater( messagingTask, 0 );
				messagingTask.setTaskId( jobId );
			}
			
			
		}
	}
	
	public void submit() {
		PrisonTaskSubmitter.runTaskLater(this, 0);
	}
	

	@Override
	public void run()
	{
		switch ( messageData.getMessageType() )
		{
			case actionBar:
			{
				
				SpigotCompatibility.getInstance().sendActionBar( player, messageData.getMessage() );
				break;
			}
			
			case title:
			{
				SpigotCompatibility.getInstance().sendTitle( player, messageData.getMessage(),
						null, 10, 20, 10 );
				
				break;
			}
			
			default:
				
				// Do nothing... let the task end since the message type was not valid
		}
				
		
	}
		
	
	private void  setTaskId( int taskId ) {
		if ( messageData != null ) {
			messageData.setTaskId( taskId );
		}
	}
}

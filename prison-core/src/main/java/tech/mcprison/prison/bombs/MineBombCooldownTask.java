package tech.mcprison.prison.bombs;

import java.util.Map;
import java.util.TreeMap;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class MineBombCooldownTask 
	implements PrisonRunnable {

	public static int DELAY_TICKS = 1;
	
	private static final Map<String, Integer> playerCooldowns = new TreeMap<>();

	private String playerUUID;
	
	private int taskId = -1;
	
	public MineBombCooldownTask( String playerUUID ) {
		super();
		
		this.playerUUID = playerUUID;
	}
	
	public static boolean addPlayerCooldown( Player player, int ticks ) {
		boolean results = false;
		
		int cooldown = checkPlayerCooldown( player );
		if ( cooldown <= 0 ) {
			submitCooldownTask( player, ticks );
			results = true;
		}
		
		return results;
	}
	
	
	private static int submitCooldownTask( Player player, int ticks ) {
		int results = -1;
		
		if ( player != null ) {
			String playerUUID = player.getUUID().toString();
			
			playerCooldowns.put( playerUUID, ticks );
			
			MineBombCooldownTask task = new MineBombCooldownTask( playerUUID );
			
			int taskId = PrisonTaskSubmitter.runTaskTimer( task, DELAY_TICKS, DELAY_TICKS);

			task.setTaskId( taskId );
		}
		
		return results;
	}
	
	/**
	 * Since this runs every 5 ticks, it removes 5 from the cooldown ticks.
	 * When it reaches a value of zero, then the cooldown is over.
	 */
	@Override
	public void run() {
		
		Integer cooldownTicks = playerCooldowns.get( getPlayerUUID() );
		
		int ticksRemaining = cooldownTicks == null ? 0 : cooldownTicks - DELAY_TICKS;
		
		if ( ticksRemaining <= 0 )
		{
			playerCooldowns.remove( getPlayerUUID() );
			
			PrisonTaskSubmitter.cancelTask( getTaskId() );
		}
		else
		{
			playerCooldowns.put( getPlayerUUID(), ticksRemaining );
		}
	}

	
	
	public static int checkPlayerCooldown( Player player )
	{
		int results = 0;
		
		if ( player != null ) {
			
			String playerUUID = player.getUUID().toString();
			
			if ( playerCooldowns.containsKey( playerUUID ) )
			{
				results = playerCooldowns.get( playerUUID );
			}
		}

		return results;
	}
	

	public String getPlayerUUID() {
		return playerUUID;
	}
	public void setPlayerUUID(String playerUUID) {
		this.playerUUID = playerUUID;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
		
}

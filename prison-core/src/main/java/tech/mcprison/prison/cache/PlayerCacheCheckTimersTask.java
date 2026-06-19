package tech.mcprison.prison.cache;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

/**
 * <p>This periodically ran task will go through all cached players and update 
 * their status with the timers and earnings.  This is a light weight task 
 * that only updates a few variables, and can be ran fairly frequently of about
 * once per minute.  There is no real need to run more frequently, but could 
 * increase the cycle time with no real impact, other than identifying when a player 
 * becomes AFK.
 * </p>
 * 
 * <p>This task has nothing to do with trying to save anything.  That is 
 * performed in PlayerCacheSaveAllPlayersTask, which is why this is a 
 * fast and lightweight.
 * </p>
 * 
 * <p>The checkTimers will cut the online
 * time in to more manageable sizes, and will eventually include checks for 
 * AFK status. These checks are bypassed if the player is not online. 
 * </p>
 * 
 * <p>By adding a zero earnings, this will force the "cache" of the last few 
 * minutes to progress and purge the older entries.  So if the player mines for 
 * 5 minutes and the cache keeps track of earnings per minute over the last 5
 * minutes, then when they stop mining, and when this task runs, there will be
 * an add earnings of zero which will create a new entry for zero.  As such, 
 * this will purge the oldest entry.  If a player is active, adding a zero
 * earning entry, will have no impact since it will change the stored value,
 * nor would it advance the entries.  
 * </p>
 *
 */
public class PlayerCacheCheckTimersTask
	extends PlayerCacheRunnable
{

	private HashSet<String> processedKeys;
	private int attempts = 0;
	
	public PlayerCacheCheckTimersTask() {
		super();
		
		this.processedKeys = new HashSet<>(); 
	}
	
	/**
	 * This will submit this task to run at regular intervals only if the rank
	 * module is enabled.
	 * 
	 * If ranks are not enabled, then this task will not be started since there will be
	 * no reason to use the player cache.
	 * 
	 * @return
	 */
	public static PlayerCacheRunnable submitPlayerStatsCacheUpdater() {
		
		PlayerCacheCheckTimersTask task = null;
		
		Module ranksModule = Prison.get().getModuleManager().getModule( 
					ModuleElementType.RANK.name() );

		if ( ranksModule != null && ranksModule.isEnabled()  ) {
			
			task = new PlayerCacheCheckTimersTask();
			
			int repeatTimeTicks = Prison.get().getPlatform()
					.getConfigInt( PlayerCache.PLAYER_CACHE_UPDATE_PLAYER_STATS_CONFIG_NAME, 
							PlayerCache.PLAYER_CACHE_UPDATE_PLAYER_STATS_SEC ) * 20;
			
			// Submit Timer Task to start running in 30 seconds (600 ticks) and then
			// refresh stats every 10 seconds (200 ticks). 
			// This does not update any files or interacts with bukkit/spigot.
			int taskId = PrisonTaskSubmitter.runTaskTimerAsync( task, 600, repeatTimeTicks );
			task.setTaskId( taskId );
		}
		
		return task;
	}
	
	@Override
	public void run()
	{
	
		// Everytime this runs, clear the processed keys set:
		processedKeys.clear();
		attempts = 0;
		
		processCache();
		
	}
	
	/**
	 * Only allow the cache to be processed if ranks is enabled.
	 */
	private void processCache() {
		PlayerCache pCache = PlayerCache.getInstance();
		
	      Module ranksModule = Prison.get().getModuleManager().getModule( 
	    		  								ModuleElementType.RANK.name() );
	        
	      if ( ranksModule != null && ranksModule.isEnabled() &&
				pCache.getPlayers() != null && pCache.getPlayers().keySet().size() > 0 ) {
			
			try
			{
				Set<String> keys = null;
				
				synchronized ( pCache.getPlayers() ) {
					keys = new TreeSet<>( pCache.getPlayers().keySet() );
				}
				
				for ( String key : keys )
				{
					if ( processedKeys.contains( key ) ) {
						// Already processed this key so skip it:
						break;
					}
					processedKeys.add( key );
					
					PlayerCachePlayerData playerData = null;
					
					synchronized ( pCache.getPlayers() ) {
						
						playerData = pCache.getPlayers().get( key );
					}
					
					if ( playerData != null ) {
						
						
						playerData.checkTimers();
						
						// By adding a zero earnings, this will force the earnings "cache" to 
						// progress, even if the player stopped mining.
						playerData.addEarnings( 0, null );

						RankPlayer rPlayer = null;
						
						Player player = playerData.getPlayer();
						
						if ( player != null ) {
							rPlayer = player.getRankPlayer();
						}
						else {
							UUID uuid = UUID.fromString( key );
							
							
							rPlayer = Prison.get().getPlatform()
									.getRankPlayer( uuid, 
											playerData != null ? playerData.getPlayerName() : "" );
						}
						
						if ( rPlayer != null ) {
							rPlayer.updateTotalLastValues(playerData);
						}
					}
					
				}
			}
			catch ( ConcurrentModificationException e )
			{
				// We can ignore this overall.  It is a very rare occurrence which happens when
				// a player is added or removed from an external process.  Since this is a maintenance 
				// thread, it takes on a secondary priority.
				
				// Try to process the list three times then give up:
				if ( attempts++ < 3 ) {
					processCache();
				}
			}
		}
	
	}

}

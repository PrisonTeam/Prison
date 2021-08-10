package tech.mcprison.prison.cache;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public void run()
	{
	
		PlayerCache pCache = PlayerCache.getInstance();
		
		List<String> keys = new ArrayList<>( pCache.getPlayers().keySet() );
		
		for ( String key : keys )
		{
			PlayerCachePlayerData playerData = pCache.getPlayers().get( key );
			
			if ( playerData != null ) {
				
				playerData.checkTimers();
				
				// By adding a zero earnings, this will force the earnings "cache" to 
				// progress, even if the player stopped mining.
				playerData.addEarnings( 0 );
			}
			
		}
	
	}

}

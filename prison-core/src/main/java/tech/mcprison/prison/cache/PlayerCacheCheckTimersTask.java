package tech.mcprison.prison.cache;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
	
	@Override
	public void run()
	{
	
		// Everytime this runs, clear the processed keys set:
		processedKeys.clear();
		attempts = 0;
		
		processCache();
		
	}
	private void processCache() {
		PlayerCache pCache = PlayerCache.getInstance();
		
		if ( pCache.getPlayers() != null && pCache.getPlayers().keySet().size() > 0 ) {
			
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

package tech.mcprison.prison.backpacks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import tech.mcprison.prison.cache.PlayerCacheStats;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class BackpackCache {

	public static final String BACKPACK_CACHE_WRITE_DELAY_CONFIG_NAME = "backpack.cache.write_delay";
	public static final long BACKPACK_CACHE_WRITE_DELAY_VALUE_MS = 60000; // 60 seconds
	
	public static final String BACKPACK_CACHE_TIME_TO_LIVE_CONFIG_NAME = "backpack.cache.time_to_live";
	public static final long BACKPACK_CACHE_TIME_TO_LIVE_VALUE_MS = 30 * 60 * 1000; // 30 mins
	
	private static BackpackCache instance;
	
	private BackpackCacheFiles cacheFiles;
	private BackpackCacheEvents cacheEvents;
	
	
//	private long writeDelay = 0L;
	
	private PlayerCacheStats stats;

	
	private SortedMap<String, BackpackCachePlayerData> players;
	
	private Map<BackpackCacheRunnable, BackpackCachePlayerData> tasks;
	
	private BackpackCacheRunnable saveAllTask;
	

	
	private BackpackCache() {
		super();
		
		this.players = Collections.synchronizedSortedMap( new TreeMap<>() );
		
		this.tasks = Collections.synchronizedMap( new HashMap<>() );
		
		this.cacheFiles = new BackpackCacheFiles();
		
		
		this.stats = new PlayerCacheStats();

	}
	
	public static BackpackCache getInstance() {
		if ( instance == null ) {
			synchronized ( BackpackCache.class )
			{
				if ( instance == null ) {

					instance = new BackpackCache();
					instance.internalInititalize();
				}
			}
		}
		return instance;
	}


	
	private void internalInititalize() {
		
		// The BackpackCacheEvents self-registers with Prison's eventBus:
		cacheEvents = new BackpackCacheEvents();

		saveAllTask = submitCacheRefresh();
		
	}
	
	/**
	 * <p>The plugin is shutting down so flush all dirty cache items, but first
	 * disable the caching so as any changes are passed through directly to the
	 * database.
	 * </p>
	 * 
	 * <p>Since the plugin is shutting down, flush the cache synchronously in this
	 * thread.  Do not submit, or the database may be shutdown before all dirty
	 * cache items can be saved.
	 * </p>
	 */
	public static void onDisable() {
		getInstance().onDisableInternal();
		
	}
	
	/**
	 * <p>This shuts down the cache and unloads all the players while 
	 * updating the database if they have uncommitted values.  This also
	 * will cancel any outstanding tasks and then flush the uncommitted
	 * values if they still exist.
	 * </p>
	 * 
	 * <p>This function runs all database transactions synchronously so as to 
	 * ensure all data is written to the database before the server is 
	 * terminated.
	 * </p>
	 * 
	 */
	private void onDisableInternal() {
		
		// Shutdown the save all task so it won't reload players or try to refresh data:
		PrisonTaskSubmitter.cancelTask( saveAllTask.getTaskId() );

		// Shutdown the timerTasks
//		PrisonTaskSubmitter.cancelTask( checkTimersTask.getTaskId() );

		
		// save all dirty cache items and purge cache:
		if ( getPlayers().size() > 0 ) {


			Set<String> keys = getPlayers().keySet();

			synchronized ( getPlayers() ) {
				
				for ( String key : keys ) {
					// Remove the player from the cache and get the playerData:
					BackpackCachePlayerData playerData = getPlayers().remove( key );
					
					if ( playerData != null ) {
						
						// Note: Since we are logging online time, then all players that are
						//       in the cache are considered constantly dirty and needs to be
						//       saved.
						
						// Since the disable function has been called, we can only assume the
						// server is shutting down.  We need to save dirty player caches, but
						// they must be done in-line so the shutdown process will wait for all
						// players to be saved.
						
						getCacheFiles().toJsonFile( playerData );
						
						if ( playerData.getTask() != null ) {
							
							synchronized( getTasks() ) {
								
								getTasks().remove( playerData.getTask() );
							}
							
							PrisonTaskSubmitter.cancelTask( playerData.getTask().getTaskId() );
						}
					}
				}
			}
			
		}
		
		// Cancel and flush any uncompleted tasks that are scheduled to run:
		if ( getTasks().size() > 0  ) {
			
			
			Set<BackpackCacheRunnable> keys = getTasks().keySet();
			
			for ( BackpackCacheRunnable key : keys ) {
				
				if ( !key.isCancelled() ) {
					// Cancel the task:
					key.cancel();
					
					// Remove the task and get the player data:
					BackpackCachePlayerData playerData = null;

					synchronized( getTasks() ) {
						
						playerData = getTasks().remove( key );
					}
					
					if ( playerData != null && 
							playerData.getTask() != null &&
							playerData.getTask().getTaskId() > 0 ) {
						
						PrisonTaskSubmitter.cancelTask( playerData.getTask().getTaskId() );
					}
					
				}
			}
			
		}
		
		// Shutdown the connections:
		
	}


	
	public void addPlayerData( BackpackCachePlayerData playerData ) {
		
		if ( playerData != null ) {
			getStats().incrementLoadPlayers();
			
			synchronized ( getPlayers() ) {
				
				getPlayers().put( playerData.getPlayerUuid(), playerData );
			}
			
		}
	}
	
	public BackpackCachePlayerData removePlayerData( BackpackCachePlayerData playerData ) {
		BackpackCachePlayerData removed = playerData;
		if ( playerData != null ) {
			getStats().incrementRemovePlayers();
			
			synchronized ( getPlayers() ) {
				
				removed = getPlayers().remove( playerData.getPlayerUuid() );
			}
		}
		return removed;
	}
	
	
	/**
	 * <p>This function will return a null if the player is not loaded in the cache.
	 * Null is a valid value even if the player is online.
	 * This function should NEVER be used
	 * for any critical data such as tracking blocks, time, earnings, or inventory. 
	 * Examples of acceptable loss would be with messaging.  Loss of a few messages is
	 * not that critical, and actually would be a very rare situation. Example, if a 
	 * player is mining then their cache should already be loaded so calling this function
	 * should never find the situation where the player's cache entry does not exist.
	 * </p>
	 * 
	 * <p>Since this function will fail with the return a null if the player is not loaded,
	 * this function will not cause blocking on the runnable thread.
	 * </p>
	 * 
	 * <p>If the player is not loaded, and a null is returned, then an async task
	 * will be submitted to load it.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public BackpackCachePlayerData getOnlinePlayer( Player player ) {
		BackpackCachePlayerData playerData = getPlayer( player );
				
		return playerData;
	}
	
	private BackpackCachePlayerData getPlayer( Player player ) {
		return getPlayer( player, true );
	}
	
	
	/**
	 * <p>This returns the cached player object.  If they have not been loaded
	 * yet, then this will load the player object while waiting for it.
	 * </p>
	 * 
	 * <p>This used to return a null while submitting a loadPlayer task.
	 * </p>
	 * 
	 * @param player
	 * @return The cached player object. If null, then may indicate the player is 
	 * 				actively being loaded, so try again later.
	 */
	private BackpackCachePlayerData getPlayer( Player player, boolean loadIfNotInCache ) {
		BackpackCachePlayerData playerData = null;
		getStats().incrementGetPlayers();
		
		if ( player != null && player.getUUID() != null ) {
			
			String playerUuid = player.getUUID().toString();
			
			
			if ( getPlayers().containsKey( playerUuid ) ) {
				
				// Note: if the player has not been loaded yet, this will return a null:
				synchronized ( getPlayers() ) {
					
					playerData = getPlayers().get( playerUuid );
				}
			}
			else if ( loadIfNotInCache ) {
				
				// Load the player's existing balance:
				playerData = getCacheFiles().fromJson( player );
				
				// NOTE: playerData.isOnline() is dynamic and tied back to the Player object.
				//       So if they are offline, an OfflinePlayer, then it will automatically
				//       track that.  Also if the PlayerData object does not have a reference
				///      to Player, then it's automatically considered offline.
				
				// Save it to the cache:
				addPlayerData( playerData );
//			runLoadPlayerNow( player );
//			submitAsyncLoadPlayer( player );
			}
			
			if ( playerData != null  ) {
				
				if ( playerData.getPlayer() == null || !playerData.getPlayer().equals( player )  ) {
					
					playerData.setPlayer( player );
				}
				
//				playerData.updateLastSeen();
			}
		}
		
		return playerData;
	}


	protected void submitAsyncLoadPlayer( Player player ) {
	
		if ( player != null ) {
			
			BackpackCacheLoadPlayerTask task = new BackpackCacheLoadPlayerTask( player );
			
			// Submit task to run right away:
			int taskId = PrisonTaskSubmitter.runTaskLaterAsync( task, 0 );
			task.setTaskId( taskId );
		}
	}
	
	
//	/**
//	 * <p>This loads the player cache object inline.  It does not run it as a 
//	 * task in another thread.
//	 * </p>
//	 * 
//	 * <p>This is not used anywhere.
//	 * </p>
//	 * 
//	 * @param player
//	 */
//	protected void runLoadPlayerNow( Player player ) {
//		
//		if ( player != null ) {
//			
//			PlayerCacheLoadPlayerTask task = new PlayerCacheLoadPlayerTask( player );
//			
//			task.run();
////			// Submit task to run right away:
////			int taskId = PrisonTaskSubmitter.runTaskLaterAsync( task, 0 );
////			task.setTaskId( taskId );
//		}
//	}
	
	
	protected void submitAsyncUnloadPlayer( Player player ) {
		
		BackpackCachePlayerData playerData = getPlayer( player, false );
		
		if ( playerData != null ) {
			
			// Submit to unload right away with no delay:
			BackpackCacheUnloadPlayerTask task = new BackpackCacheUnloadPlayerTask( playerData );
			int taskId = PrisonTaskSubmitter.runTaskLaterAsync( task, 0 );
			task.setTaskId( taskId );
		}
	}
	
	

	/** 
	 * Submit task to run only once.  Should be started when this cache initializes.
	 * 
	 */
	public BackpackCacheRunnable submitCacheRefresh() {
		
		BackpackCacheSaveAllPlayersTask task = new BackpackCacheSaveAllPlayersTask();
		
		// Submit Timer Task to start running in 10 minutes (6000 ticks) and then
		// refresh stats every 5 minutes (3000 ticks):
		int taskId = PrisonTaskSubmitter.runTaskTimerAsync( task, 6000, 3000 );
		task.setTaskId( taskId );
		
		return task;
	}
	
	
	
	public BackpackCacheFiles getCacheFiles() {
		return cacheFiles;
	}
	public void setCacheFiles(BackpackCacheFiles cacheFiles) {
		this.cacheFiles = cacheFiles;
	}

	public BackpackCacheEvents getCacheEvents() {
		return cacheEvents;
	}
	public void setCacheEvents(BackpackCacheEvents cacheEvents) {
		this.cacheEvents = cacheEvents;
	}

	public PlayerCacheStats getStats() {
		return stats;
	}
	public void setStats(PlayerCacheStats stats) {
		this.stats = stats;
	}

	public SortedMap<String, BackpackCachePlayerData> getPlayers() {
		return players;
	}
	public void setPlayers(SortedMap<String, BackpackCachePlayerData> players) {
		this.players = players;
	}

	public Map<BackpackCacheRunnable, BackpackCachePlayerData> getTasks() {
		return tasks;
	}
	public void setTasks(Map<BackpackCacheRunnable, BackpackCachePlayerData> tasks) {
		this.tasks = tasks;
	}

}

package tech.mcprison.prison.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;


public class PlayerCache {
	
	public static final String PLAYER_CACHE_WRITE_DELAY = "playercache.write_delay";
	public static final long PLAYER_CACHE_WRITE_DELAY_VALUE_MS = 60000; // 60 seconds
	
	public static final String PLAYER_CACHE_TIME_TO_LIVE = "playercache.time_to_live";
	public static final long PLAYER_CACHE_TIME_TO_LIVE_VALUE_MS = 30 * 60 * 1000; // 30 mins
	
	private static PlayerCache instance;
	
	private PlayerCacheFiles cacheFiles;
	private PlayerCacheEvents cacheEvents;
	
	
	private long writeDelay = 0L;
	
	private PlayerCacheStats stats;

	
	private TreeMap<String, PlayerCachePlayerData> players;
	
	private Map<PlayerCacheRunnable, PlayerCachePlayerData> tasks;
	
	private PlayerCacheRunnable saveAllTask;
	
	
	private PlayerCache() {
		super();
		
		this.players = new TreeMap<>();
		
		this.tasks = new HashMap<>();
		
		this.cacheFiles = new PlayerCacheFiles();
		
		
		this.stats = new PlayerCacheStats();

	}
	
	public static PlayerCache getInstance() {
		if ( instance == null ) {
			synchronized ( PlayerCache.class )
			{
				if ( instance == null ) {

					instance = new PlayerCache();
					instance.internalInititalize();
				}
			}
		}
		return instance;
	}

	
	private void internalInititalize() {
		
		// The PlayerCacheEvents self-registers with Prison's eventBus:
		cacheEvents = new PlayerCacheEvents();

		saveAllTask = submitCacheRefresh();

		log( "PlayerCache: " + 
				"  " + PLAYER_CACHE_WRITE_DELAY + ": " + getWriteDelay() );
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

		
		// save all dirty cache items and purge cache:
		if ( getPlayers().size() > 0 ) {

			// Create a new set so as to prevent keys from being removed when purging
			// the getPlayers() collection:
			Set<String> keys = new TreeSet<>(getPlayers().keySet());

			for ( String key : keys ) {
				// Remove the player from the cache and get the playerData:
				PlayerCachePlayerData playerData = getPlayers().remove( key );
				
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
						getTasks().remove( playerData.getTask() );

						PrisonTaskSubmitter.cancelTask( playerData.getTask().getTaskId() );
					}
				}
			}
			
		}
		
		// Cancel and flush any uncompleted tasks that are scheduled to run:
		if ( getTasks().size() > 0  ) {
			List<PlayerCacheRunnable> keys = new ArrayList<>( getTasks().keySet() );
			
			for ( PlayerCacheRunnable key : keys ) {
				
				if ( !key.isCancelled() ) {
					// Cancel the task:
					key.cancel();
					
					// Remove the task and get the player data:
					PlayerCachePlayerData playerData = getTasks().remove( key );
					
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

	
	public void addPlayerData( PlayerCachePlayerData playerData ) {
		
		if ( playerData != null ) {
			getStats().incrementLoadPlayers();
			
			getPlayers().put( playerData.getPlayerUuid(), playerData );
			
		}
	}
	
	public void removePlayerData( PlayerCachePlayerData playerData ) {
		
		if ( playerData != null ) {
			getStats().incrementRemovePlayers();
			
			getPlayers().remove( playerData.getPlayerUuid() );
		}
	}
	
	
	
	/**
	 * <p>This returns the cached player object.  If they have not been loaded
	 * yet, then this will submit the loadPlayer task, which will then result 
	 * in this function returning a null.
	 * </p>
	 * 
	 * @param player
	 * @return The cached player object. If null, then may indicate the player is 
	 * 				actively being loaded, so try again later.
	 */
	private PlayerCachePlayerData getPlayer( Player player ) {
		getStats().incrementGetPlayers();
		
		if ( !getPlayers().containsKey( player.getUUID().toString() ) ) {
			
			// Load the player's existing balance:
			submitAsyncLoadPlayer( player );
		}

		// Note: if the player has not been loaded yet, this will return a null:
		PlayerCachePlayerData playerData = getPlayers().get( player.getUUID().toString() );
		return playerData;
	}


	protected void submitAsyncLoadPlayer( Player player ) {
	
		if ( player != null ) {
			
			PlayerCacheLoadPlayerTask task = new PlayerCacheLoadPlayerTask( player );
			
			// Submit task to run right away:
			int taskId = PrisonTaskSubmitter.runTaskLaterAsync( task, 0 );
			task.setTaskId( taskId );
		}
	}
	
	
	protected void submitAsyncUnloadPlayer( Player player ) {
		
		PlayerCachePlayerData playerData = getPlayer( player );
		
		if ( playerData != null ) {
			
			// Submit to unload right away with no delay:
			PlayerCacheUnloadPlayerTask task = new PlayerCacheUnloadPlayerTask( playerData );
			int taskId = PrisonTaskSubmitter.runTaskLaterAsync( task, 0 );
			task.setTaskId( taskId );
		}
	}
	
	

	/** 
	 * Submit task to run only once.  Should be started when this cache initializes.
	 * 
	 */
	public PlayerCacheRunnable submitCacheRefresh() {
		
		PlayerCacheSaveAllPlayersTask task = new PlayerCacheSaveAllPlayersTask();
		
		// Submit Timer Task to start running in 30 seconds (600 ticks) and then
		// refresh stats every 15 seconds (300 ticks):
		int taskId = PrisonTaskSubmitter.runTaskTimerAsync( task, 600, 300 );
		task.setTaskId( taskId );
		
		return task;
	}
	


	public String getPlayerDumpStats() {
		StringBuilder sb = new StringBuilder();

		List<String> keys = new ArrayList<>( getPlayers().keySet() );
		
		for ( String key : keys ) {
			PlayerCachePlayerData playerData = getPlayers().get( key );
			
			sb.append( playerData.toString() );
		}
		return sb.toString();
	}
	
	public PlayerCacheFiles getCacheFiles() {
		return cacheFiles;
	}
	public void setCacheFiles( PlayerCacheFiles cacheFiles ) {
		this.cacheFiles = cacheFiles;
	}

	public PlayerCacheEvents getCacheEvents() {
		return cacheEvents;
	}
	public void setCacheEvents( PlayerCacheEvents cacheEvents ) {
		this.cacheEvents = cacheEvents;
	}

	public PlayerCacheStats getStats() {
		return stats;
	}
	public void setStats( PlayerCacheStats stats ) {
		this.stats = stats;
	}
	
	

	protected void log( String message ) {
		Output.get().logInfo( message );
	}
	

	public long getWriteDelay() {
		return writeDelay;
	}
	public void setWriteDelay( long writeDelay ) {
		this.writeDelay = writeDelay;
	}

	protected Map<String, PlayerCachePlayerData> getPlayers() {
		return players;
	}


	public Map<PlayerCacheRunnable, PlayerCachePlayerData> getTasks() {
		return tasks;
	}

}

package tech.mcprison.prison.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;


public class PlayerCache {
	
	public static final String PLAYER_CACHE_WRITE_DELAY_CONFIG_NAME = "playercache.write_delay";
	public static final long PLAYER_CACHE_WRITE_DELAY_VALUE_MS = 60000; // 60 seconds
	
	public static final String PLAYER_CACHE_TIME_TO_LIVE_CONFIG_NAME = "playercache.time_to_live";
	public static final long PLAYER_CACHE_TIME_TO_LIVE_VALUE_MS = 30 * 60 * 1000; // 30 mins
	
	private static PlayerCache instance;
	
	private PlayerCacheFiles cacheFiles;
	private PlayerCacheEvents cacheEvents;
	
	
	private long writeDelay = 0L;
	
	private PlayerCacheStats stats;

	
	private SortedMap<String, PlayerCachePlayerData> players;
	
	private Map<PlayerCacheRunnable, PlayerCachePlayerData> tasks;
	
	private PlayerCacheRunnable saveAllTask;
	
	private PlayerCacheRunnable checkTimersTask;
	
	
	private PlayerCache() {
		super();
		
		this.players = Collections.synchronizedSortedMap( new TreeMap<>() );
		
		this.tasks = Collections.synchronizedMap( new HashMap<>() );
		
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
		
		checkTimersTask = submitCacheUpdatePlayerStats();

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
		PrisonTaskSubmitter.cancelTask( checkTimersTask.getTaskId() );

		
		// save all dirty cache items and purge cache:
		if ( getPlayers().size() > 0 ) {


			Set<String> keys = getPlayers().keySet();

			synchronized ( getPlayers() ) {
				
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
			
			
			Set<PlayerCacheRunnable> keys = getTasks().keySet();
			
			for ( PlayerCacheRunnable key : keys ) {
				
				if ( !key.isCancelled() ) {
					// Cancel the task:
					key.cancel();
					
					// Remove the task and get the player data:
					PlayerCachePlayerData playerData = null;

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

	
	public void addPlayerData( PlayerCachePlayerData playerData ) {
		
		if ( playerData != null ) {
			getStats().incrementLoadPlayers();
			
			synchronized ( getPlayers() ) {
				
				getPlayers().put( playerData.getPlayerUuid(), playerData );
			}
			
		}
	}
	
	public PlayerCachePlayerData removePlayerData( PlayerCachePlayerData playerData ) {
		PlayerCachePlayerData removed = playerData;
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
	public PlayerCachePlayerData getOnlinePlayer( Player player ) {
		PlayerCachePlayerData playerData = getPlayer( player );
				
		return playerData;
	}
	
	private PlayerCachePlayerData getPlayer( Player player ) {
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
	private PlayerCachePlayerData getPlayer( Player player, boolean loadIfNotInCache ) {
		PlayerCachePlayerData playerData = null;
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
				
				playerData.updateLastSeen();
			}
		}
		
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
		
		PlayerCachePlayerData playerData = getPlayer( player, false );
		
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
		
		// Submit Timer Task to start running in 10 minutes (6000 ticks) and then
		// refresh stats every 5 minutes (3000 ticks):
		int taskId = PrisonTaskSubmitter.runTaskTimerAsync( task, 6000, 3000 );
		task.setTaskId( taskId );
		
		return task;
	}
	
	
	public PlayerCacheRunnable submitCacheUpdatePlayerStats() {
		
		PlayerCacheCheckTimersTask task = new PlayerCacheCheckTimersTask();
		
		// Submit Timer Task to start running in 30 seconds (600 ticks) and then
		// refresh stats every 10 seconds (200 ticks). 
		// This does not update any files or interacts with bukkit/spigot.
		int taskId = PrisonTaskSubmitter.runTaskTimerAsync( task, 600, 200 );
		task.setTaskId( taskId );
		
		return task;
	}
	
	
	public void addPlayerBlocks( Player player, String mine, PrisonBlockStatusData block, int quantity ) {
		if ( block.getBlockType() == PrisonBlockType.minecraft ) {
			addPlayerBlocks( player, mine, block.getBlockName(), quantity );
		}
		else {
			String blockName = block.getBlockType() + ":" + block.getBlockName();
			addPlayerBlocks( player, mine, blockName, quantity );
		}
		
	}
//	public void addPlayerBlocks( Player player, String mine, PrisonBlock block, int quantity ) {
//		addPlayerBlocks( player, mine, block.getBlockName(), quantity );
//	}
	private void addPlayerBlocks( Player player, String mine, String blockName, int quantity ) {
		PlayerCachePlayerData playerData = getPlayer( player );
		
//		Output.get().logInfo( "### addPlayerBlock: mine= " + (mine == null ? "null" : mine) +
//				" block= " + (block == null ? "null" : block.getBlockName()) + " qty= " + quantity + "  playerData= " +
//				(playerData == null ? "null" : playerData.toString() ));
		
//		if ( playerData != null && playerData.getBlocksTotal() % 20 == 0 ) {
//			Output.get().logInfo( "#### PlayerCache: " + playerData.toString() );
//		}
		
		playerData.addBlock( mine, blockName, quantity );
		
		if ( player.isMinecraftStatisticsEnabled() ) {
			
			player.incrementMinecraftStatsMineBlock( player, blockName, quantity );
		}
	}
	
	/**
	 * This stores the earnings from the player so they can
	 * be averaged to find their earnings per minute.
	 * 
	 * @param player
	 * @param earnings
	 */
	public void addPlayerEarnings( Player player, double earnings ) {
		PlayerCachePlayerData playerData = getPlayer( player );
		
		String mineName = null;
		playerData.addEarnings( earnings, mineName );
	}
	public void addPlayerEarnings( Player player, double earnings, String mineName ) {
		PlayerCachePlayerData playerData = getPlayer( player );
		
		playerData.addEarnings( earnings, mineName );
	}
	public double getPlayerEarningsPerMinute( Player player ) {
		double earningsPerMinute = 0;
		
		PlayerCachePlayerData playerData = getPlayer( player );
		
		if ( playerData != null ) {
			earningsPerMinute = playerData.getAverageEarningsPerMinute();
		}
		
		return earningsPerMinute;
	}

	public long getPlayerBlocksTotal( Player player )
	{
		long blocksTotal = 0;
		
		PlayerCachePlayerData playerData = getPlayer( player );
		
		if ( playerData != null ) {
			blocksTotal = playerData.getBlocksTotal();
		}

		return blocksTotal;
	}
	public long getPlayerBlocksTotalByMine( Player player, String mineName )
	{
		long blocksTotalByMine = 0;
		
		PlayerCachePlayerData playerData = getPlayer( player );
		
		if ( playerData != null && mineName != null && playerData.getBlocksByMine() != null &&
				playerData.getBlocksByMine().containsKey( mineName ) ) {
			
			blocksTotalByMine = playerData.getBlocksByMine().get( mineName );
		}
		
		return blocksTotalByMine;
	}
	public long getPlayerBlocksTotalByBlockType( Player player, String blockType )
	{
		long blocksTotalByBlockType = 0;
		
		PlayerCachePlayerData playerData = getPlayer( player );
		
		if ( playerData != null && blockType != null && playerData.getBlocksByType() != null &&
				playerData.getBlocksByType().containsKey( blockType ) ) {
			blocksTotalByBlockType = playerData.getBlocksByType().get( blockType );
		}
		
		return blocksTotalByBlockType;
	}


	public String getPlayerDumpStats() {
		StringBuilder sb = new StringBuilder();

		Set<String> keys = getPlayers().keySet();
		
		synchronized ( getPlayers() ) {
			
			for ( String key : keys ) {
				PlayerCachePlayerData playerData = getPlayers().get( key );
				
				sb.append( playerData.toString() );
			}
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

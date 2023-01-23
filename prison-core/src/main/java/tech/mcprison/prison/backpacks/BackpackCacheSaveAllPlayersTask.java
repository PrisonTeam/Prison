package tech.mcprison.prison.backpacks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

public class BackpackCacheSaveAllPlayersTask 
	extends BackpackCacheRunnable
{
	public static long LAST_SEEN_INTERVAL_30_MINUTES = 30 * 60 * 1000;

	@Override
	public void run()
	{
	
		BackpackCache bCache = BackpackCache.getInstance();
		
		
		List<Player> onlinePlayersList = Prison.get().getPlatform().getOnlinePlayers();
		Set<String> onlinePlayers = new HashSet<>();
		for ( Player player : onlinePlayersList ) {
			onlinePlayers.add( player.getUUID().toString() );
		}
		
		
		List<BackpackCachePlayerData> purge = new ArrayList<>();
		
		Set<String> keys = bCache.getPlayers().keySet();
		
		for ( String key : keys )
		{
			BackpackCachePlayerData playerData = null;

			synchronized ( bCache.getPlayers() ) {
				
				playerData = bCache.getPlayers().get( key );
			}
			
			if ( playerData != null ) {
				
				// If a cached item is found with the player being offline, then 
				// purge them from the cache.  They were usually added only because
				// some process had to inspect their stats, so they are safe to remove.
				String uuid = playerData.getPlayerUuid();
				if ( uuid != null && !onlinePlayers.contains( uuid ) ) {
					purge.add( playerData );
				}
				
				if ( playerData.isDirty() ) {
					
					try
					{
						playerData.setDirty( false );
						bCache.getCacheFiles().toJsonFile( playerData );
					}
					catch ( Exception e )
					{
						String message = String.format( 
								"PlayerCache: Error trying to save a player's " +
										"cache data. Will try again later. " +
										"%s", e.getMessage() );
						Output.get().logError( message, e );
					}
				}
			}
			
		}
		
		synchronized ( bCache.getPlayers() ) {
			
			for ( BackpackCachePlayerData playerData : purge ) {
				try {
					if ( !playerData.isDirty() ) {
						
						bCache.getPlayers().remove( playerData.getPlayerUuid() );
					}
				}
				catch ( Exception e ) {
					// Ignore any possible errors. They will be addressed on the next
					// run of this task.
				}
			}
		}
		
	}
}

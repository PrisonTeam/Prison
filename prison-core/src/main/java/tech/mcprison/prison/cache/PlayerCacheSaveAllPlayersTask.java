package tech.mcprison.prison.cache;

/**
 * This class provide periodical saving of all player data.  
 * This ensures that it's at least stored every once in a while.
 * 
 * About every 5 to 10 minutes would be good.  After the server has
 * been running for a while and it appears to be stable, then you may
 * increase the time between saves.
 * 
 * This class only saves the data. It does not recalculate anything.
 * 
 * @author RoyalBlueRanger
 *
 */
public class PlayerCacheSaveAllPlayersTask
		extends PlayerCacheRunnable
{

	@Override
	public void run()
	{
	
		PlayerCache pCache = PlayerCache.getInstance();
		
		for ( String key : pCache.getPlayers().keySet() )
		{
			PlayerCachePlayerData playerData = pCache.getPlayers().get( key );
			
			pCache.getCacheFiles().toJsonFile( playerData );
		}
		
	}

}

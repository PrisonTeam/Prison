package tech.mcprison.prison.cache;

public class PlayerCacheCheckTimersTask
	extends PlayerCacheRunnable
{

	@Override
	public void run()
	{
	
		PlayerCache pCache = PlayerCache.getInstance();
		
		for ( String key : pCache.getPlayers().keySet() )
		{
			PlayerCachePlayerData playerData = pCache.getPlayers().get( key );
			
			playerData.checkTimers();
		}
	
	}

}

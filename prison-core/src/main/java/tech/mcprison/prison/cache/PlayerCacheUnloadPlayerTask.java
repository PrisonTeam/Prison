package tech.mcprison.prison.cache;

public class PlayerCacheUnloadPlayerTask
		extends PlayerCacheTask
{

	public PlayerCacheUnloadPlayerTask( PlayerCachePlayerData playerData ) {
		super( playerData );
	
	}

	public void run() {
	
		PlayerCache pCache = PlayerCache.getInstance();
		
		// Remove from the player cache:
		pCache.removePlayerData( getPlayerData() );
		
		pCache.getCacheFiles().toJsonFile( getPlayerData() );
		
	}

}

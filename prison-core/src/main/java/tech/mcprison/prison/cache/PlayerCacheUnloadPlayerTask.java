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
		PlayerCachePlayerData removed = null;

		synchronized ( pCache.getPlayers() ) {
			
			removed = pCache.removePlayerData( getPlayerData() );
		}
		
		if ( removed != null ) {
			
			pCache.getCacheFiles().toJsonFile( removed );
		}
		
	}

}

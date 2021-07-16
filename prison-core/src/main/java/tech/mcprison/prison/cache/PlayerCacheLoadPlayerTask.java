package tech.mcprison.prison.cache;

import tech.mcprison.prison.internal.Player;

public class PlayerCacheLoadPlayerTask
	extends PlayerCacheTask
{

	private Player player;
	
	public PlayerCacheLoadPlayerTask( Player player ) {
		super( null );
	
	}

	public void run() {
	
		PlayerCache pCache = PlayerCache.getInstance();
		
		PlayerCachePlayerData playerData = pCache.getCacheFiles().fromJson( player );
		
		pCache.addPlayerData( playerData );
		
	}

}

package tech.mcprison.prison.cache;

import tech.mcprison.prison.internal.Player;

public class PlayerCacheLoadPlayerTask
	extends PlayerCacheTask
{

	private Player player;
	
	public PlayerCacheLoadPlayerTask( Player player ) {
		super( null );
	
		this.player = player;
	}

	public void run() {
	
		PlayerCache pCache = PlayerCache.getInstance();
		
		PlayerCachePlayerData playerData = pCache.getCacheFiles().fromJson( player );
		
		if ( playerData != null ) {
			
			pCache.addPlayerData( playerData );
		}
		
	}

}

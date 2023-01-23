package tech.mcprison.prison.backpacks;

import tech.mcprison.prison.internal.Player;

public class BackpackCacheLoadPlayerTask
	extends BackpackCacheTask
{

	private Player player;
	
	public BackpackCacheLoadPlayerTask( Player player ) {
		super( null );
	
		this.player = player;
	}

	public void run() {
	
		BackpackCache bCache = BackpackCache.getInstance();
		
		BackpackCachePlayerData playerData = bCache.getCacheFiles().fromJson( player );
		
		if ( playerData != null ) {
			
			synchronized ( bCache.getPlayers() ) {
				
			}
		}
		
	}

}

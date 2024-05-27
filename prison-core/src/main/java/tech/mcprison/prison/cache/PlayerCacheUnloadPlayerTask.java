package tech.mcprison.prison.cache;

import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.ranks.data.RankPlayer;

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
			
			UUID uuid = UUID.fromString( removed.getPlayerUuid() );
			
			RankPlayer rPlayer = Prison.get().getPlatform()
								.getRankPlayer( uuid, 
								removed != null ? removed.getPlayerName() : "" );
			rPlayer.updateTotalLastValues( removed );
			
			pCache.getCacheFiles().toJsonFile( removed );
		}
		
	}

}

package tech.mcprison.prison.backpacks;

public class BackpackCacheUnloadPlayerTask
	extends BackpackCacheTask
{

	public BackpackCacheUnloadPlayerTask( BackpackCachePlayerData backpackData ) {
		super( backpackData );
	
	}
	
	public void run() {
	
		BackpackCache bCache = BackpackCache.getInstance();
		
		// Remove from the player cache:
		BackpackCachePlayerData removed = null;
	
		synchronized ( bCache.getPlayers() ) {
			
			removed = bCache.removePlayerData( getBackpackData() );
		}
		
		if ( removed != null ) {
			
			bCache.getCacheFiles().toJsonFile( removed );
		}
		
	}
}

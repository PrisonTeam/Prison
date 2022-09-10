package tech.mcprison.prison.backpacks;

import tech.mcprison.prison.cache.CoreCacheData;
import tech.mcprison.prison.cache.CoreCacheFiles;
import tech.mcprison.prison.internal.Player;

public class BackpackCacheFiles
	extends CoreCacheFiles
{
	public static final String FILE_BACKPACK_CACHE_PATH = "data_storage/backpackCache";
	
	
	public BackpackCacheFiles() {
		super( FILE_BACKPACK_CACHE_PATH );
		
	}

	public BackpackCachePlayerData fromJson( Player player ) {
		
		CoreCacheData results = fromJson( player, BackpackCachePlayerData.class );
		
		return (BackpackCachePlayerData) results;
	}
	
}

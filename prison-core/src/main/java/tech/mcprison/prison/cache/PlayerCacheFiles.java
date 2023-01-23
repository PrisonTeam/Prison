package tech.mcprison.prison.cache;

import tech.mcprison.prison.internal.Player;

/**
 * <p>This class is intended to be kept online and active for the full
 * life of the server.  It is intended to be used for the source point 
 * for accessing the PlayerCache files and directory.  Plus this class
 * is intended to manage the ORM (Java Objects to json files).
 * </p>
 * 
 * <p>It should be noted that the loading and saving of the Player's 
 * data should always be done asynchronously.  This class makes no 
 * attempt to control that aspect, but it is intended that these functions
 * will be used through an asyc thread.
 * </p>
 * 
 * @author RoyalBlueRanger
 *
 */
public class PlayerCacheFiles
	extends CoreCacheFiles
{
	public static final String FILE_PLAYER_CACHE_PATH = "data_storage/playerCache";
	
	
	public PlayerCacheFiles() {
		super( FILE_PLAYER_CACHE_PATH );
		
	}

	public PlayerCachePlayerData fromJson( Player player ) {
		
		CoreCacheData results = fromJson( player, PlayerCachePlayerData.class );
		
		return (PlayerCachePlayerData) results;
	}
	
}

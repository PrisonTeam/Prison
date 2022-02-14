package tech.mcprison.prison.cache;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

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
{
	public static final String FILE_SUFFIX_JSON = ".json";
	public static final String FILE_PREFIX_BACKUP = ".backup_";
	public static final String FILE_SUFFIX_BACKUP = ".bu";
	public static final String FILE_SUFFIX_TEMP = ".temp";
	public static final String FILE_TIMESTAMP_FORMAT = "_yyyy-MM-dd_HH-mm-ss";
	public static final String FILE_PLAYERCACHE_PATH = "data_storage/playerCache";
	
	
	private Gson gson = null;
	
	private File playerCacheDirectory = null;
	
	
	private TreeMap<String, File> playerFiles;
	
	
	public PlayerCacheFiles() {
		super();
		
	}
	
	/**
	 * <p>This constructs the Gson engine using the optional pretty 
	 * printing to help make it human readable.
	 * </p>
	 * 
	 * @return
	 */
	private Gson getGson() {
		if ( gson == null ) {
			gson = new GsonBuilder().setPrettyPrinting().create();
		}
		return gson;
	}
	
	/**
	 * <p>For generating a json String object from the player's data.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public String toJson( PlayerCachePlayerData player ) {
		return getGson().toJson( player );
	}
	
	/**
	 * <p>This function serializes the provided Player's object to the 
	 * json file, using the player data object as the source for the
	 * json data, and the File object that is stored in that object too.
	 * </p>
	 * 
	 * <p>If the player changes their name while they are online, and if that change
	 * actually percolates through to bukkit, the caching system will never attempt
	 * to change the Player's cached file while it's active.  It will rename the 
	 * file upon loading in to the cache the next time they are activated.
	 * </p>
	 * 
	 * <p>This function first saves the new player data to a temp file.  If that 
	 * was successful, then it deletes the original file, and renames the temp
	 * file back to the original name.
	 * </p>
	 * 
	 * @param player
	 */
	public void toJsonFile( PlayerCachePlayerData player) {
		
		if ( player != null ) {
			
			File playerFile = player.getPlayerFile();
			File outTemp = createTempFile( playerFile );
			
			if ( !getPlayerFiles().containsKey( playerFile.getName() )) {
				getPlayerFiles().put( playerFile.getName(), playerFile );
			}
			
			boolean success = false;
			
			try (
					FileWriter fw = new FileWriter( outTemp );
					){
				getGson().toJson( player, fw );
				
				success = true;
			}
			catch ( JsonIOException | IOException e ) {
				e.printStackTrace();
			}
			
			// If there is a significant change in file size, or the new file is smaller than the
			// old, then rename it to a backup and keep it.  If it is smaller, then something went wrong
			// because player cache data should always increase, with the only exception being 
			// the player cache.
			if ( playerFile.exists() ) {
				long pfSize = playerFile.length();
				long tmpSize = outTemp.length();
				
				if ( tmpSize < pfSize ) {
					 
					renamePlayerFileToBU( playerFile );
				}
			}
			
			if ( success && ( !playerFile.exists() || playerFile.delete()) ) {
				outTemp.renameTo( playerFile );
			}
			else {
				
				boolean removed = false;
				if ( outTemp.exists() ) {
					removed = outTemp.delete();
				}
				
				String message = String.format( 
						"Unable to rename PlayerCache temp file. It was %sremoved: %s", 
						(removed ? "" : "not "), outTemp.getAbsolutePath() );
				
				Output.get().logWarn( message );
			}
		}
	}
	
	private void renamePlayerFileToBU( File playerFile )
	{
		String buFileName = FILE_PREFIX_BACKUP + 
					playerFile.getName().replace( FILE_SUFFIX_TEMP, FILE_SUFFIX_BACKUP );
		
		File backupFile = new File( playerFile.getParent(), buFileName );
		
		playerFile.renameTo( backupFile );
		
	}

	private File createTempFile( File file ) {
	    SimpleDateFormat sdf = new SimpleDateFormat( FILE_TIMESTAMP_FORMAT );
	    String name = file.getName() + sdf.format( new Date() ) + FILE_SUFFIX_TEMP;
	    
	    return new File( file.getParentFile(), name);
	}
	
	/**
	 * <p>This loads the PlayerCachePlayerData object from a saved json file.
	 * </p>
	 * 
	 * <p>Since this deals with the actual file name, the "knowledge" on how to 
	 * properly generate that file name is contained within this class and should
	 * never be exposed since it may not be correct and can lead to errors.
	 * </p>
	 * 
	 */
	private PlayerCachePlayerData fromJsonFile( File inputFile ) {
		PlayerCachePlayerData results = null;
		try (
			FileReader fr = new FileReader( inputFile );
				) {
			
			results = getGson().fromJson( 
					fr, PlayerCachePlayerData.class );
			
			results.setPlayerFile( inputFile );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		catch ( JsonSyntaxException | JsonIOException e )
		{
			e.printStackTrace();
		}
		
		return results;
	}
	
	/**
	 * <p>This function returns the file name which is constructed by 
	 * using the player's UUID and their name.  The player's name is not
	 * used in the selection of a player's file, only the UUID prefix.
	 * </p>
	 * 
	 * <p>The UUID prefix is based upon the HEX representation of the 
	 * the UUID, and includes the first 13 characters which includes one
	 * hyphen. Since the minecraft UUID is based upon random numbers
	 * (type 4 UUID), then odds are great that file name prefixes will
	 * be unique, but they don't have to be.
	 * </p>
	 * 
	 * <p>Its a high importance that file names can be found based upon
	 * Player information, hence the UUID prefix.  Plus it's very important
	 * to be able to have the files human readable so admins can find 
	 * specific player files if they need to; hence the player name suffix.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	private String getPlayerFileName( Player player ) {
		String UUIDString = player.getUUID().toString();
		String uuidFragment = getFileNamePrefix( UUIDString );
		
		return uuidFragment + "_" + player.getName() + FILE_SUFFIX_JSON;
	}
	
	/**
	 * <p>This function returns the first 13 characters of the supplied
	 * file name, or UUID String. The hyphen is around the 12 or 13th position, 
	 * so it may or may not include it.
	 * </p>
	 * 
	 * @param playerFileName
	 * @return
	 */
	private String getFileNamePrefix( String UUIDString ) {
		return UUIDString.substring( 0, 14 );
	}
	
	
	/**
	 * <p>This function will take the project's data folder and construct the the path
	 * to the directory, if it does not exist, to where the player cache files are stored.
	 * </p>
	 * 
	 * @return
	 */
	public File getPlayerFilePath() {
		if ( playerCacheDirectory == null ) {
			
			playerCacheDirectory = new File( Prison.get().getDataFolder(), FILE_PLAYERCACHE_PATH ); 
			playerCacheDirectory.mkdirs();
			
		}
		return playerCacheDirectory;
	}
	
	/**
	 * <p>Constructs a File object for a specific player.
	 * </p>
	 * 
	 * @param playerFileName
	 * @return
	 */
	private File getPlayerFile( String playerFileName ) {
		return new File( getPlayerFilePath(), playerFileName );
	}
	
	
	/**
	 * <p>This function will take a Prison Player object, and load the correct PlayerCache
	 * file.  The file name has a prefix of the first part of the player's UUID and then 
	 * followed by the player's name.  But it is important to understand that the Player's
	 * name part is totally ignored since player could change their name at any time.
	 * </p>
	 * 
	 * <p>By using the Player's UUID and player name, it constructs the file's full name.
	 * Then it takes the prefix, of the first 14 characters of the UUID, and gets all of 
	 * the File objects within the cache directory.  Since minecraft UUIDs are generated
	 * from random numbers, the first 14 characters "should" be unique.  If the returned 
	 * Files are not unique, then each returned File object is loaded so the the correct
	 * file can be selected by the internal UUID. Odds are there will never be duplicates,
	 * but if there is, it will handle it.    
	 * </p>
	 * 
	 * <p>If the supplied Player is not in the cache, a new PlayerCachePlayerData object
	 * is created for the player (keyed on the player's UUID and name) and then it is 
	 * saved to the cache directory.
	 * </p>
	 * 
	 * <p>Although the Player's file name prefix will always match it's data file, their 
	 * name portion of the file can be dynamic. It is important that if the Player changes
	 * their name, that their save cached file will be change within this function.  
	 * This process will only happen the first time the player is loaded from the cache 
	 * (which can vary, such as at server startup or they logoff and they are cleared from
	 * the cache, then added back when they login the next time).  So this function will
	 * rename the cache file to reflect the player's current name.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public PlayerCachePlayerData fromJson( Player player ) {
		PlayerCachePlayerData results = null;
		
//		// This is the "target" file name for the player, based upon their
//		// current name. The saved cache file may not be named exactly the same,
//		// and if it's not, then their existing cache file will be renamed 
//		// within the function getCachedFileMatch()
		String playerFileName = getPlayerFileName( player );
		
		File playerFile = getCachedFileMatch( playerFileName );

		
		if ( playerFile.exists() ) {
			
			results = fromJsonFile( playerFile );
		}
		

		// New player and file does not exist so create it.
		if ( results == null ) {
			results = new PlayerCachePlayerData( player, playerFile );
			
			// Then save it:
			toJsonFile( results );
		}
		
		return results;
	}



	private TreeMap<String, File> getPlayerFiles() {
		// load the player's files:
		if ( playerFiles == null ) {
			
			playerFiles = new TreeMap<>();

			FileFilter fileFilter = (file) -> {
			
				String fname = file.getName();
				boolean isTemp = fname.startsWith( FILE_PREFIX_BACKUP ) ||
								 fname.endsWith( FILE_SUFFIX_BACKUP ) ||
								 fname.endsWith( FILE_SUFFIX_TEMP );
				
				return !file.isDirectory() && !isTemp &&
							fname.endsWith( FILE_SUFFIX_JSON );
			};
			
			
			File[] files = getPlayerFilePath().listFiles( fileFilter );
			for ( File f : files )
			{
				String fileNamePrefix = getFileNamePrefix( f.getName() );
				getPlayerFiles().put( fileNamePrefix, f );
			}
			
		}
		
		return playerFiles;
	}

	/**
	 * <p>Potentially there could be more than one result, but considering if a player
	 * changes their name, then it should only return only one entry.  The reason for
	 * this, is that the file name should be based upon the player's UUID, which is the
	 * first 13 characters of the file, and the name itself, which follows, should
	 * never be part of the "key".
	 * </p>
	 * 
	 * @param playerFile
	 * @param playerFileName
	 * @return
	 */
	private File getCachedFileMatch( String playerFileName )
	{
		File results = null;
		
		String fileNamePrefix = getFileNamePrefix( playerFileName );
		
		results = getPlayerFiles().get( fileNamePrefix );
		
		if ( results == null ) {
			
			// This is the "target" file name for the player, based upon their
			// current name. The saved cache file may not be named exactly the same,
			// and if it's not, then their existing cache file needs to be 
			// renamed.
			results = getPlayerFile( playerFileName );
			
			// NOTE: because the file was NOT found in the directory, then we can assume
			//       this is a new player therefore we won't have an issue with the player's
			//       name changing.
			getPlayerFiles().put( fileNamePrefix, results );
		}
		else if ( !playerFileName.equalsIgnoreCase( results.getName() )) {
			
			// File name changed!!! Need to rename the file in the file system, and 
			// update what is in the playerFiles map!!
			
			File newFile = getPlayerFile( playerFileName );

			if ( results.exists() )  {
				// rename what's on the file system:
				results.renameTo( newFile );
			}

			// Replace what's in the map:
			getPlayerFiles().put( fileNamePrefix, newFile );
			
			results = newFile;
		}
		
//		NavigableMap<String, File> files = getPlayerFiles().tailMap( fileNamePrefix, true );
//		Set<String> keys = files.keySet();
//		for ( String key : keys ) {
//			if ( !key.startsWith( fileNamePrefix ) ) {
//				break;
//			}
//			
//			results.add( files.get( key ) );
//		}
		
		return results;
	}
	
}

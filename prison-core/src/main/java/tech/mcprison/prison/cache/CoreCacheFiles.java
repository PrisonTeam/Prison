package tech.mcprison.prison.cache;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

public abstract class CoreCacheFiles {

	public static final String FILE_SUFFIX_JSON = ".json";
	public static final String FILE_PREFIX_BACKUP = ".backup_";
	public static final String FILE_SUFFIX_BACKUP = ".bu";
	public static final String FILE_SUFFIX_TEMP = ".temp";
	public static final String FILE_SUFFIX_TXT = ".txt";
	public static final String FILE_TIMESTAMP_FORMAT = "_yyyy-MM-dd_HH-mm-ss";

	private final String cachePath;
	private File cacheDirectory = null;
	
	private Gson gson = null;
//	private TreeMap<String, File> playerFiles;

	
	public CoreCacheFiles( String cachePath ) {
		super();
		
		this.cachePath = cachePath;
	}
	
	public String getCachePath() {
		return cachePath;
	}

	/**
	 * <p>This constructs the Gson engine using the optional pretty 
	 * printing to help make it human readable.
	 * </p>
	 * 
	 * @return
	 */
	protected Gson getGson() {
		if ( gson == null ) {
			gson = new GsonBuilder().setPrettyPrinting().create();
		}
		return gson;
	}

	protected void renamePlayerFileToBU(File playerFile) {
		String buFileName = FILE_PREFIX_BACKUP + 
					playerFile.getName().replace( FILE_SUFFIX_TEMP, FILE_SUFFIX_BACKUP );
		
		File backupFile = new File( playerFile.getParent(), buFileName );
		
		playerFile.renameTo( backupFile );
		
	}

	protected File createTempFile(File file) {
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
	protected CoreCacheData fromJsonFile(File inputFile, Class<? extends CoreCacheData> classOfT ) {
		CoreCacheData results = null;
		
		if ( inputFile.exists() ) {
			
			try (
					FileReader fr = new FileReader( inputFile );
					) {
				
				results = (CoreCacheData) getGson().fromJson( fr, classOfT );
				
				if ( results != null ) {
					
					results.setPlayerFile( inputFile );
				}
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			catch ( JsonSyntaxException | JsonIOException e )
			{
				e.printStackTrace();
			}
		}
		
		return results;
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
	public CoreCacheData fromJson( Player player, Class<? extends CoreCacheData> classOfT ) {
		CoreCacheData results = null;
		
		File playerCacheFile = JsonFileIO.fileCache(player);
		
//		String cacheFilename = JsonFileIO.filenameCache(player);
////		String playerId = player.getUUID().toString();
//		
//		File playerCacheFile = getPlayerFile( cacheFilename );
		
//		// This is the "target" file name for the player, based upon their
//		// current name. The saved cache file may not be named exactly the same,
//		// and if it's not, then their existing cache file will be renamed 
//		// within the function getCachedFileMatch()
//		String playerFileName = getPlayerFileName( player );
		
//		String uuidPrefix = JsonFileIO.getFileNameUUIDFragment(playerFileName)
		
//		File playerFile = getCachedFileMatch( playerId, cacheFilename );

		
		if ( playerCacheFile.exists() ) {
			
			results = fromJsonFile( playerCacheFile, classOfT );
		}
		

		// New player and file does not exist so create it.
		if ( results == null ) {
			results = new PlayerCachePlayerData( player, playerCacheFile );
			
			// Then save it:
			toJsonFile( results );
		}
		
		return results;
	}

	
	/**
	 * <p>For generating a json String object from a cache data object type.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public String toJson(CoreCacheData cacheData) {
		return getGson().toJson( cacheData );
	}

	/**
	 * <p>This function serializes the provided core cache data object to the 
	 * json file, using the core cache data object as the source for the
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
	public void toJsonFile(CoreCacheData cacheData) {
		
		if ( cacheData != null ) {
			
			File playerFile = cacheData.getPlayerFile();
			File outTemp = createTempFile( playerFile );
			
//			if ( !getPlayerFiles().containsKey( playerFile.getName() )) {
//				getPlayerFiles().put( playerFile.getName(), playerFile );
//			}
			
			if ( outTemp.getParentFile().mkdirs() ) {
				Output.get().logInfo( "CoreCacheFiles.toJsonFile(): Created missing directories: %s",
						 outTemp.getParentFile().getAbsolutePath() );
			}
			
			boolean success = false;
			
			try (
					FileWriter fw = new FileWriter( outTemp );
					){
				getGson().toJson( cacheData, fw );
				
				success = true;
			}
			catch ( JsonIOException | IOException e ) {

				String msg = String.format(
						"&3CoreCacheFiles.toJsonFile: &6Failure to write to temp file. &3This is probably an " +
						"issue with the underlying OS and file system. Did you run out of file storage space? " +
						"Please confirm. Tempfile: &6%s&3  OriginalFile: %s  Error message: [&6%s&3]",
						outTemp.getAbsolutePath(),
						playerFile.getAbsolutePath(),
						e.getMessage()
						);
						
				Output.get().logInfo( msg );
			}
			
			if ( success ) {
				
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
	}

//	/**
//	 * <p>Constructs a File object for a specific player.
//	 * </p>
//	 * 
//	 * @param playerFileName
//	 * @return
//	 */
//	protected File getPlayerFile(String playerFileName) {
//		return new File( getPlayerFilePath(), playerFileName );
//	}

//	protected TreeMap<String, File> getPlayerFiles() {
//		// load the player's files:
//			
//		TreeMap<String, File> playerFiles = new TreeMap<>();
//
//		FileFilter fileFilter = (file) -> {
//		
//			String fname = file.getName();
//			boolean isTemp = fname.startsWith( FILE_PREFIX_BACKUP ) ||
//							 fname.endsWith( FILE_SUFFIX_BACKUP ) ||
//							 fname.endsWith( FILE_SUFFIX_TEMP ) ||
//							 fname.endsWith( FILE_SUFFIX_TXT );
//			
//			return !file.isDirectory() && !isTemp &&
//						fname.endsWith( FILE_SUFFIX_JSON );
//		};
//		
//		
//		File[] files = getPlayerFilePath().listFiles( fileFilter );
//		for ( File f : files )
//		{
//			String fName = f.getName();
//				
//			String fileNamePrefix = JsonFileIO.getFileNameUUIDFragment( fName );
//			
//			if ( fileNamePrefix != null && fileNamePrefix.length() > 0 ) {
//				
//				playerFiles.put( fileNamePrefix, f );
//			}
//		}
//		
//	
//		return playerFiles;
//	}

	
	
	/**
	 * <p>This function will take the project's data folder and construct the the path
	 * to the directory, if it does not exist, to where the player cache files are stored.
	 * </p>
	 * 
	 * @return
	 */
	public File getPlayerFilePath() {
		if ( cacheDirectory == null ) {
			
			cacheDirectory = new File( Prison.get().getDataFolder(), getCachePath() ); 
			cacheDirectory.mkdirs();
			
		}
		return cacheDirectory;
	}
	
//	/**
//	 * <p>This function returns the file name which is constructed by 
//	 * using the player's UUID and their name.  The player's name is not
//	 * used in the selection of a player's file, only the UUID prefix.
//	 * </p>
//	 * 
//	 * <p>The UUID prefix is based upon the HEX representation of the 
//	 * the UUID, and includes the first 13 characters which includes one
//	 * hyphen. Since the minecraft UUID is based upon random numbers
//	 * (type 4 UUID), then odds are great that file name prefixes will
//	 * be unique, but they don't have to be.
//	 * </p>
//	 * 
//	 * <p>Its a high importance that file names can be found based upon
//	 * Player information, hence the UUID prefix.  Plus it's very important
//	 * to be able to have the files human readable so admins can find 
//	 * specific player files if they need to; hence the player name suffix.
//	 * </p>
//	 * 
//	 * @param player
//	 * @return
//	 */
//	private String getPlayerFileName( Player player ) {
//		
//		return JsonFileIO.filenameCache( player );
//		
////		String UUIDString = player.getUUID().toString();
////		String uuidFragment = getFileNamePrefix( UUIDString );
////		
////		return uuidFragment + "_" + player.getName() + FILE_SUFFIX_JSON;
//	}
	
//	/**
//	 * <p>This function returns the first 13 characters of the supplied
//	 * file name, or UUID String. The hyphen is around the 12 or 13th position, 
//	 * so it may or may not include it.
//	 * </p>
//	 * 
//	 * @param playerFileName
//	 * @return
//	 */
//	String getFileNamePrefix( String UUIDString ) {
//		return UUIDString.substring( 0, 14 );
//	}
	


//	/**
//	 * <p>This takes a playerId (their UUID) and tries to get their file name from the
//	 * playerFile collection.  If it does not exist, a new file is created for their
//	 * user.  If the saved file name does not match the playerFilename, then it indicates
//	 * there could have been a player name change, so update the saved file on the file
//	 * system and update what's stored in the playerFiles.
//	 * </p>
//	 * 
//	 * 
//	 * @param playerFile
//	 * @param playerFileName
//	 * @return
//	 */
//	private File getCachedFileMatch( String playerId, String playerFileName )
//	{
//		File results = null;
//		
//		if ( getPlayerFiles().containsKey( playerId ) ) {
//			
//			results = getPlayerFiles().get( playerId );
//		}
//		else {
//			// This is the "target" file name for the player, based upon their
//			// current name. The saved cache file may not be named exactly the same,
//			// and if it's not, then their existing cache file needs to be 
//			// renamed.
//			results = getPlayerFile( playerFileName );
//			
//			// NOTE: because the file was NOT found in the directory, then we can assume
//			//       this is a new player therefore we won't have an issue with the player's
//			//       name changing.
//			getPlayerFiles().put( playerId, results );
//		}
//		
//		
//		if ( results != null && !results.getName().equalsIgnoreCase(playerFileName) ) {
//			// They player's name changed.  Update the name of the file and save it:
//			
//			// File name changed!!! Need to rename the file in the file system, and 
//			// update what is in the playerFiles map!!
//			
//			File newFile = getPlayerFile( playerFileName );
//
//			if ( results.exists() )  {
//				// rename what's on the file system:
//				results.renameTo( newFile );
//			}
//
//			// Replace what's in the map:
//			getPlayerFiles().put( playerId, newFile );
//			
//			results = newFile;
//		}
//		
		
//		String fileNamePrefix = JsonFileIO.getFileNameUUIDFragment( playerFileName );
////		String fileNamePrefix = getFileNamePrefix( playerFileName );
//		
//		if ( fileNamePrefix != null && fileNamePrefix.length() > 0 ) {
//			results = getPlayerFiles().get( fileNamePrefix );
//		}
//		
//		if ( results == null ) {
//			
//			// This is the "target" file name for the player, based upon their
//			// current name. The saved cache file may not be named exactly the same,
//			// and if it's not, then their existing cache file needs to be 
//			// renamed.
//			results = getPlayerFile( playerFileName );
//			
//			// NOTE: because the file was NOT found in the directory, then we can assume
//			//       this is a new player therefore we won't have an issue with the player's
//			//       name changing.
//			getPlayerFiles().put( fileNamePrefix, results );
//		}
//		else if ( !playerFileName.equalsIgnoreCase( results.getName() )) {
//			
//			// File name changed!!! Need to rename the file in the file system, and 
//			// update what is in the playerFiles map!!
//			
//			File newFile = getPlayerFile( playerFileName );
//
//			if ( results.exists() )  {
//				// rename what's on the file system:
//				results.renameTo( newFile );
//			}
//
//			// Replace what's in the map:
//			getPlayerFiles().put( fileNamePrefix, newFile );
//			
//			results = newFile;
//		}
		
//		NavigableMap<String, File> files = getPlayerFiles().tailMap( fileNamePrefix, true );
//		Set<String> keys = files.keySet();
//		for ( String key : keys ) {
//			if ( !key.startsWith( fileNamePrefix ) ) {
//				break;
//			}
//			
//			results.add( files.get( key ) );
//		}
		
//		return results;
//	}
}

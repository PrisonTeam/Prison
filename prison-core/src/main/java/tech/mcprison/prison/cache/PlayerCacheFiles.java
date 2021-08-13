package tech.mcprison.prison.cache;

import java.io.File;
import java.io.FileFilter;
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
	private Gson gson = null;
	
	private File playerCacheDirectory = null;
	
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
		
		File playerFile = player.getPlayerFile();
		File outTemp = createTempFile( playerFile );
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
	
	private File createTempFile( File file ) {
	    SimpleDateFormat sdf = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
	    String name = file.getName() + sdf.format( new Date() ) + ".temp";
	    
	    return new File( file.getParentFile(), name);
	}
	
	/**
	 * <p>This loads the PlayerCachePlayerData object from a saved json file.
	 * </p>
	 * 
	 */
	public PlayerCachePlayerData fromJsonFile( File inputFile ) {
		PlayerCachePlayerData results = null;
		try (
			FileReader fr = new FileReader( inputFile );
				) {
			
			results = getGson().fromJson( 
					fr, PlayerCachePlayerData.class );
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
		String uuidHex = player.getUUID().toString();
		String uuidFragment = getFileNamePrefix( uuidHex );
		
		return uuidFragment + "_" + player.getName() + ".json";
	}
	
	/**
	 * <p>This function returns the first 13 characters of the supplied
	 * file name, or UUID String. 13 characters only because the 14th character
	 * is a hyphen. 
	 * </p>
	 * 
	 * @param playerFileName
	 * @return
	 */
	private String getFileNamePrefix( String fileName ) {
		return fileName.substring( 0, 14 );
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
			
			playerCacheDirectory = new File( Prison.get().getDataFolder(), "data_storage/playerCache" ); 
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
		
		String playerFileName = getPlayerFileName( player );
		String fileNamePrefix = getFileNamePrefix( playerFileName );
		
		// This is the "target" file name for the player, based upon their
		// current name. The saved cache file may not be named exactly the same,
		// and if it's not, then their existing cache file needs to be 
		// renamed.
		File playerFile = getPlayerFile( playerFileName );
		
		
		FileFilter fileFilter = file -> !file.isDirectory() && 
				file.getName().startsWith(fileNamePrefix) &&
				file.getName().endsWith(".json");
		
		File[] files = playerFile.getParentFile().listFiles( fileFilter );
		
		if ( files != null && files.length > 0 ) {
			
			for ( File file : files )
			{
				PlayerCachePlayerData temp = fromJsonFile( file );
				if ( temp != null && temp.getPlayerUuid().equalsIgnoreCase( 
								player.getUUID().toString() ) ) {
					
					results = temp;
					results.setPlayerFile( file );
					break;
				}
			}
		}
		
		// New player and file does not exist so create it.
		if ( results == null ) {
			results = new PlayerCachePlayerData( player, playerFile );
			
			// Then save it:
			toJsonFile( results );
		}
		
		// Check to see if the player's name has changed, which means the generated 
		// file name does not match.  If that is the situation, then need to rename
		// the file to match the current player's name.
		if ( results != null ) {
			if ( results.getPlayerFile().equals( playerFile ) ) {
				
				results.getPlayerFile().renameTo( playerFile );
				results.setPlayerFile( playerFile );
			}
		}
		
		return results;
	}
	
}

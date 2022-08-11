package tech.mcprison.prison.file;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tech.mcprison.prison.error.ErrorManager;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.modules.ModuleStatus;
import tech.mcprison.prison.output.Output;

public class JsonFileIO
		extends FileIO
{
	public static final String FILE_SUFFIX_JSON = ".json";
	public static final String FILE_PREFIX_BACKUP = ".backup_";
	public static final String FILE_SUFFIX_BACKUP = ".bu";
	public static final String FILE_SUFFIX_TEMP = ".temp";
	public static final String FILE_TIMESTAMP_FORMAT = "_yyyy-MM-dd_HH-mm-ss";

	private final Gson gson;
	
	/**
	 * 
	 * @param errorManager Optional; set to null if used outside of a module.
	 * @param status Optional; set to null if used outside of a module.
	 */
	public JsonFileIO(ErrorManager errorManager, ModuleStatus status)
	{
		super(errorManager, status);
		
		this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	}

	public JsonFileIO() {
		this( null, null );
	}
	
	public Gson getGson()
	{
		return gson;
	}
	
    /**
     * <p>This constructs a player file named based upon the UUID followed 
     * by the player's name.  This format is used so it's easier to identify
     * the correct player.
     * </p>
     * 
     * <p>The format should be UUID-PlayerName.json.  The UUID is a shortened 
     * format, which should still produce a unique id.  The name, when read, 
     * is based upon the UUID and not the player's name, which may change.
     * This format includes the player's name to make it easier to identify
     * who's record is whom's.
     * </p>
     * 
     * @return
     */
    public static String getPlayerFileName( Player player ) {
    	
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
	private static String getFileNamePrefix( String UUIDString ) {
		return UUIDString.substring( 0, 14 );
	}
	
	/**
	 * This function will save a file as a JSON format.  It will first save it as a
	 * temp file to make sure the data can be written to the file system, then once
	 * it is successful, it will then delete the original target and rename the temp
	 * file.  This helps to ensure that data will not be lost if something goes wrong:
	 * either the original file will remain, or the new file will saved under the 
	 * *.tmp suffix, or both.  Should not reach a condition where both files disappear.
	 * 
	 * @param file
	 * @param data
	 */
	public void saveJsonFile( File file, FileIOData data ) 
	{
		if ( file != null && data != null )
		{
			String json = getGson().toJson( data );

			saveFile( file, json );	
		}
	}
	
	/**
	 * This function will try to load the given file, of which the contents should be 
	 * JSon.  If it is successful then the resulting object will represent the file.
	 * Otherwise the resulting object will be the data object that is passed to this
	 * function, which should be empty or however the new object should be structured.
	 * 
	 * @param file The source file that will be loaded. Should be JSON data within.
	 * @param data This is the "new" object that is to be returned from this function if the source
	 * 				file cannot be read successfully.
	 * @return
	 */
	public FileIOData readJsonFile( File file, FileIOData data )
	{
		FileIOData results = data;
		
		String json = super.readFile( file );
		
		if ( json != null )
		{
			try
			{
				results = getGson().fromJson( json, data.getClass() );
			}
			catch ( Exception e ) {
				
				String message = String.format( 
						"JsonFileIO.readJsonFile: JsonParse failure: file: [%s] " +
						"error: [%s]  json: [%s] ", 
						file.getAbsoluteFile(), e.getMessage(), 
						json );
				
				Output.get().logError( message );

				// e.printStackTrace();
			}
		}
		
		return results;
	}
}

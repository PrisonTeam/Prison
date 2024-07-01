package tech.mcprison.prison.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.backups.PrisonSystemSettings;
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
	public static final String FILE_SUFFIX_TXT = ".txt";
	public static final String FILE_TIMESTAMP_FORMAT = "_yyyy-MM-dd_HH-mm-ss";
	
	public static final String PLAYER_PATH = "data_storage/ranksDb/players/";
	public static final String CACHE_PATH = "data_storage/playerCache/";

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
	
	public Gson getGsonExposed()
	{
		return  new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.create();
	}
	
	/**
	 * <p>This function generate a partial user file name. This is based upon
	 * the UUID-fragment (first and last parts of the player UUID), 
	 * plus the player's name, and the file suffix, which is '.json'.
	 * This function does not add any prefix such as 'player_' or 
	 * 'cache_'.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
    private static String getPlayerFileNameNewVersion( Player player ) {
    	
		String uuidFragment = getFileNamePrefixNew( player );
		
		return uuidFragment + "_" + player.getName() + FILE_SUFFIX_JSON;
    }
	
    /**
     * <p>This generates the fragment UUID that is used within file names.
     * This is based upon the first 8 UUID digits, plus the hyphen.  Followed
     * by the last segment of the UUID, which starts at character 25.
     * </p>
     * 
     * <p>This uses the start and end of the UUID because bedrock players 
     * only have zeros for the first part of the UUID, so the ending must
     * be included too.
     * </p>
     * 
     * <p>Examples:
     * </p>
     * '22cacd8c-d0ff-4dd7-a8ba-2a6a8b46be92'
     * ''
     * 
     * @param player
     * @return
     */
	private static String getFileNamePrefixNew( Player player ) {
		String uuid = player.getUUID().toString();
		return uuid.substring( 0, 9 ) + 
				uuid.substring( 25 );
	}
	
	/**
	 * <p>This function extracts the UUID fragment from player file names.
	 * </p>
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileNameUUIDFragment( String filename ) {
		String uuid = null;
		
		if ( filename != null && filename.trim().length() > 0 ) {
			
			String uuidTmp = filename.replace("cache_", "").replace("player_", "");
			
			uuid = uuidTmp.indexOf("_") > 0 ?
						uuidTmp.substring(0, uuidTmp.indexOf("_")) 
						: uuidTmp;
		}
		return uuid;
		
//		String uuid = player.getUUID().toString();
//		return uuid.substring( 0, 9 ) + 
//				uuid.substring( 25 );
	}
	
//	/**
//     * <p>This is a helper function to ensure that the given file name is 
//     * always generated correctly and consistently.
//     * </p>
//     * 
//     * @return "player_" plus the least significant bits of the UID
//     */
//    public static String filenamePlayer( Player player )
//    {
//    	boolean useNewFormat = useFriendlyUserFileNames();
//    	
//    	return useNewFormat ? filenamePlayerNew( player ) : filenamePlayerOld( player );
//    }
    
//    public static String filenameCache( Player player )
//    {
//    	boolean useNewFormat = useFriendlyUserFileNames();
//    	
//    	return useNewFormat ? filenameCacheNew( player ) : filenameCacheOld( player );
//    }
    
    public String filenamePrefix( String filename, String prefixDeliminator ) {
    	int idx = filename.lastIndexOf( prefixDeliminator ) + 1;
    	String prefix = idx > 0 ? filename.substring(0, idx) : null;
    	return prefix;
    }
    
    /**
     * Using the path and file name, this will check to see if any files 
     * preexist with the given file prefix so if the player changes their 
     * name, it will still load the correct file.
     * 
     * If no file is found, then it will crate a new File object based 
     * upon the path and filename.
     * 
     * If existing file are found, there should only be at most one, but
     * if there is more than one, then use the first one.
     * 
     * @param path
     * @param filename
     * @return
     */
    public File checkFile( File path, String filename, String prefixDeliminator ) {
    	
    	String prefix = filenamePrefix( filename, prefixDeliminator );
    	
    	List<File> files = getFilesFromPrefix( prefix, path );
    	
    	return files.size() > 0 ? files.get(0) : new File( path, filename );
    }
    
    public static File filePlayer( Player player ) {
    	JsonFileIO jfIO = new JsonFileIO();
    	return jfIO.checkFiles( filenamePlayerNew( player ), filenamePlayerOld( player ), PLAYER_PATH );
    }
    
    public static File fileCache( Player player ) {
    	JsonFileIO jfIO = new JsonFileIO();
    	return jfIO.checkFiles( filenameCacheNew( player ), filenameCacheOld( player ), CACHE_PATH );
    }
    
    private File checkFiles( String newFileName, String oldFileName, String pathName )
    {
    	File results = null;
    	
    	File path = new File( Prison.get().getDataFolder(), pathName );
    	
    	File newPlayerFile = checkFile( path, newFileName, "_" );
    	
    	if ( newPlayerFile.exists() ) {
    		results = newPlayerFile;
    	}
    	else {
    		File oldPlayerFile = checkFile( path, oldFileName, "." );
    		
    		if ( oldPlayerFile.exists() ) {
    			results = oldPlayerFile;
    		}
    	}
    	
    	if ( results == null ) {
    		// Did not find a new format file, or an old file format, so use the new format:
    		results = newPlayerFile;
    	}
    	
    	// If the file chosen is not equal to the newFileName, then rename it:
    	else if ( !newFileName.equals(results.getName()) ) {
    		File newFile = new File( path, newFileName );
    		results.renameTo(newFile);
    		
    		// Rename does not change the original file path in results so have to reassign it:
    		results = newFile;
    	}

    	return results;
    }
    
    
    /**
     * <p>This function will check two things.  First will be the config settings within 
     * 'config.yml' to see if the new format is enabled. Secondly, it checks to see if 
     * the PrisonSystemSettings has recorded if the conversion has taken place.
     * Both have to be true in order for this function to return a value of true.,
     * </p>
     * 
     * @return
     */
    public static boolean useFriendlyUserFileNames() {
    	
    	return PrisonSystemSettings.useFriendlyUserFileNames();
    }
    
    /**
     * Do not use.  Use 'filenameCache( player )'.
     * 
     * @param player
     * @return
     */
    public static String filenameCacheNew( Player player ) {
    	
    	return "cache_" + getPlayerFileNameNewVersion( player );
    }
    /**
     * Do not use.  Use 'filenameCache( player )'.
     * 
     * @param player
     * @return
     */
    public static String filenameCacheOld( Player player ) {
    	
    	return getPlayerFileNameShortVersion( player );
    }
    
    /**
     * Do not use.  Use 'filenamePlayer( player )'.
     * 
     * @param player
     * @return
     */
    public static String filenamePlayerNew( Player player )
    {
    	return "player_" + getPlayerFileNameNewVersion( player );
    }
    /**
     * Do not use.  Use 'filenamePlayer( player )'.
     * 
     * @param player
     * @return
     */
    public static String filenamePlayerOld( Player player )
    {
    	return "player_" + player.getUUID().getLeastSignificantBits() + FILE_SUFFIX_JSON;
    }
    
	
    /**
     * <p>Do not use. This version is not compatible with bedrock players 
     * because all bedrock UUIDs are just zeros when using this formmat.
     * The newer format also includes the trailing 
     * 
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
	@Deprecated
    public static String getPlayerFileNameShortVersion( Player player ) {
    	
    	String UUIDString = player.getUUID().toString();
		String uuidFragment = getFileNamePrefixObsolete( UUIDString );
		
		return uuidFragment + "_" + player.getName() + FILE_SUFFIX_JSON;
    }
    
	/**
	 * <p>Do not use.  This does not support bedrock players because
	 * the prefix of bedrock UUIDs is all zeros.
	 * </p>
	 * 
	 * <p>This function returns the first 13 characters of the supplied
	 * file name, or UUID String. The hyphen is around the 12 or 13th position, 
	 * so it may or may not include it.
	 * </p>
	 * 
	 * @param playerFileName
	 * @return
	 */
	@Deprecated
	private static String getFileNamePrefixObsolete( String UUIDString ) {
		return UUIDString.substring( 0, 14 );
	}
	
	
	public String toString( Object obj ) {
		String json = getGson().toJson( obj );
				
		return json;
	}
	
	public <T> T fromString( String json, Class<T> klass ) {
		
		
		T obj = getGson().fromJson( json, klass );
		
		
		return obj;
	}
	
	
	
	public List<File> getFilesFromPrefix( String filePrefix, File path ) {
		List<File> results = new ArrayList<>();
    	
    	FileFilter fFilter = getFilePrefixFilter( filePrefix );
    	
    	
    	File[] collectionFiles = path.listFiles( fFilter );
    	for (File file : collectionFiles ) {
			results.add(file);
		}

    	return results;
	}
	
	
	public FileFilter getFilePrefixFilter( String filePrefix ) {
		
		FileFilter fileFilter = (file) -> {
			
			String fname = file.getName();
			boolean isTemp = fname.startsWith( FILE_PREFIX_BACKUP ) ||
							 fname.endsWith( FILE_SUFFIX_BACKUP ) ||
							 fname.endsWith( FILE_SUFFIX_TEMP ) ||
							 fname.endsWith( FILE_SUFFIX_TXT );
			
			return 
					fname.toLowerCase().startsWith( filePrefix.toLowerCase() ) &&
					!file.isDirectory() && !isTemp &&
					fname.endsWith( FILE_SUFFIX_JSON );
		};
		
		return fileFilter;
	}
	
	public static FileFilter getPrisonFileFilter() {
		
		FileFilter fileFilter = (file) -> {
			
			String fname = file.getName();
			boolean isTemp = fname.startsWith( FILE_PREFIX_BACKUP ) ||
							 fname.endsWith( FILE_SUFFIX_BACKUP ) ||
							 fname.endsWith( FILE_SUFFIX_TEMP ) ||
							 fname.endsWith( FILE_SUFFIX_TXT );
			
			return !file.isDirectory() && !isTemp &&
						fname.endsWith( FILE_SUFFIX_JSON );
		};
		
		return fileFilter;
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
	
	public void saveJsonExposedFile( File file, FileIOData data ) 
	{
		if ( file != null && data != null )
		{
			String json = getGsonExposed().toJson( data );
			
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
						file.getAbsoluteFile(), 
						(e.getMessage() == null ? "no-error-message" : e.getMessage()), 
						(json.length() > 500 ? 
								json.substring(0, 500) + "... (first 500 chars)" : json )
									.replace("%", "\\%"));
				
				Output.get().logError( message );

				// e.printStackTrace();
			}
		}
		
		return results;
	}
}

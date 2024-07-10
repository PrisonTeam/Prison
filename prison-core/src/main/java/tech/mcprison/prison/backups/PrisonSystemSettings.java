package tech.mcprison.prison.backups;

import tech.mcprison.prison.file.FileIOData;

/**
 * <p>This class serves as a way for prison to track major events and 
 * configuration settings.  Such as changes in settings that cannot be 
 * determined by looking at other files.  This is also an internal 
 * prison settings file that the users should never touch.
 * </p>
 * 
 * <p>For the friendly file names, the primary logic is located within 
 * the Ranks module since it has everything to do with ranks.
 * The source code can be found in this class:
 * '/prison-ranks/src/main/java/tech/mcprison/prison/ranks/tasks/PlayerNewFileNameCheckAsyncTask.java'
 * </p>
 */
public class PrisonSystemSettings
	implements FileIOData {
	public static final String PRISON_SYSTEM_FILENAME = "prison-system-settings.json";
	
	public static final String PRISON_SYSTEM_SETTING_FRIENDLY_PLAYER_FILE_NAMES = "prison-ranks.use-friendly-user-file-names";
	
//	private static PrisonSystemSettings instance = null;
	
//	private TreeMap<String,TreeMap<String,Object>> settings;
	
	public enum SystemSettingsKey {
		PlayerFileNameUpdate
		
		;
	}
	
//	private PrisonSystemSettings() {
//		super();
//		
//		this.settings = new TreeMap<>();
//	}
	
	
//	public static PrisonSystemSettings getInstance() {
//		if ( instance == null ) {
//			synchronized ( PrisonSystemSettings.class ) {
//				if ( instance == null ) {
//					
//					instance = new PrisonSystemSettings();
//				}
//			}
//		}
//		
//		return instance;
//	}
	
//	/**
//	 * <p>This function should be used to add new root nodes to ensure
//	 * that they are of type SystemSettingKey values.  This functional
//	 * also adds a TreeMap as it's value pair so it's ready to 
//	 * take on values.
//	 * </p>
//	 * 
//	 * @param rootKey
//	 */
//	public void addRootSetting( SystemSettingsKey rootKey ) {
//		if ( !getSettings().containsKey( rootKey.name()) ) {
//			
//			getSettings().put( rootKey.name(), new TreeMap<>() );
//		}
//	}
//
//	public TreeMap<String, Object> getRootSetting( SystemSettingsKey rootkey ) {
//		TreeMap<String, Object> results = null;
//		
//		if ( getSettings() == null ) {
//			load();
//		}
//		
//		// Try to add the rootKey just in case it does not exist:
//		addRootSetting(rootkey);
//		
//		results = getSettings().get(rootkey.name());
//		
//		return results;
//	}
//	
//	private TreeMap<String, TreeMap<String, Object>> getSettings() {
//		if ( settings == null ) {
//			this.settings = new TreeMap<>();
//		}
//		return settings;
//	}
//	private void setSettings(TreeMap<String, TreeMap<String, Object>> settings) {
//		this.settings = settings;
//	}
	
	
//	public void save() {
//		File file = getPrisonSystemSettingsFile();
//		
//		JsonFileIO jfIo = new JsonFileIO();
//		
//		jfIo.saveJsonFile(file, this);
//	}
	
//	public void load() {
//		File file = getPrisonSystemSettingsFile();
//		
//		if ( file.exists() ) {
//			JsonFileIO jfIo = new JsonFileIO();
//			
//			PrisonSystemSettings settings = (PrisonSystemSettings) jfIo.readJsonFile(file, this);
//			
//			setSettings( settings.getSettings() );
//			
//		}
//	}
	
//	public void unload() {
//		setSettings( null );
//	}
	
//	public File getPrisonSystemSettingsFile() {
//		
//		File dataStoragePath = new File( Prison.get().getDataFolder(), "data_storage" );
//		
//		File systemPath = new File( dataStoragePath, "system" );
//
//		systemPath.mkdirs();
//		
//		return new File( systemPath, PRISON_SYSTEM_FILENAME );
//	}
	
	
//    /**
//     * <p>This function will check two things.  First will be the config settings within 
//     * 'config.yml' to see if the new format is enabled. Secondly, it checks to see if 
//     * the PrisonSystemSettings has recorded if the conversion has taken place.
//     * Both have to be true in order for this function to return a value of true.,
//     * </p>
//     * 
//     * @return
//     */
//    public static boolean useFriendlyUserFileNames() {
//    	
//    	boolean useNewFormat = Prison.get().getPlatform()
//				.getConfigBooleanFalse( PRISON_SYSTEM_SETTING_FRIENDLY_PLAYER_FILE_NAMES );
//    	
//    	if ( useNewFormat ) {
//        	
//        	boolean converted = checkSystemFriendlyUserFileNamesConverted();
//
//        	useNewFormat = converted;
//    	}
//    	
//    	return useNewFormat;
//    }
    
//    /**
//     * <p>This function specifically returns the value of the 'converted' setting for the 
//     * 'playerFileNameUpdate' prison system key.
//     * </p>
//     * 
//     * <p>This does not perform any checks on the settings within 'config.yml'.  For a full
//     * check of both this 'converted' value, and the config settings use the 
//     * 'useFriendlyUserFileNames()' function instead of this function.
//     * </p>
//     * 
//     * @return
//     */
//    public static boolean checkSystemFriendlyUserFileNamesConverted() {
//    	
//    	TreeMap<String, Object> pfnUpdate = 
//    			PrisonSystemSettings.getInstance().getRootSetting( SystemSettingsKey.PlayerFileNameUpdate );
//    	
//    	// Unload the data:
////    	PrisonSystemSettings.getInstance().unload();
//    	
//    	boolean converted = !pfnUpdate.containsKey("converted") ?
//    			false : (Boolean) pfnUpdate.get("converted");
//    	
//    	return converted;
//    }
	
 }

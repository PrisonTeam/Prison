package tech.mcprison.prison.backups;

import java.io.File;
import java.util.TreeMap;

import tech.mcprison.prison.file.FileIOData;
import tech.mcprison.prison.file.JsonFileIO;

public class PrisonSystemSettings
	implements FileIOData {
	public static final String PRISON_SYSTEM_FILENAME = "prison-system-settings.json";
	
	private static PrisonSystemSettings instance = null;
	
	private TreeMap<String,TreeMap<String,Object>> settings;
	
	public enum SystemSettingKey {
		PlayerFileNameUpdate
		
		;
	}
	
	private PrisonSystemSettings() {
		super();
		
		this.settings = new TreeMap<>();
	}
	
	
	public static PrisonSystemSettings getInstance() {
		if ( instance == null ) {
			synchronized ( PrisonSystemSettings.class ) {
				if ( instance == null ) {
					
					instance = new PrisonSystemSettings();
				}
			}
		}
		
		return instance;
	}
	
	/**
	 * <p>This function should be used to add new root nodes to ensure
	 * that they are of type SystemSettingKey values.  This functional
	 * also adds a TreeMap as it's value pair so it's ready to 
	 * take on values.
	 * </p>
	 * 
	 * @param rootKey
	 */
	public void addRootSetting( SystemSettingKey rootKey ) {
		if ( !getSettings().containsKey( rootKey.name()) ) {
			
			getSettings().put( rootKey.name(), new TreeMap<>() );
		}
	}

	public TreeMap<String, Object> getRootSetting( SystemSettingKey rootkey ) {
		TreeMap<String, Object> results = null;
		
		if ( getSettings() == null ) {
			load();
		}
		
		// Try to add the rootKey just in case it does not exist:
		addRootSetting(rootkey);
		
		results = getSettings().get(rootkey.name());
		
		return results;
	}
	
	private TreeMap<String, TreeMap<String, Object>> getSettings() {
		return settings;
	}
	private void setSettings(TreeMap<String, TreeMap<String, Object>> settings) {
		this.settings = settings;
	}
	
	
	public void save() {
		File file = getPrisonSystemSettingsFile();
		
		JsonFileIO jfIo = new JsonFileIO();
		
		jfIo.saveJsonFile(file, this);
	}
	
	public void load() {
		File file = getPrisonSystemSettingsFile();
		
		JsonFileIO jfIo = new JsonFileIO();
		
		PrisonSystemSettings settings = (PrisonSystemSettings) jfIo.readJsonFile(file, this);
		
		setSettings( settings.getSettings() );
	}
	
	public void unload() {
		setSettings( null );
	}
	
	public File getPrisonSystemSettingsFile() {
		PrisonBackups pb = new PrisonBackups();
		File path = pb.getBackupDirectory();
		
		return new File( path, PRISON_SYSTEM_FILENAME );
	}
	
 }

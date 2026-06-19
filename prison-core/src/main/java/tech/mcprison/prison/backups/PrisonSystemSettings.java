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
	
	public enum SystemSettingsKey {
		PlayerFileNameUpdate
		
		;
	}
	
 }

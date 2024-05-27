package tech.mcprison.prison.ranks.tasks;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.backups.PrisonSystemSettings;
import tech.mcprison.prison.backups.PrisonSystemSettings.SystemSettingKey;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

/**
 * This task will run after the server starts up.
 * If the 'prison-ranks.use-friendly-user-file-name'
 */
public class PlayerNewFileNameCheckAsyncTask
	implements PrisonRunnable {

	public PlayerNewFileNameCheckAsyncTask() {
		super();
	}
	
	public static void submitTaskSync( long delayTicks ) {
		
		boolean useNewFormat = Prison.get().getPlatform()
				.getConfigBooleanFalse( "prison-ranks.use-friendly-user-file-name" );
		
		if ( useNewFormat ) {
			
			Output.get().logInfo( 
					"&PlayerFileNameCheck Task: &aUsing Friendly User filenames: "
					+ "&dConversion file check async task submitted." );
			
			Output.get().logInfo( 
					"&PlayerFileNameCheck Task: &aChecking: " +
							"&dPlayer Rank files &aand &dPlayer Cache files&a." );
			
			PlayerNewFileNameCheckAsyncTask task = new PlayerNewFileNameCheckAsyncTask();
			
			PrisonTaskSubmitter.runTaskLaterAsync( task, delayTicks );
		}
		else {
			// Fail silently...
		}
	}
	
	@Override
	public void run() {
		
		int changedPlayRankFiles = 0;
		int changedCacheFiles = 0;
		
		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		List<RankPlayer> players = pm.getPlayers();
		
		File playerRankpath = new File( new File( Prison.get().getDataFolder(), "ranksDb" ), "players");
		File playerCachePath = new File( Prison.get().getDataFolder(), "playerCache" );
				
		for (RankPlayer player : players) {
			
			// Rank player file:
			if ( playerRankpath.exists() )
			{
				
				String oldFilename = JsonFileIO.filenamePlayerOld( player );
				File oldPlayerFile = new File( playerRankpath, oldFilename );
				
				if ( oldPlayerFile.exists() ) {
					
					String newFilename = JsonFileIO.filenamePlayerNew( player );
					File newPlayerFile = new File( playerRankpath, newFilename );
					
					oldPlayerFile.renameTo(newPlayerFile);
					
//						Files.move( oldPlayerFile, newPlayerFile );
					changedPlayRankFiles++;
				}
			}
			
			
			// Player cache file:
			if ( playerCachePath.exists() )
			{
				
				String oldFilename = JsonFileIO.filenameCacheOld( player );
				File oldCacheFile = new File( playerCachePath, oldFilename );
				
				if ( oldCacheFile.exists() ) {
					
					String newFilename = JsonFileIO.filenameCacheNew( player );
					File newCacheFile = new File( playerCachePath, newFilename );
					
					oldCacheFile.renameTo(newCacheFile);
//						Files.move( oldCacheFile, newCacheFile );
					changedCacheFiles++;
					
					// The player's cache data object contains a File object pointing to
					// the old cache file, which needs to be replaced with the new
					// cache File object, otherwise the player cache will keep trying to
					// write to the old file name.
					// If the player cache does not contain this player, then it will not
					// be loaded. If the player is loaded later on, then it will load
					// from the newly renamed files.
					PlayerCachePlayerData pCache = player.getPlayerCache().getOnlinePlayerCached( player );
					if ( pCache != null ) {
						pCache.setPlayerFile( newCacheFile );
					}
				}
			}
 		}
		
		Output.get().logInfo( 
				"&3PlayerFileNameCheck Task: Files updated for %d players:  &aPlayer Rank files: "
						+ "&d%d  "
						+ "&aPlayer Cache files: &d%d",
						players.size(),
						changedPlayRankFiles,
						changedCacheFiles );
		
		if ( changedCacheFiles > 0 || changedCacheFiles > 0 ) {
			saveStatusDetailLog( changedPlayRankFiles, changedCacheFiles );
		}
		else if ( players.size() > 0 ) {
			Output.get().logInfo( 
					"&3PlayerFileNameCheck Task: It appears as if all player files have "
					+ "already been converted." );
		}
		
		List<String> msgs = getStatusDetails();
		
		for (String msg : msgs) {
			
			Output.get().logInfo( msg );
		}
		
	}
	
	public void saveStatusDetailLog( int changedPlayRankFiles, int changedCacheFiles ) {
		// Need to log it:
		TreeMap<String, Object> settings = PrisonSystemSettings.getInstance()
									.getRootSetting( SystemSettingKey.PlayerFileNameUpdate );
		
		if ( !settings.containsKey( "converted") ) {
			settings.put("converted", Boolean.TRUE );
		}
		
		if ( !settings.containsKey( "logs") ) {
			settings.put("logs", new ArrayList<Object>() );
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<Object> logs = (ArrayList<Object>) settings.get("logs");
		
		TreeMap<String,Object> log = new TreeMap<>();
		
		SimpleDateFormat sdFmt = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		log.put("run-date", sdFmt.format(new Date()) );
		log.put("player-cache-files-changed", Integer.valueOf(changedPlayRankFiles) );
		log.put("player-rank-files-changed", Integer.valueOf(changedCacheFiles) );
				
		logs.add( log );
		
		// Save the update:
		PrisonSystemSettings.getInstance().save();
		
		// Unload the data:
		PrisonSystemSettings.getInstance().unload();
		

	}
	
	public List<String> getStatusDetails() {
		List<String> msg = new ArrayList<>();

		String msgFmt = "  %-13s  %7s  %7s";
		
    	boolean useNewFormat = Prison.get().getPlatform()
				.getConfigBooleanFalse( "prison-ranks.use-friendly-user-file-name" );
    	
    	TreeMap<String, Object> pfnUpdate = 
    			PrisonSystemSettings.getInstance().getRootSetting( SystemSettingKey.PlayerFileNameUpdate );

		// Unload the data:
		PrisonSystemSettings.getInstance().unload();
    	
    	boolean converted = !pfnUpdate.containsKey("converted") ?
    						false : (Boolean) pfnUpdate.get("converted");
    	
    	msg.add( "PlayerFileNameUpdate: status: " );
    	msg.add( "  'prison-ranks.use-friendly-user-file-name': " + Boolean.toString(useNewFormat) );
    	msg.add( "  converted: " + Boolean.toString(converted) );
    	
    	if ( converted ) {

			@SuppressWarnings("unchecked")
			ArrayList<Object> logs = (ArrayList<Object>) pfnUpdate.get("logs");
			
			msg.add( String.format(msgFmt, "", "Changed", "Changed"));
			msg.add( String.format(msgFmt, "Date", "P-Ranks", "P-Cache"));
			msg.add( String.format(msgFmt, "-----------", "-------", "-------"));
			
			for (Object logObj : logs) {
				@SuppressWarnings("unchecked")
				TreeMap<String,Object> log = (TreeMap<String,Object>) logObj;
				
				String runDate = (String) log.get("run-date");
				int changedPlayRankFiles = (Integer) log.get("player-cache-files-changed");
				int changedCacheFiles = (Integer) log.get("player-rank-files-changed");
				
				msg.add( String.format(msgFmt, 
								runDate, 
								Integer.toString(changedCacheFiles), 
								Integer.toString(changedPlayRankFiles)));
			}
			
    	}

    	// msg.add( "" );

    	
    	return msg;
	}

}

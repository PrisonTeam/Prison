package tech.mcprison.prison.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.backups.PrisonBackups;
import tech.mcprison.prison.commands.RegisteredCommand;
import tech.mcprison.prison.discord.PrisonPasteChat;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;

public class PrisonStatsUtil {
	
	
	/**
	 * <p>This will return in a StringBuilder all of the information from the following
	 * commands.  They will be combined in one large StringBuilder object so a table of 
	 * contents can be built from it.  This is similar to running the following 4 
	 * commands, but are better organized with the order of the details (files last).
	 * </p>
	 * 
	 * <pre>/prison support submit version</pre>
	 * <pre>/prison support submit ranks</pre>
	 * <pre>/prison support submit mines</pre>
	 * <pre>/prison support submit commands</pre>
	 * 
	 * <p>The difference with this function, is that all of the raw files are 
	 * included at the end, instead of being mixed in with the other non-raw file 
	 * dumps.
	 * </p>
	 * 
	 * <p>Order of reports:</p>
	 * <ul>
	 *   <li>Version ALL</li>
	 *   <li>listeners ALL</li>
	 *   <li>Command Stats</li>
	 *   
	 *   <li>Ladder List - Missing?</li>
	 *   <li>Rank List</li>
	 *   <li>Rank details</li>
	 *   
	 *   <li>Mine List</li>
	 *   <li>Mine details</li>
	 *   
	 *   <li>Prison Backup Files</li>

	 *   <li>Ladder Files</li>
	 *   <li>Rank Files</li>
	 *   
	 *   <li>Mine Files</li>
	 *   
	 *   <li>ConfigSettings Files</li>
	 * 
	 * </ul>
	 * 
	 * @return
	 */
	public StringBuilder getSupportSubmitBasic() {
		StringBuilder sb = new StringBuilder();
		
		// version info:
		sb.append( getSupportSubmitVersionData() );
		
		// Listeners:
		sb.append( getSupportSubmitListenersData( "all" ) );
		
		// Command Stats:
		sb.append( getCommandStatsDetailData() );
		
		
		// Rank Lists and Rank details:
		sb.append( getSupportSubmitRanksData() );
	
		
		// Mine lists and Mine details:
		sb.append( getSupportSubmitMinesData() );
		
		
		// Backup log files:
		sb.append( getPrisonBackupLogsData() );
		
		
		// Rank Files:
		sb.append( getSupportSubmitRanksFileData() );
		
		
		// Mine files:
		sb.append( getSupportSubmitMinesFileData() );
		

		// Config files:
		sb.append( getSupportSubmitConfigsData() );
		
		
		sb.append( getColorTest() );
		
		return sb;
	}

	public ChatDisplay displayVersion(String options) {

		boolean isBasic = options == null || "basic".equalsIgnoreCase(options);

		ChatDisplay display = new ChatDisplay("/prison version");
		display.addText("&7Prison Version: %s", Prison.get().getPlatform().getPluginVersion());

		display.addText("&7Running on Platform: %s", Prison.get().getPlatform().getClass().getName());
		display.addText("&7Minecraft Version: %s", Prison.get().getMinecraftVersion());

		// System stats:
		display.addText("");

		Prison.get().displaySystemSettings(display);

		Prison.get().displaySystemTPS(display);

		display.addText("");

		// This generates the module listing, the autoFeatures overview,
		// the integrations listings, and the plugins listings.
		boolean showLaddersAndRanks = true;
		Prison.get().getPlatform().prisonVersionFeatures(display, isBasic, showLaddersAndRanks);

		
		// check directory structures:
		checkDirectoryStructures( display );
		
		
		return display;
	}

	public StringBuilder getSupportSubmitVersionData() {
		ChatDisplay display = displayVersion("ALL");
		StringBuilder text = display.toStringBuilder();
		return text;
	}


	public void checkDirectoryStructures(ChatDisplay display) {

	       
        display.addText(".");
        display.addText("&7Prison File System Check:");

        display.addText( checkDirectory( "/" ) );
        display.addText( checkDirectory( "backpacks" ) );
        display.addText( checkDirectory( "backups" ) );
        display.addText( checkDirectory( "data_storage" ) );
        display.addText( checkDirectory( "data_storage/mines" ) );
        display.addText( checkDirectory( "data_storage/playerCache" ) );
        display.addText( checkDirectory( "data_storage/ranksDb" ) );
        display.addText( checkDirectory( "data_storage/ranksDb/ladders" ) );
        display.addText( checkDirectory( "data_storage/ranksDb/players" ) );
        display.addText( checkDirectory( "data_storage/ranksDb/ranks" ) );
        display.addText( checkDirectory( "module_conf" ) );
		
	}

	private String checkDirectory( String dirPath ) {
		
		String pathMask = Prison.get().getDataFolder().getParentFile().getAbsolutePath();
		
		File path = new File( Prison.get().getDataFolder(), dirPath );
		boolean pathCreated = path.mkdirs();
		
		int countDirs = 0;
		int countFiles = 0;
		double fileSize = 0;
		String fileSizeUnit = "";
		
		File[] files = path.listFiles();
		for (File f : files) {
			
			if ( f.isDirectory() ) {
				countDirs++;
			}
			else if ( f.isFile() ) {
				countFiles++;
				fileSize += f.length();
			}
		}
		
		if ( fileSize > 0 ) {
			fileSizeUnit = "bytes";
			
			if ( fileSize >= 1024 ) {
				fileSize /= 1024.0;
				fileSizeUnit = "KB";
				
				if ( fileSize >= 1024 ) {
					fileSize /= 1024.0;
					fileSizeUnit = "MB";
					
					if ( fileSize >= 1024 ) {
						fileSize /= 1024.0;
						fileSizeUnit = "GB";
						
					}
				}
			}
		}
		
		
		
		DecimalFormat iFmt = Prison.getDecimalFormatStaticInt();
		DecimalFormat dFmt = Prison.getDecimalFormatStaticDouble();
		String msg = String.format(
				"  &bplugins%-40s  &2dirs: %s%3s  &2files: %s%3s  &2totalFileSize: %s%7s &3%s %s",
				path.getAbsolutePath().replace(pathMask, ""),
				(countDirs == 0 ? "&3" : "&b"), iFmt.format( countDirs ),
				(countFiles == 0 ? "&3" : "&b"), iFmt.format( countFiles ),
				(fileSize == 0 ? "&3" : "&b"), dFmt.format( fileSize ),
				fileSizeUnit, 
				( pathCreated ? " &6DirCreated!" : "" )
				);
		
		return msg;
	}

	
	public StringBuilder getColorTest() {
		StringBuilder sb = new StringBuilder();

		sb.append( "\n\n" );
		sb.append( "Color Test:\n\n" );
		sb.append( "||Color Test||\n" );

		sb.append( "&0#########  &r&1#########  &r&2#########\n" );
		sb.append( "&0### 0 ###  &r&1### 1 ###  &r&2### 2 ###\n" );
		sb.append( "&0#########  &r&1#########  &r&2#########\n" );

		sb.append( "&3#########  &r&4#########  &r&5#########\n" );
		sb.append( "&3### 3 ###  &r&4### 4 ###  &r&5### 5 ###\n" );
		sb.append( "&3#########  &r&4#########  &r&5#########\n" );
		
		sb.append( "&6#########  &r&7#########  &r&8#########\n" );
		sb.append( "&6### 6 ###  &r&7### 7 ###  &r&8### 8 ###\n" );
		sb.append( "&6#########  &r&7#########  &r&8#########\n" );
		
		sb.append( "&9#########  &r&a#########  &r&b#########\n" );
		sb.append( "&9### 9 ###  &r&a### a ###  &r&b### b ###\n" );
		sb.append( "&9#########  &r&a#########  &r&b#########\n" );
		
		sb.append( "&c#########  &r&d#########  &r&e#########\n" );
		sb.append( "&c### c ###  &r&d### d ###  &r&e### e ###\n" );
		sb.append( "&c#########  &r&d#########  &r&e#########\n" );
		
		sb.append( "&f#########  &r\n" );
		sb.append( "&f### f ###  &r\n" );
		sb.append( "&f#########  &r\n" );
		
		
		sb.append( "&3&l#####################&r\n" );
		sb.append( "&3&l###   Bold & 3    ###&r\n" );
		sb.append( "&3&l#####################&r\n" );
		
		sb.append( "&3&m#####################&r\n" );
		sb.append( "&3&m###  Strike & 3   ###&r\n" );
		sb.append( "&3&m#####################&r\n" );
		
		sb.append( "&3&n#####################&r\n" );
		sb.append( "&3&n### Underline & 3 ###&r\n" );
		sb.append( "&3&n#####################&r\n" );
		
		sb.append( "&3&o#####################&r\n" );
		sb.append( "&3&o###  Italic & 3   ###&r\n" );
		sb.append( "&3&o#####################&r\n" );
		
		sb.append( "\n\n" );
		
		return sb;
	}
	
	
	public StringBuilder getSupportSubmitConfigsData() {
		Prison.get().getPlatform().saveResource("plugin.yml", true);

		String fileNames = "config.yml plugin.yml backups/versions.log "
				+ "autoFeaturesConfig.yml blockConvertersConfig.json "
				+ "modules.yml module_conf/mines/config.json module_conf/mines/mineBombsConfig.json "
				+ "SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml";
		List<File> files = convertNamesToFiles(fileNames);

		StringBuilder text = new StringBuilder();

		for (File file : files) {

			addFileToText(file, text);

			if (file.getName().equalsIgnoreCase("plugin.yml")) {
				file.delete();
			}
		}
		return text;
	}
	
	
	public void copyConfigsFiles() {
		
		String fileNames = "config.yml "
				+ "autoFeaturesConfig.yml blockConvertersConfig.json "
				+ "modules.yml module_conf/mines/mineBombsConfig.json "
				+ "SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml";
		List<File> files = convertNamesToFiles(fileNames);
		
		JsonFileIO fio = new JsonFileIO();
		
		for (File file : files) {

			if ( file.exists() ) {
				File buFile = fio.getBackupFile( file, "newPrisonVersion", "bu" );
				
				try {
					Files.copy( file.toPath(), buFile.toPath() );
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	
	
	public StringBuilder getSupportSubmitRanksData() {
		
		StringBuilder text = new StringBuilder();
		
		text.append(Prison.get().getPlatform().getRanksListString());
		printFooter(text);
		
//		List<File> files = listFiles("data_storage/ranksDb/ladders/", ".json");
//		files.addAll(listFiles("data_storage/ranksDb/ranks/", ".json"));
//		for (File file : files) {
//			
//			addFileToText(file, text);
//		}
			
		return text;
	}

	public StringBuilder getSupportSubmitRanksFileData() {
		List<File> files = listFiles("data_storage/ranksDb/ladders/", ".json");
		files.addAll(listFiles("data_storage/ranksDb/ranks/", ".json"));

		StringBuilder text = new StringBuilder();

		printFooter(text);

		for (File file : files) {

			addFileToText(file, text);

		}
		return text;
	}

	public StringBuilder getSupportSubmitMinesData() {
//		List<File> files = listFiles("data_storage/mines/mines/", ".json");
//		Collections.sort(files);

		StringBuilder text = new StringBuilder();

//		text.append("\n");
//		text.append("Table of contents:\n");
//		text.append("  1. Mine list - All mines including virtual mines: /mines list all\n");
//		text.append("  2. Mine info - All mines: /mines info <mineName> all\n");
//		text.append("  3. Mine files - Raw JSON dump of all mine configuration files.\n");
//		text.append("\n");

		// Display a list of all mines, then display the /mines info <mineName> all for
		// each:
		text.append(Prison.get().getPlatform().getMinesListString());
//		printFooter(text);

//		// get all the file details for each mine:
//		for (File file : files) {
//
//			addFileToText(file, text);
//		}
		
		return text;
	}
	
	public StringBuilder getSupportSubmitMinesFileData() {
		List<File> files = listFiles("data_storage/mines/mines/", ".json");
		Collections.sort(files);
		
		StringBuilder text = new StringBuilder();
		
		printFooter(text);
		
		// get all the file details for each mine:
		for (File file : files) {
			
			addFileToText(file, text);
		}
		
		return text;
	}
	
	public StringBuilder getSupportSubmitListenersData( String listenerType ) {
		
		StringBuilder sb = new StringBuilder();

		if ( listenerType == null ) {
			listenerType = "all";
		}
		
    	if ( "blockBreak".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( "||Listeners blockBreak||" );
    		sb.append( Prison.get().getPlatform().dumpEventListenersBlockBreakEvents() );
    	}
    	
    	if ( "blockPlace".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( "||Listeners blockPlace||" );
    		sb.append( Prison.get().getPlatform().dumpEventListenersBlockPlaceEvents() );
    	}
    	
    	if ( "chat".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( "||Listeners chat||" );
    		sb.append( Prison.get().getPlatform().dumpEventListenersPlayerChatEvents() );
    	}
    	
//    	if ( "traceBlockBreak".equalsIgnoreCase( listenerType ) ) {
//    		
//    		Prison.get().getPlatform().traceEventListenersBlockBreakEvents( sender );
//    		
//    	}
    	
    	if ( "playerInteract".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( "||Listeners playerInteract||" );
    		sb.append( Prison.get().getPlatform().dumpEventListenersPlayerInteractEvents() );
    	}

    	if ( "playerDropItem".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( "||Listeners playerDropItem||" );
    		sb.append( Prison.get().getPlatform().dumpEventListenersPlayerDropItemEvents() );
    	}
    	
    	if ( "playerPickupItem".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( "||Listeners playerPickupItem||" );
    		sb.append( Prison.get().getPlatform().dumpEventListenersPlayerPickupItemEvents() );
    	}
    	
    	
		return sb;
	}

	
	public StringBuilder getCommandStatsDetailData() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "\n\n" );
		
		List<String> cmds = getCommandStats();
		cmds.add( 1,  "||CommandStats List||" );
		
    	for (String cmd : cmds) {
			
    		sb.append( cmd ).append( "\n" );
		}
		
		return sb;
	}
	
	public void getCommandStatsData() {
		
		List<String> cmds = getCommandStats();
		for (String cmd : cmds) {
			
			Output.get().logInfo( cmd );
		}
	}
	
	private List<String> getCommandStats() {
		List<String> results = new ArrayList<>();
		
		DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
		DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
		
    	TreeSet<RegisteredCommand> allCmds = Prison.get().getCommandHandler().getAllRegisteredCommands();
    	
    	results.add( "Prison Command Stats:" );
    	results.add( 
    			Output.stringFormat( "    &a&n%-40s&r  &a&n%7s&r  &a&n%-11s&r", 
    					" Commands     ", " Usage ", "  Avg ms  ") );
    	
    	int count = 0;
    	int totals = 0;
    	double totalDuration = 0d;
    	for (RegisteredCommand cmd : allCmds) {
			
    		if ( cmd.getUsageCount() > 0 ) {
    			
    			double duration = cmd.getUsageRunTimeNanos() / (double) cmd.getUsageCount() / 1000000.0d;
    			
    			results.add( Output.stringFormat( "    &2%-40s  &2%7s  &2%11s",
    					cmd.getCompleteLabel(), 
    					iFmt.format( cmd.getUsageCount() ),
    					dFmt.format( duration )
    					) );
    			count++;
    			totals += cmd.getUsageCount();
    			totalDuration += cmd.getUsageRunTimeNanos();
    		}
		}
    	
    	results.add( Output.stringFormat("  &3Total Registered Prison Commands: &7%9s", iFmt.format( allCmds.size() )) );
    	results.add( Output.stringFormat("  &3Total Prison Commands Listed:     &7%9s", iFmt.format( count )) );
    	results.add( Output.stringFormat("  &3Total Prison Command Usage:       &7%9s", iFmt.format( totals )) );
    	
    	double avgDuration = totalDuration / (double) count / 1000000.0d;
    	results.add( Output.stringFormat("  &3Average Command Duration ms:      &7%9s", dFmt.format( avgDuration )) );
    	
    	results.add( "  &d&oNOTE: Async Commands like '/mines reset' will not show actual runtime values. " );

    	
		return results;
	}

	
	public StringBuilder getPrisonBackupLogsData() {
		StringBuilder sb = new StringBuilder();
		
    	// Include Prison backup logs:
		sb.append( "\n\n" );
		sb.append( "Prison Backup Logs:" ).append( "\n" );
    	List<String> backupLogs = getPrisonBackupLogs();
    	
    	for (String log : backupLogs) {
    		sb.append( Output.decodePercentEncoding(log) ).append( "\n" );
		}
    	
		return sb;
	}
	
	
    public List<String> getPrisonBackupLogs() {
    	PrisonBackups prisonBackup = new PrisonBackups();
    	List<String> backupLogs = prisonBackup.backupReport02BackupLog();
    	return backupLogs;
    }
    
    
	public void readFileToStringBulider(File textFile, StringBuilder text) {
		try (BufferedReader br = Files.newBufferedReader(textFile.toPath());) {
			String line = br.readLine();
			while (line != null && text.length() < PrisonPasteChat.HASTEBIN_MAX_LENGTH) {

				text.append(line).append("\n");

				line = br.readLine();
			}

			if (text.length() > PrisonPasteChat.HASTEBIN_MAX_LENGTH) {

				String trimMessage = "\n\n### Log has been trimmed to a max length of "
						+ PrisonPasteChat.HASTEBIN_MAX_LENGTH + "\n";
				int pos = PrisonPasteChat.HASTEBIN_MAX_LENGTH - trimMessage.length();

				text.insert(pos, trimMessage);
				text.setLength(PrisonPasteChat.HASTEBIN_MAX_LENGTH);
			}

		} catch (IOException e) {
			Output.get().logInfo("Failed to read log file: %s  [%s]", textFile.getAbsolutePath(), e.getMessage());
			return;
		}
	}

	private List<File> listFiles(String path, String fileSuffix) {
		List<File> files = new ArrayList<>();

		File dataFolder = Prison.get().getDataFolder();
		File filePaths = new File(dataFolder, path);

		for (File file : filePaths.listFiles()) {
			if (file.getName().toLowerCase().endsWith(fileSuffix.toLowerCase())) {
				files.add(file);
			}
		}

		return files;
	}

	private void addFileToText(File file, StringBuilder sb) {
		DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
		DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
		SimpleDateFormat sdFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		
		String parentDirName = file.getParentFile().getName();
		
		String header = "";
		String name = "";
		
		if ( "ranks".equalsIgnoreCase(parentDirName) ) {
			// Rank file names have a prefix of "ranks_" and then the rankId, followed by the suffix.
			// Need to use the rankId to find the rank's name:

			header = "Rank";
			name = Prison.get().getPlatform().getRankByFileName( file.getName() );
		}
		else if ( "ladders".equalsIgnoreCase(parentDirName) ) {
			// Ladder file names have a prefix of "ladders_" and then the ladderId, followed by the suffix.
			// Need to use the ladderId to find the ladder's name:
			
			header = "Ladder";
			name = Prison.get().getPlatform().getLadderByFileName( file.getName() );
		}
		else if ( "mines".equalsIgnoreCase(parentDirName) ) {
			// The file name of the mine, minus the suffix, is the name's name.
			
			header = "Mine";
			name = file.getName().replace(".json", "");
		}
		else {
			header = "Config";
			name = file.getName();
		}
		
		
		sb.append("\n");

		JumboTextFont.makeJumboFontText(file.getName(), sb);

		sb.append("\n");
		

		// Hyper Link codes: 
		sb.append( "||" )
			.append( header ) 
			.append( " " )
			.append( name )
			.append( " " )
			.append( "File" )
			.append( "||\n" );
			
		String prisonPath = Prison.get().getDataFolder().getAbsolutePath();
		String filePath = file.getAbsolutePath().replace( prisonPath, "" );
		
		long fileSize = file.length();
		double fileSizeKB = fileSize / 1024.0;

		sb.append("File Name:   ").append( file.getName() ).append("\n");
		sb.append("Prison Path: ").append( prisonPath ).append("\n");
		sb.append("File Path:   ").append( filePath ).append("\n");
		sb.append("File Size:   ").append( iFmt.format( fileSize ) ).append(" bytes\n");
		sb.append("File Size:   ").append( dFmt.format( fileSizeKB ) ).append(" KB\n");
		sb.append("File Date:   ").append( sdFmt.format(new Date(file.lastModified())) ).append(" \n");
		sb.append("File Stats:  ").append( file.exists() ? "EXISTS " : "" ).append(file.canRead() ? "READABLE " : "")
				.append(file.canWrite() ? "WRITEABLE " : "").append("\n");

		sb.append("\n");
		sb.append("=== ---  ---   ---   ---   ---   ---   ---   ---  --- ===\n");
		sb.append("\n");

		if (file.exists() && file.canRead()) {
			sb.append("&-");
			readFileToStringBulider(file, sb);
			sb.append("\n&+");
		} else {
			sb.append("Warning: The file is not readable so it cannot be included.\n");
		}

		printFooter(sb);
	}

	public void printFooter(StringBuilder sb) {

		sb.append("\n\n");
		sb.append("===  ---  ===   ---   ===   ---   ===   ---   ===  ---  ===\n");
		sb.append("=== # # ### # # # ### # # # ### # # # ### # # # ### # # ===\n");
		sb.append("===  ---  ===   ---   ===   ---   ===   ---   ===  ---  ===\n");
		sb.append("\n\n");

	}

	private List<File> convertNamesToFiles(String fileNames) {
		List<File> files = new ArrayList<>();

		File dataFolder = Prison.get().getDataFolder();

		for (String fileName : fileNames.split(" ")) {
			File file = new File(dataFolder, fileName);
			files.add(file);
		}

		return files;
	}

}

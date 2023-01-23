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

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.discord.PrisonPasteChat;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;

public class PrisonStatsUtil {

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

		return display;
	}

	public StringBuilder getSupportSubmitVersionData() {
		ChatDisplay display = displayVersion("ALL");
		StringBuilder text = display.toStringBuilder();
		return text;
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
		List<File> files = listFiles("data_storage/ranksDb/ladders/", ".json");
		files.addAll(listFiles("data_storage/ranksDb/ranks/", ".json"));

		StringBuilder text = new StringBuilder();

		text.append(Prison.get().getPlatform().getRanksListString());
		printFooter(text);

		for (File file : files) {

			addFileToText(file, text);

		}
		return text;
	}

	public StringBuilder getSupportSubmitMinesData() {
		List<File> files = listFiles("data_storage/mines/mines/", ".json");
		Collections.sort(files);

		StringBuilder text = new StringBuilder();

		text.append("\n");
		text.append("Table of contents:\n");
		text.append("  1. Mine list - All mines including virtual mines: /mines list all\n");
		text.append("  2. Mine info - All mines: /mines info <mineName> all\n");
		text.append("  3. Mine files - Raw JSON dump of all mine configuration files.\n");
		text.append("\n");

		// Display a list of all mines, then display the /mines info <mineName> all for
		// each:
		text.append(Prison.get().getPlatform().getMinesListString());
		printFooter(text);

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
    		
    		sb.append( Prison.get().getPlatform().dumpEventListenersBlockBreakEvents() );
    	}
    	
    	if ( "chat".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( Prison.get().getPlatform().dumpEventListenersPlayerChatEvents() );
    	}
    	
//    	if ( "traceBlockBreak".equalsIgnoreCase( listenerType ) ) {
//    		
//    		Prison.get().getPlatform().traceEventListenersBlockBreakEvents( sender );
//    		
//    	}
    	
    	if ( "playerInteract".equalsIgnoreCase( listenerType ) || "all".equalsIgnoreCase( listenerType ) ) {
    		
    		sb.append( Prison.get().getPlatform().dumpEventListenersPlayerInteractEvents() );
    	}

		return sb;
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
		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
		SimpleDateFormat sdFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sb.append("\n");

		JumboTextFont.makeJumboFontText(file.getName(), sb);

		sb.append("\n");

		sb.append("File Name:   ").append(file.getName()).append("\n");
		sb.append("File Path:   ").append(file.getAbsolutePath()).append("\n");
		sb.append("File Size:   ").append(dFmt.format(file.length())).append(" bytes\n");
		sb.append("File Date:   ").append(sdFmt.format(new Date(file.lastModified()))).append(" bytes\n");
		sb.append("File Stats:  ").append(file.exists() ? "EXISTS " : "").append(file.canRead() ? "READABLE " : "")
				.append(file.canWrite() ? "WRITEABLE " : "").append("\n");

		sb.append("\n");
		sb.append("=== ---  ---   ---   ---   ---   ---   ---   ---  --- ===\n");
		sb.append("\n");

		if (file.exists() && file.canRead()) {
			readFileToStringBulider(file, sb);
		} else {
			sb.append("Warning: The file is not readable so it cannot be included.\n");
		}

		printFooter(sb);
	}

	public void printFooter(StringBuilder sb) {

		sb.append("\n\n\n");
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

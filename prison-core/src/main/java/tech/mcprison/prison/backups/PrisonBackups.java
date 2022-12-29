package tech.mcprison.prison.backups;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cache.CoreCacheFiles;
import tech.mcprison.prison.file.ZipFileIO;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.PrisonStatsUtil;

public class PrisonBackups {

	public static final String FILE_BACKUP_DIRECTORY_PATH = "backups";
	public static final String FILEBACKUP_VERSIONS_FILE_NAME = "/versions.log";
	public static final String FILE_BACKUP_VERSIONS_FILE = FILE_BACKUP_DIRECTORY_PATH + FILEBACKUP_VERSIONS_FILE_NAME;
	
	public static final String VERSIONS_FILE_VERSION_PREFIX = "New_Prison_Version:";
	public static final String VERSIONS_FILE_BACKUP_MADE_PREFIX = "Backup:";
	
	
	private File backupDirectory = null;
	private Date backupStartDate;
	private long startTimeNanos = 0L;
	
	
	private String zipFilePrefix;
	private Path sourceDirectoryPath;
	private File zipFile;
	
	private ArrayList<File> filesBackups;
	private ArrayList<File> filesToBackup;
	private ArrayList<File> filesToDelete;
	private ArrayList<File> filesWithErrors;
	
	private DecimalFormat dFmt;
	private SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd_kk-mm" );
	private SimpleDateFormat sdsFmt = new SimpleDateFormat( "yyyy-MM-dd kk:mm:ss.SSS" );
	
	
	public enum BackupTypes {
		upgrade,
		auto,
		manual
	}
	
	public PrisonBackups() {
		super();
		
		this.dFmt = Prison.get().getDecimalFormatDouble();
		
		this.filesBackups = new ArrayList<>();
		this.filesToBackup = new ArrayList<>();
		this.filesToDelete = new ArrayList<>();
		this.filesWithErrors = new ArrayList<>();
		
	}
	

	public void initialStartupVersionCheck() {

		String prisonVersion = Prison.get().getPlatform().getPluginVersion();
		String lastWrittenVersion = getLastWrittenVersion();
		
		
		// Run a backup if lastWrittenVersion is null or not equal to prisonVersion:
		if ( lastWrittenVersion == null || !prisonVersion.equalsIgnoreCase(lastWrittenVersion) ) {
		
			// Make backups of the following files:
			PrisonStatsUtil psUtils = new PrisonStatsUtil();
			psUtils.copyConfigsFiles();
		}
	}
	
	private String getLastWrittenVersion() {
		String lastWrittenVersion = null;
		
		File versionsFile = new File( Prison.get().getDataFolder(), FILE_BACKUP_VERSIONS_FILE );
		
		try {
			if ( versionsFile.exists() ) {
				List<String> lines = Files.readAllLines( versionsFile.toPath() );
				
				for (String line : lines) {
					if ( line != null && line.length() > 0 && line.startsWith( VERSIONS_FILE_VERSION_PREFIX ) ) {
						String vers[] = line.split( " " );
						if ( vers.length >= 2 ) {
							
							lastWrittenVersion = vers[1];
						}
					}
				}
			}
			else {
				
				versionsFile.getParentFile().mkdirs();
				
				Files.createFile( versionsFile.toPath() );
			}
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return lastWrittenVersion;
	}
	
	public void serverStartupVersionCheck() {
		
		String prisonVersion = Prison.get().getPlatform().getPluginVersion();
		String lastWrittenVersion = getLastWrittenVersion();
		
		File versionsFile = new File( Prison.get().getDataFolder(), FILE_BACKUP_VERSIONS_FILE );
		
		// Run a backup if lastWrittenVersion is null or not equal to prisonVersion:
		if ( lastWrittenVersion == null || !prisonVersion.equalsIgnoreCase(lastWrittenVersion) ) {
			
			String message = String.format( 
					"Prison detected a version change. Forcing a backup of all settings.  " +
					"Current version: %s  Previous version: %s", 
					prisonVersion,
					( lastWrittenVersion == null ? "(not detected)" : lastWrittenVersion )
					);
			
			Output.get().logInfo( message );
			
			// First write the current version to the file:
			writeCurrentVersionToVersionsFile( versionsFile, prisonVersion );
			
			// Run the backup:
			startBackup( BackupTypes.upgrade, null );
		}
		
	}
	
	private void writeCurrentVersionToVersionsFile(File versionsFile, String prisonVersion) {
		
		String line = String.format( 
				"%s %s %s :: New prison version detected. Forcing a backup.\n",
				VERSIONS_FILE_VERSION_PREFIX, 
				prisonVersion,
				sdsFmt.format( new Date() )
				);
		try {
			Files.write( versionsFile.toPath(), line.getBytes(), StandardOpenOption.APPEND );
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void writeCurrentBackupInfoToVersionsFile( String message ) {
		File versionsFile = new File( Prison.get().getDataFolder(), FILE_BACKUP_VERSIONS_FILE );
		
		String line = String.format( 
				"%s :: %s \n",
				VERSIONS_FILE_BACKUP_MADE_PREFIX, 
				message
				);
		try {
			Files.write( versionsFile.toPath(), line.getBytes(), StandardOpenOption.APPEND );
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public String startBackup( BackupTypes backupType, String notes ) {
		
		this.backupStartDate = new Date();
		this.startTimeNanos = System.nanoTime();
		
		
		// Reset collections:
		this.filesBackups.clear();
		this.filesToBackup.clear();
		this.filesToDelete.clear();
		this.filesWithErrors.clear();
		
		this.zipFile = getNewBackupFile( backupType, notes );
		
		// Gather all files:
		gatherFiles( Prison.get().getDataFolder() );
		
		
		// The files in the zip file needs to be placed in a directory:
//		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd_kk-mm" );
		String zipFilePrefix = "backup_" + sdFmt.format( backupStartDate );
		this.zipFilePrefix = zipFilePrefix;
		
		// Save to zip file:
		ZipFileIO zipIo = new ZipFileIO();
		
		zipIo.writeToZipFileBackups(  
				zipFile,
				this );
		
		
		// Print out the list of errors from generating the zip file:
		for ( File file : filesWithErrors ) {
			
			Path targetFile = Prison.get().getDataFolder().toPath().relativize( file.toPath() );
			
			String message = String.format( 
					"PrisonBackups: Error trying to add file Prison backup file: %s  %s", 
					zipFile.getAbsolutePath(), 
					targetFile.toString()
					);
			Output.get().logError( message );

		}
		
		// Delete the files that should be deleted:
		for ( File file : filesToDelete ) {
			
			try {
				Files.delete( file.toPath() );
			} 
			catch (IOException e) {
				
				String message = String.format( 
						"PrisonBackups: Error trying to delete a temp file in prison which was " +
						"backed in: %s   " +
						"Temp file: %s  [%s]", 
						zipFile.getAbsoluteFile(), 
						file.getAbsolutePath(),
						e.getMessage()
						);
				Output.get().logError( message );

			}
		}
		
		long size = zipFile.length();
		double sizeKb = size / 1024.0;
		
		String message = String.format( 
				"Backup status: %s  %s KB   files: %d   temp files purged: %d   errors: %d",
				zipFile.getAbsolutePath(),
				dFmt.format( sizeKb ),
				filesToBackup.size(),
				filesToDelete.size(),
				filesWithErrors.size()
				
				);
		
		writeCurrentBackupInfoToVersionsFile( message );
		
		return message;
	}
	
	
	public String backupReport01() {
		
//		DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
		long stop = System.nanoTime();
		double runTimeMs = ( stop - getStartTimeNanos() ) / 1000000.0d;
		
//		long size = zipFile.length();
//		double sizeKb = size / 1024.0;
		
		
		
		String msg1 = String.format(
				"Prison backup:\n" +
				"  Started:  %s \n" +
				"  Compleated: %s    %s ms \n" +
				"  %s   \n" +
				"  files backed up: %d  \n" +
				"  temp files: %d   (deleted) \n" + 
				"  Errors: %d \n\n",
				sdsFmt.format( getBackupStartDate() ),
				sdsFmt.format( new Date() ),
				dFmt.format( runTimeMs ),
				zipFile.getAbsolutePath(),
//				dFmt.format( sizeKb ),
				filesBackups.size(),
				filesToDelete.size(),
				filesWithErrors.size()
				);
		
		return msg1;
	}
	
	
	public List<String> backupReport02BackupLog() {
		List<String> results = new ArrayList<>();

		File backupDir = getBackupDirectoryFile();
		
		File backupLogFile = new File( backupDir, FILEBACKUP_VERSIONS_FILE_NAME );

		try {
			results = Files.readAllLines( backupLogFile.toPath() );
		} 
		catch (IOException e) {
			results.add( 
					Output.stringFormat( "\n\nError reading %s.  [%s]",
							backupLogFile.toString(), e.getMessage() ) );
		}
		
		return results;
	}
	
	
	public StringBuilder backupReportVersionData() {
		return Prison.get().getPrisonStatsUtil().getSupportSubmitVersionData();
	}
	
	public StringBuilder backupReportConfigsData() {
		return Prison.get().getPrisonStatsUtil().getSupportSubmitConfigsData();
	}
	
	public StringBuilder backupReportRanksData() {
		return Prison.get().getPrisonStatsUtil().getSupportSubmitRanksData();
	}
	
	public StringBuilder backupReportMinesData() {
		return Prison.get().getPrisonStatsUtil().getSupportSubmitMinesData();
	}
	
	public StringBuilder backupReportListenersData() {
		return Prison.get().getPrisonStatsUtil().getSupportSubmitListenersData( "all" );
	}
	
	/**
	 * This generates a report of everything that was backed up.
	 * 
	 * @return
	 */
	public String backupReportListTemporalFiles() {
		StringBuilder sb = new StringBuilder();
		
//		sb.append( "\n\n" );
//		
//		sb.append( "Files Backed Up:  \n- - - - - - - - - -\n" );
//		for ( File file : filesToBackup ) {
//			sb.append( printFileDetails(file, true) );
//		}
		
		sb.append( "\n\n" );
		
		sb.append( "Files Deleted:  \n- - - - - - - - - -\n  Warning: They are only contained in this " + 
						"zip file now. delete this zip file with caution.\n" );
		for ( File file : filesToDelete ) {
			sb.append( printFileDetails(file, true) );
		}
		
		sb.append( "\n\n" );
		
		sb.append( "Files with Errors - Unable to backup:\n" +
				   "- - - - - - - - - - - - - - - - - - -\n" );
		for ( File file : filesWithErrors ) {
			sb.append( printFileDetails(file, true) );
		}
		
		sb.append( "\n\n" );
		
		sb.append( "Existing Backup Files (not included in zip):\n" +
				   "- - - - - - - - - - - - - - - - - - - - - - \n" );
		for ( File file : filesBackups ) {
			sb.append( printFileDetails(file, true) );
		}
		
		return sb.toString();
	}
	
	private StringBuilder printFileDetails(File file, boolean includeZipPrefix ) {
		StringBuilder sb = new StringBuilder();
		
		Path targetFile = getSourceDirectoryPath().relativize( file.toPath() );
		
		sb.append( "  " );
		
		if ( includeZipPrefix ) {
			sb.append( getZipFilePrefix() );
			sb.append( "/" );
		}
		
		sb.append( targetFile.toString() );
					
		double size = file.length() / 1024.0d;
		sb.append( "  " );
		sb.append( dFmt.format(size) );
		sb.append( " kb  " );
		
		sb.append( sdFmt.format( new Date( file.lastModified() ) ));
		
		sb.append( "\n" );
		
		return sb;
	}


	/**
	 * File names able to be deleted:
	 *   *.bu
	 *   *.tmp
	 *   _archived_*
	 *   
	 *   Backups of minebombs need to be changed... was changed to have a .bu suffix.
	 *   
	 * @param folder
	 */
	private void gatherFiles( File folder ) {
		
		File[] files = folder.listFiles();
		
		for ( File file : files ) {
			
			
			if ( file.isDirectory() ) {
				
				gatherFiles( file );
			}

			else if ( file.isFile() ) {

				String fName = file.getName();
				
				boolean isDeleteable = 
						fName.endsWith( CoreCacheFiles.FILE_SUFFIX_BACKUP ) ||
						fName.endsWith( CoreCacheFiles.FILE_SUFFIX_TEMP ) ||
						fName.endsWith( ".del" ) ||
						fName.startsWith( "_archived_" ) ||
						fName.contains( ".json.ver_" ) && fName.endsWith( ".txt" )
						;

				if ( folder.equals( getBackupDirectoryFile() ) ) {
					this.filesBackups.add( file );
				}
				else {
					this.filesToBackup.add( file );
					
					if ( isDeleteable ) {
						this.filesToDelete.add( file );
					}
				}

				
			}
		}
	}
	
	/**
	 * <p>This function will take the project's data folder and construct the the path
	 * to the directory, if it does not exist, to where the backup files are stored.
	 * </p>
	 * 
	 * @return
	 */
	public File getBackupDirectoryFile() {
		if ( backupDirectory == null ) {
			
			backupDirectory = new File( Prison.get().getDataFolder(), FILE_BACKUP_DIRECTORY_PATH ); 
			backupDirectory.mkdirs();
		}
			
		return backupDirectory;
	}
	
	
	public File getNewBackupFile( BackupTypes backupType, String fileNameNotes ) {
		String notes = null;
		
		if ( fileNameNotes != null && fileNameNotes.trim().length() > 0 ) {
			notes = "_" + fileNameNotes.replaceAll( "[^a-zA-Z0-9\\.\\-]", "_" );
			
			// Notes can only be a max length of 20 characters
			if ( notes.length() > 20 ) {
				notes = notes.substring( 0, 20 );
			}
		}

		if ( backupType != null && backupType == BackupTypes.manual && 
				notes != null && notes.length() > 0) {
			backupType = null;
		}
		
		String prisonVersion = Prison.get().getPlatform().getPluginVersion();
		
//		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd_hh-mm" );
		
		String fileName = "prison_" + sdFmt.format( new Date() ) + 
				"_v" + prisonVersion +
				( backupType == null ? "" : "_" + backupType.name()) + 
				( notes == null ? "" : notes ) +
				".zip";
		
		File file = new File( getBackupDirectoryFile(), fileName );
		
		return file;
	}


	public long getStartTimeNanos() {
		return startTimeNanos;
	}
	public void setStartTimeNanos(long startTimeNanos) {
		this.startTimeNanos = startTimeNanos;
	}

	public Date getBackupStartDate() {
		return backupStartDate;
	}
	public void setBackupStartDate(Date backupStartDate) {
		this.backupStartDate = backupStartDate;
	}

	public String getZipFilePrefix() {
		return zipFilePrefix;
	}
	public void setZipFilePrefix(String zipFilePrefix) {
		this.zipFilePrefix = zipFilePrefix;
	}

	public Path getSourceDirectoryPath() {
		if ( sourceDirectoryPath == null ) {
			sourceDirectoryPath = Prison.get().getDataFolder().toPath();
		}
		return sourceDirectoryPath;
	}


	public File getBackupDirectory() {
		return backupDirectory;
	}
	public void setBackupDirectory(File backupDirectory) {
		this.backupDirectory = backupDirectory;
	}

	public ArrayList<File> getFilesBackups() {
		return filesBackups;
	}
	public void setFilesBackups(ArrayList<File> filesBackups) {
		this.filesBackups = filesBackups;
	}

	public ArrayList<File> getFilesToBackup() {
		return filesToBackup;
	}
	public void setFilesToBackup(ArrayList<File> filesToBackup) {
		this.filesToBackup = filesToBackup;
	}

	public ArrayList<File> getFilesToDelete() {
		return filesToDelete;
	}
	public void setFilesToDelete(ArrayList<File> filesToDelete) {
		this.filesToDelete = filesToDelete;
	}

	public ArrayList<File> getFilesWithErrors() {
		return filesWithErrors;
	}
	public void setFilesWithErrors(ArrayList<File> filesWithErrors) {
		this.filesWithErrors = filesWithErrors;
	}

}

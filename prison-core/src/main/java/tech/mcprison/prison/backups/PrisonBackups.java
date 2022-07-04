package tech.mcprison.prison.backups;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cache.PlayerCacheFiles;
import tech.mcprison.prison.file.ZipFileIO;
import tech.mcprison.prison.output.Output;

public class PrisonBackups {

	public static final String FILE_BACKUP_DIRECTORY_PATH = "backups";
	
	private File backupDirectory = null;
	
	
	private ArrayList<File> filesBackups;
	private ArrayList<File> filesToBackup;
	private ArrayList<File> filesToDelete;
	private ArrayList<File> filesWithErrors;
	
	
	public enum BackupTypes {
		upgrade,
		auto,
		manual
	}
	
	public PrisonBackups() {
		super();
		
		this.filesBackups = new ArrayList<>();
		this.filesToBackup = new ArrayList<>();
		this.filesToDelete = new ArrayList<>();
		this.filesWithErrors = new ArrayList<>();
		
	}
	
	
	public String startBackup( BackupTypes backupType, String notes ) {
		
		// Reset collections:
		this.filesBackups.clear();
		this.filesToBackup.clear();
		this.filesToDelete.clear();
		this.filesWithErrors.clear();
		
		File zipFile = getNewBackupFile( backupType, notes );
		
		// Gather all files:
		gatherFiles( Prison.get().getDataFolder() );
		
		
		// The files in the zip file needs to be placed in a directory:
		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd_hh-mm" );
		String zipFilePrefix = "backup_" + sdFmt.format( new Date() );
		
		// Save to zip file:
		ZipFileIO zipIo = new ZipFileIO();
		
		zipIo.writeToZipFileBackups( Prison.get().getDataFolder().toPath(), 
				zipFile, 
				zipFilePrefix,
				filesToBackup, filesWithErrors );
		
		
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
		
		DecimalFormat nFmt = new DecimalFormat( "#,###.000" );
		
		String message = String.format( 
				"Backup status: %s  %s KB   files: %d   temp files purged: %d   errors: %d",
				zipFile.getAbsolutePath(),
				nFmt.format( sizeKb ),
				filesBackups.size(),
				filesToDelete.size(),
				filesWithErrors.size()
				
				);
		
		return message;
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
						fName.endsWith( PlayerCacheFiles.FILE_SUFFIX_BACKUP ) ||
						fName.endsWith( PlayerCacheFiles.FILE_SUFFIX_TEMP ) ||
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
		
		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd_hh-mm" );
		
		String fileName = "prison_" + sdFmt.format( new Date() ) + 
				"_v" + prisonVersion +
				( backupType == null ? "" : "_" + backupType.name()) + 
				( notes == null ? "" : notes ) +
				".zip";
		
		File file = new File( getBackupDirectoryFile(), fileName );
		
		return file;
	}
	
	
}

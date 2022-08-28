package tech.mcprison.prison.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import tech.mcprison.prison.backups.PrisonBackups;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Text;

public class ZipFileIO {

	
	public void writeToZipFileBackups( 
			File zipFile, 
			PrisonBackups pBackups ) {
		
		try ( 
				final ZipOutputStream out = new ZipOutputStream(new FileOutputStream( zipFile ));
				) {
			
			for (File file : pBackups.getFilesToBackup() ) {
				
				try {
					Path targetFile = pBackups.getSourceDirectoryPath().relativize( file.toPath() );
					
					String zipEntryName =  pBackups.getZipFilePrefix() + "/" + targetFile.toString();
					
					ZipEntry zEntry = new ZipEntry( zipEntryName );
					out.putNextEntry( zEntry );
					
					
					byte[] bytes = Files.readAllBytes(file.toPath());
					out.write(bytes, 0, bytes.length);
					out.closeEntry();
				} 
				catch (IOException e) {
					
					pBackups.getFilesWithErrors().add( file );
					
					e.printStackTrace();
				}
				
			}
			
//			// Add a stats file at the root with backup stats:
			try {
				String statsFileName = pBackups.getZipFilePrefix() + "_stats.txt";
				
				ZipEntry zEntry = new ZipEntry( statsFileName );
				out.putNextEntry( zEntry );

				// Basic backup stats:
				writeOutput( out, pBackups.backupReport01() );
				
				// Prison version:
				writeOutput( out, pBackups.backupReportVersionData().toString() );
				
				// List of backup files that had errors or are temporary and will be removed:
				writeOutput( out, pBackups.backupReportListTemporalFiles() );
				
				
				writeOutput( out, pBackups.backupReportConfigsData().toString() );
				
				writeOutput( out, pBackups.backupReportRanksData().toString() );
				
				writeOutput( out, pBackups.backupReportMinesData().toString() );
				
				writeOutput( out, pBackups.backupReportListenersData().toString() );

				
				out.closeEntry();
			} 
			catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		catch ( Exception e ) {
			
			String message = String.format( 
					"Error trying to build Prison backup file: %s  [%s]", 
						zipFile.getAbsolutePath(), 
						e.getMessage()
					);
			
			Output.get().logWarn( message, e );
		}
		

	}
	
	private void writeOutput(ZipOutputStream out, String message) {

		byte[] bytes = Text.stripColor( message ).getBytes();

		try {
			out.write(bytes, 0, bytes.length);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}

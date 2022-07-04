package tech.mcprison.prison.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import tech.mcprison.prison.output.Output;

public class ZipFileIO {

	
	public void writeToZipFileBackups( 
			Path sourceDirectoryPath, File zipFile, 
			String zipPathPrefix,
			ArrayList<File> filesToBackup,
			ArrayList<File> filesWithErrors ) {
		
		
		try ( 
				final ZipOutputStream out = new ZipOutputStream(new FileOutputStream( zipFile ));
				) {
			
			for (File file : filesToBackup) {
				
				try {
					Path targetFile = sourceDirectoryPath.relativize( file.toPath() );
					
					String zipEntryName = zipPathPrefix + "/" + targetFile.toString();
					
					ZipEntry zEntry = new ZipEntry( zipEntryName );
					out.putNextEntry( zEntry );
					
					
					byte[] bytes = Files.readAllBytes(file.toPath());
					out.write(bytes, 0, bytes.length);
					out.closeEntry();
				} 
				catch (IOException e) {
					
					filesWithErrors.add( file );
					
					e.printStackTrace();
				}
				
			}
			
//			// Add a stats file at the root with backup stats:
//			try {
//				String statsFileName = zipPathPrefix + "_backup_stats.txt";
//				
//				ZipEntry zEntry = new ZipEntry( statsFileName );
//				out.putNextEntry( zEntry );
//				
//				
//				byte[] bytes = ;
//
//				out.write(bytes, 0, bytes.length);
//				out.closeEntry();
//			} 
//			catch (IOException e) {
//				
//				filesWithErrors.add( file );
//				
//				e.printStackTrace();
//			}
			
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
	
}

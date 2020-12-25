package tech.mcprison.prison.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import tech.mcprison.prison.error.Error;
import tech.mcprison.prison.error.ErrorManager;
import tech.mcprison.prison.modules.ModuleStatus;
import tech.mcprison.prison.output.Output;

public abstract class FileIO
	extends FileVirtualDelete
{

	private final SimpleDateFormat sdf;
	
	private final ErrorManager errorManager;
	private final ModuleStatus status;

	public FileIO()
	{
		this(null, null);
	}
	
	public FileIO(ErrorManager errorManager, ModuleStatus status)
	{
		super();
	
		this.errorManager = errorManager;
		this.status = status;

		this.sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	}

	
	protected void saveFile( File file, String data ) 
	{
		if ( file != null && data != null )
		{
			String tempFileName = file.getName() + "." + getTimestampFormat() + ".tmp";
			File tempFile = new File(file.getParentFile(), tempFileName);
			
			try
			{
				// Write as a .tmp file:
				
				// Add json data to lines, splitting on \n:
				List<String> lines = Arrays.asList( data.split( "\n" ));
				
				// Write as an UTF-8 stream:
				Files.write( tempFile.toPath(), lines, StandardCharsets.UTF_8 );
				
//				Files.write( tempFile.toPath(), data.getBytes() );
				
				// If original target exists, then delete it:
				if ( file.exists() )
				{
					file.delete();
				}
				
				tempFile.renameTo( file );
			}
			catch ( IOException e )
			{
				logException( "Failed to create file", file, e );
			}
			
		}
	}

	protected String readFile( File file )
	{
		StringBuilder results = new StringBuilder();
//		String results = null;
		
		try
		{
			List<String> lines = Files.readAllLines( file.toPath(), StandardCharsets.UTF_8 );

			for ( String line : lines ) {
				results.append( line ).append( "\n" );
			}
			
//			byte[] bytes = Files.readAllBytes( file.toPath() );
//			results = new String(bytes);
		}
		catch ( IOException e )
		{
			logException( "Failed to load file", file, e );
		}
		
		return results.toString();
	}

	private void logException( String description, File file, IOException e )
	{
		String message = description + " " + file.getAbsolutePath();
		
		if ( getStatus() != null )
		{
			getStatus().toFailed(message);
		}
		
		if ( getErrorManager() != null )
		{
			getErrorManager().throwError(
					new Error(message).appendStackTrace("Additional info:", e));
		}
		else
		{
			Output.get().logError(message, e);
		}
	}
	
	private String getTimestampFormat()
	{
		return sdf.format( new Date() );
	}
	
	public ErrorManager getErrorManager()
	{
		return errorManager;
	}

	public ModuleStatus getStatus()
	{
		return status;
	}

}

package tech.mcprison.prison.file;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tech.mcprison.prison.error.ErrorManager;
import tech.mcprison.prison.modules.ModuleStatus;
import tech.mcprison.prison.output.Output;

public class JsonFileIO
		extends FileIO
{
	private final Gson gson;
	
	public JsonFileIO(ErrorManager errorManager, ModuleStatus status)
	{
		super(errorManager, status);
		
		this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	}

	public Gson getGson()
	{
		return gson;
	}
	
	/**
	 * This function will save a file as a JSON format.  It will first save it as a
	 * temp file to make sure the data can be written to the file system, then once
	 * it is successful, it will then delete the original target and rename the temp
	 * file.  This helps to ensure that data will not be lost if something goes wrong:
	 * either the original file will remain, or the new file will saved under the 
	 * *.tmp suffix, or both.  Should not reach a condition where both files disappear.
	 * 
	 * @param file
	 * @param data
	 */
	public void saveJsonFile( File file, FileIOData data ) 
	{
		if ( file != null && data != null )
		{
			String json = getGson().toJson( data );

			saveFile( file, json );	
		}
	}
	
	/**
	 * This function will try to load the given file, of which the contents should be 
	 * JSon.  If it is successful then the resulting object will represent the file.
	 * Otherwise the resulting object will be the data object that is passed to this
	 * function, which should be empty or however the new object should be structured.
	 * 
	 * @param file The source file that will be loaded. Should be JSON data within.
	 * @param data This is the "new" object that is to be returned from this function if the source
	 * 				file cannot be read successfully.
	 * @return
	 */
	public FileIOData readJsonFile( File file, FileIOData data )
	{
		FileIOData results = data;
		
		String json = super.readFile( file );
		
		if ( json != null )
		{
			try
			{
				results = getGson().fromJson( json, data.getClass() );
			}
			catch ( Exception e ) {
				
				String message = String.format( 
						"JsonFileIO.readJsonFile: JsonParse failure: file: [%s] " +
						"error: [%s]  json: [%s] ", 
						file.getAbsoluteFile(), e.getMessage(), 
						json );
				
				Output.get().logError( message );

				// e.printStackTrace();
			}
		}
		
		return results;
	}
}

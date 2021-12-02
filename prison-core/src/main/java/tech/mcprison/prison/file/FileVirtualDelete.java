/**
 * 
 */
package tech.mcprison.prison.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.io.Files;

import tech.mcprison.prison.output.Output;

/**
 *
 */
public abstract class FileVirtualDelete
{
	public static final String FILE_LOGICAL_DELETE_PREFIX = ".deleted_";
	public static final String FILE_LOGICAL_BACKUP_PREFIX = ".backup";
	
	public FileVirtualDelete()
	{
		super();
	}
	
    /**
     * <p>This function will rename a source with a prefix of <code>.deleted_</code> and a 
     * suffix of <code>_</code><i>timestamp</i><code>.del</code>.  The prefix is used to logically delete
     * resources since it would be rather rare that a user would delete a whole 
     * sub-storage system.  This provides the ability to manually undo a mistake.
     * </p>
     * 
     * @param source
     * @return
     */
    protected boolean virtualDelete( File source )
    {
        // It would be rare to delete a database.  Instead just rename it to deleted incase it needs to be recovered:
        SimpleDateFormat sdf = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
        String name = FILE_LOGICAL_DELETE_PREFIX + source.getName() + sdf.format( new Date() ) + ".del";
        File newName = new File( source.getParentFile(), name);
        return source.renameTo( newName );
    }
    
    /**
     * <p>This function will make a backup of a file source.  It's included in this virtual delete
     * class since it's very similar and by changing the isDeleted() function, the backups can
     * also be ignored.
     * </p>
     * 
     * @param source
     * @return File name of the backup file.
     */
    protected File virtualBackup( File source )
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
    	String name = FILE_LOGICAL_BACKUP_PREFIX + source.getName() + sdf.format( new Date() ) + ".del";
    	File backupFile = new File( source.getParentFile(), name);
    	
    	try {
			Files.copy( source, backupFile );
		}
		catch ( IOException e )
		{
			Output.get().logError( 
					String.format( 
							"Could not create a backup. SourceFile: %s  BackupFile: %s Error: [%s]",
							source.getAbsolutePath(), backupFile.getAbsolutePath(), e.getMessage() ));
			e.printStackTrace();
		}
    	
    	return backupFile;
    }
    
    /**
     * <p>This function will return a boolean value to indicate if it has been logically 
     * deleted.  It will ONLY inspect the beginning of the file name which much have
     * a prefix of <code>.deleted_</code>.  The trailing timestamp is ignored.
     * </p>
     * 
     * @param source
     * @return
     */
    protected boolean isDeleted( File source )
    {
    	return 
    			source.getName().toLowerCase().startsWith( FILE_LOGICAL_DELETE_PREFIX ) ||
		    	source.getName().toLowerCase().startsWith( FILE_LOGICAL_BACKUP_PREFIX );
    }
	
}

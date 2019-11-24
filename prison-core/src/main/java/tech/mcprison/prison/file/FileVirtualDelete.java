/**
 * 
 */
package tech.mcprison.prison.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public abstract class FileVirtualDelete
{
	public static final String FILE_LOGICAL_DELETE_PREFIX = ".deleted_";
	
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
    	return source.getName().toLowerCase().startsWith( FILE_LOGICAL_DELETE_PREFIX );
    }
	
}

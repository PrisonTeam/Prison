package tech.mcprison.prison.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Database;
import tech.mcprison.prison.store.Storage;

/**
 * 
 * @author Faizaan A. Datoo
 */
public class FileStorage 
	extends FileVirtualDelete
	implements Storage 
{
    private File rootDir;
    private Map<String, Database> databaseMap;

    /**
     * <p>This constructor sets up the root directory to use and instantiates the
     * databaseMap.  It then loads the databaseMap with all the related FileDatabases
     * that are within this structure.
     * </p>
     * 
     * @param rootDir
     */
    public FileStorage(File rootDir) {
        this.rootDir = rootDir;
        this.databaseMap = new HashMap<>();

        refresh();
    }
    
    /**
     * <p>Refresh is a function that will clear the databaseMap collection if it contains anything,
     * then load all possible FileDatabase objects, ignoring those that been virtually deleted.
     * </p>
     * 
     *  <p>It will print an info message to the system console listing all of the FileDatabase
     *  directories that have been logically/virtually deleted.
     *  </p>
     */
    public void refresh() {
    	databaseMap.clear();
    	
    	// Each folder in the root directory is its own database.
    	// We'll initialize each of them here.
    	File[] databaseFiles = this.rootDir.listFiles(File::isDirectory);
    	if (databaseFiles != null) {
    		for (File dbFile : databaseFiles) {
    			if ( isDeleted( dbFile ) ) {
    				String message = "FileStorage.refresh skipping logically deleted FileDatabase: " + 
    							dbFile.getAbsolutePath();
    				Output.get().logInfo( message );
    			} else {
    				databaseMap.put(dbFile.getName(), new FileDatabase(dbFile));
    				
    			}
    		}
    	}
    }


	/**
     * <p>Confirm if the FileStorage is "connected" by confirming that the root directory exists.
     * </p>
     * 
     * @return The FileStorage system is operational
     */
    @Override 
    public boolean isConnected() {
        return rootDir.exists();
    }

    /**
     * If it the database does not exist, then create it.
     * 
     * @param name
     * @return The FileDatabase wrapped in an Optional
     */
    @Override 
    public Optional<Database> getDatabase(String name) {
    	Database results = databaseMap.get(name);
    	
    	if ( results == null ) 
    	{
    		// try to create the FileDatabase:
    		createDatabase(name);
    		results = databaseMap.get(name);
    	}
    	
        return Optional.ofNullable(results);
    }

    /**
     * <p>This function will create a new FileDatabase on the file system (a directory).
     * It will generate the new directory with the provided name.  If there is already
     * a directory by that name, then this function will fail and it will log a
     * warning.  If successful, then it will add a FileDatabase entry to the 
     * databaseMap.
     * </p>
     * 
     * @param name
     * @return if successfully created the FileDatabase
     */
    @Override 
    public boolean createDatabase(String name) {
    	boolean results = false;
    	
        File directory = new File(rootDir, name);
        if (!directory.exists()) {
        	results = directory.mkdir();
        	databaseMap.put(name, new FileDatabase(directory));
        } else {
        	String message = "The attempt to create a new FileDatabase named " + name + 
        			" failed because a directory on the file system already exists by that name.";
        	Output.get().logWarn( message );
        }
        return results;
    }

    /**
     * <p>This function will perform a logical delete on a FileDatabase (a folder).
     * The actual directory has to exist on the file system and has to have an
     * entry within the databaseMap before the attempt is made.
     * </p>
     * 
     * <p>Upon deleting, the reference within the databaseMap will be removed and the
     * directory will be renamed with a prefix of <code>.deleted_</code> and a 
     * suffix of <code>_</code><i>timestamp</i>.
     * </p>
     * 
     * <p>If the user changes their mind, then they need to rename the folder on the 
     * file system manually, then restart the server.  It is not recommended to do a /reload, 
     * although that may work.
     * </p>
     * 
     * <p>Note: There really is no way for the user to ever delete a FileDatabase from
     * what I can tell. lol </p>
     * 
     * @param name
     * @return boolean value indicating if the delete was successful
     */
    @Override
    public boolean deleteDatabase(String name) {
    	boolean results = false;
    	
        File directory = new File(rootDir, name);
        Database db = databaseMap.get(name);

        if (directory.exists() && db != null) {
        	// Perform a logical delete on the database so it can be manually recovered if this is an error:
        	virtualDelete( directory );
        	
        	// This dispose just removes the entries from the collection and deletes nothing from the file system:
        	db.dispose();
        	//directory.delete();
        	databaseMap.remove(name);
        	results = true;
        } else {
        	String message = "The attempt to delete a FileDatabase named " + name + 
        			" failed because either the directory does not exist or it was not in the databaseMap.";
        	Output.get().logWarn( message );
        }
        
        return results;
    }

    /**
     * <p>This function returns a List of FileDatabase values.  It does not return the
     * keys.
     * </p>
     * 
     * @return A List of FileDatabase values
     */
    @Override 
    public List<Database> getDatabases() {
        return new ArrayList<>(databaseMap.values());
    }
}

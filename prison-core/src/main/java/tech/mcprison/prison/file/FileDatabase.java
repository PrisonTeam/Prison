package tech.mcprison.prison.file;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Database;

import java.io.File;
import java.util.*;

/**
 * @author Faizaan A. Datoo
 */
public class FileDatabase 
	extends FileVirtualDelete
	implements Database 
{

    private File dbDir;
    private Map<String, Collection> collectionMap;

    public FileDatabase(File dbDir) {
        this.dbDir = dbDir;
        this.collectionMap = new HashMap<>();

        refresh();
    }
    
    /**
     * <p>Refresh is a function that will clear the collectionMap collection if it contains anything,
     * then load all possible FileCollection objects, ignoring those that been virtually deleted.
     * </p>
     * 
     *  <p>It will print an info message to the system console listing all of the FileCollection
     *  directories that have been logically/virtually deleted.
     *  </p>
     */
    public void refresh() {
    	collectionMap.clear();
    	
    	// Each folder in the db directory is its own collection.
    	// We'll initialize each of them here.
    	File[] collectionDirs = dbDir.listFiles(File::isDirectory);
    	if (collectionDirs != null) {
    		for (File collDir : collectionDirs) {
    			if ( isDeleted( collDir ) ) {
    				String message = "FileDatabase.refresh skipping logically deleted FileCollection: " + 
    						collDir.getAbsolutePath();
    				Output.get().logInfo( message );
    			} else {
    				collectionMap.put(collDir.getName(), new FileCollection(collDir));
    			}
    		}
    	}
    	
    }

    /**
     * If the collection does not exist, then create it.
     * 
     * @param name
     * @return The FileCollection wrapped in an Optional
     */
    @Override 
    public Optional<Collection> getCollection(String name) 
    {
    	Collection results = collectionMap.get(name);
    	
    	if ( results == null )
    	{
    		// try to create the FileCollection:
    		createCollection(name);
    		results = collectionMap.get(name);
    	}
    	
        return Optional.ofNullable(results);
    }

    /**
     * <p>This function will create a new FileCollection on the file system (a directory).
     * It will generate the new directory with the provided name.  If there is already
     * a directory by that name, then this function will fail and it will log a
     * warning.  If successful, then it will add a File entry Collection to the 
     * collectionMap.
     * </p>
     * 
     * @param name
     * @return if successfully created the FileCollection
     */
    @Override 
    public boolean createCollection(String name) {
    	boolean results = false;
    	
        File collDir = new File(dbDir, name);
        if (!collDir.exists()) {
        	results = collDir.mkdir();
        	collectionMap.put(name, new FileCollection(collDir));
        } else {
        	String message = "The attempt to create a new FileCollection named " + name + 
        			" failed because a directory on the file system already exists by that name.";
        	Output.get().logWarn( message );
        }
        
        return results;
    }

    /**
     * <p>This function will perform a logical delete on a FileCollection (a folder).
     * The actual directory has to exist on the file system and has to have an
     * entry within the collectionMap before the attempt is made.
     * </p>
     * 
     * <p>Upon deleting, the reference within the collectionMap will be removed and the
     * directory will be renamed with a prefix of <code>.deleted_</code> and a 
     * suffix of <code>_</code><i>timestamp</i>.
     * </p>
     * 
     * <p>If the user changes their mind, then they need to rename the folder on the 
     * file system manually, then restart the server.  It is not recommended to do a /reload, 
     * although that may work.
     * </p>
     * 
     * <p>Note: There really is no way for the user to ever delete a FileCollection from
     * what I can tell. lol </p>
     * 
     * @param name
     * @return boolean value indicating if the delete was successful
     */
    @Override 
    public boolean deleteCollection(String name) {
    	boolean results = false;
    	
        File collDir = new File(dbDir, name);
        Collection coll = collectionMap.get(name);

        if (collDir.exists() && coll != null) {
        	// Perform a logical delete on the collection so it can be manually recovered if this is an error:
        	virtualDelete( collDir );
        	
        	// This dispose just removes the entries from the collection and deletes nothing from the file system:
        	//coll.dispose();
        	//results = collDir.delete();
        	collectionMap.remove(name);
        	results = true;
        } else {
        	String message = "The attempt to delete a FileCollection named " + name + 
        			" failed because either the directory does not exist or it was not in the collectionMap.";
        	Output.get().logWarn( message );
        }

        return results;
    }

    /**
     * <p>This function returns a List of collectionMap values.  It does not return the
     * keys.
     * </p>
     * 
     * @return A List of FileCollection values
     */
    @Override public List<Collection> getCollections() {
        return new ArrayList<>(collectionMap.values());
    }

    @Override public String getName() {
        return dbDir.getName();
    }

    @Override public void dispose() {
        collectionMap.clear();
    }

}

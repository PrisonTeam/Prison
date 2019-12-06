package tech.mcprison.prison.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

/**
 * <p>This makes no sense to cache anything.  Let the operations performed at this level 
 * be live against the file system to keep this simple, improve performance, and to 
 * reduce the amount of memory used.
 * </p>
 */
public class FileCollection 
	extends JsonFileIO
	implements Collection 
{
    private File collDir;
    
    public FileCollection(File collDir) {
    	// This may be within a module. If so then pass these values...
    	super(null, null);
    	
        this.collDir = collDir;
    }
    
    /**
     * <p>This is the name of the collection.</p>
     * 
     * @return the collection's name
     */
    @Override 
    public String getName() {
        return collDir.getName();
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
    @Override
    public List<Document> getAll() {
    	List<Document> allDocs = new ArrayList<>();
    	
    	// Each folder in the root directory is its own database.
    	// We'll initialize each of them here.
    	File[] collectionFiles = this.collDir.listFiles((dir, name) -> name.endsWith(".json"));
    	if (collectionFiles != null) {
    		for (File dbFile : collectionFiles) {
    			if ( isDeleted( dbFile ) ) {
    				String message = "FileCollection.getAll skipping logically deleted FileDocument: " + 
    							dbFile.getAbsolutePath();
    				Output.get().logInfo( message );
    			} else {
    				Document doc = (Document) readJsonFile(dbFile, new Document());
    				if ( doc != null )
    				{
    					allDocs.add( doc );
    				}
    			}
    		}
    	}
    	
    	return allDocs;
    }
    

    @Override 
    public Optional<Document> get(String key) {
    	File dbFile = new File(collDir, key + ".json");
    	Document doc = (Document) readJsonFile(dbFile, new Document());
    	
        return Optional.ofNullable(doc);
    }
    
    @Override 
    public void save(Document document)
    {
    	save((String)document.get("name"), document);
    }
    
    @Override 
    public void save(String filename, Document document)
    {
    	File dbFile = new File(collDir, filename + ".json");
    	saveJsonFile( dbFile, document );
    }
    
    @Override 
    public boolean delete(String name)
    {
    	File dbFile = new File(collDir, name + ".json");
    	return virtualDelete( dbFile );
    }

}

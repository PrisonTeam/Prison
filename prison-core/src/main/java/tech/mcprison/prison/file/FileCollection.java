package tech.mcprison.prison.file;

import java.io.File;
import java.io.FileFilter;
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

    public File getCollDir() {
		return collDir;
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
    	
    	FileFilter fFilter = JsonFileIO.getPrisonFileFilter();
    	
    	
    	// Each folder in the root directory is its own database.
    	// We'll initialize each of them here.
    	File[] collectionFiles = this.collDir.listFiles( fFilter );
//    	File[] collectionFiles = this.collDir.listFiles((dir, name) -> name.endsWith(".json"));
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
    	
    	File dbFile = getFile(key);
    	Document doc = (Document) readJsonFile(dbFile, new Document());
    	
        return Optional.ofNullable(doc);
    }
    
//    @Override 
//    public void save(Document document)
//    {
//    	save((String)document.get("name"), document);
//    }
    
//    @Override 
//    public void save(String filename, Document document)
//    {
//    	String suffix = filename.endsWith(FILE_SUFFIX_JSON) ? "" : FILE_SUFFIX_JSON;
//    	File dbFile = new File(collDir, filename + suffix);
//    	saveJsonFile( dbFile, document );
//    }
    

	@Override
	public void save(String filename, Document document, 
				String oldFilename, String fileType ) {
		
		File dbFile = getFile(filename);
    	saveJsonFile( dbFile, document );

    	if ( oldFilename != null ) {
    		
    		// Since the new file should have been saved by now...
    		File oldDbFile = getFile(oldFilename);
    		
    		// If both the new file and old file exists, then need to remove the old file:
    		if ( dbFile.exists() && dbFile.length() > 0 && oldDbFile.exists() ) {
    			
    			boolean deleted = oldDbFile.delete();
    			
    			if ( deleted ) {
    				
    				Output.get().logInfo( 
    						"&3%s File Converted: &7%s &3--> &7%s",
    								fileType,
    								oldFilename + FILE_SUFFIX_JSON, 
    								filename + FILE_SUFFIX_JSON
    						);
    			}
    			else {
    				Output.get().logInfo( 
    						"&3The old %s file could not be removed: "
    						+ "Old file &7%s &3. " +
    								"Reason unknown (check logs?). "
    								+ "New file name &7%s. [%s]",
    								fileType,
    								oldFilename + FILE_SUFFIX_JSON, 
    								filename + FILE_SUFFIX_JSON,
    								oldDbFile.getAbsolutePath() 
    						);
    				
    			}
    		}
    	}
	}
    
	
	private File getFile(String name) 
	{
		String suffix = name.endsWith(FILE_SUFFIX_JSON) ? "" : FILE_SUFFIX_JSON;
		File dbFile = new File(collDir, name + suffix);
		return dbFile;
	}
	
	@Override
	public boolean exists(String name) 
	{
		boolean results = false;
		
		if ( name != null && name.trim().length() > 0 ) {
			
			File dbFile = getFile(name);
			results = dbFile.exists();
		}
		
		return results;
	}
	
    @Override 
    public boolean delete(String name)
    {
    	File dbFile = getFile(name);
    	return dbFile.exists() ? virtualDelete( dbFile ) : false;
    }
    
    @Override 
    public File backup( String name )
    {
    	File dbFile = getFile(name);
    	File backupFile = dbFile.exists() ? virtualBackup( dbFile ) : null;
    	
    	return backupFile;
    }


}

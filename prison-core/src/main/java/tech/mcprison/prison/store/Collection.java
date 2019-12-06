package tech.mcprison.prison.store;

import java.util.List;
import java.util.Optional;

/**
 * Represents a collection of similar documents.
 * For example, if you have documents for each type of fruit, you'd put them in a "fruit" collection.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public interface Collection {

    /**
     * @return The name of this collection
     */
    public String getName();

    /**
     * @return Returns a list of all documents in this collection.
     */
    public List<Document> getAll();

    /**
     * Attempts to retrieve a document from the collection.
     *
     * @param key The name of the document to retrieve.
     * @return An optional containing the document if it was found, or an empty optional if it does not exist.
     */
    public Optional<Document> get(String key);

    
    public void save(Document document);
    public void save(String filename, Document document);
    
    
    public boolean delete(String name);
    
}

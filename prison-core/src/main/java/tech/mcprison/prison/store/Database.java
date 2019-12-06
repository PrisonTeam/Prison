package tech.mcprison.prison.store;

import java.util.List;
import java.util.Optional;

/**
 * Represents a database in the storage system.
 * Databases can contain multiple collections and many documents.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Database {

    /**
     * Attempts to retrieve a collection from the database.
     *
     * @param name The name of the collection.
     * @return An optional containing the collection if it could be found, or an empty optional if it doesn't exist.
     */
    public Optional<Collection> getCollection(String name);

    /**
     * Create a new collection. If a collection with the provided name already exists, this method will do nothing.
     *
     * @param name The name of the collection.
     */
    public boolean createCollection(String name);

    /**
     * Deletes a collection. If a collection with the provided name does not exist, this method will do nothing.
     *
     * @param name The name of the collection.
     */
    public boolean deleteCollection(String name);

    /**
     * @return Returns a list of all the collections in this database.
     */
    public List<Collection> getCollections();

    /**
     * @return Returns the name of this database.
     */
    public String getName();

    /**
     * Dispose of the cached data in this database. This does not remove any files.
     */
    public void dispose();
}

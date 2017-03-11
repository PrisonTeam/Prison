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
     * @param name The name of the collection.
     * @return An optional containing the collection if it could be found, or an empty optional if it doesn't exist.
     */
    Optional<Collection> getCollection(String name);

    /**
     * Create a new collection. If a collection with the provided name already exists, this method will do nothing.
     * @param name The name of the collection.
     */
    void createCollection(String name);

    /**
     * Deletes a collection. If a collection with the provided name does not exist, this method will do nothing.
     * @param name The name of the collection.
     */
    void deleteCollection(String name);

    /**
     * @return Returns a list of all the collections in this database.
     */
    List<Collection> getCollections();

    /**
     * @return Returns the name of this database.
     */
    String getName();

}

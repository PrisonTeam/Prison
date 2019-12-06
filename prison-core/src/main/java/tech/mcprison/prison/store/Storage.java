package tech.mcprison.prison.store;

import java.util.List;
import java.util.Optional;

/**
 * Access the storage API.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public interface Storage {

    /**
     * @return true if the storage backend is up and running, and false if something went wrong. If this
     * is false, it is probably not safe to attempt to read/write data.
     */
    public boolean isConnected();

    /**
     * Attempts to retrieve a database from the storage system.
     *
     * @param name The name of the database to retrieve.
     * @return An optional containing the database if it was found, or an empty optional if it doesn't exist.
     */
    public Optional<Database> getDatabase(String name);

    /**
     * Create a new database. If a database exists by the provided name,
     * this method will do nothing.
     *
     * @param name The name of the new database.
     * @return If create was successful
     */
    public boolean createDatabase(String name);

    /**
     * Deletes a database. If no database exists by the provided name,
     * this method will do nothing.
     *
     * @param name The name of the database to delete.
     * @return If the delete was successful
     */
    public boolean deleteDatabase(String name);

    /**
     * @return A list of all databases in the storage system.
     */
    public List<Database> getDatabases();

}

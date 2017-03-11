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
    String getName();

    /**
     * Attempts to retrieve a document from the collection.
     *
     * @param key The name of the document to retrieve.
     * @return An optional containing the document if it was found, or an empty optional if it does not exist.
     */
    Optional<Document> get(String key);

    /**
     * Inserts a new document into the collection.
     *
     * @param key      The name of the document to insert.
     * @param document The document to insert.
     */
    void insert(String key, Document document);

    /**
     * Remove a document from the collection.
     *
     * @param key The name of the document to remove.
     */
    void remove(String key);

    /**
     * Filters through each document in the collection and attempts to find a match based on whatever is stored
     * in the passed in document.
     *
     * @param document A document containing everything the filter matches should contain.
     * @return A list of matching documents. May be empty, but will never be null.
     */
    List<Document> filter(Document document);

    /**
     * @return Returns a list of all documents in this collection.
     */
    List<Document> getAll();

}

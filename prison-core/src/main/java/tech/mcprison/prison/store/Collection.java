/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

    /**
     * Disposes of all cached data in this collection. Note that this does not remove any files.
     */
    void dispose();
}

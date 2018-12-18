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
    Optional<Collection> getCollection(String name);

    /**
     * Create a new collection. If a collection with the provided name already exists, this method will do nothing.
     *
     * @param name The name of the collection.
     */
    void createCollection(String name);

    /**
     * Deletes a collection. If a collection with the provided name does not exist, this method will do nothing.
     *
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

    /**
     * Dispose of the cached data in this database. This does not remove any files.
     */
    void dispose();
}

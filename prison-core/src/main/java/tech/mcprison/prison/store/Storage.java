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
    boolean isConnected();

    /**
     * Attempts to retrieve a database from the storage system.
     *
     * @param name The name of the database to retrieve.
     * @return An optional containing the database if it was found, or an empty optional if it doesn't exist.
     */
    Optional<Database> getDatabase(String name);

    /**
     * Create a new database. If a database exists by the provided name,
     * this method will do nothing.
     *
     * @param name The name of the new database.
     */
    void createDatabase(String name);

    /**
     * Deletes a database. If no database exists by the provided nmae,
     * this method will do nothing.
     *
     * @param name The name of the database to delete.
     */
    void deleteDatabase(String name);

    /**
     * @return A list of all databases in the storage system.
     */
    List<Database> getDatabases();

}

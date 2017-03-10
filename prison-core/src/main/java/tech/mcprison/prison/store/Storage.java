/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.store;

import java.util.List;

/**
 * A storage adapter, implemented on a per-platform level. Use this to access and write stored data.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public interface Storage {

    /**
     * Writes the data to disk.
     *
     * @param key The key to identify this data by. You will use this to retrieve the data later.
     * @param obj The object to write out.
     */
    void write(String key, Object obj);

    /**
     * Read data from disk into an object.
     *
     * @param key  The key that the data is identified by. This is case-sensitive.
     * @param type The class type of this data.
     * @return The read object, or null if the operation fails.
     */
    <T> T read(String key, Class<T> type);

    /**
     * Reads every piece of data from the same class type from disk. This is useful
     * for loading data in bulk.
     *
     * @param type The type to load.
     * @return A list containing each object.
     */
    <T> List<T> readAll(Class<T> type);

    /**
     * Remove a piece of data completely.
     *
     * @param key  The key that the data is identified by. This is case-sensitive.
     * @param type The class type this was.
     */
    void delete(String key, Class type);

}

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
import java.util.Map;

/**
 * Contains data for a specific entity, and is stored using the {@link Storage} implementation.
 * The only criteria for a valid document is that it <b>must</b> be JSON-serializable. Otherwise,
 * the document can contain whatever data you'd like to store.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public interface Document {

    /**
     * Returns any value stored in this document as a string.
     * A suggested way to convert this is via {@link Object#toString()}.
     *
     * @param key The key to retrieve.
     * @return A {@link String} containing the string representation of the object stored with this key.
     */
    String getAsString(String key);

    /**
     * Returns a list of all the keys in this document.
     *
     * @return The list of keys in this document.
     */
    List<String> getKeys();

    /**
     * Returns a {@link Map} in which the keys are the keys in this document, and the values are the objects contained
     * in this document as strings.
     *
     * @return A {@link Map} containing the keys and string values.
     * @see #getAsString(String)
     */
    Map<String, String> getAllAsStrings();

    /**
     * Converts the data of this document into JSON.
     *
     * @return The JSON string.
     */
    String toJson();

    /**
     * Converts JSON into a {@link Document}.
     *
     * @param json The JSON string to de-serialize.
     * @return The {@link Document} that was de-serialized.
     */
    <T> T fromJson(String json, Class<T> type);

}

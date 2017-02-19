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

import java.io.File;
import java.io.IOException;

/**
 * An object that can be serialized to/deserialized from JSON via GSON.
 *
 * @author Faizaan A. Datoo
 * @since API 0.1
 */
public interface Jsonable<T extends Jsonable<T>> {

    /**
     * De-serializes JSON into the object represented by this {@link Jsonable}.
     *
     * @param json The JSON to de-serialize.
     * @return An object of type T.
     */
    T fromJson(String json);

    /**
     * De-serializes a JSON file into the object represented by this {@link Jsonable}.
     *
     * @param file The {@link File} to de-serialize.
     * @return An object of type T.
     * @throws IOException If the file could not be read.
     */
    T fromFile(File file) throws IOException;

    /**
     * Serializes this {@link Jsonable} into JSON.
     *
     * @return The JSON text in a string.
     */
    String toJson();

    /**
     * Serializes this {@link Jsonable} into a JSON file.
     *
     * @param file The {@link File} to serialize to. If it does not exist, it will be created.
     * @throws IOException If the file could not be written.
     */
    void toFile(File file) throws IOException;

}

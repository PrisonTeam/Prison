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

package tech.mcprison.prison.internal;

import tech.mcprison.prison.store.Blueprint;

import java.util.concurrent.CompletableFuture;

/**
 * A low-level manager that handles storing and retrieving populated {@link Blueprint}s.
 * A populated blueprint is a blueprint which has all its {@link tech.mcprison.prison.store.Data} values set.
 * There is a higher-level utility class, {@link tech.mcprison.prison.store.Storage}, which is recommended for use instead.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public interface StorageManager {

    /**
     * Stores a populated {@link Blueprint} to disk.
     * A populated blueprint is a blueprint which has all its {@link tech.mcprison.prison.store.Data} values set.
     *
     * @param id        The ID to identify this data by.
     * @param blueprint The populated {@link Blueprint} object.
     * @return A {@link CompletableFuture} containing <code>true</code> if the store succeeds and <code>false</code> otherwise.
     */
    CompletableFuture<Boolean> store(String id, Blueprint blueprint);

    /**
     * Retrieves a populated {@link Blueprint} from disk.
     * A populated blueprint is a blueprint which has all its {@link tech.mcprison.prison.store.Data} values set.
     *
     * @param id    The ID of this data. This must be the same, case-sensitive, as what it was stored as.
     * @param clazz The type of class that this Blueprint will be deserialized into.
     * @return A {@link CompletableFuture} containing the {@link Blueprint} if it could be found, or <code>null</code> if the retrieval fails.
     */
    <T> CompletableFuture<T> retrieve(String id, Class<T> clazz);

}

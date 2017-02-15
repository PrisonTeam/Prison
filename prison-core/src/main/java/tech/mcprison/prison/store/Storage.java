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
import java.util.concurrent.CompletableFuture;

/**
 * An asynchronous JSON-based storage system for Prison.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public interface Storage {

    /**
     * Returns a {@link Document} which identifies by the specified ID.
     *
     * @param id The ID of the {@link Document} to retrieve.
     * @return A {@link CompletableFuture} with the retrieved {@link Document}, or null if there is no document by the ID.
     */
    CompletableFuture<Document> get(String id);

    /**
     * Returns a list of {@link Document}s containing every document which matches the data stored in the criteria document.
     * For example, if the criteria contains a key called 'id' and a value of '1', and a key called 'name' and a value of 'Tom',
     * then all documents containing these keys with these values will be returned in the list.
     *
     * @param criteria The {@link Document} containing the criteria, as explained above.
     * @return A {@link CompletableFuture} with a list of the retrieved {@link Document}s. If no matches are found, this will return an empty list.
     */
    CompletableFuture<List<Document>> filter(Document criteria);

    /**
     * Store an edited or new {@link Document} into the storage system by the specified ID.
     *
     * @param id  The ID to store this {@link Document} with. This is case-sensitive and will be used to retrieve this {@link Document} later.
     * @param doc The {@link Document} to store.
     * @return A {@link CompletableFuture} containing <code>true</code> if the set succeeded, and <code>false</code> otherwise.
     */
    CompletableFuture<Boolean> set(String id, Document doc);

}

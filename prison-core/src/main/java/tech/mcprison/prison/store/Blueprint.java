/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines how an object is stored in a database.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public abstract class Blueprint {

    private List<String> columns = new ArrayList<>();
    private Class<?> type;

    public Blueprint(Class<?> type) {
        this.type = type;
    }

    /**
     * Returns the name of this blueprint.
     * This will be used later when de-serializing an entry.
     */
    public abstract String getName();

    /**
     * Returns a list of all the columns in this blueprint.
     * Each column will store a single type of data.
     *
     * @return The {@link List} containing the columns.
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * Add a column to the blueprint.
     *
     * @param name The name of the column.
     */
    protected void addColumn(String name) {
        columns.add(name);
    }

    public Class<?> getType() {
        return type;
    }

}

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

import tech.mcprison.prison.store.database.Database;

import java.util.Map;

/**
 * Inserts and retrieves data to/from databases using the blueprint system.
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public class DataManager {

    private Database database;

    public DataManager(Database database) {
        this.database = database;
    }

    public void compileAndInsert(Blueprint blueprint, Object object) {
        BlueprintCompiler compiler = new BlueprintCompiler(blueprint, object);
        Map<String, Object> values = compiler.compile();

        String table = blueprint.getName();

    }

    /*
    TODO Determine how to store stuff into the database.
    The primitives and Strings will be straightforward, but the JSON-serialized (i.e. Object) types will
    be a bit more complicated. Must find a way to differentiate between the two, maybe through SQL data types.
     */

    private void createTableIfNotExists(String table) {

    }

}

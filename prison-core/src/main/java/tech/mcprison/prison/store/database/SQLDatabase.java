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

package tech.mcprison.prison.store.database;

import tech.mcprison.prison.Prison;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents a database that uses SQL as the query engine.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public abstract class SQLDatabase implements Database {

    protected Connection connection;

    @Override public abstract boolean connect();

    @Override public abstract void disconnect();

    /**
     * Query the database for results.
     *
     * @param query The query to run.
     * @return The {@link ResultSet} containing the results (never null).
     * @throws SQLException If the database could not be accessed, or if the query produces no {@link ResultSet} (i.e. is an update query).
     */
    public ResultSet query(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    // We won't make async queries for now, because we'd have to get all into callbacks
    // and such, and it's not really that useful if we're querying when we want data anyways.

    /**
     * Run an update query, in which you expect no results.
     * If you want to run a result query, use the {@link #query(String)} method.
     * It is recommended that you use the {@link #updateAsync(String)} method instead, to not
     * lock up the main thread.
     *
     * @param query The query to run.
     * @throws SQLException If the database could not be accessed, or if a result query is ran.
     */
    public void update(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    // Updates, however, can be asynchronous because they don't return anything.

    /**
     * Run an update query asynchronously, which doesn't lock up the main thread.
     *
     * @param query The query to run.
     */
    public void updateAsync(String query) {
        Prison.get().getPlatform().getScheduler().runTaskLaterAsync(() -> {
            try {
                update(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0L);
    }

    public Connection getConnection() {
        return connection;
    }

}

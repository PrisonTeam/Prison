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

package tech.mcprison.prison.database;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An abstract class containing common methods for all supported databases.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public abstract class Database {

    protected Connection connection;

    public Database() {
        this.connection = null;
    }

    /**
     * Attempts to establish a connection to the database.
     *
     * @return true if the connection succeeds, false if a SQLException is thrown, or if the JDBC driver needed is not supported.
     */
    public abstract boolean establishConnection();

    /**
     * Executes a query on the database.
     *
     * @param query The valid SQL query to run on the database.
     * @return The {@link ResultSet} returned from the query.
     */
    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            Output.get().logError(
                "Your database connection is flawed, and could not be utilized correctly.", e);
            return null;
        }
    }

    /**
     * Executes an update query on the database. This does not have a response involved, unlike {@link #executeQuery(String)}.
     *
     * @param query The valid SQL query to run on the database.
     */
    public void executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            Output.get().logError(
                "Your database connection is flawed, and could not be utilized correctly.", e);
        }
    }

    /**
     * Asynchronously execute a query on the database.
     * This is recommended because performing database operations tends to have a high chance of blocking up the main thread.
     *
     * @param query The valid SQL query to run on the database.
     * @return The {@link ResultSet} returned from the database.
     */
    public ResultSet executeQueryAsync(String query) {
        final ResultSet[] ret =
            {null}; // Hacky! Final arrays can still have their contents changed.

        Prison.get().getPlatform().getScheduler()
            .runTaskLaterAsync(() -> ret[0] = executeQuery(query), 0L);

        return ret[0];
    }

    /**
     * Asynchronously execute an update query on the database. This does not have a response involved, unlike {@link #executeQueryAsync(String)}.
     * This is recommended because performing database operations tends to have a high chance of blocking up the main thread.
     *
     * @param query The valid SQL query to run on the database.
     */
    public void executeUpdateAsync(String query) {
        Prison.get().getPlatform().getScheduler().runTaskLaterAsync(() -> executeUpdate(query), 0L);
    }

    /**
     * Close the connection to the database.
     */
    public abstract void closeConnection();

    /*
     * Getters
     */

    public Connection getConnection() {
        return connection;
    }

}

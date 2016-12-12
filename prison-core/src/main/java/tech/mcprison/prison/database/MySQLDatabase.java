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

import tech.mcprison.prison.output.Output;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An implementation of {@link Database} for MySQL.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public class MySQLDatabase extends Database {

    public MySQLDatabase(String host, String username, String password, String database, int port) {
        super(host, username, password, database, port);
    }

    @Override public boolean establishConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            }

            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return true;
                }

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this
                        .getDatabase(), this.getUsername(), this.getPassword());
            }
            return true;
        } catch (SQLException e) {
            Output.get().logError("Failed to establish a connection to the MySQL database.", e);
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            Output.get().logError(
                "You do not have the required MySQL JDBC driver to connect to the database..", e);
            return false;
        }
    }

    @Override public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            Output.get().logError(
                "Your database connection is flawed, and could not be utilized correctly.", e);
            return null;
        }
    }

    @Override public void executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            Output.get().logError(
                "Your database connection is flawed, and could not be utilized correctly.", e);
            return;
        }
    }

    @Override public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            Output.get().logError(
                "Cannot close database connection.", e);
        }
    }

}

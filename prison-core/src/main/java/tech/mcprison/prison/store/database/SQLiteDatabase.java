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

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A database implementation for SQLite.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public class SQLiteDatabase extends SQLDatabase {

    private File dataFile;

    /**
     * @param dataFile The .db file to use. Must already exist.
     */
    public SQLiteDatabase(File dataFile) {
        this.dataFile = dataFile;
    }

    @Override public boolean connect() {
        try {
            // The connection already exists and is active.
            if (connection != null && !connection.isClosed()) {
                return true;
            }

            synchronized (this) {
                // We'll check again to be thread-safe.
                if (connection != null && !connection.isClosed()) {
                    return true;
                }

                // Establish the connection
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dataFile);

                return true;
            }

            // We won't use our Output methods because we want this code to be testable.
        } catch (SQLException e) {
            System.err.println("Could not establish a connection to the SQLite database.");
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver could not be found.");
            e.printStackTrace();
            return false;
        }
    }

    @Override public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err
                .println("An error occurred whilst ending the connection to your SQLite database.");
            e.printStackTrace();
        }
    }

}

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

package tech.mcprison.prison.config;

/**
 * The result of loading a {@link Configurable} in {@link ConfigurationLoader#loadConfiguration()}.
 *
 * @author Faizaan A. Datoo
 * @since API 0.1
 */
public enum LoadResult {

    /**
     * The {@link Configurable} loaded successfully, with no problems.
     */
    SUCCESS,

    /**
     * The JSON configuration has a different version than the {@link Configurable} in the source,
     * so the file was regenerated and the old one was saved under a separate name for user reference.
     * <p>
     * This load can be considered successful, since a {@link Configurable} was loaded.
     */
    REGENERATED,

    /**
     * The JSON configuration file was not found and had to be created for the first time.
     * <p>
     * This load can be considered successful, since a {@link Configurable} was loaded.
     */
    CREATED,

    /**
     * The configuration failed to load.
     */
    FAILURE

}

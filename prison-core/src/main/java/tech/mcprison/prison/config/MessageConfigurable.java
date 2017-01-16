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

import tech.mcprison.prison.store.Exclude;

/**
 * A {@link Configurable} with the prefixes as protected class members for easy access.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public abstract class MessageConfigurable implements Configurable {

    // Naming conventions are a bit off, but I tried to maintain backwards-compatibility
    @Exclude protected String INFO_PREFIX = "&3Info &8&l/// &7";
    @Exclude protected String WARNING_PREFIX = "&6Warning &8&l/// &7";
    @Exclude protected String ERROR_PREFIX = "&cError &8&l/// &7";

}

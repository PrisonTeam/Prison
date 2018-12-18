/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.integration;

/**
 * Represents an integration into a third-party plugin.
 * An integration should serve as a template which implementations should easily and safely
 * be able to fill with the desired data.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Integration {

    /**
     * Returns the type of integration that this class provides.
     *
     * @return The {@link IntegrationType}.
     */
    IntegrationType getType();

    /**
     * Returns the name of the third-party plugin that this class is integrating with.
     *
     * @return The name of the provider.
     */
    String getProviderName();

    /**
     * Returns true if this class has integrated with the provider successfully, or false otherwise.
     * This should also return false if the third-party plugin which this integration is built for is not present.
     *
     * @return true if this class has integrated successfully, false otherwise.
     */
    boolean hasIntegrated();

}

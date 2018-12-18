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

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.platform.Platform;

/**
 * An {@link Integration} for a permissions plugin.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface PermissionIntegration extends Integration {

    /**
     * Adds a permission to this player.
     *
     * @param holder     The player that will receive this permission.
     * @param permission The permission to add.
     */
    void addPermission(Player holder, String permission);

    /**
     * Removes a permission from this player.
     *
     * @param holder     The player that will have this permission revoked.
     * @param permission The permission to remove.
     */
    void removePermission(Player holder, String permission);

    @Override default IntegrationType getType() {
        return IntegrationType.PERMISSION;
    }

}

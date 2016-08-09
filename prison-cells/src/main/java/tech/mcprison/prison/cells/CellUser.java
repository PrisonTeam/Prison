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

package tech.mcprison.prison.cells;

import java.util.*;

/**
 * Represents a user with permissions for certain cells.
 *
 * @author SirFaizdat
 */
public class CellUser {

    private UUID uuid;
    private Map<Integer, List<Permission>> cells;

    /**
     * For serialization purposes only.
     */
    public CellUser() {
    }

    /**
     * Create a brand new user with the specified UUID.
     *
     * @param uuid the {@link UUID}.
     */
    public CellUser(UUID uuid) {
        this.uuid = uuid;
        this.cells = new HashMap<>();
    }

    /**
     * Checks whether or not the user has any permissions for a certain cell.
     *
     * @param cellId The ID of the cell.
     * @return true if the user has access, false otherwise
     */
    public boolean hasAccess(int cellId) {
        return cells.containsKey(cellId);
    }

    /**
     * Checks whether or not the user has a permission in a certain cell.
     *
     * @param cellId     The ID of the cell.
     * @param permission The {@link Permission} to check for.
     * @return true if the user has the permission, false otherwise
     */
    public boolean hasPermission(int cellId, Permission permission) {
        return getPermissions(cellId).contains(Permission.IS_OWNER) || getPermissions(cellId)
            .contains(permission);
    }

    /**
     * Returns all of the permissions the user has in a cell.
     * This will never return null. If there is no permissions list initialized for the user at the cell ID,
     * it will be created.
     *
     * @param cellId The ID of the cell.
     * @return A {@link List} of {@link Permission}s. Will never be null.
     */
    public List<Permission> getPermissions(int cellId) {
        cells.putIfAbsent(cellId, new ArrayList<>());
        return cells.get(cellId);
    }

    /**
     * Add a permission to a user in a certain cell.
     *
     * @param cellId     The ID of the cell.
     * @param permission The {@link Permission} to add.
     */
    public void addPermission(int cellId, Permission permission) {
        List<Permission> permissions = getPermissions(cellId);
        if (permissions.contains(permission)) {
            return;
        }
        permissions.add(permission);
        cells.put(cellId, permissions);
    }

    /**
     * Removes a permission from a user in a certain cell.
     *
     * @param cellId     The ID of the cell.
     * @param permission The {@link Permission} to remove.
     */
    public void removePermission(int cellId, Permission permission) {
        List<Permission> permissions = getPermissions(cellId);
        permissions.remove(permission);
        cells.put(cellId, permissions);
    }

    /**
     * Removes all this user's permissions in a certain cell.
     *
     * @param cellId The ID of the cell.
     */
    public void removePermissions(int cellId) {
        cells.put(cellId, new ArrayList<>());
    }

    /**
     * Returns the UUID of this cell user, which should be used to identify it.
     *
     * @return the {@link UUID}.
     */
    public UUID getUUID() {
        return uuid;
    }

}

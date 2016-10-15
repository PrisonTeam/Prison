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
 * Represents a player that has certain permissions within a cell.
 *
 * @author SirFaizdat
 */
public class CellUser {

    private UUID uuid;
    private Map<Integer, List<CellPermission>> permissionsPerCell = new HashMap<>();

    // For serialization
    public CellUser() {
    }

    public CellUser(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns a list of the {@link CellPermission}s a user has in a cell.
     *
     * @param cellID The ID of the cell.
     * @return A list of the cell permissions. If the user does not have any permissions in the cell,
     * an empty list will be returned. This method never returns a null value.
     */
    public List<CellPermission> getPermissionsForCell(int cellID) {
        return permissionsPerCell.putIfAbsent(cellID, new ArrayList<>());
    }

    /**
     * Grant a user permission to do something in a certain cell.
     *
     * @param cellID     The ID of the cell.
     * @param permission The {@link CellPermission} representing the new permission.
     */
    public void addPermissionToCell(int cellID, CellPermission permission) {
        List<CellPermission> perms = getPermissionsForCell(cellID);

        perms.add(permission);

        permissionsPerCell.put(cellID, perms);
    }

    /**
     * Remove a user's permission to do something in a certain cell.
     * Nothing will happen if the user doesn't already have this permission.
     *
     * @param cellID     The ID of the cell.
     * @param permission The {@link CellPermission} representing the permission to remove.
     */
    public void removePermissionForCell(int cellID, CellPermission permission) {
        List<CellPermission> perms = getPermissionsForCell(cellID);

        perms.remove(permission);

        permissionsPerCell.put(cellID, perms);
    }

    /**
     * Removes all the user's permission to do anything in a certain cell.
     * Nothing will happen if the user doesn't have any permissions in the cell.
     *
     * @param cellID The ID of the cell.
     */
    public void removeAllPermissionsForCell(int cellID) {
        List<CellPermission> perms = getPermissionsForCell(cellID);

        perms.clear();

        permissionsPerCell.put(cellID, perms);
    }

}

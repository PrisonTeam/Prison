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

package io.github.prison.cells;

import java.util.*;

/**
 * Represents a player, who has some form of access to at least one cell.
 *
 * @author SirFaizdat
 */
public class CellUser {

    private UUID uuid;
    private Map<Integer, List<CellPermission>> cells;

    public CellUser() {
    }

    public CellUser(UUID uuid) {
        this.uuid = uuid;
        this.cells = new HashMap<>();
    }

    public void addCell(int cellId) {
        cells.put(cellId, new ArrayList<>());
    }

    public void addPermission(int cellId, CellPermission permission) {
        List<CellPermission> cellPermissions = cells.get(cellId);
        if(!cells.containsKey(cellId)) cellPermissions = new ArrayList<>();

        cellPermissions.add(permission);
        cells.put(cellId, cellPermissions);
    }

    public void removePermission(int cellId, CellPermission permission) {
        List<CellPermission> cellPermissions = cells.get(cellId);
        if(!cells.containsKey(cellId)) cellPermissions = new ArrayList<>();

        cellPermissions.remove(permission);
        cells.put(cellId, cellPermissions);
    }

    public boolean hasAccess(int cellId) {
        return cells.containsKey(cellId);
    }

    public UUID getUUID() {
        return uuid;
    }

}

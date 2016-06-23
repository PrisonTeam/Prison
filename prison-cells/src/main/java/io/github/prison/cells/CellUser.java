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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    public List<CellPermission> getCellPermissions(int cellId) {
        return cells.get(cellId);
    }

    public UUID getUUID() {
        return uuid;
    }

    public Set<Integer> getOwnedCells() {
        return cells.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellUser)) return false;

        CellUser cellUser = (CellUser) o;

        if (uuid != null ? !uuid.equals(cellUser.uuid) : cellUser.uuid != null) return false;
        return cells != null ? cells.equals(cellUser.cells) : cellUser.cells == null;

    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (cells != null ? cells.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CellUser{" +
                "uuid=" + uuid +
                ", cells=" + cells +
                '}';
    }
}

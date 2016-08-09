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

/**
 * Represents the permission to perform a certain action within a cell.
 *
 * @author SirFaizdat
 */
public enum Permission {

    IS_OWNER("own the cell"), OPEN_DOOR("open doors"), OPEN_CHEST("open chests"), BUILD_BLOCKS(
        "place/break blocks");

    private String userFriendlyName;

    Permission(String userFriendlyName) {
        this.userFriendlyName = userFriendlyName;
    }

    /**
     * Retrieves a permission by its enum constant name, case-insensitive.
     *
     * @param name The name to look for.
     * @return The {@link Permission} enum constant, or null.
     */
    public static Permission byName(String name) {
        for (Permission permission : values()) {
            if (permission.name().equalsIgnoreCase(name)) {
                return permission;
            }
        }
        return null;
    }

    public String getUserFriendlyName() {
        return userFriendlyName;
    }

}

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

/**
 * Different actions that can be controlled.
 *
 * @author SirFaizdat
 */
public enum CellPermission {

    CAN_ACCESS_DOOR("open or close Doors"),
    CAN_ACCESS_CHESTS("access chests"),
    BUILD("build or break Blocks");

    private String userFriendlyName;

    CellPermission(String userFriendlyName) {
        this.userFriendlyName = userFriendlyName;
    }

    public String getUserFriendlyName() {
        return userFriendlyName;
    }

}

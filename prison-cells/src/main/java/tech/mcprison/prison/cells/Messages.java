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

import tech.mcprison.prison.internal.config.Configurable;

/**
 * See the Messages class in prison-core to learn how to use this.
 *
 * @author SirFaizdat
 */
public class Messages implements Configurable {

    public static final int VERSION = 1;
    public int version = VERSION;

    public String selectDoor =
        tech.mcprison.prison.Messages.INFO_PREFIX + "Now, punch the cell's door.";
    public String cellCreated =
        tech.mcprison.prison.Messages.INFO_PREFIX + "Successfully created &3cell #%d&7.";
    public String noAccess = tech.mcprison.prison.Messages.WARNING_PREFIX
        + "You don't have permission to %s in this cell.";
    public String cellDoesNotExist =
        tech.mcprison.prison.Messages.ERROR_PREFIX + "There is no cell with the ID #%d.";

    @Override public int getVersion() {
        return version;
    }
}

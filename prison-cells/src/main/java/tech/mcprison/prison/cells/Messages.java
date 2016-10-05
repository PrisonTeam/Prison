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

import tech.mcprison.prison.platform.config.MessageConfigurable;

/**
 * See the Messages class in prison-core to learn how to use this.
 *
 * @author SirFaizdat
 */
public class Messages extends MessageConfigurable {

    public static final int VERSION = 0;
    public int version = VERSION;

    public String selectDoor = INFO_PREFIX + "Now, punch the cell's door.";
    public String cellCreated = INFO_PREFIX + "Successfully created &3cell #%d&7.";
    public String cellRented = INFO_PREFIX + "You have rented &3cell #%d&7 for &3%s&7.";
    public String permissionGranted =
        INFO_PREFIX + "You have granted &3%s&7 permission to &3%s&7 in your cell.";
    public String permissionUngranted =
        INFO_PREFIX + "You have removed &3%s&7's permission to &3%s&7 in your cell.";
    public String permissionGrantedOther =
        INFO_PREFIX + "You have been granted permission to %3%s&7 in &3%s's cell&7.";

    public String cellRentalExpired = WARNING_PREFIX + "Your rental of &6cell #%d&7 has expired.";
    public String noAccess = WARNING_PREFIX + "You don't have permission to %s in this cell.";
    public String cellDoesNotExist = ERROR_PREFIX + "There is no cell with the ID #%d.";
    public String doesNotOwnCell = ERROR_PREFIX + "You don't own a cell.";
    public String onlyTheOwnerMay =
        ERROR_PREFIX + "Only the owner of the cell is allowed to &c%s&7.";
    public String playerDoesNotExist = ERROR_PREFIX + "That player does not exist.";

    @Override public int getVersion() {
        return version;
    }
}

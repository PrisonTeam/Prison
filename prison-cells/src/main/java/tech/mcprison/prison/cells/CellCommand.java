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

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.selection.Selection;

/**
 * @author SirFaizdat
 */
public class CellCommand {

    private CellsModule cellsModule;

    public CellCommand(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }

    @Command(identifier = "cells create", description = "Create a new cell")
    public void createCell(Player sender) {
        // Get the selection
        Selection sel = Prison.getInstance().getSelectionManager().getSelection(sender);
        if (!sel.isComplete()) {
            sender.sendMessage(Prison.getInstance().getMessages().selectionNeeded);
            Prison.getInstance().getSelectionManager().bestowSelectionTool(sender);
            return;
        }

        // Create the cell creation task, and alert the user to select the door
        Cell cell = new Cell(cellsModule.getNextCellId(), sel.asBounds());
        cellsModule.getCellCreationQueue().commence(sender, cell);
        sender.sendMessage(cellsModule.getMessages().selectDoor);
    }

    @Command(identifier = "cells rent", description = "Rent a cell")
    public void rentCell(Player sender, @Arg(name = "cell_id") int id) {
        // Get the cell being rented
        Cell cell = cellsModule.getCell(id);
        if (cell == null) {
            sender.sendMessage(String.format(cellsModule.getMessages().cellDoesNotExist, id));
            return;
        }

        // Set the cell params
        cell.setRentedAtMillis(System.currentTimeMillis());
        cell.setRentalExpiresAtMillis(
            System.currentTimeMillis() + 60000); // 60000 = 1 day, TODO make this configurable
        cell.setRentedBy(sender.getUUID());
        cellsModule.saveCell(cell);

        // Edit the user to make it the owner
        CellUser user = cellsModule.getUser(sender.getUUID());
        if (user == null)
            user = new CellUser(sender.getUUID());
        user.addPermission(cell.getId(), Permission.IS_OWNER);
        cellsModule.saveUser(user);

        // Alert the renter
        sender.sendMessage(
            String.format(cellsModule.getMessages().cellRented, cell.getId(), "1 minute"));
    }

    @Command(identifier = "cells allow", description = "Allow a player to perform an action in your cell.")
    public void allowCell(Player sender, @Arg(name = "player") Player player,
        @Arg(name = "permission", description = "list") String permissionString) {

        // Get the permission
        Permission perm = Permission.byName(permissionString);

        // If the permission does not exist, tell the user the permissions
        if (permissionString.equals("list") || perm == null) {
            StringBuilder builder = new StringBuilder("&3Permissions: ");

            for (Permission permission : Permission.values()) {
                builder.append(
                    "&7" + permission.name() + " &8(" + permission.getUserFriendlyName() + "), ");
            }

            String list = builder.substring(0, builder.length() - 2);
            sender.sendMessage(list);
            return;
        }

        // Get the CellUser of the sender
        CellUser senderUser = cellsModule.getUser(sender.getUUID());
        if (senderUser == null)
            throw new IllegalStateException(
                "User must be present"); // This shouldn't even happen, unless it does, but I'll find out soon enough

        // Get the cell that the sender owns
        // We always assume that the sender owns just one cell, because that's all that's possible
        // In the future, if more than one cell is allowed, then this will have to be adapted to allow for that
        Cell cell = cellsModule.getCellByOwner(senderUser);
        if (cell == null) {
            sender.sendMessage(cellsModule.getMessages().doesNotOwnCell);
            return;
        }

        // Only the owner may bestow permissions upon other players
        if (!senderUser.hasPermission(cell.getId(), Permission.IS_OWNER)) {
            sender.sendMessage(
                String.format(cellsModule.getMessages().onlyTheOwnerMay, "edit permissions"));
            return;
        }

        // Get the CellUser of the receiver of the permissions
        CellUser receiverUser = cellsModule.getUser(player.getUUID());
        if (receiverUser == null) {
            sender.sendMessage(cellsModule.getMessages().playerDoesNotExist);
            return;
        }

        // Add the permission
        receiverUser.addPermission(cell.getId(), perm);
        cellsModule.saveUser(receiverUser);

        // Alert both parties
        sender.sendMessage(String
            .format(cellsModule.getMessages().permissionGranted, player.getName(),
                perm.getUserFriendlyName()));

        sender.sendMessage(String
            .format(cellsModule.getMessages().permissionGrantedOther, perm.getUserFriendlyName(),
                sender.getName()));
    }

    @Command(identifier = "cells disallow", description = "Remove a permission from a player in your cell.")
    public void disallowCell(Player sender, @Arg(name = "player") Player player,
        @Arg(name = "permission", description = "list") String permissionString) {
        // TODO This command
    }

    @Command(identifier = "cells drop", description = "Remove your current cell rental. No refunds!")
    public void dropCell(Player sender) {
        // TODO This command
    }

}

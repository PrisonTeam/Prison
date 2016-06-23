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

import io.github.prison.Prison;
import io.github.prison.commands.Arg;
import io.github.prison.commands.Command;
import io.github.prison.internal.CommandSender;
import io.github.prison.internal.Player;
import io.github.prison.selection.Selection;
import io.github.prison.util.Bounds;
import io.github.prison.util.TextUtil;

import static io.github.prison.cells.CellPermission.CAN_ACCESS_DOOR;

/**
 * @author Camouflage100
 */
public class CellsCommands {

    private CellsModule cellsModule;

    public CellsCommands(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }


    ///////////////////////////
    ////// USER COMMANDS //////
    ///////////////////////////

    @Command(identifier = "cells list", description = "List your or all cells", onlyPlayers = false, permissions = {"prison.cells.list"})
    public void listCommand(CommandSender sender) {
    }

    @Command(identifier = "cells autoclaim", description = "Autoclaim a cell", permissions = {"prison.cells.autoclaim"})
    public void autoclaimCommand(Player sender) {
        CellUser cUser = new CellUser(sender.getUUID());
        cUser.addCell(12);
    }

    @Command(identifier = "cells home", description = "Go to a home cell", permissions = {"prison.cells.home"})
    public void homeCommand(Player sender, @Arg(name = "cell_id", def = "-1") int id) {
        CellUser cUser = new CellUser(sender.getUUID());

        if (cUser.getOwnedCells().size() == 1) {
            int cid = cUser.getOwnedCells().iterator().next();
            sender.teleport(cellsModule.getCell(cid).getDoorLocation());
            sender.sendMessage(TextUtil.parse("&7Teleported to cell #" + id));
        } else {
            if (id != -1) {
                if (cUser.getCellPermissions(id) != null) {
                    for (CellPermission p : cUser.getCellPermissions(id)) {
                        if (p.equals(CAN_ACCESS_DOOR)) {
                            sender.teleport(cellsModule.getCell(id).getDoorLocation());
                            sender.sendMessage(TextUtil.parse("&7Teleported to cell #" + id));
                            return;
                        }
                    }

                    sender.sendMessage(TextUtil.parse(
                            "&cError: &7You do not have permission to open that cell's door!"
                    ));
                } else {
                    sender.sendMessage(TextUtil.parse("&cError: &7That is not a valid cell!"));
                }
            } else {
                sender.sendMessage("&7========== &d/cells home &7==========");
                sender.sendMessage("&7Please choose a cell:");
                for (Cell cell : cellsModule.getCells()) {
                    cUser.getCellPermissions(cell.getCellId()).stream().filter(p -> p.equals(CAN_ACCESS_DOOR)).forEachOrdered(p -> sender.sendMessage("&c    #" + cell.getCellId() + " - Owner: " + Prison.getInstance().getPlatform().getPlayer(cellsModule.getCell(cell.getCellId()).getOwner()).getName()));
                }
                sender.sendMessage("&7========== &d/cells home &7==========");
            }
        }

    }


    ////////////////////////////
    ////// ADMIN COMMANDS //////
    ////////////////////////////

    @Command(identifier = "cells create", description = "Create a new cell", permissions = {"prison.cells.createcell"})
    public void createCommand(Player sender) {
        Selection sel = Prison.getInstance().getSelectionManager().getSelection(sender);
        if (!sel.isComplete()) {
            sender.sendMessage(Prison.getInstance().getMessages().selectionNeeded);
            Prison.getInstance().getSelectionManager().bestowSelectionTool(sender);
            return;
        }

        Cell cell = new Cell(cellsModule.getNextCellId(), new Bounds(sel.getMin(), sel.getMax()), sender.getUUID());
        cellsModule.getQueue().setQueuedCell(sender, cell);
        sender.sendMessage("&3Success! &7Now, punch the door of the cell, or type \"cancel\" to cancel the creation of the cell.");
    }

    @Command(identifier = "cells delete", description = "Delete a cell", onlyPlayers = false, permissions = {"prison.cells.deletecell"})
    public void deleteCommand(CommandSender sender) {
    }

    @Command(identifier = "cells setprice", description = "Set the price of a cell", onlyPlayers = false, permissions = {"prison.cells.setprice"})
    public void setPriceCommand(CommandSender sender) {
    }

    @Command(identifier = "cells rollback", description = "Rollback a cell", onlyPlayers = false, permissions = {"prison.cells.rollback"})
    public void rollbackCommand(CommandSender sender) {
    }

    @Command(identifier = "cells reload", description = "Reload the configuration files", onlyPlayers = false, permissions = {"prison.cells.reload"})
    public void reloadCommand(CommandSender sender) {
    }

    @Command(identifier = "cells addsign", description = "Add a sign to a cell", onlyPlayers = false, permissions = {"prison.cells.addsign"})
    public void addSignCommand(CommandSender sender) {
    }

    @Command(identifier = "cells removesign", description = "Remove a sign from a cell", onlyPlayers = false, permissions = {"prison.cells.removesign"})
    public void removeSignCommand(CommandSender sender) {
    }

    @Command(identifier = "cells setentrance", description = "Set the entrance of a cell", onlyPlayers = false, permissions = {"prison.cells.setentrance"})
    public void setEnteranceCommand(CommandSender sender) {
    }

    @Command(identifier = "cells reloadsigns", description = "Set the entrance of a cell", onlyPlayers = false, permissions = {"prison.cells.reloadsigns"})
    public void reloadCellSignCommand(CommandSender sender) {
    }
}

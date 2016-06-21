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

import io.github.prison.commands.Command;
import io.github.prison.internal.CommandSender;

/**
 * @author Camouflage100
 */
public class CellsCommands {
    ///////////////////////////
    ////// USER COMMANDS //////
    ///////////////////////////

    @Command(identifier = "cells", description = "Base command of the Cells module", onlyPlayers = false)
    public void cellsCommand(CommandSender sender) {
    }

    @Command(identifier = "cells list", description = "List your or all cells", onlyPlayers = false, permissions = {"prison.cells.list"})
    public void listCommand(CommandSender sender) {
    }

    @Command(identifier = "cells autoclaim", description = "Autoclaim a cell", onlyPlayers = false, permissions = {"prison.cells.autoclaim"})
    public void autoclaimCommand(CommandSender sender) {
    }

    @Command(identifier = "cells home", description = "Go to a home cell", onlyPlayers = false, permissions = {"prison.cells.home"})
    public void homeCommand(CommandSender sender) {
    }


    ////////////////////////////
    ////// ADMIN COMMANDS //////
    ////////////////////////////

    @Command(identifier = "cells create", description = "Create a new cell", onlyPlayers = false, permissions = {"prison.cells.createcell"})
    public void createCommand(CommandSender sender) {
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

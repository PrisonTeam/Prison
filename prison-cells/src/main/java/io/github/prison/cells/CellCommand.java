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
import io.github.prison.commands.Command;
import io.github.prison.internal.Player;
import io.github.prison.selection.Selection;

/**
 * @author SirFaizdat
 */
public class CellCommand {

    private CellsModule cellsModule;

    public CellCommand(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }

    @Command(
            identifier = "cells create",
            description = "Create a new cell"
    )
    public void createCell(Player sender) {
        Selection sel = Prison.getInstance().getSelectionManager().getSelection(sender);
        if(!sel.isComplete()) {
            sender.sendMessage(Prison.getInstance().getMessages().selectionNeeded);
            Prison.getInstance().getSelectionManager().bestowSelectionTool(sender);
            return;
        }

        Cell cell = new Cell(cellsModule.getNextCellId(), sel.asBounds());
        cellsModule.getCellCreator().commence(sender, cell);
        sender.sendMessage(cellsModule.getMessages().selectDoor);
    }

}

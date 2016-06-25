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

package io.github.prison.cells.events;

import io.github.prison.cells.Cell;
import io.github.prison.internal.Player;
import io.github.prison.internal.events.Cancelable;

/**
 * Posted when a cell door is opened or closed.
 *
 * @author SirFaizdat
 */
public class CellDoorEvent implements Cancelable {

    private Cell cell;
    private Player opener;
    private boolean canceled = false;

    public CellDoorEvent(Cell cell, Player opener) {
        this.cell = cell;
        this.opener = opener;
    }

    public Cell getCell() {
        return cell;
    }

    public Player getOpener() {
        return opener;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public enum Action {

        OPEN_DOOR,
        CLOSE_DOOR;

    }

}

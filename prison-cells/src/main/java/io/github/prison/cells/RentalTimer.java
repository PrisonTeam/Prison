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

/**
 * Checks for expired rentals every 5 seconds.
 *
 * @author SirFaizdat
 */
public class RentalTimer {

    private CellsModule cellsModule;

    public RentalTimer(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }

    public void init() {
        Prison.getInstance().getPlatform().getScheduler().runTaskTimerAsync(() -> {
            // Only runs if the current time equals the rental end time
            cellsModule.getCells().stream().filter(cell -> System.currentTimeMillis() == cell.getRentalEnds()).forEach(cell -> {
                cellsModule.expireRental(cell);
            });
        }, 5, 5);
    }

}

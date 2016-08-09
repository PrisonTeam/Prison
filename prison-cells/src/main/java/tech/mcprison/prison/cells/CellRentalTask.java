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

/**
 * Checks to see which cells have expired rentals.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class CellRentalTask {

    private CellsModule cellsModule;

    public CellRentalTask(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }

    public void run() {
        Prison.getInstance().getPlatform().getScheduler()
            .runTaskTimer(() -> cellsModule.getCells().forEach(cell -> {
                if (cell.getRentalExpiresAtMillis() > 0) {
                    if (System.currentTimeMillis() >= cell.getRentalExpiresAtMillis()) {
                        // The cell is expired
                        expireOwnership(cell);
                    }
                }
            }), 1, 1); // each 1 second
    }

    private void expireOwnership(Cell cell) {
        CellUser cellUser = cellsModule.getUser(cell.getRentedBy()); // old owner

        cellsModule.resetCell(cell.getId());

        cell.setRentedBy(null);
        cell.setRentalExpiresAtMillis(0L);
        cell.setRentedAtMillis(0L);
        cellsModule.saveCell(cell);

        Prison.getInstance().getPlatform().getPlayer(cellUser.getUUID())
            .sendMessage(String.format(cellsModule.getMessages().cellRentalExpired, cell.getId()));
    }

}

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

package io.github.prison.cells.listeners;

import com.google.common.eventbus.Subscribe;
import io.github.prison.Prison;
import io.github.prison.cells.Cell;
import io.github.prison.cells.CellUser;
import io.github.prison.cells.CellsModule;
import io.github.prison.cells.Permission;
import io.github.prison.internal.events.PlayerInteractEvent;
import io.github.prison.util.Block;

/**
 * @author SirFaizdat
 */
public class CellListener {

    private CellsModule cellsModule;

    public CellListener(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }

    public void init() {
        Prison.getInstance().getEventBus().register(this);
    }

    @Subscribe
    public void onPlayerHitDoor(PlayerInteractEvent e) {
        try {
            if (e.getAction() != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) return;

            Block block = e.getClicked().getWorld().getBlockAt(e.getClicked());
            if (!Block.isDoor(block)) return; // Must be a door

            if (cellsModule.getCellCreator().getCell(e.getPlayer()) == null) return; // Player isn't creating a cell
            e.setCanceled(true);

            cellsModule.getCellCreator().addDoorLocation(e.getPlayer(), e.getClicked());
            int cellId = cellsModule.getCellCreator().complete(e.getPlayer());
            e.getPlayer().sendMessage(String.format(cellsModule.getMessages().cellCreated, cellId));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Subscribe
    public void enforceDoorPermission(PlayerInteractEvent e) {
        try {
            if (e.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
            if (cellsModule.getUser(e.getPlayer().getUUID()) == null) return;

            CellUser user = cellsModule.getUser(e.getPlayer().getUUID());
            Cell cell = cellsModule.getCellByDoorLocation(e.getClicked());
            if (cell == null) return;

            if (!user.hasAccess(cell.getId()) || !user.getPermissions(cell.getId()).contains(Permission.OPEN_DOOR)) {
                e.getPlayer().sendMessage(String.format(cellsModule.getMessages().noAccess, Permission.OPEN_DOOR.getUserFriendlyName()));
                return;
            }

            // So they have access. Let's be kind and open the door for them.
            Prison.getInstance().getPlatform().toggleDoor(e.getClicked());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}

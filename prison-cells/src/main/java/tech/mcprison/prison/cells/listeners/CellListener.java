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

package tech.mcprison.prison.cells.listeners;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cells.Cell;
import tech.mcprison.prison.cells.CellUser;
import tech.mcprison.prison.cells.CellsModule;
import tech.mcprison.prison.cells.Permission;
import tech.mcprison.prison.internal.events.BlockPlaceEvent;
import tech.mcprison.prison.internal.events.PlayerInteractEvent;
import tech.mcprison.prison.util.Block;
import tech.mcprison.prison.util.Location;

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

    @Subscribe public void onPlayerHitDoor(PlayerInteractEvent e) {
        try {
            if (e.getAction() != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
                return;

            Block block = e.getClicked().getWorld().getBlockAt(adjustToTopHalf(e.getClicked()));
            if (!Block.isDoor(block))
                return; // Must be a door

            if (cellsModule.getCellCreator().getCell(e.getPlayer()) == null)
                return; // Player isn't creating a cell
            e.setCanceled(true);

            cellsModule.getCellCreator()
                .addDoorLocation(e.getPlayer(), adjustToTopHalf(e.getClicked()));
            int cellId = cellsModule.getCellCreator().complete(e.getPlayer());
            e.getPlayer().sendMessage(String.format(cellsModule.getMessages().cellCreated, cellId));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private Location adjustToTopHalf(Location loc) {
        Location oneBelow =
            new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        if (!Block.isDoor(loc.getWorld().getBlockAt(oneBelow)))
            return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 1,
                loc.getBlockZ());
        return loc;
    }

    @Subscribe public void enforceDoorPermission(PlayerInteractEvent e) {
        try {
            if (e.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
                return;
            if (cellsModule.getUser(e.getPlayer().getUUID()) == null)
                return;

            CellUser user = cellsModule.getUser(e.getPlayer().getUUID());
            Location adjustedLoc = adjustToTopHalf(e.getClicked());
            Cell cell = cellsModule.getCellByDoorLocation(adjustedLoc);
            if (cell == null)
                return;

            if (!user.hasAccess(cell.getId()) || !user
                .hasPermission(cell.getId(), Permission.OPEN_DOOR)) {
                e.getPlayer().sendMessage(String.format(cellsModule.getMessages().noAccess,
                    Permission.OPEN_DOOR.getUserFriendlyName()));
                e.setCanceled(true);
                return;
            }

            // So they have access. Let's be kind and open the door for them.
            Prison.getInstance().getPlatform().toggleDoor(adjustedLoc);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Subscribe public void enforceChestPermission(PlayerInteractEvent e) {
        try {
            if (e.getClicked().getWorld().getBlockAt(e.getClicked()) != Block.CHEST)
                return;
            if (cellsModule.getUser(e.getPlayer().getUUID()) == null)
                return;

            CellUser user = cellsModule.getUser(e.getPlayer().getUUID());
            Cell cell = cellsModule.getCellByLocationWithin(e.getClicked());
            if (cell == null)
                return;

            if (!user.hasAccess(cell.getId()) || !user
                .hasPermission(cell.getId(), Permission.OPEN_CHEST)) {
                e.getPlayer().sendMessage(String.format(cellsModule.getMessages().noAccess,
                    Permission.OPEN_CHEST.getUserFriendlyName()));
                e.setCanceled(true);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Subscribe public void enforceBuildPermission(BlockPlaceEvent e) {
        try {
            if (cellsModule.getUser(e.getPlayer().getUUID()) == null)
                return;

            CellUser user = cellsModule.getUser(e.getPlayer().getUUID());
            Cell cell = cellsModule.getCellByLocationWithin(e.getBlockLocation());
            if (cell == null)
                return;

            if (!user.hasAccess(cell.getId()) || !user
                .hasPermission(cell.getId(), Permission.BUILD_BLOCKS)) {
                e.getPlayer().sendMessage(String.format(cellsModule.getMessages().noAccess,
                    Permission.BUILD_BLOCKS.getUserFriendlyName()));
                e.setCanceled(true);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}

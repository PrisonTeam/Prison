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
import tech.mcprison.prison.cells.CellUser;
import tech.mcprison.prison.cells.CellsModule;
import tech.mcprison.prison.platform.events.PlayerJoinEvent;

/**
 * @author SirFaizdat
 */
public class UserListener {

    private CellsModule cellsModule;

    public UserListener(CellsModule cellsModule) {
        this.cellsModule = cellsModule;
    }

    public void init() {
        Prison.getInstance().getEventBus().register(this);
    }

    @Subscribe public void onPlayerJoin(PlayerJoinEvent e) {
        if (cellsModule.getUser(e.getPlayer().getUUID()) == null) {
            cellsModule.saveUser(new CellUser(e.getPlayer().getUUID()));
        }
    }

}

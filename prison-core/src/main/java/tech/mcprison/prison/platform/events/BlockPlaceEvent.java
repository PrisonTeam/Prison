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

package tech.mcprison.prison.platform.events;

import tech.mcprison.prison.platform.Player;
import tech.mcprison.prison.util.Block;
import tech.mcprison.prison.util.Location;

/**
 * Platform-independent event, which is posted when a player places a block.
 *
 * @author Camouflage100
 * @since 3.0
 */
public class BlockPlaceEvent implements Cancelable {

    private Block block;
    private Location blockLocation;
    private Player player;
    private boolean canceled = false;

    public BlockPlaceEvent(Block block, Location blockLocation, Player player) {
        this.block = block;
        this.blockLocation = blockLocation;
        this.player = player;
    }

    @Override public boolean isCanceled() {
        return canceled;
    }

    @Override public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Block getBlock() {
        return block;
    }

    public Location getBlockLocation() {
        return blockLocation;
    }

    public Player getPlayer() {
        return player;
    }

}

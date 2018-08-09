/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

package xyz.faizaan.prison.internal;

import xyz.faizaan.prison.internal.block.Block;
import xyz.faizaan.prison.util.Location;

import java.util.List;

/**
 * Represents a world on the Minecraft server.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface World {

    /**
     * Returns the name of this world.
     */
    String getName();

    /**
     * Returns a list of all the players in this world.
     */
    List<Player> getPlayers();

    /**
     * Returns the {@link Block} at a specified location.
     *
     * @param location The {@link Location} of the block.
     */
    Block getBlockAt(Location location);

}

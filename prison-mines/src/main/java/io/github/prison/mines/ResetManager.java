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

package io.github.prison.mines;

import io.github.prison.util.Block;
import io.github.prison.util.Location;

/**
 * @author SirFaizdat
 */
public class ResetManager {

    // TODO Everything

    private MinesModule minesModule;

    public ResetManager(MinesModule minesModule) {
        this.minesModule = minesModule;
    }

    public void reset(Mine mine) {
        int minX = mine.getBounds().getMin().getBlockX();
        int minY = mine.getBounds().getMin().getBlockY();
        int minZ = mine.getBounds().getMin().getBlockZ();
        int maxX = mine.getBounds().getMax().getBlockX();
        int maxY = mine.getBounds().getMax().getBlockY();
        int maxZ = mine.getBounds().getMax().getBlockZ();

        // Temporarily reset all the mine blocks to bricks
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mine.getBounds().getMax().getWorld().setBlockAt(new Location(mine.getBounds().getMax().getWorld(), x, y, z), Block.BRICK);
                }
            }
        }
    }

}

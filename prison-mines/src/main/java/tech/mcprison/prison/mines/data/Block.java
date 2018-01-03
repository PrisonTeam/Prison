/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2017 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.mines.util;

import tech.mcprison.prison.util.BlockType;

/**
 * Represents a block in a mine
 */
public class Block {
    /**
     * The {@link BlockType} represented by this {@link Block}
     */
    public BlockType type = BlockType.AIR;
    /**
     * The chance of this block appearing in it's associated mine
     */
    public double chance = 100;

    /**
     * Assigns the type and chance
     *
     * @param block
     * @param chance
     * @return
     */
    public Block create(BlockType block, double chance) {
        type = block;
        this.chance = chance;
        return this;
    }
}

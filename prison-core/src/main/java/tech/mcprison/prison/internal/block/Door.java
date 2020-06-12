/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.util.BlockType;

/**
 * Represents a door.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Door extends BlockState {

    /**
     * Returns whether or not the door is open.
     *
     * @return true if it's open, false otherwise.
     */
    boolean isOpen();

    /**
     * Open or close the door.
     *
     * @param open true to open, false to close.
     */
    void setOpen(boolean open);

    /**
     * Open the door if it's closed, and close the door if it's open.
     */
    default void toggleOpen() {
        setOpen(!isOpen());
    }

    /**
     * Returns true if this is a wooden door, false otherwise.
     *
     * @return true if it's a wooden door, false otherwise.
     */
    default boolean isWoodenDoor() {
        BlockType block = getBlock().getType();
        return block == BlockType.ACACIA_DOOR_BLOCK || block == BlockType.BIRCH_DOOR_BLOCK
            || block == BlockType.DARK_OAK_DOOR_BLOCK || block == BlockType.JUNGLE_DOOR_BLOCK
            || block == BlockType.OAK_DOOR_BLOCK || block == BlockType.SPRUCE_DOOR_BLOCK;
    }

}

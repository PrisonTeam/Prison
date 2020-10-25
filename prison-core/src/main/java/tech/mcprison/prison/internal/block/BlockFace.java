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

/**
 * One of the six sides of a block.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public enum BlockFace {

    NORTH, SOUTH, EAST, WEST, TOP, BOTTOM, UP, DOWN;
	
	    public BlockFace getOppositeFace() {
	        switch (this) {
	        case NORTH:
	            return SOUTH;
	        case SOUTH:
	        	return NORTH;
	        case EAST:
	        	return WEST;
	        case WEST:
	        	return EAST;
	        case TOP:
	        	return BOTTOM;
	        case BOTTOM:
	        	return TOP;
	        case UP:
	        	return DOWN;
	        case DOWN:
	        	return UP;
	        default:
	        	return NORTH;

	        }
	 }
    
    

}

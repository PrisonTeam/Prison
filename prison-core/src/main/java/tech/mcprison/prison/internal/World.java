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

package tech.mcprison.prison.internal;

import java.util.List;

import tech.mcprison.prison.bombs.MineBombs.AnimationArmorStandItemLocation;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Location;

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
     * Returns a list of all entities in this world.
     * @return
     */
    List<Entity> getEntities();
    
    /**
     * Returns the {@link Block} at a specified location.
     *
     * @param location The {@link Location} of the block.
     */
    public Block getBlockAt(Location location);
    
    public Block getBlockAt( Location location, boolean containsCustomBlocks );
    
    
    public void setBlock( PrisonBlock block, int x, int y, int z );

    
    /**
     * <p>This function should be called from an async task, and it will
     * drop down in to the synchronous thread to first get the block 
     * from the world, then it will change to the specified PrisonBlock type.
     * </p>
     * 
     * @param prisonBlock
     * @param location
     */
	public void setBlockAsync( PrisonBlock prisonBlock, Location location );

	public void setBlocksSynchronously( List<MineTargetPrisonBlock> tBlocks, 
						MineResetType resetType, 
								PrisonStatsElapsedTimeNanos nanos );

	

//	public Entity spawnEntity(EntityType entityType);

	public Entity spawnEntity( Location loc, EntityType entityType);

	
	public ArmorStand spawnArmorStand( Location location );

	
	public ArmorStand spawnArmorStand(Location location, String itemType, 
							AnimationArmorStandItemLocation asLocation );

	public int getMaxHeight();


}

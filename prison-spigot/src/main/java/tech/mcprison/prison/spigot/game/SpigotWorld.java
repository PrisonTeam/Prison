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

package tech.mcprison.prison.spigot.game;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.PrisonStatsElapsedTimeNanos;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotWorld implements World {

    private org.bukkit.World bukkitWorld;

    public SpigotWorld(org.bukkit.World bukkitWorld) {
        this.bukkitWorld = bukkitWorld;
    }

    @Override public String getName() {
        return bukkitWorld.getName();
    }

    @Override public List<Player> getPlayers() {
        return Bukkit.getServer().getOnlinePlayers().stream()
            .filter(player -> 
            		player.getWorld().getName().equals(
            					bukkitWorld.getName()))
            .map((Function<org.bukkit.entity.Player, SpigotPlayer>) SpigotPlayer::new)
            .collect(Collectors.toList());
    }

    /**
     * <p>This should be the ONLY usage in the whole Prison plugin that gets the 
     * bukkit block from the world and converts it to a SpigotBlock..
     * </p>
     * 
     * <p>This gets the actual block from the world, but it only reads, and does not 
     * update.  I cannot say this is safe to run asynchronously, but so far I have
     * not see any related problems when it is.
     * 
     */
    @Override 
    public Block getBlockAt( Location location, boolean containsCustomBlocks ) {
    	SpigotBlock sBlock = null;
    	
    	if ( location != null ) {
    		
    		org.bukkit.Location bLocation = getBukkitLocation( location );
    		org.bukkit.block.Block bBlock = bukkitWorld.getBlockAt( bLocation );
    		
    		
    		sBlock = SpigotCompatibility.getInstance().getSpigotBlock( bBlock );
    		
    		if ( sBlock == null ) {
    			
    			sBlock = new SpigotBlock( bBlock, PrisonBlock.AIR.clone() );
    		}
    		
    		
    		if ( containsCustomBlocks ) {
    			
    			List<CustomBlockIntegration> cbIntegrations = 
    					PrisonAPI.getIntegrationManager().getCustomBlockIntegrations();
    			
    			for ( CustomBlockIntegration customBlock : cbIntegrations )
    			{
    				PrisonBlock pBlock = customBlock.getCustomBlock( sBlock );
    				
    				if ( pBlock != null ) {
    					
    					//if ( Output.get().isDebug() ) 
//    					{
//    						
//    						String message = String.format( 
//    								"SpigotWorld.getBlockAt: customBlock: %s  " +
//    								"spigot: %s  bukkit: %s",
//    								pBlock.getBlockName(), sBlock.getBlockName(), 
//    								bBlock.getType().name() );
//    						
//    						Output.get().logInfo( message );
//    					}
    					
    					sBlock.setBlockName( pBlock.getBlockName() );
    					sBlock.setBlockType( customBlock.getBlockType() );
    					break;
    				}
    			}
    		}
    		
    		
    	}
        
        return sBlock;
    }
    
    public Block getBlockAt( Location location ) {
    	return getBlockAt( location, false );
    }
 
    
//    public SpigotBlock getSpigotBlockAt(Location location) {
//    	return new SpigotBlock(
//    			bukkitWorld.getBlockAt(SpigotUtil.prisonLocationToBukkit(location)));
//    }
    
    public org.bukkit.Location getBukkitLocation(Location location) {
    	return SpigotUtil.prisonLocationToBukkit(location);
    }
    
    public org.bukkit.inventory.ItemStack getBukkitItemStack( ItemStack itemStack ) {
    	
    	SpigotItemStack sItemStack = (SpigotItemStack) itemStack;
    	
    	return sItemStack.getBukkitStack();
    }
    
    @Override
    public void setBlock( PrisonBlock block, int x, int y, int z ) {
    	
    	Location loc = new Location( this, x, y, z );
    	org.bukkit.block.Block bukkitBlock = 
    			bukkitWorld.getBlockAt(SpigotUtil.prisonLocationToBukkit(loc));
    	
    	SpigotCompatibility.getInstance().updateSpigotBlock( block, bukkitBlock );
    }
    
    
    /**
     * <p>This function should be called from an async task, and it will
     * drop down in to the synchronous thread to first get the block 
     * from the world, then it will change to the specified PrisonBlock type.
     * </p>
     * 
     * @param prisonBlock
     * @param location
     */
	public void setBlockAsync( PrisonBlock prisonBlock, Location location ) {
		
		switch ( prisonBlock.getBlockType() )
		{
			case minecraft:
				
				SpigotCompatibility.getInstance().updateSpigotBlockAsync( prisonBlock, location );
				
				break;

			case CustomItems:
				{
					CustomBlockIntegration customItemsIntegration = 
									PrisonAPI.getIntegrationManager().getCustomBlockIntegration( prisonBlock.getBlockType() );
					
					customItemsIntegration.setCustomBlockIdAsync( prisonBlock, location );
				}
				
				break;
				
			default:
				break;
		}
	}
	
	
	/**
	 * <p>This list of blocks to be updated, should be ran from an asynchronous thread.
	 * This function will take it's code block and run it in bukkit's synchronous 
	 * thread so the block updates will be thread safe.
	 * </p>
	 * 
	 * <p>The MineTargetPrisonBlock List should be a fairly short list of blocks that
	 * will be updated in one synchronous slice.
	 * </p>
	 * 
	 */
	@Override
	public void setBlocksSynchronously( List<MineTargetPrisonBlock> tBlocks, MineResetType resetType, 
			PrisonStatsElapsedTimeNanos nanos ) {
		
		new BukkitRunnable() {
			@Override
			public void run() {
				
				long start = System.nanoTime();
				
				MineTargetPrisonBlock current = null;
				try
				{
					for ( MineTargetPrisonBlock tBlock : tBlocks )
					{
						current = tBlock;
						
						final PrisonBlock pBlock = tBlock.getPrisonBlock( resetType );
						
						if ( pBlock != null ) {
							
							Location location = tBlock.getLocation();
							
							SpigotBlock sBlock = (SpigotBlock) getBlockAt( location );
//							SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
							
							sBlock.setPrisonBlock( pBlock );
						}
					}
				}
				catch ( Exception e ) {
					
					if ( current != null ) {
						
						String blkName = current.getPrisonBlock().getBlockName();
						PrisonBlock pBlock = current.getPrisonBlock( resetType );
						String resetTypeBlockName = pBlock == null ? "null" : pBlock.getBlockName();
						
						Output.get().logError( 
								String.format( "SpigotWorld.setBlocksSynchronously: %s  resetType: %s  %s",
										blkName, resetType.name(), resetTypeBlockName ), e );
					}
					else {
						
						Output.get().logError( 
								String.format( "SpigotWorld.setBlocksSynchronously: --noBlock--  resetType: %s  " +
										"[unable to set 'current']",
										resetType.name()  ), e );
					}
				}
				
				long elapsedNanos = System.nanoTime() - start;
				
					
				if ( nanos != null ) {
					nanos.addNanos( elapsedNanos );
				}
				
			}
		}.runTaskLater( SpigotPrison.getInstance(), 0 );
		
	}

    public org.bukkit.World getWrapper() {
        return bukkitWorld;
    }
    

}

package tech.mcprison.prison.spigot.block;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.util.Location;

public class SpigotBlockSetAsynchronously {
	
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
}

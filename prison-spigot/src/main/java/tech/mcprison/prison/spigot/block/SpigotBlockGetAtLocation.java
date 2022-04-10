package tech.mcprison.prison.spigot.block;

import java.util.List;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.util.Location;

public class SpigotBlockGetAtLocation {

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
    public Block getBlockAt( Location location, boolean containsCustomBlocks, 
    		SpigotWorld world ) {
    	SpigotBlock sBlock = null;
    	
    	if ( location != null ) {
    		
    		org.bukkit.Location bLocation = world.getBukkitLocation( location );
    		org.bukkit.block.Block bBlock = world.getWrapper().getBlockAt( bLocation );
    		
    		
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

    
}

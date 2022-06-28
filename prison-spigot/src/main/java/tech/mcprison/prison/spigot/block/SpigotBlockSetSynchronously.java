package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import tech.mcprison.prison.internal.PrisonStatsElapsedTimeNanos;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.util.Location;

public class SpigotBlockSetSynchronously {

	public void setBlocksSynchronously( List<MineTargetPrisonBlock> tBlocks, MineResetType resetType, 
			PrisonStatsElapsedTimeNanos nanos, SpigotWorld world ) {
	
		List<MineTargetPrisonBlock> tBlocksCloned = new ArrayList<>();
		for ( MineTargetPrisonBlock mtpb : tBlocks ) {
			
			tBlocksCloned.add( mtpb );
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				
				long start = System.nanoTime();
				
				MineTargetPrisonBlock current = null;
				try
				{
					for ( MineTargetPrisonBlock tBlock : tBlocksCloned )
					{
						current = tBlock;
						
						if ( tBlock != null && tBlock.getLocation() != null ) {
							
							final PrisonBlock pBlock = tBlock.getPrisonBlock( resetType );
							
							if ( pBlock != null ) {
								
								Location location = tBlock.getLocation();
								
								SpigotBlock sBlock = (SpigotBlock) world.getBlockAt( location );
//							SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
								
								sBlock.setPrisonBlock( pBlock );
							}
						}
						
					}
				}
				catch ( Exception e ) {
					
					if ( current != null ) {
						
						String blkName = current.getPrisonBlock().getBlockName();
						PrisonBlock pBlock = current.getPrisonBlock( resetType );
						String resetTypeBlockName = pBlock == null ? "null" : pBlock.getBlockName();
						
						Output.get().logError( 
								String.format( "SpigotWorld.setBlocksSynchronously Exception: %s  resetType: %s  %s :: %s",
										blkName, resetType.name(), resetTypeBlockName, e.getMessage() ), e );
					}
					else {
						
						Output.get().logError( 
								String.format( "SpigotWorld.setBlocksSynchronously Exception: --noBlock--  resetType: %s  " +
										"[unable to set 'current'] :: %s",
										resetType.name(), e.getMessage() ), e );
					}
				}
				
				long elapsedNanos = System.nanoTime() - start;
				
					
				if ( nanos != null ) {
					nanos.addNanos( elapsedNanos );
				}
				
			}
		}.runTaskLater( SpigotPrison.getInstance(), 0 );
		
	}

}

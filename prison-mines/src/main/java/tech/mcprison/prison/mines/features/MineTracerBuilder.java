package tech.mcprison.prison.mines.features;

import java.util.Optional;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;

public class MineTracerBuilder
{

	public enum TracerType {
		
		pink("PINK_STAINED_GLASS"),
		red("REd_STAINED_GLASS"),
		
		redstone("REDSTONE_BLOCK"),
		dust("REDSTONE_wire"), // redstone_wire is the block, redstone is the inventory item
		
		air("AIR")
		
		;
		
		private final String blockType;
		
		private TracerType( String blockType ) {
			this.blockType = blockType;
		}
		
		public String getBlockType() {
			return blockType;
		}

		public static TracerType fromString( String type ) {
			TracerType results = TracerType.pink;
			
			for ( TracerType tracerType : values() )
			{
				if ( tracerType.name().equalsIgnoreCase( type )) {
					results = tracerType;
					break;
				}
			}
			
			return results;
		}
	}
    
    public void clearMine( Mine mine, boolean tracer ) {
		
    	if ( mine == null ) {
    		Output.get().logError(" #### NPE ###");
    	}
		try {
			
			if ( mine.isVirtual() ) {
				// Mine is virtual and cannot be reset.  Just skip this with no error messages.
				return;
			}
			
			
			// Output.get().logInfo( "MineRest.resetSynchonouslyInternal() " + getName() );

			Optional<World> worldOptional = mine.getWorld();
			World world = worldOptional.get();
			
			
			PrisonBlock blockAirPB = new PrisonBlock( "AIR" );
			BlockType blockAirBT = BlockType.AIR;
			
			PrisonBlock blockRedPB = new PrisonBlock( "PINK_STAINED_GLASS" );
			BlockType blockRedBT = BlockType.PINK_STAINED_GLASS;

//			PrisonBlock blockRedstonePB = new PrisonBlock( "REDSTONE_BLOCK" );
//			BlockType blockRedstoneBT = BlockType.REDSTONE_BLOCK;
			
	
			
			// Reset the block break count before resetting the blocks:
//			setBlockBreakCount( 0 );
//			Random random = new Random();
			
			int yMin = mine.getBounds().getyBlockMin();
			int yMax = mine.getBounds().getyBlockMax();
			
			int xMin = mine.getBounds().getxBlockMin();
			int xMax = mine.getBounds().getxBlockMax();
			
			int zMin = mine.getBounds().getzBlockMin();
			int zMax = mine.getBounds().getzBlockMax();
			
			for (int y = yMax; y >= yMin; y--) {
//    			for (int y = getBounds().getyBlockMin(); y <= getBounds().getyBlockMax(); y++) {
				for (int x = xMin; x <= xMax; x++) {
					for (int z = zMin; z <= zMax; z++) {
						Location targetBlock = new Location(world, x, y, z);
						
						boolean xEdge = x == xMin || x == xMax;
						boolean yEdge = y == yMin || y == yMax;
						boolean zEdge = z == zMin || z == zMax;
						
						boolean isEdge = xEdge && yEdge || xEdge && zEdge ||
										 yEdge && zEdge;
						
						if ( mine.isUseNewBlockModel() ) {
							
							targetBlock.getBlockAt().setPrisonBlock( 
									tracer && isEdge ? blockRedPB : blockAirPB );
						}
						else {
							
							targetBlock.getBlockAt().setType( 
									tracer && isEdge ? blockRedBT : blockAirBT );
						}
					}
				}
			}
			
			
		} 
		catch (Exception e) {
			Output.get().logError("&cFailed to clear mine " + mine.getName() + 
					"  Error: [" + e.getMessage() + "]", e);
		}
    }
    
 
}

package tech.mcprison.prison.mines.features;

import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.tasks.MinePagedResetAsyncTask;
import tech.mcprison.prison.output.Output;

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
	
    public void clearMine( Mine mine, MineResetType resetType ) {
		
	    	if ( mine == null ) {
	    		Output.get().logError(" #### Null MINE? ###");
	    	}
	
	    	if ( mine.isVirtual() ) {
	    		// Mine is virtual and cannot be reset.  Just skip this with no error messages.
	    		return;
	    	}
    			
		MinePagedResetAsyncTask resetTask = new MinePagedResetAsyncTask( mine, resetType );
		resetTask.submitTaskAsync();
    }
    
}

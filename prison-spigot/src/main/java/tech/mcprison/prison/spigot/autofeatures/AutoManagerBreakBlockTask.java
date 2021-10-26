package tech.mcprison.prison.spigot.autofeatures;

import java.util.List;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class AutoManagerBreakBlockTask
	implements PrisonRunnable
{
	private SpigotBlock block;
	private List<SpigotBlock> blocks;
	
	public AutoManagerBreakBlockTask( SpigotBlock block ) {
		super();
		
		this.block = block;
		this.blocks = null;
	}
	
	public AutoManagerBreakBlockTask( List<SpigotBlock> blocks ) {
		super();
		
		this.block = null;
		this.blocks = blocks;
	}
	
	public static void submitTask( SpigotBlock block ) {
		
		AutoManagerBreakBlockTask blockTask = new AutoManagerBreakBlockTask( block );
		
		PrisonTaskSubmitter.runTaskLater( blockTask, 0 );
	}
	
	public static void submitTask( List<SpigotBlock> blocks ) {
		
		AutoManagerBreakBlockTask blockTask = new AutoManagerBreakBlockTask( blocks );
		
		PrisonTaskSubmitter.runTaskLater( blockTask, 0 );
	}
	
	@Override
	public void run() {
		
//		// Set the broken block to AIR and cancel the event
		if ( block != null && !block.isEmpty() ) {
			block.setPrisonBlock( PrisonBlock.AIR );
		}
		
		if ( blocks != null ) {
			for ( SpigotBlock spigotBlock : blocks )
			{
				spigotBlock.setPrisonBlock( PrisonBlock.AIR );
				
			}
		}
	}

}

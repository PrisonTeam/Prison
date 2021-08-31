package tech.mcprison.prison.spigot.autofeatures;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class AutoManagerBreakBlockTask
	implements PrisonRunnable
{
	private SpigotBlock block;
	
	public AutoManagerBreakBlockTask( SpigotBlock block ) {
		super();
		
		this.block = block;
	}
	
	public static void submitTask( SpigotBlock block ) {
		
		AutoManagerBreakBlockTask blockTask = new AutoManagerBreakBlockTask( block );
		
		PrisonTaskSubmitter.runTaskLater( blockTask, 0 );
	}
	
	@Override
	public void run() {
		
//		// Set the broken block to AIR and cancel the event
		if ( !block.isEmpty() ) {
			block.setPrisonBlock( PrisonBlock.AIR );
		}
		
	}

}

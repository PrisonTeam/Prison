package tech.mcprison.prison.spigot.autofeatures;

import java.util.List;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.MineStateMutex;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class AutoManagerBreakBlockTask
	implements PrisonRunnable
{
	private SpigotBlock block;
	private List<SpigotBlock> blocks;
	
	private Mine mine;
	private MineStateMutex mineStateMutexClone;
	
	public AutoManagerBreakBlockTask( SpigotBlock block, Mine mine ) {
		super();
		
		this.block = block;
		this.blocks = null;
		
		this.mine = mine;
		
		if ( mine != null ) {
			this.mineStateMutexClone = mine.getMineStateMutex().clone();
		}
	}
	
	public AutoManagerBreakBlockTask( List<SpigotBlock> blocks, Mine mine ) {
		super();
		
		this.block = null;
		this.blocks = blocks;

		this.mine = mine;

		if ( mine != null ) {
			this.mineStateMutexClone = mine.getMineStateMutex().clone();
		}
	}
	
	public static void submitTask( SpigotBlock block, Mine mine ) {
		
		AutoManagerBreakBlockTask blockTask = new AutoManagerBreakBlockTask( block, mine );
		
		PrisonTaskSubmitter.runTaskLater( blockTask, 0 );
	}
	
	public static void submitTask( List<SpigotBlock> blocks, Mine mine ) {
		
		AutoManagerBreakBlockTask blockTask = new AutoManagerBreakBlockTask( blocks, mine );
		
		PrisonTaskSubmitter.runTaskLater( blockTask, 0 );
	}
	
	@Override
	public void run() {
		
		if ( mineStateMutexClone != null && 
				!mine.getMineStateMutex().isValidState( mineStateMutexClone ) ) {
			return;
		}
		
//		// Set the broken block to AIR and cancel the event
		if ( block != null && !block.isEmpty() ) {
			
			block.setPrisonBlock( PrisonBlock.AIR );
		}
		
		if ( blocks != null ) {
			int count = 0;
			for ( SpigotBlock spigotBlock : blocks )
			{
				if ( count++ % 10 == 0 && mineStateMutexClone != null && 
						!mine.getMineStateMutex().isValidState( mineStateMutexClone ) ) {
					return;
				}
				
				spigotBlock.setPrisonBlock( PrisonBlock.AIR );
				
			}
		}
	}

}

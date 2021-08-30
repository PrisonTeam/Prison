package tech.mcprison.prison.mines.tasks;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class MineTeleportTask
	implements PrisonRunnable
{
	private Mine mine;
	
	public MineTeleportTask( Mine mine ) {
		super();
		
		this.mine = mine;
	}
	
	public void submitTaskSync() {
		
		
		// Prevent the task from being submitted if it is a virtual mine:
		if ( mine.isVirtual() ) {
			return;
		}

		PrisonTaskSubmitter.runTaskLater( this, 0 );
	}
	
	@Override
	public void run() {
		
		mine.teleportAllPlayersOut();
		
	}
}

package tech.mcprison.prison.mines.data;

import java.util.List;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonRunnable;

public class PrisonDispatchCommandTask
		implements PrisonRunnable {
	
	private List<String> tasks;
	private String errorMessage;
	
	private Player player;
	private boolean playerTask = false;

	public PrisonDispatchCommandTask( List<String> tasks, String errorMessage, 
										Player player, boolean playerTask ) {
		this.tasks = tasks;
		this.errorMessage = errorMessage;
		
		this.player = player;
		this.playerTask = playerTask;
	}

	@Override
	public void run() {
		if ( tasks != null && tasks.size() > 0 ) {
			
			for ( String task : tasks ) {
				
				try {
					if ( playerTask && player != null ) {
						
						PrisonAPI.dispatchCommand( player, task );
					}
					else {
						PrisonAPI.dispatchCommand( task );
					}
				}
				catch ( Exception e ) {

					Output.get().logError( "PrisonDispatchCommand: Error trying to run task: " + errorMessage + 
							"  Task: [" + task + "] " + e.getMessage() );
				}
			}
			
		}
	}
}

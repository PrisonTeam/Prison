package tech.mcprison.prison.mines.data;

import java.util.List;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.output.Output;

public class PrisonDispatchCommandTask
		implements PrisonRunnable {
	
	private List<String> tasks;
	private String errorMessage;

	public PrisonDispatchCommandTask( List<String> tasks, String errorMessage ) {
		this.tasks = tasks;
		this.errorMessage = errorMessage;
	}

	@Override
	public void run() {
		if ( tasks != null && tasks.size() > 0 ) {
			
			for ( String task : tasks ) {
				
				try {
					PrisonAPI.dispatchCommand( task );
				}
				catch ( Exception e ) {

					Output.get().logError( "PrisonDispatchCommand: Error trying to run task: " + errorMessage + 
							"  Task: [" + task + "] " + e.getMessage() );
				}
			}
			
		}
	}
}

package tech.mcprison.prison.tasks;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

public class PrisonDispatchCommandTask
		implements PrisonRunnable {
	
	private List<String> tasks;
	private List<Long> elapsedTimes; 
	
	private String errorMessage;
	
	private List<PrisonCommandTaskPlaceholderData> customPlaceholders;

	private Player player;
	private boolean playerTask = false;
	
	

	public PrisonDispatchCommandTask( List<String> tasks, String errorMessage, 
										Player player, boolean playerTask ) {
		this.tasks = tasks;
		this.elapsedTimes = new ArrayList<>();
		
		this.errorMessage = errorMessage;
		
		this.player = player;
		this.playerTask = playerTask;
		
		this.customPlaceholders = new ArrayList<>();
	}

	
	@Override
	public void run() {
		if ( tasks != null && tasks.size() > 0 ) {
			
			for ( String task : tasks ) {
				
				long start = System.nanoTime();
				
				// If the task is '{ifPerm:<perm>}' then the player must have the perm to 
				// continue:
				if ( task.toLowerCase().startsWith( "{ifperm:" ) ) {
					String perm = task.substring( 8, task.length() - 1 );
					
					boolean hasPerm = player.hasPermission( perm );
					
					if ( !hasPerm ) {
						break;
					}
				}
				if ( task.toLowerCase().startsWith( "{ifnotperm:" ) ) {
					String perm = task.substring( 11, task.length() - 1 );
					
					boolean hasPerm = player.hasPermission( perm );
					
					if ( hasPerm ) {
						break;
					}
				}
				
				// Apply the custom placeholders:
				for ( PrisonCommandTaskPlaceholderData cPlaceholder : getCustomPlaceholders() ) {
					if ( cPlaceholder.contains( task ) ) {
						task = cPlaceholder.replace( task );
					}
				}
				
				try {
					
					if ( playerTask && player != null ) {
//						double start = System.currentTimeMillis();
						
						PrisonAPI.dispatchCommand( player, task );
						
//						double stop = System.currentTimeMillis();
//						Output.get().logDebug( "PrisonDispatchCommandTask.run: (player) " + 
//									(stop - start) + " ms  player= " + player.getName() + 
//									"  task: " + task );
					}
					else {
//						double start = System.currentTimeMillis();
						
						PrisonAPI.dispatchCommand( task );

//						double stop = System.currentTimeMillis();
//						Output.get().logDebug( "PrisonDispatchCommandTask.run: (console) " + 
//									(stop - start) + " ms" +
//									"  task: " + task );
					}
				}
				catch ( Exception e ) {

					Output.get().logError( "PrisonDispatchCommand: Error trying to run task: " + errorMessage + 
							"  Task: [" + task + "] " + e.getMessage() );
				}
				
				long stop = System.nanoTime();
				long elapsed = stop - start;
				
				elapsedTimes.add( Long.valueOf( elapsed ) );

			}
			
		}
	}

	public List<PrisonCommandTaskPlaceholderData> getCustomPlaceholders() {
		return customPlaceholders;
	}
	public void setCustomPlaceholders( List<PrisonCommandTaskPlaceholderData> customPlaceholders ) {
		this.customPlaceholders = customPlaceholders;
	}
	
}

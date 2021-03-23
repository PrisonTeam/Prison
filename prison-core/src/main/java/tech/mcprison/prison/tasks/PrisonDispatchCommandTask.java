package tech.mcprison.prison.tasks;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

public class PrisonDispatchCommandTask
		implements PrisonRunnable {
	
	private List<String> tasks;
	private String errorMessage;
	
	private Player player;
	private boolean playerTask = false;
	
	private List<PrisonCommandTaskPlaceholderData> customPlaceholders;
	

	public PrisonDispatchCommandTask( List<String> tasks, String errorMessage, 
										Player player, boolean playerTask ) {
		this.tasks = tasks;
		this.errorMessage = errorMessage;
		
		this.player = player;
		this.playerTask = playerTask;
		
		this.customPlaceholders = new ArrayList<>();
	}

	/**
	 * <p>Add a custom placeholder that will be applied to each task when its running.
	 * </p>
	 * 
	 * @param placeholder The String placeholder that should include the { } escape 
	 * 						characters.
	 * @param value The value that is used to replace the placeholder.
	 */
	public void addCustomPlaceholder( String placeholder, String value ) {
		
	}
	
	@Override
	public void run() {
		if ( tasks != null && tasks.size() > 0 ) {
			
			for ( String task : tasks ) {
				
				// Apply the custom placeholders:
				for ( PrisonCommandTaskPlaceholderData cPlaceholder : getCustomPlaceholders() ) {
					if ( task.contains( cPlaceholder.getPlaceholder() ) ) {
						task = task.replace( cPlaceholder.getPlaceholder(), cPlaceholder.getValue() );
					}
				}
				
				try {
					if ( playerTask && player != null ) {
						double start = System.currentTimeMillis();
						
						PrisonAPI.dispatchCommand( player, task );
						
						double stop = System.currentTimeMillis();
						Output.get().logDebug( "PrisonDispatchCommandTask.run: (player) " + 
									(stop - start) + " ms  player= " + player.getName() + 
									"  task: " + task );
					}
					else {
						double start = System.currentTimeMillis();
						
						PrisonAPI.dispatchCommand( task );

						double stop = System.currentTimeMillis();
						Output.get().logDebug( "PrisonDispatchCommandTask.run: (console) " + 
									(stop - start) + " ms" +
									"  task: " + task );
					}
				}
				catch ( Exception e ) {

					Output.get().logError( "PrisonDispatchCommand: Error trying to run task: " + errorMessage + 
							"  Task: [" + task + "] " + e.getMessage() );
				}
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

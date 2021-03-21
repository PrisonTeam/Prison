package tech.mcprison.prison.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.mcprison.prison.internal.Player;

public class PrisonCommandTask {
	
	private String errorMessagePrefix;
	
	private int taskId;

	public enum TaskMode {
		inline, 
		inlinePlayer(true), 
		
		sync,
		syncPlayer(true);
		
		private final boolean playerTask;
		private TaskMode() {
			this(false);
		}
		private TaskMode( boolean playerTask ) {
			this.playerTask = playerTask;
		}
		
		public boolean isPlayerTask() {
			return playerTask;
		}
		
		public static TaskMode fromString( String taskMode ) {
			TaskMode results = inline;
			
			if ( taskMode != null ) {
				
				for ( TaskMode mode : values() ) {
					if ( mode.name().equalsIgnoreCase( taskMode ) ) {
						results = mode;
						
						break;
					}
				}
			}
			
			return results;
		}
	}
	
	public PrisonCommandTask( String errorMessagePrefix ) {
		super();
		
		this.errorMessagePrefix = errorMessagePrefix;
		this.taskId = 0;
	}
	
	
	
	public void submitCommandTask( String command ) {
		submitCommandTask( null, command, TaskMode.sync );
	}
	
	public void submitCommandTask( Player player, String command ) {
		submitCommandTask( player, command, TaskMode.sync );
	}
	
	public void submitCommandTask( Player player, String command, 
			TaskMode taskMode ) {
		
		String commandTranslated = translateCommand( player, command );
		
		// Split multiple commands in to a List of individual tasks:
		List<String> tasks = new ArrayList<>( 
				Arrays.asList( commandTranslated.split( ";" ) ));
		
		if ( tasks.size() > 0 ) {
			
			String errorMessage = errorMessagePrefix + ": " +
						(player == null ? "" : "Player: " + player.getName() + " ");
			
			
			PrisonDispatchCommandTask task = 
					new PrisonDispatchCommandTask( tasks, errorMessage, 
									player, taskMode.isPlayerTask() );
			
			
			switch ( taskMode )
			{
				case inline:
				case inlinePlayer:
					// Don't submit, but run it here within this thread:
					task.run();
					break;
					
				case sync:
				case syncPlayer:
				//case "async": // async will cause failures so run as sync:
					
					// submit task: 
					setTaskId( PrisonTaskSubmitter.runTaskLater(task, 0) );
					break;
					
				default:
					break;
			}
			
		}

	}
	
	
	private String translateCommand( Player player, String command ) {
		
		String formatted = command
				.replace( "{broadcast}", "prison utils broadcast " );
		
		if ( player != null ) {
			formatted = formatted
					.replace( "{msg}", "prison utils msg {player} " )
					.replace( "{player}", player.getName())
					.replace( "{player_uid}", player.getUUID().toString());
		}
		
		return formatted;
	}

	public String getErrorMessagePrefix() {
		return errorMessagePrefix;
	}
	public void setErrorMessagePrefix( String errorMessagePrefix ) {
		this.errorMessagePrefix = errorMessagePrefix;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId( int taskId ) {
		this.taskId = taskId;
	}
	
}

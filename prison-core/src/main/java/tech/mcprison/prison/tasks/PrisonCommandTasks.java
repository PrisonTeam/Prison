package tech.mcprison.prison.tasks;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

public class PrisonCommandTasks
	implements PrisonRunnable
{
	
	private Player player;
	private List<PrisonCommandTaskData> cmdTasks;
	
	private int cmTasksPosition = 0;
	
	private int taskId = 0;
	
	private PrisonCommandTasks() {
		super();
	}
	
	public static void submitTasks( PrisonCommandTaskData cmdTask ) {
		submitTasks( null, cmdTask );
	}
	
	public static void submitTasks( Player player, PrisonCommandTaskData cmdTask ) {
		
		List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
		cmdTasks.add( cmdTask );
		
		submitTasks( player, cmdTasks );
		
	}
	
	public static void submitTasks( List<PrisonCommandTaskData> cmdTasks ) {
		submitTasks( null, cmdTasks );
	}
	
	public static void submitTasks( Player player, List<PrisonCommandTaskData> cmdTasks ) {
		
		if ( cmdTasks.size() > 0 ) {
			
			PrisonCommandTasks rcTask = new PrisonCommandTasks();
			rcTask.setPlayer( player );
			rcTask.setCmdTasks( cmdTasks );
			
			rcTask.setTaskId( PrisonTaskSubmitter.runTaskLater(rcTask, 1) );
		}
	}
	
	private void resubmitTask() {
		
		taskId = PrisonTaskSubmitter.runTaskLater(this, 1);
	}
	
	@Override
	public void run() {
		
		if ( cmdTasks.size() > cmTasksPosition ) {
			
			PrisonCommandTaskData task = cmdTasks.get( cmTasksPosition++ );
			
			task.runCommandTask( getPlayer() );
			
			resubmitTask();
		}
		else if ( Output.get().isDebug() && cmTasksPosition > 0 ) {
			// Done running all tasks.  If debug is enabled, print:
			
			String message = String.format( "Prison Command Debug Details: %d", cmTasksPosition );
			Output.get().logDebug( message );
			
			for ( PrisonCommandTaskData cmdTask : cmdTasks ) {
				
				Output.get().logInfo( cmdTask.getDebugDetails() );
			}
			
		}
	}
	

	public Player getPlayer() {
		return player;
	}
	public void setPlayer( Player player ) {
		this.player = player;
	}

	public List<PrisonCommandTaskData> getCmdTasks() {
		return cmdTasks;
	}
	public void setCmdTasks( List<PrisonCommandTaskData> cmdTasks ) {
		this.cmdTasks = cmdTasks;
	}

	public int getCmTasksPosition() {
		return cmTasksPosition;
	}
	public void setCmTasksPosition( int cmTasksPosition ) {
		this.cmTasksPosition = cmTasksPosition;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId( int taskId ) {
		this.taskId = taskId;
	}

}

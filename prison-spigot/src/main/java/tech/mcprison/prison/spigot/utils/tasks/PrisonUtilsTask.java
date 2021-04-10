package tech.mcprison.prison.spigot.utils.tasks;

import java.util.List;

import tech.mcprison.prison.spigot.utils.tasks.PrisonUtilsTaskTypes.PrisonUtilsTaskTypeDelay;
import tech.mcprison.prison.spigot.utils.tasks.PrisonUtilsTaskTypes.UtilTaskType;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class PrisonUtilsTask
	implements PrisonRunnable
{
	private List<PrisonUtilsTaskTypes> tasks;
	
	public PrisonUtilsTask( List<PrisonUtilsTaskTypes> tasks ) {
		super();
		
		this.tasks = tasks;
	}

	
	public void submit() {
		PrisonTaskSubmitter.runTaskLater(this, 0);
	}
	

	@Override
	public void run()
	{
		PrisonUtilsTaskTypes task = tasks.remove( 0 );
		
		if ( task.getTaskType() == UtilTaskType.delay && 
						task instanceof PrisonUtilsTaskTypeDelay ) {
			
			int delayTicks = ((PrisonUtilsTaskTypeDelay) task).getDelayTicks();
			
			PrisonTaskSubmitter.runTaskLater(this, delayTicks);
		}
		else {
			task.run();
		}
	}

}

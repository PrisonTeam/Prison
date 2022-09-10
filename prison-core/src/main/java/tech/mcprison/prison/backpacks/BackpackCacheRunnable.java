package tech.mcprison.prison.backpacks;

import tech.mcprison.prison.tasks.PrisonRunnable;

public abstract class BackpackCacheRunnable
	implements PrisonRunnable {

	private int taskId = 0;
	private boolean cancelled = false;
	
	public BackpackCacheRunnable() {
		super();
		
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId( int taskId ) {
		this.taskId = taskId;
	}

	public void cancel() {
		setCancelled( true );
	}
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled( boolean cancelled ) {
		this.cancelled = cancelled;
	}
}

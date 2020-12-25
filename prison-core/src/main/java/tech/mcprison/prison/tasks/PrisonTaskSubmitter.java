package tech.mcprison.prison.tasks;

import tech.mcprison.prison.Prison;

public class PrisonTaskSubmitter {

    /**
     * Run a task on the server thread, after a certain amount of time.
     *
     * @param task   The {@link Runnable} with the task inside.
     * @param delayInTicks The time to wait, in ticks, until the task is run.
     * @return The task ID.
     */
	public static int runTaskLater( PrisonRunnable task, long delayInTicks ) {
		int taskId = Prison.get().getPlatform().getScheduler().runTaskLater(task, delayInTicks);
		
		return taskId;
	}
	

    /**
     * Run a task on its own thread, after a certain amount of time.
     *
     * @param task   The {@link Runnable} with the task inside.
     * @param delayInTicks The time to wait, in ticks, until the task is run.
     * @return The task ID.
     */
    public static int runTaskLaterAsync(Runnable task, long delayInTicks) {
    	int taskId = Prison.get().getPlatform().getScheduler().runTaskLaterAsync(task, delayInTicks);
    	
    	return taskId;
    }

    /**
     * Run a task on the server thread, at the specified interval.
     *
     * @param task      The {@link Runnable} with the task inside.
     * @param delayInTicks    The time to wait, in ticks, until the timer is started.
     * @param intervalInTicks The time between runs, in ticks.
     * @return The task ID.
     */
    public static int runTaskTimer(Runnable task, long delayInTicks, long intervalInTicks){
    	int taskId = Prison.get().getPlatform().getScheduler().runTaskTimer(task, delayInTicks, 
    								intervalInTicks);
    	
    	return taskId;
    }

    /**
     * Run a task on its own thread, at the specified interval.
     *
     * @param task      The {@link Runnable} with the task inside.
     * @param delayInTicks    The time to wait, in ticks, until the timer is started.
     * @param intervalInTicks The time between runs, in ticks.
     * @return The task ID.
     */
    public static int runTaskTimerAsync(Runnable task, long delayInTicks, long intervalInTicks){
    	int taskId = Prison.get().getPlatform().getScheduler().runTaskTimerAsync(task, delayInTicks, 
    								intervalInTicks);
    	
    	return taskId;
    }

    /**
     * Cancel a certain task.
     *
     * @param taskId The task's ID.
     */
    public static void cancelTask(int taskId) {
    	Prison.get().getPlatform().getScheduler().cancelTask( taskId );
    }

    /**
     * Cancels all tasks registered through this scheduler.
     */
    public static void cancelAll(){
    	Prison.get().getPlatform().getScheduler().cancelAll();
    }
    
    /**
     * Check to see if this is running in Bukkit's primary thread, if not
     * then it indicates its running as an async mode.
     * 
     * @return
     */
    public static boolean isPrimaryThread() {
    	return Prison.get().getPlatform().getScheduler().isPrimaryThread();
    }
	
}

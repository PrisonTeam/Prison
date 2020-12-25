package tech.mcprison.prison;

import tech.mcprison.prison.internal.Scheduler;

/**
 * @author Faizaan A. Datoo
 */
public class TestScheduler implements Scheduler {
    @Override public int runTaskLater(Runnable run, long delay) {
        return 0;
    }

    @Override public int runTaskLaterAsync(Runnable run, long delay) {
        return 0;
    }

    @Override public int runTaskTimer(Runnable run, long delay, long interval) {
        return 0;
    }

    @Override public int runTaskTimerAsync(Runnable run, long delay, long interval) {
        return 0;
    }

    @Override public void cancelTask(int taskId) {

    }

    @Override public void cancelAll() {

    }
    
    @Override
    public boolean isPrimaryThread() {
    	return false;
    }
}

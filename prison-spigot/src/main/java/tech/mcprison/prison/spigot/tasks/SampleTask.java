package tech.mcprison.prison.spigot.tasks;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SampleTask
	extends BukkitRunnable
{
	private final Plugin plugin;
	private BukkitTask bukkitTask;
	
	public SampleTask(Plugin plugin) {
		super();
		this.plugin = plugin;
	}
	
	/**
	 * Run a task every 1 second.
	 * 
	 * @param plugin
	 * @return
	 */
	public static SampleTask submit( Plugin plugin ) {
		return submit( plugin, 20 );
	}
	public static SampleTask submit( Plugin plugin, long delay ) {

		SampleTask task = new SampleTask(plugin);
		
		BukkitTask bTask = task.runTaskTimer( plugin, delay, delay );
		
		task.setBukkitTask( bTask );
		
		return task;
	}
	
	public void run() {
		boolean finished = false;
		
		// code: do something then let it exit:
		
		if ( finished ) {
			getBukkitTask().cancel();
		}
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public BukkitTask getBukkitTask() {
		return bukkitTask;
	}
	public void setBukkitTask( BukkitTask bukkitTask ) {
		this.bukkitTask = bukkitTask;
	}
}

package tech.mcprison.prison.spigot.spiget;

import org.inventivetalent.update.spiget.SpigetUpdate;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class PrisonSpigetUpdateCheckTask
	implements PrisonRunnable {
	
	private long delayTicks = 8 * 20; // 8 seconds
	
	private int taskId = 0;
	

	public PrisonSpigetUpdateCheckTask() {
		super();
	}

	public void submit() {
		
		taskId = PrisonTaskSubmitter.runTaskLater(this, delayTicks );
	}
	
	@Override
	public void run() {

		SpigotPrison plugin = SpigotPrison.getInstance();
		
		SpigetUpdate updater = new SpigetUpdate( plugin, Prison.SPIGOTMC_ORG_PROJECT_ID);
  
  
		BluesSpigetSemVerComparator aRealSemVerComparator = new BluesSpigetSemVerComparator();
		updater.setVersionComparator( aRealSemVerComparator );

		updater.checkForUpdate( new PrisonSpigetUpdateCallback() );
      
	}

	public long getDelayTicks() {
		return delayTicks;
	}
	public void setDelayTicks(long delayTicks) {
		this.delayTicks = delayTicks;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

}

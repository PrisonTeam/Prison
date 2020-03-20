package tech.mcprison.prison.mines.data;

public class MineCountAirBlocksAsyncTask
		implements PrisonRunnable {
	
	private MineReset mine;
	
	public MineCountAirBlocksAsyncTask(MineReset mine) {
		this.mine = mine;
	}

	@Override
	public void run() {
		this.mine.refreshAirCountAsyncTask();
	}

}

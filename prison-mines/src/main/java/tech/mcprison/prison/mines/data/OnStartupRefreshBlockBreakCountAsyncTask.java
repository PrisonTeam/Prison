package tech.mcprison.prison.mines.data;

import tech.mcprison.prison.tasks.PrisonRunnable;

public class OnStartupRefreshBlockBreakCountAsyncTask
	implements PrisonRunnable {
		
	private MineReset mine;
	
	public OnStartupRefreshBlockBreakCountAsyncTask(MineReset mine) {
		this.mine = mine;
	}
	
	@Override
	public void run() {
		this.mine.refreshAirCountAsyncTask();
		
		int airBlocks = mine.getAirCount();
		mine.setBlockBreakCount( mine.getBlockBreakCount() + airBlocks );
	}
}

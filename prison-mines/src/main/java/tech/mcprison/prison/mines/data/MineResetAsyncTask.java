package tech.mcprison.prison.mines.data;

import tech.mcprison.prison.tasks.PrisonRunnable;

public class MineResetAsyncTask
		implements PrisonRunnable {
	
	private MineReset mine;
	private PrisonRunnable callbackAsync;
	
	public MineResetAsyncTask(MineReset mine, PrisonRunnable callbackAsync) {
		this.mine = mine;
		this.callbackAsync = callbackAsync;
	}

	@Override
	public void run() {
		this.mine.refreshMineAsyncTask();
		
		if ( this.callbackAsync != null ) {
			this.mine.submitAsyncTask( callbackAsync );
		}
	}

}

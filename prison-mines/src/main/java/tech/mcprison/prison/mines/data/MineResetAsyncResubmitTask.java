package tech.mcprison.prison.mines.data;

import java.util.List;

import tech.mcprison.prison.mines.data.MineScheduler.MineResetActions;
import tech.mcprison.prison.tasks.PrisonRunnable;

public class MineResetAsyncResubmitTask
	implements PrisonRunnable 
{
		
	private MineReset mine;
	private PrisonRunnable callbackAsync;
	
	private List<MineResetActions> resetActions;
	
	public MineResetAsyncResubmitTask(MineReset mine, PrisonRunnable callbackAsync, 
			List<MineResetActions> resetActions ) {
		
		this.mine = mine;
		this.callbackAsync = callbackAsync;

		this.resetActions = resetActions;
	}
	
	@Override
	public void run() {
		
		this.mine.getCurrentJob().setResetActions( resetActions );
		
		this.mine.refreshMineAsyncResubmitTask();
		
		if ( this.callbackAsync != null ) {
			this.mine.submitAsyncTask( callbackAsync );
		}
	}
	
}

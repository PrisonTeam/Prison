package tech.mcprison.prison.mines.data;

public class MineResetAsyncResubmitTask
	implements PrisonRunnable 
{
		
	private MineReset mine;
	private PrisonRunnable callbackAsync;
	
	public MineResetAsyncResubmitTask(MineReset mine, PrisonRunnable callbackAsync) {
		this.mine = mine;
		this.callbackAsync = callbackAsync;
	}
	
	@Override
	public void run() {
		this.mine.refreshMineAsyncResubmitTask();
		
		if ( this.callbackAsync != null ) {
			this.mine.submitAsyncTask( callbackAsync );
		}
	}
}

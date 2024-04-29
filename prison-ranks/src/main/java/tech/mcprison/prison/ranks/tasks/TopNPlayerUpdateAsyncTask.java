package tech.mcprison.prison.ranks.tasks;

import tech.mcprison.prison.ranks.data.TopNPlayers;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class TopNPlayerUpdateAsyncTask 
		implements PrisonRunnable {

	private TopNPlayers topNPlayers;
	
	private boolean forceReload = true;
	
//	private boolean startup = true;
	
	public TopNPlayerUpdateAsyncTask( TopNPlayers topNPlayers ) {
		super();
		
		this.topNPlayers = topNPlayers;
	}
	
	public static void submitTaskTimerAsync( TopNPlayers topNPlayers, 
									Long delayTicks, long intervalTicks  ) {
		
		TopNPlayerUpdateAsyncTask asyncTask = 
							new TopNPlayerUpdateAsyncTask( topNPlayers );
		
		topNPlayers.setUpdaterTask( asyncTask );
		
		PrisonTaskSubmitter.runTaskTimerAsync( asyncTask, delayTicks, intervalTicks );
	}
	
	@Override
	public void run() {
		
//		if ( startup ) {
//			startup = false;
//			
//			topNPlayers.loadSaveFile();
//		}
		
		if ( forceReload ) {
			topNPlayers.setLoading( true );
			
			topNPlayers.forceReloadAllPlayers();
		
			topNPlayers.setLoading( false );
			forceReload = false;
		}
		else {
			
			topNPlayers.setLoading( true );
			
			topNPlayers.refreshAndSort();
			
			topNPlayers.setLoading( false );
		}
	}

	public boolean isForceReload() {
		return forceReload;
	}
	public void setForceReload(boolean forceReload) {
		this.forceReload = forceReload;
	}
	
}

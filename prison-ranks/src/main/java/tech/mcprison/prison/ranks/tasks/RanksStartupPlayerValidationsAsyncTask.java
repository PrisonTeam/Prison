package tech.mcprison.prison.ranks.tasks;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.TopNPlayers;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class RanksStartupPlayerValidationsAsyncTask
	implements PrisonRunnable {

	private PrisonRanks pRanks;
	
	public RanksStartupPlayerValidationsAsyncTask( PrisonRanks pRanks ) {
		super();
		
		this.pRanks = pRanks;
	}
	
	public static void submitTaskSync( PrisonRanks pRanks ) {
		
		RanksStartupPlayerValidationsAsyncTask rspvaTask = 
							new RanksStartupPlayerValidationsAsyncTask( pRanks );

		PrisonTaskSubmitter.runTaskLaterAsync( rspvaTask, 0 );
	}
	
	@Override
	public void run() {
		
		pRanks.checkAllPlayersForJoin();
		
		
        // Start up the TopNPlayer's collections after all players have been loaded:
        // NOTE: getting the instance of TopNPlayers must be done "after" player validation.
        //       So that thread needs to initiate it after done validating and fixing all players.
        TopNPlayers.getInstance();

		
//		// The following can take awhile to run if there are a lot of players
//		// and if they need to load their balance.  This is impacted more so if
//		// there is a high cost to get the balance.
//		pRanks.getPlayerManager().sortPlayerByTopRanked();
		
	}
}

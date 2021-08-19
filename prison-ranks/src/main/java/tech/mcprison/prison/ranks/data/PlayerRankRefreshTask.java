package tech.mcprison.prison.ranks.data;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

/**
 * <p>This task will process all players and recalculate their rank multipliers.
 * This task should be ran whenever a ladder's base rank multiplier changes, 
 * or if a rank is added or removed from a ladder.
 * </p>
 * 
 * <p>This should not be ran when a player ranks up since only that player should
 * have it's multipliers recalculated, and all the other players shouldn't 
 * have no reason to be recalculated.
 * </p>
 *
 */
public class PlayerRankRefreshTask
	implements PrisonRunnable
{
	
	public void submitAsyncTPSTask() {
		
		PrisonTaskSubmitter.runTaskLaterAsync( this, 1 );
	}
	
	
	public void run() {
		
		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		
		for ( RankPlayer rPlayer : pm.getPlayers() ) {
			
			rPlayer.recalculateRankMultipliers();
		}
	}
}

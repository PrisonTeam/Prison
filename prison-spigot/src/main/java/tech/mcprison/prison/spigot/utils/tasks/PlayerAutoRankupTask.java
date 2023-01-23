package tech.mcprison.prison.spigot.utils.tasks;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.utils.tasks.PrisonUtilsTaskTypes.PrisonUtilsTaskTypRunCommand;

public class PlayerAutoRankupTask {

	
	/**
	 * <p>If the config.yml settings 'prison-mines.forced-auto-rankups' is enabled,
	 * then it will check to see if the player has enough money to rankup.
	 * This will perform all rankups; `/rankup` and `/prestige`.  The prestige is 
	 * actually just `/rankup prestiges [playerName]`.
	 * </p>
	 * 
	 * @param sPlayer
	 */
	public static void autoSubmitPlayerRankupTask( SpigotPlayer sPlayer, StringBuilder debugInfo ) {
		
		if ( Prison.get().getPlatform().getConfigBooleanFalse( "prison-mines.forced-auto-rankups" ) ) {
			
			RankPlayer rPlayer = sPlayer.getRankPlayer();
			
			PlayerRank nextRank = rPlayer.getNextPlayerRank();
			
			if ( nextRank != null ) {
				
				String currency = nextRank.getCurrency();
				double balance = rPlayer.getBalance( currency );
				
				double rankBalance = nextRank.getRankCost();
				
				if ( balance >= rankBalance ) {
					
					if ( debugInfo != null ) {
						debugInfo.append( "(forcing auto rankup) " );
					}
					
					String cmd = 
							String.format( "rankup %s %s",
									nextRank.getRank().getLadder().getName(),
									rPlayer.getName() );
					
					List<PrisonUtilsTaskTypes> tasks = new ArrayList<>();
					PrisonUtilsTaskTypRunCommand task = new PrisonUtilsTaskTypRunCommand( rPlayer, cmd );
					tasks.add( task );
					
					PrisonUtilsTask taskRunner = new PrisonUtilsTask( tasks );
					taskRunner.submit();
					
				}
			}
			
		}
	}
}

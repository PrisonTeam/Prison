package tech.mcprison.prison.ranks.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tech.mcprison.prison.Prison;

public class StatsRankPlayerBalance
{
	public static final long REFRESH_INTERVAL = 5 * 60 * 1000; // 5 minutes
	private final Rank rank;
	private long lastRefresh = 0;
	
	private transient final List<StatsRankPlayerBalanceData> playerStats;
	 
	public StatsRankPlayerBalance( Rank rank ) {
		super();
	
		this.rank = rank;
		
		this.playerStats = new ArrayList<>();
	}
	
	public void refresh() {
		long current = System.currentTimeMillis();
		if ( current > lastRefresh + REFRESH_INTERVAL ) {
			
			boolean penalty = isHesitancyDelayPenaltyEnabled();
			// refresh time:
			for ( StatsRankPlayerBalanceData playerStats : playerStats ) {
				playerStats.recalc( penalty );
			}
			
			Collections.sort( playerStats );
		}
	}
	
	public void addPlayer( RankPlayer player ) {
		
		StatsRankPlayerBalanceData pStats = new StatsRankPlayerBalanceData( rank, 
													player, isHesitancyDelayPenaltyEnabled() );
		if ( !playerStats.contains( pStats ) ) {
			playerStats.add( pStats );
		}
	}
	
	public void removePlayer( RankPlayer player ) {
		
		StatsRankPlayerBalanceData pStats = new StatsRankPlayerBalanceData( rank, 
				player, isHesitancyDelayPenaltyEnabled() );
		if ( playerStats.contains( pStats ) ) {
			playerStats.remove( pStats );
		}
	}
	
	public StatsRankPlayerBalanceData getTopStats( int position ) {
		StatsRankPlayerBalanceData stats = null;
		if ( position > 0 && position <= playerStats.size() ) {
			
			// check to see if the stats should be refreshed:
			refresh();
			
			playerStats.get( position - 1 );
		}
		return stats;
	}
	
	public boolean isHesitancyDelayPenaltyEnabled() {
		return Prison.get().getPlatform()
				.getConfigBooleanTrue( "top-stats.rank-players.hesitancy-delay-penalty" );
	}
}

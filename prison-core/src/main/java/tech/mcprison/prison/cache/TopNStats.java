package tech.mcprison.prison.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopNStats
{

	private static TopNStats instance;
	
	List<TopNStatsData> mainList;

	List<TopNStatsData> topBlocksList;
	List<TopNStatsData> topTokensList;
	List<TopNStatsData> topBalancesList;
	
	List<TopNStatsData> topRanksList;
	
	
	
	private TopNStats() {
		super();
	
		this.mainList = new ArrayList<>();
	}
	
	public static TopNStats getInstance() {
		if ( instance == null ) {
			synchronized ( TopNStats.class )
			{
				if ( instance == null ) {

					instance = new TopNStats();
				}
			}
		}
		return instance;
	}
	
	
	protected void sortAllLists() {
		
		Collections.sort( topBlocksList, new TopNBlocksComparator() );
		
		Collections.sort( topTokensList, new TopNTokensComparator() );
		
		Collections.sort( topBalancesList, new TopNBalancesComparator() );

		Collections.sort( topRanksList, new TopNRanksComparator() );
		
	}
	
	
}

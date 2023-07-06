package tech.mcprison.prison.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopNStatsSingleton
{

	private static TopNStatsSingleton instance;
	
	List<TopNStatsData> mainList;

	List<TopNStatsData> topBlocksList;
	List<TopNStatsData> topTokensList;
	List<TopNStatsData> topBalancesList;
	
	List<TopNStatsData> topRanksList;
	
	
	
	private TopNStatsSingleton() {
		super();
	
		this.mainList = new ArrayList<>();
	}
	
	public static TopNStatsSingleton getInstance() {
		if ( instance == null ) {
			synchronized ( TopNStatsSingleton.class )
			{
				if ( instance == null ) {

					instance = new TopNStatsSingleton();
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

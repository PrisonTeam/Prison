package tech.mcprison.prison.cache;

import java.util.Comparator;

public class TopNRanksComparator
	implements Comparator<TopNStatsData>
{
	@Override
	public int compare( TopNStatsData o1, TopNStatsData o2 ) {
		
		int results = 0;
		
		if ( o1.getTopRankPrestiges() == null && o2.getTopRankPrestiges() == null ) {
			results = 0;
		}
		if ( o1.getTopRankPrestiges() == null ) {
			return 1;
		}
		if ( o2.getTopRankPrestiges() == null ) {
			return -1;
		}
		
		if ( o1.getTopRankDefault() == null && o2.getTopRankDefault() == null ) {
			return Double.compare( o1.getCurrentBalance(), o2.getCurrentBalance() );
		}
		if ( o1.getTopRankDefault() == null ) {
			return 1;
		}
		if ( o2.getTopRankDefault() == null ) {
			return -1;
		}
		
		results = Integer.compare( 
					o1.getTopRankPrestiges().getPosition(),  
					o2.getTopRankPrestiges().getPosition() );
		
		if ( results == 0 ) {
			
			results = Integer.compare( 
					o1.getTopRankDefault().getPosition(),  
					o2.getTopRankDefault().getPosition() );
			
			if ( results == 0 ) {
				
				results = Double.compare( o1.getCurrentBalance(), o2.getCurrentBalance() );
			}
			
		}
				
		return 0;
	}
}

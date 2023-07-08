package tech.mcprison.prison.cache;

import java.util.Comparator;

/**
 * <p>This is a comparator for the basic TopNStatsData.
 * It first compares the Prestiges ladder ranks, if both are the 
 * same then it will compare the Default ladder ranks. 
 * If both of those are the same, then it will compare the 
 * balances.
 * </p>
 * 
 * @author Blue
 *
 */
public class TopNRanksComparator
	implements Comparator<TopNStatsData>
{
	@Override
	public int compare( TopNStatsData o1, TopNStatsData o2 ) {
		
		int results = 0;
		
		if ( o1.getTopRankPrestiges() == null && o2.getTopRankPrestiges() == null ) {
			
			// Both prestiges are null, so set results to 0 and then we need to check the default ranks
			results = 0;
		}
		else if ( o1.getTopRankPrestiges() == null ) {
			// One is not null, so set to 1 and exit:
			results = 1;
		}
		else if ( o2.getTopRankPrestiges() == null ) {
			// One is not null, so set to -1 and exit:
			results = -1;
		}
		else {
			// Must now evaluate the prestige ranks since both are not null:
			int o1Pos = o1.getTopRankPrestiges().getPosition();
			int o2Pos = o2.getTopRankPrestiges().getPosition();
			
			results = Integer.compare(o1Pos, o2Pos);
		}
		
		if ( results == 0 ) {
			
			if ( o1.getTopRankDefault() == null && o2.getTopRankDefault() == null ) {
				results = 0;
			}
			else if ( o1.getTopRankDefault() == null ) {
				results = 1;
			}
			else if ( o2.getTopRankDefault() == null ) {
				results = -1;
			}
			else {
				
				// Must now evaluate the default ranks since both are not null:
				int o1Pos = o1.getTopRankDefault().getPosition();
				int o2Pos = o2.getTopRankDefault().getPosition();
				
				results = Integer.compare(o1Pos, o2Pos);
			}
			
			if ( results == 0 ) {
				// Next compare the balances:
				results = Double.compare( o1.getCurrentBalance(), o2.getCurrentBalance() );
				
			}
		}

				
		return results;
	}
}

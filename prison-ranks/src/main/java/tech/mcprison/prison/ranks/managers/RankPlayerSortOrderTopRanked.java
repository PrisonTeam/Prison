package tech.mcprison.prison.ranks.managers;

import java.util.Comparator;

import tech.mcprison.prison.ranks.data.RankPlayer;

public class RankPlayerSortOrderTopRanked
	implements Comparator<RankPlayer>
{

	
	public RankPlayerSortOrderTopRanked() {
		super();

	}
	
	
	@Override
	public int compare( RankPlayer rp1, RankPlayer rp2 )
	{
		int results = 0;
		
		if ( rp1 == null && rp2 == null ) {
			results = 0;
		}
		else if ( rp1 == null ) {
			results = -1;
		}
		else if ( rp2 == null ) {
			results = 1;
		}
		else {
			results = compareLadderPrestiges( rp1, rp2 );
			
			if ( results == 0 ) {
				
				results = compareLadderDefault( rp1, rp2 );
				
				if ( results == 0 ) {
					results = Double.compare( rp1.getRankScore(), rp2.getRankScore() );
				}
			}
		}
		
		return results;
	}	
	
	private int compareLadderPrestiges( RankPlayer rp1, RankPlayer rp2 ) {
		int results = 0;
		
		if ( rp1.getPlayerRankPrestiges() == null && rp2.getPlayerRankPrestiges() == null ) {
			results = 0;
		}
		else if ( rp2.getPlayerRankPrestiges() == null ) {
//  Handled when comparing PlayerRanks... ??
			results = -1;
		}
		else if ( rp1.getPlayerRankPrestiges() == null ) {
			results = 1;
		}
		else {
			results = rp2.getPlayerRankPrestiges().compareTo( rp1.getPlayerRankPrestiges() );
		}
		
		return results;
	}
	
	private int compareLadderDefault( RankPlayer rp1, RankPlayer rp2 ) {
		int results = 0;
		
		if ( rp1.getPlayerRankDefault() == null && rp2.getPlayerRankDefault() == null ) {
			results = 0;
		}
		else if ( rp2.getPlayerRankDefault() == null ) {
//  Handled when comparing PlayerRanks... 
			results = -1;
		}
		else if ( rp1.getPlayerRankDefault() == null ) {
			results = 1;
		}
		else {
			results = rp2.getPlayerRankDefault().compareTo( rp1.getPlayerRankDefault() );
		}
		
		return results;
	}

}

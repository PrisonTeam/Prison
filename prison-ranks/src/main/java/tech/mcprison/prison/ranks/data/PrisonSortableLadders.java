package tech.mcprison.prison.ranks.data;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.sorting.PrisonSorter;

public class PrisonSortableLadders
		extends PrisonSorter
{

	protected class PrisonSortComparableLadders
			implements Comparator<RankLadder>
	{

		@Override
		public int compare( RankLadder l1, RankLadder l2 ) {
			int results = 0;

			if ( l1 == null ) {
				results = -1;
			}
			else if ( l2 == null ) {
				results = 1;
			}
			else if ( "default".equalsIgnoreCase( l1.getName() ) ) {
				results = -999999;
			} 
			else if ( "prestige".equalsIgnoreCase( l1.getName() )) {
				results = 999999;
			} 
			else {
				results = l1.getName().toLowerCase().compareTo( l2.getName().toLowerCase() );
			}

			return results;
		}

	}

	@Override
	public Set<RankLadder> getSortedSet()
	{
		TreeSet<RankLadder> ladders = new TreeSet<>( new PrisonSortComparableLadders() );

		List<RankLadder> unsortedLadders = PrisonRanks.getInstance().getLadderManager().getLadders();
		
		ladders.addAll( unsortedLadders );

		return ladders;
	}
}

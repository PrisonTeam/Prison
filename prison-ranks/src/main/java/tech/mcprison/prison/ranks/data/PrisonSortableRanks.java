package tech.mcprison.prison.ranks.data;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.sorting.PrisonSorter;

/**
 * <p>This class sorts all ranks by alphabetical order. Ladders are not
 * considered in this sorting.
 * </p>
 *
 */
public class PrisonSortableRanks
		extends PrisonSorter {


	protected class PrisonSortComparableRanks
			implements Comparator<Rank>
	{

		@Override
		public int compare( Rank r1, Rank r2 ) {
			int results = 0;

			if ( r1 == null ) {
				results = -1;
			}
			else if ( r2 == null ) {
				results = 1;
			}
			else {
				results = r1.getName().toLowerCase().compareTo( r2.getName().toLowerCase() );
			}

			return results;
		}

	}


	@Override
	public Set<Rank> getSortedSet()
	{
		TreeSet<Rank> ranks = new TreeSet<>( new PrisonSortComparableRanks() );

		List<Rank> unsortedRanks = PrisonRanks.getInstance().getRankManager().getRanks();
		
		ranks.addAll( unsortedRanks );

		return ranks;
	}
	
}

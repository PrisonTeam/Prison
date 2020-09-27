package tech.mcprison.prison.ranks.data;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.sorting.PrisonSorter;

public class PrisonSortableLaddersRanks
		extends PrisonSorter {


	@Override
	public Set<Rank> getSortedSet()
	{
		Set<Rank> results = new LinkedHashSet<>();
		
		// Get a sorted ladder set:
		PrisonSortableLadders psl = new PrisonSortableLadders();
		TreeSet<RankLadder> ladders = new TreeSet<>( psl.new PrisonSortComparableLadders() );
		List<RankLadder> unsortedLadders = PrisonRanks.getInstance().getLadderManager().getLadders();
		ladders.addAll( unsortedLadders );


		List<Rank> unsortedRanks = PrisonRanks.getInstance().getRankManager().getRanks();


		// We may not want to sort the ranks within the ladders by alpha.
		// the rank list we get from the ladders is in rank-order so just use that.
		// The following code for PrisonSortableRanks is shown here and commented out
		// to serve as a guide on how you "could" perform multi-tiered sorting 
		// through the use of multiple comparators and collections.
		PrisonSortableRanks psr = new PrisonSortableRanks();
		TreeSet<Rank> ranksSorted = new TreeSet<>( psr.new PrisonSortComparableRanks() );
		

		for ( RankLadder rankLadder : ladders ) {
			List<Rank> rankList = rankLadder.getRanks();
			
			// Perform the sort of the ranks:
//			ranksSorted.addAll( rankList );
			
			// Add the ranks to the result set in sorted order:
			results.addAll( rankList );
//			results.addAll( ranksSorted );
			
			// Remove from the unsortedRanks list, all ranks that were used:
			unsortedRanks.removeAll( rankList );
//			unsortedRanks.removeAll( ranksSorted );
		}
		
		
		// Finally if there are any leftover ranks that have not yet been added
		// then sort there before adding to the results.
		
		ranksSorted.addAll( unsortedRanks );
		results.addAll( ranksSorted );
		

		return results;
	}
	
}

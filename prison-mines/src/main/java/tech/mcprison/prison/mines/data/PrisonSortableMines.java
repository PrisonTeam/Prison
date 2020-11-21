package tech.mcprison.prison.mines.data;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.sorting.PrisonSorter;

@Deprecated
public class PrisonSortableMines
		extends PrisonSorter {

	public class PrisonSortComparableMines
					implements Comparator<Mine> {

		@Override
		public int compare( Mine m1, Mine m2 )
		{
			int results = 0;
			
			if ( m1 == null ) {
				results = -1;
			}
			else if ( m2 == null ) {
				results = 1;
			}
			else {
				results = m1.getName().toLowerCase().compareTo( m2.getName().toLowerCase() );
			}
			
			return results;
		}
		
	}


	@Override
	public Set<Mine> getSortedSet() {
		TreeSet<Mine> mines = new TreeSet<>( new PrisonSortComparableMines() );
		
		List<Mine> unsortedMines = PrisonMines.getInstance().getMineManager().getMines();
		
		mines.addAll( unsortedMines );
		return mines;
	}

}

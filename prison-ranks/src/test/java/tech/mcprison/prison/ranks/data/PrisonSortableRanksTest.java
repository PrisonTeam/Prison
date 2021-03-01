package tech.mcprison.prison.ranks.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

public class PrisonSortableRanksTest
		extends
		PrisonSortableRanks
{

	@Test
	public void test()
	{
		Rank rA = new Rank("A");
		Rank rB = new Rank("b");
		Rank rC = new Rank("c");
		Rank rD = new Rank("D");
		
		
		
		List<Rank> unsortedList = new ArrayList<>();
		unsortedList.add( rD );
		unsortedList.add( rC );
		unsortedList.add( rB );
		unsortedList.add( rA );
		

		assertEquals( "D", unsortedList.get( 0 ).getName() );
		assertEquals( "c", unsortedList.get( 1 ).getName() );
		assertEquals( "b", unsortedList.get( 2 ).getName() );
		assertEquals( "A", unsortedList.get( 3 ).getName() );
		
		TreeSet<Rank> ranks = new TreeSet<>( new PrisonSortComparableRanks() );
		
		ranks.addAll( unsortedList );
		
		List<Rank> sortedList = new ArrayList<>(ranks);
		
		for ( Rank r : sortedList )
		{
			System.out.println(r.toString());
		}
		
		assertEquals( "A", sortedList.get( 0 ).getName() );
		assertEquals( "b", sortedList.get( 1 ).getName() );
		assertEquals( "c", sortedList.get( 2 ).getName() );
		assertEquals( "D", sortedList.get( 3 ).getName() );

		
	}

}

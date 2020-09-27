package tech.mcprison.prison.mines.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

public class PrisonSortableMinesTest
		extends PrisonSortableMines
{

	/**
	 * A simple alphabetic test of mines that are case insensitive.
	 */
	@Test
	public void testGetSortedSet()
	{
		Mine a = new Mine();
		a.setName( "A" );
		Mine b = new Mine();
		b.setName( "b" );
		Mine c = new Mine();
		c.setName( "C" );
		
		List<Mine> unsortedList = new ArrayList<>();
		unsortedList.add( b );
		unsortedList.add( c );
		unsortedList.add( a );
		
		assertEquals( "b", unsortedList.get( 0 ).getName() );
		assertEquals( "C", unsortedList.get( 1 ).getName() );
		assertEquals( "A", unsortedList.get( 2 ).getName() );
		
		TreeSet<Mine> mines = new TreeSet<>( new PrisonSortComparableMines() );
		
		mines.addAll( unsortedList );
		
		List<Mine> sortedList = new ArrayList<>(mines);
		
		assertEquals( "A", sortedList.get( 0 ).getName() );
		assertEquals( "b", sortedList.get( 1 ).getName() );
		assertEquals( "C", sortedList.get( 2 ).getName() );
		
	}

}

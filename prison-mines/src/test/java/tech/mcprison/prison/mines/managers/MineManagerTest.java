package tech.mcprison.prison.mines.managers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;

public class MineManagerTest
		extends
		MineManager
{
	private List<Mine> getTestMines() {
		List<Mine> mines = new ArrayList<>();
		
		Mine a = new Mine();
		a.setName( "A" );
		Mine b = new Mine();
		b.setName( "b" );
		Mine c = new Mine();
		c.setName( "C" );
		Mine d = new Mine();
		d.setName( "D" );

		mines.add( d );
		mines.add( b );
		mines.add( a );
		mines.add( c );
		
		return mines;
	}
	
	public String toString(List<Mine> mines, String desc ) {
		StringBuilder sb = new StringBuilder();
		
		for ( Mine mine : mines ) {
			if ( sb.length() > 0 ) {
				sb.append( " " );
			}
			sb.append( mine.getName() + " : sort= " + mine.getSortOrder() + 
					" mined= " + mine.getTotalBlocksMined() + ", ");
		}
		
		sb.insert( 0, desc + "\n" );
		sb.append( "\n - - - - \n" );
		return sb.toString();
	}

	@Test
	public void testGetMinesMineSortOrderListOfMine() {
		List<Mine> mines = getTestMines();
		
		
		// check to make sure the mines are in an unsorted order:
		assertEquals( "D", mines.get( 0 ).getName() );
		assertEquals( "b", mines.get( 1 ).getName() );
		assertEquals( "A", mines.get( 2 ).getName() );
		assertEquals( "C", mines.get( 3 ).getName() );
		
		System.out.println( toString(mines, "Before sort:") );

		// sort by alpha and confirm:
		PrisonSortableResults sorted = getMines( MineSortOrder.alpha, mines );
		System.out.println( toString(sorted.getSortedList(), "After alpha:") );
		
		assertEquals( "A", sorted.getSortedList().get( 0 ).getName() );
		assertEquals( "b", sorted.getSortedList().get( 1 ).getName() );
		assertEquals( "C", sorted.getSortedList().get( 2 ).getName() );
		assertEquals( "D", sorted.getSortedList().get( 3 ).getName() );
		
		// Set mine b to sortOrder -1 so it is not included in alpha sort:
		mines.get( 1 ).setSortOrder( -1 );
		
		// Resort with alpha and b should not be included:
		sorted = getMines( MineSortOrder.alpha, sorted.getSortedList() );
		System.out.println( toString(sorted.getSortedList(), "After alpha without b:") );

		// Without mine b:
		assertEquals( "A", sorted.getSortedList().get( 0 ).getName() );
		// assertEquals( "b", sorted.getSortedList().get( 1 ).getName() );
		assertEquals( "C", sorted.getSortedList().get( 1 ).getName() );
		assertEquals( "D", sorted.getSortedList().get( 2 ).getName() );
		
		
		// Reset and now sort with alphaAll:
		mines = getTestMines();	// d, b, a, c
		mines.get( 1 ).setSortOrder( -1 );
		sorted = getMines( MineSortOrder.xAlpha, mines );
		System.out.println( toString(sorted.getSortedList(), "After xAlpha with only the excluded:") );
		
//		assertEquals( "A", sorted.getSortedList().get( 0 ).getName() );
		assertEquals( "b", sorted.getSortedList().get( 0 ).getName() );
//		assertEquals( "C", sorted.getSortedList().get( 2 ).getName() );
//		assertEquals( "D", sorted.getSortedList().get( 3 ).getName() );
		
	
		// Reset and now sort with active:
		mines = getTestMines();	// d, b, a, c
		//mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 ); // b
		mines.get( 3 ).setTotalBlocksMined( 1 ); // C
		sorted = getMines( MineSortOrder.active, mines );
		System.out.println( toString(sorted.getSortedList(), "After active:") );
		
		assertEquals( "b", sorted.getSortedList().get( 0 ).getName() );
		assertEquals( "C", sorted.getSortedList().get( 1 ).getName() );
		assertEquals( "A", sorted.getSortedList().get( 2 ).getName() );
		assertEquals( "D", sorted.getSortedList().get( 3 ).getName() );

		
		// Reset and now sort with active:
		mines = getTestMines();	// d, b, a, c
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		sorted = getMines( MineSortOrder.active, mines );
		System.out.println( toString(sorted.getSortedList(), "After active & suppress:") );
		
		// assertEquals( "b", sorted.getSortedList().get( 0 ).getName() );
		assertEquals( "C", sorted.getSortedList().get( 0 ).getName() );
		assertEquals( "A", sorted.getSortedList().get( 1 ).getName() );
		assertEquals( "D", sorted.getSortedList().get( 2 ).getName() );
		
	
		
		// Reset and now sort with allActive:
		mines = getTestMines();	// d, b, a, c
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		sorted = getMines( MineSortOrder.xActive, mines );
		System.out.println( toString(sorted.getSortedList(), "After xActive:") );
		
		assertEquals( "b", sorted.getSortedList().get( 0 ).getName() );
//		assertEquals( "C", sorted.getSortedList().get( 1 ).getName() );
//		assertEquals( "A", sorted.getSortedList().get( 2 ).getName() );
//		assertEquals( "D", sorted.getSortedList().get( 3 ).getName() );
		
		
		
		
		// Reset and now sort with sortOrder and no -1:
		mines = getTestMines();	// d, b, a, c
		mines.get( 0 ).setSortOrder( 9 );
		mines.get( 1 ).setSortOrder( 7 );
		mines.get( 2 ).setSortOrder( 5 );
		mines.get( 3 ).setSortOrder( 3 );
		//mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		sorted = getMines( MineSortOrder.sortOrder, mines );
		System.out.println( toString(sorted.getSortedList(), "After sortOrder:") );
		
		assertEquals( "C", sorted.getSortedList().get( 0 ).getName() );
		assertEquals( "A", sorted.getSortedList().get( 1 ).getName() );
		assertEquals( "b", sorted.getSortedList().get( 2 ).getName() );
		assertEquals( "D", sorted.getSortedList().get( 3 ).getName() );
		

		
		
		// Reset and now sort with sortOrder with a -1 on b:
		mines = getTestMines();	// d, b, a, c
		mines.get( 0 ).setSortOrder( 9 );
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 2 ).setSortOrder( 5 );
		mines.get( 3 ).setSortOrder( 3 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		sorted = getMines( MineSortOrder.sortOrder, mines );
		System.out.println( toString(sorted.getSortedList(), "After sortOrder:") );
		
		assertEquals( "C", sorted.getSortedList().get( 0 ).getName() );
		assertEquals( "A", sorted.getSortedList().get( 1 ).getName() );
		// assertEquals( "b", sorted.getSortedList().get( 2 ).getName() );
		assertEquals( "D", sorted.getSortedList().get( 2 ).getName() );
		
		
		// Reset and now sort with sortOrder with a -1 on b:
		mines = getTestMines();	// d, b, a, c
		mines.get( 0 ).setSortOrder( 9 );
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 2 ).setSortOrder( 5 );
		mines.get( 3 ).setSortOrder( 3 );
		mines.get( 3 ).setTotalBlocksMined( 5 );
		mines.get( 0 ).setTotalBlocksMined( 1 );
		sorted = getMines( MineSortOrder.xSortOrder, mines );
		System.out.println( toString(sorted.getSortedList(), "After xSortOrder:") );
		
		assertEquals( "b", sorted.getSortedList().get( 0 ).getName() );
//		assertEquals( "C", sorted.getSortedList().get( 1 ).getName() );
//		assertEquals( "A", sorted.getSortedList().get( 2 ).getName() );
//		assertEquals( "D", sorted.getSortedList().get( 3 ).getName() );

	}

}

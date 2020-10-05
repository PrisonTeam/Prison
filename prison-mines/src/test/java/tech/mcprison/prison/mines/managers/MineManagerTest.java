package tech.mcprison.prison.mines.managers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tech.mcprison.prison.mines.data.Mine;

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
		mines = getMines( MineSortOrder.alpha, mines );
		System.out.println( toString(mines, "After alpha:") );
		
		assertEquals( "A", mines.get( 0 ).getName() );
		assertEquals( "b", mines.get( 1 ).getName() );
		assertEquals( "C", mines.get( 2 ).getName() );
		assertEquals( "D", mines.get( 3 ).getName() );
		
		// Set mine b to sortOrder -1 so it is not included in alpha sort:
		mines.get( 1 ).setSortOrder( -1 );
		
		// Resort with alpha and b should not be included:
		mines = getMines( MineSortOrder.alpha, mines );
		System.out.println( toString(mines, "After alpha without b:") );

		// Without mine b:
		assertEquals( "A", mines.get( 0 ).getName() );
		// assertEquals( "b", mines.get( 1 ).getName() );
		assertEquals( "C", mines.get( 1 ).getName() );
		assertEquals( "D", mines.get( 2 ).getName() );
		
		
		// Reset and now sort with alphaAll:
		mines = getTestMines();	// d, b, a, c
		mines.get( 1 ).setSortOrder( -1 );
		mines = getMines( MineSortOrder.allAlpha, mines );
		System.out.println( toString(mines, "After allAlpha with all:") );
		
		assertEquals( "A", mines.get( 0 ).getName() );
		assertEquals( "b", mines.get( 1 ).getName() );
		assertEquals( "C", mines.get( 2 ).getName() );
		assertEquals( "D", mines.get( 3 ).getName() );
		
	
		// Reset and now sort with active:
		mines = getTestMines();	// d, b, a, c
		//mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 ); // b
		mines.get( 3 ).setTotalBlocksMined( 1 ); // C
		mines = getMines( MineSortOrder.active, mines );
		System.out.println( toString(mines, "After active:") );
		
		assertEquals( "b", mines.get( 0 ).getName() );
		assertEquals( "C", mines.get( 1 ).getName() );
		assertEquals( "A", mines.get( 2 ).getName() );
		assertEquals( "D", mines.get( 3 ).getName() );

		
		// Reset and now sort with active:
		mines = getTestMines();	// d, b, a, c
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		mines = getMines( MineSortOrder.active, mines );
		System.out.println( toString(mines, "After active & suppress:") );
		
		// assertEquals( "b", mines.get( 0 ).getName() );
		assertEquals( "C", mines.get( 0 ).getName() );
		assertEquals( "A", mines.get( 1 ).getName() );
		assertEquals( "D", mines.get( 2 ).getName() );
		
	
		
		// Reset and now sort with allActive:
		mines = getTestMines();	// d, b, a, c
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		mines = getMines( MineSortOrder.allActive, mines );
		System.out.println( toString(mines, "After allActive:") );
		
		assertEquals( "b", mines.get( 0 ).getName() );
		assertEquals( "C", mines.get( 1 ).getName() );
		assertEquals( "A", mines.get( 2 ).getName() );
		assertEquals( "D", mines.get( 3 ).getName() );
		
		
		
		
		// Reset and now sort with sortOrder and no -1:
		mines = getTestMines();	// d, b, a, c
		mines.get( 0 ).setSortOrder( 9 );
		mines.get( 1 ).setSortOrder( 7 );
		mines.get( 2 ).setSortOrder( 5 );
		mines.get( 3 ).setSortOrder( 3 );
		//mines.get( 1 ).setSortOrder( -1 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		mines = getMines( MineSortOrder.sortOrder, mines );
		System.out.println( toString(mines, "After sortOrder:") );
		
		assertEquals( "C", mines.get( 0 ).getName() );
		assertEquals( "A", mines.get( 1 ).getName() );
		assertEquals( "b", mines.get( 2 ).getName() );
		assertEquals( "D", mines.get( 3 ).getName() );
		

		
		
		// Reset and now sort with sortOrder with a -1 on b:
		mines = getTestMines();	// d, b, a, c
		mines.get( 0 ).setSortOrder( 9 );
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 2 ).setSortOrder( 5 );
		mines.get( 3 ).setSortOrder( 3 );
		mines.get( 1 ).setTotalBlocksMined( 5 );
		mines.get( 3 ).setTotalBlocksMined( 1 );
		mines = getMines( MineSortOrder.sortOrder, mines );
		System.out.println( toString(mines, "After sortOrder:") );
		
		assertEquals( "C", mines.get( 0 ).getName() );
		assertEquals( "A", mines.get( 1 ).getName() );
		// assertEquals( "b", mines.get( 2 ).getName() );
		assertEquals( "D", mines.get( 2 ).getName() );
		
		
		// Reset and now sort with sortOrder with a -1 on b:
		mines = getTestMines();	// d, b, a, c
		mines.get( 0 ).setSortOrder( 9 );
		mines.get( 1 ).setSortOrder( -1 );
		mines.get( 2 ).setSortOrder( 5 );
		mines.get( 3 ).setSortOrder( 3 );
		mines.get( 3 ).setTotalBlocksMined( 5 );
		mines.get( 0 ).setTotalBlocksMined( 1 );
		mines = getMines( MineSortOrder.allSortOrder, mines );
		System.out.println( toString(mines, "After sortOrder including suppressed:") );
		
		assertEquals( "b", mines.get( 0 ).getName() );
		assertEquals( "C", mines.get( 1 ).getName() );
		assertEquals( "A", mines.get( 2 ).getName() );
		assertEquals( "D", mines.get( 3 ).getName() );

	}

}

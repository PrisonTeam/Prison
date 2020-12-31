package tech.mcprison.prison.ranks.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

public class PrisonSortableLaddersTest
		extends PrisonSortableLadders
{

	@Test
	public void testGetSortedSet()
	{
		RankLadder ladderDefault = new RankLadder();
		ladderDefault.setName( "default" );
		RankLadder ladderPrestige = new RankLadder();
		ladderPrestige.setName( "prestige" );
		
		RankLadder ladderMods = new RankLadder();
		ladderMods.setName( "mods" );
		RankLadder ladderAnimals = new RankLadder();
		ladderAnimals.setName( "Animals" );
		RankLadder ladderDonors = new RankLadder();
		ladderDonors.setName( "Donors" );
		RankLadder ladderZapps = new RankLadder();
		ladderZapps.setName( "ZaPpS" );
		
		
		List<RankLadder> unsortedList = new ArrayList<>();
		unsortedList.add( ladderZapps );
		unsortedList.add( ladderDonors );
		unsortedList.add( ladderPrestige );
		unsortedList.add( ladderMods );
		unsortedList.add( ladderAnimals );
		unsortedList.add( ladderDefault );
		

		assertEquals( "ZaPpS", unsortedList.get( 0 ).getName() );
		assertEquals( "Donors", unsortedList.get( 1 ).getName() );
		assertEquals( "prestige", unsortedList.get( 2 ).getName() );
		assertEquals( "mods", unsortedList.get( 3 ).getName() );
		assertEquals( "Animals", unsortedList.get( 4 ).getName() );
		assertEquals( "default", unsortedList.get( 5 ).getName() );
		
		TreeSet<RankLadder> ladders = new TreeSet<>( new PrisonSortComparableLadders() );
		
		ladders.addAll( unsortedList );
		
		List<RankLadder> sortedList = new ArrayList<>(ladders);
		
//		for ( RankLadder l : sortedList )
//		{
//			System.out.println(l.toString());
//		}
		
		assertEquals( "default", sortedList.get( 0 ).getName() );
		assertEquals( "Animals", sortedList.get( 1 ).getName() );
		assertEquals( "Donors", sortedList.get( 2 ).getName() );
		assertEquals( "mods", sortedList.get( 3 ).getName() );
		assertEquals( "ZaPpS", sortedList.get( 4 ).getName() );
		assertEquals( "prestige", sortedList.get( 5 ).getName() );
		
		
	}

}

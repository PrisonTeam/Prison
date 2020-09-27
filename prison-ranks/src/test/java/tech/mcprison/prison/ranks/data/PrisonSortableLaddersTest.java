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
		ladderDefault.name = "default";
		RankLadder ladderPrestige = new RankLadder();
		ladderPrestige.name = "prestige";
		
		RankLadder ladderMods = new RankLadder();
		ladderMods.name = "mods";
		RankLadder ladderAnimals = new RankLadder();
		ladderAnimals.name = "Animals";
		RankLadder ladderDonors = new RankLadder();
		ladderDonors.name = "Donors";
		RankLadder ladderZapps = new RankLadder();
		ladderZapps.name = "ZaPpS";
		
		
		List<RankLadder> unsortedList = new ArrayList<>();
		unsortedList.add( ladderZapps );
		unsortedList.add( ladderDonors );
		unsortedList.add( ladderPrestige );
		unsortedList.add( ladderMods );
		unsortedList.add( ladderAnimals );
		unsortedList.add( ladderDefault );
		

		assertEquals( "ZaPpS", unsortedList.get( 0 ).name );
		assertEquals( "Donors", unsortedList.get( 1 ).name );
		assertEquals( "prestige", unsortedList.get( 2 ).name );
		assertEquals( "mods", unsortedList.get( 3 ).name );
		assertEquals( "Animals", unsortedList.get( 4 ).name );
		assertEquals( "default", unsortedList.get( 5 ).name );
		
		TreeSet<RankLadder> ladders = new TreeSet<>( new PrisonSortComparableLadders() );
		
		ladders.addAll( unsortedList );
		
		List<RankLadder> sortedList = new ArrayList<>(ladders);
		
//		for ( RankLadder l : sortedList )
//		{
//			System.out.println(l.toString());
//		}
		
		assertEquals( "default", sortedList.get( 0 ).name );
		assertEquals( "Animals", sortedList.get( 1 ).name );
		assertEquals( "Donors", sortedList.get( 2 ).name );
		assertEquals( "mods", sortedList.get( 3 ).name );
		assertEquals( "ZaPpS", sortedList.get( 4 ).name );
		assertEquals( "prestige", sortedList.get( 5 ).name );
		
		
	}

}

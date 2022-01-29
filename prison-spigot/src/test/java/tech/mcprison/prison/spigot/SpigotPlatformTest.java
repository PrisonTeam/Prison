package tech.mcprison.prison.spigot;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.spigot.sellall.SellAllBlockData;

public class SpigotPlatformTest
		//extends SpigotPlatform
{
	private SpigotPlatform sp = new SpigotPlatform();

	//@Test
	public void testMineBlockList()
	{
		List<String> blockList = new ArrayList<>();

    	for ( SellAllBlockData xMatCost : sp.buildBlockListXMaterial() ) {
    		
    		// Add only the primary blocks to this blockList which will be used to generate the
    		// mine's block contents:
			if ( xMatCost.isPrimary() ) {
				blockList.add( xMatCost.getBlock().name() );
			}
		}
		
//		List<SellAllBlockData> blockList = sp.buildBlockListXMaterial();
		
		
		
		List<Double> percents = new ArrayList<>();
		percents.add(5d);
		percents.add(10d);
		percents.add(20d);
		percents.add(20d);
		percents.add(20d);
		percents.add(25d);

		
		
		int i = -1;
		
		assertEquals( 0, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 1, mineBlockList( blockList, i++, percents.size() ).size() );
		
		// i represents startPos which starts at 1, but testing with -1.
		assertEquals( 2, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: A
		assertEquals( 3, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 4, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: E
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: J
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: O
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: S
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: T
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 4, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: W
		assertEquals( 3, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 2, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: Y
		assertEquals( 1, mineBlockList( blockList, i++, percents.size() ).size() ); // Mine name: Z
		assertEquals( 0, mineBlockList( blockList, i++, percents.size() ).size() );
		assertEquals( 0, mineBlockList( blockList, i++, percents.size() ).size() );
		
		
	}
	
	protected List<String> mineBlockList( List<String> blockList, int startPos, int length ) {
		
		List<String> results =  sp.mineBlockList( blockList, startPos, length );
		
		System.out.println( results );
		
		return results;
	}

}

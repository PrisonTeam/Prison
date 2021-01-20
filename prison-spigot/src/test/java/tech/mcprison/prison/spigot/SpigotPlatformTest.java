package tech.mcprison.prison.spigot;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class SpigotPlatformTest
		//extends SpigotPlatform
{
	private SpigotPlatform sp = new SpigotPlatform();

	//@Test
	public void testMineBlockList()
	{

		List<String> blockList = sp.buildBlockListBlockType();
		
		
		int i = -1;
		
		assertEquals( 0, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 1, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 2, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: A
		assertEquals( 3, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 4, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: E
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: J
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: O
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: S
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: T
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 5, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 4, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: W
		assertEquals( 3, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 2, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: Y
		assertEquals( 1, mineBlockList( blockList, i++, 5 ).size() ); // Mine name: Z
		assertEquals( 0, mineBlockList( blockList, i++, 5 ).size() );
		assertEquals( 0, mineBlockList( blockList, i++, 5 ).size() );
		
		
	}
	
	protected List<String> mineBlockList( List<String> blockList, int startPos, int length ) {
		List<String> results =  sp.mineBlockList( blockList, startPos, length);
		
		System.out.println( results );
		
		return results;
	}

}

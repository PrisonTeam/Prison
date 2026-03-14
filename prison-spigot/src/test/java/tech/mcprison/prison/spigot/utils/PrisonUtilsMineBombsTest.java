package tech.mcprison.prison.spigot.utils;

import static org.junit.Assert.assertEquals;

public class PrisonUtilsMineBombsTest extends PrisonUtilsMineBombs {

//	@Test
	public void testAdjustYA() {
		
		// yAdjust too high. Reduce to fit in mine.
		int yMin = 50;
		int yMax = 100;
		
		int y = 110;
		int yAdjust = -5;
		
		int deltaY = adjustY( y, yMin, yMax, yAdjust );
		
		assertEquals( -10, deltaY );
		
	}
	
//	@Test
	public void testAdjustYB() {
		
		// yAdjust is too much, reduce it so it stays in the mine.
		int yMin = 50;
		int yMax = 100;
		
		int y = 52;
		int yAdjust = -5;
		
		int deltaY = adjustY( y, yMin, yMax, yAdjust );
		
		assertEquals( -2, deltaY );
		
	}
	
//	@Test
	public void testAdjustYC() {
		
		// Its in the mine, so no adjustment.
		
		int yMin = 50;
		int yMax = 100;
		
		int y = 80;
		int yAdjust = -5;
		
		int deltaY = adjustY( y, yMin, yMax, yAdjust );
		
		assertEquals( -5, deltaY );
		
	}

}

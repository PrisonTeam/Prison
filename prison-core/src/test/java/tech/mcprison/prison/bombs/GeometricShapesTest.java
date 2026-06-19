package tech.mcprison.prison.bombs;

import java.text.DecimalFormat;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.WorldTest;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Vector;

public class GeometricShapesTest extends GeometricShapes {

//	@Test
	public void test() {
		
		DecimalFormat dFmt = new DecimalFormat( "##0" );
		double radius = 1d;
		
		for ( double deg = 0; deg <= 360; deg += 5 ) {
			
			Vector v = getPointsOnCircleXZ( deg, radius );
			
			System.out.println(
					String.format(
							"points on a circle:  degree: %s  r: %s  %s",
							dFmt.format( deg ),
							dFmt.format( radius ),
							v.toString()
							)
					 );
		}
	}
	
//	@Test
	public void testWithLocations() {
		
		DecimalFormat dFmt = new DecimalFormat( "##0" );

		double x = 120;
		double y = 60;
		double z = -50;
		
		World w = new WorldTest();
		
		Location loc = new Location( w, x, y, z );
		
		double radius = 1d;
		
		for ( double deg = 0; deg <= 360; deg += 5 ) {
			
			Vector v = getPointsOnCircleXZ( deg, radius );
			
			Location circleLoc = loc.add( v );
			
			System.out.println(
					String.format(
							"points on a circle:  degree: %3s  r: %2s  %s  %s",
							dFmt.format( deg ),
							dFmt.format( radius ),
							v.toString(), 
							circleLoc.toString()
							)
					 );
		}
		
	}

}

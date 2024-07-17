package tech.mcprison.prison.internal;

import org.junit.Test;

public class EulerAngleTest {

	@Test
	public void test() {
		
		// Have no idea what the starting values should be, which 0,0,0 should work, 
		// but they don't.
		// Also not sure what angle is expected, such as degrees or radians.
		EulerAngle ea = new EulerAngle( 1, 1, 1 );
		
		for ( double angle = -10; angle <= 10; angle += 0.5 ) {
			ea.rotateAroundAxisX( angle );
			
//			System.out.println( ea.toString() + "  angle: " + angle );
		}
		
//		fail("Not yet implemented");
	}

}

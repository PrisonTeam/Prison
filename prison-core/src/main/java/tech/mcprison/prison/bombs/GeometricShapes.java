package tech.mcprison.prison.bombs;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Vector;

/**
 * <p>These functions were basically copied from the following post.  They have 
 * been modified to allow them to work using Prison components.
 * </p>
 * 
 * https://www.minecraftforum.net/forums/archive/tutorials/931256-map-generation-advanced-shapes-code
 * 
 * <p>Overall these functions are not usable by prison directly, but they can be
 * used indirectly as templates on how to generate these kind of shapes.
 * </p>
 * 
 */
public class GeometricShapes
{

	public static double getDistance( int x1, int z1, int x2, int z2 )
	{
		int dx = x1 - x2;
		int dz = z1 - z2;
		return Math.sqrt( (dx * dx + dz * dz) );
	}

	public static void drawCircle( PrisonBlock block, World world, int xi, int yi, int zi, int radius )
	{
		int r = radius;
		for ( int x = xi - r; x <= xi + r; x++ )
		{
			for ( int z = zi - r; z <= zi + r; z++ )
			{
				if ( (int) (getDistance( x, z, xi, zi )) == r )
				{
					world.setBlock( block, x, yi, z );
//					world.setBlock( x, yi, z, Block.stone.blockID );
				}
			}
		}
	}

	public static void drawFilledCircle( PrisonBlock block, World world, int xi, int yi, int zi, int radius )
	{
		int r = radius;
		for ( int x = xi - r; x <= xi + r; x++ )
		{
			for ( int z = zi - r; z <= zi + r; z++ )
			{
				if ( (int) (getDistance( x, z, xi, zi )) <= r )
				{
					world.setBlock( block, x, yi, z );
//					world.setBlock( x, yi, z, Block.stone.blockID );
				}
			}
		}
	}

	public static void drawDisk( PrisonBlock block, World world, int xi, int yi, int zi, int innerRadius, int outerRadius )
	{
		int r = outerRadius;
		for ( int x = xi - r; x <= xi + r; x++ )
		{
			for ( int z = zi - r; z <= zi + r; z++ )
			{
				int dist = (int) (getDistance( x, z, xi, zi ));
				if ( dist < outerRadius && dist >= innerRadius )
				{
					world.setBlock( block, x, yi, z );
//					world.setBlock( x, yi, z, Block.stone.blockID );
				}
			}
		}
	}

	public static void drawCylinder( PrisonBlock block, World world, int xi, int yi, int zi )
	{
		int radius = 9 + (int) (Math.random() * 7);
		if ( radius % 2 == 0 )
			radius++;

		int initialh = yi;
		int height = 5 + (int) (Math.random() * 16);
		for ( ; yi < initialh + height; yi++ )
			drawCircle( block, world, xi, yi, zi, radius );
	}

	public static void drawCone( PrisonBlock block, World world, int xi, int yi, int zi )
	{
		int height = 18 + (int) (Math.random() * 15);
		int radius = 7 + (int) (Math.random() * 10);
		double slope = -height / radius;

		for ( int h = yi; h < yi + height; h++ )
		{
			int r = (int) ((h - height) / slope);
			drawFilledCircle( block, world, xi, h, zi, r );
		}
	}

	public static double getDistance( int x1, int y1, int z1, int x2, int y2, int z2 )
	{
		int dx = x1 - x2;
		int dy = y1 - y2;
		int dz = z1 - z2;
		return Math.sqrt( (dx * dx + dy * dy + dz * dz) );
	}


	/**
	 * NOTE: This function is not complete, or at least only draws a hollow sphere.
	 * 
	 * @param block
	 * @param world
	 * @param xi
	 * @param yi
	 * @param zi
	 */
	public static void drawSphere( PrisonBlock block, World world, int xi, int yi, int zi )
	{

		int r = 24 + (int) (Math.random() * 12);
		if ( r % 2 != 0 )
			r++;

		for ( int x = xi - r; x <= xi + r; x++ )
		{
			for ( int z = zi - r; z <= zi + r; z++ )
			{
				for ( int y = yi - r; y <= yi + r; y++ )
				{
					if ( (int) (getDistance( x, y, z, xi, yi, zi )) == r ) {
						world.setBlock( block, x, y, z );
					}
																			// >=
																			// for
																			// solid
//						world.setBlock( x, y, z, Block.stone.blockID );
					// else if( (int)(getDistance(x,y,z,xi,yi,zi) )== r-1 && yi
					// <= y)
					// world.setBlock(x,y,z,Block.glowStone.blockID);
				}
			}
		}
	}

	public static void wireFrameCube( PrisonBlock block, World world, int xi, int yi, int zi, int wid, int hei, int dep )
	{
		int width = wid;// x
		int height = hei;// y
		int depth = dep; // z
		//int id = Block.stone.blockID;
		for ( int i = xi; i < xi + width; i++ )
		{
			
			world.setBlock( block, i, yi, zi );
			world.setBlock( block, i, yi + height - 1, zi );
			world.setBlock( block, i, yi, zi + depth - 1 );
			world.setBlock( block, i, yi + height - 1, zi + depth - 1 );
			
//			world.setBlock( i, yi, zi, id );
//			world.setBlock( i, yi + height - 1, zi, id );
//			world.setBlock( i, yi, zi + depth - 1, id );
//			world.setBlock( i, yi + height - 1, zi + depth - 1, id );
		}
		for ( int i = yi; i < yi + height; i++ )
		{
			world.setBlock( block, xi, i, zi );
			world.setBlock( block, xi + width - 1, i, zi );
			world.setBlock( block, xi, i, zi + depth - 1 );
			world.setBlock( block, xi + width - 1, i, zi + depth - 1 );
			
//			world.setBlock( xi, i, zi, id );
//			world.setBlock( xi + width - 1, i, zi, id );
//			world.setBlock( xi, i, zi + depth - 1, id );
//			world.setBlock( xi + width - 1, i, zi + depth - 1, id );
		}
		for ( int i = zi; i < zi + depth; i++ )
		{
			world.setBlock( block, xi, yi, i );
			world.setBlock( block, xi, yi + height - 1, i );
			world.setBlock( block, xi + width - 1, yi, i );
			world.setBlock( block, xi + width - 1, yi + height - 1, i );
			
//			world.setBlock( xi, yi, i, id );
//			world.setBlock( xi, yi + height - 1, i, id );
//			world.setBlock( xi + width - 1, yi, i, id );
//			world.setBlock( xi + width - 1, yi + height - 1, i, id );
		}
	}

	public static void drawParaboloid( PrisonBlock block, World world, int xi, int yi, int zi )
	{
		int height = 29;
		int initialh = yi;
		for ( ; yi < initialh + height; yi++ )
		{
			int radius = (int) (3 * Math.sqrt( initialh + height - yi ));
			drawFilledCircle( block, world, xi, yi, zi, radius );
		}
	}

	public static void drawHyperbloid( PrisonBlock block, World world, int xi, int yi, int zi )
	{
		int height = 15 + (int) (Math.random() * 10);
		int minR = 5 + (int) Math.random() * 9;
		int maxR = 9 + (int) (Math.random() * 11);
		double csq = (height * height) / (((double) (maxR * maxR) / (minR * minR)) - 1);

		int initialh = yi + height;
		for ( ; yi <= initialh + height; yi++ )
		{
			int h = yi - initialh;
			int radius = (int) Math.sqrt( ((1 + ((h * h) / csq)) * (minR * minR)) );

			drawCircle( block, world, xi, yi + 1, zi, radius );
		}
	}

	public static void drawTorus( PrisonBlock block, World world, int xi, int yi, int zi )
	{
		int majorRadius = 21;
		int minorRadius = 7;

		for ( int h = -minorRadius; h <= minorRadius; h++ )
		{
			double theta = Math.asin( h / (double) minorRadius );
			int localRadius = (int) Math.abs( (double) minorRadius * Math.cos( theta ) );
			drawDisk( block, world, xi, yi + h, zi, majorRadius - localRadius, majorRadius + localRadius );
		}
	}

	
//	public class Point {
//		private double x;
//		private double y;
//		private double z;
//		public Point( double x, double y, double z ) {
//			this.x = x;
//			this.y = y;
//			this.z = z;
//		}
//		public double getX() {
//			return x;
//		}
//		public void setX(double x) {
//			this.x = x;
//		}
//		public double getY() {
//			return y;
//		}
//		public void setY(double y) {
//			this.y = y;
//		}
//		public double getZ() {
//			return z;
//		}
//		public void setZ(double z) {
//			this.z = z;
//		}
//	}
	
	/**
	 * This will calculate a new Vector based upon the degrees (angle), and the 
	 * radius (distance from the center);
	 * 
	 * Incrementing the degrees, with keeping the same value for radius, should 
	 * draw a circle on the plane X-Z.
	 * 
	 * @param degrees
	 * @param radius
	 * @return
	 */
	public static Vector getPointsOnCircleXZ( double degrees, double radius ) {
		Vector results = null;
		
		final double angle = Math.toRadians( degrees );

		double x = (Math.cos(angle) * radius);
		double y = 0d;
		double z = (Math.sin(angle) * radius);
		
	    results = new Vector( x, y, z );
	    
	    return results;
	    
		
//		final int NUM_POINTS = 1000;
//		final double RADIUS = 100d;
//
//		final Point[] points = new Point[NUM_POINTS];
//
//		for (int i = 0; i < NUM_POINTS; ++i)
//		{
//		    final double angle = Math.toRadians(((double) i / NUM_POINTS) * 360d);
//
//		    points[i] = new Point(
//		        Math.cos(angle) * RADIUS, 
//		        Math.sin(angle) * RADIUS
//		    );
//		}
		
	}
	
//	public void someMethod()
//	{
//		{
//			{
//
//				int rad = 20;
//				double theta = 0;
//				int color = 0;
//				for ( int s = 0; s < 21; s++ )
//				{
//					wireFrameCube( world, (int) (xi + (rad * Math.cos( theta ))), yi, (int) (zi + (rad * Math.sin( theta ))), 10,
//							10, 10, color );
//					theta += 3.14159 / 4;
//					yi += 8;
//					color++;
//				}
//			}
//		} // mop null
//
//		return itemstack;
//	}
//
//	public void wireFrameCube( World world, int xi, int yi, int zi, int wid, int hei, int dep, int col )
//	{
//		int width = wid;// x
//		int height = hei;// y
//		int depth = dep; // z
//		int id = Block.cloth.blockID;
//		for ( int i = xi; i < xi + width; i++ )
//		{
//			world.setBlockAndMetadata( i, yi, zi, id, col );
//			world.setBlockAndMetadata( i, yi + height - 1, zi, id, col );
//			world.setBlockAndMetadata( i, yi, zi + depth - 1, id, col );
//			world.setBlockAndMetadata( i, yi + height - 1, zi + depth - 1, id, col );
//		}
//		for ( int i = yi; i < yi + height; i++ )
//		{
//			world.setBlockAndMetadata( xi, i, zi, id, col );
//			world.setBlockAndMetadata( xi + width - 1, i, zi, id, col );
//			world.setBlockAndMetadata( xi, i, zi + depth - 1, id, col );
//			world.setBlockAndMetadata( xi + width - 1, i, zi + depth - 1, id, col );
//		}
//		for ( int i = zi; i < zi + depth; i++ )
//		{
//			world.setBlockAndMetadata( xi, yi, i, id, col );
//			world.setBlockAndMetadata( xi, yi + height - 1, i, id, col );
//			world.setBlockAndMetadata( xi + width - 1, yi, i, id, col );
//			world.setBlockAndMetadata( xi + width - 1, yi + height - 1, i, id, col );
//
//		}
//
//	}

}

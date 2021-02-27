package tech.mcprison.prison.spatial;

public class CoordinateKey 
	implements Comparable<CoordinateKey> {

	private int x;
	private int y;
	private int z;
	
	public CoordinateKey( int x, int y, int z ) {
		super();
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public int compareTo( CoordinateKey skey )
	{
		int results = 0;
		
		if ( skey == null ) {
			results = -1;
		}
		else {
			results = Integer.compare( x, skey.getX() );
			
			if ( results == 0 ) {
				results = Integer.compare( z, skey.getZ() );
				
				if ( results == 0 ) {
					results = Integer.compare( y, skey.getY() );
				}
			}
		}
		
		return results;
	}

	public int getX() {
		return x;
	}
	public void setX( int x ) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY( int y ) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}
	public void setZ( int z ) {
		this.z = z;
	}

}

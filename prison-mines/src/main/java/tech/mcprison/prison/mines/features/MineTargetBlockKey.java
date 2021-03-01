package tech.mcprison.prison.mines.features;

import tech.mcprison.prison.util.Location;

public class MineTargetBlockKey
	implements Comparable<MineTargetBlockKey>
{
	private final int x, y, z;
	
	public MineTargetBlockKey( int x, int y, int z ) {
		super();
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public MineTargetBlockKey( Location location ) {
		this( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "(" ).append( x ).append( ", " ).append( y ).append( ", " )
				.append( z ).append( ")" );
		
		return sb.toString();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public int compareTo( MineTargetBlockKey key ) {
		int result = 0;
		if ( key == null ) {
			result = -1;
		}
		else {
			
			result = Integer.compare( getX(), key.getX() );
			
			if ( result == 0 ) {
				result = Integer.compare( getZ(), key.getZ() );
				
				if ( result == 0 ) {
					result = Integer.compare( getY(), key.getY() );
				}
			}
		}
		
		return result;
	}
}

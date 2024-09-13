package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.util.Location;

public class MineTargetBlockKey
	implements Comparable<MineTargetBlockKey>
{
	private final World world;
	private final int x, y, z;
	
	public MineTargetBlockKey( World world, int x, int y, int z ) {
		super();
		
		this.world = world;
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public MineTargetBlockKey( Location location ) {
		this( location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	}
	
	
	public Location getLocation() {
		Location location = new Location( world, x, y, z );
		return location;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "(" ).append( x ).append( ", " ).append( y ).append( ", " )
				.append( z ).append( ")" );
		
		return sb.toString();
	}

	public World getWorld() {
		return world;
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
			
			result = getWorld().getName().compareTo( key.getWorld().getName() );
			
			if ( result == 0 ) {
				
				result = Integer.compare( getX(), key.getX() );
				
				if ( result == 0 ) {
					result = Integer.compare( getZ(), key.getZ() );
					
					if ( result == 0 ) {
						result = Integer.compare( getY(), key.getY() );
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean equals( Object obj ) {
		boolean results = false;
		
		if ( obj != null && obj instanceof MineTargetBlockKey ) {
			MineTargetBlockKey mtbKey = (MineTargetBlockKey) obj;
			
			results = compareTo( mtbKey ) == 0;
		}
		return results;
	}

	@Override
	public int hashCode()
	{
		int hash = getWorld().getName().hashCode();
		
		hash += x * 13 + y * 37 + z * 17;
				
//		return super.hashCode();
		
		return hash;
	}
	
	
	
	
}

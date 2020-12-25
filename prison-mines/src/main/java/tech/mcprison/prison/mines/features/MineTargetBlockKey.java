package tech.mcprison.prison.mines.features;

public class MineTargetBlockKey
	implements Comparable<Object>
{
	private final int x, y, z;
	
	public MineTargetBlockKey( int x, int y, int z ) {
		super();
		
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}

	@Override
	public int compareTo( Object arg0 ) {
		if ( !(arg0 instanceof MineTargetBlockKey) ) {
			return -1;
		}
		MineTargetBlockKey key = (MineTargetBlockKey) arg0;
		
		int result = key.getX() - getX();
		
		if ( result == 0 ) {
			result = key.getY() - getY();

			if ( result == 0 ) {
				result = key.getZ() - getZ();
			}
		}
		
		return result;
	}
}

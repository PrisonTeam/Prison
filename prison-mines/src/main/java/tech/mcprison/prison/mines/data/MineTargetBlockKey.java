package tech.mcprison.prison.mines.data;

public class MineTargetBlockKey
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
}

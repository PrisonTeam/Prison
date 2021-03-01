package tech.mcprison.prison.mines.features;

import tech.mcprison.prison.internal.block.PrisonBlockStatusData;

public class MineTargetPrisonBlock
	implements Comparable<MineTargetPrisonBlock>
{
	private MineTargetBlockKey blockKey;
	
	private PrisonBlockStatusData prisonBlock;
	
	protected MineTargetPrisonBlock( int x, int y, int z ) {
		
		this.blockKey = new MineTargetBlockKey( x, y, z );
	}
	
	public MineTargetPrisonBlock( PrisonBlockStatusData prisonBlock, int x, int y, int z ) {
		this( x, y, z );
		
		this.prisonBlock = prisonBlock;
	}

	@Override
	public String toString() {
		return "MineTargetPrisonBlock: key= " + getBlockKey().toString() + 
											" block= " + getPrisonBlock().toString();
	}
	
	public PrisonBlockStatusData getPrisonBlock() {
		return prisonBlock;
	}
	public void setPrisonBlock( PrisonBlockStatusData prisonBlock ) {
		this.prisonBlock = prisonBlock;
	}

	
	public MineTargetBlockKey getBlockKey() {
		return blockKey;
	}


	@Override 
	public int compareTo( MineTargetPrisonBlock block ) {
		return block.getBlockKey().compareTo( block.getBlockKey() );
	}
}

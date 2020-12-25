package tech.mcprison.prison.mines.features;

import tech.mcprison.prison.internal.block.PrisonBlock;

public class MineTargetPrisonBlock 
		extends MineTargetBlock
{
	private PrisonBlock prisonBlock;
	
	public MineTargetPrisonBlock( PrisonBlock prisonBlock, int x, int y, int z ) {
		super( x, y, z );
		
		this.prisonBlock = prisonBlock;
	}

	public PrisonBlock getPrisonBlock()
	{
		return prisonBlock;
	}
	public void setPrisonBlock( PrisonBlock prisonBlock )
	{
		this.prisonBlock = prisonBlock;
	}

}

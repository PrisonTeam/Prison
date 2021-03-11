package tech.mcprison.prison.mines.features;

import tech.mcprison.prison.internal.block.PrisonBlockStatusData;

public class MineTargetPrisonBlock
	implements Comparable<MineTargetPrisonBlock>
{
	private MineTargetBlockKey blockKey;
	
	private PrisonBlockStatusData prisonBlock;
	
	private boolean airBroke;
	
	
	protected MineTargetPrisonBlock( int x, int y, int z ) {
		
		this.blockKey = new MineTargetBlockKey( x, y, z );
	}
	
	public MineTargetPrisonBlock( PrisonBlockStatusData prisonBlock, int x, int y, int z ) {
		this( x, y, z );
		
		this.prisonBlock = prisonBlock;
		
		if ( prisonBlock == null || prisonBlock.isAir() ) {
			this.airBroke = true;
		}
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

	/**
	 * <p>This is a quick way to check to see if the block was originally set to air, or if
	 * the block was previously broke and "counted".  This field, airBroke, needs to be
	 * set to 'true' when the block is counted as broken the first time so it won't be
	 * double counted in the future.  Explosion events tends to cause blocks to be
	 * counted multiple times since it does not check to see if they are air prior to 
	 * selecting them.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isAirBroke() {
		return airBroke;
	}
	public void setAirBroke( boolean airBroke ) {
		this.airBroke = airBroke;
	}

	
	@Override 
	public int compareTo( MineTargetPrisonBlock block ) {
		return block.getBlockKey().compareTo( block.getBlockKey() );
	}
}

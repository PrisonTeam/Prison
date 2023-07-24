package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.util.Location;

public class MineTargetPrisonBlock
	implements Comparable<MineTargetPrisonBlock>
{
	private MineTargetBlockKey blockKey;
	
	private PrisonBlockStatusData prisonBlock;
	
	private boolean airBroke;
	private boolean isEdge;
	private boolean isCorner;
	private boolean exploded;
	
	private boolean mined = false;
	private Block minedBlock;
	
//	private boolean blockEvent = false;
	private boolean counted = false;
	
	private boolean ignoreAllBlockEvents = false;
	
	
	public MineTargetPrisonBlock( 
				PrisonBlockStatusData prisonBlock, 
				World world, 
						int x, int y, int z, 
						boolean isEdge,
						boolean isCorner ) {
		this.blockKey = new MineTargetBlockKey( world, x, y, z );
		
		this.prisonBlock = prisonBlock;
		
		if ( prisonBlock == null || prisonBlock.isAir() ) {
			this.airBroke = true;
		}
		
		this.isEdge = isEdge;
		this.isCorner = isCorner;
	}

	@Override
	public String toString() {
		return "MineTargetPrisonBlock: key= " + getBlockKey().toString() + 
											" block= " + getPrisonBlock().toString();
	}
	
	public  PrisonBlock getPrisonBlock( MineResetType resetType ) {
		
		final PrisonBlock pBlock;
		
		if ( ( resetType == MineResetType.tracer ||
				   resetType == MineResetType.outline )  && isEdge() )
		{
			// Generates the tracer along all edges of the mine
			// NOTE: outline and tracer are the same. Outline should be 
			//       converted to tracer, but if it's not, then handle it here
			pBlock = PrisonBlock.PINK_STAINED_GLASS;
		}
		else if ( resetType == MineResetType.corners && isCorner() )
		{
			// Generates the trace along all the corners of the mine
			pBlock = PrisonBlock.PINK_STAINED_GLASS;
		}
		else if ( resetType == MineResetType.clear || 
				resetType == MineResetType.tracer || 
				resetType == MineResetType.outline || 
				resetType == MineResetType.corners )
		{
			// clears the mine with all AIR, or for tracers the non edges, 
			// or clears all of the mine except the corners.
			pBlock = PrisonBlock.AIR;
		}
		else if ( getPrisonBlock() != null && 
				getPrisonBlock() instanceof PrisonBlock )
		{
			
			// MineResetType.normal and MineResetType.paged
			pBlock = (PrisonBlock) getPrisonBlock();
		}
		else
		{
			pBlock = null;
		}
		
		return pBlock;
		
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

	public String getBlockCoordinates() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getPrisonBlock().getBlockName() );
//		sb.append( getPrisonBlock().getBlockNameFormal() );
		
		if ( getLocation() != null ) {
			sb.append( "::" );
			
			sb.append( getLocation().toWorldCoordinates() );
		}
		
		return sb.toString();
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
	
	public boolean isEdge() {
		return isEdge;
	}
	public void setEdge( boolean isEdge ) {
		this.isEdge = isEdge;
	}

	public boolean isCorner() {
		return isCorner;
	}
	public void setCorner(boolean isCorner) {
		this.isCorner = isCorner;
	}

	public boolean isExploded() {
		return exploded;
	}
	public void setExploded( boolean exploded ) {
		this.exploded = exploded;
	}

	public boolean isMined() {
		return mined;
	}
	public void setMined( boolean mined ) {
		this.mined = mined;
	}

//	public boolean isBlockEvent() {
//		return blockEvent;
//	}
//	public void setBlockEvent( boolean blockEvent ) {
//		this.blockEvent = blockEvent;
//	}

	public boolean isCounted() {
		return counted;
	}
	public void setCounted( boolean counted ) {
		this.counted = counted;
	}

	public Block getMinedBlock() {
		return minedBlock;
	}
	public void setMinedBlock( Block minedBlock ) {
		this.minedBlock = minedBlock;
	}

	public boolean isIgnoreAllBlockEvents() {
		return ignoreAllBlockEvents;
	}
	public void setIgnoreAllBlockEvents( boolean ignoreAllBlockEvents ) {
		this.ignoreAllBlockEvents = ignoreAllBlockEvents;
	}

	@Override 
	public int compareTo( MineTargetPrisonBlock block ) {
		return block.getBlockKey().compareTo( block.getBlockKey() );
	}

	public Location getLocation()
	{
		return getBlockKey().getLocation();
	}
}

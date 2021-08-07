package tech.mcprison.prison.internal.block;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;

public abstract class PrisonBlockStatusData {

	// blockName is more of an internal reference:
	private String blockName;
	
	private double chance;
	
	private int constraintMin;
	private int constraintMax;

	private int constraintExcludeTopLayers;
	private int constraintExcludeBottomLayers;
	
	private int blockPlacedCount;
	
	private long blockCountTotal;
	private long blockCountSession;
	private long blockCountUnsaved;
	
	// The rangeBlockCounts identifies the valid range in which this block may
	// appear within the target blocks list:
	private int rangeBlockCountLow;
	private int rangeBlockCountHigh;
	
	private int rangeBlockCountLowLimit;
	private int rangeBlockCountHighLimit;
	
	
	public PrisonBlockStatusData( String blockName, double chance, long blockCountTotal ) {
		super();
		
		this.blockName = blockName;
		
		this.chance = chance;
		
		this.constraintMin = 0;
		this.constraintMax = 0;

		this.constraintExcludeTopLayers = 0;
		this.constraintExcludeBottomLayers = 0;
		
		this.blockPlacedCount = 0;
		
		this.blockCountTotal = blockCountTotal;
		this.blockCountSession = 0;
		this.blockCountUnsaved = 0;
		
		this.rangeBlockCountLow = -1;
		this.rangeBlockCountHigh = -1;
		
		this.rangeBlockCountLowLimit = -1;
		this.rangeBlockCountHighLimit = -1;
	}
	
	public void resetAfterSave() {
		blockCountUnsaved = 0;
	}

	public void incrementResetBlockCount() {
		blockPlacedCount++;
	}
	
	public void decrementResetBlockCount() {
		blockPlacedCount--;
	}
	
	public void incrementMiningBlockCount() {
		blockCountTotal++;
		blockCountSession++;
		blockCountUnsaved++;
	}
	
	public String toSaveFileFormat() {
		return getBlockName() + "-" + getChance() + "-" + getBlockCountTotal() + "-" + 
					getConstraintMin() + "-" + getConstraintMax() + "-" + 
					getConstraintExcludeTopLayers() + "-" + getConstraintExcludeBottomLayers();
	}
	
	
	public static PrisonBlock parseFromSaveFileFormat( String blockString ) {
		
		PrisonBlock results = null;
		
		String[] split = blockString.split("-");
		if ( split != null && split.length > 0 ) {
			
			String blockTypeName = split[0];
			// The new way to get the PrisonBlocks:  
			//   The blocks return are cloned so they have their own instance:
			results = Prison.get().getPlatform().getPrisonBlock( blockTypeName );
			
			if ( results != null ) {
				
				results.parseFromSaveFileFormatStats( blockString );
				
			}
//			else {
//				if ( blockTypeName.equalsIgnoreCase( "gold_ore" ) ) {
//					Output.get().logInfo( "### parseFromSaveFile: no results!? [" + 
//							String.join( ", ", split ) + "]"
//							);
//				}
//			}
		}
		
		return results;
	}
	
	public void parseFromSaveFileFormatStats( String blockString ) {
		
		if ( blockString != null ) {
			
			String[] split = blockString.split("-");
			
			if ( split != null && split.length > 0 ) {
//				String blockTypeName = split[0];
				// The new way to get the PrisonBlocks:  
				
				double chance = split.length > 1 ? Double.parseDouble(split[1]) : 0;
				long blockCount = split.length > 2 ? Long.parseLong(split[2]) : 0;
				int constraintMin = split.length > 3 ? Integer.parseInt(split[3]) : 0;
				int constraintMax = split.length > 4 ? Integer.parseInt(split[4]) : 0;
				int constraintExcludeTopLayers = split.length > 5 ? Integer.parseInt(split[5]) : 0;
				int constraintExcludeBottomLayers = split.length > 6 ? Integer.parseInt(split[6]) : 0;
				
				setChance( chance );
				setBlockCountTotal( blockCount );
				setConstraintMin( constraintMin );
				setConstraintMax( constraintMax );
				setConstraintExcludeTopLayers( constraintExcludeTopLayers );
				setConstraintExcludeBottomLayers( constraintExcludeBottomLayers );
				
//				if ( blockTypeName.equalsIgnoreCase( "gold_ore" ) ) {
//					Output.get().logInfo( "### parseFromSaveFile: [" + 
//							String.join( ", ", split ) + "]  [" +
//							block.toSaveFileFormat() + "]"
//							);
//				}
			}

		}
	}
	
	/**
	 * This function transfers all stats from an old block to this existing block. 
	 * The data is replaced, not "added" so use cautiously.
	 * 
	 * @param oldBlock
	 */
	public void transferStats( PrisonBlockStatusData oldBlock ) {
		if ( oldBlock != null ) {
			
			setChance( oldBlock.getChance() );
			setBlockCountTotal( oldBlock.getBlockCountTotal() );
			setConstraintMin( oldBlock.getConstraintMin() );
			setConstraintMax( oldBlock.getConstraintMax() );
			setConstraintExcludeTopLayers( oldBlock.getConstraintExcludeTopLayers() );
			setConstraintExcludeBottomLayers( oldBlock.getConstraintExcludeBottomLayers() );
		}
	}
	
	public String toPlaceholderString() {
		StringBuilder sb = new StringBuilder();
		
    	DecimalFormat dFmt = new DecimalFormat("#,##0");
    	DecimalFormat fFmt = new DecimalFormat("#,##0.00");

    	String percent = fFmt.format(getChance());
    	
//    	String spawned = dFmt.format( getResetBlockCount() );
    	String remaining = dFmt.format( getBlockPlacedCount() - getBlockCountUnsaved() );
    	String total = PlaceholdersUtil.formattedKmbtSISize( 1.0d * getBlockCountTotal(), dFmt, "" );
    	
		sb.append( getBlockName() ).append( " (" )
			.append( percent ).append( " pct) " )
//			.append( spawned )
			.append( "  r: " ).append( remaining )
			.append( "  T: " ) .append( total )
			;
		
		return sb.toString();
	}
	
	/**
	 * <p>This function records the lowest and highest value for the targetBlockPosition
	 * in which this block type is valid to be spawned.  This represents the 
	 * targetBlock array's valid range within that List that may contain this block type, 
	 * which will be used when regenerating blocks to satisfy the minimum constraint.
	 * </p>
	 * 
	 * <p>This function should only be called if other constraints that could limit the 
	 * placement of this block within the targetBlock List have validated that it is 
	 * satisfies their constraints.  Example would be with the ExcludeTopLayers and
	 * ExcludeBottomLayers.
	 * </p>
	 * 
	 * <p>Values of -1 for rangeBlockCountLow and rangeBlockCountHigh means a range
	 * has not been set.  If one has been set, then both will be set.  Once the 
	 * the rangeBlockCountLow has been set, it never needs to be checked again since it
	 * will always be the lowest value when generating blocks.
	 * </p>
	 * 
	 * @param targetBlockPosition
	 */
	private void setTargetBlockRange( int targetBlockPosition ) {
		
		if ( getRangeBlockCountLow() == -1 ) {
			// Only need to set the low once since it will be lowest it ever will be:
			setRangeBlockCountLow( targetBlockPosition );
			
			// Set the high
			setRangeBlockCountHigh( targetBlockPosition );
		}

		else if ( getRangeBlockCountHigh() < targetBlockPosition ) {
			// Set the high value since it's higher than what it was before:
			setRangeBlockCountHigh( targetBlockPosition );
		}
		
	}
	
	/**
	 * <p>This function will identify if the currentLevel for Y is valid to be 
	 * processed for this block.  
	 * ExcludeTopLayers,
	 * or ExcludeBottomLayers should exclude the generation of blocks.  If not, then it
	 * will call setTargetBlockRange and return a value of true from this function.
	 * </p>
	 * 
	 * <p>Note that currentLevel is ONE based and not zero based.  The first layer of 
	 * the mine will have a level of ONE.
	 * </p>
	 * 
	 * <p>A value of zero for ExcludeTopLayers or for ExcludeBottomLayers implies
	 * it is disabled.
	 * </p>
	 * 
	 * @param currentLevel
	 * @param targetBlockPosition
	 * @return
	 */
	public boolean isBlockConstraintsEnbled( int currentLevel, int targetBlockPosition ) {
		boolean enabled = false;
		
		if ( 
			// Is block able to be spawned in these top layer?
			(getConstraintExcludeTopLayers() == 0 || 
				currentLevel > getConstraintExcludeTopLayers()) &&
			
			// Is block able to be spwned in these bottom layers?
			(getConstraintExcludeBottomLayers() == 0 ||
			    currentLevel < getConstraintExcludeBottomLayers() ) )  {

			// Need to set the block ranges only if the current layer is within a valid range:
			setTargetBlockRange( targetBlockPosition );
			
			// If Max is enabled (non zero) has the block reached that limit yet?
			if ( ( getConstraintMax() == 0 || 
			getConstraintMax() > 0 && getBlockPlacedCount() < getConstraintMax() )
					) {
				
				enabled = true;
			}
		}
		
		return enabled;
	}
	
	/**
	 * <p>The Block that is passed to this function will be added to this block. This function
	 * acts as a way to gather totals from other block.
	 * </p>
	 * 
	 * @param block2
	 */
	public void addStats( PrisonBlockStatusData block ) {
		
		setBlockPlacedCount( getBlockPlacedCount() + block.getBlockPlacedCount() );
		setBlockCountSession( getBlockCountSession() + block.getBlockCountSession() );
		setBlockCountTotal( getBlockCountTotal() + block.getBlockCountTotal() );
		setBlockCountUnsaved( getBlockCountUnsaved() + block.getBlockCountUnsaved() );
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getBlockName() ).append( "  chance= " ).append( getChance() )
					.append( "%" );
		
		return sb.toString();
	}
	
	public abstract boolean isAir();
	
	
	public String getBlockName(){
		return blockName;
	}
	public void setBlockName( String blockName ) {
		this.blockName = blockName;
	}

	public double getChance() {
		return chance;
	}
	public void setChance( double chance ) {
		this.chance = chance;
	}

	public int getBlockPlacedCount() {
		return blockPlacedCount;
	}
	public void setBlockPlacedCount( int blockPlacedCount ) {
		this.blockPlacedCount = blockPlacedCount;
	}

	public long getBlockCountTotal() {
		return blockCountTotal;
	}
	public void setBlockCountTotal( long blockCountTotal ) {
		this.blockCountTotal = blockCountTotal;
	}

	public long getBlockCountSession() {
		return blockCountSession;
	}
	public void setBlockCountSession( long blockCountSession ) {
		this.blockCountSession = blockCountSession;
	}

	public long getBlockCountUnsaved() {
		return blockCountUnsaved;
	}
	public void setBlockCountUnsaved( long blockCountUnsaved ) {
		this.blockCountUnsaved = blockCountUnsaved;
	}

	public int getConstraintMin() {
		return constraintMin;
	}
	public void setConstraintMin( int constraintMin ) {
		this.constraintMin = constraintMin;
	}

	public int getConstraintMax() {
		return constraintMax;
	}
	public void setConstraintMax( int constraintMax ) {
		this.constraintMax = constraintMax;
	}

	public int getConstraintExcludeTopLayers() {
		return constraintExcludeTopLayers;
	}
	public void setConstraintExcludeTopLayers( int constraintExcludeTopLayers ) {
		this.constraintExcludeTopLayers = constraintExcludeTopLayers;
	}

	public int getConstraintExcludeBottomLayers() {
		return constraintExcludeBottomLayers;
	}
	public void setConstraintExcludeBottomLayers( int constraintExcludeBottomLayers ) {
		this.constraintExcludeBottomLayers = constraintExcludeBottomLayers;
	}

	public int getRangeBlockCountLow() {
		return rangeBlockCountLow;
	}
	public void setRangeBlockCountLow( int rangeBlockCountLow ) {
		this.rangeBlockCountLow = rangeBlockCountLow;
	}

	public int getRangeBlockCountHigh() {
		return rangeBlockCountHigh;
	}
	public void setRangeBlockCountHigh( int rangeBlockCountHigh ) {
		this.rangeBlockCountHigh = rangeBlockCountHigh;
	}

	public int getRangeBlockCountLowLimit() {
		return rangeBlockCountLowLimit;
	}
	public void setRangeBlockCountLowLimit( int rangeBlockCountLowLimit ) {
		this.rangeBlockCountLowLimit = rangeBlockCountLowLimit;
	}

	public int getRangeBlockCountHighLimit() {
		return rangeBlockCountHighLimit;
	}
	public void setRangeBlockCountHighLimit( int rangeBlockCountHighLimit ) {
		this.rangeBlockCountHighLimit = rangeBlockCountHighLimit;
	}

}

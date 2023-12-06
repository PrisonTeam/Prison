package tech.mcprison.prison.internal.block;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;

public abstract class PrisonBlockStatusData {

	// blockName is more of an internal reference:
	private PrisonBlockType blockType;
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
	
	
	private boolean gravity = false;
	
	private transient boolean includeInLayerCalculations;
	
	
	public PrisonBlockStatusData( PrisonBlockType blockType, String blockName, double chance, long blockCountTotal ) {
		super();
		
		this.blockType = blockType;
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
		
		this.gravity = checkGravityAffects( blockName );
		
		this.includeInLayerCalculations = true;
	}

	

	@Override
	public boolean equals( Object obj )
	{
		boolean results = false;
		
		if ( obj instanceof PrisonBlockStatusData ) {
			PrisonBlockStatusData pbsBlock = (PrisonBlockStatusData) obj;
			
			results = getBlockName().equalsIgnoreCase( pbsBlock.getBlockName() );
		}
		
		return results;
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
			
			// The blockName is the first element. Use that to setup the PrisonBlock that
			// will be used if the other stats that are available.
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
		
    	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    	DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");

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
	
	
	/**
	 * <p>If a block is affected by gravity, which means the block can fall, then 
	 * this function will return a value of true.
	 * </p>
	 * 
	 * <p>The items that are most likely to appear in the mine should
	 * be at the top to allow the minimization of what is required to be
	 * checked.
	 * </p>
	 * 
	 * https://minecraft.fandom.com/wiki/Falling_Block
	 * 
	 * @param blockName
	 * @return
	 */
	private boolean checkGravityAffects( String blockName )
	{
		boolean results = false;
		
		if ( blockName != null ) {
			
			switch ( blockName )
			{
				case "sand":
				case "red_sand":
				case "gravel":
					
				case "white_concrete_powder":
				case "orange_concrete_powder":
				case "magenta_concrete_powder":
				case "light_blue_concrete_powder":
				case "yellow_concrete_powder":
				case "lime_concrete_powder":
				case "pink_concrete_powder":
				case "gray_concrete_powder":
				case "light_gray_concrete_powder":
				case "cyan_concrete_powder":
				case "purple_concrete_powder":
				case "blue_concrete_powder":
				case "brown_concrete_powder":
				case "green_concrete_powder":
				case "red_concrete_powder":
				case "black_concrete_powder":
					
				case "anvil":
				case "chipped_anvil":
				case "damaged_anvil":
					
				case "scaffolding":
				case "pointed_dripstone":
				case "dragon_egg":
				{
					results = true;
				}
			}
		}
		return results;
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

	public PrisonBlockType getBlockType() {
		return blockType;
	}
	public void setBlockType( PrisonBlockType blockType ) {
		this.blockType = blockType;
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

	public boolean isGravity() {
		return gravity;
	}
	public void setGravity( boolean gravity ) {
		this.gravity = gravity;
	}


	
	public boolean isIncludeInLayerCalculations() {
		return includeInLayerCalculations;
	}
	public void setIncludeInLayerCalculations(boolean includeInLayerCalculations) {
		this.includeInLayerCalculations = includeInLayerCalculations;
	}


	/**
	 * <p>This is only used when calculating which blocks should be placed
	 * in each layer, one layer at a time. This would be used if a block is
	 * usable for a layer, but it has exceeded it's max constraint, so a 
	 * value of 'false' would then prevent this block from being used, but
	 * will allow the stats to continue to collect the max usable range
	 * in which this block could be placed.
	 * </p>
	 * 
	 * @param targetBlocks
	 * @return
	 */
	public int getRandomBlockPositionInRangeUnmatched(
			List<MineTargetPrisonBlock> targetBlocks) {
		return getRandomBlockPositionInRange( targetBlocks, false );
	}

	public int getRandomBlockPositionInRangeMatched(
			List<MineTargetPrisonBlock> targetBlocks) {
		return getRandomBlockPositionInRange( targetBlocks, true );
	}
	
	/**
	 * <p>This function will select a block position from the
	 * targetBlocks list that is either of the same block type, or
	 * that is not equal to the same block type.  This is to 
	 * find valid blocks to either replace, or to add to the 
	 * list without randomly trying to select a block to try.
	 * </p>
	 * 
	 * @param targetBlocks
	 * @param matched
	 * @return
	 */
	private int getRandomBlockPositionInRange(
						List<MineTargetPrisonBlock> targetBlocks,
						boolean matched) {
		int position = -1;
		
		int rangeLow = getRangeBlockCountLowLimit();
		int rangeHigh = getRangeBlockCountHighLimit();

		List<Integer> choices = new ArrayList<>();
		
		for ( int i = rangeLow; i < rangeHigh && i < targetBlocks.size(); i++ ) {
			String bName = targetBlocks.get( i ).getPrisonBlock().getBlockName();
			
			if ( matched == getBlockName().equals( bName ) ) {
				choices.add( i );
			}
		}
		
		if ( choices.size() > 0 ) {
			int p = (int) (Math.random() * choices.size());
			
			position = choices.get( p );
		}
		
		return position;
	}

}

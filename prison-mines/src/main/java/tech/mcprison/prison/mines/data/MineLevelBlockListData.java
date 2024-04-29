package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;

public class MineLevelBlockListData
{

	private int currentMineLevel;
	
	private int maxMineLevel;
	
	private Mine mine;
	
	private Random random;
	
	private List<PrisonBlock> selectedBlocks;
	
	private PrisonBlock fillerBlock;
	
	private double totalChance = 0d;
	
	private double selectedChance = 0d;
	
	private double airChance = 0d;
	
	private boolean errorMessageSent = false;
	

	public MineLevelBlockListData( int currentMineLevel, int maxMineLevel, Mine mine, Random random ) {
		super();
		
		this.currentMineLevel = currentMineLevel;
		this.maxMineLevel = maxMineLevel;
		
		this.mine = mine;
		
		this.random = random;
		
		this.selectedBlocks = new ArrayList<>();
		
		initialize();
	}
	
	private void initialize() {
		
		totalChance = 0d;
		selectedChance = 0d;
		airChance = 0d;
		
		fillerBlock = null;
		
		
		// PrisonBlocks contains the percent chance of spawning:
		for ( PrisonBlock pBlock : mine.getPrisonBlocks() )
		{
			// First calculate the total percent chance:
			totalChance += pBlock.getChance();
			
			int cExcludeTop = pBlock.getConstraintExcludeTopLayers();
			int cExcludeBottom = pBlock.getConstraintExcludeBottomLayers();
			
			int cMin = pBlock.getConstraintMin();
			int cMax = pBlock.getConstraintMax();
			
			boolean hasConstraints = cExcludeTop != 0 || cExcludeBottom != 0 ||
					cMin != 0 || cMax != 0;
			
			boolean includeOnThisLevel = 
					cExcludeTop == 0 && cExcludeBottom == 0 ||
					
					(cExcludeTop == 0 || cExcludeTop != 0 && currentMineLevel > cExcludeTop) &&
					(cExcludeBottom == 0 || cExcludeBottom != 0 && currentMineLevel < cExcludeBottom );
					
			
			
			// If the block has no constraints, or if the block is within the 
			// mine constraints of top and bottom, then add it to our list.  
			// If block has exceed max constraint, let it pass through this 
			// code so it can get the update on the rangeBlockCountHigh.
			// Check to ensure that the max placement has not been exceeded and if
			// it has, exclude from this level. Note that past levels may have 
			// placed more than max.
			if ( includeOnThisLevel ) {
				
				
				// If block has reached it's max, then do not add it's chance or add
				// add it as a selected block:
				if ( cMax == 0 || 
						cMax != 0 && pBlock.getBlockPlacedCount() < cMax) {
					
					// Sum the selected blocks:
					selectedChance += pBlock.getChance();
					
					// Add the selected blocks to our list:
					selectedBlocks.add( pBlock );
					
					// Need to find a filler block that has no constraints used:
					if ( !hasConstraints && 
							(fillerBlock == null || 
							 fillerBlock != null && pBlock.getChance() > fillerBlock.getChance() ) ) {
						fillerBlock = pBlock;
					}
					
				}
				
				
				// Only set the rangeBlockCountLowLimit on the first pass through here.
				// The initial value for getRangeBlockCountLowLimit is -1.
				if ( pBlock.getRangeBlockCountLowLimit() == -1 ) {
					
					int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
					pBlock.setRangeBlockCountLowLimit( targetBlockPosition );
				}
				
				
				// Always set getConstraintExcludeBottomLayers value:
				// If exclude top layers, then do not record for the bottom layers until 
				// the top layers is cleared.
				int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
				pBlock.setRangeBlockCountHighLimit( targetBlockPosition );

			}
			
		}
		
		
		// Using the total chance, need to calculate the AIR percent:
		airChance = 100d - totalChance;
		
		// If airChance is not zero, add an AIR block to the selectedBlocks list:
		if ( airChance > 0d ) {
			PrisonBlock airBlock = PrisonBlock.AIR.clone();
			
			airBlock.setChance( airChance );
			
			selectedBlocks.add( airBlock );
		}
		
		
	}
	
	
	/**
	 * <p>For each block, need to update the rangeBlockCountHighLimit, even if the
	 * block has reached it's max constraint.  This is not tracking what was the
	 * last block that was placed, but it's tracking what is the max range of
	 * the targetBlocks collection (`mine.getMineTargetPrisonBlocks()`) in which 
	 * this block can be placed while honoring the constraints exclude from 
	 * top and exclude from bottom.
	 * </p>
	 */
	public void checkSelectedBlockExcludeFromBottomLayers() {
		
		for ( PrisonBlock pBlock : selectedBlocks )
		{
			int cExcludeTop = pBlock.getConstraintExcludeTopLayers();
			int cExcludeBottom = pBlock.getConstraintExcludeBottomLayers();
			
//			int cMin = pBlock.getConstraintMin();
//			int cMax = pBlock.getConstraintMax();
			
//			boolean hasConstraints = cExcludeTop != 0 || cExcludeBottom != 0 ||
//					cMin != 0 || cMax != 0;
			
			boolean includeOnThisLevel = 
					cExcludeTop == 0 && cExcludeBottom == 0 ||
					
					(cExcludeTop == 0 || cExcludeTop != 0 && currentMineLevel > cExcludeTop) &&
					(cExcludeBottom == 0 || cExcludeBottom != 0 && currentMineLevel < cExcludeBottom );
					
			
			
			// If the block has no constraints, or if the block is within the 
			// mine constraints of top and bottom, then add it to our list.  
			// If block has exceed max constraint, let it pass through this 
			// code so it can get the update on the rangeBlockCountHigh.
			// Check to ensure that the max placement has not been exceeded and if
			// it has, exclude from this level. Note that past levels may have 
			// placed more than max.
			if ( includeOnThisLevel ) {
				
				int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
				pBlock.setRangeBlockCountHighLimit( targetBlockPosition );

			}
			
//			// If exclude bottom layer is enabled, then we need to track every number
//			// until the currentLevel exceeds the getConstraintExcludeBottomLayers value.
//			// If exclude top layers, then do not record for the bottom layers until 
//			// the top layers is cleared.
//			if ( (pBlock.getConstraintExcludeTopLayers() > 0 && 
//					currentMineLevel > pBlock.getConstraintExcludeTopLayers() ||
//					pBlock.getConstraintExcludeTopLayers() == 0) &&
//					
//					(pBlock.getConstraintExcludeBottomLayers() == 0 ||
//					pBlock.getConstraintExcludeBottomLayers() > 0 && 
//					pBlock.getConstraintExcludeBottomLayers() < currentMineLevel)
//					) { 
//				
//				
//			}

		}
	}

	public PrisonBlock randomlySelectPrisonBlock()
	{
		PrisonBlock selected = null;

		// Will have a value of 100% if no blocks are excluded due to block constraints:
		double totalSelectedChance = selectedChance + airChance;
		
		double chance = random.nextDouble() * totalSelectedChance;
		
		for ( PrisonBlock block : selectedBlocks ) {
			
			// NOTE: do not have use this field anymore:
//			block.isIncludeInLayerCalculations();
			
			// If chance falls on this block, then select it as long as it has not
			// exceed the max count for this block if the max constraint is enabled.
			// If the block's constraint max is reached, then isIncludedInLayerCalculation will 
			// prevent more of them from being added to the mine.
			if ( chance <= block.getChance() ) {
				
				// If this block is chosen and it was not skipped, then use this block and exit.
				// Otherwise the chance will be recalculated and tried again to find a valid block,
				// since the odds have been thrown off...
				selected = block;
				
				break;
			} 
			else {
				chance -= block.getChance();
			}
		}
		
		// If block reaches it's max amount, remove it from the block list so it will not 
		// be selected again. If max is reached, then this block is the max number allowed.
		// Note that the block has not been incremented yet, so add one to the placement count
		if ( selected != null && 
				selected.getConstraintMax() > 0 &&
				(selected.getBlockPlacedCount() + 1) >= selected.getConstraintMax() ) {
			
//			selected.setIncludeInLayerCalculations( false );
			
			selectedBlocks.remove(selected);
			
			selectedChance -= selected.getChance();
		}
		
		// if selected == null, then the block feel through the cracks because
		// if the mine did not have a full 100% chance of all blocks combined, 
		// then an AIR block was added to the selectedBlocks collection when 
		// starting to process this level.  So if selected is null, then something
		// went wrong... assign it the filler block.
		// If all blocks have constraints, the the filler block will be null so
		// then assign it an AIR block.
		if ( chance == 0d && selected == null ) {
			if ( fillerBlock != null ) {
				selected = fillerBlock;
			}
			else {
				selected = PrisonBlock.AIR.clone();
				
				if ( !errorMessageSent ) {
					
					String msg = String.format( 
							"Error: generateBlockListAsync() selectBlock: "
							+ "Mine: %s  Layer: %d : All blocks "
							+ "have constraints so could not backfill a void with a block "
							+ "so using AIR. Add a non-constrained block to the mine to "
							+ "prevent air blocks from spawning in this mine when a valid "
							+ "block cannot be choosen.  This is not a bug, but it's an "
							+ "issue with base-2 being coverted to base-10 and the "
							+ "resulting random "
							+ "generated number not falling on any valid blocks. ",
							getMine().getName(), getCurrentMineLevel()
							);
					Output.get().logError( msg );
					errorMessageSent = true;
				}
			}
		}
		
		return selected;
	}

	public int getCurrentMineLevel() {
		return currentMineLevel;
	}
	public void setCurrentMineLevel(int currentMineLevel) {
		this.currentMineLevel = currentMineLevel;
	}

	public int getMaxMineLevel() {
		return maxMineLevel;
	}
	public void setMaxMineLevel(int maxMineLevel) {
		this.maxMineLevel = maxMineLevel;
	}

	public Mine getMine() {
		return mine;
	}
	public void setMine(Mine mine) {
		this.mine = mine;
	}

	public Random getRandom() {
		return random;
	}
	public void setRandom(Random random) {
		this.random = random;
	}

	public List<PrisonBlock> getSelectedBlocks() {
		return selectedBlocks;
	}
	public void setSelectedBlocks(List<PrisonBlock> selectedBlocks) {
		this.selectedBlocks = selectedBlocks;
	}

	public PrisonBlock getFillerBlock() {
		return fillerBlock;
	}
	public void setFillerBlock(PrisonBlock fillerBlock) {
		this.fillerBlock = fillerBlock;
	}

	public double getTotalChance() {
		return totalChance;
	}
	public void setTotalChance(double totalChance) {
		this.totalChance = totalChance;
	}

	public double getSelectedChance() {
		return selectedChance;
	}
	public void setSelectedChance(double selectedChance) {
		this.selectedChance = selectedChance;
	}

	public double getAirChance() {
		return airChance;
	}
	public void setAirChance(double airChance) {
		this.airChance = airChance;
	}
	
}

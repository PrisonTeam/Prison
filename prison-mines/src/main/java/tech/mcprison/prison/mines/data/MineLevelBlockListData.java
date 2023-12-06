package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tech.mcprison.prison.internal.block.PrisonBlock;

public class MineLevelBlockListData
{

	private int currentMineLevel;
	
	private Mine mine;
	
	private Random random;
	
	private List<PrisonBlock> selectedBlocks;
	
	private double totalChance = 0d;
	
	private double selectedChance = 0d;
	
	private double airChance = 0d;

	public MineLevelBlockListData( int currentMineLevel, Mine mine, Random random ) {
		super();
		
		this.currentMineLevel = currentMineLevel;
		this.mine = mine;
		
		this.random = random;
		
		this.selectedBlocks = new ArrayList<>();
		
		initialize();
	}
	
	private void initialize() {
		
		
		
		// PrisonBlocks contains the percent chance of spawning:
		for ( PrisonBlock pBlock : mine.getPrisonBlocks() )
		{
			// First calculate the total percent chance:
			totalChance += pBlock.getChance();
			
			// If the block has no constraints, or if the block is within the 
			// mine constraints, add it to our list.  
			// Check to ensure that the max placement has not been exceeded and if
			// it has, exclude from this level. Note that past levels may have 
			// placed more than max.
			if (
				( pBlock.getConstraintExcludeTopLayers() == 0 && 
					pBlock.getConstraintExcludeBottomLayers() == 0 ||
					
					pBlock.getConstraintExcludeTopLayers() <= currentMineLevel && 
						(pBlock.getConstraintExcludeBottomLayers() == 0 || 
						pBlock.getConstraintExcludeBottomLayers() > currentMineLevel) )
					
					) {
				
				// Sum the selected blocks:
				selectedChance += pBlock.getChance();
				
				// Add the selected blocks to our list:
				selectedBlocks.add( pBlock );
				
				
				// If exclude top layers is enabled, then only try to set the 
				// rangeBlockCountLowLimit once since we need the lowest possible 
				// value.  The initial value for getRangeBlockCountLowLimit is -1.
				if ( pBlock.getRangeBlockCountLowLimit() == -1 &&
						(pBlock.getConstraintExcludeTopLayers() <= 0 ||
						currentMineLevel > pBlock.getConstraintExcludeTopLayers())
						) {
					
					int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
					pBlock.setRangeBlockCountLowLimit( targetBlockPosition );
				}
				
				
				// If exclude bottom layer is enabled, then we need to track every number
				// until the currentLevel exceeds the getConstraintExcludeBottomLayers value.
				// If exclude top layers, then do not record for the bottom layers until 
				// the top layers is cleared.
				if ( (pBlock.getConstraintExcludeTopLayers() > 0 && 
						currentMineLevel > pBlock.getConstraintExcludeTopLayers() ||
						pBlock.getConstraintExcludeTopLayers() == 0) &&
						
						(pBlock.getConstraintExcludeBottomLayers() == 0 || 
						pBlock.getConstraintExcludeBottomLayers() > 0 && 
						pBlock.getConstraintExcludeBottomLayers() < currentMineLevel)
						) { 
					
					int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
					pBlock.setRangeBlockCountHighLimit( targetBlockPosition );
					
				}

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
	 * <p>For each block, run this to ensure all selected blocks that have a specified 
	 * exclusion from the lower levels of the mine, that the rangeBlockCountHighLimit is
	 * properly set.
	 * </p>
	 */
	public void checkSelectedBlockExcludeFromBottomLayers() {
		
		for ( PrisonBlock pBlock : selectedBlocks )
		{
			// If exclude bottom layer is enabled, then we need to track every number
			// until the currentLevel exceeds the getConstraintExcludeBottomLayers value.
			// If exclude top layers, then do not record for the bottom layers until 
			// the top layers is cleared.
			if ( (pBlock.getConstraintExcludeTopLayers() > 0 && 
					currentMineLevel > pBlock.getConstraintExcludeTopLayers() ||
					pBlock.getConstraintExcludeTopLayers() == 0) &&
					
					(pBlock.getConstraintExcludeBottomLayers() == 0 ||
					pBlock.getConstraintExcludeBottomLayers() > 0 && 
					pBlock.getConstraintExcludeBottomLayers() < currentMineLevel)
					) { 
				
				int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
				pBlock.setRangeBlockCountHighLimit( targetBlockPosition );
				
			}

		}
	}

	public PrisonBlock randomlySelectPrisonBlock()
	{
		PrisonBlock selected = null;

		// Will have a value of 100% if no blocks are excluded due to block constraints:
		double totalSelectedChance = selectedChance + airChance;
		
		double chance = random.nextDouble() * totalSelectedChance;
		
		for ( PrisonBlock block : selectedBlocks ) {
			
			// If chance falls on this block, then select it as long as it has not
			// exceed the max count for this block if the max constraint is enabled.
			// If the block's constraint max is reached, then isIncludedInLayerCalculation will 
			// prevent more of them from being added to the mine.
			if ( block.isIncludeInLayerCalculations() && chance <= block.getChance() ) {
				
				// If this block is chosen and it was not skipped, then use this block and exit.
				// Otherwise the chance will be recalculated and tried again to find a valid block,
				// since the odds have been thrown off...
				selected = block;
				
				break;
			} else {
				chance -= block.getChance();
			}
		}
		
		// If block reaches it's max amount, remove it from the block list.
		// Note that the block has not been incremented yet, so add one to the placement count
		if ( selected != null && 
				selected.getConstraintMax() > 0 &&
				(selected.getBlockPlacedCount() + 1) >= selected.getConstraintMax() ) {
			
			selected.setIncludeInLayerCalculations( false );
			
//			selectedBlocks.remove(selected);
			
//			selectedChance -= selected.getChance();
		}
		
		return selected;
	}
	
	
}

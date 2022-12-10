package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;

public class MineLevelBlockListData
{

	private int currentMineLevel;
	
	private Mine mine;
	
	private Random random;
	
	private List<PrisonBlockStatusData> selectedBlocks;
	
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
		Set<String> keys = mine.getBlockStats().keySet();
		
		for ( String key : keys  )
		{
			PrisonBlockStatusData blockStats = mine.getBlockStats().get( key );
			
			// First calculate the total percent chance:
			totalChance += blockStats.getChance();
			
			// If the block has no constraints, or if the block is within the 
			// mine constraints, add it to our list:
			if ( blockStats.getConstraintExcludeTopLayers() == 0 && 
					blockStats.getConstraintExcludeBottomLayers() == 0 ||
					
					blockStats.getConstraintExcludeTopLayers() <= currentMineLevel && 
						(blockStats.getConstraintExcludeBottomLayers() == 0 || 
								blockStats.getConstraintExcludeBottomLayers() > currentMineLevel) ) {
				
				// Sum the selected blocks:
				selectedChance += blockStats.getChance();
				
				// Add the selected blocks to our list:
				selectedBlocks.add( blockStats );
				
				
				// If exclude top layers is enabled, then only try to set the 
				// rangeBlockCountLowLimit once since we need the lowest possible 
				// value.  The initial value for getRangeBlockCountLowLimit is -1.
				if ( blockStats.getRangeBlockCountLowLimit() <= 0 &&
						currentMineLevel > blockStats.getConstraintExcludeTopLayers() ) {
					
					int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
					blockStats.setRangeBlockCountLowLimit( targetBlockPosition );
				}
				
				
				// If exclude bottom layer is enabled, then we need to track every number
				// until the currentLevel exceeds the getConstraintExcludeBottomLayers value.
				// If exclude top layers, then do not record for the bottom layers until 
				// the top layers is cleared.
				if ( (blockStats.getConstraintExcludeTopLayers() > 0 && 
						currentMineLevel > blockStats.getConstraintExcludeTopLayers() ||
						blockStats.getConstraintExcludeTopLayers() == 0) &&
						
						blockStats.getConstraintExcludeBottomLayers() > 0 && 
						blockStats.getConstraintExcludeBottomLayers() < currentMineLevel 
						) { 
					
					int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
					blockStats.setRangeBlockCountHighLimit( targetBlockPosition );
					
				}

			}
			
		}
		
		
		// Using the total chance, need to calculate the AIR percent:
		airChance = 100d - totalChance;
		
		// If airChance is not zero, add an AIR block to the selectedBlocks list:
		if ( airChance > 0d ) {
			PrisonBlock airBlock = PrisonBlock.AIR.clone();
			
			airBlock.setChance( airChance );
			
			selectedBlocks.add( new PrisonBlockStatusData( airBlock ) );
		}
		
		
	}
	
	
	/**
	 * <p>For each block, run this to ensure all selected blocks that have a specified 
	 * exclusion from the lower levels of the mine, that the rangeBlockCountHighLimit is
	 * properly set.
	 * </p>
	 */
	public void checkSelectedBlockExcludeFromBottomLayers() {
		
		for ( PrisonBlockStatusData blockStats : selectedBlocks )
		{
			// If exclude bottom layer is enabled, then we need to track every number
			// until the currentLevel exceeds the getConstraintExcludeBottomLayers value.
			// If exclude top layers, then do not record for the bottom layers until 
			// the top layers is cleared.
			if ( (blockStats.getConstraintExcludeTopLayers() > 0 && 
					currentMineLevel > blockStats.getConstraintExcludeTopLayers() ||
					blockStats.getConstraintExcludeTopLayers() == 0) &&
					
					blockStats.getConstraintExcludeBottomLayers() > 0 && 
					blockStats.getConstraintExcludeBottomLayers() < currentMineLevel 
					) { 
				
				int targetBlockPosition = mine.getMineTargetPrisonBlocks().size();
				blockStats.setRangeBlockCountHighLimit( targetBlockPosition );
				
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
			
			if ( chance <= block.getChance() ) {
				
				// If this block is chosen and it was not skipped, then use this block and exit.
				// Otherwise the chance will be recalculated and tried again to find a valid block,
				// since the odds have been thrown off...
				selected = block;
				
				break;
			} else {
				chance -= block.getChance();
			}
		}
		
		return selected;
	}
	
	
}

package tech.mcprison.prison.spigot.api;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.MaterialType;

/**
 * <p>These are some api end points to help access some core components within prison.
 * </p>
 * 
 * <p>Use of these are at your own risk.  Misuse can result in corruption of Prison's
 * internal data.
 * </p>
 *
 */
public class PrisonSpigotAPI {

	/**
	 * <p>This returns all mines that are within prison.
	 * </p>
	 * 
	 * @return
	 */
	public List<Mine> getMines() {
		List<Mine> results = new ArrayList<>();
		
    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();

    		results = mm.getMines();
    	}
		
		return results;
	}
	
	/**
	 * <p>Returns all mines within prison, but sorted by the specified sort order.
	 * Because some sort types omit mines, there are two different collections within the
	 * PrisonSortableResults.  There is an include and exclude list.
	 * </p>
	 * 
	 * <p>All sort types that omit mines from the result type has a counter sort type
	 * that will include all mines and will not omit any.  Those begin with an "x".
	 * </p>
	 * 
	 * @param sortOrder
	 * @return
	 */
	public PrisonSortableResults getMines( MineSortOrder sortOrder ) {
		PrisonSortableResults results = null;
		
    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();

    		results = mm.getMines(sortOrder);
    	}
		
		return results;
	}
	
	
	/**
	 * <p>This returns a list of all ranks.
	 * </p>
	 * 
	 * @return
	 */
	public List<Rank> getRanks() {
		List<Rank> results = new ArrayList<>();
		
		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled()
				) {
			RankManager rm = PrisonRanks.getInstance().getRankManager();
			
			results = rm.getRanks();
		}
		
		return results;
	}
	
	/**
	 * <p>This function verifies that the block name that you are trying to use is
	 * actually a valid block name within prison.  If it is invalid then a null value
	 * will be returned.
	 * </p>
	 * 
	 * <p>There are a lot of cross references that occur that ensures that the best
	 * match occurs to fit the requested block name to an actual prison block.
	 * Since prison supports minecraft 1.8 through 1.16 (and soon to be 1.17), there
	 * are various possible names for some blocks since their names have changed 
	 * between versions.  This also takes in to consideration prison block name 
	 * variations.
	 * </p>
	 * 
	 * @param blockName The name of a block that is intended to b validated
	 * @return The name of a valid block within prison
	 */
	public String getPrisonBlockName( String blockName ) {
		String results = null;
		
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
        	
        	PrisonBlock prisonBlock = Prison.get().getPlatform().getPrisonBlock( blockName );
        	if ( prisonBlock != null && prisonBlock.isMineable() ) {
        		results = prisonBlock.getBlockName();
        	}
        }
        else {
        	
        	BlockType blockType = BlockType.getBlock(blockName);
        	if (blockType != null && blockType.getMaterialType() == MaterialType.BLOCK ) {
        		results = blockType.getMaterialType().name();
        	}
        }
		
		return results;
	}
	
	/**
	 * <p>Provides a list of all mines that contains the specfied block.
	 * </p>
	 * 
	 * @param prisonBlockName The prison block name
	 * @return List of all mines that contains the specified block name
	 */
	public List<Mine> getMines( String prisonBlockName ) {
		List<Mine> results = new ArrayList<>();
		
		if ( prisonBlockName != null && prisonBlockName.trim().length() > 0 ) {
			
			PrisonBlock prisonBlock = null;
			BlockType blockType = null;
			
			if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
				
				prisonBlock = Prison.get().getPlatform().getPrisonBlock( prisonBlockName );
				if ( prisonBlock != null && !prisonBlock.isMineable() ) {
					prisonBlock = null;
				}
			}
			else {
				
				blockType = BlockType.getBlock( prisonBlockName );
				if (blockType != null && blockType.getMaterialType() != MaterialType.BLOCK ) {
					blockType = null;
				}
			}
			
			if ( prisonBlock != null || blockType != null ) {
				if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
					MineManager mm = PrisonMines.getInstance().getMineManager();
					
					List<Mine> mines = mm.getMines();
					for ( Mine mine : mines ) {
						if ( prisonBlock != null && mine.isInMine( blockType ) ||
								blockType != null && mine.isInMine( blockType ) ) {
							results.add( mine );
							break;
						}
					}
				}
			}
		}
		
		return results;
	}

}

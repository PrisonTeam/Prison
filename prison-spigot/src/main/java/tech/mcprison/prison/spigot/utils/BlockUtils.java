package tech.mcprison.prison.spigot.utils;

import java.util.HashMap;
import java.util.HashSet;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.util.Location;

public class BlockUtils
{
	private static BlockUtils instance;
	
	private HashMap<Location, UnbreakableBlockData> unbreakableBlocks;
	private HashMap<Mine, HashSet<UnbreakableBlockData>> unbreakableBlocksByMine;
	
	
	private BlockUtils() {
		super();
		
		this.unbreakableBlocks = new HashMap<>();
		this.unbreakableBlocksByMine = new HashMap<>();
	}
	
	public static BlockUtils getInstance() {
		if ( instance == null ) {
			synchronized ( BlockUtils.class )
			{
				if ( instance == null ) {
					instance = new BlockUtils();
				}
			}
		}
		return instance;
	}
	

	public boolean isUnbreakable( SpigotBlock block ) {
		boolean results = false;
		if ( block != null ) {
			PrisonBlock pBlock = block.getPrisonBlock();
			results = isUnbreakable( pBlock );
		}
		return results;
	}
	
	public boolean isUnbreakable( PrisonBlock block ) {
		
		return block == null || block.getLocation() == null ? false : 
						getUnbreakableBlocks().containsKey( block.getLocation() );
	}
	
	
	public UnbreakableBlockData addUnbreakable( PrisonBlock block, Mine mine ) {
		UnbreakableBlockData data = null;
		
		if ( block.getLocation() != null ) {
			data = new UnbreakableBlockData( block, mine );
			
			getUnbreakableBlocks().put( data.getKey(), data );
			
			if ( mine != null ) {
				if ( !getUnbreakableBlocksByMine().containsKey( mine ) ) {
					HashSet<UnbreakableBlockData> set = new HashSet<>();
					
					getUnbreakableBlocksByMine().put( mine, set );
				}
				
				getUnbreakableBlocksByMine().get( mine ).add( data );
			}
		}

		return data;
	}
	
	
	/**
	 * Removes all blocks from a specified mine.
	 * 
	 * @param mine
	 */
	public void removeUnbreakable( Mine mine ) {
		if ( mine != null ) {
			
			HashSet<UnbreakableBlockData> blocks = getUnbreakableBlocksByMine().get( mine );

			for ( UnbreakableBlockData data : blocks ) {
				getUnbreakableBlocks().remove( data.getKey() );

				if ( data.getTaskId() > 0 ) {
					// If task is still queued to run, cancel it:
					PrisonTaskSubmitter.cancelTask( data.getTaskId() );
				}
			}
			
			getUnbreakableBlocksByMine().remove( mine );
			
		}
	}
	
	public void removeUnbreakable( Location location ) {
		if ( location != null && getUnbreakableBlocks().containsKey( location ) ) {
			
			synchronized ( BlockUtils.class ) {
				
				if ( getUnbreakableBlocks().containsKey( location ) ) {
					
					UnbreakableBlockData data = getUnbreakableBlocks().remove( location );
					
					if ( data != null && data.getMine() != null && 
							getUnbreakableBlocksByMine().containsKey( data.getMine() )) {
						
						getUnbreakableBlocksByMine().get( data.getMine() ).remove( data );
					}
					
					if ( data.getTaskId() > 0 ) {
						// If task is still queued to run, cancel it:
						PrisonTaskSubmitter.cancelTask( data.getTaskId() );
					}
				}
			}
		}
	}
	public void removeUnbreakable( PrisonBlock block ) {
		if ( block != null ) {
			
			removeUnbreakable( block.getLocation() );
		}
	}
	
	public HashMap<Location, UnbreakableBlockData> getUnbreakableBlocks() {
		return unbreakableBlocks;
	}

	public HashMap<Mine, HashSet<UnbreakableBlockData>> getUnbreakableBlocksByMine() {
		return unbreakableBlocksByMine;
	}
}

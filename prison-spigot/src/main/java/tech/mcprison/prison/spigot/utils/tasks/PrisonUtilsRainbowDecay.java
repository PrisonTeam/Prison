package tech.mcprison.prison.spigot.utils.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.features.MineTargetBlockKey;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockEventEvent;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.utils.tasks.PrisonUtilsTaskTypes.PrisonUtilsTaskTypRemoveUnbreakableBlock;
import tech.mcprison.prison.spigot.utils.tasks.PrisonUtilsTaskTypes.PrisonUtilsTaskTypReplaceBlock;
import tech.mcprison.prison.spigot.utils.tasks.PrisonUtilsTaskTypes.PrisonUtilsTaskTypeDelay;
import tech.mcprison.prison.util.Location;

public class PrisonUtilsRainbowDecay {
	
	private List<PrisonUtilsTaskTypes> tasks;
	
	private PrisonMinesBlockEventEvent event;
	private HashMap<MineTargetBlockKey, SpigotBlock> unbreakableBlockList;
	
	private List<PrisonBlock> rainbowList;
	
	public PrisonUtilsRainbowDecay( PrisonMinesBlockEventEvent event,
							HashMap<MineTargetBlockKey, SpigotBlock> hashMap ) {
		super();
		
		this.event = event;
		this.unbreakableBlockList = hashMap;
		
		this.tasks = new ArrayList<>();
		
		this.rainbowList = new ArrayList<>();
		
		addBlock( XMaterial.BLACK_WOOL );
		addBlock( XMaterial.PURPLE_WOOL );
		addBlock( XMaterial.BLUE_WOOL );
		addBlock( XMaterial.GREEN_WOOL );
		addBlock( XMaterial.YELLOW_WOOL );
		addBlock( XMaterial.ORANGE_WOOL );
		addBlock( XMaterial.RED_WOOL );
		addBlock( XMaterial.PINK_WOOL );
		addBlock( XMaterial.WHITE_WOOL );
		addBlock( XMaterial.AIR );

		
		buildTasks();
		
	}

	private void buildTasks() {
		// Generate a list of blocks from the event.  Keep in mind that the main block
		// may be null with some source events:
		List<SpigotBlock> blocks = new ArrayList<>();
		
		// The lockList will keep the keys to the blocks, so they can be removed later:
		List<MineTargetBlockKey> lockList = new ArrayList<>();
		
		
		// If primary block exists, add it first since that's the one we knew they hit:
		if ( getEvent().getSpigotBlock() != null ) {
			blocks.add( getEvent().getSpigotBlock() );
		}
		
		// Then add all other event blocks:
		blocks.addAll( getEvent().getExplodedBlocks() );
		
	
		// We now have our block colors and our spigot blocks (has location).
		// Now we can start to build our tasks...
	
		
		// 1. First add all blocks to the unbreakableBlockList.  Saving the key for later use.
		for ( SpigotBlock spigotBlock : blocks ) {
			MineTargetBlockKey key = new MineTargetBlockKey( spigotBlock.getLocation() );
			lockList.add( key );
			
			getUnbreakableBlockList().put( key, spigotBlock );
		}
		
		// 2. Loop through all colors for outer loop:
		for ( PrisonBlock colorBlock : getRainbowList() ) {
			
			for ( SpigotBlock block : blocks ) {
				Location location = block.getLocation();
				
				// Create a block change task:
				PrisonUtilsTaskTypReplaceBlock replaceBlockTask = 
						new PrisonUtilsTaskTypReplaceBlock( location, colorBlock );
				
				getTasks().add( replaceBlockTask );
				
			}
			
			// Set a 4 tick delay
			PrisonUtilsTaskTypeDelay delay = new PrisonUtilsTaskTypeDelay( 3 );
			getTasks().add( delay );
			
		}
		
		// The tasks are completed and the last block should be AIR so we can now release
		// the locks on those locations:
		for ( MineTargetBlockKey blockKey : lockList )
		{
			PrisonUtilsTaskTypRemoveUnbreakableBlock release = 
					new PrisonUtilsTaskTypRemoveUnbreakableBlock( blockKey, getUnbreakableBlockList() );
					getTasks().add( release );
		}
		
		// DONE!  Task lists is fully generated!!
		
	}
	
	private void addBlock( XMaterial xMat ) {
		getRainbowList().add( SpigotUtil.getPrisonBlock( xMat.name() ) );
	}
	
	public List<PrisonBlock> getRainbowList() {
		return rainbowList;
	}

	public List<PrisonUtilsTaskTypes> getTasks() {
		return tasks;
	}

	public PrisonMinesBlockEventEvent getEvent() {
		return event;
	}

	public HashMap<MineTargetBlockKey, SpigotBlock> getUnbreakableBlockList() {
		return unbreakableBlockList;
	}
	
}

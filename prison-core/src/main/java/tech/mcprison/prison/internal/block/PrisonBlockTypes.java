package tech.mcprison.prison.internal.block;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;

/**
 * <p>This class is a new way of dealing with blocks within prison.
 * All blocks will be stored and used as string values.
 * </p>
 *
 */
public class PrisonBlockTypes {
	
	private List<PrisonBlock> blockTypes;
	
	public enum InternalBlockTypes {
		IGNORE,
		NULL_BLOCK
	}

	public PrisonBlockTypes() {
		super();
		
		this.blockTypes = new ArrayList<>();
		
		
	}
	
	public void loadServerBlockTypes() {
		
		// First clear the blockTypes:
		getBlockTypes().clear();
		
		// Add in the internal block types and mark them as not mineable.
		for ( InternalBlockTypes iBlockType : InternalBlockTypes.values() ) {
			PrisonBlock block = new PrisonBlock( iBlockType.name() );
			block.setMineable( false );
			
			getBlockTypes().add( block );
		}
		
		// Next using the server's platform, load all of the available blockTypes.
		Prison.get().getPlatform().getAllPlatformBlockTypes( getBlockTypes() );
		
	}

	public List<PrisonBlock> getBlockTypes() {
		return blockTypes;
	}
	public void setBlockTypes( List<PrisonBlock> blockTypes ) {
		this.blockTypes = blockTypes;
	}
	
}

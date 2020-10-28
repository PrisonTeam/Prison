package tech.mcprison.prison.internal.block;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;

/**
 * <p>This class is a new way of dealing with blocks within prison.
 * All blocks will be stored and used as string values.
 * </p>
 *
 */
public class PrisonBlockTypes {
	
	private List<PrisonBlock> blockTypes;
	private TreeMap<String, PrisonBlock> blockTypesByName;
	
	public enum InternalBlockTypes {
		IGNORE,
		NULL_BLOCK
	}

	public PrisonBlockTypes() {
		super();
		
		this.blockTypes = new ArrayList<>();
		
		this.blockTypesByName = new TreeMap<>();
		
	}
	
	public void loadServerBlockTypes() {
		
		// First clear the blockTypes:
		getBlockTypes().clear();
		
		// Add in the internal block types and mark them as not mineable.
		for ( InternalBlockTypes iBlockType : InternalBlockTypes.values() ) {
			PrisonBlock block = new PrisonBlock( iBlockType.name() );
			block.setBlock( false );
			
			getBlockTypes().add( block );
		}
		
		// Next using the server's platform, load all of the available blockTypes.
		Prison.get().getPlatform().getAllPlatformBlockTypes( getBlockTypes() );

		// Map all available blocks to the blockTypesByName map:
		for ( PrisonBlock pb : getBlockTypes() ) {
			getBlockTypesByName().put( pb.getBlockName().toLowerCase(), pb );
		}
	}

	public List<PrisonBlock> getBlockTypes() {
		return blockTypes;
	}
	public void setBlockTypes( List<PrisonBlock> blockTypes ) {
		this.blockTypes = blockTypes;
	}

	public TreeMap<String, PrisonBlock> getBlockTypesByName() {
		return blockTypesByName;
	}
	public void setBlockTypesByName( TreeMap<String, PrisonBlock> blockTypesByName ) {
		this.blockTypesByName = blockTypesByName;
	}
	
}

package tech.mcprison.prison.internal.block;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;

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
		AIR,
		GLASS,
		IGNORE,
		NULL_BLOCK
	}

	public PrisonBlockTypes() {
		super();
		
		this.blockTypes = new ArrayList<>();
		
		this.blockTypesByName = new TreeMap<>();
		
		initializeBlockTypes();
	}
	
	/**
	 * <p>This internally sets up the internal block types that should be
	 * accessible to the end users. For example, INGORE needs to be exposed
	 * so it can be used within mines, but NULL_BLOCK should never be 
	 * exposed since it is used in block caching.  At best, it should only
	 * be just one or a few blocks.
	 * </p> 
	 * 
	 * <p>When all bukkit blocks are verified and added, it would use the 
	 * <pre>addBlockTypes</pre> to add in those blocks.  The bukkit block
	 * lists must be added prior to any custom blocks.
	 * </p>
	 */
	private void initializeBlockTypes() {
		
		// First clear the blockTypes:
		getBlockTypes().clear();
		
		
		// Add in prison's internal block types here:
		getBlockTypes().add( PrisonBlock.IGNORE );

		
		// Map all available blocks to the blockTypesByName map:
		for ( PrisonBlock pb : getBlockTypes() ) {
			getBlockTypesByName().put( pb.getBlockName().toLowerCase(), pb );
		}
	}
	
	/**
	 * <p>This function adds in supported block types to the listings of 
	 * valid blocks that are available for use on the server instance that
	 * is being ran.  Spigot v1.8.8 will produce a different set of blocks
	 * than what Spigot v1.16.4 would produce.
	 * </p>
	 * 
	 * <p>The bukkit blocks must be added prior to any custom blocks.
	 * When a block is added, it first confirms if the block name already
	 * exists.  If it does exist, then it sets that block so it will 
	 * automatically use the prefix to make sure it is unique. 
	 * </p>
	 * 
	 * @param blockTypes
	 */
	public void addBlockTypes( List<PrisonBlock> blockTypes ) {
		
		// Map all available blocks to the blockTypesByName map:
		for ( PrisonBlock pb : blockTypes ) {
			
			// Check to see if this current block pb already exists, if it does
			// then set the prefix usage:
			if ( getBlockTypesByName().containsKey( pb.getBlockName().toLowerCase() )) {
				
				pb.setUseBlockTypeAsPrefix( true );
			}
			
			getBlockTypesByName().put( pb.getBlockName().toLowerCase(), pb );
			getBlockTypes().add( pb );
			
			if ( pb.getBlockType() != PrisonBlockType.minecraft ) {
				
				getBlockTypesByName().put( pb.getBlockNameSearch().toLowerCase(), pb );
			}
		}
	}

	public List<PrisonBlock> getBlockTypes( String searchTerm, boolean restrictToBlocks ) {
		List<PrisonBlock> results = new ArrayList<>();
    	
    	for ( PrisonBlock pBlock : getBlockTypes() ) {
    		if ( (!restrictToBlocks || restrictToBlocks && pBlock.isBlock()) && 
    				pBlock.getBlockNameSearch().toLowerCase().contains( searchTerm.toLowerCase()  )) {
    			results.add( pBlock );
    		}
    	}
    	return results;
	}
	
	public List<PrisonBlock> getBlockTypes() {
		return blockTypes;
	}
	public void setBlockTypes( List<PrisonBlock> blockTypes ) {
		this.blockTypes = blockTypes;
	}

	/**
	 * <p>Gets the block by name.  The block that is returned is cloned so 
	 * each mine can have it's own independent instance so as to prevent conflict with
	 * the stats.
	 * </p>
	 * 
	 * @param blockName
	 * @return
	 */
	public PrisonBlock getBlockTypesByName( String blockName ) {
		PrisonBlock results = null;
		
		if ( blockName != null ) {
			
			blockName = blockName.toLowerCase();
			if ( "air".equals( blockName ) ) {
				results = PrisonBlock.AIR;
			}
			else if ( blockName.startsWith( PrisonBlockType.minecraft.name() + ":" )) {
				blockName = blockName.replaceAll( PrisonBlockType.minecraft.name() + ":", "" );
			}
			
			results = searchBlockTypesByName( blockName );
			
			if ( results != null ) {
				results = results.clone();
			}
		}
		return results;
	}
	
	private PrisonBlock searchBlockTypesByName( String blockName ) {
		PrisonBlock block = blockTypesByName.get( blockName );
		
		if ( block == null && !blockName.contains( ":" ) ) {
			for ( PrisonBlockType blockType : PrisonBlockType.values()  )
			{
				block = blockTypesByName.get( blockType.name() + ":" + blockName );
				if ( block != null ) {
					break;
				}
			}
		}
		
		return block;
	}
	
	
	public TreeMap<String, PrisonBlock> getBlockTypesByName() {
		return blockTypesByName;
	}
	public void setBlockTypesByName( TreeMap<String, PrisonBlock> blockTypesByName ) {
		this.blockTypesByName = blockTypesByName;
	}
	
}

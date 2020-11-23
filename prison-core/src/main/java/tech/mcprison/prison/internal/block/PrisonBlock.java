package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.internal.block.PrisonBlockTypes.InternalBlockTypes;

/**
 * <p>This class embodies the nature of the block and different behaviors, if
 * they exist.  The block name should be based upon the XMaterial name if
 * possible to ensure correct mapping for different versions of spigot.
 * </p>
 *
 */
public class PrisonBlock
			implements Comparable<PrisonBlock> {
	
	public static PrisonBlock AIR;
	public static PrisonBlock IGNORE;
	public static PrisonBlock NULL_BLOCK;
	
	private String blockName;
	
	private double chance;
	
	private boolean valid = true;
	private boolean block = true;
	
	private boolean legacyBlock = false;
	
	static {
		AIR = new PrisonBlock( InternalBlockTypes.AIR.name(), false );
		IGNORE = new PrisonBlock( InternalBlockTypes.IGNORE.name(), false );
		NULL_BLOCK = new PrisonBlock( InternalBlockTypes.NULL_BLOCK.name(), false );
	}
	
	/**
	 * The name of this block should be based upon the XMaterial name in all 
	 * lower case.
	 * 
	 * @param blockName
	 */
	public PrisonBlock( String blockName ) {
		this( blockName, 0);
	}
	public PrisonBlock( String blockName, boolean block ) {
		this( blockName, 0);
		this.block = block;
	}

	/**
	 * The block name will be set to all lower case for consistency when searching and mapping.
	 * 
	 * @param blockName
	 * @param chance
	 */
	public PrisonBlock( String blockName, double chance ) {
		super();
		
		this.blockName = blockName.toLowerCase();
		this.chance = chance;
	}
	
	
	@Override
	public String toString() {
		return getBlockName() + " " + Double.toString( getChance() );
	}
	
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName( String blockName ) {
		this.blockName = blockName;
	}

	public double getChance() {
		return chance;
	}
	public void setChance( double chance ) {
		this.chance = chance;
	}

	public boolean isValid() {
		return valid;
	}
	public void setValid( boolean valid ) {
		this.valid = valid;
	}

	public boolean isBlock() {
		return block;
	}
	public void setBlock( boolean isBlock ){
		this.block = isBlock;
	}

	/**
	 * <p>This value isLegacyBlock indicates that there was not a direct match
	 * with the stored (saved) name of the block, and list of valid block types
	 * for the server.  In order to find a successful match, had to use the 
	 * obsolete BlockType to make a connection to the valid block types.
	 * </p>
	 * 
	 * <p>If this value is set to true, it only has a purpose during the loading 
	 * of the mine when the server is starting up.  If it is set to true, then it
	 * indicates that the block name that was saved to disk is not directly translatable
	 * to a valid block type, and therefore once the mine is loaded, if any of the
	 * blocks have this set, then the mine must be saved to store the 
	 * correct block name.  The next time it is loaded, it will not have to
	 * fall back to the legacy BlockType for a conversion.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isLegacyBlock() {
		return legacyBlock;
	}
	public void setLegacyBlock( boolean legacyBlock ) {
		this.legacyBlock = legacyBlock;
	}

	@Override
	public boolean equals( Object block ) {
		boolean results = false;

		if ( block != null && block instanceof PrisonBlock) {
			results = getBlockName().equalsIgnoreCase( ((PrisonBlock) block).getBlockName() );
		}
		
		return results;
	}
	
	@Override
	public int compareTo( PrisonBlock block )
	{
		int results = 0;
		
		if ( block == null ) {
			results = 1;
		}
		else {
			results = getBlockName().compareToIgnoreCase( block.getBlockName() );
		}
			
		return results;
	}
	
}

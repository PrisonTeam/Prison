package tech.mcprison.prison.internal.block;

/**
 * <p>This class embodies the nature of the block and different behaviors, if
 * they exist.
 * </p>
 *
 */
public class PrisonBlock {
	private String blockName;
	
	private double chance;
	
	private boolean valid = true;
	private boolean mineable = true;
	
	private boolean legacyBlock = false;
	
	public PrisonBlock( String blockName ) {
		this( blockName, 0);
	}

	public PrisonBlock( String blockName, double chance ) {
		super();
		
		this.blockName = blockName;
		this.chance = chance;
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

	public boolean isMineable() {
		return mineable;
	}
	public void setMineable( boolean mineable ){
		this.mineable = mineable;
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
	
}

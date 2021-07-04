package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlockTypes.InternalBlockTypes;
import tech.mcprison.prison.util.Location;

/**
 * <p>This class embodies the nature of the block and different behaviors, if
 * they exist.  The block name should be based upon the XMaterial name if
 * possible to ensure correct mapping for different versions of spigot.
 * </p>
 *
 */
public class PrisonBlock
			extends PrisonBlockStatusData
			implements Comparable<PrisonBlock>, 
				BlockExtendedDescription {
	
	public static PrisonBlock AIR;
	public static PrisonBlock GLASS;
	public static PrisonBlock IGNORE;
	public static PrisonBlock NULL_BLOCK;
	
	private PrisonBlockType blockType;
	private boolean useBlockTypeAsPrefix = false;
	
//	private String blockName;
	
//	private double chance;
	
	private boolean valid = true;
	private boolean block = true;
	
	private boolean legacyBlock = false;
	
	private Location location = null;
	
	static {
		AIR = new PrisonBlock( InternalBlockTypes.AIR.name(), false );
		GLASS = new PrisonBlock( InternalBlockTypes.GLASS.name(), true );
		IGNORE = new PrisonBlock( InternalBlockTypes.IGNORE.name(), false );
		NULL_BLOCK = new PrisonBlock( InternalBlockTypes.NULL_BLOCK.name(), false );
	}
	
	public enum PrisonBlockType {
		minecraft,
		CustomItems
	}
	
	/**
	 * The name of this block should be based upon the XMaterial name in all 
	 * lower case.
	 * 
	 * @param blockName
	 */
	public PrisonBlock( String blockName ) {
		this( PrisonBlockType.minecraft, blockName, 0, 0);
	}
	public PrisonBlock( PrisonBlockType blockType, String blockName ) {
		this( blockType, blockName, 0, 0);
	}
	
	public PrisonBlock( String blockName, boolean block ) {
		this( PrisonBlockType.minecraft, blockName, 0, 0);
		this.block = block;
	}

	/**
	 * The block name will be set to all lower case for consistency when searching and mapping.
	 * 
	 * @param blockName
	 * @param chance
	 */
	public PrisonBlock( PrisonBlockType blockType, String blockName, double chance, long blockCountTotal ) {
		super( blockName, chance, blockCountTotal );

		this.blockType = blockType;
		
//		this.blockName = blockName.toLowerCase();
//		this.chance = chance;
		
	}
	
	public PrisonBlock( PrisonBlock clonable ) {
		this( clonable.getBlockType(), clonable.getBlockName(), clonable.getChance(), clonable.getBlockCountTotal() );
		
		this.useBlockTypeAsPrefix = clonable.isUseBlockTypeAsPrefix();
		this.valid = clonable.isValid();
		this.block = clonable.isBlock();
		this.legacyBlock = clonable.isLegacyBlock();
		
		
		this.location = clonable == null || clonable.getLocation() == null ? null : 
									new Location( clonable.getLocation() );
	}
	
	@Override
	public String toString() {
		return getBlockType().name() + ": " + getBlockName() +
				( getChance() > 0 ? " " + Double.toString( getChance()) : "");
	}
	
	public PrisonBlockType getBlockType() {
		return blockType;
	}
	public void setBlockType( PrisonBlockType blockType ) {
		this.blockType = blockType;
	}

//	public String getBlockName() {
//		return blockName;
//	}
//	public void setBlockName( String blockName ) {
//		this.blockName = blockName;
//	}
	
	/**
	 * <p>This function always prefixes the block name with the BlockType.
	 * This is critical when saving the block to a file because there 
	 * are no guarantees that when the server restarts the environment
	 * will be the same.  There is a good chance that if new blocks are 
	 * added, or if new plugins are added with new collection of custom 
	 * blocks, then there could be a conflict.  There is also the 
	 * chance that a plugin could be removed for a block that's in a 
	 * mine too.
	 * </p>
	 * 
	 * <p>So having the BlockType as a prefix will help correctly align
	 * the blocks back to their proper source.  This is critical because
	 * the correct plugin must handle both the block placements, and also
	 * the correct plugin must be used when breaking the blocks.
	 * </p>
	 *  
	 * @return
	 */
	public String getBlockNameFormal() {
		return getBlockType().name() + ":" + getBlockName();
	}
	
	/**
	 * <p>This provides the blockName prefixed with the block type if it is not
	 * a type of minecraft. 
	 * </p>
	 * 
	 * @return
	 */
	public String getBlockNameSearch() {
		return getBlockType() != PrisonBlockType.minecraft ? 
					getBlockType().name() + ":" + getBlockName() : getBlockName();
	}

	
	public String getBlockCoordinates() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getBlockNameFormal() );
		
		if ( getLocation() != null ) {
			sb.append( "::" );
			
			sb.append( getLocation().toWorldCoordinates() );
		}
		
		return sb.toString();
	}
	
	public static PrisonBlock fromBlockName( String blockName ) {
		PrisonBlock results = null;
		
		if ( blockName != null && !blockName.trim().isEmpty() ) {
			
			results = Prison.get().getPlatform().getPrisonBlock( blockName );
		}
		
		return results;
	}
	
	public static PrisonBlock fromBlockCoordinates( String blockCoordinates ) {
		PrisonBlock results = null;
		
		if ( blockCoordinates != null && !blockCoordinates.trim().isEmpty() ) {
			String[] bc = blockCoordinates.split( "::" );
			String blockName = bc[0];
			String coordinates = bc.length >= 2 ? bc[1] : null;
			
			results = fromBlockName( blockName );
			
			if ( results != null && coordinates != null ) {
				Location location = Location.decodeWorldCoordinates( coordinates );
				
				if ( location != null ) {
					results.setLocation( location );
				}
			}
		}
		
		return results;
	}
	
	/**
	 * When adding custom blocks to prison, there is a check to ensure 
	 * that the name is not in conflict with a preexisting block name.
	 * If there is a conflict, then this field will be set to true and
	 * then the BlockType will be used as the prefix.
	 * 
	 * @return
	 */
	public boolean isUseBlockTypeAsPrefix() {
		return useBlockTypeAsPrefix;
	}
	public void setUseBlockTypeAsPrefix( boolean useBlockTypeAsPrefix ) {
		this.useBlockTypeAsPrefix = useBlockTypeAsPrefix;
	}
	
//	public double getChance() {
//		return chance;
//	}
//	public void setChance( double chance ) {
//		this.chance = chance;
//	}

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

	public Location getLocation() {
		return location;
	}
	public void setLocation( Location location ) {
		this.location = location;
	}
	
	public PrisonBlock clone() {
		return new PrisonBlock( this );
	}
	
	@Override
	public boolean equals( Object block ) {
		boolean results = false;

		if ( block != null && block instanceof PrisonBlock) {
			results = getBlockNameFormal().equalsIgnoreCase( ((PrisonBlock) block).getBlockNameFormal() );
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
	
	@Override
	public boolean isAir() {
		return compareTo( AIR ) == 0;
	}
	
}

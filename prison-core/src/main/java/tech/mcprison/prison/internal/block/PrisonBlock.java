package tech.mcprison.prison.internal.block;

import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlockTypes.InternalBlockTypes;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

/**
 * <p>This class embodies the nature of the block and different behaviors, if
 * they exist.  The block name should be based upon the XMaterial name if
 * possible to ensure correct mapping for different versions of spigot.
 * </p>
 *
 */
public class PrisonBlock
			extends PrisonBlockStatusData
			implements Block,
				BlockState,
				Comparable<PrisonBlock>, 
				BlockExtendedDescription {
	
	public static PrisonBlock AIR;
	public static PrisonBlock GLASS;
	public static PrisonBlock PINK_STAINED_GLASS;
	public static PrisonBlock SELECTION_WAND;
	public static PrisonBlock BLAZE_ROD;
	public static PrisonBlock LAPIS_ORE;
	public static PrisonBlock IGNORE;
	public static PrisonBlock NULL_BLOCK;
	
	private boolean useBlockTypeAsPrefix = false;
	
	private boolean valid = true;
	private boolean block = true;
	private boolean sellallOnly = false;
	
	private boolean legacyBlock = false;
	
	private Location location = null;
	
	private Double salePrice = null;
	private Double purchasePrice = null;
	private boolean loreAllowed = false;
	
	
	static {
		AIR = new PrisonBlock( InternalBlockTypes.AIR.name(), false );
		GLASS = new PrisonBlock( InternalBlockTypes.GLASS.name(), true );
		PINK_STAINED_GLASS = new PrisonBlock( InternalBlockTypes.PINK_STAINED_GLASS.name(), true );
		SELECTION_WAND = new PrisonBlock( InternalBlockTypes.BLAZE_ROD.name(), false );
		SELECTION_WAND.setDisplayName( "&6Selection Wand" );
		
		BLAZE_ROD = new PrisonBlock( InternalBlockTypes.BLAZE_ROD.name(), false );
		LAPIS_ORE = new PrisonBlock( InternalBlockTypes.LAPIS_ORE.name(), true );
		IGNORE = new PrisonBlock( InternalBlockTypes.IGNORE.name(), true );
		NULL_BLOCK = new PrisonBlock( InternalBlockTypes.NULL_BLOCK.name(), false );
	}
	
	public enum PrisonBlockType {
		minecraft,
		CustomItems,
		ItemsAdder,
		heads;
		
		public boolean isCustomBlockType() {
			return this != minecraft;
		}
	}
	
	/**
	 * The name of this block should be based upon the XMaterial name in all 
	 * lower case.
	 * 
	 * @param blockName
	 */
	public PrisonBlock( String blockName ) {
		this( PrisonBlockType.minecraft, blockName, 0, 0);
		
		// If the blockName as a PrisonBlockType, then strip it off of the blockName
		// and set the new name for the block, plus set the BlockType.
		for (PrisonBlockType bType : PrisonBlockType.values() ) {
			String blockType = bType.name() + ":";
			
			if ( blockName.startsWith( blockType ) ) {
				blockName = blockName.replace( blockType, blockName );
				setBlockName( blockName );
				setBlockType( bType );
				
				break;
			}
		}
		
		// If there is still a ':' then everything after that is a formatted displayName.
		// Strip it off save it.
		if ( getBlockName().contains( ":" ) ) {
			String[] bNameDisplayName = getBlockName().split(":");
			
			if ( bNameDisplayName.length == 2 ) {
				setBlockName( bNameDisplayName[0] );
				setDisplayName( bNameDisplayName[1] );
			}
		}
		
	}
	public PrisonBlock( String blockName, String displayName  ) {
		this( PrisonBlockType.minecraft, blockName, 0, 0);
		
		setDisplayName( displayName );
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
		super( blockType, blockName, chance, blockCountTotal );

	}
	
	public PrisonBlock( PrisonBlock clonable ) {
		this( clonable.getBlockType(), 
				clonable.getBlockName(), 
				clonable.getChance(), 
				clonable.getBlockCountTotal() );
		
		this.setDisplayName( clonable.getDisplayName() );

		this.useBlockTypeAsPrefix = clonable.isUseBlockTypeAsPrefix();
		this.valid = clonable.isValid();
		this.block = clonable.isBlock();
		this.sellallOnly = clonable.isSellallOnly();
		
		this.legacyBlock = clonable.isLegacyBlock();
		
		this.setLoreAllowed( clonable.isLoreAllowed() );
		this.setSalePrice( clonable.getSalePrice() );
		this.setPurchasePrice( clonable.getPurchasePrice() );
		
		this.location = clonable == null || clonable.getLocation() == null ? null : 
									new Location( clonable.getLocation() );
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( getBlockNameSearch() );
		
		if ( getChance() > 0 ) {
			sb.append( " " )
				.append( Double.toString( getChance()) );
		}
		
		return sb.toString();
	}
	
	
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
	 * <p>If the PrisonBlock has a display name, the color codes are stripped and then
	 * set to lower case, and spaces are replaced with '_'. Then the block search name is
	 * appended with the display name preceded with a ':'.
	 * 
	 * @return
	 */
	public String getBlockNameSearch() {
		
		String blockType = getBlockType() != PrisonBlockType.minecraft ? 
				getBlockType().name() + ":" : "";
		
		String blockName = getBlockName().toLowerCase();
		
		
		String displayName = getDisplayNameText();
		if ( displayName != null ) {
			displayName = ":" + displayName.toLowerCase().replace(" ", "_");
		}
		else {
			displayName = "";
		}
				
		return blockType + blockName + displayName;
	}

	public String getDisplayNameText() {
		String results = getDisplayName();
		
		if ( results != null ) {
			results = Text.stripColor( results.trim() );
		}
		
		return results;
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
	 * <p>If isSellallOnly, this item should never be used within a mine since prison
	 * cannot regenerate the block to place it in the mines.  Custom blocks have
	 * many uncontrollable aspects on how they create and use blocks, of which 
	 * prison cannot begin to magically guess all of these requirements.
	 * </p>
	 * 
	 * <p>If this value is set to true, then the PrisonBlock was added to prison as
	 * a custom block that is supported only within sellall.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isSellallOnly() {
		return sellallOnly;
	}
	public void setSellallOnly(boolean sellallOnly) {
		this.sellallOnly = sellallOnly;
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
	
	
	/**
	 * <p>The sale price is the amount that is paid to the player when they
	 * sell their items. A null indicates that no price was set.
	 * </p>
	 * 
	 * @return
	 */
	public Double getSalePrice() {
		return salePrice;
	}
	
	/**
	 * <p>The sale price is the amount that is paid to the player when they
	 * sell their items. A null indicates that no price was set.
	 * </p>
	 * 
	 * @param price
	 */
	public void setSalePrice( Double price ) {
		this.salePrice = price;
	}
	
	/**
	 * <p>The purchase price is what player pays to purchase the items.
	 *  A null indicates that no price was set.
	 * </p>
	 * 
	 * @return
	 */
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	
	/**
	 * <p>The purchase price is what player pays to purchase the items.,
	 *  A null indicates that no price was set.
	 * </p>
	 * 
	 * @param price
	 */
	public void setPurchasePrice( Double price ) {
		this.purchasePrice = price;
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
			
			results = getBlockType() == block.getBlockType() ? 0 : 1;
			
			if ( results == 0 ) {
				
				results = getBlockName().compareToIgnoreCase( block.getBlockName() );
				
				if ( results == 0 && getBlockNameSearch() != null && block.getBlockNameSearch() != null ) {
					results = getBlockNameSearch().compareToIgnoreCase( block.getBlockNameSearch() );
				}
			}
		}
			
		return results;
	}
	
	@Override
	public boolean isAir() {
		return compareTo( AIR ) == 0;
	}
	@Override
	public Block getRelative( BlockFace face )
	{
		Block results = null;
		
		if ( getLocation() != null ) {
			
			Location loc = new Location( getLocation() );
			
			switch ( face )
			{
				case NORTH: {
					// North is z axis in the negative direction:
					results = loc.getBlockAtDelta( 0, 0, -1 );
					break;
				}
				case SOUTH: {
					// South is z axis in the positive direction:
					results = loc.getBlockAtDelta( 0, 0, 1 );
					break;
				}
				case EAST: {
					// East is x axis in the positive direction:
					results = loc.getBlockAtDelta( 1, 0, 0 );
					break;
				}
				case WEST: {
					// West is x axis in the negative direction:
					results = loc.getBlockAtDelta( -1, 0, 0 );
					break;
				}
				case TOP: 
				case UP: {
					// TOP and UP is y axis in the positive direction:
					results = loc.getBlockAtDelta( 0, 1, 0 );
					break;
				}
				case BOTTOM: 
				case DOWN: {
					// BOTTOM and DOWN is y axis in the negative direction:
					results = loc.getBlockAtDelta( 0, -1, 0 );
					break;
				}

				default:
					break;
			}
		}
		
		return results;
	}
	
	@Override
	public PrisonBlock getPrisonBlock() {
		return this;
	}
	@Override
	public PrisonBlock getBlock() {
		return this;
	}
	
	@Override
	public void setPrisonBlock( PrisonBlock prisonBlock ) {
		
		if ( prisonBlock != null ) {
			
			PrisonBlock cloned = prisonBlock.clone();
			getLocation().setBlockAsync( cloned );
		}
		
	}
	
	@Override
	public void setBlockFace( BlockFace blockFace ) {
		
		Block relativeBlock = getRelative( blockFace );
		
		if ( relativeBlock != null && relativeBlock instanceof PrisonBlock ) {

			(( PrisonBlock ) relativeBlock).setPrisonBlock( this );
			
		}
	}
	
	@Override
	public BlockState getState() {
		return this.clone();
	}
	
	@Override
	public boolean breakNaturally() {
		return false;
	}
	
	
	/**
	 * This function needs to be overriden in the platform implementation.
	 */
	@Override
	public List<ItemStack> getDrops()
	{
		return null;
	}
	
	
	/**
	 * This function needs to be overriden in the platform implementation.
	 */
	@Override
	public List<ItemStack> getDrops( ItemStack tool )
	{
		return null;
	}
	
	
	public ItemStack getItemStack( int blockQuantity ) {
		
		ItemStack results = Prison.get().getPlatform().getItemStack( this, blockQuantity );
		
		return results;
	}
	
	public boolean isLoreAllowed() {
		return loreAllowed;
	}
	public void setLoreAllowed(boolean loreAllowed) {
		this.loreAllowed = loreAllowed;
	}
	
}

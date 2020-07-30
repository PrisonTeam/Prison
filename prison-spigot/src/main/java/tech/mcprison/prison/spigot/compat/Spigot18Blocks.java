package tech.mcprison.prison.spigot.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.util.BlockType;

public abstract class Spigot18Blocks 
	extends CompatibilityCache
	implements Compatibility {


	/**
	 * <p>This function provides a minecraft v1.8 way of getting
	 * the prison BlockType from a bukkit Block.  This function 
	 * should be used for all block types prior to 1.13.x because
	 * of the use of magic numbers.  The variations of types for a 
	 * base material cannot be accessed in any other way. 
	 * For example the item lapis lazuli, which is not a block type,
	 * but it is one of the primary problem item.
	 * <p>
	 * 
	 * <p>For versions 1.13.x and higher, a different function would
	 * need to be used to get the BlockType.
	 * </p>
	 * 
	 * @param spigotBlock
	 * @return
	 */
	@SuppressWarnings( "deprecation" )
	public BlockType getBlockType(Block spigotBlock) {
		BlockType results = null;
		
		if ( spigotBlock != null ) {
			
			int id = spigotBlock.getType().getId();
			short data = spigotBlock.getData();

			results = getCachedBlockType( spigotBlock, (byte) data );
			if ( results == null ) {
		
				// NOTE: namespace is 1.13+
//				Output.get().logInfo( "### getBlockType:  " + spigotBlock.getType().name() + "  " +
//						spigotBlock.getType().getKey().getKey() +  "  " + 
//						spigotBlock.getType().getKey().getNamespace() );

				results = BlockType.getBlock(id, data);
				
				putCachedBlockType( spigotBlock, (byte) data, results );
			}
		}
		
        return results == BlockType.NULL_BLOCK ? null : results;
    }
	
	@SuppressWarnings( "deprecation" )
	public BlockType getBlockType( ItemStack spigotStack ) {
		BlockType results = null;
		
		if ( spigotStack != null ) {
			
			int id = spigotStack.getType().getId();
			short data = spigotStack.getData().getData();
			
			results = getCachedBlockType( spigotStack, (byte) data );
			if ( results == null ) {
				results = BlockType.getBlock(id, data);
				
				putCachedBlockType( spigotStack, (byte) data, results );
			}
		}
		
		return results == BlockType.NULL_BLOCK ? null : results;
	}
	
	@SuppressWarnings( "deprecation" )
	public XMaterial getXMaterial( Block spigotBlock ) {
		XMaterial results = null;
		
		if ( spigotBlock != null ) {
//			int id = spigotBlock.getType().getId();
			short data = spigotBlock.getData();
			
			results = getCachedXMaterial( spigotBlock, (byte) data );
			if ( results == null ) {
								
				String blockName =  spigotBlock.getType().name() +
		    			( data > 0 ? ":" + data : "" );
				
				results =  XMaterial.matchXMaterial( blockName ).orElse( null );
				
				putCachedXMaterial( spigotBlock, (byte) data, results );
			}
			
		}
		
		return results == NULL_TOKEN ? null : results;
	}
	
	public XMaterial getXMaterial( BlockType blockType ) {
		XMaterial results = null;
		
		if ( blockType != null && blockType != BlockType.IGNORE ) {
			short data = blockType.getData();
			
			results = getCachedXMaterial( blockType, (byte) data );
			if ( results == null ) {
				
				results =  XMaterial.matchXMaterial( blockType.getXMaterialNameLegacy() ).orElse( null );
				
				if ( results == null ) {
					results =  XMaterial.matchXMaterial( blockType.name() ).orElse( null );
					
					if ( results == null ) {
						for ( String altName : blockType.getXMaterialAltNames() ) {
							
							results =  XMaterial.matchXMaterial( altName ).orElse( null );
							
							if ( results != null ) {
								break;
							}
						}
					}
					
				}
				
				putCachedXMaterial( blockType, (byte) data, results );
			}

		}
		
		return results == NULL_TOKEN ? null : results;
	}
	
//	public Material getMaterial( BlockType blockType ) {
//		Material results = null;
//		
//		if ( blockType != null && blockType != BlockType.IGNORE ) {
//			short data = blockType.getData();
//			
////			Material.bush
////			
////			Material.matchMaterial( name, legacyName )
////			
////			results = getCachedXMaterial( blockType, (byte) data );
////			if ( results == null ) {
////				
////				results =  XMaterial.matchXMaterial( blockType.getXMaterialNameLegacy() ).orElse( null );
////				
////				if ( results == null ) {
////					for ( String altName : blockType.getXMaterialAltNames() ) {
////						
////						results =  XMaterial.matchXMaterial( altName ).orElse( null );
////						
////						if ( results != null ) {
////							break;
////						}
////					}
////				}
////				
////				putCachedXMaterial( blockType, (byte) data, results );
////			}
//
//		}
//		
//		return results == NULL_TOKEN ? null : results;
//	}


	
	
	
	public void updateSpigotBlock( BlockType blockType, Block spigotBlock ) {
    	
    	if ( blockType != null && blockType != BlockType.IGNORE && spigotBlock != null ) {
    		
    		XMaterial xMat = getXMaterial( blockType );
    		
    		updateSpigotBlock( xMat, spigotBlock );
    	}
    }
	
	@SuppressWarnings( "deprecation" )
	public void updateSpigotBlock( XMaterial xMat, Block spigotBlock ) {
		
		if ( xMat != null ) {
			Material newType = xMat.parseMaterial().orElse( null );
			if ( newType != null ) {
				// No physics update:
				spigotBlock.setType( newType, false );
				
				if ( xMat.getData() != spigotBlock.getState().getRawData() ) {
					spigotBlock.getState().setRawData( xMat.getData() );
					
				}
			}
		}
	}

	
	
	public int getDurabilityMax( ItemStack itemInHand ) {
		return itemInHand.getType().getMaxDurability();
	}
	
	@SuppressWarnings( "deprecation" )
	public int getDurability( ItemStack itemInHand ) {
		return itemInHand.getDurability();
	}
	
	@SuppressWarnings( "deprecation" )
	public void setDurability( ItemStack itemInHand, int newDurability ) {
		itemInHand.setDurability( (short) newDurability );
	}
	
	
}

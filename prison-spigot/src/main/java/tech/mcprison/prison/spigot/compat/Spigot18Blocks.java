package tech.mcprison.prison.spigot.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.output.Output;
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
				
				if ( results == null ) {
					Output.get().logWarn( "Spigot1.8Blocks.getBlockType() : " +
							"Spigot block cannot be mapped to a prison BlockType : " +
							spigotBlock.getType().name() + 
							" id = " + id + " data = " + data +
							"  BlockType = " + ( results == null ? "null" : results.name()));
					
				}
				
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
				
				// First match by BlockType name:
				results =  XMaterial.matchXMaterial( blockType.getXMaterialName() ).orElse( null );
				
				// do not use... redundant with blockType.getXMaterialName():
//				results =  XMaterial.matchXMaterial( blockType.name() ).orElse( null );

				if ( results == null ) {
					
					// Try to match on altNames if they exist:
					for ( String altName : blockType.getXMaterialAltNames() ) {
						
						results =  XMaterial.matchXMaterial( altName ).orElse( null );
						
						if ( results != null ) {
							break;
						}
					}
					
					if ( results == null ) {
						
						// Finally, Try to match on legacy name and magic number:
						results =  XMaterial.matchXMaterial( blockType.getXMaterialNameLegacy() ).orElse( null );
					}
					
					putCachedXMaterial( blockType, (byte) data, results );
				}

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
    		
    		if ( xMat != null ) {
    			
    			updateSpigotBlock( xMat, spigotBlock );
    		}
    	}
    }
	
	@SuppressWarnings( "deprecation" )
	public void updateSpigotBlock( XMaterial xMat, Block spigotBlock ) {
		
		if ( xMat != null ) {
			Material newType = xMat.parseMaterial().orElse( null );
			if ( newType != null ) {
				
				BlockState bState = spigotBlock.getState();
				
				// Set the block state with the new type and rawData:
				bState.setType( newType );;
				bState.setRawData( xMat.getData() );
				
				// Force the update but don't apply the physics:
				bState.update( true, false );
				
			}
		}
	}

	

	/**
	 * <p>This function is supposed to find all possible blocks available
	 * on the server.  The number of available items, and blocks, will vary based
	 * upon different version the server is running.
	 * </p>
	 * 
	 * <p>This function is not simple for 1.8 mode of block types.  The primary
	 * reason for this is that there is a list of materials that is available, 
	 * but depending upon what magic numbers (data) you set on the ItemStacks, 
	 * you will be different blocks or items.  Like the difference between oak
	 * logs and birch logs.
	 * </p>
	 * 
	 * <p>One problem is that the data can be set to any value and it 
	 * will be valid. It may not render in game, but we cannot test for that.
	 * So in turn, we cannot use the magic numbers to check to see if they
	 * are actually valid.  As a result, all we can do is use the Materials
	 * directly, and as a result, we will miss out of the variations that
	 * may otherwise exist.  For example with minecraft:log and minecraft:log2
	 * where we can only count two for those two materials, but they actually
	 * have about 6 to 8 declared types, or more (I've seen naturally spawned
	 * log with a value of 7 and 8 which does not exist in the documentation).
	 * </p>
	 * 
	 * <p>As a result of preventing arbitrarily high false reporting of 
	 * block types, all magic number related code is commented out.
	 * </p>
	 * 
	 * @return
	 */
	public BlockTestStats testCountAllBlockTypes() {
		BlockTestStats stats = new BlockTestStats();
		
		stats.setMaterialSize( Material.values().length );
		
		// go through all available materials:
		for ( Material mat : Material.values() ) {
			
			// Need to try all data values from 0 through 20:
//			for ( short data = 0; data < (short) 20; data++ ) {
				
				// Must create an item stack:
				ItemStack iStack = new ItemStack( mat, 1 );
//				ItemStack iStack = new ItemStack( mat, 1, data );
				
				if ( iStack != null ) {
					
//					stats.addMaxData( data );
					
					if ( mat.isBlock() ) {
						stats.addCountBlocks();
					}
//					if ( mat.isItem() ) // isItem() is not a function in v1.8.8
					else {
						stats.addCountItems();
					}
				} 
//			}
		}
		
		return stats;
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

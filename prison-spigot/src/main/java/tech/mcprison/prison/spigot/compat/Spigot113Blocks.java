package tech.mcprison.prison.spigot.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.util.BlockType;

public abstract class Spigot113Blocks 
	extends CompatibilityCache 
	implements Compatibility {

	public BlockType getBlockType(Block spigotBlock) {
		BlockType results = getCachedBlockType( spigotBlock, NO_DATA_VALUE );
		
		if ( results == null ) {
			if ( spigotBlock != null ) {
				
				results = BlockType.getBlock( spigotBlock.getType().name() );
				
				putCachedBlockType( spigotBlock, NO_DATA_VALUE, results );
			}
		}
		
        return results == BlockType.NULL_BLOCK ? null : results;
    }
	
	public BlockType getBlockType(ItemStack spigotStack) {
		BlockType results = getCachedBlockType( spigotStack, NO_DATA_VALUE );
		
		if ( results == null ) {
			if ( spigotStack != null ) {
				
				results = BlockType.getBlock( spigotStack.getType().name() );
				
				putCachedBlockType( spigotStack, NO_DATA_VALUE, results );
			}
		}
		
		return results == BlockType.NULL_BLOCK ? null : results;
	}
	
	public XMaterial getXMaterial( Block spigotBlock ) {
		XMaterial results = getCachedXMaterial( spigotBlock, NO_DATA_VALUE );
		
		if ( results == null ) {
			if ( spigotBlock != null ) {
				results =  XMaterial.matchXMaterial( spigotBlock.getType().name() ).orElse( null );
				
				putCachedXMaterial( spigotBlock, NO_DATA_VALUE, results );
			}
		}
		
		return results == NULL_TOKEN ? null : results;
	}
	
	/**
	 * <p>This function tries to use up to three different sources to get a match
	 * on the XMaterial.  Just because the XMateral may be a match, does not 
	 * mean it actually is a valid Block for that version of spigot.  
	 * </p>
	 * 
	 * @param blockType
	 * @return
	 */
	public XMaterial getXMaterial( BlockType blockType ) {
		XMaterial results = getCachedXMaterial( blockType, NO_DATA_VALUE );
		
		if ( results == null ) {
			if ( blockType != null && blockType != BlockType.IGNORE ) {
				
				results =  XMaterial.matchXMaterial( blockType.getXMaterialName() ).orElse( null );
				
				if ( results == null ) {
					results =  XMaterial.matchXMaterial( blockType.getXMaterialNameLegacy() ).orElse( null );
					
				}
				
				if ( results == null ) {
					
					for ( String altName : blockType.getXMaterialAltNames() ) {
						results =  XMaterial.matchXMaterial( altName ).orElse( null );
						
						if ( results != null ) {
							break;
						}
					}
				}
				
				putCachedXMaterial( blockType, NO_DATA_VALUE, results );
			}

		}
		
		return results == NULL_TOKEN ? null : results;
	}

	
	public void updateSpigotBlock( BlockType blockType, Block spigotBlock ) {
    	
    	if ( blockType != null && blockType != BlockType.IGNORE && spigotBlock != null ) {
    		
    		XMaterial xMat = getXMaterial( blockType );
    		
    		updateSpigotBlock( xMat, spigotBlock );
    	}
    }
	
	public void updateSpigotBlock( XMaterial xMat, Block spigotBlock ) {
		
		if ( xMat != null ) {
			Material newType = xMat.parseMaterial();
			if ( newType != null ) {
				// No physics update:
				spigotBlock.setType( newType, false );
				
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
	 * @return
	 */
	public BlockTestStats testCountAllBlockTypes() {
		BlockTestStats stats = new BlockTestStats();
		
		stats.setMaterialSize( Material.values().length );
		
		// go through all available materials:
		for ( Material mat : Material.values() ) {
			
			// Must create an item stack:
			ItemStack iStack = new ItemStack( mat, 1 );
			
			if ( iStack != null ) {
				
				if ( mat.isBlock() ) {
					stats.addCountBlocks();
				}
				else if ( mat.isItem() ) {
					stats.addCountItems();
				}
			} 
		}
		
		return stats;
	}
	
	
	public int getDurabilityMax( ItemStack itemInHand ) {
		return itemInHand.getType().getMaxDurability();
	}
	
	public int getDurability( ItemStack itemInHand ) {
		
		Damageable damage = (Damageable) itemInHand.getItemMeta();
		return damage.getDamage();
	}
	
	public void setDurability( ItemStack itemInHand, int newDamage ) {
		
		Damageable damage = (Damageable) itemInHand.getItemMeta();
		damage.setDamage( newDamage );
	}
}

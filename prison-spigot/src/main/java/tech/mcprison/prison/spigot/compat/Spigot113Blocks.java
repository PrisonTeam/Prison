package tech.mcprison.prison.spigot.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes.InternalBlockTypes;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;

public abstract class Spigot113Blocks 
	extends Spigot19Player 
	implements CompatibilityBlocks {

	@Override
	public BlockType getBlockType(Block spigotBlock) {
		BlockType results = getCachedBlockType( spigotBlock, NO_DATA_VALUE );
		
		if ( results == null ) {
			if ( spigotBlock != null ) {
			
				results = BlockType.getBlock( spigotBlock.getType().name() );

//				if ( results == null ) {
//					Output.get().logInfo( "#### 1.13 getBlockType() Cannot map block from spigot to prison:" +
//							"  spigotBlock.getType().name() = %s " +
//							"  BlockType.getBlock() = %s ",
//							spigotBlock.getType().name(),
//							(results == null ? "" : results.name() ));
//				}
				
				putCachedBlockType( spigotBlock, NO_DATA_VALUE, results );
			}
		}
		
        return results == BlockType.NULL_BLOCK ? null : results;
    }
	
	@Override
	public PrisonBlock getPrisonBlock(Block spigotBlock) {
		PrisonBlock pBlock = null;
		
		XMaterial xMat = getXMaterial( spigotBlock );
		
		if ( xMat != null ) {
			pBlock = new PrisonBlock( xMat.name() );
		}
		// ignore nulls because errors were logged in getXMaterial() so they only
		// are logged once
		
		return pBlock;
	}
	
	@Override
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
	
	@Override
	public XMaterial getXMaterial( Block spigotBlock ) {
		XMaterial results = NULL_TOKEN;

		if ( spigotBlock != null ) {
			results = getCachedXMaterial( spigotBlock, NO_DATA_VALUE );
			
			if ( results == null ) {
				results = XMaterial.matchXMaterial( spigotBlock.getType() );
				
				if ( results == null ) {
					results = XMaterial.matchXMaterial( spigotBlock.getType().name() ).orElse( null );
				}
				
				if ( results == null ) {
					
					Output.get().logWarn( "Spigot113Blocks.getXMaterial() : " +
							"Spigot block cannot be mapped to a XMaterial : " +
							spigotBlock.getType().name() + 
							"  SpigotBlock = " + ( spigotBlock == null ? "null" : 
								spigotBlock.getType().name()));
				}

				
				if ( results != null ) {
					
					putCachedXMaterial( spigotBlock, NO_DATA_VALUE, results );
				}
			}
		}
		
		
		return results == NULL_TOKEN ? null : results;
	}
	
	
	@Override
	public XMaterial getXMaterial( PrisonBlock prisonBlock ) {
		XMaterial results = NULL_TOKEN;
		
		if ( prisonBlock != null ) {
			
			results = getCachedXMaterial( prisonBlock );
			if ( results == null ) {
				
				String blockName = prisonBlock.getBlockName();
				
				results = XMaterial.matchXMaterial( blockName ).orElse( null );
				
				putCachedXMaterial( prisonBlock, results );
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
	@Override
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

	
	@Override
	public void updateSpigotBlock( BlockType blockType, Block spigotBlock ) {
    	
    	if ( blockType != null && blockType != BlockType.IGNORE && spigotBlock != null ) {
    		
    		XMaterial xMat = getXMaterial( blockType );
    		
    		updateSpigotBlock( xMat, spigotBlock );
    	}
    }
	
	
	@Override
	public void updateSpigotBlock( PrisonBlock prisonBlock, Block spigotBlock ) {
		
		if ( prisonBlock != null && 
				!prisonBlock.getBlockName().equalsIgnoreCase( InternalBlockTypes.IGNORE.name() ) && 
				spigotBlock != null ) {
			
			XMaterial xMat = getXMaterial( prisonBlock );
			
			if ( xMat != null ) {
				
				updateSpigotBlock( xMat, spigotBlock );
			}
		}
	}
	
	
	@Override
	public void updateSpigotBlock( XMaterial xMat, Block spigotBlock ) {
		
		if ( xMat != null ) {
			Material newType = xMat.parseMaterial();
			if ( newType != null ) {
				// No physics update:
				spigotBlock.setType( newType, false );
				
			}
		}
	}

	
//	@Override
//	public void updateSpigotBlockAsync( BlockType blockType, Block spigotBlock ) {
//		
//		if ( blockType != null && blockType != BlockType.IGNORE && spigotBlock != null ) {
//			
//			XMaterial xMat = getXMaterial( blockType );
//			
//			updateSpigotBlockAsync( xMat, spigotBlock );
//		}
//	}
//	
//	
//	@Override
//	public void updateSpigotBlockAsync( PrisonBlock prisonBlock, Block spigotBlock ) {
//		
//		if ( prisonBlock != null && 
//				!prisonBlock.getBlockName().equalsIgnoreCase( InternalBlockTypes.IGNORE.name() ) && 
//				spigotBlock != null ) {
//			
//			XMaterial xMat = getXMaterial( prisonBlock );
//			
//			if ( xMat != null ) {
//				
//				updateSpigotBlockAsync( xMat, spigotBlock );
//			}
//		}
//	}
//	
//	
//	@Override
//	public void updateSpigotBlockAsync( XMaterial xMat, Block spigotBlock ) {
//		
//		if ( xMat != null ) {
//			Material newType = xMat.parseMaterial();
//			if ( newType != null ) {
//				
//				new BukkitRunnable() {
//					@Override
//					public void run() {
//
//						// No physics update:
//						spigotBlock.setType( newType, false );
//					}
//				}.runTaskLater( getPlugin(), 0 );
//				
//			}
//		}
//	}
	
	/**
	 * <p>This function both get's the block and then updates it within
	 * the same runnable transaction.  This should eliminate the need to 
	 * risk reading the block in an async thread, and improve performance.
	 * </p>
	 * 
	 * @param prisonBlock
	 * @param location
	 */
	@Override
	public void updateSpigotBlockAsync( PrisonBlock prisonBlock, Location location ) {
		
		XMaterial xMat = getXMaterial( prisonBlock );
		
		if ( xMat != null && location != null ) { 
			Material newType = xMat.parseMaterial();
			if ( newType != null ) {
				
				new BukkitRunnable() {
					@Override
					public void run() {
						
						// No physics update:
						Block spigotBlock = ((SpigotBlock) location.getBlockAt()).getWrapper();
						
						// For 1.13.x and higher:
						spigotBlock.setType( newType, false );
						
					}
				}.runTaskLater( getPlugin(), 0 );
				
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
	@Override
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
	
	
	@Override
	public int getDurabilityMax( SpigotItemStack itemInHand ) {
		return itemInHand.getBukkitStack().getType().getMaxDurability();
	}
	

	@Override
	public boolean hasDurability( SpigotItemStack itemInHand )
	{
		boolean results = false;
		
		if ( itemInHand != null && itemInHand.getBukkitStack().hasItemMeta() &&
				itemInHand.getBukkitStack().getItemMeta() instanceof Damageable ) {
			
			Damageable item = (Damageable) itemInHand.getBukkitStack().getItemMeta();
			results = item.hasDamage();
		}
			
		return results;	
	}

	@Override
	public int getDurability( SpigotItemStack itemInHand )
	{
		int results = 0;
		
		if ( itemInHand != null && itemInHand.getBukkitStack().hasItemMeta() &&
						itemInHand.getBukkitStack().getItemMeta() instanceof Damageable ) {
			Damageable item = (Damageable) itemInHand.getBukkitStack().getItemMeta();
			results = item.getDamage();
		}
		
		return results;
	}

	@Override
	public boolean setDurability( SpigotItemStack itemInHand, int newDamage ) {
		boolean results = false;
		if ( itemInHand != null && itemInHand.getBukkitStack().getItemMeta() != null &&
						itemInHand.getBukkitStack().getItemMeta() instanceof Damageable ) {
			Damageable item = (Damageable) itemInHand.getBukkitStack().getItemMeta();
			
			item.setDamage( newDamage );
			results = itemInHand.getBukkitStack().setItemMeta((ItemMeta) item);
		}
		return results;
	}
	
//	public int getDurability( SpigotItemStack itemInHand ) {
//		
//		Damageable damage = (Damageable) itemInHand.getBukkitStack().getItemMeta();
//		return damage.getDamage();
//	}
//	
//	public void setDurability( SpigotItemStack itemInHand, int newDamage ) {
//		
//		Damageable damage = (Damageable) itemInHand.getBukkitStack().getItemMeta();
//		damage.setDamage( newDamage );
//	}
	
	/**
	 * This is called setBlockFace, but it is really intended for use with ladders. 
	 * The block face is the face in which to place the ladder.  So when 
	 * BlockFace.NORTH is specified, it needs to set the 
	 * org.bukkit.block.BlockFace.SOUTH.  Not sure why it has to be the
	 * opposite, which is unlike v1.8.8?
	 */
	@Override
	public void setBlockFace( Block spigotBlock, BlockFace blockFace ) {
		
		
		org.bukkit.block.BlockFace spigotBlockFace = null;
		
		switch ( blockFace )
		{
			case TOP:
				spigotBlockFace = org.bukkit.block.BlockFace.UP;
				break;
			case BOTTOM:
				spigotBlockFace = org.bukkit.block.BlockFace.DOWN;
				break;
			case NORTH:
				spigotBlockFace = org.bukkit.block.BlockFace.SOUTH;
				break;
			case EAST:
				spigotBlockFace = org.bukkit.block.BlockFace.WEST;
				break;
			case SOUTH:
				spigotBlockFace = org.bukkit.block.BlockFace.NORTH;
				break;
			case WEST:
				spigotBlockFace = org.bukkit.block.BlockFace.EAST;
				break;

			default:
				break;
		}
		
		if ( spigotBlockFace != null ) {
			
			BlockState state = spigotBlock.getState();
			
			if ( state.getBlockData() instanceof Directional ) {
				
				Directional bukkitDirectional = (Directional) state.getBlockData();
				bukkitDirectional.setFacing( spigotBlockFace );
				state.setBlockData(bukkitDirectional);
				state.update( true, false );
			}
			
		}
	}

    @Override
    public ItemStack getLapisItemStack() {
    	return XMaterial.LAPIS_LAZULI.parseItem();
//        if (XMaterial.LAPIS_LAZULI.parseItem() != null) {
//            return new ItemStack(XMaterial.LAPIS_LAZULI.parseItem());
//        }
//        return null;
    }
}

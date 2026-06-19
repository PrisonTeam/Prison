package tech.mcprison.prison.spigot.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.util.Location;

public abstract class Spigot_1_8_Blocks 
	extends Spigot_1_8_Player 
	implements CompatibilityBlocks {

	
	@Override
	public SpigotBlock getSpigotBlock( Block bukkitBlock ) {
		return SpigotBlock.getSpigotBlock( bukkitBlock );
	}
	

	
	/**
	 * <p>This will take a given block and return the XMaterial object for it.
	 * </p>
	 * 
	 * <p>But please keep in mind that just because the block is in a mine, that the
	 * blocks may not belong in the mine.  Matter of fact, this may be the situation
	 * when creating a new mine in a new world, or moving a mine.  I mention this 
	 * fact, because even though the list of blocks for a mine may be very constrained
	 * ( 'mines block list f') that it may actually contain blocks outside of that
	 * list.  This actually becomes a problem when older versions of XSeries does
	 * not support that block (ie... updated to new spigot or paper version) and 
	 * therefore XSeries cannot match to one of it's block types.
	 * This may not be a problem, because the mine may not have been reset yet.
	 * Resetting the mine will purge those unknown block types.
	 * Or at least, we can just ignore them since they are not blocks we are 
	 * trying to track.
	 * </p>
	 * 
	 */
	@SuppressWarnings( "deprecation" )
	@Override
	public XMaterial getXMaterial( Block spigotBlock ) {
		XMaterial results = NULL_TOKEN;
		
		if ( spigotBlock != null ) {
			byte data = spigotBlock.getData();
			
			results = getCachedXMaterial( spigotBlock, data );
			if ( results == null ) {
				
				String blockName = spigotBlock.getType().name() + ":" + data;
				results = XMaterial.matchXMaterial( blockName ).orElse( null );
				
				
				if ( results == null ) {
					// Last chance: try to match by id:
					// WARNING: This will not work with "modern material".  So if this 
					//          throws an exception when trying to get the ID, then just 
					//          ignore it since results will be null and it will try to 
					//          match by MaterialType and then by name.
					try {
						int id = spigotBlock.getType().getId();
						
						results = matchXMaterial( id, data );
					} 
					catch (Exception e) {
						// Ignore this exception and allow it to find a match by
						// other means...
					}
				}
				
				if ( results == null ) {
					try {
						results = XMaterial.matchXMaterial(spigotBlock.getType());
					} 
					catch (Exception e) {
						// Ignore and let next test try it...
					}
				}
				
				if ( results == null ) {
					try {
						results = XMaterial.matchXMaterial( spigotBlock.getType().name() ).orElse( null );
					} 
					catch (Exception e) {
						// Ignore and let the next test try it...
					}
				}
				
				if ( results == null ) {
					// Last chance, try to match without data. If it matches, then
					// the cache will be setup with a data appended so as to bypass all this
					// extra checks:
					results = getCachedXMaterial( spigotBlock, (byte) 0 );
				}
				
				putCachedXMaterial( spigotBlock, data, results );
				
				if ( results == null ) {
					Output.get().logWarn( "Spigot18Blocks.getXMaterial() : " +
							"Spigot block cannot be mapped to a XMaterial : " +
							spigotBlock.getType().name() + 
							"  SpigotBlock = " + ( spigotBlock == null ? "null" : 
								spigotBlock.getType().name()) +
							" data = " + data );
				}
			}
		}
		
		return results == NULL_TOKEN ? null : results;
	}
	
	
	/**
	 * This function was removed from XMaterial as of XSeries v11.0.0.
	 * 
	 * This behaves the same, so spigot 1.8 can still be used with 
	 * XSeries v11.1.0+.
	 * 
     * Gets the XMaterial based on the material's ID (Magic Value) and data value.<br>
     * You should avoid using this for performance issues.
     * 
     * Since prison uses a material cache in this function, this will only get the 
     * XMaterial the first time the item is requested.  So performance is bad, but
     * it's a one time hit.
	 *
	 * Warning: this method loops through all the available materials and matches their 
	 * ID using {@link #getId()} which takes a really long time.
     *
     * @param id   the ID (Magic value) of the material.
     * @param data the data value of the material.
     * @return a parsed XMaterial with the same ID and data value.
     * @see #matchXMaterial(ItemStack)
     */
    public static XMaterial matchXMaterial(int id, byte data) {
	    	XMaterial results = null;
	    	
	    	// NOTE: XMaterial.MAX_ID == 2267.
	//        if (id < 0 || id > 2267 || data < 0) return null;
	
	    	if (id >= 0 && id <= 2267 && data >= 0) {
	    		
	    		for (XMaterial materials : XMaterial.VALUES) {
	    			if (materials.getData() == data && materials.getId() == id) {
	    				
	    				results = materials;
	    				break;
	    			}
	    		}
	    	}
        
        return results;
    }
    
    
	@Override
	public XMaterial getXMaterial( PrisonBlock prisonBlock ) {
		XMaterial results = NULL_TOKEN;
		
		if ( prisonBlock != null ) {
			
			results = getCachedXMaterial( prisonBlock );
			if ( results == null ) {
				
				String blockName = prisonBlock.getBlockName();
				
				results = XMaterial.matchXMaterial( blockName ).orElse( null );
				
				// Convert items to their block representation:
				if ( results == XMaterial.MELON_SLICE && 
						prisonBlock.getBlockName().equalsIgnoreCase( "melon" ) ) {
					results = XMaterial.MELON;
				}
				else if ( results == XMaterial.BRICK && 
						prisonBlock.getBlockName().equalsIgnoreCase( "bricks" ) ) {
					results = XMaterial.BRICKS;
				}
				else if ( results == XMaterial.BRICK && 
						prisonBlock.getBlockName().equalsIgnoreCase( "brick" ) ) {
					results = XMaterial.BRICKS;
				}
				
				putCachedXMaterial( prisonBlock, results );
			}
		}
		
		return results == NULL_TOKEN ? null : results;
	}
	
	
	
	
	@Override
	public void updateSpigotBlock( PrisonBlock prisonBlock, Block spigotBlock ) {
		
		if ( prisonBlock != null && 
				!prisonBlock.equals( PrisonBlock.IGNORE ) && 
				spigotBlock != null ) {
			
			XMaterial xMat = getXMaterial( prisonBlock );
			
			if ( xMat != null ) {
				
				updateSpigotBlock( xMat, spigotBlock );
			}
		}
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	public void updateSpigotBlock( XMaterial xMat, Block spigotBlock ) {
		
		if ( xMat != null ) {
			
			Material newType = xMat.parseMaterial();
			
			if ( newType != null ) {
				
				BlockState bState = spigotBlock.getState();
				
				// Set the block state with the new type and rawData:
				bState.setType( newType );
				bState.setRawData( xMat.getData() );
				
				// Force the update but don't apply the physics:
				bState.update( true, false );
				
			}
		}
	}

	
	
	
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
					@SuppressWarnings( "deprecation" )
					@Override
					public void run() {
						
						// Using the World's location to get the block, will ensure that 
						// the wrapper contains the bukkit's block:
						SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
						
						
						Block bBlock = sBlock.getWrapper();
						
						// For 1.13.x and higher:
						// spigotblock.setType( newType, false );

						
						// No physics update:
						BlockState bState = bBlock.getState();
						 
						// Set the block state with the new type and rawData:
						bState.setType( newType );
						bState.setRawData( xMat.getData() );
						 
						// Force the update but don't apply the physics:
						bState.update( true, false );
						
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
	@Override
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
	
	
	@Override
	public int getDurabilityMax( SpigotItemStack itemInHand ) {
		int results = 0;
		
		if ( itemInHand != null && itemInHand.getBukkitStack() != null ) {
			results =  itemInHand.getBukkitStack().getType().getMaxDurability();
		}
		return results;
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	public boolean hasDurability( SpigotItemStack itemStack ) {
		boolean results = false;
		
		if ( itemStack != null && itemStack.getBukkitStack() != null ) {
			results = itemStack.getBukkitStack().getDurability() > 0;
		}
		
		return results;	
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public int getDurability( SpigotItemStack itemStack ) {
		int results = 0;
		
		if ( itemStack != null && itemStack.getBukkitStack() != null ) {
			results = itemStack.getBukkitStack().getDurability();
		}
		
		return results;
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public boolean setDurability( SpigotItemStack itemStack, int damage ) {
		boolean results = false;
		if ( itemStack != null && itemStack.getBukkitStack() != null ) {
			itemStack.getBukkitStack().setDurability( (short) damage );
			results = true;
		}
		return results;
	}
	
	
	@Override
	public void setBlockFace( Block spigotBlock, BlockFace blockFace ) {
		
		
		if ( spigotBlock.getType() == Material.LADDER ) {
			
			
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
					spigotBlockFace = org.bukkit.block.BlockFace.NORTH;
					break;
				case EAST:
					spigotBlockFace = org.bukkit.block.BlockFace.EAST;
					break;
				case SOUTH:
					spigotBlockFace = org.bukkit.block.BlockFace.SOUTH;
					break;
				case WEST:
					spigotBlockFace = org.bukkit.block.BlockFace.WEST;
					break;
						
				default:
					break;
			}
			
			if ( spigotBlockFace != null ) {
				
				BlockState state = spigotBlock.getState();
				
				org.bukkit.material.Ladder ladder = (org.bukkit.material.Ladder) state.getData();
				
				ladder.setFacingDirection( spigotBlockFace );
				
				state.setData( ladder );
				
				// turn off physics so the ladders will "stick" to glowstone and other blocks.
				state.update( true, false );
				
			}
		}
		
	}
	
    //@SuppressWarnings("deprecation")
    @Override
    public ItemStack getLapisItemStack() {
    	
    		return XMaterial.LAPIS_LAZULI.parseItem();
    }
 
    
    
    @Override
    public int getMinY() {
    		return 0;
    }
    
    @Override
    public int getMaxY() {
    		return 255;
    }
    
    
    /**
     * Not compatible with Spigot 1.8 through 1.13 so return a value of 0.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
    @Override
    public int getCustomModelData( SpigotItemStack itemStack ) {
    		return 0;
    }
    /**
     * Not compatible with Spigot 1.8 through 1.13 so return a value of 0.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
    @Override
    public int getCustomModelData( ItemStack itemStack ) {
    		return 0;
    }
    
    /**
     * Not compatible with Spigot 1.8 through 1.13 so do nothing.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
    @Override
	public void setCustomModelData( SpigotItemStack itemStack, int customModelData ) {
		if ( itemStack != null ) {
			setCustomModelData( itemStack.getBukkitStack(), customModelData);
		}
	}
	/**
	 * Not compatible with Spigot 1.8 through 1.13 so do nothing.
	 * Only available with 1.14 and higher.
	 * @param itemStack
	 * @return
	 */
    @Override
	public void setCustomModelData( ItemStack itemStack, int customModelData ) {
		
	}


    
	/**
	 * <p>With spigot 1.14 and newer, there is a function on a block that
	 * identifies if a block is passable.  The description in the api docs are:
	 * </p>
	 * 
	 * <pre>
	 * Checks if this block is passable.

A block is passable if it has no colliding parts that would prevent 
players from moving through it.

Examples: Tall grass, flowers, signs, etc. are passable, but open doors, 
fence gates, trap doors, etc. are not because they still have parts that 
can be collided with.
	 * </p>
	 * 
	 * <p>For 1.8 through 1.13.x, just check the material type for 
	 * some of the possibilities like what is listed.  This does not
	 * need to be a complete list, but it can also be expanded as 
	 * needed based upon feedback too. Some items really don't matter,
	 * such as crops or short grass, since it's only about one block
	 * from the ground, so the item, if thrown, will land near that 
	 * area.
	 * </p>
	 * 
	 * @param bBlock
	 * @return
	 */
    @Override
	public boolean isPassable( Block bBlock ) {
	    	boolean results = false;
	    	
	    	if ( bBlock != null ) {
	    		
	    		XMaterial xMat = getXMaterial( bBlock );
	    		
	    		if ( xMat != null ) {
	    		
				boolean isSign = xMat.name().toLowerCase().contains("sign");
				
				if ( isSign || XBlock.isCrop(xMat) ) {
					results = true;
				}
				
				else {
					
					
					switch ( xMat ) {
					case AIR:

					case SHORT_GRASS:
					case TALL_GRASS:
					case SEAGRASS:
					case TALL_SEAGRASS:
						
					case VINE:	
					case CAVE_VINES_PLANT:	
					case TWISTING_VINES_PLANT:
					case WEEPING_VINES_PLANT:
						
						results = true;
						break;
						
					default:
						break;
					}
				}
				
				
			}
	    	}
	    	
	    	return results;
    }
    
}

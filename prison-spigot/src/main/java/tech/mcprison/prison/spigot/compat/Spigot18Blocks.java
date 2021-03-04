package tech.mcprison.prison.spigot.compat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.util.BlockType;

public abstract class Spigot18Blocks 
	extends CompatibilityCache
	implements CompatibilityBlocks {


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
		BlockType results = BlockType.NULL_BLOCK;
		
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
					
					results = BlockType.getBlock( spigotBlock.getType().name() );
					
					if ( results == null ) {
						
						Output.get().logWarn( "Spigot18Blocks.getBlockType() : " +
								"Spigot block cannot be mapped to a prison BlockType : " +
								spigotBlock.getType().name() + 
								" id = " + id + " data = " + data +
								"  BlockType = " + ( results == null ? "null" : results.name()));
						
					}
				}
				
				putCachedBlockType( spigotBlock, (byte) data, results );
			}
		}
		
        return results == BlockType.NULL_BLOCK ? null : results;
    }
	
	
	public PrisonBlock getPrisonBlock(Block spigotBlock) {
		PrisonBlock pBlock = null;
		
		XMaterial xMat = getXMaterial( spigotBlock );
		
		if ( xMat != null ) {
			pBlock = SpigotPrison.getInstance().getPrisonBlockTypes().getBlockTypesByName( xMat.name() );
//			pBlock = new PrisonBlock( xMat.name() );
		}
		// ignore nulls because errors were logged in getXMaterial() so they only
		// are logged once
		
		return pBlock;
	}
	
	
	@SuppressWarnings( "deprecation" )
	public BlockType getBlockType( ItemStack spigotStack ) {
		BlockType results = BlockType.NULL_BLOCK;
		
		if ( spigotStack != null ) {
			
			int id = spigotStack.getType().getId();
			short data = spigotStack.getData().getData();
			
			results = getCachedBlockType( spigotStack, (byte) data );
			if ( results == null ) {
				
				results = BlockType.getBlock(id, data);
				
				if ( results == null ) {
					
					// NOTE: Some items may have invalid data values.  Example are with pickaxes
					//       should have a value of zero, but could range from +- 256.
					// Try to use XMaterial to map back to a BlockType (old block model).
					XMaterial xMat = xMatMatchXMaterial( spigotStack );
					
					if ( xMat != null ) {
						results = BlockType.getBlock( xMat.name() );
					}
					
					if ( results == null ) {
						
						String message = String.format( "Spigot18Blocks: getBlockType(): " +
								"Unable to map to a BlockType. XMaterial = %s :: %s %s " +
								"Material = %s ", 
								(xMat == null ? "null" : xMat.name()), 
								Integer.toString( id ), Integer.toString( data ), 
								spigotStack.getType().name() );
						
						Output.get().logInfo( message );
					}
				}
				
				putCachedBlockType( spigotStack, (byte) data, results );
			}
		}
		
		return results == BlockType.NULL_BLOCK ? null : results;
	}
	
	/**
	 * <p>Something is causing XMaterial to throw an exception that makes no sense
	 * since the item listed does not exist in game.
	 * </p>
	 * 
	 *  <pre>
	 *  Caused by: java.lang.IllegalArgumentException: Unsupported material from item: BED (14)
	at tech.mcprison.prison.cryptomorin.xseries.XMaterial.lambda$matchXMaterial$1(XMaterial.java:1559) ~[?:?]
	at tech.mcprison.prison.cryptomorin.xseries.XMaterial$$Lambda$197/0x0000000069039ff0.get(Unknown Source) ~[?:?]
	at java.util.Optional.orElseThrow(Optional.java:290) ~[?:1.8.0_272]
	at tech.mcprison.prison.cryptomorin.xseries.XMaterial.matchXMaterial(XMaterial.java:1559) ~[?:?]
	at tech.mcprison.prison.spigot.compat.Spigot18Blocks.getBlockType(Spigot18Blocks.java:116) ~[?:?]
	at tech.mcprison.prison.spigot.block.SpigotItemStack.<init>(SpigotItemStack.java:45) ~[?:?]
	at tech.mcprison.prison.spigot.SpigotUtil.bukkitItemStackToPrison(SpigotUtil.java:583) ~[?:?]
	at tech.mcprison.prison.spigot.SpigotListener.onPlayerInteract(SpigotListener.java:172) ~[?:?]
	at sun.reflect.GeneratedMethodAccessor64.invoke(Unknown Source) ~[?:?]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_272]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_272]
	at org.bukkit.plugin.java.JavaPluginLoader$1.execute(JavaPluginLoader.java:302) ~[spigot-1.12.2.jar:git-Spigot-eb3d921-2b93d83]
	... 17 more
	 *  </pre>
	 *  
	 *  
	 * NOTE: Some items may have invalid data values.  Example are with pickaxes 
	 *     should have a value of zero, but could range from +- 256. 
	 *     Try to use XMaterial to map back to a BlockType (old block model).
	 *     
	 * @param spigotStack
	 * @return
	 */
	@SuppressWarnings( "deprecation" )
	private XMaterial xMatMatchXMaterial( ItemStack spigotStack ) {
		XMaterial xMat = null;
		
		if ( spigotStack != null ) {
			
			try {
				xMat = XMaterial.matchXMaterial( spigotStack );
			}
			catch ( Exception e ) {
				
				int id = spigotStack.getType().getId();
				short data = spigotStack.getData().getData();
				
				// Invalid type from the stack:
				Output.get().logDebug( "Spigot188Blocks: unable to matchXMaterial.  " +
						"Type=%s  Qty=%s  id=%s  data=%s  Error=[%s]",
						spigotStack.getType().name(), 
						Integer.toString( spigotStack.getAmount() ),
						Integer.toString( id ),
						Integer.toString( data ),
						e.getMessage() );
			}
		}
		
		return xMat;
	}
	
	@SuppressWarnings( "deprecation" )
	public XMaterial getXMaterial( Block spigotBlock ) {
		XMaterial results = NULL_TOKEN;
		
		if ( spigotBlock != null ) {
			byte data = spigotBlock.getData();
			
			results = getCachedXMaterial( spigotBlock, data );
			if ( results == null ) {
				
				String blockName = spigotBlock.getType().name() + ":" + data;
				results = XMaterial.matchXMaterial( blockName ).orElse( null );
				
//				if ( results == null ) {
//					
//					Output.get().logInfo( "####  Spigot18Blocks.getXMaterial(Block) : %s => %s ",
//							blockName, (results == null ? "null" : results.name() ));
//				}
						
				
				if ( results == null ) {
					// Last chance: try to match by id:
					int id = spigotBlock.getType().getId();
					results = XMaterial.matchXMaterial( id, data ).orElse( null );
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
	


	public XMaterial getXMaterial( BlockType blockType ) {
		XMaterial results = NULL_TOKEN;
		
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
	
//	@SuppressWarnings( "deprecation" )
//	public int getDurability( SpigotItemStack itemInHand ) {
//		return itemInHand.getBukkitStack().getDurability();
//	}
//	
//	@SuppressWarnings( "deprecation" )
//	public void setDurability( SpigotItemStack itemInHand, int newDurability ) {
//		itemInHand.getBukkitStack().setDurability( (short) newDurability );
//	}
	
	

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
}

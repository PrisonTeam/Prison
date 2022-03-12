/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.BlockState;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.customblock.CustomItems;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotBlock
	extends PrisonBlock
//	implements Block 
{

    private org.bukkit.block.Block bBlock;

    /**
     * If this block was identified as being within a specific mine, then 
     * keep track of the mine's PrisonBlockTypes to ensure the
     * correct custom block integration is able to identify it if it
     * is a custom block type.
     */
    private transient Set<PrisonBlockType> prisonBlockTypes;
    
    private SpigotBlock( String blockName, org.bukkit.block.Block bBlock ) {
    	super( blockName );
    	
    	this.bBlock = bBlock;
    	
    	this.prisonBlockTypes = new HashSet<>();
    	
    }

    private SpigotBlock( PrisonBlockType blockType, String blockName, org.bukkit.block.Block bBlock ) {
    	super( blockType, blockName );
    	
    	this.bBlock = bBlock;
    	
    	this.prisonBlockTypes = new HashSet<>();
    	
    }

    public SpigotBlock( org.bukkit.block.Block bBlock, PrisonBlock targetBlockType ) {
    	this( targetBlockType.getBlockName(), bBlock );
	}

	public static SpigotBlock getSpigotBlock( org.bukkit.block.Block bukkitBlock) {
    	SpigotBlock sBlock = null;
    	
    	if (bukkitBlock != null ) {
    		
    		XMaterial xMat = SpigotCompatibility.getInstance().getXMaterial( bukkitBlock );
    		
    		if ( xMat == null ) {
    			for ( CustomBlockIntegration custIntegration : Prison.get().getIntegrationManager().getCustomBlockIntegrations() ) {
    				
    				if ( custIntegration.isRegistered() ) {
    					
    					if ( custIntegration instanceof CustomItems ) {
    						CustomItems cItems = (CustomItems) custIntegration;
    						
    						String blockId = cItems.getCustomBlockId(bukkitBlock);
    						
    						if ( blockId != null ) {
    							
    							sBlock = new SpigotBlock( cItems.getBlockType(), blockId, bukkitBlock );
    						}
    					}
    				}
    			}
    			
    		}
    		
    		else if ( xMat != null ) {
    			sBlock = new SpigotBlock( xMat.name(), bukkitBlock );
    		}
    	}

    	
//    	SpigotBlock sBlock = SpigotCompatibility.getInstance().getPrisonBlock( bBlock );
    	
//    	super( SpigotCompatibility.getInstance().getPrisonBlock( bBlock ) );
//    	super( XMaterial.matchXMaterial( bBlock.getType() ).name() );
//    	super( XBlock. .getType( bBlock ).name() );
    	
    	return sBlock;
    }
    
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append( getPrisonBlock().getBlockName() ).append( " " )
    			.append( getLocation().toWorldCoordinates() );
    	
    	
    	return sb.toString();
    }

//    public String getBlockName() {
//    	return super.getBlockName();
//    }
    
    @Override public Location getLocation() {
        return getWrapper() == null ? null :
        		SpigotUtil.bukkitLocationToPrison(getWrapper().getLocation());
    }

    @Override public PrisonBlock getRelative(BlockFace face) {
        return getSpigotBlock(
        		getWrapper().getRelative(
        				org.bukkit.block.BlockFace.valueOf(
        						face.name())));
    }

//    @Override public BlockType getType() {
//    	return SpigotCompatibility.getInstance().getBlockType( getWrapper() );
////        return SpigotUtil.materialToBlockType(bBlock.getType());
//    }

//    @Override
    public PrisonBlock getPrisonBlock() {
    	
    	return this;
    	
//    	PrisonBlock results = null;
//    	
//    	if ( getPrisonBlockTypes() != null ) {
//    		
//    		// Need to see if any PrisonBlockTypes exist in the mine where this block is located.
//    		for ( PrisonBlockType blockType : getPrisonBlockTypes() ) {
//    			
//    			results = getPrisonBlockFromCustomBlockIntegration( blockType );
//    			if ( results != null ) {
//    				
//    				break;
//    			}
//    		}
//    	}
//
//    	if ( results == null && getWrapper() != null ) {
//    		results = SpigotCompatibility.getInstance().getPrisonBlock( getWrapper() );
//    	}
//
//    	if ( results != null && results.getLocation() == null && getLocation() != null ) {
//    		// Clone the block that was found in the mine.  This will allow us to 
//    		// set the location:
//    		results = new PrisonBlock( results );
//    		
//    		results.setLocation( getLocation() );
//    	}
//    	
//    	return results;
    }
    
	private PrisonBlock getPrisonBlockFromCustomBlockIntegration( PrisonBlockType blockType ) {
		PrisonBlock results = null;
    	
    	switch ( blockType )
		{
			case minecraft:
				// No special processing for minecraft types since that will be the fallback later on:

			case CustomItems:
				{
					CustomBlockIntegration customItemsIntegration = 
									PrisonAPI.getIntegrationManager().getCustomBlockIntegration( blockType );
					// NOTE: This would be the situation where the admin added the Custom Items plugin, added blocks
					//       then removed the plugin.  So if it's null, ignore it.
					if ( customItemsIntegration != null ) {
						results = customItemsIntegration.getCustomBlock( this );
					}
				}
				
				break;
				
			default:
				break;
		}
    	
    	return results;
    }

    
	public Set<PrisonBlockType> getPrisonBlockTypes() {
		return prisonBlockTypes;
	}
    public void setPrisonBlockTypes( Set<PrisonBlockType> prisonBlockTypes ) {
		this.prisonBlockTypes = prisonBlockTypes;
	}

    public void setPrisonBlock( XMaterial xMat ) {
    	setPrisonBlock( SpigotUtil.getPrisonBlock( xMat.name() ) );
		
	}

    public void setPrisonBlock( PrisonBlock prisonBlock ) {
    	
    	if ( prisonBlock == null ) {
    		prisonBlock = PrisonBlock.AIR;
    	}
    	
    	switch ( prisonBlock.getBlockType() )
		{
			case minecraft:
				
				SpigotCompatibility.getInstance().
							updateSpigotBlock( prisonBlock, getWrapper() );
				
				break;

			case CustomItems:
				{
					CustomBlockIntegration customItemsIntegration = 
									PrisonAPI.getIntegrationManager().getCustomBlockIntegration( prisonBlock.getBlockType() );
					
					Block results = customItemsIntegration.setCustomBlockId( this, prisonBlock.getBlockName(), false );
					if ( results != null ) {
						this.bBlock = ((SpigotBlock) results).getWrapper();
					}
					else {
						Output.get().logInfo( "SpigotBLock.setPrisonBlock: Failed to set a custom block %s ", prisonBlock.getBlockNameFormal() );
					}
				}
				
				break;
				
			default:
				break;
		}
    	
    	
    }
    
    public void setBlockFace( BlockFace blockFace ) {
    	
    	SpigotCompatibility.getInstance()
					.setBlockFace( getWrapper(), blockFace );
    }
    
//    /**
//     * <p>When setting the Data and Type, turn off apply physics which will reduce the over head on block updates
//     * by about 1/3.  Really do not need to apply physics in the mines especially if no air blocks and nothing
//     * that could fall (sand) or flow is placed.
//     * </p>
//     */
//	@Override 
//	public void setType( PrisonBlock blockType) {
//    	
//		SpigotCompatibility.getInstance()
//						.updateSpigotBlock( blockType, getWrapper() );
//		
////    	if ( type != null && type != BlockType.IGNORE ) {
////    		
////    		Material mat = SpigotUtil.getMaterial( type );
////    		if ( mat != null ) {
////    			bBlock.setType( mat, false );
////    		}
////    		
//////    		Optional<XMaterial> xMatO = XMaterial.matchXMaterial( type.name() );
//////    		
//////    		if ( xMatO.isPresent() ) {
//////    			XMaterial xMat = xMatO.get();
//////    			Optional<Material> matO = xMat.parseMaterial();
//////    			
//////    			if ( matO.isPresent() ) {
//////    				Material mat = matO.get();
//////    				
//////    				bBlock.setType( mat, false );
//////
//////    			}
//////    		}
////    		else {
////    			// spigot 1.8.8 support for XMaterial: 
////    			//   MOSS_STONE  LAPIS_LAZULI_ORE  LAPIS_LAZULI_BLOCK  PILLAR_QUARTZ_BLOCK
////    			// 
////    			
////    			Output.get().logWarn( "SpigotBlock.setType: could not match BlockType " + 
////    						type.name() + " defaulting to AIR instead.");
////    			
////    			mat = SpigotUtil.getMaterial( BlockType.AIR );
////        		if ( mat != null ) {
////        			bBlock.setType( mat, false );
////        		}
////    		}
////    		
//////    		try {
//////				MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
//////				bBlock.setType(materialData.getItemType(), false);
//////				if ( type.getMaterialVersion() == MaterialVersion.v1_8) {
//////					
//////					bBlock.setData(materialData.getData(), false);
//////				}
//////			}
//////			catch ( Exception e ) {
//////				Output.get().logError( 
//////						String.format( "BlockType could not be set: %s %s ", 
//////						(type == null ? "(null)" : type.name()), e.getMessage()) );
//////			}
////    	}
//    }

    @Override 
    public BlockState getState() {
    	
    	BlockState results = null;
    	
    	XMaterial xMat = SpigotUtil.getXMaterial( getPrisonBlock() );
    	
        switch ( xMat ) {
        	case LEVER:
                results = new SpigotLever(this);
                break;
            case ACACIA_SIGN:
            case ACACIA_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case CRIMSON_SIGN:
            case CRIMSON_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_WALL_SIGN:
            case WARPED_SIGN:
            case WARPED_WALL_SIGN:
                results = new SpigotSign(this);
                break;
                
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case CRIMSON_DOOR:
            case DARK_OAK_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case WARPED_DOOR:
            	results = new SpigotDoor(this);
            	break;

            default:
                results = new SpigotBlockState(this);
        }
        
        return results;
    }

    @Override 
    public boolean breakNaturally() {
    	boolean results = false;
    	
    	if ( getWrapper() != null ) {
    		
    		results = getWrapper().breakNaturally();
    	}
        
        return results;
    }

    @Override 
    public List<ItemStack> getDrops() {
        List<ItemStack> ret = new ArrayList<>();

        if ( getWrapper() != null ) {
        	
        	getWrapper().getDrops()
        			.forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));
        }

        return ret;
    }

	
    @Override 
    public List<ItemStack> getDrops(ItemStack tool) {
        List<ItemStack> ret = new ArrayList<>();

        if ( getWrapper() != null ) {
        	
        	if ( tool == null ) {
        		ret.addAll( getDrops() );
        	}
        	else {
        		
        		org.bukkit.inventory.ItemStack bukkitItemStack = SpigotUtil.prisonItemStackToBukkit(tool);
        		
        		if ( bukkitItemStack != null ) {
        			
        			getWrapper().getDrops( bukkitItemStack )
        			.forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));
        		}
        	}
        	
        }

        return ret;
    }
    
    /**
     * <p>This clears the drops for the given block, so if the event is not canceled, it will
 	 * not result in duplicate drops.
	 * </p>
     */
    public void clearDrops() {

    	if ( getWrapper() != null && getWrapper().getDrops() != null ) {
    		for ( org.bukkit.inventory.ItemStack iStack : getWrapper().getDrops() )
			{
				iStack.setAmount( 0 );
			}
//    		getWrapper().getDrops().clear();
    	}
    }

    
//    public List<SpigotItemStack> getDrops(SpigotItemStack tool) {
//    	List<SpigotItemStack> ret = new ArrayList<>();
//    	
//    	bBlock.getDrops(SpigotUtil.prisonItemStackToBukkit(tool))
//    	.forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));
//    	
//    	return ret;
//    }

    public org.bukkit.block.Block getWrapper() {
        return bBlock;
    }


}

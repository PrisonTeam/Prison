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

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.BlockState;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotBlock implements Block {

    private org.bukkit.block.Block bBlock;

    /**
     * If this block was identified as being within a specific mine, then 
     * keep track of the mine's PrisonBlockTypes to ensure the
     * correct custom block integration is able to identify it if it
     * is a custom block type.
     */
    private transient Set<PrisonBlockType> prisonBlockTypes;


    public SpigotBlock(org.bukkit.block.Block bBlock) {
        this.bBlock = bBlock;
        
        this.prisonBlockTypes = new HashSet<>();
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append( getPrisonBlock().getBlockName() ).append( " " )
    			.append( getLocation().toCoordinates() );
    	
    	
    	return sb.toString();
    }

    public String getBlockName() {
    	return getPrisonBlock().getBlockName();
    }
    
    @Override public Location getLocation() {
        return SpigotUtil.bukkitLocationToPrison(bBlock.getLocation());
    }

    @Override public Block getRelative(BlockFace face) {
        return new SpigotBlock(
        		bBlock.getRelative(
        				org.bukkit.block.BlockFace.valueOf(
        						face.name())));
    }

    @Override public BlockType getType() {
    	return SpigotPrison.getInstance().getCompatibility().getBlockType( bBlock );
//        return SpigotUtil.materialToBlockType(bBlock.getType());
    }

    @Override
    public PrisonBlock getPrisonBlock() {
    	PrisonBlock results = null;
    	
    	if ( getPrisonBlockTypes() != null ) {
    		
    		// Need to see if any PrisonBlockTypes exist in the mine where this block is located.
    		for ( PrisonBlockType blockType : getPrisonBlockTypes() ) {
    			
    			results = getPrisonBlockFromCustomBlockIntegration( blockType );
    			if ( results != null ) {
    				break;
    			}
    		}
    	}

    	if ( results == null ) {
    		results = SpigotPrison.getInstance().getCompatibility().getPrisonBlock( bBlock );
    	}
    	
    	return results;
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
    	
    	switch ( prisonBlock.getBlockType() )
		{
			case minecraft:
				{
					SpigotPrison.getInstance().getCompatibility().
									updateSpigotBlock( prisonBlock, bBlock );
				}
				
				
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
    	
    	SpigotPrison.getInstance().getCompatibility()
					.setBlockFace( bBlock, blockFace );
    }
    /**
     * <p>When setting the Data and Type, turn off apply physics which will reduce the over head on block updates
     * by about 1/3.  Really do not need to apply physics in the mines especially if no air blocks and nothing
     * that could fall (sand) or flow is placed.
     * </p>
     */
	@Override 
	public void setType(BlockType blockType) {
    	
		SpigotPrison.getInstance().getCompatibility()
						.updateSpigotBlock( blockType, bBlock );
		
//    	if ( type != null && type != BlockType.IGNORE ) {
//    		
//    		Material mat = SpigotUtil.getMaterial( type );
//    		if ( mat != null ) {
//    			bBlock.setType( mat, false );
//    		}
//    		
////    		Optional<XMaterial> xMatO = XMaterial.matchXMaterial( type.name() );
////    		
////    		if ( xMatO.isPresent() ) {
////    			XMaterial xMat = xMatO.get();
////    			Optional<Material> matO = xMat.parseMaterial();
////    			
////    			if ( matO.isPresent() ) {
////    				Material mat = matO.get();
////    				
////    				bBlock.setType( mat, false );
////
////    			}
////    		}
//    		else {
//    			// spigot 1.8.8 support for XMaterial: 
//    			//   MOSS_STONE  LAPIS_LAZULI_ORE  LAPIS_LAZULI_BLOCK  PILLAR_QUARTZ_BLOCK
//    			// 
//    			
//    			Output.get().logWarn( "SpigotBlock.setType: could not match BlockType " + 
//    						type.name() + " defaulting to AIR instead.");
//    			
//    			mat = SpigotUtil.getMaterial( BlockType.AIR );
//        		if ( mat != null ) {
//        			bBlock.setType( mat, false );
//        		}
//    		}
//    		
////    		try {
////				MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
////				bBlock.setType(materialData.getItemType(), false);
////				if ( type.getMaterialVersion() == MaterialVersion.v1_8) {
////					
////					bBlock.setData(materialData.getData(), false);
////				}
////			}
////			catch ( Exception e ) {
////				Output.get().logError( 
////						String.format( "BlockType could not be set: %s %s ", 
////						(type == null ? "(null)" : type.name()), e.getMessage()) );
////			}
//    	}
    }

    @Override public BlockState getState() {
        switch (getType()) {
            case LEVER:
                return new SpigotLever(this);
            case SIGN:
                return new SpigotSign(this);
            case ACACIA_DOOR_BLOCK:
            case OAK_DOOR_BLOCK:
            case BIRCH_DOOR_BLOCK:
            case SPRUCE_DOOR_BLOCK:
            case DARK_OAK_DOOR_BLOCK:
            case IRON_DOOR_BLOCK:
            case JUNGLE_DOOR_BLOCK:
                return new SpigotDoor(this);
            default:
                return new SpigotBlockState(this);
        }
    }

    @Override public boolean breakNaturally() {
        return bBlock.breakNaturally();
    }

    @Override public List<ItemStack> getDrops() {
        List<ItemStack> ret = new ArrayList<>();

        bBlock.getDrops()
            .forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));

        return ret;
    }

	
    @Override 
    public List<ItemStack> getDrops(ItemStack tool) {
        List<ItemStack> ret = new ArrayList<>();

        bBlock.getDrops(SpigotUtil.prisonItemStackToBukkit(tool))
            .forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));

        return ret;
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

/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.spigot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.inventory.InventoryType;
import tech.mcprison.prison.internal.inventory.Viewable;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.BlockTestStats;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

/**
 * Utilities for converting Prison-Core types to Spigot types.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotUtil {

    private SpigotUtil() {
    	super();
    }

  /*
   * BlockType and Material
   */

    public static XMaterial getXMaterial( Material material) {
    	return XMaterial.matchXMaterial(material);
    }
    
    public static XMaterial getXMaterial( String materialName ) {
    	return XMaterial.matchXMaterial( materialName ).orElse( null );
    }
    
    /**
     * <p>Gets the XMaterial based upon the BlockType name, and if it fails to hit
     * anything, then it falls back on to the id, of which XMaterial strips the 
     * prefix of "minecraft:".
     * </p>
     * 
     * @param prisonBlockType
     * @return
     */
    public static XMaterial getXMaterial( BlockType prisonBlockType ) {
    	
    	XMaterial xMat = SpigotPrison.getInstance().getCompatibility()
    						.getXMaterial( prisonBlockType );
    	
    	return xMat;
    }
    
    public static XMaterial getXMaterial( PrisonBlock prisonBlock ) {
    	
    	XMaterial xMat = getXMaterial( prisonBlock.getBlockName());
    	
    	return xMat;
    }
    
    public static Material getMaterial( BlockType prisonBlockType ) {
    	XMaterial xMat = getXMaterial( prisonBlockType );
    	
    	return xMat == null ? null : xMat.parseMaterial();
    }
    

    
	public static BlockType blockToBlockType( Block spigotBlock ) {
		BlockType results = SpigotPrison.getInstance().getCompatibility()
				.getBlockType( spigotBlock );
		
//		
//		XMaterial xMatMatch = XMaterial.matchXMaterial( material );
//		
//		for ( BlockType blockType : BlockType.values() ) {
//			XMaterial xMat = getXMaterial( blockType );
//			if ( xMat != null ) {
//				results = blockType;
//				break;
//			}
//		}
		
        return results;
    }
	
	public static BlockType prisonBlockToBlockType( PrisonBlock prisonBlock ) {
		
		BlockType results = BlockType.getBlock( prisonBlock.getBlockName() );
		
		return results;
	}

	/**
	 * <p>Returns a stack of BlockType or a stack of air.
	 * </p>
	 * 
	 * @param prisonBlockType
	 * @param amount
	 * @return
	 */
	public static ItemStack getItemStack( BlockType prisonBlockType, int amount ) {
		ItemStack bukkitStack = null;
        XMaterial xMat = getXMaterial( prisonBlockType );
        if ( xMat != null ) {
        	bukkitStack = xMat.parseItem();
        	bukkitStack.setAmount( amount );
        }
        else {
        	bukkitStack = getXMaterial( "air" ).parseItem();
        	bukkitStack.setAmount( 1 );
        }
        return bukkitStack;
	}
	
	public static ItemStack getItemStack( Material material, int amount ) {
		XMaterial xMat = getXMaterial( material );
		ItemStack bukkitStack = xMat.parseItem();
		bukkitStack.setAmount( amount );
		return bukkitStack;
	}
    
	
	public static ItemStack getItemStack( XMaterial xMaterial, int amount ) {
		ItemStack bukkitStack = xMaterial.parseItem();
		bukkitStack.setAmount( amount );
		return bukkitStack;
	}
	
	public static SpigotItemStack getSpigotItemStack( XMaterial xMaterial, int amount ) {
		SpigotItemStack itemStack = new SpigotItemStack( getItemStack( xMaterial, amount ) );
		
		return itemStack;
	}
	
	/**
	 * Used in AutoManagerFeatures.
	 * 
	 * @param player
	 * @param itemStack
	 * @return
	 */
	public static HashMap<Integer, SpigotItemStack> addItemToPlayerInventory( 
			Player player, SpigotItemStack itemStack ) {
		HashMap<Integer, SpigotItemStack> results = new HashMap<>();
		
		HashMap<Integer, ItemStack> overflow = player.getInventory().addItem( itemStack.getBukkitStack() );
		Set<Integer> keys = overflow.keySet();
		for ( Integer key : keys ) {
			results.put( key, new SpigotItemStack( overflow.get( key ) ));
		}
		
		return results;
	}
	
	/**
	 * Used in AutoManagerFeatures.
	 * @param player
	 * @param itemStack
	 */
	public static void dropPlayerItems( Player player, SpigotItemStack itemStack ) {
		
		org.bukkit.Location dropPoint = player.getLocation().add( player.getLocation().getDirection());
		
		player.getWorld().dropItem( dropPoint, itemStack.getBukkitStack() );
		
	}
	
	
	public static void dropItems( SpigotBlock block, SpigotItemStack itemStack ) {
		
		
		block.getWrapper().getLocation().getWorld().dropItem( 
						block.getWrapper().getLocation(), itemStack.getBukkitStack() );
		
	}
	
	public static boolean playerInventoryContainsAtLeast( Player player, 
						SpigotItemStack itemStack, int quantity ) {
		boolean results = player.getInventory().containsAtLeast( 
								itemStack.getBukkitStack(), quantity );
		
		return results;
	}
	
	public static HashMap<Integer, ItemStack> playerInventoryRemoveItem( Player player, SpigotItemStack itemStack ) {
		HashMap<Integer, ItemStack> results = player.getInventory().removeItem( itemStack.getBukkitStack() );
		
		return results;
	}
	
	/**
	 * Note that XMaterial.parseMaterial() may work well for v1.13.x and higher,
	 * but it does not represent the correct block types in lower versions, 
	 * such as with 1.8.x. This has everything to do with magic numbers.
	 * Instead convert it to an ItemStack.
	 * 
	 * @param blockTypes
	 */
	public static List<PrisonBlock> getAllPlatformBlockTypes() {
		List<PrisonBlock> blockTypes = new ArrayList<>();
		
		for ( XMaterial xMat : XMaterial.values() ) {
			if ( xMat.isSupported() ) {
				
				ItemStack itemStack = xMat.parseItem();
				if ( itemStack != null ) {
					
					//if ( itemStack.getType().isBlock() ) 
					{
						
						PrisonBlock block = new PrisonBlock( xMat.name().toLowerCase() );
						
						block.setValid( true );
						block.setBlock( itemStack.getType().isBlock() );
						
						blockTypes.add( block );
					}
				}
				
//				Material mat = xMat.parseMaterial();
//				if ( mat != null ) {
//					if ( mat.isBlock() ) {
//						
//						PrisonBlock block = new PrisonBlock( xMat.name().toLowerCase() );
//						
//						block.setValid( true );
//						block.setBlock( mat.isBlock() );
//						
//						blockTypes.add( block );
//					}
//				}
				else {
					Output.get().logWarn( "### SpigotUtil.testAllPrisonBlockTypes: " +
							"Possible XMaterial FAIL: XMaterial " + xMat.name() +
							" is supported for this version, but the XMaterial cannot " +
							"be mapped to an actual Material.");
				}
			}
		}
		
		return blockTypes;
	}
	
	public static List<PrisonBlock> getAllCustomBlockTypes() {
		List<PrisonBlock> blockTypes = new ArrayList<>();
		
		List<Integration> customBlockInterfaces = 
				Prison.get().getIntegrationManager().getAllForType( IntegrationType.CUSTOMBLOCK );

		for ( Integration integration : customBlockInterfaces )
		{
			if ( integration.hasIntegrated() && integration instanceof CustomBlockIntegration ) {
				CustomBlockIntegration blkInt = (CustomBlockIntegration) integration;
				
				List<PrisonBlock> blocks = blkInt.getCustomBlockList();
				
				if ( blocks.size() > 0 ) {
					blockTypes.addAll( blocks );
				}
				
				Output.get().logInfo( "&3Custom Block Integration: &7%s &3registered &7%d &3blocks " +
						"&with the PrisonBlockType of &7%s&3.",
						blkInt.getDisplayName(), blocks.size(), 
						blkInt.getBlockType().name() );
			}
		}

		return blockTypes;
	}
	
	
	/**
	 * <p>This will take a string name of a block, and convert it to the
	 * String name of a XMaterial.  If it cannot directly perform the
	 * conversion, then it will fall back to using the old prison's
	 * BlockType to perform the conversion.
	 * </p>
	 * 
	 * @param blockName
	 * @return
	 */
	public static PrisonBlock getPrisonBlock( String blockName ) {
		
		PrisonBlock results = null;
		BlockType bTypeObsolete = null;
		
		XMaterial xMat = getXMaterial( blockName );
		
		if ( xMat == null ) {
			// Try to get the material through the old prison blocks:
			bTypeObsolete = BlockType.getBlock( blockName );
			
			xMat = getXMaterial( bTypeObsolete );
		}
		
		if ( xMat != null ) {
			results = new PrisonBlock( xMat.name() );
			
			if ( bTypeObsolete != null ) {
				results.setLegacyBlock( true );
			}
		}
		else {
			results = new PrisonBlock( blockName );
			results.setValid( false );
		}
		return results;
	}
	
	
	public static void testAllPrisonBlockTypes() {
		double version = XMaterial.getVersion();
		
		StringBuilder sbNoMap = new StringBuilder();
		StringBuilder sbNotSupported = new StringBuilder();
		StringBuilder sbSpigotNotSupported = new StringBuilder();
		
		int supportedBlockCountPrison = 0;
		int supportedBlockCountXMaterial = 0;
		
		for ( BlockType block : BlockType.values() ) {
			
			if ( block.isBlock() ) {
				XMaterial xMat = getXMaterial( block );
				
				if ( xMat == null ) {
					if ( sbNoMap.length() > 0 ) {
						sbNoMap.append( " " );
					}
					
					Material mat = getMaterial( block );
					
					String bName = block.name() + (mat == null ? "" : "(" + mat.name() + ")");
					sbNoMap.append( bName );
				}
				else if ( !xMat.isSupported() ) {
					if ( sbNotSupported.length() > 0 ) {
						sbNotSupported.append( " " );
					}
					sbNotSupported.append( block.name() );
				}
				else {
					supportedBlockCountPrison++;
				}
			}
		}
		
		for ( XMaterial xMat : XMaterial.values() ) {
			if ( xMat.isSupported() ) {
				
				Material mat = xMat.parseMaterial();
				if ( mat != null ) {
					if ( mat.isBlock() ) {
						supportedBlockCountXMaterial++;
					}
				}
				else {
					Output.get().logWarn( "### SpigotUtil.testAllPrisonBlockTypes: " +
							"Possible XMaterial FAIL: XMaterial " + xMat.name() +
							" is supported for this version, but the XMaterial cannot " +
							"be mapped to an actual Material.");
				}
			}
		}
		
		
		for ( Material spigotMaterial : Material.values() ) {
			
			if ( spigotMaterial.isBlock() &&
					BlockType.getBlock( spigotMaterial.name() ) == null ) {
				
				String name = spigotMaterial.name().toLowerCase();
				if ( !name.contains( "banner" ) && !name.contains( "button" ) && 
						!name.contains( "pressure_plate" ) && !name.contains( "potted_" ) && 
						!name.contains( "_head" ) && !name.contains( "_skull" ) && 
						!name.contains( "_bed" ) && !name.contains( "_trapdoor" ) && 
						!name.contains( "stem" ) && !name.contains( "stairs" ) && 
						!name.contains( "_slab" ) ) {
					
					sbSpigotNotSupported.append( spigotMaterial.name() );
					sbSpigotNotSupported.append( " " );
				}
				
			}
			
		}
		
		// Next test all of the spigot/bukkit Materials:
		BlockTestStats stats = SpigotPrison.getInstance().getCompatibility()
										.testCountAllBlockTypes();
		
		
		Output.get().logWarn( "### SpigotUtil.testAllPrisonBlockTypes: Bukkit version: " + version + 
									"  Supported Prison Blocks: " + supportedBlockCountPrison +
									"  Supported XMaterial Blocks: " + supportedBlockCountXMaterial );
		
		Output.get().logWarn( "### SpigotUtil.testAllPrisonBlockTypes: Raw Bukkit/Spigot " + stats.toString() );
		
		logTestBlocks( sbNoMap, "### SpigotUtil.testAllPrisonBlockTypes:  " +
										"Prison Blocks no maps to XMaterial: " );
		logTestBlocks( sbNotSupported, "### SpigotUtil.testAllPrisonBlockTypes:  " +
										"Prison Blocks not supported: " );
		
		
		Output.get().logWarn( "### SpigotUtil.testAllPrisonBlockTypes: Spigot blocks ignored: " +
				"banner, button, pressure_plate, potted, head, skull, bed, trapdoor, stem, stairs, slab "  );
		logTestBlocks( sbSpigotNotSupported, "### SpigotUtil.testAllPrisonBlockTypes:  " +
				"Spigot blocks not supported: " );
	}
    
	
	private static void logTestBlocks( StringBuilder sb, String message ) {
		
		int start = 0;
		int end = 100;
		
		while ( sb.length() > end ) {
			end = sb.lastIndexOf( " ", end );
			Output.get().logWarn( message + 
					(end < 0 ? sb.substring( start ) : sb.substring( start, end )));

			start = end;
			end += 100;
		}
		
		Output.get().logWarn( message + sb.substring( start ));
	}
	
//    @SuppressWarnings( "deprecation" )
//	public static BlockType materialToBlockType(Material material) {
//        return BlockType.getBlock(material.getId()); // To be safe, we use legacy ID
//    }

//    @SuppressWarnings( "deprecation" )
//	public static MaterialData blockTypeToMaterial(BlockType type) {
//        Material material = Material.getMaterial(type.getLegacyId());
//        if ( material == null ) {
//        	material = Material.STONE;
//        }
//        return new MaterialData(material, (byte) type.getData()); // To be safe, we use legacy ID
////		Material material = Material.getMaterial(type.getLegacyId());
////		if ( material == null ) {
////			material = Material.STONE;
////		}
////		
////		return new MaterialData(material, (byte) type.getData()); // To be safe, we use legacy ID
////
////    	
////    	
////    	MaterialData results = null;
////    	
////    	if ( type.getMaterialVersion() == MaterialVersion.v1_13 ) {
////    		Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: v1_13 : %s ",
////    				type.getId()) );
////    		
////    		// Material type for 1.13 and higher have a legacyID == 0:
////    		Material material = null;
////    		material = getMaterial( type.getId() );
////    		
////    		if ( material == null ) {
////    			String materialName = type.getId().toUpperCase();
////    			material = getMaterial( materialName );
////    			
////    			Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: was null : %s -> %s [%s]", 
////    					type.name(), materialName, (material == null ? "null" : "NOT null")) );
////    			
//////    			if ( material == null ) {
//////    				material = Material.
//////    				Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: was null : %s -> %s [%s]", 
//////    						type.name(), materialName, (material == null ? "null" : "NOT null")) );
//////    				
//////    			}
////    		}
////    		else {
////    			Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: %s [%s]", 
////    					type.name(), (material == null ? "null" : "NOT null")) );
////    			
////    		}
////    		
////    		if ( material == null ) {
////    			material = Material.STONE;
////    		}
////    		results = new MaterialData(material);
////    	} 
////    	else {
//////    		Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: v1_8 : %s  %s  data=%s", 
//////    				type.getId(), Integer.toString( type.getLegacyId()), 
//////    				Integer.toString( type.getData())) );
//////    		// type.getMaterialVersion() == MaterialVersion.v1_8
////    		
////    		// Material types for 1.12 and lower:
////    		Material material = Material.getMaterial(type.getLegacyId());
////    		if ( material == null ) {
////    			material = Material.STONE;
////    		}
////    		
////    		results = new MaterialData(material, (byte) type.getData()); // To be safe, we use legacy ID
////    		
////    	}
////    	return results;
//    }
    
//    private static Material getMaterial( String materialName ) {
//    	Material results = null;
//    	
//    	try {
//    		results = Material.matchMaterial( materialName );
//    	}
//    	catch ( Exception e ) {
//    		// Do nothing for now... 
//    		// Will try other combination later and will report failure if needed;
//    		Output.get().logInfo( "&cSpigotUtil.getMaterial() Failure : &7" + e.getMessage() );
//    	}
//    	
//    	return results;
//    }

  /*
   * Location
   */

    public static Location bukkitLocationToPrison(org.bukkit.Location bukkitLocation) {
        return new Location(new SpigotWorld(bukkitLocation.getWorld()), bukkitLocation.getX(),
            bukkitLocation.getY(), bukkitLocation.getZ(), bukkitLocation.getPitch(),
            bukkitLocation.getYaw());
    }

    public static org.bukkit.Location prisonLocationToBukkit(Location prisonLocation) {
        return new org.bukkit.Location(Bukkit.getWorld(prisonLocation.getWorld().getName()),
            prisonLocation.getX(), prisonLocation.getY(), prisonLocation.getZ(),
            prisonLocation.getYaw(), prisonLocation.getPitch());
    }

  /*
   * ItemStack
   */

    public static SpigotItemStack bukkitItemStackToPrison( ItemStack bukkitStack) {
    	SpigotItemStack results = null;
    	
    	if ( bukkitStack != null ) {
    		results = new SpigotItemStack( bukkitStack );
    	}
    	
    	return results;
    }

    public static ItemStack prisonItemStackToBukkit(
    				tech.mcprison.prison.internal.ItemStack prisonStack) {
        int amount = prisonStack.getAmount();
        
        ItemStack bukkitStack = getItemStack( prisonStack.getMaterial(), amount );
        
//        MaterialData materialData = blockTypeToMaterial(prisonStack.getMaterial());
//
//        ItemStack bukkitStack = new ItemStack(materialData.getItemType(), amount);
//        bukkitStack.setData(materialData);
        
        ItemMeta meta;
        if (bukkitStack.getItemMeta() == null || !bukkitStack.hasItemMeta()) {
        	meta = Bukkit.getItemFactory().getItemMeta(bukkitStack.getType());
        } else {
        	meta = bukkitStack.getItemMeta();
        }
        
        if (meta != null) {
        	if (prisonStack.getDisplayName() != null) {
        		meta.setDisplayName(Text.translateAmpColorCodes(prisonStack.getDisplayName()));
        	}
        	if (prisonStack.getLore() != null) {
        		List<String> colored = new ArrayList<>();
        		for (String uncolor : prisonStack.getLore()) {
        			colored.add(Text.translateAmpColorCodes(uncolor));
        		}
        		meta.setLore(colored);
        	}
        	bukkitStack.setItemMeta(meta);
        }        

        return bukkitStack;
    }
    
  public static List<SpigotItemStack> getDrops(SpigotBlock block, SpigotItemStack tool) {
	List<SpigotItemStack> ret = new ArrayList<>();
	
	block.getWrapper().getDrops( tool.getBukkitStack() )
			.forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));
	
	return ret;
}
    
  /*
   * InventoryType
   */

    public static InventoryType bukkitInventoryTypeToPrison( org.bukkit.event.inventory.InventoryType type) {
        return InventoryType.valueOf(type.name());
    }

    public static org.bukkit.event.inventory.InventoryType prisonInventoryTypeToBukkit(
        InventoryType type) {
        return org.bukkit.event.inventory.InventoryType.valueOf(type.name());
    }

    public static InventoryType.SlotType bukkitSlotTypeToPrison(
        org.bukkit.event.inventory.InventoryType.SlotType type) {

        return InventoryType.SlotType.valueOf(type.name());
    }

  /*
   * Property
   */

    public static InventoryView.Property prisonPropertyToBukkit(Viewable.Property property) {
        return InventoryView.Property.valueOf(property.name());
    }

    
    /**
     * <p>Vault economy requires the parameter of bukkit's OfflinePlayer.
     * That was never exposed for good reasons, and do not want to use
     * bukkit/spigot specific code within that integration.  So, this is
     * where this code will live since it is a Spigot untility.
     * </p>
     * 
     * @param uuid
     * @return OfflinePlayer
     */
    public static OfflinePlayer getBukkitOfflinePlayer( UUID uuid ) {
    	OfflinePlayer results = null;
    	
    	for ( OfflinePlayer offP : Bukkit.getOfflinePlayers() ) {
    		if ( uuid != null && offP.getUniqueId().equals(uuid) ) {
    			results = offP;
    			break;
	  		}
		}
    	
    	return results;
    }
}

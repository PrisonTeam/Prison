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

package tech.mcprison.prison.spigot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import tech.mcprison.prison.internal.inventory.InventoryType;
import tech.mcprison.prison.internal.inventory.Viewable;
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
    }

  /*
   * BlockType and Material
   */

    @SuppressWarnings( "deprecation" )
	public static BlockType materialToBlockType(Material material) {
        return BlockType.getBlock(material.getId()); // To be safe, we use legacy ID
    }

    @SuppressWarnings( "deprecation" )
	public static MaterialData blockTypeToMaterial(BlockType type) {
        Material material = Material.getMaterial(type.getLegacyId());
        if ( material == null ) {
        	material = Material.STONE;
        }
        return new MaterialData(material, (byte) type.getData()); // To be safe, we use legacy ID
//		Material material = Material.getMaterial(type.getLegacyId());
//		if ( material == null ) {
//			material = Material.STONE;
//		}
//		
//		return new MaterialData(material, (byte) type.getData()); // To be safe, we use legacy ID
//
//    	
//    	
//    	MaterialData results = null;
//    	
//    	if ( type.getMaterialVersion() == MaterialVersion.v1_13 ) {
//    		Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: v1_13 : %s ",
//    				type.getId()) );
//    		
//    		// Material type for 1.13 and higher have a legacyID == 0:
//    		Material material = null;
//    		material = getMaterial( type.getId() );
//    		
//    		if ( material == null ) {
//    			String materialName = type.getId().toUpperCase();
//    			material = getMaterial( materialName );
//    			
//    			Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: was null : %s -> %s [%s]", 
//    					type.name(), materialName, (material == null ? "null" : "NOT null")) );
//    			
////    			if ( material == null ) {
////    				material = Material.
////    				Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: was null : %s -> %s [%s]", 
////    						type.name(), materialName, (material == null ? "null" : "NOT null")) );
////    				
////    			}
//    		}
//    		else {
//    			Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: %s [%s]", 
//    					type.name(), (material == null ? "null" : "NOT null")) );
//    			
//    		}
//    		
//    		if ( material == null ) {
//    			material = Material.STONE;
//    		}
//    		results = new MaterialData(material);
//    	} 
//    	else {
////    		Output.get().logInfo( String.format( "SpigotUtil.blockTypeToMaterial: v1_8 : %s  %s  data=%s", 
////    				type.getId(), Integer.toString( type.getLegacyId()), 
////    				Integer.toString( type.getData())) );
////    		// type.getMaterialVersion() == MaterialVersion.v1_8
//    		
//    		// Material types for 1.12 and lower:
//    		Material material = Material.getMaterial(type.getLegacyId());
//    		if ( material == null ) {
//    			material = Material.STONE;
//    		}
//    		
//    		results = new MaterialData(material, (byte) type.getData()); // To be safe, we use legacy ID
//    		
//    	}
//    	return results;
    }
    
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

    public static tech.mcprison.prison.internal.ItemStack bukkitItemStackToPrison(
        ItemStack bukkitStack) {
        if (bukkitStack == null || bukkitStack.getType().equals(Material.AIR)) {
            return new tech.mcprison.prison.internal.ItemStack(0, BlockType.AIR);
        }

        ItemMeta meta;
        if (!bukkitStack.hasItemMeta()) {
            meta = Bukkit.getItemFactory().getItemMeta(bukkitStack.getType());
        } else {
            meta = bukkitStack.getItemMeta();
        }

        String displayName = null;

        if (meta.hasDisplayName()) {
            displayName = meta.getDisplayName();
        }

        int amount = bukkitStack.getAmount();

        BlockType type = materialToBlockType(bukkitStack.getType());

        List<String> lore = meta.hasLore() ? meta.getLore() : Collections.emptyList();
        String[] lore_arr = lore.toArray(new String[lore.size()]);

        return new tech.mcprison.prison.internal.ItemStack(displayName, amount, type, lore_arr);
    }

    public static ItemStack prisonItemStackToBukkit(
        tech.mcprison.prison.internal.ItemStack prisonStack) {
        int amount = prisonStack.getAmount();
        MaterialData materialData = blockTypeToMaterial(prisonStack.getMaterial());

        ItemStack bukkitStack = new ItemStack(materialData.getItemType(), amount);
        bukkitStack.setData(materialData);

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

  /*
   * InventoryType
   */

    public static InventoryType bukkitInventoryTypeToPrison(
        org.bukkit.event.inventory.InventoryType type) {
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

}

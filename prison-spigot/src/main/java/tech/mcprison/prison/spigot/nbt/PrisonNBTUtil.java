package tech.mcprison.prison.spigot.nbt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import tech.mcprison.prison.output.Output;

public class PrisonNBTUtil {

	
    public NBTItem getNBT( ItemStack bukkitStack ) {
    	NBTItem nbtItemStack = null;
    	
    	if ( bukkitStack != null && bukkitStack.getType() != Material.AIR  ) {
    		try {
				nbtItemStack = new NBTItem( bukkitStack, true );
				
				nbtDebugLog( nbtItemStack, "getNbt" );
			} catch (Exception e) {
				// ignore - the bukkit item stack is not compatible with the NBT library
			}
    	}
    	
    	return nbtItemStack;
    }
    
    
    private void nbtDebugLog( NBTItem nbtItem, String desc ) {
		if ( Output.get().isDebug() ) {
			org.bukkit.inventory.ItemStack iStack = nbtItem.getItem();
			
			int sysId = System.identityHashCode(iStack);
			
			String message = String.format( 
					"NBT %s ItemStack for %s: %s  sysId: %d", 
					desc,
					iStack.hasItemMeta() && iStack.getItemMeta().hasDisplayName() ? 
							iStack.getItemMeta().getDisplayName() :
							iStack.getType().name(),
					nbtItem.toString(),
					sysId );
			
			Output.get().logInfo( message );
			
			//Output.get().logInfo( "NBT: " + new NBTItem( getBukkitStack() ) );
			
		}
    }
    
    
    public boolean hasNBTKey( NBTItem nbtItem, String key ) {
    	boolean results = false;
    	
    	if ( nbtItem != null ) {
    		results = nbtItem.hasKey( key );
    	}
    	
    	return results;
    }
    
    public String getNBTString( NBTItem nbtItem, String key ) {
    	String results = null;
    	
    	if ( nbtItem != null ) {
    		results = nbtItem.getString( key );
    	}
    	return results;
    }
    
    public void setNBTString( NBTItem nbtItem, String key, String value ) {

    	if ( nbtItem != null ) {
    		nbtItem.setString( key, value );
    		nbtDebugLog( nbtItem, "setNBTString" );
    	}
    }
    
}

package tech.mcprison.prison.spigot.nbt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import tech.mcprison.prison.output.Output;

/**
 * <p>This class manages the use of NBTs within prison so it's a common interface
 * that is consistent and works properly.
 * </p>
 * 
 * <p>This class has debug logging, but by default it is turned off to keep it from 
 * getting to be too verbose in the logs.  The idea is that once a section of code
 * is working then it does not need to have debugging enabled.  So this is used
 * more for testing new sections of code.
 * </p>
 * 
 * @author Blue
 *
 */
public class PrisonNBTUtil {

	private boolean enableDebugLogging = false;
	
    public NBTItem getNBT( ItemStack bukkitStack ) {
    	NBTItem nbtItemStack = null;
    	
    	if ( bukkitStack != null && bukkitStack.getType() != Material.AIR  ) {
    		try {
				nbtItemStack = new NBTItem( bukkitStack, true );
				
				if ( isEnableDebugLogging() ) {
					nbtDebugLog( nbtItemStack, "getNbt" );
				}
				
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
			
			String message = Output.stringFormat( 
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
    		
    		if ( isEnableDebugLogging() ) {
    			nbtDebugLog( nbtItem, "setNBTString" );
    		}
    	}
    }


	public boolean isEnableDebugLogging() {
		return enableDebugLogging;
	}
	public void setEnableDebugLogging(boolean enableDebugLogging) {
		this.enableDebugLogging = enableDebugLogging;
	}
    
}

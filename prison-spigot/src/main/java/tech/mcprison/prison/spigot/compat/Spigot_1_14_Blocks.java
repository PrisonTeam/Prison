package tech.mcprison.prison.spigot.compat;

import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tech.mcprison.prison.spigot.block.SpigotItemStack;

public class Spigot_1_14_Blocks 
	extends Spigot_1_13
	//implements CompatibilityBlocks
{

	private static Method SET_CUSTOM_MODEL_DATA = null;
	private static Method GET_CUSTOM_MODEL_DATA = null;

	static {
		try {
			SET_CUSTOM_MODEL_DATA = ItemMeta.class.getDeclaredMethod("setCustomModelData", Integer.class);
			GET_CUSTOM_MODEL_DATA = ItemMeta.class.getDeclaredMethod("getCustomModelData");
		} 
		catch (NoSuchMethodException ex) { 
			ex.printStackTrace(); 
		}
	}
	
    /**
     * Not compatible with Spigot 1.8 through 1.13 so return a value of 0.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
    @Override
    public int getCustomModelData( SpigotItemStack itemStack ) {
    	return getCustomModelData( itemStack == null ? null : itemStack.getBukkitStack() );
    }
    /**
     * Not compatible with Spigot 1.8 through 1.13 so return a value of 0.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
    @Override
    public int getCustomModelData( ItemStack itemStack ) {
    	int results = 0;
    	
    	if ( itemStack != null ) {
			ItemMeta meta = itemStack.getItemMeta();
			
			if (meta != null) {
				try {
					Integer customModelData = (Integer) GET_CUSTOM_MODEL_DATA.invoke(meta);
					
					if ( customModelData != null ) {
						results = customModelData.intValue();
					}
				} 
				catch (ReflectiveOperationException ex ) { 
					ex.printStackTrace(); 
				}
				catch ( ClassCastException ex ) {
					ex.printStackTrace();
				}
			}
			itemStack.setItemMeta(meta);
    	}
    	
    	return results;
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
		
    	if ( itemStack != null ) {
			ItemMeta meta = itemStack.getItemMeta();
			
			if (meta != null) {
				try {
					
					SET_CUSTOM_MODEL_DATA.invoke( meta, customModelData );
				} 
				catch (ReflectiveOperationException ex) { 
					ex.printStackTrace(); 
				}
			}
			itemStack.setItemMeta(meta);
    	}
    	
	}
}

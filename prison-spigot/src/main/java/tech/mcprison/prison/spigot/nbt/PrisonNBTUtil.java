package tech.mcprison.prison.spigot.nbt;

import java.util.function.Function;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
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
 * <p>References:</p>
 * https://github.com/tr7zw/Item-NBT-API/wiki/Using-the-NBT-API
 * 
 * 
 * @author Blue
 *
 */
public class PrisonNBTUtil {

	private static boolean enableDebugLogging = false;
	
	
	public static boolean hasNBTString( ItemStack bukkitStack, String key ) {
		String results = null;
		
		results = getNBTString( bukkitStack, key );
		
		return results != null && results.trim().length() > 0;
	}
	
	public static String getNBTString( ItemStack bukkitStack, String key ) {
		String results = null;
		
		try {
			Function<ReadableItemNBT, String> gsFnc = nbt -> {
				String value = null;
				
				try {
					Object vObj = nbt.getString(key);
					
					if ( vObj != null ) {
						value = (String) vObj;
					}
				} 
				catch (Exception e) {
				}
				
//				if ( Output.get().isDebug() ) {
//					Output.get().logInfo( "PrisonNBTUtil.getNBTString():ItemStack: %s", nbt.toString() );
//				}
				return value;
			};
			
			results = NBT.get(bukkitStack, gsFnc );
//			results = NBT.get(bukkitStack, nbt -> nbt.getString(key));
		}
		catch (Exception e) {
			// ignore exception... these are very common if there are no NBTs on the ItemStack
			
//			Output.get().logInfo(
//					"PrisonNBTUtil.getNBTString(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
		
		return results;
	}
	
	public static String getNBTString( Block bukkitBlock, String key ) {
		String results = null;
		
		try {
			Function<ReadableNBT, String> gsFnc = nbt -> (String) nbt.getString(key);
			
			BlockState blockState = null;

			try {
				blockState = bukkitBlock.getState();
			} 
			catch (Exception e) {
			}
			
			results = NBT.get(blockState, gsFnc );
		} 
		catch (Exception e) {
			// ignore exception... these are very common if there are no NBTs on the ItemStack
			
//			Output.get().logInfo(
//					"PrisonNBTUtil.getNBTString(block): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
		
		return results;
	}

	public static void setNBTString( ItemStack bukkitStack, String key, String value ) {
	
		try {
			NBT.modify(bukkitStack, nbt -> {
				nbt.setString(key, value);
			});
				 
//			if ( isEnableDebugLogging() ) {
//				nbtDebugLog( bukkitStack, "setNBTString" );
//			}
		} 
		catch (Exception e) {
//			Output.get().logInfo(
//					"PrisonNBTUtil.setNBTString(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
	}
	
	public static boolean getNBTBoolean( ItemStack bukkitStack, String key ) {
		boolean results = false;
		
		try {
			Function<ReadableItemNBT, Boolean> gbFnc = nbt -> nbt.getBoolean(key);

			results = NBT.get(bukkitStack, gbFnc );
//		results = NBT.get(bukkitStack, nbt -> nbt.getBoolean(key));
		} 
		catch (Exception e) {
			// ignore exception... these are very common if there are no NBTs on the ItemStack
			
//			Output.get().logInfo(
//					"PrisonNBTUtil.getNBTBoolean(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
		
		return results;
	}
	
	public static void setNBTBoolean( ItemStack bukkitStack, String key, boolean value ) {
		
		try {
			NBT.modify(bukkitStack, nbt -> {
				nbt.setBoolean(key, value);
			});
			
//			if ( isEnableDebugLogging() ) {
//				nbtDebugLog( bukkitStack, "setNBTBoolean" );
//			}
		} 
		catch (Exception e) {
//			Output.get().logInfo(
//					"PrisonNBTUtil.setNBTBoolean(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
	}

	
	public static boolean hasNBTInt( ItemStack bukkitStack, String key ) {
		int results = -1;
		
		results = getNBTInt( bukkitStack, key );
		
		return results != -1;
	}
	
	
	public static int getNBTInt( ItemStack bukkitStack, String key ) {
		int results = -1;
		
		try {
			Function<ReadableItemNBT, Integer> gsFnc = nbt -> {
				Integer value = null;
				
				try {
					value = nbt.getInteger(key);
				} 
				catch (Exception e) {
				}
				
//				if ( Output.get().isDebug() ) {
//					Output.get().logInfo( "PrisonNBTUtil.getNBTString():ItemStack: %s", nbt.toString() );
//				}
				return value;
			};
			
			results = NBT.get(bukkitStack, gsFnc );
		}
		catch (Exception e) {
			// ignore exception... these are very common if there are no NBTs on the ItemStack
			
//			Output.get().logInfo(
//					"PrisonNBTUtil.getNBTInt(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
		
		return results;
	}
	
	public static boolean hasNBTString( Entity entity, String key ) {
		String results = null;
		
		results = getNBTString( entity, key );
		
		return results != null && results.trim().length() > 0;
	}
	public static String getNBTString( Entity entity, String key ) {
		String results = null;
		
		try {
			results = NBT.get(entity, nbt -> { 
				
			String value = null;
			
			try {
				Object vObj = nbt.getString(key);
				value = (String) vObj;
			} 
			catch (Exception e) {
			}
			
			return value;
		});
			
//			Function<ReadableNBT, String> gsFnc = nbt -> (String) nbt.getString(key);
			
//			results = NBT.get(entity, gsFnc );
//			results = NBT.get(bukkitStack, nbt -> nbt.getString(key));
		}
		catch (Exception e) {
			// ignore exception... these are very common if there are no NBTs on the ItemStack
			
//			Output.get().logInfo(
//					"PrisonNBTUtil.getNBTString(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
		
		return results;
	}
	public static void setNBTString( Entity entity, String key, String value ) {
		
		try {
			NBT.modify(entity, nbt -> {
				nbt.setString(key, value);
			});
				 
			if ( isEnableDebugLogging() ) {
				nbtDebugLog( entity, "setNBTString" );
			}
		} 
		catch (Exception e) {
//			Output.get().logInfo(
//					"PrisonNBTUtil.setNBTString(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
	}
	
	
	public static void nbtDebugLog( ItemStack bukkitStack, String desc ) {
		if ( Output.get().isDebug() ) {
			
			String nbtDebug = nbtDebugString( bukkitStack );
			
			int sysId = System.identityHashCode(bukkitStack);
			
			String message = Output.stringFormat( 
					"NBT %s ItemStack for %s: %s  sysId: %d", 
					desc,
					bukkitStack.hasItemMeta() && bukkitStack.getItemMeta().hasDisplayName() ? 
							bukkitStack.getItemMeta().getDisplayName() :
								bukkitStack.getType().name(),
								nbtDebug,
								sysId );
			
			Output.get().logInfo( message );
			
			//Output.get().logInfo( "NBT: " + new NBTItem( getBukkitStack() ) );
			
		}
	}
	
	public static void nbtDebugLog( Entity entity, String desc ) {
		if ( Output.get().isDebug() ) {
			
			int sysId = System.identityHashCode(entity);
			
			String message = Output.stringFormat( 
					"NBT %s ItemStack for %s:  sysId: %d", 
					desc,
					entity.getName(),
								sysId );
			
			Output.get().logInfo( message );
			
			//Output.get().logInfo( "NBT: " + new NBTItem( getBukkitStack() ) );
			
		}
	}
	
	
	public static String nbtDebugString( ItemStack bukkitStack ) {
		
		ReadWriteNBT nbtItem = NBT.itemStackToNBT(bukkitStack);
		return nbtItem.toString();
	}
	
	public static String nbtDebugString() {
		
		ReadWriteNBT nbtItem = NBT.createNBTObject();
		
//		ReadWriteNBT nbtItem = NBT.itemStackToNBT(bukkitStack);
		return nbtItem.toString();
	}
	
//    public NBTItem getNBT( ItemStack bukkitStack ) {
//    	NBTItem nbtItemStack = null;
//    	
//    	if ( bukkitStack != null && bukkitStack.getType() != Material.AIR  ) {
//    		try {
//				nbtItemStack = new NBTItem( bukkitStack, true );
//				
//				if ( isEnableDebugLogging() ) {
//					nbtDebugLog( nbtItemStack, "getNbt" );
//				}
//				
//			} catch (Exception e) {
//				// ignore - the bukkit item stack is not compatible with the NBT library
//			}
//    	}
//    	
//    	return nbtItemStack;
//    }
    
    
//    private void nbtDebugLog( NBTItem nbtItem, String desc ) {
//		if ( Output.get().isDebug() ) {
//			org.bukkit.inventory.ItemStack iStack = nbtItem.getItem();
//			
//			int sysId = System.identityHashCode(iStack);
//			
//			String message = Output.stringFormat( 
//					"NBT %s ItemStack for %s: %s  sysId: %d", 
//					desc,
//					iStack.hasItemMeta() && iStack.getItemMeta().hasDisplayName() ? 
//							iStack.getItemMeta().getDisplayName() :
//							iStack.getType().name(),
//					nbtItem.toString(),
//					sysId );
//			
//			Output.get().logInfo( message );
//			
//			//Output.get().logInfo( "NBT: " + new NBTItem( getBukkitStack() ) );
//			
//		}
//    }
    
    
//    public boolean hasNBTKey( NBTItem nbtItem, String key ) {
//    	boolean results = false;
//    	
//    	if ( nbtItem != null ) {
//    		results = nbtItem.hasKey( key );
//    	}
//    	
//    	return results;
//    }
    
//    private String getNBTString( NBTItem nbtItem, String key ) {
//    	String results = null;
//    	
//    	if ( nbtItem != null ) {
//    		results = nbtItem.getString( key );
//    	}
//    	return results;
//    }
    
//    private void setNBTString( NBTItem nbtItem, String key, String value ) {
//
//    	if ( nbtItem != null ) {
//    		nbtItem.setString( key, value );
//    		
//    		if ( isEnableDebugLogging() ) {
//    			nbtDebugLog( nbtItem, "setNBTString" );
//    		}
//    	}
//    }


	public static boolean isEnableDebugLogging() {
		return enableDebugLogging;
	}
	public static void setEnableDebugLogging(boolean enableDebugLogging) {
		PrisonNBTUtil.enableDebugLogging = enableDebugLogging;
	}
    
}

package tech.mcprison.prison.spigot.nbt;

	import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTType;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import tech.mcprison.prison.file.JsonFileIO;
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
				
				if ( nbt.hasNBTData() && nbt.hasTag( key ) ) {
					value = nbt.getString(key);
				}
				
				
//				try {
//					Object vObj = nbt.getString(key);
//					
//					if ( vObj != null ) {
//						value = (String) vObj;
//					}
//				} 
//				catch (Exception e) {
//				}
				
//				if ( Output.get().isDebug() ) {
//					Output.get().logInfo( "PrisonNBTUtil.getNBTString():ItemStack: %s", nbt.toString() );
//				}
				return value;
			};
			
			results = NBT.get(bukkitStack, gsFnc );
//			results = NBT.get(bukkitStack, nbt -> nbt.getString(key));
		}
		catch (NoClassDefFoundError | Exception e) {
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
	
	public static String getNBTData( Entity entity ) {
		String results = null;
		
		try {
			results = NBT.get(entity, nbt -> { 
				String  mString = null;
				
				
				
				TreeMap<String,Object> map = new TreeMap<>();

				JsonFileIO json = new JsonFileIO();
				
				try {
					String nbtDump = json.toString( nbt );
					String msg = String.format(
							"PrisonNBTUtil.getNBTStrings: json nbt dump: %s ",
							json.toString( nbtDump )
							);
					Output.get().logInfo( msg );
				}
				catch ( Exception e ) {
					Output.get().logWarn( "PrisonNBTUtil.getNBTData - Error:: " + e.getMessage() );
					e.fillInStackTrace();
				}
					
				try {
					Set<String> keys = nbt.getKeys();
					
					for (String key : keys) {
		
						NBTType type = nbt.getType(key);
						
						switch (type) {
							case NBTTagString:
							{
								String val = nbt.getString(key);
								map.put(key, val);
							}
							break;

							case NBTTagDouble:
							{
								Double val = nbt.getDouble(key);
								map.put(key, Double.toString(val) );
							}
							break;

							case NBTTagFloat:
							{
								Float val = nbt.getFloat(key);
								map.put(key, Float.toString(val) );
							}
							break;
							
							case NBTTagInt:
							{
								Integer val = nbt.getInteger(key);
								map.put(key, Integer.toString(val) );
							}
							break;
							
							case NBTTagLong:
							{
								Long val = nbt.getLong(key);
								map.put(key, Long.toString(val) );
							}
							break;
							
							case NBTTagShort:
							{
								Short val = nbt.getShort(key);
								map.put(key, Short.toString(val) );
							}
							break;
							
							case NBTTagByte:
							{
								Byte val = nbt.getByte(key);
								map.put(key, Byte.toString(val) );
							}
							break;
							
							case NBTTagIntArray:
							{
								int[] val = nbt.getIntArray(key);
								map.put(key, val.toString() );
							}
							break;
							
							case NBTTagByteArray:
							{
								byte[] val = nbt.getByteArray(key);
								map.put(key, val.toString() );
							}
							break;
							
							case NBTTagLongArray:
							{
								long[] val = nbt.getLongArray(key);
								map.put(key, val.toString() );
							}
							break;
							
							
//							case NBTTagList:
//							{
//								getNbtListValues( key, nbt, map );
//								
//							}
//							break;
							
						default:
							String msg = String.format(
									"PrisonNBTUtil.getNBTData: Unhandled NBT type: %s   NBT key: %s ",
									type.name(),
									key
									);
							Output.get().logInfo( msg );
							break;
						}
					}
					
					
					
					mString = json.toString(  map );
					
					
				} 
				catch (Exception e) {
					Output.get().logWarn( "PrisonNBTUtil.getNBTData: Error:: " + e.getMessage() );
					e.fillInStackTrace();
				}
				
				return mString;
			});
			
		}
		catch (Exception e) {
			Output.get().logWarn( "PrisonNBTUtil.getNBTData: Error: " + e.getMessage() );
			e.fillInStackTrace();

			// ignore exception... these are very common if there are no NBTs on the ItemStack
			
//			Output.get().logInfo(
//					"PrisonNBTUtil.getNBTString(): Failure trying to use NBTs. "
//					+ "Contact prison support. The NBT library may need to be updated. [%s]",
//					e.getMessage()
//					);
		}
		
		return results;
	}
	
//	private static void getNbtListValues( String key, ReadableNBT nbt, TreeMap<String, Object> map) {
//
//		try {
//			
//			NBTType listType = nbt.getListType(key);
//			
//			switch (listType) {
//			case NBTTagString:
//				map.put( key, nbt.getStringList(key) );
//				break;
//			case NBTTagInt:
//				map.put( key, nbt.getIntegerList(key) );
//				break;
//			case NBTTagIntArray:
//				map.put( key, nbt.getIntArrayList(key) );
//				break;
//				
//			case NBTTagDouble:
//				map.put( key, nbt.getDoubleList(key) );
//				break;
//			case NBTTagLong:
//				map.put( key, nbt.getLongList(key) );
//				break;
//			case NBTTagFloat:
//				map.put( key, nbt.getFloatList(key) );
//				break;
////		case NBTTagCompound:
////			map.put( key, nbt.getCompoundList(key) );
////			break;
//				
//			default:
//				String msg = String.format(
//						"PrisonNBTUtil.getNbtListValues: Unhandled NBT list type: %s   NBT key: %s ",
//						listType.name(),
//						key
//						);
//				Output.get().logInfo( msg );
//				break;
//			}
//		}
//		catch ( Exception e ) {
//
//			Output.get().logWarn( "PrisonNBTUtil.getNbtListValues: "
//					+ "nbt List key: " + key + " Error: " +
//					e.getMessage() );
//			e.fillInStackTrace();
//		}
//		
//	}

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
	
	 /**
     * Copies all custom NBT data from a source ItemStack to a target ItemStack.
     * This will overwrite any custom NBT tags on the target item that have the same key
     * as tags on the source item.
     *
     * @param sourceItem The ItemStack from which to copy NBT data.
     * @param targetItem The ItemStack to which to copy NBT data.
     * @return The modified target ItemStack.
     */
    public static ItemStack copyCustomNBT(ItemStack sourceItem, ItemStack targetItem) {
        if (sourceItem == null || sourceItem.getType() == Material.AIR) {
            return targetItem;
        }
        if (targetItem == null || targetItem.getType() == Material.AIR) {
            throw new IllegalArgumentException("Target ItemStack cannot be null or AIR for NBT copy.");
        }

        // 1. Get the custom NBT from the source item as an SNBT string.
        // The NBT.get() method for ItemStacks expects a Function<ReadableItemNBT, T>.
        // We cast the lambda explicitly to match this.
        final String sourceNbtString = NBT.get(sourceItem, (Function<ReadableItemNBT, String>) nbt -> nbt.toString());

        // 2. Parse the SNBT string into a new, independent ReadWriteNBT compound.
        // This is our deep copy of the source item's custom NBT.
        ReadWriteNBT sourceCustomNBT = NBT.parseNBT(sourceNbtString);

        // If sourceItem had no custom NBT (e.g., empty string after toString),
        // sourceCustomNBT.getKeys() will be empty. Handle this case.
        if (sourceCustomNBT.getKeys().isEmpty()) {
            return targetItem; // Nothing to copy
        }

        // 3. Modify the target item to merge the copied NBT.
        // NBT.modify() takes a Consumer<ReadWriteNBT> for ItemStacks.
        NBT.modify(targetItem, targetCustomNBT -> {
            targetCustomNBT.mergeCompound(sourceCustomNBT);
        });

        // The targetItem is modified in place.
        return targetItem;
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

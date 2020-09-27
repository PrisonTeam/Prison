package tech.mcprison.prison.spigot.compat;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.BlockType;

/**
 * <p>This function provides a way to cache BlockTypes and XMaterial
 * types so as to eliminate a high cost of access after the first time
 * the resource was accessed.
 * </p>
 *
 */
public class CompatibilityCache {
	
	public static final byte NO_DATA_VALUE = (byte) -1;
	
	public static final XMaterial NULL_TOKEN = XMaterial.VOID_AIR;
	
	
	private Map<String, BlockType> blockTypeCache;
	
	private Map<String, XMaterial> xMaterialCache;
	
	public CompatibilityCache() {
		super();
		
		this.blockTypeCache = new TreeMap<>();
		this.xMaterialCache = new TreeMap<>();
	}



	
	public BlockType getCachedBlockType( Block spigotBlock, byte data ) {
		String key = spigotBlock.getType().name() + ( data <= 0 ? "" : ":" +data);
		
		BlockType blockType = blockTypeCache.get( key );
		
//		return blockType == BlockType.NULL_BLOCK ? null : blockType;
		return blockType;
	}
	public void putCachedBlockType( Block spigotBlock, byte data, BlockType blockType ) {
		if ( spigotBlock != null ) {
			String key = spigotBlock.getType().name() + ( data <= 0 ? "" : ":" +data);
			
			if ( !blockTypeCache.containsKey( key ) ) {
				blockTypeCache.put( key, blockType == null ? BlockType.NULL_BLOCK : blockType );
			}
		}
	}
	
	
	public BlockType getCachedBlockType( ItemStack spigotStack, byte data ) {
		String key = spigotStack.getType().name() + ( data <= 0 ? "" : ":" +data);
		
		BlockType blockType = blockTypeCache.get( key );
		
//		return blockType == BlockType.NULL_BLOCK ? null : blockType;
		return blockType;
	}
	public void putCachedBlockType( ItemStack spigotStack, byte data, BlockType blockType ) {
		if ( spigotStack != null ) {
			String key = spigotStack.getType().name() + ( data <= 0 ? "" : ":" +data);
			
			if ( !blockTypeCache.containsKey( key ) ) {
				blockTypeCache.put( key, blockType == null ? BlockType.NULL_BLOCK : blockType );
			}
		}
	}
	
	
	
	
	public XMaterial getCachedXMaterial( PrisonBlock prisonBlock )
	{
		String key = prisonBlock.getBlockName();
		
		XMaterial xMat = xMaterialCache.get( key );
		
		// Using VOID_AIR as temp placeholder for null values:
//		return xMat == XMaterial.VOID_AIR ? null : xMat;
		return xMat;
	}
	
	public void putCachedXMaterial( PrisonBlock prisonBlock, XMaterial xMat )
	{
		String key = prisonBlock.getBlockName();
		
		if ( !xMaterialCache.containsKey( key ) ) {
			// Using VOID_AIR as temp placeholder for null values:
			xMaterialCache.put( key, xMat == null ? XMaterial.VOID_AIR : xMat );
		}
	}

	public XMaterial getCachedXMaterial( Block spigotBlock, byte data ) {
		String key = spigotBlock.getType().name() + ( data <= 0 ? "" : ":" +data);
		
		XMaterial xMat = xMaterialCache.get( key );
		
		// Using VOID_AIR as temp placeholder for null values:
//		return xMat == XMaterial.VOID_AIR ? null : xMat;
		return xMat;
	}
	public void putCachedXMaterial( Block spigotBlock, byte data, XMaterial xMat ) {
		String key = spigotBlock.getType().name() + ( data <= 0 ? "" : ":" +data);
		
		if ( !xMaterialCache.containsKey( key ) ) {
			// Using VOID_AIR as temp placeholder for null values:
			xMaterialCache.put( key, xMat == null ? XMaterial.VOID_AIR : xMat );
		}
	}
	
	public XMaterial getCachedXMaterial( BlockType blockType, byte data ) {
		String key = blockType.name() + ( data <= 0 ? "" : ":" +data);
		
		XMaterial xMat = xMaterialCache.get( key );
		
		// Using VOID_AIR as temp placeholder for null values:
//		return xMat == XMaterial.VOID_AIR ? null : xMat;
		return xMat;
	}
	public void putCachedXMaterial( BlockType blockType, byte data, XMaterial xMat ) {
		String key = blockType.name() + ( data <= 0 ? "" : ":" +data);
		
		if ( !xMaterialCache.containsKey( key ) ) {
			// Using VOID_AIR as temp placeholder for null values:
			xMaterialCache.put( key, xMat == null ? XMaterial.VOID_AIR : xMat );
		}
	}
	
}

package tech.mcprison.prison.spigot.utils;

import java.util.HashMap;

import tech.mcprison.prison.mines.features.MineTargetBlockKey;
import tech.mcprison.prison.spigot.block.SpigotBlock;

public class BlockUtils
{
	private static BlockUtils instance;
	
	private HashMap<MineTargetBlockKey, SpigotBlock> unbreakableBlockList;
	
	
	private BlockUtils() {
		super();
		
		this.unbreakableBlockList = new HashMap<>();
	}
	
	public static BlockUtils getInstance() {
		if ( instance == null ) {
			synchronized ( BlockUtils.class )
			{
				if ( instance == null ) {
					instance = new BlockUtils();
				}
			}
		}
		return instance;
	}
	

	public static boolean isBlockUnbreakable( SpigotBlock block ) {
		BlockUtils bu = getInstance();
		
		MineTargetBlockKey blockKey = new MineTargetBlockKey( block.getLocation() );
		
		return bu.getUnbreakableBlockList().containsKey( blockKey );
	}
	
	public static void addBlockUnbreakable( SpigotBlock block ) {
		BlockUtils bu = getInstance();
		
		MineTargetBlockKey blockKey = new MineTargetBlockKey( block.getLocation() );
		
		bu.getUnbreakableBlockList().put( blockKey, block );
	}
	
	public HashMap<MineTargetBlockKey, SpigotBlock> getUnbreakableBlockList() {
		return unbreakableBlockList;
	}

}

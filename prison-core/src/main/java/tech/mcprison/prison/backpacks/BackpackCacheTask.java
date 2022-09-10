package tech.mcprison.prison.backpacks;

import tech.mcprison.prison.cache.PlayerCacheRunnable;

public abstract class BackpackCacheTask
	extends PlayerCacheRunnable 
{
	private final BackpackCachePlayerData backpackData;
	
	public BackpackCacheTask( BackpackCachePlayerData backpackData ) {
		super();
	
		this.backpackData = backpackData;
	}

	public BackpackCachePlayerData getBackpackData() {
		return backpackData;
	}
}

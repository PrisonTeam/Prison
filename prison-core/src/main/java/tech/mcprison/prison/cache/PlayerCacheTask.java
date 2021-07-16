package tech.mcprison.prison.cache;

public abstract class PlayerCacheTask
	extends PlayerCacheRunnable
{
	private final PlayerCachePlayerData playerData;
	
	public PlayerCacheTask( PlayerCachePlayerData playerData ) {
		super();
	
		this.playerData = playerData;
	}

	public PlayerCachePlayerData getPlayerData() {
		return playerData;
	}
	
}

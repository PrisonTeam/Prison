package tech.mcprison.prison.internal;

import java.util.UUID;

public abstract class PlayerUtil
{
	
	private UUID playerUuid;
	
	public PlayerUtil( UUID playerUuid ) {
		super();
		
		this.playerUuid = playerUuid;
		
	}
	
	public abstract boolean isActive();
	
	public abstract double getMaxHealth();
	
	public abstract void setMaxHealth( double maxHealth );
	
	public abstract int getMaximumAir();
	
	public abstract int getRemainingAir();
	
	public abstract int getFoodLevel();
	
	public abstract double getExhaustion();
	
	public abstract double getExp();
	
	public abstract int getLevel();
	
	public abstract double getWalkSpeed();
	
	public abstract ItemStack getPrisonItemStack();
	
	public abstract int getItemInHandDurability();
	
	public abstract int getItemInHandDurabilityMax();

	public UUID getPlayerUuid() {
		return playerUuid;
	}
	public void setPlayerUuid( UUID playerUuid ) {
		this.playerUuid = playerUuid;
	}
}

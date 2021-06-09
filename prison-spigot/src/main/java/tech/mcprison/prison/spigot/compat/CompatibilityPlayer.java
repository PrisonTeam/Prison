package tech.mcprison.prison.spigot.compat;

import org.bukkit.entity.Player;

public interface CompatibilityPlayer {
	
	public double getMaxHealth( Player player );
	
	public void setMaxHealth( Player player, double maxHealth );
	
}

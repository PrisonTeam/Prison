package tech.mcprison.prison.spigot.compat;

import org.bukkit.entity.Player;

public abstract class Spigot18Player
		extends CompatibilityCache
		implements CompatibilityPlayer
{
	
	@SuppressWarnings( "deprecation" )
	public double getMaxHealth( Player player ) {
		double maxHealth = 0;
		
		if ( player != null ) {
			maxHealth = player.getMaxHealth();
		}
		
		return maxHealth;
	}
	
	@SuppressWarnings( "deprecation" )
	public void setMaxHealth( Player player, double maxHealth ) {
		if ( player != null ) {
			player.setMaxHealth( maxHealth );
		}
	}
	
}

package tech.mcprison.prison.spigot.compat;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public abstract class Spigot19Player
		extends CompatibilityCache
		implements CompatibilityPlayer
{
	public void setMaxHealth( Player player, double maxHealth ) {
		
		if ( player != null ) {
			AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
	        if( maxHealthAttribute != null ) {
	        	maxHealthAttribute.setBaseValue( maxHealth );
	        }
		}
	}
	
	
	public double getMaxHealth( Player player ) {
		double maxHealth = 0;
		
		if ( player != null ) {
			AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
	        if( maxHealthAttribute != null ) {
	        	maxHealth = maxHealthAttribute.getValue();
	        }
		}
		
		return maxHealth;
	}
}

package tech.mcprison.prison.spigot.compat;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class Spigot19Player
		extends Spigot18Blocks
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
	
	@Override
	@SuppressWarnings( "deprecation" )
	public void sendTitle( Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
		player.sendTitle( title, subtitle );
	}

	@Override
	public void sendActionBar( Player player, String actionBar ) {
		player.spigot().sendMessage( ChatMessageType.ACTION_BAR, 
					new TextComponent( actionBar ) );

//		player.sendTitle( "", actionBar );
	}
}

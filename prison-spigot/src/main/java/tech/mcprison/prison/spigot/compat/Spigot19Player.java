package tech.mcprison.prison.spigot.compat;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;

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
	//@SuppressWarnings( "deprecation" )
	public void sendTitle( Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
		
		//player.sendTitle( title, subtitle );
		
		Titles.sendTitle( player, fadeIn, stay, fadeOut, title, subtitle );
		
	}

	@Override
	public void sendActionBar( Player player, String actionBar ) {
		
		ActionBar.sendActionBar( player, actionBar );
		
		// Was using the following until it was replaced with XSeries' ActionBar:
//		player.spigot().sendMessage( ChatMessageType.ACTION_BAR, 
//					new TextComponent( actionBar ) );

//		player.sendTitle( "", actionBar );
	}
}

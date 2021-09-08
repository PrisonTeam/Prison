package tech.mcprison.prison.spigot.compat;

import org.bukkit.entity.Player;

public abstract class Spigot110Player
		extends Spigot19
{
	@Override
	public void sendTitle( Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
		player.sendTitle( title, subtitle, fadeIn, stay, fadeOut );
	}

	
	// NOTE: Just use the Spigot19Player class's sendActionBar():
//	@Override
//	public void sendActionBar( Player player, String actionBar ) {
//		player.spigot().sendMessage( ChatMessageType.ACTION_BAR, 
//				new TextComponent( actionBar ) );
//
////		player.sendTitle( null, actionBar );
//	}
}

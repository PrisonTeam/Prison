package tech.mcprison.prison.spigot.compat;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class Spigot110Player
		extends Spigot19
{
	@Override
	public void sendTitle( Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
		player.sendTitle( title, subtitle, fadeIn, stay, fadeOut );
	}

	@Override
	public void sendActionBar( Player player, String actionBar ) {
		player.spigot().sendMessage( ChatMessageType.ACTION_BAR, 
				new TextComponent( actionBar ) );

//		player.sendTitle( null, actionBar );
	}
}

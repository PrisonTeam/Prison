package tech.mcprison.prison.spigot.compat;

import org.bukkit.entity.Player;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;

import tech.mcprison.prison.util.Text;

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
	
	/**
	 * Provides display of the Player's title and subtitles. Spigot 1.8 and 1.9 did not
	 * support the fadeIn, stay, and fadeOut parameters.
	 * 
	 * @param player
	 * @param title
	 * @param subtitle
	 * @param fadeIn - parameter ignored
	 * @param stay - parameter ignored
	 * @param fadeOut - parameter ignored
	 */
	@Override
	public void sendTitle( Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
		//player.sendTitle( title, subtitle );
		
		title = title == null ? null : Text.translateAmpColorCodes( title );
		subtitle = subtitle == null ? null : Text.translateAmpColorCodes( subtitle );

		Titles.sendTitle( player, fadeIn, stay, fadeOut, title, subtitle );
		
	}

	/**
	 * Provides pseudo-actionBar, which did not exist on spigot 1.8.8, so this instead uses
	 * the subtitle.  Limitation is that you cannot then use actionBar and title and subtitle 
	 * at the same time, but not much uses these components yet.
	 * 
	 * @param player
	 * @param actionBar
	 */
	@Override
	public void sendActionBar( Player player, String actionBar ) {
		
		String message = Text.translateAmpColorCodes( actionBar );
		ActionBar.sendActionBar( player, message );
		
		// Was using the following until it was replaced with XSeries' ActionBar:
//		player.sendTitle( "", actionBar );

		// The following class does not exist under spigot 1.8.8
//		player.spigot().sendMessage( ChatMessageType.ACTION_BAR, 
//						new TextComponent( actionBar ) );
	}
	

}

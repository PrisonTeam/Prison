package tech.mcprison.prison.spigot.compat;

import org.bukkit.entity.Player;

public interface CompatibilityPlayer {
	
	public double getMaxHealth( Player player );
	
	public void setMaxHealth( Player player, double maxHealth );
	
    
    /**
     * 
     * org.bukkit.Player Titles
     * <ul>
     *   <li>Bukkit 1.8 - no method</li>
     *   <li>Bukkit 1.8.8 - deprecated: sendTitle( title, subtitle), deprecated: resetTitle()</li>
     *   <li>Bukkit 1.9 - deprecated: sendTitle( title, subtitle), deprecated: resetTitle()</li>
     *   <li>Bukkit 1.10 - deprecated: sendTitle( title, subtitle), deprecated: resetTitle(), 
     *   				sendTitle(title, subtitle, fadeIn, stay, fadeOut), resetTitle()</li>
     *   <li>Bukkit 1.11 - same </li>
     *   <li>Bukkit 1.12 - same, hideTitle(), sendTitle(), deprecated: setTitleTimes(), updateTitle() </li>
     *   <li>Bukkit 1.13 - same </li>
     *   <li>Bukkit 1.14 - same </li>
     *   <li>Bukkit 1.15 - same </li>
     *   <li>Bukkit 1.16 - same </li>
     *   <li>Bukkit 1.17 - same </li>
     * </ul>
     * 
     * <p>NOTE: For actionBar, it is Paper only or bungee, starting with 1.12 with sendActionBar().  Then with
     * 1.15 it was deprecated and suggested to use Audience.sendActionBar().  There is no comparable
     * function for bukkit.</p>
     * 
     * @param title
     * @param subtitle
     * @param fadeIn
     * @param stay
     * @param fadeOut
     */
	public void sendTitle( Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut );

	public void sendActionBar( Player player, String actionBar );
	
}

package tech.mcprison.prison.spigot.placeholder;

import java.util.function.Function;

import org.bukkit.Bukkit;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * Register the non-suppressed place holders for prison.
 */
public class MVdWPlaceholderIntegrationWrapper
{
	
	public MVdWPlaceholderIntegrationWrapper(String providerName) {
		super();
	}

    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
    	PlaceholderAPI.registerPlaceholder(
    			Bukkit.getPluginManager().getPlugin("Prison"), placeholder, 
    					e -> action.apply(new SpigotPlayer(e.getPlayer())));
    }

}

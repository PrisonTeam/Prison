package tech.mcprison.prison.spigot.placeholder;

import java.util.function.Function;

import org.bukkit.Bukkit;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.Text;

/**
 * Register the non-suppressed place holders for prison.
 */
public class MVdWPlaceholderIntegrationWrapper
{
	
	public MVdWPlaceholderIntegrationWrapper(String providerName) {
		super();
		
        PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
        for ( PrisonPlaceHolders placeHolder : PrisonPlaceHolders.values() ) {
        	if ( !placeHolder.isSuppressed() ) {
        		registerPlaceholder(placeHolder.name(),
        				player -> Text.translateAmpColorCodes(
        						pm.getTranslatePlayerPlaceHolder( player.getUUID(), placeHolder.name() )
        						));
        	}
		}

	}
	
    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
    	PlaceholderAPI.registerPlaceholder(
    			Bukkit.getPluginManager().getPlugin("Prison"), placeholder, 
    					e -> action.apply(new SpigotPlayer(e.getPlayer())));
    }

}

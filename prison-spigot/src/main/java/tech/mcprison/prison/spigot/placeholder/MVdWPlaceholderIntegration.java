package tech.mcprison.prison.spigot.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import tech.mcprison.prison.integration.PlaceholderIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.util.function.Function;

public class MVdWPlaceholderIntegration implements PlaceholderIntegration {

    public static final String PROVIDER_NAME = "MVdWPlaceholderAPI";
	private boolean pluginInstalled;

    public MVdWPlaceholderIntegration() {
        pluginInstalled = Bukkit.getPluginManager().isPluginEnabled(PROVIDER_NAME);
    }

    @Override
    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
        if (hasIntegrated()) {
        	PlaceholderAPI.registerPlaceholder(Bukkit.getPluginManager().getPlugin("Prison"), placeholder, 
        			e -> action.apply(new SpigotPlayer(e.getPlayer())));
        }
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean hasIntegrated() {
        return pluginInstalled;
    }

}

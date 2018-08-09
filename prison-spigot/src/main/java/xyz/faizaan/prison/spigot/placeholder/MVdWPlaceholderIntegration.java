package xyz.faizaan.prison.spigot.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import xyz.faizaan.prison.integration.PlaceholderIntegration;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.spigot.game.SpigotPlayer;

import java.util.function.Function;

public class MVdWPlaceholderIntegration implements PlaceholderIntegration {

    private boolean pluginInstalled;

    public MVdWPlaceholderIntegration() {
        pluginInstalled = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
    }

    @Override
    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
        if (!hasIntegrated()) return;
        PlaceholderAPI.registerPlaceholder(Bukkit.getPluginManager().getPlugin("Prison"), placeholder, e -> action.apply(new SpigotPlayer(e.getPlayer())));
    }

    @Override
    public String getProviderName() {
        return "MVdWPlaceholderAPI";
    }

    @Override
    public boolean hasIntegrated() {
        return pluginInstalled;
    }

}

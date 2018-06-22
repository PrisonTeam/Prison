package me.faizaand.prison.spigot.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.faizaand.prison.integration.PlaceholderIntegration;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.spigot.game.SpigotPlayer;
import org.bukkit.Bukkit;

import java.util.function.Function;

public class MVdWPlaceholderIntegration implements PlaceholderIntegration {

    private boolean pluginInstalled;

    public MVdWPlaceholderIntegration() {
        pluginInstalled = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
    }

    @Override
    public void registerPlaceholder(String placeholder, Function<GamePlayer, String> action) {
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

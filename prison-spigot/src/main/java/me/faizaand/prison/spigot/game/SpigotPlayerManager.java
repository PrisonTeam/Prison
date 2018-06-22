package me.faizaand.prison.spigot.game;

import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.platform.PlayerManager;
import me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * We don't want to create new player instances each time the API requests a
 * player, so we'll store them here.
 *
 * @since 4.0
 */
public class SpigotPlayerManager implements PlayerManager {

    private Map<UUID, GamePlayer> players;

    SpigotPlayerManager() {
        this.players = new HashMap<>();

        Events.subscribe(PlayerJoinEvent.class).handler(e ->
                players.put(e.getPlayer().getUniqueId(),
                        getPlayerInstance(e.getPlayer())));

        Events.subscribe(PlayerQuitEvent.class).handler(e -> players.remove(e.getPlayer().getUniqueId()));
    }

    public Optional<GamePlayer> getPlayer(UUID uid) {
        return Optional.ofNullable(players.get(uid));
    }

    public Optional<GamePlayer> getPlayer(String name) {
        return players.values().stream().filter(spigotPlayer -> spigotPlayer.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public List<GamePlayer> getOnlinePlayers() {
        return new ArrayList<>(players.values());
    }

    private SpigotPlayer getPlayerInstance(Player bukkitPlayer) {
        return new SpigotPlayer(bukkitPlayer);
    }

}

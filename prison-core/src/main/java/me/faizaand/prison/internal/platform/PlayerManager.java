package me.faizaand.prison.internal.platform;

import me.faizaand.prison.internal.GamePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Allows us to store and access instances of players.
 *
 * @since 4.0
 */
public interface PlayerManager {

    Optional<GamePlayer> getPlayer(UUID uid);

    Optional<GamePlayer> getPlayer(String name);

    List<GamePlayer> getOnlinePlayers();

}

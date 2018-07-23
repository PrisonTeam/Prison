package me.faizaand.prison.internal.platform;

import me.faizaand.prison.internal.GameWorld;

import java.util.List;
import java.util.Optional;

/**
 * Provides instances of the worlds on the server.
 */
public interface WorldManager {

    Optional<GameWorld> getWorld(String name);

    List<GameWorld> getWorlds();

}

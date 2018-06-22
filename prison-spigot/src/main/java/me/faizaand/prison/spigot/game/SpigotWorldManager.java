package me.faizaand.prison.spigot.game;

import me.faizaand.prison.internal.GameWorld;
import me.faizaand.prison.internal.platform.WorldManager;
import me.lucko.helper.Events;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.*;

public class SpigotWorldManager implements WorldManager {

    private Map<String, GameWorld> worlds;

    public SpigotWorldManager() {
        this.worlds = new HashMap<>();

        for (World world : Bukkit.getWorlds()) {
            worlds.put(world.getName(), new SpigotWorld(world));
        }

        // look for any new worlds during game (due to a multiworld plugin or something)
        // and adds them to our little list
        Events.subscribe(WorldLoadEvent.class).handler(e -> worlds.put(
                e.getWorld().getName(), new SpigotWorld(e.getWorld())));

        // look for unloading worlds, we don't want them in our list
        Events.subscribe(WorldUnloadEvent.class).handler(e -> worlds.remove(e.getWorld().getName()));
    }

    @Override
    public Optional<GameWorld> getWorld(String name) {
        if (worlds.containsKey(name)) {
            return Optional.of(worlds.get(name));
        }

        if (Bukkit.getWorld(name) == null) {
            return Optional.empty(); // Avoid NPE
        }
        SpigotWorld newWorld = new SpigotWorld(Bukkit.getWorld(name));
        worlds.put(newWorld.getName(), newWorld);
        return Optional.of(newWorld);
    }

    @Override
    public List<GameWorld> getWorlds() {
        return new ArrayList<>(worlds.values());
    }

}

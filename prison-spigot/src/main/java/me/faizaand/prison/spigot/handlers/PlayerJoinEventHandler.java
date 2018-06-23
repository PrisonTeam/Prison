package me.faizaand.prison.spigot.handlers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventHandler;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.events.Subscription;
import me.lucko.helper.Events;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventHandler extends EventHandler {

    public PlayerJoinEventHandler() {
        Events.subscribe(PlayerJoinEvent.class).handler(e -> {
            Object[] obj = new Object[EventType.PlayerJoinEvent.getExpectedTypes().length];
            obj[0] = Prison.get().getPlatform().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()).get();
            obj[1] = e.getJoinMessage();

            for (Subscription sub : getSubscriptions()) {
                sub.getCallback().apply(obj);
            }
        });

        Prison.get().getEventManager().registerHandler(EventType.PlayerJoinEvent, this);
    }

}

package me.faizaand.prison.spigot.handlers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventException;
import me.faizaand.prison.events.EventHandler;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.events.Subscription;
import me.lucko.helper.Events;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEventHandler extends EventHandler {

    public PlayerChatEventHandler() {
        Events.subscribe(AsyncPlayerChatEvent.class).handler(e -> {
            Object[] objects = new Object[EventType.PlayerChatEvent.getExpectedTypes().length];

            objects[0] = e.getMessage();
            objects[1] = e.getFormat();
            objects[2] = Prison.get().getPlatform().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()).get();

            for (Subscription sub : getSubscriptions()) {

                Object[] ret = sub.getCallback().apply(objects);
                if (ret == null) e.setCancelled(true);
                else if (ret.length != 0) {
                    // we have values to set, make sure we have them all
                    if (ret.length != EventType.PlayerChatEvent.getExpectedTypes().length)
                        throw new EventException("not enough objects were returned");

                    // good, now it's a matter of setting them
                    try {
                        e.setMessage((String) ret[0]);
                        e.setFormat((String) ret[1]);
                    } catch(ClassCastException ex) {
                        throw new EventException("type mismatch when attempting to set event results; check the order of your returned objects", ex);
                    }
                }
            }
        });
    }

}

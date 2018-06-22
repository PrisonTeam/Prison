package me.faizaand.prison.spigot.handlers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventHandler;
import me.faizaand.prison.events.Subscription;
import me.faizaand.prison.internal.GamePlayer;
import me.lucko.helper.Events;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventHandler extends EventHandler {

    public PlayerJoinEventHandler() {
        Events.subscribe(PlayerJoinEvent.class).handler(e -> {
            for (Subscription sub : getSubscriptions()) {
                Class<?>[] types = sub.getTypes();
                Object[] obj = new Object[types.length];
                for (int i = 0; i < obj.length; i++) {
                    Class<?> type = types[i];
                    if (type == GamePlayer.class) {
                        obj[i] = Prison.get().getPlatform().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
                    } else if (type == String.class) {
                        obj[i] = e.getJoinMessage();
                    }
                }

                sub.getCallback().apply(obj);
            }
        });
    }

}

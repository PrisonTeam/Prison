package me.faizaand.prison.spigot.handlers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventHandler;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.events.Subscription;
import me.faizaand.prison.spigot.SpigotUtil;
import me.lucko.helper.Events;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventHandler extends EventHandler {

    public PlayerInteractEventHandler() {

        Events.subscribe(PlayerInteractEvent.class)
                .filter(event -> event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                .handler(event -> {

                    Object[] objects = new Object[EventType.PlayerInteractBlockEvent.getExpectedTypes().length];
                    objects[0] = SpigotUtil.bukkitItemStackToPrison(event.getItem());
                    objects[1] = Prison.get().getPlatform().getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).get();
                    objects[2] = event.getAction() == Action.LEFT_CLICK_BLOCK;
                    objects[3] = SpigotUtil.bukkitLocationToPrison(event.getClickedBlock().getLocation());

                    for (Subscription sub : getSubscriptions()) {
                        Object[] ret = sub.getCallback().apply(objects);
                        if (ret == null) event.setCancelled(true);
                    }

                });
    }
}

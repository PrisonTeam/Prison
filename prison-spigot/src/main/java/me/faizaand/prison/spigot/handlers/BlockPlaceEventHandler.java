package me.faizaand.prison.spigot.handlers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventException;
import me.faizaand.prison.events.EventHandler;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.events.Subscription;
import me.faizaand.prison.spigot.SpigotUtil;
import me.lucko.helper.Events;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceEventHandler extends EventHandler {

    public BlockPlaceEventHandler() {
        Events.subscribe(BlockPlaceEvent.class).handler(e -> {
            for (Subscription sub : getSubscriptions()) {
                Object[] objects = new Object[EventType.BlockPlaceEvent.getExpectedTypes().length];
                objects[0] = Prison.get().getPlatform().getWorldManager().getWorld(e.getBlock().getWorld().getName()).get().getBlockAt(
                        SpigotUtil.bukkitLocationToPrison(e.getBlockPlaced().getLocation())
                );
                objects[1] = Prison.get().getPlatform().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()).get();
                objects[2] = SpigotUtil.bukkitItemStackToPrison(e.getItemInHand());
                objects[3] = e.canBuild();

                Object[] ret = sub.getCallback().apply(objects);
                if (ret == null) e.setCancelled(true);
                else if (ret.length != 0) {
                    // we have values to set, make sure we have them all
                    if (ret.length != EventType.BlockBreakEvent.getExpectedTypes().length)
                        throw new EventException("not enough objects were returned");

                    // good, now it's a matter of setting them
                    try {
                        e.setBuild((boolean) ret[3]);
                    } catch(ClassCastException ex) {
                        throw new EventException("type mismatch when attempting to set event results; check the order of your returned objects", ex);
                    }
                }
            }

        });
    }

}

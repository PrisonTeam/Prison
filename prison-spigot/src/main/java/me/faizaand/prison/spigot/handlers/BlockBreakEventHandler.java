package me.faizaand.prison.spigot.handlers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventHandler;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.events.Subscription;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.block.Block;
import me.faizaand.prison.spigot.SpigotUtil;
import me.lucko.helper.Events;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventHandler extends EventHandler {

    public BlockBreakEventHandler() {
        Events.subscribe(BlockBreakEvent.class).handler(e -> {
            for (Subscription sub : getSubscriptions()) {
                Class<?>[] types = sub.getTypes();
                Object[] obj = new Object[types.length];
                for (int i = 0; i < obj.length; i++) {
                    Class<?> type = types[i];
                    if (type == GamePlayer.class) {
                        obj[i] = Prison.get().getPlatform().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
                    } else if (type == Block.class) {
                        obj[i] = Prison.get().getPlatform().getWorldManager().getWorld(e.getBlock().getWorld().getName()).get().getBlockAt(
                                SpigotUtil.bukkitLocationToPrison(e.getBlock().getLocation())
                        );
                    }
                }
                e.setCancelled(sub.getCallback().apply(obj));
            }
        });

        Prison.get().getEventManager().registerHandler(EventType.BlockBreakEvent, this);
    }
}

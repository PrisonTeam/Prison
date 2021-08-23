package tech.mcprison.prison.cache;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.internal.events.player.PlayerKickEvent;
import tech.mcprison.prison.internal.events.player.PlayerQuitEvent;

/**
 * <p>This PlayerCacheEvents class self-registers these events with 
 * prison's event bus.
 * </p>
 * 
 * @author RoyalBlueRanger
 *
 */
public class PlayerCacheEvents
		 {
	
	public PlayerCacheEvents() {
		super();
		
		Prison.get().getEventBus().register(this);
	}

	// NOTE:  These are the bukkit event listeners that within the spigot module.
	//        They are included here to better express what these are for.
//    @EventHandler 
//    public void onPlayerJoin(PlayerJoinEvent e) {
//        Prison.get().getEventBus().post(
//            new tech.mcprison.prison.internal.events.player.PlayerJoinEvent(
//                new SpigotPlayer(e.getPlayer())));
//    }
//
//    @EventHandler 
//    public void onPlayerQuit(PlayerQuitEvent e) {
//        Prison.get().getEventBus().post(
//            new tech.mcprison.prison.internal.events.player.PlayerQuitEvent(
//                new SpigotPlayer(e.getPlayer())));
//    }
//    @EventHandler 
//    public void onPlayerKicked(PlayerKickEvent e) {
//        Prison.get().getEventBus().post(
//            new tech.mcprison.prison.internal.events.player.PlayerKickEvent(
//                new SpigotPlayer(e.getPlayer()), e.getReason()));
//    }
    
    @Subscribe
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	PlayerCache.getInstance().submitAsyncLoadPlayer( player );
    }

    @Subscribe
    public void onPlayerQuit(PlayerQuitEvent event) {
    	
     	Player player = event.getPlayer();
    	PlayerCache.getInstance().submitAsyncUnloadPlayer( player );
    }

    @Subscribe
    public void onPlayerKicked(PlayerKickEvent event) {
    	
    	Player player = event.getPlayer();
    	PlayerCache.getInstance().submitAsyncUnloadPlayer( player );
    }
}

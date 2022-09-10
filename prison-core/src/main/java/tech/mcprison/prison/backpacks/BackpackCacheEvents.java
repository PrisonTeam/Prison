package tech.mcprison.prison.backpacks;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.internal.events.player.PlayerKickEvent;
import tech.mcprison.prison.internal.events.player.PlayerQuitEvent;

public class BackpackCacheEvents {

	public BackpackCacheEvents() {
		super();
		
		Prison.get().getEventBus().register(this);
	}
	
	
    @Subscribe
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	BackpackCache.getInstance().submitAsyncLoadPlayer( player );
    }

    @Subscribe
    public void onPlayerQuit(PlayerQuitEvent event) {
    	
     	Player player = event.getPlayer();
     	BackpackCache.getInstance().submitAsyncUnloadPlayer( player );
    }

    @Subscribe
    public void onPlayerKicked(PlayerKickEvent event) {
    	
    	Player player = event.getPlayer();
    	BackpackCache.getInstance().submitAsyncUnloadPlayer( player );
    }
}

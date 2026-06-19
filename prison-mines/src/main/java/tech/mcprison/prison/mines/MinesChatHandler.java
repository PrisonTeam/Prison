package tech.mcprison.prison.mines;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;

public class MinesChatHandler {

    public MinesChatHandler() {
        Prison.get().getEventBus().register(this);
    }


    @Subscribe
    public void onPlayerChat(PlayerChatEvent e) {
	    	String newFormat = e.getFormat();
	
	    	Player player = e.getPlayer();
	    	
	    	// Now translates 
	    	
	    	String translated = Prison.get().getPlatform().getPlaceholders()
	    							.placeholderTranslateText( player.getUUID(), player.getName(), newFormat );
	    	
	    	e.setFormat( translated );
	    	
    }
}

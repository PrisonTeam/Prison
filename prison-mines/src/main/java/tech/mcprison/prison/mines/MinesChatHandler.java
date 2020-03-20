package tech.mcprison.prison.mines;

import java.util.List;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.integration.PlaceHolderKey;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.util.Text;

public class MinesChatHandler {

    public MinesChatHandler() {
        Prison.get().getEventBus().register(this);
    }


    @Subscribe
    public void onPlayerChat(PlayerChatEvent e) {
    	String newFormat = e.getFormat();
    	MineManager mm = PrisonMines.getInstance().getMineManager();
    	
    	List<PlaceHolderKey> placeholderKeys = mm.getTranslatedPlaceHolderKeys();
    	
    	for ( PlaceHolderKey placeHolderKey : placeholderKeys ) {
    		String key = "{" + placeHolderKey.getKey() + "}";
    		if ( newFormat.contains( key )) {
    			newFormat = newFormat.replace(key, Text.translateAmpColorCodes(
    					mm.getTranslateMinesPlaceHolder( placeHolderKey ) ));
    		}
    	}
        
        e.setFormat(newFormat);
    }
}

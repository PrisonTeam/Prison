package tech.mcprison.prison.ranks;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;

/**
 * Handles replacing chat messages for all players.
 *
 * @author Faizaan A. Datoo
 */
public class ChatHandler {

    /*
     * Constructor
     */

    public ChatHandler() {
        Prison.get().getEventBus().register(this);
        
        // This is pushed back in to the place holder integrations:
//        Optional<Integration> placeholderIntegration = Prison.get().getIntegrationManager().getForType(IntegrationType.PLACEHOLDER);
//        if (placeholderIntegration.isPresent()) {
//            PlaceholderIntegration integration = ((PlaceholderIntegration) placeholderIntegration.get());
//            
//            PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
//            for ( PrisonPlaceHolders placeHolder : PrisonPlaceHolders.values() ) {
//            	if ( !placeHolder.isSuppressed() ) {
//            		integration.registerPlaceholder(placeHolder.name(),
//            				player -> Text.translateAmpColorCodes(
//            						pm.getTranslatePlayerPlaceHolder( player.getUUID(), placeHolder.name() )
//            				));
//            	}
//			}
            
////            integration.registerPlaceholder("PRISON_RANK",
////                    player -> Text.translateAmpColorCodes(getPrefix(player.getUUID())));
//        }
    }

    /*
     * Listeners
     */

    @Subscribe
    public void onPlayerChat(PlayerChatEvent e) {
    	String newFormat = e.getFormat();

//    	Output.get().logDebug( "ChatHandler.onPlayerChat: before: %s", newFormat.replace( "%", "^" ) );
    	
    	
    	Player player = e.getPlayer();
    	
    	String results = Prison.get().getPlatform().getPlaceholders()
    							.placeholderTranslateText( player.getUUID(), player.getName(), newFormat );

//    	Output.get().logDebug( "ChatHandler.onPlayerChat: after: %s", results.replace( "%", "^" ) );
    	
    	e.setFormat( results );
    	
    }


}

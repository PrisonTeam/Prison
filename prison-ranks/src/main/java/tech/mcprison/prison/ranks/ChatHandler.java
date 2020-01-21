package tech.mcprison.prison.ranks;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.util.Text;

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
    	PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    	
    	for ( PrisonPlaceHolders placeHolder : PrisonPlaceHolders.values() ) {
    		String key = "{" + placeHolder.name() + "}";
    		if ( newFormat.contains( key )) {
    			newFormat = newFormat.replace(key, Text.translateAmpColorCodes(
    					pm.getTranslatePlayerPlaceHolder( e.getPlayer().getUUID(), placeHolder.name() ) ));
    		}
    	}
        
//        String prefix = getPrefix(e.getPlayer().getUUID());
//        String newFormat = e.getFormat().replace("{PRISON_RANK}", Text.translateAmpColorCodes(prefix));
        e.setFormat(newFormat);
    }

//    /*
//     * Util
//     */
//
//    private String getPrefix(UUID uid) {
//        Optional<RankPlayer> player =
//                PrisonRanks.getInstance().getPlayerManager().getPlayer(uid);
//        String prefix = "";
//
//        if (player.isPresent() && !player.get().getRanks().isEmpty()) {
//            StringBuilder builder = new StringBuilder();
//            for (Map.Entry<RankLadder, Rank> entry : player.get().getRanks().entrySet()) {
//                builder.append(entry.getValue().tag);
//            }
//            prefix = builder.toString();
//        }
//
//        return prefix;
//    }

}

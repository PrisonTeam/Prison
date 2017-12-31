package tech.mcprison.prison.ranks;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.util.Text;

import java.util.Map;
import java.util.Optional;

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
    }

    /*
     * Listeners
     */

    @Subscribe public void onPlayerChat(PlayerChatEvent e) {
        Optional<RankPlayer> player =
            PrisonRanks.getInstance().getPlayerManager().getPlayer(e.getPlayer().getUUID());
        String prefix = "";

        if(player.isPresent()) {
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<RankLadder, Rank> entry: player.get().getRanks().entrySet()) {
                builder.append(entry.getValue().tag);
            }
            prefix = builder.toString();
        }


        String newFormat = e.getFormat().replace("{PRISON_RANK}", Text.translateAmpColorCodes(prefix));
        e.setFormat(newFormat);
    }

}

package me.faizaand.prison.ranks;

import com.google.common.eventbus.Subscribe;
import me.faizaand.prison.Prison;
import me.faizaand.prison.integration.Integration;
import me.faizaand.prison.integration.IntegrationType;
import me.faizaand.prison.integration.PlaceholderIntegration;
import me.faizaand.prison.internal.events.player.PlayerChatEvent;
import me.faizaand.prison.ranks.data.Rank;
import me.faizaand.prison.ranks.data.RankLadder;
import me.faizaand.prison.ranks.data.RankPlayer;
import me.faizaand.prison.util.Text;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        Optional<Integration> placeholderIntegration = Prison.get().getIntegrationManager().getForType(IntegrationType.PLACEHOLDER);
        if (placeholderIntegration.isPresent()) {
            PlaceholderIntegration integration = ((PlaceholderIntegration) placeholderIntegration.get());
            integration.registerPlaceholder("PRISON_RANK",
                    player -> Text.translateAmpColorCodes(getPrefix(player.getUUID())));
        }
    }

    /*
     * Listeners
     */

    @Subscribe
    public void onPlayerChat(PlayerChatEvent e) {

        String prefix = getPrefix(e.getPlayer().getUUID());
        String newFormat = e.getFormat().replace("{PRISON_RANK}", Text.translateAmpColorCodes(prefix));
        e.setFormat(newFormat);
    }

    /*
     * Util
     */

    private String getPrefix(UUID uid) {
        Optional<RankPlayer> player =
                PrisonRanks.getInstance().getPlayerManager().getPlayer(uid);
        String prefix = "";

        if (player.isPresent() && !player.get().getRanks().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<RankLadder, Rank> entry : player.get().getRanks().entrySet()) {
                builder.append(entry.getValue().tag);
            }
            prefix = builder.toString();
        }

        return prefix;
    }

}

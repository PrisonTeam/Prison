package me.faizaand.prison.ranks;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventPriority;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.integration.Integration;
import me.faizaand.prison.integration.IntegrationType;
import me.faizaand.prison.integration.PlaceholderIntegration;
import me.faizaand.prison.internal.GamePlayer;
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

        // Register our placeholders
        // TODO Revamp placeholders
        Optional<Integration> placeholderIntegration = Prison.get().getIntegrationManager().getForType(IntegrationType.PLACEHOLDER);
        if (placeholderIntegration.isPresent()) {
            PlaceholderIntegration integration = ((PlaceholderIntegration) placeholderIntegration.get());
            integration.registerPlaceholder("PRISON_RANK",
                    player -> Text.translateAmpColorCodes(getPrefix(player.getUUID())));
        }

        listenPlayerChat();

    }

    /*
     * Listeners
     */

    private void listenPlayerChat() {
        Prison.get().getEventManager().subscribe(EventType.PlayerChatEvent, objects -> {
            String format = (String) objects[1];
            GamePlayer player = (GamePlayer) objects[2];

            String prefix = getPrefix(player.getUUID());
            format = format.replace("{PRISON_RANK}", Text.translateAmpColorCodes(prefix));

            return new Object[] {objects[0], format, objects[2]};

        }, EventPriority.NORMAL);
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

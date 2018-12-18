/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.ranks;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.integration.PlaceholderIntegration;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.util.Text;

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

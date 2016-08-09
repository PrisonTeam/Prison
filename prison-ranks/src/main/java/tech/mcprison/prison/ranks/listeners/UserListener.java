/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.ranks.listeners;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.events.PlayerChatEvent;
import tech.mcprison.prison.internal.events.PlayerJoinEvent;
import tech.mcprison.prison.internal.events.PlayerQuitEvent;
import tech.mcprison.prison.ranks.RankUser;
import tech.mcprison.prison.ranks.RanksModule;

/**
 * @author Camouflage100
 */
public class UserListener {

    private RanksModule ranksModule;

    public UserListener(RanksModule ranksModule) {
        this.ranksModule = ranksModule;
    }

    public void init() {
        Prison.getInstance().getEventBus().register(this);
    }

    @Subscribe public void onPlayerJoin(PlayerJoinEvent e) {
        try {
            if (ranksModule.getUser(e.getPlayer().getUUID()) == null) {
                ranksModule.saveRankUser(
                    new RankUser(e.getPlayer().getUUID(), ranksModule.getBottomRank()));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Subscribe public void onPlayerLeave(PlayerQuitEvent e) {
        try {
            ranksModule.saveRankUser(ranksModule.getUser(e.getPlayer().getUUID()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Subscribe public void onPlayerChat(PlayerChatEvent e) {
        try {
            String tag = ranksModule.getUser(e.getPlayer().getUUID()).getRank().getTag();
            e.setFormat(e.getFormat().replace("{PRISON-RANK-TAG}", tag));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}

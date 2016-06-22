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

package io.github.prison;

import com.google.common.eventbus.Subscribe;
import io.github.prison.gui.Button;
import io.github.prison.internal.events.PlayerJoinEvent;
import io.github.prison.util.Block;

/**
 * @author SirFaizdat
 */
public class PrisonListener {

    @Subscribe
    public void onPlayerJoin(PlayerJoinEvent e) {
        Prison.getInstance().getPlatform().getScheduler().runTaskLater(() -> {
            try {
                Prison.getInstance().getPlatform().createGUI("My GUI", 1)
                        .addButton(3, new Button(Block.RED_STAINED_GLASS_PANE, (gui) -> {
                            e.getPlayer().sendMessage("Hello, world!");
                        }, "Say hello", true, "", "Says hello"))
                        .build()
                        .show(e.getPlayer());
            } catch (Exception fe) {
                fe.printStackTrace();
            }
        }, 3);
    }


}

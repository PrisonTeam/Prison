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

package io.github.prison.spigot;

import io.github.prison.Prison;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Posts Prison's internal events.
 *
 * @author SirFaizdat
 */
public class SpigotListener implements Listener {

    private SpigotPrison spigotPrison;

    public SpigotListener(SpigotPrison spigotPrison) {
        this.spigotPrison = spigotPrison;
    }

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this.spigotPrison);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.PlayerJoinEvent(new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.PlayerQuitEvent(new SpigotPlayer(e.getPlayer())));
    }

}

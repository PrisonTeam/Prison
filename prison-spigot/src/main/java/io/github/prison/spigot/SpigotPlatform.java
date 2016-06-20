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

import io.github.prison.Platform;
import io.github.prison.internal.Player;
import io.github.prison.internal.World;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @author SirFaizdat
 */
public class SpigotPlatform implements Platform {

    private SpigotPrison plugin;

    public SpigotPlatform(SpigotPrison plugin) {
        this.plugin = plugin;
    }

    @Override
    public World getWorld(String name) {
        return null;
    }

    @Override
    public Player getPlayer(String name) {
        return null;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public List<Player> getOnlinePlayers() {
        return null;
    }

    @Override
    public String getPluginVersion() {
        return null;
    }

    @Override
    public File getPluginDirectory() {
        return null;
    }

}

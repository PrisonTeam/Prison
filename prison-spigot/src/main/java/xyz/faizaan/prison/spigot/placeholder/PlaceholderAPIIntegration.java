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

package xyz.faizaan.prison.spigot.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.Bukkit;
import xyz.faizaan.prison.integration.PlaceholderIntegration;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.spigot.game.SpigotPlayer;

import java.util.function.Function;

public class PlaceholderAPIIntegration implements PlaceholderIntegration {

    private boolean pluginInstalled;

    public PlaceholderAPIIntegration() {
        this.pluginInstalled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
        if (!hasIntegrated()) return;
        PlaceholderAPI.registerPlaceholderHook(placeholder, new PlaceholderHook() {
            @Override
            public String onPlaceholderRequest(org.bukkit.entity.Player player, String s) {
                return action.apply(new SpigotPlayer(player));
            }
        });
    }

    @Override
    public String getProviderName() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean hasIntegrated() {
        return pluginInstalled;
    }

}

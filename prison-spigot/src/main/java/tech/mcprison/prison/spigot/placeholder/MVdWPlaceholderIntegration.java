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

package tech.mcprison.prison.spigot.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import tech.mcprison.prison.integration.PlaceholderIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.util.function.Function;

public class MVdWPlaceholderIntegration implements PlaceholderIntegration {

    private boolean pluginInstalled;

    public MVdWPlaceholderIntegration() {
        pluginInstalled = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
    }

    @Override
    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
        if (!hasIntegrated()) return;
        PlaceholderAPI.registerPlaceholder(Bukkit.getPluginManager().getPlugin("Prison"), placeholder, e -> action.apply(new SpigotPlayer(e.getPlayer())));
    }

    @Override
    public String getProviderName() {
        return "MVdWPlaceholderAPI";
    }

    @Override
    public boolean hasIntegrated() {
        return pluginInstalled;
    }

}

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

package io.github.prison.ranks.plugins;

import io.github.prison.ranks.RanksModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

/**
 * @author SirFaizdat
 */
@Plugin(id = "prison-ranks", name = "PrisonRanks", version = "3.0.0-SNAPSHOT", description = "The ranks module for Prison.", dependencies = {@Dependency(id = "prison-sponge")})
public class RanksSponge {

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        new RanksModule(getVersion());
    }

    private String getVersion() {
        return Sponge.getPluginManager().getPlugin("prison-ranks").get().getVersion().get();
    }

}
